package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsMonthPrdDtlService",table = "dws_month_prd_dtl")
@Data
public class DwsMonthPrdDtl {

   @ExcelProperty(value = "ID")
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @ExcelProperty(value = "数据日期")
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期(月末自然日)", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @ExcelProperty(value = "产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prdc_cd = $S{prdcCd}" ,field = "prdc_cd")
   private String prdcCd;
   @ExcelProperty(value = "本期总募集金额")
   @GraphQLField(kkhtml = "KFieldText", label = "本期总募集金额(元)", sql = "tot_raise_tt = $S{totRaiseTt}" ,field = "tot_raise_tt")
   private String totRaiseTt;
   @ExcelProperty(value = "本期净募集金额")
   @GraphQLField(kkhtml = "KFieldText", label = "本期净募集金额(元)", sql = "net_raise_tt = $S{netRaiseTt}" ,field = "net_raise_tt")
   private String netRaiseTt;
   @ExcelProperty(value = "期末余额")
   @GraphQLField(kkhtml = "KFieldText", label = "期末余额(元)", sql = "bal_et = $S{balEt}" ,field = "bal_et")
   private String balEt;
   @ExcelProperty(value = "本期兑付金额")
   @GraphQLField(kkhtml = "KFieldText", label = "本期兑付金额(元)", sql = "cur_pay_tt = $S{curPayTt}" ,field = "cur_pay_tt")
   private String curPayTt;
   @ExcelProperty(value = "本期客户端实现收益总额")
   @GraphQLField(kkhtml = "KFieldText", label = "本期客户端实现收益总额(元)", sql = "inv_acv_yield_tt = $S{invAcvYieldTt}" ,field = "inv_acv_yield_tt")
   private String invAcvYieldTt;
   @ExcelProperty(value = "本期银行端实现收益总额")
   @GraphQLField(kkhtml = "KFieldText", label = "本期银行端实现收益总额(元)", sql = "bnk_acv_yield_tt = $S{bnkAcvYieldTt}" ,field = "bnk_acv_yield_tt")
   private String bnkAcvYieldTt;
   @ExcelProperty(value = "募集方式")
   @GraphQLField(kkhtml = "KFieldText", label = "产品募集方式", sql = "raise_type = $S{raiseType}" ,field = "raise_type")
   private String raiseType;
   @ExcelProperty(value = "投资性质")
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资性质", sql = "inv_prop = $S{invProp}" ,field = "inv_prop")
   private String invProp;
   @ExcelProperty(value = "运作模式")
   @GraphQLField(kkhtml = "KFieldText", label = "产品运作模式", sql = "opt_mod = $S{optMod}" ,field = "opt_mod")
   private String optMod;
   @ExcelProperty(value = "产品期限")
   @GraphQLField(kkhtml = "KFieldText", label = "产品期限(中债)", sql = "prd_trm = $S{prdTrm}" ,field = "prd_trm")
   private String prdTrm;
   @ExcelProperty(value = "自然人")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(自然人)(元)", sql = "inv_hld_ntr = $S{invHldNtr}" ,field = "inv_hld_ntr")
   private String invHldNtr;
   @ExcelProperty(value = "法人或其他组织")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(法人或其他组织)(元)", sql = "inv_hld_lg_org = $S{invHldLgOrg}" ,field = "inv_hld_lg_org")
   private String invHldLgOrg;
   @ExcelProperty(value = "非金融机构")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(非金融机构)(元)", sql = "inv_hld_non_fnc_org = $S{invHldNonFncOrg}" ,field = "inv_hld_non_fnc_org")
   private String invHldNonFncOrg;
   @ExcelProperty(value = "银行类金融机构")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(银行类金融机构)(元)", sql = "inv_hld_bnk_fnc_org = $S{invHldBnkFncOrg}" ,field = "inv_hld_bnk_fnc_org")
   private String invHldBnkFncOrg;
   @ExcelProperty(value = "保险业金融机构")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(保险业金融机构)(元)", sql = "inv_hld_isr_fnc_org = $S{invHldIsrFncOrg}" ,field = "inv_hld_isr_fnc_org")
   private String invHldIsrFncOrg;
   @ExcelProperty(value = "信托公司")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(信托公司)(元)", sql = "inv_hld_tst_cpn = $S{invHldTstCpn}" ,field = "inv_hld_tst_cpn")
   private String invHldTstCpn;
   @ExcelProperty(value = "证券公司")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(证券公司)(元)", sql = "inv_hld_sct_cpn = $S{invHldSctCpn}" ,field = "inv_hld_sct_cpn")
   private String invHldSctCpn;
   @ExcelProperty(value = "基金公司")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(基金公司)(元)", sql = "inv_hld_fnd_cpn = $S{invHldFndCpn}" ,field = "inv_hld_fnd_cpn")
   private String invHldFndCpn;
   @ExcelProperty(value = "其他金融机构")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(其他金融机构)(元)", sql = "inv_hld_otr_fnc_org = $S{invHldOtrFncOrg}" ,field = "inv_hld_otr_fnc_org")
   private String invHldOtrFncOrg;
   @ExcelProperty(value = "金融机构资产管理产品")
   @GraphQLField(kkhtml = "KFieldText", label = "投资者持有(金融机构资产管理产品)(元)", sql = "inv_hld_fnc_org_prd = $S{invHldFncOrgPrd}" ,field = "inv_hld_fnc_org_prd")
   private String invHldFncOrgPrd;
   @ExcelProperty(value = "人民币")
   @GraphQLField(kkhtml = "KFieldText", label = "按币种划分(人民币)", sql = "cur_cny = $S{curCny}" ,field = "cur_cny")
   private String curCny;
   @ExcelProperty(value = "美元")
   @GraphQLField(kkhtml = "KFieldText", label = "按币种划分(美元)", sql = "cur_usd = $S{curUsd}" ,field = "cur_usd")
   private String curUsd;
   @ExcelProperty(value = "欧元")
   @GraphQLField(kkhtml = "KFieldText", label = "按币种划分(欧元)", sql = "cur_eur = $S{curEur}" ,field = "cur_eur")
   private String curEur;
   @ExcelProperty(value = "其他币种")
   @GraphQLField(kkhtml = "KFieldText", label = "按币种划分(其他币种)", sql = "cur_otr = $S{curOtr}" ,field = "cur_otr")
   private String curOtr;
   @ExcelProperty(value = "是否现金管理类")
   @GraphQLField(kkhtml = "KFieldText", label = "是否现金管理类产品", sql = "csh_mng_f = $S{cshMngF}" ,field = "csh_mng_f")
   private String cshMngF;
   @ExcelProperty(value = "管理方式")
   @GraphQLField(kkhtml = "KFieldText", label = "管理方式", sql = "mng_mth = $S{mngMth}" ,field = "mng_mth")
   private String mngMth;
   @ExcelProperty(value = "估值方式")
   @GraphQLField(kkhtml = "KFieldText", label = "估值方式", sql = "vltn_mthd = $S{vltnMthd}" ,field = "vltn_mthd")
   private String vltnMthd;
   @ExcelProperty(value = "是否金融同业专属")
   @GraphQLField(kkhtml = "KFieldText", label = "是否金融同业专属", sql = "blg_fin_sam_bus_f = $S{blgFinSamBusF}" ,field = "blg_fin_sam_bus_f")
   private String blgFinSamBusF;
   @ExcelProperty(value = "平均剩余期限")
   @GraphQLField(kkhtml = "KFieldText", label = "平均剩余期限", sql = "avg_rmn_trm = $S{avgRmnTrm}" ,field = "avg_rmn_trm")
   private String avgRmnTrm;
   @ExcelProperty(value = "是否从母行划转")
   @GraphQLField(kkhtml = "KFieldText", label = "是否从母行划转", sql = "is_prod_tsf = $S{isProdTsf}" ,field = "is_prod_tsf")
   private String isProdTsf;
   @ExcelProperty(value = "划转日的成立金额")
   @GraphQLField(kkhtml = "KFieldText", label = "划转日的成立金额(元)", sql = "tsf_fnd_amt = $S{tsfFndAmt}" ,field = "tsf_fnd_amt")
   private String tsfFndAmt;
   @ExcelProperty(value = "是否为养老产品")
   @GraphQLField(kkhtml = "KFieldText", label = "是否养老产品", sql = "pen_inv_prd_f = $S{penInvPrdF}" ,field = "pen_inv_prd_f")
   private String penInvPrdF;
   @ExcelProperty(value = "是否个人养老金产品")
   @GraphQLField(kkhtml = "KFieldText", label = "是否养老产品", sql = "per_pen_inv_prod_f = $S{perPenInvProdF}" ,field = "per_pen_inv_prod_f")
   private String perPenInvProdF;
   @ExcelProperty(value = "单月年化收益")
   @GraphQLField(kkhtml = "KFieldText", label = "单月年化收益率", sql = "mth_anl_yield = $S{mthAnlYield}" ,field = "mth_anl_yield")
   private String mthAnlYield;
   @ExcelProperty(value = "加权价格")
   @GraphQLField(kkhtml = "KFieldText", label = "加权价格", sql = "wgt_price = $S{wgtPrice}" ,field = "wgt_price")
   private String wgtPrice;
   @ExcelProperty(value = "风险等级")
   @GraphQLField(kkhtml = "KFieldText", label = "风险等级", sql = "rsk_lev = $S{rskLev}" ,field = "rsk_lev")
   private String rskLev;
   @ExcelProperty(value = "是否封闭期在半年以上")
   @GraphQLField(kkhtml = "KFieldText", label = "是否封闭期在半年以上", sql = "is_seal_prd_past = $S{isSealPrdPast}" ,field = "is_seal_prd_past")
   private String isSealPrdPast;
   @ExcelProperty(value = "起息日")
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "found_dt = $S{foundDt}" ,field = "found_dt")
   private String foundDt;
   @ExcelProperty(value = "到期日")
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "mtu_dt = $S{mtuDt}" ,field = "mtu_dt")
   private String mtuDt;
   @ExcelProperty(value = "创建日期")
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @ExcelProperty(value = "创建时间")
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_tm = $S{crtTm}" ,field = "crt_tm")
   private String crtTm;
   @ExcelProperty(value = "业务日期")
   @GraphQLField(kkhtml = "KFieldText", label = "业务日期", sql = "ywrq = $S{ywrq}" ,field = "ywrq")
   private String ywrq;

}