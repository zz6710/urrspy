package com.kayak.rpt.zz.audit.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Builder;
import lombok.Data;

/**
 * @author Ty
 * @since 2023-05-18 11:11:02
 */
@Data
@GraphQLModel(fetcher = "reportAuditResultService", table = "base_report_data_audit_results")
@Builder
public class ReportAuditResult {
    @GraphQLField(kkhtml = "KFieldText", label = "表名", sql = "table_id = $S{tableId}" ,field = "table_id")
    private String tableId;

    @GraphQLField(kkhtml = "KFieldText", label = "表名", sql = "table_name = $S{tableName}" ,field = "table_name")
    private String tableName;

    @GraphQLField(kkhtml = "KFieldText", label = "起始时间", sql = "star_date = $S{startDate}" ,field = "star_date")
    private String startDate;

    @GraphQLField(kkhtml = "KFieldText", label = "截止时间", sql = "end_date = $S{endDate}" ,field = "end_date")
    private String endDate;

    @GraphQLField(kkhtml = "KFieldText", label = "日期类型", sql = "date_type = $S{dateType}" ,field = "date_type")
    private String dateType;

    @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;

    @GraphQLField(kkhtml = "KFieldText", label = "复核日期", sql = "audit_date = $S{auditDate}" ,field = "audit_date")
    private String auditDate;

    @GraphQLField(kkhtml = "KFieldText", label = "状态", sql = "audit_status = $S{auditStatus}" ,field = "audit_status")
    private String auditStatus;

}
