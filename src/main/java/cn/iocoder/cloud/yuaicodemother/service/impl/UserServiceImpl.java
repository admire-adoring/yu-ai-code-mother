package cn.iocoder.cloud.yuaicodemother.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.cloud.yuaicodemother.exception.BusinessException;
import cn.iocoder.cloud.yuaicodemother.exception.ErrorCode;
import cn.iocoder.cloud.yuaicodemother.mapper.UserMapper;
import cn.iocoder.cloud.yuaicodemother.model.dto.user.UserLoginRequest;
import cn.iocoder.cloud.yuaicodemother.model.dto.user.UserQueryRequest;
import cn.iocoder.cloud.yuaicodemother.model.dto.user.UserRegisterRequest;
import cn.iocoder.cloud.yuaicodemother.model.entity.User;
import cn.iocoder.cloud.yuaicodemother.model.enums.UserRoleEnum;
import cn.iocoder.cloud.yuaicodemother.model.vo.user.LoginUserVO;
import cn.iocoder.cloud.yuaicodemother.model.vo.user.UserVO;
import cn.iocoder.cloud.yuaicodemother.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.cloud.yuaicodemother.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户 服务层实现。
 *
 * @author L
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户注册
     *
     * @param request 注册信息
     * @return 用户 id
     */
    @Override
    public Long userRegister (UserRegisterRequest request) {
        String userAccount = request.getUserAccount();
        String userPassword = request.getUserPassword();
        String checkPassword = request.getCheckPassword();

        // 校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        // 验重
        User user = userMapper.selectOneByQuery(
                new QueryWrapper()
                        .eq(User::getUserAccount, userAccount)
        );
        if (user != null) {
            throw new RuntimeException("账号已存在");
        }

        //  加密
        String encryptPassword = getEncryptPassword(userPassword);

        // 保存到数据库
        User newUser = new User();
        newUser.setUserAccount(userAccount);
        newUser.setUserPassword(encryptPassword);
        newUser.setUserName("admin");
        newUser.setUserRole(UserRoleEnum.USER.getValue());

        int insert = userMapper.insert(newUser);
        if (insert < 1) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return newUser.getId();
    }

    @Override
    public String getEncryptPassword (String userPassword) {
        // 盐值，混淆密码
        final String SALT = "liuyang";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

    /**
     * 获取脱敏后的已登录用户
     *
     * @param user 用户
     * @return 脱敏后的已登录用户
     */
    @Override
    public LoginUserVO getLoginUserVO (User  user) {
        if (user != null){
            return new LoginUserVO(
                    user.getId(),
                    user.getUserAccount(),
                    user.getUserName(),
                    user.getUserAvatar(),
                    user.getUserProfile(),
                    user.getUserRole(),
                    user.getCreateTime(),
                    user.getUpdateTime()
            );
        }
        return null;
    }

    /**
     * 用户登录
     *
     * @param loginRequest 登录信息
     * @param request      请求
     * @return 登录用户
     */
    @Override
    public LoginUserVO userLogin (UserLoginRequest loginRequest, HttpServletRequest request) {

        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();

        // 校验
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        // 加密
        String encryptPassword = getEncryptPassword(userPassword);

        //校验存在
        User user = userMapper.selectOneByQuery(QueryWrapper.create()
                .eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword));
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 设置用户为登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO( user);
    }

    /**
     * 获取登录用户
     *
     * @param request 请求
     * @return 登录用户
     */
    @Override
    public User getLoginUser (HttpServletRequest request) {
        // 判断是否登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
           throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return this.getById(currentUser.getId());
    }

    /**
     * 退出登录
     *
     * @param request 请求
     * @return 是否退出成功
     */
    @Override
    public Boolean logout (HttpServletRequest request) {
        // 判断是否登录
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取脱敏后的用户信息
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }


    /**
     * 获取脱敏后的用户信息（分页）
     *
     * @param userList 用户列表
     * @return
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream()
                .map(this::getUserVO)
                .collect(Collectors.toList());
    }

    /**
     * 根据查询条件构造数据查询参数
     *
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper getQueryWrapper (UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id) // where id = ${id}
                .eq("userRole", userRole) // and userRole = ${userRole}
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }


}
