package com.kayak.subject.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsMonthNavInfService",table = "dws_month_nav_inf")
@Data
public class DwsMonthNavInf {

   @ExcelProperty(value = "ID")
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @ExcelProperty(value = "数据日期")
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @ExcelProperty(value = "产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "人行产品代码", sql = "prdc_cd_pbc = $S{prdcCdPbc}" ,field = "prdc_cd_pbc")
   private String prdcCdPbc;
   @ExcelProperty(value = "内部产品代码")
   @GraphQLField(kkhtml = "KFieldText", label = "内部产品代码", sql = "prdc_cd = $S{prdcCd}" ,field = "prdc_cd")
   private String prdcCd;
   @ExcelProperty(value = "期末净值")
   @GraphQLField(kkhtml = "KFieldText", label = "期末净值", sql = "unt_nav = $S{untNav}" ,field = "unt_nav")
   private String untNav;
   @ExcelProperty(value = "期末累计净值")
   @GraphQLField(kkhtml = "KFieldText", label = "期末累计净值", sql = "acm_nav = $S{acmNav}" ,field = "acm_nav")
   private String acmNav;
   @ExcelProperty(value = "当月年化收益率(%)")
   @GraphQLField(kkhtml = "KFieldText", label = "当月年化收益率(%)", sql = "rct_1m_grw_rat = $S{rct1mGrwRat}" ,field = "rct_1m_grw_rat")
   private String rct1mGrwRat;
   @ExcelProperty(value = "创建日期")
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @ExcelProperty(value = "创建时间")
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_tm = $S{crtTm}" ,field = "crt_tm")
   private String crtTm;
   @ExcelProperty(value = "剩余天数")
   @GraphQLField(kkhtml = "KFieldText", label = "剩余天数", sql = "remaining_days = $S{remainingDays}" ,field = "remaining_days")
   private String remainingDays;
   @ExcelProperty(value = "剩余期限")
   @GraphQLField(kkhtml = "KFieldText", label = "剩余期限", sql = "remaining_term = $S{remainingTerm}" ,field = "remaining_term")
   private String remainingTerm;

}