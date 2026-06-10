package com.kayak.report.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "reportConvertConfigService",table = "base_rpt_convert_config")
@Data
public class ReportConvertConfig {
    @GraphQLField(kkhtml = "KFieldText", label = "报表转换ID", field = "report_id")
    private String reportId;

    @GraphQLField(kkhtml = "KFieldText", label = "报表名称", field = "target_column")
    private String targetColumn;

    @GraphQLField(kkhtml = "KFieldText", label = "操作员编号", field = "source_column")
    private String sourceColumn;

    @GraphQLField(kkhtml = "KFieldText", label = "操作员名称", field = "target_order")
    private int targetOrder;
}
