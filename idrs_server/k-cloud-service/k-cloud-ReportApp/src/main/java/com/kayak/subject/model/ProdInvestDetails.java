package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "prodInvestDetailsService",table = "dws_prod_invest_details")
@Data
public class ProdInvestDetails {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "product_code = $S{productCode}" ,field = "product_code")
   private String productCode;
   @GraphQLField(kkhtml = "KFieldText", label = "理财投资资产代码", sql = "i_code = $S{icode}" ,field = "icode")
   private String icode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产三类", sql = "asset_third_type = $S{assetThirdType}" ,field = "asset_third_type")
   private String assetThirdType;
   @GraphQLField(kkhtml = "KFieldText", label = "资管产品名称(第一层)", sql = "i_name_c1 = $S{inamec1}" ,field = "i_name_c1")
   private String inamec1;
   @GraphQLField(kkhtml = "KFieldText", label = "资管产品名称(第二层)", sql = "i_name_c2 = $S{inamec2}" ,field = "i_name_c2")
   private String inamec2;
   @GraphQLField(kkhtml = "KFieldText", label = "底层代码", sql = "bottom_code = $S{bottomCode}" ,field = "bottom_code")
   private String bottomCode;
   @GraphQLField(kkhtml = "KFieldText", label = "科目名称", sql = "item_name = $S{itemName}" ,field = "item_name")
   private String itemName;
   @GraphQLField(kkhtml = "KFieldText", label = "组合代码", sql = "comcode = $S{comcode}" ,field = "comcode")
   private String comcode;
   @GraphQLField(kkhtml = "KFieldText", label = "成本", sql = "cost = $S{cost}" ,field = "cost")
   private String cost;
   @GraphQLField(kkhtml = "KFieldText", label = "市值", sql = "amount = $S{amount}" ,field = "amount")
   private String amount;
   @GraphQLField(kkhtml = "KFieldText", label = "汇率", sql = "inv_val_rate_csh = $S{invValRateCsh}" ,field = "inv_val_rate_csh")
   private String invValRateCsh;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "currency = $S{currency}" ,field = "currency")
   private String currency;
   @GraphQLField(kkhtml = "KFieldText", label = "委托/自主管理", sql = "orderfreemanage = $S{orderfreemanage}" ,field = "orderfreemanage")
   private String orderfreemanage;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前分类", sql = "org_classific = $S{orgClassific}" ,field = "org_classific")
   private String orgClassific;
   @GraphQLField(kkhtml = "KFieldText", label = "G06穿透底层分类", sql = "g06_type = $S{g06Type}" ,field = "g06_type")
   private String g06Type;
   @GraphQLField(kkhtml = "KFieldText", label = "转型表分类-二类", sql = "zxb_second_type = $S{zxbSecondType}" ,field = "zxb_second_type")
   private String zxbSecondType;
   @GraphQLField(kkhtml = "KFieldText", label = "转型表分类-大类", sql = "zxb_first_type = $S{zxbFirstType}" ,field = "zxb_first_type")
   private String zxbFirstType;
   @GraphQLField(kkhtml = "KFieldText", label = "转型表分类-三类", sql = "zxb_third_type = $S{zxbThirdType}" ,field = "zxb_third_type")
   private String zxbThirdType;
   @GraphQLField(kkhtml = "KFieldText", label = "从母行划转的产品（比例）", sql = "prodmonrate = $S{prodmonrate}" ,field = "prodmonrate")
   private String prodmonrate;
   @GraphQLField(kkhtml = "KFieldText", label = "自主发行产品（比例）", sql = "pordownrate = $S{pordownrate}" ,field = "pordownrate")
   private String pordownrate;
   @GraphQLField(kkhtml = "KFieldText", label = "我司持仓市值", sql = "mycompnyamount = $S{mycompnyamount}" ,field = "mycompnyamount")
   private String mycompnyamount;
   @GraphQLField(kkhtml = "KFieldText", label = "母行划转产品持仓市值", sql = "investmonamount = $S{investmonamount}" ,field = "investmonamount")
   private String investmonamount;
   @GraphQLField(kkhtml = "KFieldText", label = "自主发行产品持仓市值", sql = "investownamount = $S{investownamount}" ,field = "investownamount")
   private String investownamount;
   @GraphQLField(kkhtml = "KFieldText", label = "信用等级", sql = "rat_level = $S{ratLevel}" ,field = "rat_level")
   private String ratLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "是否投向公共私营合作项目（PPP）的部分", sql = "is_ppp_part = $S{isPppPart}" ,field = "is_ppp_part")
   private String isPppPart;
   @GraphQLField(kkhtml = "KFieldText", label = "是否投向市场化债转股相关", sql = "is_mkt_bts_rlt = $S{isMktBtsRlt}" ,field = "is_mkt_bts_rlt")
   private String isMktBtsRlt;
   @GraphQLField(kkhtml = "KFieldText", label = "是否投向地方政府融资平台的部分", sql = "is_gov_fnc_part = $S{isGovFncPart}" ,field = "is_gov_fnc_part")
   private String isGovFncPart;
   @GraphQLField(kkhtml = "KFieldText", label = "商业银行优先股:01-是 02-否", sql = "is_fnc_stk = $S{isFncStk}" ,field = "is_fnc_stk")
   private String isFncStk;
   @GraphQLField(kkhtml = "KFieldText", label = "商业银行永续债:01-是 02-否", sql = "is_fnc_bnd = $S{isFncBnd}" ,field = "is_fnc_bnd")
   private String isFncBnd;
   @GraphQLField(kkhtml = "KFieldText", label = "商业银行二级资本债:01-是 02-否", sql = "is_fnc_scd_bnd = $S{isFncScdBnd}" ,field = "is_fnc_scd_bnd")
   private String isFncScdBnd;
   @GraphQLField(kkhtml = "KFieldText", label = "商业银行可转债:01-是 02-否", sql = "is_fnc_tsf_bnd = $S{isFncTsfBnd}" ,field = "is_fnc_tsf_bnd")
   private String isFncTsfBnd;
   @GraphQLField(kkhtml = "KFieldText", label = "其他银行资本补充工具:01-是 02-否", sql = "is_oth_bnk_tls = $S{isOthBnkTls}" ,field = "is_oth_bnk_tls")
   private String isOthBnkTls;
   @GraphQLField(kkhtml = "KFieldText", label = "是否地方政府专项债:01-是 02-否", sql = "is_gov_spc_bnd = $S{isGovSpcBnd}" ,field = "is_gov_spc_bnd")
   private String isGovSpcBnd;
   @GraphQLField(kkhtml = "KFieldText", label = "投资估值表比例", sql = "inv_val_rate = $S{invValRate}" ,field = "inv_val_rate")
   private String invValRate;
   @GraphQLField(kkhtml = "KFieldText", label = "非保本比例", sql = "non_grt_rate = $S{nonGrtRate}" ,field = "non_grt_rate")
   private String nonGrtRate;
   @GraphQLField(kkhtml = "KFieldText", label = "投向非保本金额", sql = "non_grt_amt = $S{nonGrtAmt}" ,field = "non_grt_amt")
   private String nonGrtAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "mark = $S{mark}" ,field = "mark")
   private String mark;
   @GraphQLField(kkhtml = "KFieldText", label = "资产到期日", sql = "asset_end_date = $S{assetEndDate}" ,field = "asset_end_date")
   private String assetEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "资产期限特殊说明", sql = "asset_term_pj = $S{assetTermPj}" ,field = "asset_term_pj")
   private String assetTermPj;
   @GraphQLField(kkhtml = "KFieldText", label = "报告日", sql = "bg_date = $S{bgDate}" ,field = "bg_date")
   private String bgDate;
   @GraphQLField(kkhtml = "KFieldText", label = "剩余期限", sql = "over_day = $S{overDay}" ,field = "over_day")
   private String overDay;
   @GraphQLField(kkhtml = "KFieldText", label = "母行划转产品投资权重", sql = "investmonrate = $S{investmonrate}" ,field = "investmonrate")
   private String investmonrate;
   @GraphQLField(kkhtml = "KFieldText", label = "自主发行产品投资权重", sql = "investownrate = $S{investownrate}" ,field = "investownrate")
   private String investownrate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品分类:01-从母行划转的产品 02-自主发行产品", sql = "prod_type = $S{prodType}" ,field = "prod_type")
   private String prodType;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "act_dt = $S{actDt}" ,field = "act_dt")
   private String actDt;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @GraphQLField(kkhtml = "KFieldText", label = "是否创业投资基金", sql = "vcintfund = $S{vcintfund}" ,field = "vcintfund")
   private String vcintfund;
   @GraphQLField(kkhtml = "KFieldText", label = "是否政府出资产业投资基金", sql = "govintfund = $S{govintfund}" ,field = "govintfund")
   private String govintfund;

}