package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsAstMngPlanInfoService",table = "dws_ast_mng_plan_info")
@Data
public class DwsAstMngPlanInfo {

   @ExcelIgnore
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @ExcelProperty(value = "产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
   private String prodCd;
   @ExcelProperty(value = "产品名称")
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_nm = $S{prodNm}" ,field = "prod_nm")
   private String prodNm;
   @ExcelProperty(value = "发行机构代码")
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "issuer_orgn_cd = $S{issuerOrgnCd}" ,field = "issuer_orgn_cd")
   private String issuerOrgnCd;
   @ExcelProperty(value = "发行机构名称")
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构名称", sql = "issuer_orgn_nm = $S{issuerOrgnNm}" ,field = "issuer_orgn_nm")
   private String issuerOrgnNm;
   @ExcelProperty(value = "产品品种名称")
   @GraphQLField(kkhtml = "KFieldText", label = "产品品种名称", sql = "prod_bred_cd = $S{prodBredCd}" ,field = "prod_bred_cd")
   private String prodBredCd;
   @ExcelProperty(value = "产品起始日期")
   @GraphQLField(kkhtml = "KFieldText", label = "产品起始日期", sql = "prod_opn_dt = $S{prodOpnDt}" ,field = "prod_opn_dt")
   private String prodOpnDt;
   @ExcelProperty(value = "产品变更日期")
   @GraphQLField(kkhtml = "KFieldText", label = "产品变更日期", sql = "prod_up_dt = $S{prodUpDt}" ,field = "prod_up_dt")
   private String prodUpDt;
   @ExcelProperty(value = "产品预计终止日期")
   @GraphQLField(kkhtml = "KFieldText", label = "产品预计终止日期", sql = "prod_expc_end_dt = $S{prodExpcEndDt}" ,field = "prod_expc_end_dt")
   private String prodExpcEndDt;
   @ExcelProperty(value = "产品实际终止日期")
   @GraphQLField(kkhtml = "KFieldText", label = "产品实际终止日期", sql = "prod_actl_end_dt = $S{prodActlEndDt}" ,field = "prod_actl_end_dt")
   private String prodActlEndDt;
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