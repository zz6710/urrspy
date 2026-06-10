package com.kayak.pms.onlineEdit.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8OnlineWordTableColumnsService",table = "t8_online_word_table_columns")
public class T8OnlineWordTableColumns {
   @GraphQLField(key = true , label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "表名", sql = "table_name = $S{tableName}" ,field = "table_name")
   private String tableName;
   @GraphQLField(kkhtmlDefault = true,kkhtml = "KFieldText", label = "数据源字段", sql = "column_name like '%$U{columnName}%' " ,field = "column_name")
   private String columnName;
   @GraphQLField(kkhtmlDefault = true,kkhtml = "KFieldText", label = "列注释", sql = "column_comment like '%$U{columnComment}%' " ,field = "column_comment")
   private String columnComment;
   @GraphQLField
   private String defaultValue;
   @GraphQLField(kkhtml = "KFieldText", label = "是否可编辑", sql = "is_disabled = $S{isDisabled}" ,field = "is_disabled")
   private String isDisabled;
   @GraphQLField(kkhtml = "KFieldText", label = "数据状态", sql = "status = $S{status}" ,field = "status")
   private String status;
   @GraphQLField(kkhtml = "KFieldText", label = "数据字典", sql = "dict = $S{dict}" ,field = "dict")
   private String dict;
   @GraphQLField(kkhtml = "KFieldText", label = "数据类型", field = "data_type")
   private String dataType;
   @GraphQLField(kkhtml = "KFieldText", label = "文档映射字段", sql = "doc_column like '%$U{docColumn}%' " , field = "doc_column")
   private String docColumn;
   @GraphQLField(kkhtml = "KFieldText", label = "小数位数", field = "data_digits")
   private String dataDigits;
   @GraphQLField(kkhtml = "KFieldText",field = "empty_default_val")
   private String emptyDefaultVal;
   @GraphQLField
   private String sqlInfo;

   @GraphQLField(field = "crt_date")
   private String crtDate;

   @GraphQLField(field = "crt_time")
   private String crtTime;

   @GraphQLField(field = "upd_date")
   private String updDate;

   @GraphQLField( field = "upd_time")
   private String updTime;

   @GraphQLField( field = "crt_user")
   private String crtUser;

   @GraphQLField( field = "upd_user")
   private String updUser;

}
