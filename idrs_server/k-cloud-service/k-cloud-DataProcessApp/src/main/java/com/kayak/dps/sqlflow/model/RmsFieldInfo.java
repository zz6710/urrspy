package com.kayak.dps.sqlflow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "rmsFieldInfoService",table = "rms_table_field")
@Data
public class RmsFieldInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "字段id", sql = "table_field_id = $S{tableFieldId}" ,field = "table_field_id")
   private String tableFieldId;
   @GraphQLField(kkhtml = "KFieldText", label = "表id", sql = "table_info_id = $S{tableInfoId}" ,field = "table_info_id")
   private String tableInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "字段名", sql = "field_name = $S{fieldName}" ,field = "field_name")
   private String fieldName;
   @GraphQLField(kkhtml = "KFieldText", label = "字段数据类型", sql = "field_data_type = $S{fieldDataType}" ,field = "field_data_type")
   private String fieldDataType;
   @GraphQLField(kkhtml = "KFieldText", label = "字段注释", sql = "field_comment = $S{fieldComment}" ,field = "field_comment")
   private String fieldComment;
   @GraphQLField(kkhtml = "KFieldText", label = "字段排序", sql = "field_index = $S{fieldIndex}" ,field = "field_index")
   private String fieldIndex;
   @GraphQLField
   private String databaseName;
   @GraphQLField
   private String tableName;
   @GraphQLField
   private String comment;
   @GraphQLField
   private String owner;

}