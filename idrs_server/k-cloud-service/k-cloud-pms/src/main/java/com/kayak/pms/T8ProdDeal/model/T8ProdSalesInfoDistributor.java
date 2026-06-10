package com.kayak.pms.T8ProdDeal.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ProdSalesInfoDistributorService",table = "ods_amng_prod_impinfo_distributor")
public class T8ProdSalesInfoDistributor {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "份额表id", sql = "ods_amng_prod_impinfo_id = $S{t8ProdImpinfoId}" ,field = "ods_amng_prod_impinfo_id")
   private String t8ProdImpinfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "子产品代码", sql = "prod_code_sub = $S{prodCodeSub}" ,field = "prod_code_sub")
   private String prodCodeSub;
   @GraphQLField(kkhtml = "KFieldText", label = "销售商代码", sql = "distributor_code = $S{distributorCode}" ,field = "distributor_code")
   private String distributorCode;
   @GraphQLField(kkhtml = "KFieldText", label = "发生日期", sql = "change_date = $S{changeDate}" ,field = "change_date")
   private String changeDate;
   @GraphQLField(kkhtml = "KFieldText", label = "认申购份额", sql = "subs_vol = $S{subsVol}" ,field = "subs_vol")
   private String subsVol;
   @GraphQLField(kkhtml = "KFieldText", label = "认申购金额", sql = "subs_amt = $S{subsAmt}" ,field = "subs_amt")
   private String subsAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "赎回份额", sql = "redeem_vol = $S{redeemVol}" ,field = "redeem_vol")
   private String redeemVol;
   @GraphQLField(kkhtml = "KFieldText", label = "赎回金额", sql = "redeem_amt = $S{redeemAmt}" ,field = "redeem_amt")
   private String redeemAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "费用类型", sql = "fee_type = $S{feeType}" ,field = "fee_type")
   private String feeType;
   @GraphQLField(kkhtml = "KFieldText", label = "费用金额", sql = "fee_money = $S{feeMoney}" ,field = "fee_money")
   private String feeMoney;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有其他机构代销", sql = "sale_by_proxy = $S{saleByProxy}" ,field = "sale_by_proxy")
   private String saleByProxy;
   @GraphQLField(kkhtml = "KFieldText", label = "代销总额", sql = "proxy_sum_money = $S{proxySumMoney}" ,field = "proxy_sum_money")
   private String proxySumMoney;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "buy_ccy = $S{buyCcy}" ,field = "buy_ccy")
   private String buyCcy;
   @GraphQLField(kkhtml = "KFieldText", label = "产品流水事件", sql = "prod_deal_type = $S{prodDealType}" ,field = "prod_deal_type")
   private String prodDealType;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user = $S{updUser}" ,field = "upd_user")
   private String updUser;

}