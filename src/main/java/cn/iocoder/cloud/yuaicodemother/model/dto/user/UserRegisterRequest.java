package cn.iocoder.cloud.yuaicodemother.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author Liu Yang
 * @Date 2026/4/12 00:51
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
