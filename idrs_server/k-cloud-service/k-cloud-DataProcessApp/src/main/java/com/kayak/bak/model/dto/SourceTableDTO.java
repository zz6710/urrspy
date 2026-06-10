package com.kayak.bak.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 表模型
 */
@Data
@Accessors(chain = true)
public class SourceTableDTO {

    /**
     * 表名
     */
    private String tableName;
}
