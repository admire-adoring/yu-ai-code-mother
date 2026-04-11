package cn.iocoder.cloud.yuaicodemother.controller;

import cn.iocoder.cloud.yuaicodemother.common.BaseResponse;
import cn.iocoder.cloud.yuaicodemother.common.ResultUtils;
import cn.iocoder.cloud.yuaicodemother.model.dto.UserLoginRequest;
import cn.iocoder.cloud.yuaicodemother.model.dto.UserRegisterRequest;
import cn.iocoder.cloud.yuaicodemother.model.entity.User;
import cn.iocoder.cloud.yuaicodemother.model.vo.LoginUserVO;
import cn.iocoder.cloud.yuaicodemother.service.UserService;
import com.mybatisflex.core.paginate.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 控制层。
 *
 * @author L
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param request 注册信息
     * @return 用户 id
     */
    @PostMapping("register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest request) {
        return ResultUtils.success(userService.userRegister(request));
    }

    /**
     * 用户登录
     *
     * @param loginRequest 登录信息
     * @param request      请求
     * @return 登录用户
     */
    @PostMapping("login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest loginRequest, HttpServletRequest request) {
        return ResultUtils.success(userService.userLogin(loginRequest, request));
    }

    /**
     * 获取当前登录用户
     *
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        return ResultUtils.success(userService.getLoginUserVO(userService.getLoginUser(request)));
    }

    /**
     * 退出登录
     *
     */
    @PostMapping("logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        return ResultUtils.success(userService.logout(request));
    }
    /**
     * 保存用户。
     *
     * @param user 用户
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 根据主键删除用户。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return userService.removeById(id);
    }

    /**
     * 根据主键更新用户。
     *
     * @param user 用户
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody User user) {
        return userService.updateById(user);
    }

    /**
     * 查询所有用户。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<User> list() {
        return userService.list();
    }

    /**
     * 根据主键获取用户。
     *
     * @param id 用户主键
     * @return 用户详情
     */
    @GetMapping("getInfo/{id}")
    public User getInfo(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 分页查询用户。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<User> page(Page<User> page) {
        return userService.page(page);
    }

}
