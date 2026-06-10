package com.kayak.report.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "reportDataLockRecordService",table = "report_data_lock_record")
@Data
public class ReportDataLockRecord {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "报送报表", sql = "report_table = $S{reportTable}" ,field = "report_table")
   private String reportTable;
   @GraphQLField(kkhtml = "KFieldText", label = "报表名称", sql = "table_name = $S{tableName}" ,field = "table_name")
   private String tableName;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "锁表状态", sql = "lock_status = $S{lockStatus}" ,field = "lock_status")
   private String lockStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "操作用户", sql = "opt_user = $S{optUser}" ,field = "opt_user")
   private String optUser;
   @GraphQLField(kkhtml = "KFieldText", label = "操作日期", sql = "opt_date = $S{optDate}" ,field = "opt_date")
   private String optDate;
   @GraphQLField(kkhtml = "KFieldText", label = "操作时间", sql = "opt_time = $S{optTime}" ,field = "opt_time")
   private String optTime;

}