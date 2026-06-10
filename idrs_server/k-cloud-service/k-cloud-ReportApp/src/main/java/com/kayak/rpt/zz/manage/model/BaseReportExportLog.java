package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "baseReportExportLogService",table = "base_report_export_log")
@Data
public class BaseReportExportLog {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "用户id", sql = "userid = $S{userid}" ,field = "userid")
   private String userid;
   @GraphQLField(kkhtml = "KFieldText", label = "报表id", sql = "report_id = $S{reportId}" ,field = "report_id")
   private String reportId;
   @GraphQLField(kkhtml = "KFieldText", label = "申请时间", sql = "apply_time = $S{applyTime}" ,field = "apply_time")
   private String applyTime;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "data_time = $S{dataTime}" ,field = "data_time")
   private String dataTime;
   @GraphQLField(kkhtml = "KFieldText", label = "状态（待审批、审批通过、审批拒绝）", sql = "data_status = $S{dataStatus}" ,field = "data_status")
   private String dataStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "流程id", sql = "process_instance_id = $S{processInstanceId}" ,field = "process_instance_id")
   private String processInstanceId;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "create_by = $S{createBy}" ,field = "create_by")
   private String createBy;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "update_by = $S{updateBy}" ,field = "update_by")
   private String updateBy;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "update_time = $S{updateTime}" ,field = "update_time")
   private String updateTime;
   @GraphQLField(kkhtml = "KFieldText", label = "报表名称", sql = "report_name = $S{reportName}" ,field = "report_name")
   private String reportName;
   @GraphQLField(kkhtml = "KFieldText", label = "文件路径", sql = "file_path = $S{filePath}" ,field = "file_path")
   private String filePath;
   @GraphQLField(kkhtml = "KFieldText", label = "文件状态", sql = "file_status = $S{fileStatus}" ,field = "file_status")
   private String fileStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "远程路径", sql = "remote_path = $S{remotePath}" ,field = "remote_path")
   private String remotePath;
   @GraphQLField
   private String username;
   @GraphQLField
   private String reportStartDate;
   @GraphQLField
   private String reportEndDate;

}