package com.kayak.rpt.validate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportDataValidateResService",table = "base_data_validation")
public class ReportValidationModel {
    @GraphQLField(key = true , label = "id" ,field = "id")
    private String id;
    @GraphQLField(label = "主题域" ,field = "topic")
    private String topic;
    @GraphQLField(label = "校验类型" ,field = "validate_type")
    private String validateType;
    @GraphQLField(label = "校验结果" ,field = "validate_result")
    private String validateResult;
    @GraphQLField(label = "校验异常原因" ,field = "reason")
    private String reason;
    @GraphQLField(label = "校验表" ,field = "validate_table")
    private String validateTable;
    @GraphQLField(label = "校验字段代码" ,field = "column_code")
    private String columnCode;
    @GraphQLField(label = "报表行名称" ,field = "validate_row")
    private String validateRow;
    @GraphQLField(label = "报表列名称" ,field = "validate_column")
    private String validateColumn;
    @GraphQLField(label = "创建日期" ,field = "create_date")
    private String createDate;
    @GraphQLField(label = "创建时间" ,field = "create_time")
    private String createTime;
    @GraphQLField(label = "报表类型" ,field = "report_type")
    private String reportType;
    @GraphQLField(label = "报表名称" ,field = "report_table")
    private String reportTable;
    @GraphQLField(label = "指标代码" ,field = "index_code")
    private String indexCode;
    @GraphQLField(label = "指标名称" ,field = "index_name")
    private String indexName;
    @GraphQLField(label = "校验数据所在表id" ,field = "data_id")
    private String dataId;
    @GraphQLField(label = "数据日期" ,field = "deal_date")
    private String dealDate;
    @GraphQLField(label = "报送日期" ,field = "report_date")
    private String reportDate;
    @GraphQLField(label = "校验指标详述" ,field = "index_detail")
    private String indexDetail;
}
