package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsAstAllocationDtlService",table = "dws_ast_allocation_dtl")
@Data
public class DwsAstAllocationDtl {

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
   @GraphQLField(kkhtml = "KFieldText", label = "是否债务证券（金融）", sql = "fin_deb_sec = $S{finDebSec}" ,field = "fin_deb_sec")
   private String finDebSec;
   @GraphQLField(kkhtml = "KFieldText", label = "是否债务证券（非金融）", sql = "non_fin_deb_sec = $S{nonFinDebSec}" ,field = "non_fin_deb_sec")
   private String nonFinDebSec;
   @GraphQLField(kkhtml = "KFieldText", label = "评级", sql = "rat = $S{rat}" ,field = "rat")
   private String rat;
   @GraphQLField(kkhtml = "KFieldText", label = "非金融企业债分类", sql = "non_fin_lab = $S{nonFinLab}" ,field = "non_fin_lab")
   private String nonFinLab;
   @GraphQLField(kkhtml = "KFieldText", label = "银行资本补充工具分类", sql = "bank_cap_sup = $S{bankCapSup}" ,field = "bank_cap_sup")
   private String bankCapSup;
   @GraphQLField(kkhtml = "KFieldText", label = "市值", sql = "mkt_vol = $S{mktVol}" ,field = "mkt_vol")
   private String mktVol;

}