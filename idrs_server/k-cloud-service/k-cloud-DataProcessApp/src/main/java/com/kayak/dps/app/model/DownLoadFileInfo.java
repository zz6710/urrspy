package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "downLoadFileService")
public class DownLoadFileInfo {
    @GraphQLField(label = "表名", sql = "TABLE_NAME = $S{tableName}", field = "TABLE_NAME")
    private String tableName;
    @GraphQLField(label = "表中文名", sql = "TABLE_COMMENT = $S{tableComment}", field = "TABLE_COMMENT")
    private String tableComment;
    @GraphQLField(label = "字段名", sql = "COLUMN_NAME = $S{columnName}", field = "COLUMN_NAME")
    private String columnName;
    @GraphQLField(label = "字段注释", sql = "COLUMN_COMMENT = $S{columnComment}", field = "COLUMN_COMMENT")
    private String columnComment;
    @GraphQLField(label = "值", sql = "VALUE = $S{value}", field = "VALUE")
    private String value;
    @GraphQLField(label = "符号", sql = "SYMBOL = $S{symbol}", field = "SYMBOL")
    private String symbol;
}
