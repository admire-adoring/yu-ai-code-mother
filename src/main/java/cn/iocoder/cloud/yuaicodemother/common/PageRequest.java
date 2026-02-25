package cn.iocoder.cloud.yuaicodemother.common;

import lombok.Data;

/**
 * @Description
 * @Author Liu Yang
 * @Date 2026/2/25 23:50
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int pageNum = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}
