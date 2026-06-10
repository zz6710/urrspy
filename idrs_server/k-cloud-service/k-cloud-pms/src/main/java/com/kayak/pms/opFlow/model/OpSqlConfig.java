package com.kayak.pms.opFlow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "opSqlConfigService", table = "op_sql_config")
public class OpSqlConfig {

    @GraphQLField(label = "表单ID", sql = "form_id = $S{formId}", field = "form_id")
    private String formId;

    @GraphQLField(label = "表单操作类型", field = "action_type")
    private String actionType;

    @GraphQLField(label = "SQLid", sql = "sql_id = $S{sqlId}", field = "sql_id")
    private String sqlId;

    @GraphQLField(label = "SQL名称", sql = "sql_name = $S{sqlName}", field = "sql_name")
    private String sqlName;

    @GraphQLField(label = "SQL语句", field = "sql_statement")
    private String sqlStatement;

    @GraphQLField(label = "数据源", field = "datasource")
    private String datasource;

    @GraphQLField(label = "数据库类型", field = "db_type")
    private String dbType;

    @GraphQLField(label = "是否打印日志", field = "log_print")
    private String logPrint;

    @GraphQLField(label = "排序", field = "order_no")
    private String orderNo;

    @GraphQLField
    private List<OpSqlConfig> list;

    @GraphQLField
    private List<OpSqlCheckConfig> checkData;
}
