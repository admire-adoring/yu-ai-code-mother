package cn.iocoder.cloud.yuaicodemother.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author Liu Yang
 * @Date 2026/4/13 00:34
 */
@Data
public class AppAdminUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用封面
     */
    private String cover;

    /**
     * 优先级
     */
    private Integer priority;

    private static final long serialVersionUID = 1L;
}
