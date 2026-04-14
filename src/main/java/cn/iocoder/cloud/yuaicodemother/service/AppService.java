package cn.iocoder.cloud.yuaicodemother.service;

import cn.iocoder.cloud.yuaicodemother.model.dto.app.AppQueryRequest;
import cn.iocoder.cloud.yuaicodemother.model.entity.App;
import cn.iocoder.cloud.yuaicodemother.model.entity.User;
import cn.iocoder.cloud.yuaicodemother.model.vo.app.AppVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author L
 */
public interface AppService extends IService<App> {

    Flux<String> chatToGenCode(Long appId, String message, User loginUser);

    /**
     * 部署应用
     *
     * @param appId 应用ID
     * @param loginUser 创建应用的用户
     * @return 部署成功返回路径
     */
    String deployApp(Long appId, User loginUser);

    AppVO getAppVO(App app);

    /**
     * 获取应用封装类列表
     *
     * @param appList
     * @return
     */
    List<AppVO> getAppVOList(List<App> appList);

    /**
     * 构造应用查询条件
     *
     * @param appQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(AppQueryRequest appQueryRequest);

}
