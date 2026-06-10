package com.kayak.bak.model.request;

import lombok.Data;

@Data
public class GetFieldInfoRequest {

    /**
     * 数据库名
     */
    private String dbName;

    /**
     * 表名
     */
    private String tableName;
}
