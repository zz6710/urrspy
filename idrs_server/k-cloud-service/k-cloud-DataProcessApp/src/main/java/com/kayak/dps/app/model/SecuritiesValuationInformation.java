package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "securitiesValuationInformationService",table = "dwd_ast_csi_val_inf")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecuritiesValuationInformation {
   @GraphQLField(kkhtml = "KFieldText", label = "证券编号", sql = "scr_id = $S{scrId}" ,field = "scr_id")
   private String scrId;
   @GraphQLField(kkhtml = "KFieldText", label = "证券代码", sql = "d1.scr_cd like '%$U{scrCd}%'" ,field = "scr_cd")
   private String scrCd;
   @GraphQLField(kkhtml = "KFieldText", label = "交易市场", sql = "d1.trx_mkt = $S{trxMkt}" ,field = "trx_mkt")
   private String trxMkt;
   @GraphQLField(kkhtml = "KFieldText", label = "交易日期", sql = "trx_dt = $S{trxDt}" ,field = "trx_dt")
   private String trxDt;
   @GraphQLField(kkhtml = "KFieldText", label = "信用类型", sql = "crd_typ = $S{crdTyp}" ,field = "crd_typ")
   private String crdTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "发行市场数", sql = "isu_mkt_qty = $S{isuMktQty}" ,field = "isu_mkt_qty")
   private String isuMktQty;
   @GraphQLField(kkhtml = "KFieldText", label = "计算价格", sql = "calc_prc = $S{calcPrc}" ,field = "calc_prc")
   private String calcPrc;
   @GraphQLField(kkhtml = "KFieldText", label = "计算收益率", sql = "calc_ern_rat = $S{calcErnRat}" ,field = "calc_ern_rat")
   private String calcErnRat;
   @GraphQLField(kkhtml = "KFieldText", label = "修正久期", sql = "modi_dura = $S{modiDura}" ,field = "modi_dura")
   private String modiDura;
   @GraphQLField(kkhtml = "KFieldText", label = "凸性", sql = "cvxt = $S{cvxt}" ,field = "cvxt")
   private String cvxt;
   @GraphQLField(kkhtml = "KFieldText", label = "净价", sql = "net_prc = $S{netPrc}" ,field = "net_prc")
   private String netPrc;
   @GraphQLField(kkhtml = "KFieldText", label = "应计利息", sql = "acr_intr = $S{acrIntr}" ,field = "acr_intr")
   private String acrIntr;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
   private String updDt;

   //证券名称
   @GraphQLField(label = "证券名称")
   private String scrNm;
   //证券简称
   @GraphQLField(label = "证券简称")
   private String scrShtNm;
   @GraphQLField(kkhtml = "KFieldText", label = "开始日期")
   private String startDate;
   @GraphQLField(kkhtml = "KFieldText", label = "结束日期")
   private String endDate;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期")
   private String dealDate;


}