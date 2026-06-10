package com.kayak.dps.check.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ParamConfigModelService",table = "base_port_config_info")
public class T8ParamConfigModel {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "系统参数表ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "参数描述", sql = "config_describe like '%$U{configDescribe}%'" ,field = "config_describe")
   private String configDescribe;
   @GraphQLField(kkhtml = "KFieldText", label = "参数名称", sql = "config_name like '%$U{configName}%'" ,field = "config_name")
   private String configName;
   @GraphQLField(kkhtml = "KFieldText", label = "参数值", sql = "config_code = $S{configCode}" ,field = "config_code")
   private String configCode;
   @GraphQLField(kkhtml = "KFieldText", label = "参数类型", sql = "config_type like '%$U{configType}%'" ,field = "config_type")
   private String configType;
   @GraphQLField(kkhtml = "KFieldSelect", label = "状态", sql = "status = $S{status}" ,field = "status" , kkhtmlExt="{\"data-dict\":\"statusForInter\"}")
   private String status;
   @GraphQLField(kkhtml = "KFieldText", label = "真实参数值")
   private String realConfigCode;
}