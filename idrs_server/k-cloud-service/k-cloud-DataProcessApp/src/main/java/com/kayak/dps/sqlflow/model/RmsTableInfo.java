package com.kayak.dps.sqlflow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "rmsTableInfoService",table = "rms_table_info")
@Data
public class RmsTableInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "表id", sql = "table_info_id = $S{tableInfoId}" ,field = "table_info_id")
   private String tableInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "库名", sql = "database_name = $S{databaseName}" ,field = "database_name")
   private String databaseName;
   @GraphQLField(kkhtml = "KFieldText", label = "表名", sql = "table_name = $S{tableName}" ,field = "table_name")
   private String tableName;
   @GraphQLField(kkhtml = "KFieldText", label = "表注释", sql = "comment = $S{comment}" ,field = "comment")
   private String comment;
   @GraphQLField(kkhtml = "KFieldText", label = "表所属层级", sql = "owner = $S{owner}" ,field = "owner")
   private String owner;
   @GraphQLField(kkhtml = "KFieldText", label = "上游任务集", sql = "up_tasks = $S{upTasks}" ,field = "up_tasks")
   private String upTasks;
   @GraphQLField(kkhtml = "KFieldText", label = "下游任务集", sql = "down_tasks = $S{downTasks}" ,field = "down_tasks")
   private String downTasks;

   @GraphQLField
   private String exeid;
   @GraphQLField
   private String taskId;
   @GraphQLField
   private String taskModel;
   @GraphQLField
   private String taskName;
   @GraphQLField
   private String taskType;
   @GraphQLField
   private String date;

}