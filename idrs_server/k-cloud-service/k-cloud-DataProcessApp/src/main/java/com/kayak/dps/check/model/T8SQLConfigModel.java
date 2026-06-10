package com.kayak.dps.check.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8SQLConfigModelService",table = "base_port_sql_info")
public class T8SQLConfigModel {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "exeid", sql = "exeid LIKE '%$U{exeid}%'" ,field = "exeid")
   private String exeid;
   @GraphQLField(kkhtml = "KFieldText", label = "sqlid", sql = "sqlid LIKE '%$U{sqlid}%'" ,field = "sqlid")
   private String sqlid;
   @GraphQLField(kkhtml = "KFieldText", label = "sql内容描述", sql = "`desc` LIKE '%$U{desc}%'" ,field = "desc")
   private String desc;
   @GraphQLField(kkhtml = "KFieldText", label = "sql语句", sql = "sqlstr LIKE '%$U{sqlstr}%'" ,field = "sqlstr")
   private String sqlstr;
    @GraphQLField
    private String exeOrder;
    @GraphQLField
    private String taskName;
    @GraphQLField
    private String taskId;
    @GraphQLField
    private String userid;
    @GraphQLField
    private String username;
    @GraphQLField
    private String version;
    @GraphQLField
    private String operationDate;
    @GraphQLField
    private String operationTime;

}