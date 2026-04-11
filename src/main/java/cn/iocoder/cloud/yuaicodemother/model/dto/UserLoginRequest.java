package cn.iocoder.cloud.yuaicodemother.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 用户登录请求
 * @Author Liu Yang
 * @Date 2026/4/12 02:26
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
