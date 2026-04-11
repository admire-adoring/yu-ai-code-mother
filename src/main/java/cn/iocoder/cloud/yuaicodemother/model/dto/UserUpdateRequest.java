package cn.iocoder.cloud.yuaicodemother.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author Liu Yang
 * @Date 2026/4/12 03:07
 */
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
