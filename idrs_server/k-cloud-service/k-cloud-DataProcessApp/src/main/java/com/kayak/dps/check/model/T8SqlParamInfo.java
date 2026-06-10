package com.kayak.dps.check.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "t8SqlParamInfoService",table = "base_port_sql_param_info")
@Data
public class T8SqlParamInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "参数名", sql = "code = $S{code}" ,field = "code")
   private String code;
   @GraphQLField(kkhtml = "KFieldText", label = "取值SQL", sql = "sqlstr = $S{sqlstr}" ,field = "sqlstr")
   private String sqlstr;
   @GraphQLField(kkhtml = "KFieldText", label = "数据类型：1列表 2单值", sql = "data_type = $S{dataType}" ,field = "data_type")
   private String dataType;
   @GraphQLField(kkhtml = "KFieldText", label = "参数说明", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "状态：0未生效 1已生效", sql = "status = $S{status}" ,field = "status")
   private String status;

}