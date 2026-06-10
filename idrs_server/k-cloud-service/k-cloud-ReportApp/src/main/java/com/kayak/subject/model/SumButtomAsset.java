package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "sumButtomAssetService",table = "dwd_sum_buttom_asset")
@Data
public class SumButtomAsset {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "组合代码", sql = "comcode = $S{comcode}" ,field = "comcode")
   private String comcode;
   @GraphQLField(kkhtml = "KFieldText", label = "底层资产代码", sql = "bottom_code = $S{bottomCode}" ,field = "bottom_code")
   private String bottomCode;
   @GraphQLField(kkhtml = "KFieldText", label = "类型", sql = "asset_type = $S{assetType}" ,field = "asset_type")
   private String assetType;
   @GraphQLField(kkhtml = "KFieldText", label = "市值", sql = "amount = $S{amount}" ,field = "amount")
   private String amount;
   @GraphQLField(kkhtml = "KFieldText", label = "成本", sql = "cost = $S{cost}" ,field = "cost")
   private String cost;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "currency = $S{currency}" ,field = "currency")
   private String currency;
   @GraphQLField(kkhtml = "KFieldText", label = "估值日期" ,field = "input_date")
   private String inputDate;
   @GraphQLField(kkhtml = "KFieldText", label = "科目代码", sql = "item_id = $S{itemId}" ,field = "item_id")
   private String itemId;
   @GraphQLField(kkhtml = "KFieldText", label = "科目名称", sql = "item_name = $S{itemName}" ,field = "item_name")
   private String itemName;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "import_date = $S{importDate}" ,field = "import_date")
   private String importDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理财投资资产代码", sql = "i_code = $S{icode}" ,field = "i_code")
   private String icode;
   @GraphQLField(kkhtml = "KFieldText", label = "资管产品名称(第一层)", sql = "i_name_c1 = $S{inamec1}" ,field = "i_name_c1")
   private String inamec1;
   @GraphQLField(kkhtml = "KFieldText", label = "资管产品名称(第二层)", sql = "i_name_c2 = $S{inamec2}" ,field = "i_name_c2")
   private String inamec2;
   @GraphQLField(kkhtml = "KFieldText", label = "资产类型", sql = "a_type = $S{atype}" ,field = "a_type")
   private String atype;
   @GraphQLField(kkhtml = "KFieldText", label = "市场类型", sql = "m_type = $S{mtype}" ,field = "m_type")
   private String mtype;
   @GraphQLField(kkhtml = "KFieldText", label = "起息代码", sql = "asset_code = $S{assetCode}" ,field = "asset_code")
   private String assetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "层级", sql = "org_level = $S{orgLevel}" ,field = "org_level")
   private String orgLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "净价金额", sql = "net_value = $S{netValue}" ,field = "net_value")
   private String netValue;
   @GraphQLField(kkhtml = "KFieldText", label = "中债资产报送类别(表层)", sql = "zz_report_type = $S{zzReportType}" ,field = "zz_report_type")
   private String zzReportType;
   @GraphQLField(kkhtml = "KFieldText", label = "g06分类(穿透底层)", sql = "g06_type = $S{g06Type}" ,field = "g06_type")
   private String g06Type;
   @GraphQLField(kkhtml = "KFieldText", label = "信用等级(底层资产)", sql = "rat_level = $S{ratLevel}" ,field = "rat_level")
   private String ratLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "是否投向公共私营合作项目（PPP）的部分", sql = "is_ppp_part = $S{isPppPart}" ,field = "is_ppp_part")
   private String isPppPart;
   @GraphQLField(kkhtml = "KFieldText", label = "是否投向市场化债转股相关", sql = "is_mkt_bts_rlt = $S{isMktBtsRlt}" ,field = "is_mkt_bts_rlt")
   private String isMktBtsRlt;
   @GraphQLField(kkhtml = "KFieldText", label = "是否投向地方政府融资平台的部分", sql = "is_gov_fnc_part = $S{isGovFncPart}" ,field = "is_gov_fnc_part")
   private String isGovFncPart;
   @GraphQLField(kkhtml = "KFieldText", label = "商业银行优先股", sql = "is_fnc_stk = $S{isFncStk}" ,field = "is_fnc_stk")
   private String isFncStk;
   @GraphQLField(kkhtml = "KFieldText", label = "商业银行永续债", sql = "is_fnc_bnd = $S{isFncBnd}" ,field = "is_fnc_bnd")
   private String isFncBnd;
   @GraphQLField(kkhtml = "KFieldText", label = "商业银行二级资本债", sql = "is_fnc_scd_bnd = $S{isFncScdBnd}" ,field = "is_fnc_scd_bnd")
   private String isFncScdBnd;
   @GraphQLField(kkhtml = "KFieldText", label = "商业银行可转债", sql = "is_fnc_tsf_bnd = $S{isFncTsfBnd}" ,field = "is_fnc_tsf_bnd")
   private String isFncTsfBnd;
   @GraphQLField(kkhtml = "KFieldText", label = "其他银行资本补充工具", sql = "is_oth_bnk_tls = $S{isOthBnkTls}" ,field = "is_oth_bnk_tls")
   private String isOthBnkTls;
   @GraphQLField(kkhtml = "KFieldText", label = "是否地方政府专项债", sql = "is_gov_spc_bnd = $S{isGovSpcBnd}" ,field = "is_gov_spc_bnd")
   private String isGovSpcBnd;
   @GraphQLField(kkhtml = "KFieldText", label = "汇率", sql = "exchange_rate = $S{exchangeRate}" ,field = "exchange_rate")
   private String exchangeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资估值表比例", sql = "inv_val_rate = $S{invValRate}" ,field = "inv_val_rate")
   private String invValRate;
   @GraphQLField(kkhtml = "KFieldText", label = "非保本比例", sql = "non_grt_rate = $S{nonGrtRate}" ,field = "non_grt_rate")
   private String nonGrtRate;
   @GraphQLField(kkhtml = "KFieldText", label = "投向非保本金额", sql = "non_grt_amt = $S{nonGrtAmt}" ,field = "non_grt_amt")
   private String nonGrtAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "交易场所", sql = "trade_place = $S{tradePlace}" ,field = "trade_place")
   private String tradePlace;
   @GraphQLField(kkhtml = "KFieldText", label = "是否公募基金", sql = "is_public = $S{isPublic}" ,field = "is_public")
   private String isPublic;
   @GraphQLField(kkhtml = "KFieldText", label = "管理方式", sql = "manage_mode = $S{manageMode}" ,field = "manage_mode")
   private String manageMode;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "mark = $S{mark}" ,field = "mark")
   private String mark;
   @GraphQLField(kkhtml = "KFieldText", label = "数据插入日期", sql = "data_insr_dt = $S{dataInsrDt}" ,field = "data_insr_dt")
   private String dataInsrDt;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @GraphQLField(kkhtml = "KFieldText", label = "是否投向房地产业", sql = "is_real_setate = $S{isRealSetate}" ,field = "is_real_setate")
   private String isRealSetate;
   @GraphQLField(kkhtml = "KFieldText", label = "是否现管产品投资", sql = "is_csh_mng = $S{isCshMng}" ,field = "is_csh_mng")
   private String isCshMng;
   @GraphQLField(kkhtml = "KFieldText", label = "投资估值表比例(现管产品)", sql = "inv_val_rate_csh = $S{invValRateCsh}" ,field = "inv_val_rate_csh")
   private String invValRateCsh;
   @GraphQLField(kkhtml = "KFieldText", label = "是否养老产品投资", sql = "pen_inv_f = $S{penInvF}" ,field = "pen_inv_f")
   private String penInvF;
   @GraphQLField(kkhtml = "KFieldText", label = "投资估值表比例(养老产品)", sql = "inv_val_rate_pen = $S{invValRatePen}" ,field = "inv_val_rate_pen")
   private String invValRatePen;
   @GraphQLField(kkhtml = "KFieldText", label = "是否个人养老金产品投资", sql = "per_pen_inv_f = $S{perPenInvF}" ,field = "per_pen_inv_f")
   private String perPenInvF;
   @GraphQLField(kkhtml = "KFieldText", label = "投资估值表比例(个人养老金产品)", sql = "inv_val_rate_per_pen = $S{invValRatePerPen}" ,field = "inv_val_rate_per_pen")
   private String invValRatePerPen;
   @GraphQLField(kkhtml = "KFieldText", label = "是否通过港股通投资", sql = "hk_inv = $S{hkInv}" ,field = "hk_inv")
   private String hkInv;
   @GraphQLField(kkhtml = "KFieldText", label = "是否通过QDII投资", sql = "qdii_inv = $S{qdiiInv}" ,field = "qdii_inv")
   private String qdiiInv;


}