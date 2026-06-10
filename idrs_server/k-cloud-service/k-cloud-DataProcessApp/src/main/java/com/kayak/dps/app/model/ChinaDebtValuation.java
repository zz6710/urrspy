package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "chinaDebtValuationService",table = "dwd_ast_cnbd_val_inf")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChinaDebtValuation {
   @GraphQLField(kkhtml = "KFieldText", label = "证券编号", sql = "scr_id = $S{scrId}" ,field = "scr_id")
   private String scrId;
   @GraphQLField(kkhtml = "KFieldText", label = "证券代码", sql = "d1.scr_cd like '%$U{scrCd}%'" ,field = "scr_cd")
   private String scrCd;
   //证券名称
   @GraphQLField(label = "证券名称")
   private String scrNm;
   //证券简称
   @GraphQLField(label = "证券简称")
   private String scrShtNm;
   @GraphQLField(kkhtml = "KFieldText", label = "交易市场", sql = "d1.trx_mkt = $S{trxMkt}" ,field = "trx_mkt")
   private String trxMkt;
   @GraphQLField(kkhtml = "KFieldText", label = "交易日期", sql = "trx_dt = $S{trxDt}" ,field = "trx_dt")
   private String trxDt;
   @GraphQLField(kkhtml = "KFieldText", label = "待偿期", sql = "cps_prd = $S{cpsPrd}" ,field = "cps_prd")
   private String cpsPrd;
   @GraphQLField(kkhtml = "KFieldText", label = "估价全价", sql = "eval_full_prc = $S{evalFullPrc}" ,field = "eval_full_prc")
   private String evalFullPrc;
   @GraphQLField(kkhtml = "KFieldText", label = "应计利息", sql = "acr_intr = $S{acrIntr}" ,field = "acr_intr")
   private String acrIntr;
   @GraphQLField(kkhtml = "KFieldText", label = "估价净价", sql = "eval_net_prc = $S{evalNetPrc}" ,field = "eval_net_prc")
   private String evalNetPrc;
   @GraphQLField(kkhtml = "KFieldText", label = "估价收益率", sql = "eval_ern_rat = $S{evalErnRat}" ,field = "eval_ern_rat")
   private String evalErnRat;
   @GraphQLField(kkhtml = "KFieldText", label = "估价修正久期", sql = "eval_modi_dura = $S{evalModiDura}" ,field = "eval_modi_dura")
   private String evalModiDura;
   @GraphQLField(kkhtml = "KFieldText", label = "估价凸性", sql = "eval_cvxt = $S{evalCvxt}" ,field = "eval_cvxt")
   private String evalCvxt;
   @GraphQLField(kkhtml = "KFieldText", label = "估价基点价值", sql = "eval_bas_pnt_val = $S{evalBasPntVal}" ,field = "eval_bas_pnt_val")
   private String evalBasPntVal;
   @GraphQLField(kkhtml = "KFieldText", label = "估价利差久期", sql = "eval_sprd_dura = $S{evalSprdDura}" ,field = "eval_sprd_dura")
   private String evalSprdDura;
   @GraphQLField(kkhtml = "KFieldText", label = "估价利差凸性", sql = "eval_sprd_cvxt = $S{evalSprdCvxt}" ,field = "eval_sprd_cvxt")
   private String evalSprdCvxt;
   @GraphQLField(kkhtml = "KFieldText", label = "日终应计利息", sql = "cls_acr_intr = $S{clsAcrIntr}" ,field = "cls_acr_intr")
   private String clsAcrIntr;
   @GraphQLField(kkhtml = "KFieldText", label = "市场全价", sql = "mkt_full_prc = $S{mktFullPrc}" ,field = "mkt_full_prc")
   private String mktFullPrc;
   @GraphQLField(kkhtml = "KFieldText", label = "市场净价", sql = "mkt_net_prc = $S{mktNetPrc}" ,field = "mkt_net_prc")
   private String mktNetPrc;
   @GraphQLField(kkhtml = "KFieldText", label = "市场收益率", sql = "mkt_ern_rat = $S{mktErnRat}" ,field = "mkt_ern_rat")
   private String mktErnRat;
   @GraphQLField(kkhtml = "KFieldText", label = "市场修正久期", sql = "mkt_modi_dura = $S{mktModiDura}" ,field = "mkt_modi_dura")
   private String mktModiDura;
   @GraphQLField(kkhtml = "KFieldText", label = "市场凸性", sql = "mkt_cvxt = $S{mktCvxt}" ,field = "mkt_cvxt")
   private String mktCvxt;
   @GraphQLField(kkhtml = "KFieldText", label = "市场基点价值", sql = "mkt_bas_pnt_val = $S{mktBasPntVal}" ,field = "mkt_bas_pnt_val")
   private String mktBasPntVal;
   @GraphQLField(kkhtml = "KFieldText", label = "市场利差久期", sql = "mkt_sprd_dura = $S{mktSprdDura}" ,field = "mkt_sprd_dura")
   private String mktSprdDura;
   @GraphQLField(kkhtml = "KFieldText", label = "市场利差凸性", sql = "mkt_sprd_cvxt = $S{mktSprdCvxt}" ,field = "mkt_sprd_cvxt")
   private String mktSprdCvxt;
   @GraphQLField(kkhtml = "KFieldText", label = "估价利率久期", sql = "eval_rat_dura = $S{evalRatDura}" ,field = "eval_rat_dura")
   private String evalRatDura;
   @GraphQLField(kkhtml = "KFieldText", label = "估价利率凸性", sql = "eval_rat_cvxt = $S{evalRatCvxt}" ,field = "eval_rat_cvxt")
   private String evalRatCvxt;
   @GraphQLField(kkhtml = "KFieldText", label = "市场利率久期", sql = "mkt_rat_dura = $S{mktRatDura}" ,field = "mkt_rat_dura")
   private String mktRatDura;
   @GraphQLField(kkhtml = "KFieldText", label = "市场利率凸性", sql = "mkt_rat_cvxt = $S{mktRatCvxt}" ,field = "mkt_rat_cvxt")
   private String mktRatCvxt;
   @GraphQLField(kkhtml = "KFieldText", label = "日终估价全价", sql = "cls_eval_full_prc = $S{clsEvalFullPrc}" ,field = "cls_eval_full_prc")
   private String clsEvalFullPrc;
   @GraphQLField(kkhtml = "KFieldText", label = "点差收益率", sql = "sprd_ern_rat = $S{sprdErnRat}" ,field = "sprd_ern_rat")
   private String sprdErnRat;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
   private String updDt;
   @GraphQLField(kkhtml = "KFieldText", label = "开始日期")
   private String startDate;
   @GraphQLField(kkhtml = "KFieldText", label = "结束日期")
   private String endDate;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期")
   private String dealDate;
}