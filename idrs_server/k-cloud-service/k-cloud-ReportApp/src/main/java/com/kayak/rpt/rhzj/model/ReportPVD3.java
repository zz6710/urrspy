package com.kayak.rpt.rhzj.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportPVD3Service",table = "app_rpt_pvd3")
public class ReportPVD3 {
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "日期", sql = "report_date like '$U{reportDate}%'" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "行内产品代码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产池代码", sql = "pbc_assetscode = $S{pbcAssetscode}" ,field = "pbc_assetscode")
   private String pbcAssetscode;
   @GraphQLField(kkhtml = "KFieldText", label = "股权种类", sql = "stock_type = $S{stockType}" ,field = "stock_type")
   private String stockType;
   @GraphQLField(kkhtml = "KFieldText", label = "信托产品发起机构编码", sql = "orgno = $S{orgno}" ,field = "orgno")
   private String orgno;
   @GraphQLField(kkhtml = "KFieldText", label = "币种代码", sql = "cny = $S{cny}" ,field = "cny")
   private String cny;
   @GraphQLField(kkhtml = "KFieldText", label = "产品金额", sql = "prod_amount = $S{prodAmount}" ,field = "prod_amount")
   private String prodAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "产品金额折人民币", sql = "prod_amount_rmb = $S{prodAmountRmb}" ,field = "prod_amount_rmb")
   private String prodAmountRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "信托产品代码", sql = "product_code = $S{productCode}" ,field = "product_code")
   private String productCode;

}