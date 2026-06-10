package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "baseCfetsBondInfService",table = "base_cfets_bond_inf")
@Data
public class BaseCfetsBondInf {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date like '%$U{reportDate}%'" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "指数名称", sql = "index_nm like '%$U{indexNm}%'" ,field = "index_nm")
   private String indexNm;
   @GraphQLField(kkhtml = "KFieldText", label = "样本券代码", sql = "bond_code like '%$U{bondCode}%'" ,field = "bond_code")
   private String bondCode;
   @GraphQLField(kkhtml = "KFieldText", label = "样本券名称", sql = "bond_name like '%$U{bondName}%'" ,field = "bond_name")
   private String bondName;
   @GraphQLField(kkhtml = "KFieldText", label = "样本券权重(%)", sql = "index_weight = $S{indexWeight}" ,field = "index_weight")
   private String indexWeight;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;

}