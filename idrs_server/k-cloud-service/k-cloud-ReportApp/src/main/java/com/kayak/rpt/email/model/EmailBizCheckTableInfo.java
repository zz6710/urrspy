package com.kayak.rpt.email.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "emailBizCheckTableInfoService",table = "email_biz_check_table_info")
@Data
public class EmailBizCheckTableInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "业务主表", sql = "biz_report_table = $S{bizReportTable}" ,field = "biz_report_table")
   private String bizReportTable;
   @GraphQLField(kkhtml = "KFieldText", label = "业务关联的所有业务子表，以英文逗号拼接", sql = "biz_check_table_info = $S{bizCheckTableInfo}" ,field = "biz_check_table_info")
   private String bizCheckTableInfo;
   @GraphQLField(kkhtml = "KFieldText", label = "单次操作的业务子表", sql = "biz_report_sub_table = $S{bizReportSubTable}" ,field = "biz_report_sub_table")
   private String bizReportSubTable;
   @GraphQLField(kkhtml = "KFieldText", label = "报告业务日期，一般为当月最后一天", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间HHmmss", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期yyyyMMdd", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;

}