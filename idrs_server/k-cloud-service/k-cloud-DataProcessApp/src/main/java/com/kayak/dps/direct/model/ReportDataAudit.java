package com.kayak.dps.direct.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportDataAuditService",table = "base_report_data_audit")
public class ReportDataAudit {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "报送报表", sql = "table_id=$S{tableId}" ,field = "table_id")
    private String tableId;
    @GraphQLField(kkhtml = "KFieldText", label = "报表名称", sql = "table_name=$S{tableName}" ,field = "table_name")
    private String tableName;
    @GraphQLField(kkhtml = "KFieldText", label = "起始日期", sql = "begin_date=$S{beginDate}" ,field = "begin_date")
    private String beginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "结束日期", sql = "end_date=$S{endDate}" ,field = "end_date")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", label = "数据类型", sql = "date_type=$S{dateType}" ,field = "date_type")
    private String dateType;
    @GraphQLField(kkhtml = "KFieldText", label = "复核日期", sql = "audit_date=$S{auditDate}" ,field = "auditDate")
    private String auditDate;
    @GraphQLField(kkhtml = "KFieldText", label="复核状态", sql = "audit_status = $S{auditStatus}" ,field = "status")
    private String auditStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期")
    private String crtDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间")
    private String crtTm;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期")
    private String updDt;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间")
    private String updTm;
}