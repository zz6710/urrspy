package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assNetValSPVInfoService",table = "ods_supply_ast_mng_plan_inf")
public class AssNetValSPVInfoModel {
   @GraphQLField(kkhtml = "KFieldText", label = "证券编码", sql = "scr_id = $S{scrId}" ,field = "scr_id")
   private String scrId;
   @GraphQLField(kkhtml = "KFieldText", label = "中债一级分类", sql = "cbnd_frs_ctg = $S{cbndFrsCtg}" ,field = "cbnd_frs_ctg")
   private String cbndFrsCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "中债二级分类", sql = "cbnd_scd_ctg = $S{cbndScdCtg}" ,field = "cbnd_scd_ctg")
   private String cbndScdCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "人行一级分类", sql = "pbnk_frs_ctg = $S{pbnkFrsCtg}" ,field = "pbnk_frs_ctg")
   private String pbnkFrsCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "人行二级分类", sql = "pbnk_scd_ctg = $S{pbnkScdCtg}" ,field = "pbnk_scd_ctg")
   private String pbnkScdCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "人行三级分类", sql = "pbnk_trd_ctg = $S{pbnkTrdCtg}" ,field = "pbnk_trd_ctg")
   private String pbnkTrdCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "银行理财产品标识", sql = "bnk_inv_prod_f = $S{bnkInvProdF}" ,field = "bnk_inv_prod_f")
   private String bnkInvProdF;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc = $S{prodRegEnc}" ,field = "prod_reg_enc")
   private String prodRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "金融资产投资公司发行标识", sql = "fin_ast_inv_cmp_isu_f = $S{finAstInvCmpIsuF}" ,field = "fin_ast_inv_cmp_isu_f")
   private String finAstInvCmpIsuF;
   @GraphQLField(kkhtml = "KFieldText", label = "发起人机构编码", sql = "isu_org_enc = $S{isuOrgEnc}" ,field = "isu_org_enc")
   private String isuOrgEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "资管计划登记编码", sql = "ast_mng_plan_reg_enc = $S{astMngPlanRegEnc}" ,field = "ast_mng_plan_reg_enc")
   private String astMngPlanRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "托管人", sql = "cstd = $S{cstd}" ,field = "cstd")
   private String cstd;
   @GraphQLField(kkhtml = "KFieldText", label = "资金实际投向", sql = "fnd_actl_dir = $S{fndActlDir}" ,field = "fnd_actl_dir")
   private String fndActlDir;
   @GraphQLField(kkhtml = "KFieldText", label = "资金运用方式", sql = "fnd_crry_mth = $S{fndCrryMth}" ,field = "fnd_crry_mth")
   private String fndCrryMth;
   @GraphQLField(kkhtml = "KFieldText", label = "资金运用行业", sql = "fnd_crry_idt = $S{fndCrryIdt}" ,field = "fnd_crry_idt")
   private String fndCrryIdt;
   @GraphQLField(kkhtml = "KFieldText", label = "资管计划属性", sql = "ast_mng_plan_prpt = $S{astMngPlanPrpt}" ,field = "ast_mng_plan_prpt")
   private String astMngPlanPrpt;
   @GraphQLField(kkhtml = "KFieldText", label = "预期收益率标识", sql = "expe_rat_f = $S{expeRatF}" ,field = "expe_rat_f")
   private String expeRatF;
   @GraphQLField(kkhtml = "KFieldText", label = "预期最高收益率", sql = "expe_max_rat = $S{expeMaxRat}" ,field = "expe_max_rat")
   private String expeMaxRat;
   @GraphQLField(kkhtml = "KFieldText", label = "预期最低收益率", sql = "expe_min_rat = $S{expeMinRat}" ,field = "expe_min_rat")
   private String expeMinRat;
   @GraphQLField(kkhtml = "KFieldText", label = "购买结构", sql = "buy_strc = $S{buyStrc}" ,field = "buy_strc")
   private String buyStrc;
   @GraphQLField(kkhtml = "KFieldText", label = "管理方式", sql = "mng_mth = $S{mngMth}" ,field = "mng_mth")
   private String mngMth;
   @GraphQLField(kkhtml = "KFieldText", label = "管理费率", sql = "mng_fee_tat = $S{mngFeeTat}" ,field = "mng_fee_tat")
   private String mngFeeTat;
   @GraphQLField(kkhtml = "KFieldText", label = "托管费率", sql = "trst_fee_tat = $S{trstFeeTat}" ,field = "trst_fee_tat")
   private String trstFeeTat;
   @GraphQLField(kkhtml = "KFieldText", label = "交易相关合计费率", sql = "trx_rel_smr_fee_rat = $S{trxRelSmrFeeRat}" ,field = "trx_rel_smr_fee_rat")
   private String trxRelSmrFeeRat;
   @GraphQLField(kkhtml = "KFieldText", label = "中介服务机构合计费率", sql = "med_agn_srv_org_smr_fee_rat = $S{medAgnSrvOrgSmrFeeRat}" ,field = "med_agn_srv_org_smr_fee_rat")
   private String medAgnSrvOrgSmrFeeRat;
   @GraphQLField(kkhtml = "KFieldText", label = "其他合计费率", sql = "oth_smr_fee_rat = $S{othSmrFeeRat}" ,field = "oth_smr_fee_rat")
   private String othSmrFeeRat;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "cmt = $S{cmt}" ,field = "cmt")
   private String cmt;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
   private String updDt;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "资管计划成立日期")
   private String setUpDt;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日期")
   private String mtuDt;
   @GraphQLField(kkhtml = "KFieldText", label = "管理人", sql = "mng = $S{mng}" ,field = "mng")
   private String mng;
   @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "amt = $S{amt}" ,field = "amt")
   private String amt;
   @GraphQLField(kkhtml = "KFieldText", label = "特定目的载体发起人机构编码", sql = "spv_org_enc = $S{spvOrgEnc}" ,field = "spv_org_enc")
   private String spvOrgEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "特定目的载体产品登记编码", sql = "spv_prod_reg_enc = $S{spvProdRegEnc}" ,field = "spv_prod_reg_enc")
   private String spvProdRegEnc;
}