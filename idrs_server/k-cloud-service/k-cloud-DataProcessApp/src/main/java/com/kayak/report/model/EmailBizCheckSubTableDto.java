package com.kayak.report.model;

import com.kayak.graphql.annotation.GraphQLField;
import lombok.Data;

@Data
public class EmailBizCheckSubTableDto {
    @GraphQLField(kkhtml = "KFieldText", label = "单次操作的业务子表", sql = "biz_report_sub_table = $S{bizReportSubTable}" ,field = "biz_report_sub_table")
    private String bizReportSubTable;
}
