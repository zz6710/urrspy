package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsAstDebPbnkDtlService",table = "dws_ast_deb_pbnk_dtl")
@Data
public class DwsAstDebPbnkDtl {

   @ExcelIgnore
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;

   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date like '%$U{reportDate}%'" ,field = "report_date")
   private String reportDate;

   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd like '%$U{prodCd}%'" ,field = "prod_cd")
   private String prodCd;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_nm = $S{prodNm}" ,field = "prod_nm")
   private String prodNm;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "scr_cd like '%$U{scrCd}%'" ,field = "scr_cd")
   private String scrCd;
   @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "scr_nm = $S{scrNm}" ,field = "scr_nm")
   private String scrNm;
   @GraphQLField(kkhtml = "KFieldText", label = "资产三类", sql = "asst_thr_knd = $S{asstThrKnd}" ,field = "asst_thr_knd")
   private String asstThrKnd;
   @GraphQLField(kkhtml = "KFieldText", label = "资产方/负债方", sql = "asst_type = $S{asstType}" ,field = "asst_type")
   private String asstType;
   @GraphQLField(kkhtml = "KFieldText", label = "资产类型", sql = "asst_clss = $S{asstClss}" ,field = "asst_clss")
   private String asstClss;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "mtu_dt = $S{mtuDt}" ,field = "mtu_dt")
   private String mtuDt;
   @GraphQLField(kkhtml = "KFieldText", label = "剩余期限", sql = "prod_trm_pbnk = $S{prodTrmPbnk}" ,field = "prod_trm_pbnk")
   private String prodTrmPbnk;
   @GraphQLField(kkhtml = "KFieldText", label = "市值", sql = "mkt_vol = $S{mktVol}" ,field = "mkt_vol")
   private String mktVol;
}