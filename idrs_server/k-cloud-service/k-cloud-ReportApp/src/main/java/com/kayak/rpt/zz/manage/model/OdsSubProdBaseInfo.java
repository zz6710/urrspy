package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "odsSubProdBaseInfoService")
@Data
public class OdsSubProdBaseInfo {

   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
   private String prodName;
   @GraphQLField(kkhtml = "KFieldText", label = "产品状态", sql = "run_stat = $S{runStat}" ,field = "run_stat")
   private String runStat;
   @GraphQLField(kkhtml = "KFieldText", label = "母产品代码", sql = "mother_fund_code = $S{motherFundCode}" ,field = "mother_fund_code")
   private String motherFundCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品类型", sql = "prod_type = $S{prodType}" ,field = "prod_type")
   private String prodType;
   @GraphQLField(kkhtml = "KFieldText", label = "募集起始日", sql = "subscr_sd_earliest between $S{mdDateRangeStart} and $S{mdDateRangeEnd}" ,field = "subscr_sd_earliest")
   private String subscrSdEarliest;
   @GraphQLField(kkhtml = "KFieldText", label = "募集结束日", sql = "subscr_ed_latest = $S{subscrEdLatest}" ,field = "subscr_ed_latest")
   private String subscrEdLatest;
   @GraphQLField(kkhtml = "KFieldText", label = "产品成立日", sql = "establish_date between $S{sdDateRangeStart} and $S{sdDateRangeEnd}" ,field = "establish_date")
   private String establishDate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品到期日", sql = "real_end_date between $S{edDateRangeStart} and $S{edDateRangeEnd}" ,field = "real_end_date")
   private String realEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准类型", sql = "perfm_benchm_type = $S{perfmBenchmType}" ,field = "perfm_benchm_type")
   private String perfmBenchmType;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准上限", sql = "perfm_benchm_upper = $S{perfmBenchmUpper}" ,field = "perfm_benchm_upper")
   private String perfmBenchmUpper;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准下限", sql = "perfm_benchm_lower = $S{perfmBenchmLower}" ,field = "perfm_benchm_lower")
   private String perfmBenchmLower;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准描述", sql = "yjbjjzsm_pj = $S{yjbjjzsmPj}" ,field = "yjbjjzsm_pj")
   private String yjbjjzsmPj;
   @GraphQLField(kkhtml = "KFieldText", label = "是否ESG主题", sql = "is_esg = $S{isEsg}" ,field = "is_esg")
   private String isEsg;
   @GraphQLField(kkhtml = "KFieldText", label = "是否普惠主题", sql = "is_inclusive = $S{isInclusive}" ,field = "is_inclusive")
   private String isInclusive;
   @GraphQLField(kkhtml = "KFieldText", label = "是否养老主题", sql = "is_pension = $S{isPension}" ,field = "is_pension")
   private String isPension;
   @GraphQLField(kkhtml = "KFieldText", label = "是否养老理财", sql = "pen_inv_prod_f = $S{penInvProdF}" ,field = "pen_inv_prod_f")
   private String penInvProdF;
   @GraphQLField(kkhtml = "KFieldText", label = "是否个人养老金理财", sql = "per_pen_inv_prod_f = $S{perPenInvProdF}" ,field = "per_pen_inv_prod_f")
   private String perPenInvProdF;
   @GraphQLField
   private String mdDateRangeStart;
   @GraphQLField
   private String mdDateRangeEnd;
   @GraphQLField
   private String sdDateRangeStart;
   @GraphQLField
   private String sdDateRangeEnd;
   @GraphQLField
   private String edDateRangeStart;
   @GraphQLField
   private String edDateRangeEnd;

}