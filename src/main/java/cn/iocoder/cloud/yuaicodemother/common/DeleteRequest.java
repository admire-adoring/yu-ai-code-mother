package cn.iocoder.cloud.yuaicodemother.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author Liu Yang
 * @Date 2026/2/25 23:50
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
