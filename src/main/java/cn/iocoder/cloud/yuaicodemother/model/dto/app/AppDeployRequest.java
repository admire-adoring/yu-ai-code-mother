package cn.iocoder.cloud.yuaicodemother.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 应用部署请求类
 * @Author Liu Yang
 * @Date 2026/4/14 22:07
 */
@Data
public class AppDeployRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 应用ID
     */
    private Long appId;
}
