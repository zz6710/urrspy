package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsCounterPartyInfoService",table = "dws_counter_party_info")
@Data
public class DwsCounterPartyInfo {

   @ExcelIgnore
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @ExcelProperty(value = "产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
   private String prodCd;
   @ExcelProperty(value = "内部产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "内部产品代码", sql = "prod_intr_cd = $S{prodIntrCd}" ,field = "prod_intr_cd")
   private String prodIntrCd;
   @ExcelProperty(value = "资产负债项目")
   @GraphQLField(kkhtml = "KFieldText", label = "资产负债项目", sql = "bred_cd = $S{bredCd}" ,field = "bred_cd")
   private String bredCd;
   @ExcelProperty(value = "资产代码")
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "asset_cd = $S{assetCd}" ,field = "asset_cd")
   private String assetCd;
   @ExcelProperty(value = "交易对手产品种类")
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手产品种类", sql = "cntr_prod_type = $S{cntrProdType}" ,field = "cntr_prod_type")
   private String cntrProdType;
   @ExcelProperty(value = "交易对手机构编码")
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手机构编码", sql = "cntr_org_cd = $S{cntrOrgCd}" ,field = "cntr_org_cd")
   private String cntrOrgCd;
   @ExcelProperty(value = "交易对手机构名称")
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手机构名称", sql = "cntr_org_nm = $S{cntrOrgNm}" ,field = "cntr_org_nm")
   private String cntrOrgNm;
   @ExcelProperty(value = "交易对手产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手产品代码", sql = "cntr_prod_cd = $S{cntrProdCd}" ,field = "cntr_prod_cd")
   private String cntrProdCd;
   @ExcelProperty(value = "交易对手产品名称")
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手产品名称", sql = "cntr_prod_nm = $S{cntrProdNm}" ,field = "cntr_prod_nm")
   private String cntrProdNm;
   @ExcelProperty(value = "币种")
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "ccy_cd = $S{ccyCd}" ,field = "ccy_cd")
   private String ccyCd;
   @ExcelProperty(value = "期末金额")
   @GraphQLField(kkhtml = "KFieldText", label = "期末金额", sql = "amt_bal = $S{amtBal}" ,field = "amt_bal")
   private String amtBal;
   @ExcelProperty(value = "期末金额折人民币")
   @GraphQLField(kkhtml = "KFieldText", label = "期末金额折人民币", sql = "amt_bal_cny = $S{amtBalCny}" ,field = "amt_bal_cny")
   private String amtBalCny;
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
   @GraphQLField(kkhtml = "KFieldText", label = "状态", sql = "status = $S{status}" ,field = "status")
   private String status;
   @GraphQLField(kkhtml = "KFieldText", label = "异常描述", sql = "exception = $S{exception}" ,field = "exception")
   private String exception;
}