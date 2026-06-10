package com.kayak.rpt.rhzj.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportPVDService",table = "app_rpt_pvd")
public class ReportPVD {
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "行内产品代码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产池代码", sql = "pbc_assetscode = $S{pbcAssetscode}" ,field = "pbc_assetscode")
   private String pbcAssetscode;
   @GraphQLField(kkhtml = "KFieldText", label = "数据种类", sql = "data_type = $S{dataType}" ,field = "data_type")
   private String dataType;
   @GraphQLField(kkhtml = "KFieldText", label = "币种代码", sql = "cny = $S{cny}" ,field = "cny")
   private String cny;
   @GraphQLField(kkhtml = "KFieldText", label = "期末余额", sql = "end_amount = $S{endAmount}" ,field = "end_amount")
   private String endAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "期末余额折人民币", sql = "end_amount_rmb = $S{endAmountRmb}" ,field = "end_amount_rmb")
   private String endAmountRmb;

    @GraphQLField( sql = "isEqual = $S{isEqual}")
    private String isEqual;



}