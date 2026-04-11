package cn.iocoder.cloud.yuaicodemother.service;

import cn.iocoder.cloud.yuaicodemother.model.dto.UserLoginRequest;
import cn.iocoder.cloud.yuaicodemother.model.dto.UserQueryRequest;
import cn.iocoder.cloud.yuaicodemother.model.dto.UserRegisterRequest;
import cn.iocoder.cloud.yuaicodemother.model.vo.LoginUserVO;
import cn.iocoder.cloud.yuaicodemother.model.vo.UserVO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import cn.iocoder.cloud.yuaicodemother.model.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author L
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param request 注册信息
     * @return 用户 id
     */
    Long userRegister (UserRegisterRequest request);

    /**
     * 获取加密后的密码
     *
     * @param userPassword 密码
     * @return 加密后的密码
     */
    String getEncryptPassword (String userPassword);

    /**
     * 获取脱敏后的已登录用户
     *
     * @param user 用户 id
     * @return 脱敏后的已登录用户
     */
    LoginUserVO getLoginUserVO (User  user);

    /**
     * 用户登录
     *
     * @param loginRequest 登录信息
     * @param request 请求
     * @return 登录用户
     */
    LoginUserVO userLogin (UserLoginRequest loginRequest, HttpServletRequest request);

    /**
     * 获取登录用户
     *
     * @param request 请求
     * @return 登录用户
     */
    User getLoginUser(HttpServletRequest  request);

    /**
     * 退出登录
     *
     * @param request 请求
     * @return 是否退出成功
     */
    Boolean logout (HttpServletRequest request);

    /**
     * 获取脱敏后的用户信息
     *
     * @param user 用户信息
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏后的用户信息（分页）
     *
     * @param userList 用户列表
     * @return
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 根据查询条件构造数据查询参数
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);
}
