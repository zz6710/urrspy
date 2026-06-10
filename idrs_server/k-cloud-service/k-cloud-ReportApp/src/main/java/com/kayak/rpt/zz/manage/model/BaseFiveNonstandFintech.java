package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "baseFiveNonstandFintechService",table = "base_five_nonstand_fintech")
@Data
public class BaseFiveNonstandFintech {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "公司id", sql = "windid = $S{windid}" ,field = "windid")
   private String windid;
   @GraphQLField(kkhtml = "KFieldText", label = "万得统一机构编码", sql = "companycode like '%$U{companycode}%'" ,field = "companycode")
   private String companycode;
   @GraphQLField(kkhtml = "KFieldText", label = "五篇大文章所属行业领域", sql = "ssxyfield = $S{ssxyfield}" ,field = "ssxyfield")
   private String ssxyfield;
   @GraphQLField(kkhtml = "KFieldText", label = "五篇大文章所属行业代码", sql = "ssxycode = $S{ssxycode}" ,field = "ssxycode")
   private String ssxycode;
   @GraphQLField(kkhtml = "KFieldText", label = "五篇大文章所属行业分类名称", sql = "ssxyname = $S{ssxyname}" ,field = "ssxyname")
   private String ssxyname;
   @GraphQLField(kkhtml = "KFieldText", label = "统计局相关产业统计标准分类细项代码", sql = "nbstypecode = $S{nbstypecode}" ,field = "nbstypecode")
   private String nbstypecode;
   @GraphQLField(kkhtml = "KFieldText", label = "统计局相关产业统计标准分类细项名称", sql = "nbstypename = $S{nbstypename}" ,field = "nbstypename")
   private String nbstypename;
   @GraphQLField(kkhtml = "KFieldText", label = "国民经济行业代码", sql = "icneacode = $S{icneacode}" ,field = "icneacode")
   private String icneacode;
   @GraphQLField(kkhtml = "KFieldText", label = "国民经济行业名称", sql = "icneaname = $S{icneaname}" ,field = "icneaname")
   private String icneaname;
   @GraphQLField(kkhtml = "KFieldText", label = "置信度", sql = "confidence = $S{confidence}" ,field = "confidence")
   private String confidence;
   @GraphQLField(kkhtml = "KFieldText", label = "报告期", sql = "period like '$U{period}%'" ,field = "period")
   private String period;

}