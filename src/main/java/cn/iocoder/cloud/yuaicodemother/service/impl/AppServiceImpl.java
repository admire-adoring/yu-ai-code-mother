package cn.iocoder.cloud.yuaicodemother.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.cloud.yuaicodemother.constant.AppConstant;
import cn.iocoder.cloud.yuaicodemother.core.facade.AiCodeGeneratorFacade;
import cn.iocoder.cloud.yuaicodemother.exception.BusinessException;
import cn.iocoder.cloud.yuaicodemother.exception.ErrorCode;
import cn.iocoder.cloud.yuaicodemother.exception.ThrowUtils;
import cn.iocoder.cloud.yuaicodemother.mapper.AppMapper;
import cn.iocoder.cloud.yuaicodemother.model.dto.app.AppQueryRequest;
import cn.iocoder.cloud.yuaicodemother.model.entity.App;
import cn.iocoder.cloud.yuaicodemother.model.entity.User;
import cn.iocoder.cloud.yuaicodemother.model.enums.ChatHistoryMessageTypeEnum;
import cn.iocoder.cloud.yuaicodemother.model.enums.CodeGenTypeEnum;
import cn.iocoder.cloud.yuaicodemother.model.vo.app.AppVO;
import cn.iocoder.cloud.yuaicodemother.model.vo.user.UserVO;
import cn.iocoder.cloud.yuaicodemother.service.AppService;
import cn.iocoder.cloud.yuaicodemother.service.ChatHistoryService;
import cn.iocoder.cloud.yuaicodemother.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author L
 */
@Service
@Slf4j
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private UserService userService;

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Resource
    private ChatHistoryService chatHistoryService;

    @Override
    public Flux<String> chatToGenCode (Long appId, String message, User loginUser) {
        // 1. 参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "用户消息不能为空");
        // 2. 查询应用信息
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        // 3. 验证用户是否有权限访问该应用，仅本人可以生成代码
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限访问该应用");
        }
        // 4. 获取应用的代码生成类型
        String codeGenTypeStr = app.getCodeGenType();
        CodeGenTypeEnum codeGenTypeEnum = CodeGenTypeEnum.getEnumByValue(codeGenTypeStr);
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "不支持的代码生成类型");
        }
        // 5.校验通过，添加用户信息
        chatHistoryService.addChatMessage(appId, message, ChatHistoryMessageTypeEnum.USER.getValue(), loginUser.getId());
        // 6. 调用 AI 生成代码
        Flux<String> contentFlux = aiCodeGeneratorFacade.generateAndSaveCodeStream(message, codeGenTypeEnum, appId);
        // 7. 收集AI响应信息内容，在完成后保存到对话历史
        StringBuilder aiResponseContent = new StringBuilder();
        return contentFlux.map(chunk -> {
                    // 收集AI响应内容
                    aiResponseContent.append(chunk);
                    return chunk;
                })
                .doOnComplete(() -> {
                    // 响应完成后，添加信息
                    chatHistoryService.addChatMessage(appId, aiResponseContent.toString(), ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                }).doOnError(throwable -> {
                    // 收集错误信息
                    chatHistoryService.addChatMessage(appId, throwable.getMessage(), ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                });
    }

    /**
     * 部署应用
     *
     * @param appId     应用ID
     * @param loginUser 创建应用的用户
     * @return 部署成功返回路径
     */
    @Override
    public String deployApp (Long appId, User loginUser) {
        // 一、参数校验
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "应用 ID 不能为空");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR, "未登录");

        // 二、查询应用信息
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "应用不存在");

        // 三、验证用户是否有权限访问该应用，仅本人可以部署应用
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限访问该应用");
        }

        // 四、部署应用
        // 4.1:检查是否存在 deployKey
        String deployKey = app.getDeployKey();
        if (StrUtil.isBlank(deployKey)) {
            deployKey = RandomUtil.randomNumbers(6);
        } else {
            deployKey = deployKey;
        }

        // 4.2:获取代码生成类型，构建元目录路径
        String codeGenType = app.getCodeGenType();
        String sourceDirName = codeGenType + "_" + appId;
        String sourceDirPath = AppConstant.CODE_OUTPUT_ROOT_DIR + File.separator + sourceDirName;

        // 4.3:检查目录应用是否存在
        File sourceDir = new File(sourceDirPath);
        if (!sourceDir.exists()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用代码不存在，请先生成代码");
        }

        // 4.4:复制到部署目录
        String deployDirPath = AppConstant.CODE_DEPLOY_ROOT_DIR + File.separator + deployKey;
        try {
            FileUtil.copyContent(sourceDir, new File(deployDirPath), true);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "应用部署失败");
        }

        // 五、更新应用信息
        App updateApp = new App();
        updateApp.setId(appId);
        updateApp.setDeployKey(deployKey);
        updateApp.setDeployedTime(app.getUpdateTime());

        boolean updateResult = this.updateById(updateApp);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新应用部署信息失败");
        }

        // 六、返回可以访问的 URL
        return Paths.get(AppConstant.CODE_DEPLOY_HOST, deployKey)
                       .toString()
                       .replace("\\", "/") + "/";
    }


    @Override
    public AppVO getAppVO (App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        // 关联查询用户信息
        Long userId = app.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUser(userVO);
        }
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList (List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return new ArrayList<>();
        }
        // 批量获取用户信息，避免 N+1 查询问题
        Set<Long> userIds = appList.stream()
                .map(App::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, userService::getUserVO));
        return appList.stream().map(app -> {
            AppVO appVO = getAppVO(app);
            UserVO userVO = userVOMap.get(app.getUserId());
            appVO.setUser(userVO);
            return appVO;
        }).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper getQueryWrapper (AppQueryRequest appQueryRequest) {
        if (appQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = appQueryRequest.getId();
        String appName = appQueryRequest.getAppName();
        String cover = appQueryRequest.getCover();
        String initPrompt = appQueryRequest.getInitPrompt();
        String codeGenType = appQueryRequest.getCodeGenType();
        String deployKey = appQueryRequest.getDeployKey();
        Integer priority = appQueryRequest.getPriority();
        Long userId = appQueryRequest.getUserId();
        String sortField = appQueryRequest.getSortField();
        String sortOrder = appQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id)
                .like("appName", appName)
                .like("cover", cover)
                .like("initPrompt", initPrompt)
                .eq("codeGenType", codeGenType)
                .eq("deployKey", deployKey)
                .eq("priority", priority)
                .eq("userId", userId)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }

    @Override
    public boolean removeById(@NonNull Serializable id){
        Long appId = Long.parseLong(id.toString());

        // 先删除关联的对话历史
        try {
            chatHistoryService.deleteByAppId(appId);
        } catch (Exception e) {
          // 只记录日志，不阻止删除应用，确保核心业务稳定
            log.error("删除应用关联的对话历史失败，appId: {}", appId, e);
        }

        // 删除应用
        return super.removeById(appId);
    }
}
