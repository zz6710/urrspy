package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "baseFiveNonstandEntityService",table = "base_five_nonstand_entity")
@Data
public class BaseFiveNonstandEntity {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "公司id", sql = "windid = $S{windid}" ,field = "windid")
   private String windid;
   @GraphQLField(kkhtml = "KFieldText", label = "万得统一机构编码", sql = "companycode like '%$U{companycode}%'" ,field = "companycode")
   private String companycode;
   @GraphQLField(kkhtml = "KFieldText", label = "公司全称", sql = "companyname like '%$U{companyname}%'" ,field = "companyname")
   private String companyname;
   @GraphQLField(kkhtml = "KFieldText", label = "统一社会信用代码", sql = "usci like '%$U{usci}%'" ,field = "usci")
   private String usci;
   @GraphQLField(kkhtml = "KFieldText", label = "注册地区代码", sql = "regioncode = $S{regioncode}" ,field = "regioncode")
   private String regioncode;
   @GraphQLField(kkhtml = "KFieldText", label = "注册地区", sql = "region = $S{region}" ,field = "region")
   private String region;
   @GraphQLField(kkhtml = "KFieldText", label = "是否科技金融", sql = "isfintech = $S{isfintech}" ,field = "isfintech")
   private String isfintech;
   @GraphQLField(kkhtml = "KFieldText", label = "是否绿色金融", sql = "isgreen = $S{isgreen}" ,field = "isgreen")
   private String isgreen;
   @GraphQLField(kkhtml = "KFieldText", label = "是否普惠金融", sql = "isinclusive = $S{isinclusive}" ,field = "isinclusive")
   private String isinclusive;
   @GraphQLField(kkhtml = "KFieldText", label = "是否养老金融", sql = "ispension = $S{ispension}" ,field = "ispension")
   private String ispension;
   @GraphQLField(kkhtml = "KFieldText", label = "是否数字金融", sql = "isdigital = $S{isdigital}" ,field = "isdigital")
   private String isdigital;
   @GraphQLField(kkhtml = "KFieldText", label = "是否高新技术企业", sql = "ishightech = $S{ishightech}" ,field = "ishightech")
   private String ishightech;
   @GraphQLField(kkhtml = "KFieldText", label = "是否专精特新", sql = "issrdi = $S{issrdi}" ,field = "issrdi")
   private String issrdi;
   @GraphQLField(kkhtml = "KFieldText", label = "是否科技型中小企业", sql = "istechsme = $S{istechsme}" ,field = "istechsme")
   private String istechsme;
   @GraphQLField(kkhtml = "KFieldText", label = "是否其他科技型企业", sql = "isothertech = $S{isothertech}" ,field = "isothertech")
   private String isothertech;
   @GraphQLField(kkhtml = "KFieldText", label = "是否高技术制造业", sql = "ishightechm = $S{ishightechm}" ,field = "ishightechm")
   private String ishightechm;
   @GraphQLField(kkhtml = "KFieldText", label = "是否高技术服务业", sql = "ishightechs = $S{ishightechs}" ,field = "ishightechs")
   private String ishightechs;
   @GraphQLField(kkhtml = "KFieldText", label = "是否战略新兴产业", sql = "isstraemer = $S{isstraemer}" ,field = "isstraemer")
   private String isstraemer;
   @GraphQLField(kkhtml = "KFieldText", label = "是否知识产权密集型产业", sql = "isintellectual = $S{isintellectual}" ,field = "isintellectual")
   private String isintellectual;
   @GraphQLField(kkhtml = "KFieldText", label = "是否中小微企业", sql = "issmme = $S{issmme}" ,field = "issmme")
   private String issmme;
   @GraphQLField(kkhtml = "KFieldText", label = "企业规模", sql = "companyscale = $S{companyscale}" ,field = "companyscale")
   private String companyscale;
   @GraphQLField(kkhtml = "KFieldText", label = "是否民营企业", sql = "isprivate = $S{isprivate}" ,field = "isprivate")
   private String isprivate;
   @GraphQLField(kkhtml = "KFieldText", label = "企业所有制性质", sql = "businessnature = $S{businessnature}" ,field = "businessnature")
   private String businessnature;
   @GraphQLField(kkhtml = "KFieldText", label = "是否三农领域", sql = "isagriculture = $S{isagriculture}" ,field = "isagriculture")
   private String isagriculture;
   @GraphQLField(kkhtml = "KFieldText", label = "是否数字产业化", sql = "isdigiindustry = $S{isdigiindustry}" ,field = "isdigiindustry")
   private String isdigiindustry;
   @GraphQLField(kkhtml = "KFieldText", label = "是否产业数字化", sql = "isdigitalization = $S{isdigitalization}" ,field = "isdigitalization")
   private String isdigitalization;
   @GraphQLField(kkhtml = "KFieldText", label = "是否高污染高能耗", sql = "ispollution = $S{ispollution}" ,field = "ispollution")
   private String ispollution;
   @GraphQLField(kkhtml = "KFieldText", label = "报告期", sql = "period like '$U{period}%'" ,field = "period")
   private String period;

}