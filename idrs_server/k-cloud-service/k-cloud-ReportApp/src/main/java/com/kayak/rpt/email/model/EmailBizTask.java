package com.kayak.rpt.email.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "emailBizTaskService",table = "email_biz_task")
@Data
public class EmailBizTask {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   //关联邮件模板 业务类型type
   @GraphQLField(kkhtml = "KFieldText", label = "业务类型", sql = "biz_type = $S{bizType}" ,field = "biz_type")
   private String bizType;
   @GraphQLField(kkhtml = "KFieldText", label = "业务名称", sql = "biz_name = $S{bizName}" ,field = "biz_name")
   private String bizDate;
   @GraphQLField(kkhtml = "KFieldText", label = "业务日期", sql = "biz_date = $S{bizDate}" ,field = "biz_date")
   private String bizName;
   @GraphQLField(kkhtml = "KFieldText", label = "业务表", sql = "biz_table = $S{bizTable}" ,field = "biz_table")
   private String bizTable;
   @GraphQLField(kkhtml = "KFieldText", label = "业务表取数方法", sql = "biz_table_method = $S{bizTableMethod}" ,field = "biz_table_method")
   private String bizTableMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "业务状态", sql = "biz_status = $S{bizStatus}" ,field = "biz_status")
   private String bizStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "远程文件路径", sql = "remote_path = $S{remotePath}" ,field = "remote_path")
   private String remotePath;
   @GraphQLField(kkhtml = "KFieldText", label = "是否立即执行任务 0否 1是", sql = "task_flag = $S{taskFlag}" ,field = "task_flag")
   private String taskFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "记录启用状态，0-未启用，1-已启用", sql = "status = $S{status}" ,field = "status")
   private String status;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间HHmmss", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期yyyyMMdd", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;

}