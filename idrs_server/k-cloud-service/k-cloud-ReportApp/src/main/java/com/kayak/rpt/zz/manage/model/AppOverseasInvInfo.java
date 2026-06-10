package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "appOverseasInvInfoService",table = "app_overseas_inv_info")
@Data
public class AppOverseasInvInfo {

   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date like '%$U{reportDate}%'" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "序号", sql = "num = $S{num}" ,field = "num")
   private String num;
   @GraphQLField(kkhtml = "KFieldText", label = "公司全称", sql = "comp_nm = $S{compNm}" ,field = "comp_nm")
   private String compNm;
   @GraphQLField(kkhtml = "KFieldText", label = "资金来源类型", sql = "fund_sour_type = $S{fundSourType}" ,field = "fund_sour_type")
   private String fundSourType;
   @GraphQLField(kkhtml = "KFieldText", label = "资金最终持有人与投资资产是否均在境外", sql = "fund_sour_over = $S{fundSourOver}" ,field = "fund_sour_over")
   private String fundSourOver;
   @GraphQLField(kkhtml = "KFieldText", label = "涉及通过产品投资的产品名称", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
   private String prodCd;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc = $S{prodRegEnc}" ,field = "prod_reg_enc")
   private String prodRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品境外托管机构名称", sql = "prod_org_nm = $S{prodOrgNm}" ,field = "prod_org_nm")
   private String prodOrgNm;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品境外托管机构国别", sql = "prod_org_seas = $S{prodOrgSeas}" ,field = "prod_org_seas")
   private String prodOrgSeas;
   @GraphQLField(kkhtml = "KFieldText", label = "投资形式", sql = "inv_type = $S{invType}" ,field = "inv_type")
   private String invType;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "prod_ccy = $S{prodCcy}" ,field = "prod_ccy")
   private String prodCcy;
   @GraphQLField(kkhtml = "KFieldText", label = "投资初始日期", sql = "f_inv_tm = $S{finvTm}" ,field = "f_inv_tm")
   private String finvTm;
   @GraphQLField(kkhtml = "KFieldText", label = "投资方式1", sql = "inv_type_f = $S{invTypeF}" ,field = "inv_type_f")
   private String invTypeF;
   @GraphQLField(kkhtml = "KFieldText", label = "投资方式2", sql = "inv_type_s = $S{invTypeS}" ,field = "inv_type_s")
   private String invTypeS;
   @GraphQLField(kkhtml = "KFieldText", label = "是否境外主权债权", sql = "over_sove_bd = $S{overSoveBd}" ,field = "over_sove_bd")
   private String overSoveBd;
   @GraphQLField(kkhtml = "KFieldText", label = "外汇额度类型", sql = "fx_type = $S{fxType}" ,field = "fx_type")
   private String fxType;
   @GraphQLField(kkhtml = "KFieldText", label = "投资区域1（国家或地区）", sql = "inv_seas_f = $S{invSeasF}" ,field = "inv_seas_f")
   private String invSeasF;
   @GraphQLField(kkhtml = "KFieldText", label = "投资区域2（国家或地区）", sql = "inv_seas_s = $S{invSeasS}" ,field = "inv_seas_s")
   private String invSeasS;
   @GraphQLField(kkhtml = "KFieldText", label = "投资区域3（国家或地区）", sql = "inv_seas_t = $S{invSeasT}" ,field = "inv_seas_t")
   private String invSeasT;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产类型1", sql = "inv_asst_f = $S{invAsstF}" ,field = "inv_asst_f")
   private String invAsstF;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产类型2", sql = "inv_asst_s = $S{invAsstS}" ,field = "inv_asst_s")
   private String invAsstS;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产类型3", sql = "inv_asst_t = $S{invAsstT}" ,field = "inv_asst_t")
   private String invAsstT;
   @GraphQLField(kkhtml = "KFieldText", label = "港股通基金或QDII基金", sql = "qdii_fund = $S{qdiiFund}" ,field = "qdii_fund")
   private String qdiiFund;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产名称", sql = "inv_asst_nm = $S{invAsstNm}" ,field = "inv_asst_nm")
   private String invAsstNm;
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手", sql = "tran_count = $S{tranCount}" ,field = "tran_count")
   private String tranCount;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产本金", sql = "inv_asst_cost = $S{invAsstCost}" ,field = "inv_asst_cost")
   private String invAsstCost;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产余额", sql = "inv_asst_mkt = $S{invAsstMkt}" ,field = "inv_asst_mkt")
   private String invAsstMkt;
   @GraphQLField(kkhtml = "KFieldText", label = "债务期限结构", sql = "bd_rmai_type = $S{bdRmaiType}" ,field = "bd_rmai_type")
   private String bdRmaiType;
   @GraphQLField(kkhtml = "KFieldText", label = "风险事件", sql = "risk_envn = $S{riskEnvn}" ,field = "risk_envn")
   private String riskEnvn;
   @GraphQLField(kkhtml = "KFieldText", label = "风险项目余额", sql = "risk_pj_amt = $S{riskPjAmt}" ,field = "risk_pj_amt")
   private String riskPjAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "计提减值准备金额", sql = "depr_rdy_amt = $S{deprRdyAmt}" ,field = "depr_rdy_amt")
   private String deprRdyAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "已计提减值", sql = "depr_amt = $S{deprAmt}" ,field = "depr_amt")
   private String deprAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "note = $S{note}" ,field = "note")
   private String note;

}