package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "baseFiveTypeCmpInfService",table = "base_five_type_cmp_inf")
@Data
public class BaseFiveTypeCmpInf {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date like '$U{reportDate}%'" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "主体名称", sql = "cmp_nm like '%$U{cmpNm}%'" ,field = "cmp_nm")
   private String cmpNm;
   @GraphQLField(kkhtml = "KFieldText", label = "creditid", sql = "creditid = $S{creditid}" ,field = "creditid")
   private String creditid;
   @GraphQLField(kkhtml = "KFieldText", label = "主题标识", sql = "five_type = $S{fiveType}" ,field = "five_type")
   private String fiveType;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;

}