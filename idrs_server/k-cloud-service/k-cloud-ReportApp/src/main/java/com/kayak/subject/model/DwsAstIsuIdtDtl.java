package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsAstIsuIdtDtlService",table = "dws_ast_isu_idt_dtl")
@Data
public class DwsAstIsuIdtDtl {

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
   @GraphQLField(kkhtml = "KFieldText", label = "发行人名称", sql = "isu_org_nm = $S{isuOrgNm}" ,field = "isu_org_nm")
   private String isuOrgNm;
   @GraphQLField(kkhtml = "KFieldText", label = "发行人行业", sql = "isu_org_idt = $S{isuOrgIdt}" ,field = "isu_org_idt")
   private String isuOrgIdt;
   @GraphQLField(kkhtml = "KFieldText", label = "发行人规模", sql = "isu_org_vol = $S{isuOrgVol}" ,field = "isu_org_vol")
   private String isuOrgVol;
   @GraphQLField(kkhtml = "KFieldText", label = "市值", sql = "mkt_vol = $S{mktVol}" ,field = "mkt_vol")
   private String mktVol;
}