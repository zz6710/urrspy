package com.kayak.rpt.rhzj.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportPCDService",table = "app_rpt_pcd")
public class ReportPCD {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "日期", sql = "report_date like '$U{reportDate}%'" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "行内产品代码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "报送人行产品代码", sql = "peoplebank_submitcode like '%$U{peoplebankSubmitcode}'" ,field = "peoplebank_submitcode")
   private String peoplebankSubmitcode;
   @GraphQLField(kkhtml = "KFieldText", label = "地区代码", sql = "area_code = $S{areaCode}" ,field = "area_code")
   private String areaCode;
   @GraphQLField(kkhtml = "KFieldText", label = "客户类型", sql = "cust_type = $S{custType}" ,field = "cust_type")
   private String custType;
   @GraphQLField(kkhtml = "KFieldText", label = "币种代码", sql = "cny = $S{cny}" ,field = "cny")
   private String cny;
   @GraphQLField(kkhtml = "KFieldText", label = "当期申购金额", sql = "current_buy_amount = $S{currentBuyAmount}" ,field = "current_buy_amount")
   private String currentBuyAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "当期申购金额折人民币", sql = "current_buy_amount_rmb = $S{currentBuyAmountRmb}" ,field = "current_buy_amount_rmb")
   private String currentBuyAmountRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "当期申购份额", sql = "current_buy_vol = $S{currentBuyVol}" ,field = "current_buy_vol")
   private String currentBuyVol;
   @GraphQLField(kkhtml = "KFieldText", label = "当期兑付/赎回金额", sql = "current_redemption_amount = $S{currentRedemptionAmount}" ,field = "current_redemption_amount")
   private String currentRedemptionAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "当期兑付/赎回金额折人民币", sql = "current_redemption_amountrmb = $S{currentRedemptionAmountrmb}" ,field = "current_redemption_amountrmb")
   private String currentRedemptionAmountrmb;
   @GraphQLField(kkhtml = "KFieldText", label = "当期兑付/赎回份额", sql = "current_redemption_vol = $S{currentRedemptionVol}" ,field = "current_redemption_vol")
   private String currentRedemptionVol;
   @GraphQLField(kkhtml = "KFieldText", label = "期末产品金额", sql = "termina_prod_amount = $S{terminaProdAmount}" ,field = "termina_prod_amount")
   private String terminaProdAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "期末产品金额折人民币", sql = "termina_prod_amount_rmb = $S{terminaProdAmountRmb}" ,field = "termina_prod_amount_rmb")
   private String terminaProdAmountRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "期末产品份额", sql = "termina_prod_vol = $S{terminaProdVol}" ,field = "termina_prod_vol")
   private String terminaProdVol;
   @GraphQLField(kkhtml = "KFieldText", label = "净值型产品期末净值", sql = "termina_prod_nav = $S{terminaProdNav}" ,field = "termina_prod_nav")
   private String terminaProdNav;
   @GraphQLField(kkhtml = "KFieldText", label = "净值型产品期末净值折人民币", sql = "termina_prod_nav_rmb = $S{terminaProdNavRmb}" ,field = "termina_prod_nav_rmb")
   private String terminaProdNavRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "净值型产品期末累计净值", sql = "termina_prod_nav_add = $S{terminaProdNavAdd}" ,field = "termina_prod_nav_add")
   private String terminaProdNavAdd;
   @GraphQLField(kkhtml = "KFieldText", label = "净值型产品期末累计净值折人民币", sql = "termina_prod_nav_add_rmb = $S{terminaProdNavAddRmb}" ,field = "termina_prod_nav_add_rmb")
   private String terminaProdNavAddRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "开放式非净值型产品预期最高收益率", sql = "prod_max_rate = $S{prodMaxRate}" ,field = "prod_max_rate")
   private String prodMaxRate;
   @GraphQLField(kkhtml = "KFieldText", label = "开放式非净值型产品预期最低收益率", sql = "prod_min_rate = $S{prodMinRate}" ,field = "prod_min_rate")
   private String prodMinRate;
}