package com.kayak.report.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "reportDataLockConfigService",table = "report_data_lock_config")
@Data
public class ReportDataLockConfig {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "报表大类", sql = "report_category = $S{reportCategory}" ,field = "report_category")
   private String reportCategory;
   @GraphQLField(kkhtml = "KFieldText", label = "报送报表", sql = "report_table = $S{reportTable}" ,field = "report_table")
   private String reportTable;
   @GraphQLField(kkhtml = "KFieldText", label = "报表名称", sql = "table_name = $S{tableName}" ,field = "table_name")
   private String tableName;
   @GraphQLField(kkhtml = "KFieldText", label = "任务ID", sql = "task_id = $S{taskId}" ,field = "task_id")
   private String taskId;
   @GraphQLField(kkhtml = "KFieldText", label = "任务名称", sql = "task_name = $S{taskName}" ,field = "task_name")
   private String taskName;
   @GraphQLField(kkhtml = "KFieldText", label = "最近数据日期", sql = "latest_date = $S{latestDate}" ,field = "latest_date")
   private String latestDate;
   @GraphQLField(kkhtml = "KFieldText", label = "最近数据日期锁表状态", sql = "is_lst_lock = $S{isLstLock}" ,field = "is_lst_lock")
   private String isLstLock;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "依赖上级任务ID", sql = "upper_grade = $S{upperGrade}" ,field = "upper_grade")
   private String upperGrade;

}