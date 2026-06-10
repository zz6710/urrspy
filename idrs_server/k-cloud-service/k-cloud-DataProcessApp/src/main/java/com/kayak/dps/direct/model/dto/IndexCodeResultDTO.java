package com.kayak.dps.direct.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 指标校验结果传输模型
 */
@Data
@Accessors(chain = true)
public class IndexCodeResultDTO {

    /**
     * 指标编码
     */
    private String indexCode;

    /**
     * 最终校验公式
     */
    private String expressVal;

    /**
     * 最终校验结果
     */
    private Boolean retVal;

    /**
     * 二维报表日志记录值
     */
    private List<String> c2RowLogList;


    public static IndexCodeResultDTO initDTO() {
        return new IndexCodeResultDTO();
    }
}
