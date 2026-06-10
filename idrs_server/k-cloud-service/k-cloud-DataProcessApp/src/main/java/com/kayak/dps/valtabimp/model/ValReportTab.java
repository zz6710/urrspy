package com.kayak.dps.valtabimp.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "valReportTabService",table = "base_fa_reporttab")
public class ValReportTab {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "估值表名称", sql = "reporttab_name = $S{reporttabName}" ,field = "reporttab_name")
   private String reporttabName;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "note = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
   private String inputuser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
    @GraphQLField
    private String t8ValReporttabId;
}