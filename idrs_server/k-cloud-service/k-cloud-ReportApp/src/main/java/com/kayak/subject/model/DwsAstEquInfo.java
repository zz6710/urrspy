package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsAstEquInfoService",table = "dws_ast_equ_info")
@Data
public class DwsAstEquInfo {

   @ExcelIgnore
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @ExcelProperty(value = "产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
   private String prodCd;
   @ExcelProperty(value = "内部产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "内部产品代码", sql = "prod_intr_cd = $S{prodIntrCd}" ,field = "prod_intr_cd")
   private String prodIntrCd;
   @ExcelProperty(value = "资产代码")
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "ast_cd = $S{astCd}" ,field = "ast_cd")
   private String astCd;
   @ExcelProperty(value = "资产三类编码")
   @GraphQLField(kkhtml = "KFieldText", label = "资产三类编码", sql = "ast_thr_cd = $S{astThrCd}" ,field = "ast_thr_cd")
   private String astThrCd;
   @ExcelProperty(value = "资产三类名称")
   @GraphQLField(kkhtml = "KFieldText", label = "资产三类名称", sql = "ast_thr_nm = $S{astThrNm}" ,field = "ast_thr_nm")
   private String astThrNm;
   @ExcelProperty(value = "债权类型/资产负债项目")
   @GraphQLField(kkhtml = "KFieldText", label = "债权类型/资产负债项目", sql = "ast_typ_dbt_pjt = $S{astTypDbtPjt}" ,field = "ast_typ_dbt_pjt")
   private String astTypDbtPjt;
   @ExcelProperty(value = "地区代码")
   @GraphQLField(kkhtml = "KFieldText", label = "地区代码", sql = "zon_cd = $S{zonCd}" ,field = "zon_cd")
   private String zonCd;
   @ExcelProperty(value = "借款人名称")
   @GraphQLField(kkhtml = "KFieldText", label = "借款人名称", sql = "dbt_nm = $S{dbtNm}" ,field = "dbt_nm")
   private String dbtNm;
   @ExcelProperty(value = "借款人类型")
   @GraphQLField(kkhtml = "KFieldText", label = "借款人类型", sql = "dbt_typ = $S{dbtTyp}" ,field = "dbt_typ")
   private String dbtTyp;
   @ExcelProperty(value = "借款人代码")
   @GraphQLField(kkhtml = "KFieldText", label = "借款人代码", sql = "dbt_cd = $S{dbtCd}" ,field = "dbt_cd")
   private String dbtCd;
   @ExcelProperty(value = "行业类型")
   @GraphQLField(kkhtml = "KFieldText", label = "行业类型", sql = "idt_typ = $S{idtTyp}" ,field = "idt_typ")
   private String idtTyp;
   @ExcelProperty(value = "企业出资人经济成分")
   @GraphQLField(kkhtml = "KFieldText", label = "企业出资人经济成分", sql = "inv_icm_pct = $S{invIcmPct}" ,field = "inv_icm_pct")
   private String invIcmPct;
   @ExcelProperty(value = "企业规模")
   @GraphQLField(kkhtml = "KFieldText", label = "企业规模", sql = "etp_scl = $S{etpScl}" ,field = "etp_scl")
   private String etpScl;
   @ExcelProperty(value = "成立日")
   @GraphQLField(kkhtml = "KFieldText", label = "成立日", sql = "opn_dt = $S{opnDt}" ,field = "opn_dt")
   private String opnDt;
   @ExcelProperty(value = "到期日")
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "end_dt = $S{endDt}" ,field = "end_dt")
   private String endDt;
   @ExcelProperty(value = "展期到期日")
   @GraphQLField(kkhtml = "KFieldText", label = "展期到期日", sql = "exp_end_dt = $S{expEndDt}" ,field = "exp_end_dt")
   private String expEndDt;
   @ExcelProperty(value = "利率是否固定")
   @GraphQLField(kkhtml = "KFieldText", label = "利率是否固定", sql = "intr_rt_typ = $S{intrRtTyp}" ,field = "intr_rt_typ")
   private String intrRtTyp;
   @ExcelProperty(value = "利率水平/持股比例")
   @GraphQLField(kkhtml = "KFieldText", label = "利率水平/持股比例", sql = "intr_rt = $S{intrRt}" ,field = "intr_rt")
   private String intrRt;
   @ExcelProperty(value = "担保方式")
   @GraphQLField(kkhtml = "KFieldText", label = "担保方式", sql = "gur_typ = $S{gurTyp}" ,field = "gur_typ")
   private String gurTyp;
   @ExcelProperty(value = "币种")
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "ccy_cd = $S{ccyCd}" ,field = "ccy_cd")
   private String ccyCd;
   @ExcelProperty(value = "起息本金")
   @GraphQLField(kkhtml = "KFieldText", label = "起息本金", sql = "intr_amt_bal = $S{intrAmtBal}" ,field = "intr_amt_bal")
   private String intrAmtBal;
   @ExcelProperty(value = "余额")
   @GraphQLField(kkhtml = "KFieldText", label = "余额", sql = "amt_bal = $S{amtBal}" ,field = "amt_bal")
   private String amtBal;
   @ExcelProperty(value = "登记交易场所")
   @GraphQLField(kkhtml = "KFieldText", label = "登记交易场所", sql = "rgt_trd_plc = $S{rgtTrdPlc}" ,field = "rgt_trd_plc")
   private String rgtTrdPlc;
   @ExcelProperty(value = "登记交易场所代码")
   @GraphQLField(kkhtml = "KFieldText", label = "登记交易场所代码", sql = "rgt_trd_plc_cd = $S{rgtTrdPlcCd}" ,field = "rgt_trd_plc_cd")
   private String rgtTrdPlcCd;
   @ExcelProperty(value = "股权投资方式")
   @GraphQLField(kkhtml = "KFieldText", label = "股权投资方式", sql = "shr_hld_inv_typ = $S{shrHldInvTyp}" ,field = "shr_hld_inv_typ")
   private String shrHldInvTyp;
   @ExcelProperty(value = "股权出让方代码")
   @GraphQLField(kkhtml = "KFieldText", label = "股权出让方代码", sql = "shr_hld_tsf_cd = $S{shrHldTsfCd}" ,field = "shr_hld_tsf_cd")
   private String shrHldTsfCd;
   @ExcelProperty(value = "股权出让方名称")
   @GraphQLField(kkhtml = "KFieldText", label = "股权出让方名称", sql = "shr_hld_tsf_nm = $S{shrHldTsfNm}" ,field = "shr_hld_tsf_nm")
   private String shrHldTsfNm;
   @ExcelProperty(value = "投资退出方式")
   @GraphQLField(kkhtml = "KFieldText", label = "投资退出方式", sql = "inv_out_typ = $S{invOutTyp}" ,field = "inv_out_typ")
   private String invOutTyp;
   @ExcelProperty(value = "数据日期")
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "act_dt = $S{actDt}" ,field = "act_dt")
   private String actDt;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;

   @ExcelProperty(value = "科技相关产业标识")
   @GraphQLField(kkhtml = "KFieldText", label = "科技相关产业标识", sql = "tech_flag = $S{techFlag}" ,field = "tech_flag")
   private String techFlag;

   @ExcelProperty(value = "绿色领域标识")
   @GraphQLField(kkhtml = "KFieldText", label = "绿色领域标识", sql = "green_flag = $S{greenFlag}" ,field = "green_flag")
   private String greenFlag;

   @ExcelProperty(value = "普惠领域标识")
   @GraphQLField(kkhtml = "KFieldText", label = "普惠领域标识", sql = "spec_flag = $S{specFlag}" ,field = "spec_flag")
   private String specFlag;

   @ExcelProperty(value = "养老产业标识")
   @GraphQLField(kkhtml = "KFieldText", label = "养老产业标识", sql = "aged_flag = $S{agedFlag}" ,field = "aged_flag")
   private String agedFlag;

   @ExcelProperty(value = "数字经济核心产业标识")
   @GraphQLField(kkhtml = "KFieldText", label = "数字经济核心产业标识", sql = "num_core_flag = $S{numCoreFlag}" ,field = "num_core_flag")
   private String numCoreFlag;

   @ExcelProperty(value = "出让机构出表标识")
   @GraphQLField(kkhtml = "KFieldText", label = "出让机构出表标识", sql = "trans_org_out_table_f = $S{transOrgOutTableF}" ,field = "trans_org_out_table_f")
   private String transOrgOutTableF;

   @ExcelProperty(value = "出让机构回购标识")
   @GraphQLField(kkhtml = "KFieldText", label = "出让机构回购标识", sql = "trans_org_buy_back_f = $S{transOrgBuyBackF}" ,field = "trans_org_buy_back_f")
   private String transOrgBuyBackF;

   @ExcelProperty(value = "基础资产投向部门")
   @GraphQLField(kkhtml = "KFieldText", label = "基础资产投向部门", sql = "base_asset_trans_dep = $S{baseAssetTransDep}" ,field = "base_asset_trans_dep")
   private String baseAssetTransDep;

   @ExcelProperty(value = "原始协议金额")
   @GraphQLField(kkhtml = "KFieldText", label = "原始协议金额", sql = "base_asset_ori_prot_amt = $S{baseAssetOriProtAmt}" ,field = "base_asset_ori_prot_amt")
   private String baseAssetOriProtAmt;

   @ExcelProperty(value = "基础资产投向对象行业")
   @GraphQLField(kkhtml = "KFieldText", label = "基础资产投向对象行业", sql = "base_asset_inv_obj_idt = $S{baseAssetInvObjIdt}" ,field = "base_asset_inv_obj_idt")
   private String baseAssetInvObjIdt;

   @ExcelProperty(value = "基础资产投向对象规模")
   @GraphQLField(kkhtml = "KFieldText", label = "基础资产投向对象规模", sql = "base_asset_inv_obj_scale = $S{baseAssetInvObjScale}" ,field = "base_asset_inv_obj_scale")
   private String baseAssetInvObjScale;
}