package com.kayak.pms.opFlow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "opSqlCheckConfigService", table = "op_sql_check_config")
public class OpSqlCheckConfig {
    @GraphQLField(label = "SQLid", sql = "sql_id = $S{sqlId}", field = "sql_id")
    private String sqlId;

    @GraphQLField(label = "校验id", field = "check_id")
    private String checkId;

    @GraphQLField(label = "校验名称", field = "check_name")
    private String checkName;

    @GraphQLField(label = "校验类型", field = "check_type")
    private String checkType;

    @GraphQLField(label = "校验字段", field = "check_field")
    private String checkField;

    @GraphQLField(label = "校验sql", field = "check_sql")
    private String checkSql;

    @GraphQLField(label = "校验目标值", field = "check_target")
    private String checkTarget;

    @GraphQLField(label = "校验提示", field = "check_msg")
    private String checkMsg;

    @GraphQLField(label = "排序", field = "order_no")
    private String orderNo;

    @GraphQLField(label = "校验条件", field = "check_sign")
    private String checkSign;
}
