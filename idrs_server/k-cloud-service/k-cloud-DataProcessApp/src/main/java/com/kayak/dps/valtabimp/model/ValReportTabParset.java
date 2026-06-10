package com.kayak.dps.valtabimp.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "valReportTabParsetService",table = "base_fa_reporttab_parset")
public class ValReportTabParset {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "估值表id", sql = "t8_val_reporttab_id = $S{t8ValReporttabId}" ,field = "t8_val_reporttab_id")
   private String t8ValReporttabId;
   @GraphQLField(kkhtml = "KFieldText", label = "参数类型", sql = "param_type = $S{paramType}" ,field = "param_type")
   private String paramType;
   @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "order_num = $S{orderNum}" ,field = "order_num")
   private String orderNum;
   @GraphQLField(kkhtml = "KFieldText", label = "参数代码", sql = "param_code = $S{paramCode}" ,field = "param_code")
   private String paramCode;
   @GraphQLField(kkhtml = "KFieldText", label = "参数名称", sql = "param_name = $S{paramName}" ,field = "param_name")
   private String paramName;
   @GraphQLField(kkhtml = "KFieldText", label = "参数数据类型", sql = "param_data_type = $S{paramDataType}" ,field = "param_data_type")
   private String paramDataType;
   @GraphQLField(kkhtml = "KFieldText", label = "参数值", sql = "param_value = $S{paramValue}" ,field = "param_value")
   private String paramValue;
   @GraphQLField(kkhtml = "KFieldText", label = "参数条件", sql = "param_condition = $S{paramCondition}" ,field = "param_condition")
   private String paramCondition;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "note = $S{note}" ,field = "note")
   private String note;
   @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
   private String inputuser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;

    @GraphQLField
    private String reporttabName;
}