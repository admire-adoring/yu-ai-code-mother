package cn.iocoder.cloud.yuaicodemother.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author Liu Yang
 * @Date 2026/4/12 03:08
 */
@Data
public class AppUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    private static final long serialVersionUID = 1L;
}
