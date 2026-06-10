package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "baseReportReloadLogService",table = "base_report_reload_log")
@Data
public class BaseReportReloadLog {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "菜单id", sql = "menu_id = $S{menuId}" ,field = "menu_id")
   private String menuId;
   @GraphQLField(kkhtml = "KFieldText", label = "报表日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "开始日期", sql = "start_date = $S{startDate}" ,field = "start_date")
   private String startDate;
   @GraphQLField(kkhtml = "KFieldText", label = "开始时间", sql = "start_time = $S{startTime}" ,field = "start_time")
   private String startTime;
   @GraphQLField(kkhtml = "KFieldText", label = "结束日期", sql = "end_date = $S{endDate}" ,field = "end_date")
   private String endDate;
   @GraphQLField(kkhtml = "KFieldText", label = "结束时间", sql = "end_time = $S{endTime}" ,field = "end_time")
   private String endTime;
   @GraphQLField(kkhtml = "KFieldText", label = "生成报表状态", sql = "result_status = $S{resultStatus}" ,field = "result_status")
   private String resultStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "生成报表信息", sql = "result_info = $S{resultInfo}" ,field = "result_info")
   private String resultInfo;
   @GraphQLField(kkhtml = "KFieldText", label = "操作人姓名", sql = "user_name = $S{userName}" ,field = "user_name")
   private String userName;

}