package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assNonStandInfoService",table = "ods_ast_nstd_ast_inf")
public class AssNonStandInfoModel {
   @GraphQLField(kkhtml = "KFieldText", label = "证券编号", sql = "scr_id = $S{scrId}" ,field = "scr_id")
   private String scrId;

   @GraphQLField(kkhtml = "KFieldText", label = "付息频率", sql = "pay_intr_frq = $S{payIntrFrq}" ,field = "pay_intr_frq")
   private String payIntrFrq;


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

   @GraphQLField(kkhtml = "KFieldText", label = "份额面值", sql = "lot_par_val = $S{lotParVal}" ,field = "lot_par_val")
   private String lotParVal;
   @GraphQLField(kkhtml = "KFieldText", label = "收/受益权类型", sql = "inc_ben_rit_typ = $S{incBenRitTyp}" ,field = "inc_ben_rit_typ")
   private String incBenRitTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "买入返售标识", sql = "buy_back_f = $S{buyBackF}" ,field = "buy_back_f")
   private String buyBackF;
   @GraphQLField(kkhtml = "KFieldText", label = "项目期限", sql = "prj_trm = $S{prjTrm}" ,field = "prj_trm")
   private String prjTrm;
   @GraphQLField(kkhtml = "KFieldText", label = "计息类型", sql = "intr_typ = $S{intrTyp}" ,field = "intr_typ")
   private String intrTyp;

   @GraphQLField(kkhtml = "KFieldText", label = "利息分布方式", sql = "intr_alc_mth = $S{intrAlcMth}" ,field = "intr_alc_mth")
   private String intrAlcMth;
   @GraphQLField(kkhtml = "KFieldText", label = "还本付息情况说明", sql = "pay_prcp_intr_sts_cmt = $S{payPrcpIntrStsCmt}" ,field = "pay_prcp_intr_sts_cmt")
   private String payPrcpIntrStsCmt;

   @GraphQLField(kkhtml = "KFieldText", label = "基准利率种类", sql = "bchm_rat_typ = $S{bchmRatTyp}" ,field = "bchm_rat_typ")
   private String bchmRatTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "浮动因子标识", sql = "flt_fct_f = $S{fltFctF}" ,field = "flt_fct_f")
   private String fltFctF;
   @GraphQLField(kkhtml = "KFieldText", label = "浮动因子", sql = "flt_fct = $S{fltFct}" ,field = "flt_fct")
   private String fltFct;
   @GraphQLField(kkhtml = "KFieldText", label = "利差", sql = "sprd = $S{sprd}" ,field = "sprd")
   private String sprd;
   @GraphQLField(kkhtml = "KFieldText", label = "结构档次", sql = "strc_grd = $S{strcGrd}" ,field = "strc_grd")
   private String strcGrd;

   @GraphQLField(kkhtml = "KFieldText", label = "分期还本标识", sql = "ins_pay_prcp_f = $S{insPayPrcpF}" ,field = "ins_pay_prcp_f")
   private String insPayPrcpF;
   @GraphQLField(kkhtml = "KFieldText", label = "基础资产类型", sql = "bas_ast_typ = $S{basAstTyp}" ,field = "bas_ast_typ")
   private String basAstTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "超额收益分配比例", sql = "exs_ern_alc_rto = $S{exsErnAlcRto}" ,field = "exs_ern_alc_rto")
   private String exsErnAlcRto;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人", sql = "lvrg = $S{lvrg}" ,field = "lvrg")
   private String lvrg;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人内部信用评级", sql = "lvrg_in_crd_rat = $S{lvrgInCrdRat}" ,field = "lvrg_in_crd_rat")
   private String lvrgInCrdRat;
   @GraphQLField(kkhtml = "KFieldText", label = "外部评级机构及融资人评级", sql = "out_rat_org_and_lvrg_rat = $S{outRatOrgAndLvrgRat}" ,field = "out_rat_org_and_lvrg_rat")
   private String outRatOrgAndLvrgRat;
   @GraphQLField(kkhtml = "KFieldText", label = "机构类型（按规模划分）", sql = "lvrg_typ_siz = $S{lvrgTypSiz}" ,field = "lvrg_typ_siz")
   private String lvrgTypSiz;
   @GraphQLField(kkhtml = "KFieldText", label = "机构类型（按技术领域划分）", sql = "lvrg_typ_tchno = $S{lvrgTypTchno}" ,field = "lvrg_typ_tchno")
   private String lvrgTypTchno;
   @GraphQLField(kkhtml = "KFieldText", label = "机构类型（按经济类型划分）", sql = "lvrg_typ_ecn = $S{lvrgTypEcn}" ,field = "lvrg_typ_ecn")
   private String lvrgTypEcn;
   @GraphQLField(kkhtml = "KFieldText", label = "融资项目", sql = "lvrg_prj = $S{lvrgPrj}" ,field = "lvrg_prj")
   private String lvrgPrj;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人归属行业", sql = "lvrg_blg_idt = $S{lvrgBlgIdt}" ,field = "lvrg_blg_idt")
   private String lvrgBlgIdt;
   @GraphQLField(kkhtml = "KFieldText", label = "融资项目归属行业", sql = "lvrg_prj_blg_idt = $S{lvrgPrjBlgIdt}" ,field = "lvrg_prj_blg_idt")
   private String lvrgPrjBlgIdt;
   @GraphQLField(kkhtml = "KFieldText", label = "项目归属重点监控行业和领域标识", sql = "prj_blg_key_mnt_idt = $S{prjBlgKeyMntIdt}" ,field = "prj_blg_key_mnt_idt")
   private String prjBlgKeyMntIdt;
   @GraphQLField(kkhtml = "KFieldText", label = "重点监控行业和领域类别", sql = "key_mnt_idt_typ = $S{keyMntIdtTyp}" ,field = "key_mnt_idt_typ")
   private String keyMntIdtTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "重点监控行业和领域类别说明", sql = "key_mnt_idt_typ_cmt = $S{keyMntIdtTypCmt}" ,field = "key_mnt_idt_typ_cmt")
   private String keyMntIdtTypCmt;
   @GraphQLField(kkhtml = "KFieldText", label = "担保方式", sql = "grnt_mth = $S{grntMth}" ,field = "grnt_mth")
   private String grntMth;
   @GraphQLField(kkhtml = "KFieldText", label = "担保情况说明", sql = "grnt_sts_cmt = $S{grntStsCmt}" ,field = "grnt_sts_cmt")
   private String grntStsCmt;
   @GraphQLField(kkhtml = "KFieldText", label = "抵质押物类型", sql = "plg_typ = $S{plgTyp}" ,field = "plg_typ")
   private String plgTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "抵质押物价值", sql = "plg_val = $S{plgVal}" ,field = "plg_val")
   private String plgVal;
   @GraphQLField(kkhtml = "KFieldText", label = "担保性质", sql = "grnt_chr = $S{grntChr}" ,field = "grnt_chr")
   private String grntChr;
   @GraphQLField(kkhtml = "KFieldText", label = "担保人与融资人关系", sql = "grnt_lvrg_rel = $S{grntLvrgRel}" ,field = "grnt_lvrg_rel")
   private String grntLvrgRel;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人主体评级", sql = "grnt_main_rat = $S{grntMainRat}" ,field = "grnt_main_rat")
   private String grntMainRat;
   @GraphQLField(kkhtml = "KFieldText", label = "资产内部评级", sql = "ast_in_rat = $S{astInRat}" ,field = "ast_in_rat")
   private String astInRat;
   @GraphQLField(kkhtml = "KFieldText", label = "资产外部评级", sql = "ast_out_rat = $S{astOutRat}" ,field = "ast_out_rat")
   private String astOutRat;
   @GraphQLField(kkhtml = "KFieldText", label = "含权类型", sql = "emb_opt_typ = $S{embOptTyp}" ,field = "emb_opt_typ")
   private String embOptTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "行权方式", sql = "xcs_rit_mth = $S{xcsRitMth}" ,field = "xcs_rit_mth")
   private String xcsRitMth;
   @GraphQLField(kkhtml = "KFieldText", label = "固定行权日", sql = "fix_xcs_rit_dt = $S{fixXcsRitDt}" ,field = "fix_xcs_rit_dt")
   private String fixXcsRitDt;
   @GraphQLField(kkhtml = "KFieldText", label = "首次行权日期", sql = "frs_fix_xcs_dt = $S{frsFixXcsDt}" ,field = "frs_fix_xcs_dt")
   private String frsFixXcsDt;
   @GraphQLField(kkhtml = "KFieldText", label = "行权周期", sql = "xcs_rit_prd = $S{xcsRitPrd}" ,field = "xcs_rit_prd")
   private String xcsRitPrd;
   @GraphQLField(kkhtml = "KFieldText", label = "行权价格", sql = "xcs_rit_prc = $S{xcsRitPrc}" ,field = "xcs_rit_prc")
   private String xcsRitPrc;
   @GraphQLField(kkhtml = "KFieldText", label = "永续条款类型", sql = "perp_typ = $S{perpTyp}" ,field = "perp_typ")
   private String perpTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "利息递延条款类型", sql = "intr_ppn_typ = $S{intrPpnTyp}" ,field = "intr_ppn_typ")
   private String intrPpnTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "递延利息计息标识", sql = "ppn_intr_intr_f = $S{ppnIntrIntrF}" ,field = "ppn_intr_intr_f")
   private String ppnIntrIntrF;
   @GraphQLField(kkhtml = "KFieldText", label = "首次重定价日期", sql = "frs_rprc_dt = $S{frsRprcDt}" ,field = "frs_rprc_dt")
   private String frsRprcDt;
   @GraphQLField(kkhtml = "KFieldText", label = "重定价周期", sql = "rprc_prd = $S{rprcPrd}" ,field = "rprc_prd")
   private String rprcPrd;
   @GraphQLField(kkhtml = "KFieldText", label = "部分赎回标识", sql = "part_rdm_f = $S{partRdmF}" ,field = "part_rdm_f")
   private String partRdmF;
   @GraphQLField(kkhtml = "KFieldText", label = "部分赎回比例", sql = "part_rdm_rto = $S{partRdmRto}" ,field = "part_rdm_rto")
   private String partRdmRto;
   @GraphQLField(kkhtml = "KFieldText", label = "选择权", sql = "chc_rit = $S{chcRit}" ,field = "chc_rit")
   private String chcRit;
   @GraphQLField(kkhtml = "KFieldText", label = "行权条件说明", sql = "xcs_rit_cond_cmt = $S{xcsRitCondCmt}" ,field = "xcs_rit_cond_cmt")
   private String xcsRitCondCmt;
   @GraphQLField(kkhtml = "KFieldText", label = "所属地区", sql = "lvrg_blg_zon = $S{lvrgBlgZon}" ,field = "lvrg_blg_zon")
   private String lvrgBlgZon;
   @GraphQLField(kkhtml = "KFieldText", label = "增信机构代码", sql = "inc_crd_org_cd = $S{incCrdOrgCd}" ,field = "inc_crd_org_cd")
   private String incCrdOrgCd;
   @GraphQLField(kkhtml = "KFieldText", label = "增信机构名称", sql = "inc_crd_org_nm = $S{incCrdOrgNm}" ,field = "inc_crd_org_nm")
   private String incCrdOrgNm;
   @GraphQLField(kkhtml = "KFieldText", label = "融资总费率", sql = "lvrg_tot_fee = $S{lvrgTotFee}" ,field = "lvrg_tot_fee")
   private String lvrgTotFee;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人组织机构（社会信用）代码", sql = "lvrg_org_org_cd = $S{lvrgOrgOrgCd}" ,field = "lvrg_org_org_cd")
   private String lvrgOrgOrgCd;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "cmt = $S{cmt}" ,field = "cmt")
   private String cmt;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
   private String updDt;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
    @GraphQLField(kkhtml = "KFieldText", label = "品种代码", sql = "bred_cd = $S{bredCd}" ,field = "bred_cd")
    private String bredCd;
   @GraphQLField(kkhtml = "KFieldText", label = "交易流通场所", sql = "trx_pla = $S{trxPla}" ,field = "trx_pla")
   private String trxPla;
   @GraphQLField(kkhtml = "KFieldText", label = "规则付息标识", sql = "rul_pay_intr_f = $S{rulPayIntrF}" ,field = "rul_pay_intr_f")
   private String rulPayIntrF;
   @GraphQLField(kkhtml = "KFieldText", label = "表内外城商行投资分类", sql = "invest_type = $S{investType}" ,field = "invest_type")
   private String investType;



}