package com.kayak.rpt.rhzj.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "reportOverseasInvInfoService",table = "dws_overseas_inv_info")
@Data
public class ReportOverseasInvInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
   private String prodCd;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc = $S{prodRegEnc}" ,field = "prod_reg_enc")
   private String prodRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "持仓类型", sql = "hold_type = $S{holdType}" ,field = "hold_type")
   private String holdType;
   @GraphQLField(kkhtml = "KFieldText", label = "首层资产代码", sql = "f_asst_cd = $S{fasstCd}" ,field = "f_asst_cd")
   private String fasstCd;
   @GraphQLField(kkhtml = "KFieldText", label = "资产三类", sql = "asset_third_type = $S{assetThirdType}" ,field = "asset_third_type")
   private String assetThirdType;
   @GraphQLField(kkhtml = "KFieldText", label = "首层资产名称", sql = "f_asst_nm = $S{fasstNm}" ,field = "f_asst_nm")
   private String fasstNm;
   @GraphQLField(kkhtml = "KFieldText", label = "首层折算人民币金额（万元）", sql = "f_amount = $S{famount}" ,field = "f_amount")
   private String famount;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透前分类", sql = "org_classific = $S{orgClassific}" ,field = "org_classific")
   private String orgClassific;
   @GraphQLField(kkhtml = "KFieldText", label = "底层科目代码", sql = "itm_cd = $S{itmCd}" ,field = "itm_cd")
   private String itmCd;
   @GraphQLField(kkhtml = "KFieldText", label = "底层科目名称", sql = "itm_nm = $S{itmNm}" ,field = "itm_nm")
   private String itmNm;
   @GraphQLField(kkhtml = "KFieldText", label = "底层折算人民币金额（万元）", sql = "d_amount = $S{damount}" ,field = "d_amount")
   private String damount;
   @GraphQLField(kkhtml = "KFieldText", label = "穿透后分类", sql = "new_classific = $S{newClassific}" ,field = "new_classific")
   private String newClassific;
   @GraphQLField(kkhtml = "KFieldText", label = "初始投资时间", sql = "f_inv_tm = $S{finvTm}" ,field = "f_inv_tm")
   private String finvTm;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产分类", sql = "asst_type = $S{asstType}" ,field = "asst_type")
   private String asstType;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产本金（万元）", sql = "asst_amount = $S{asstAmount}" ,field = "asst_amount")
   private String asstAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "投资地区", sql = "asst_zon = $S{asstZon}" ,field = "asst_zon")
   private String asstZon;
   @GraphQLField(kkhtml = "KFieldText", label = "管理人注册地", sql = "mang_zon = $S{mangZon}" ,field = "mang_zon")
   private String mangZon;
   @GraphQLField(kkhtml = "KFieldText", label = "计提减值准备金额", sql = "depr_rdy_amt = $S{deprRdyAmt}" ,field = "depr_rdy_amt")
   private String deprRdyAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "已计提减值", sql = "depr_amt = $S{deprAmt}" ,field = "depr_amt")
   private String deprAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "外汇额度类型", sql = "fx_type = $S{fxType}" ,field = "fx_type")
   private String fxType;
   @GraphQLField(kkhtml = "KFieldText", label = "债务期限结构", sql = "bd_rmai_type = $S{bdRmaiType}" ,field = "bd_rmai_type")
   private String bdRmaiType;
   @GraphQLField(kkhtml = "KFieldText", label = "剩余天数", sql = "rmai_day = $S{rmaiDay}" ,field = "rmai_day")
   private String rmaiDay;
   @GraphQLField(kkhtml = "KFieldText", label = "风险事件", sql = "risk_envn = $S{riskEnvn}" ,field = "risk_envn")
   private String riskEnvn;
   @GraphQLField(kkhtml = "KFieldText", label = "风险项目余额", sql = "risk_pj_amt = $S{riskPjAmt}" ,field = "risk_pj_amt")
   private String riskPjAmt;

}