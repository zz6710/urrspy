package com.kayak.bak.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SourceConfigDTO {

    /**
     * 数据库名称
     */
    private String dbName;
}
