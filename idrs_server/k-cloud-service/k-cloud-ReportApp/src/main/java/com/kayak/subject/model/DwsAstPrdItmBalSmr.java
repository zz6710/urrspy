package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsAstPrdItmBalSmrService",table = "dws_ast_prd_itm_bal_smr")
@Data
public class DwsAstPrdItmBalSmr {

   @ExcelIgnore
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @ExcelProperty(value = "产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
   private String prodCd;
   @ExcelProperty(value = "内部产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "内部产品代码", sql = "prod_intr_cd = $S{prodIntrCd}" ,field = "prod_intr_cd")
   private String prodIntrCd;
   @ExcelProperty(value = "币种")
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "ccy_cd = $S{ccyCd}" ,field = "ccy_cd")
   private String ccyCd;
   @ExcelProperty(value = "数据种类")
   @GraphQLField(kkhtml = "KFieldText", label = "数据种类", sql = "ctg_cd = $S{ctgCd}" ,field = "ctg_cd")
   private String ctgCd;
   @ExcelProperty(value = "金额")
   @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "amt_bal = $S{amtBal}" ,field = "amt_bal")
   private String amtBal;
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

}