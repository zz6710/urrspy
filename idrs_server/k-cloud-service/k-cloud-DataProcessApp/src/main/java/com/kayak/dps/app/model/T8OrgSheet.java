package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "t8OrgSheetService",table = "ODS_SUPPLY_ORG_BAS_INF")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class T8OrgSheet {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "机构信息ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "机构编码", sql = "ORG_NBR_EXT = $S{orgNbrExt}" ,field = "ORG_NBR_EXT")
   private String orgNbrExt;
   @GraphQLField(kkhtml = "KFieldText", label = "机构全称", sql = "ORG_FULL_NAME like '%$U{orgFullName}%'" ,field = "ORG_FULL_NAME")
   private String orgFullName;
   @GraphQLField(kkhtml = "KFieldText", label = "机构简称", sql = "ORG_SHT_NM like '%$U{orgShtNm}%'" ,field = "ORG_SHT_NM")
   private String orgShtNm;
   @GraphQLField(kkhtml = "KFieldText", label = "统一社会信用代码", sql = "CSLD_SOC_CRD_CD like '%$U{csldSocCrdCd}%'" ,field = "CSLD_SOC_CRD_CD")
   private String csldSocCrdCd;
   @GraphQLField(kkhtml = "KFieldText", label = "机构种类", sql = "ORG_TYP = $S{orgTyp}" ,field = "ORG_TYP")
   private String orgTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "同业机构类型", sql = "SAM_BUS_ORG_TYP = $S{samBusOrgTyp}" ,field = "SAM_BUS_ORG_TYP")
   private String samBusOrgTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "机构所属地区", sql = "ORG_BLG_ZON = $S{orgBlgZon}" ,field = "ORG_BLG_ZON")
   private String orgBlgZon;
   @GraphQLField(kkhtml = "KFieldText", label = "机构外部评级", sql = "ORG_OUT_RAT = $S{orgOutRat}" ,field = "ORG_OUT_RAT")
   private String orgOutRat;
   @GraphQLField(kkhtml = "KFieldText", label = "机构内部评级", sql = "ORG_IN_RAT = $S{orgInRat}" ,field = "ORG_IN_RAT")
   private String orgInRat;
   @GraphQLField(kkhtml = "KFieldText", label = "是否政府融资平台(财汇)", sql = "IS_PLAT_FORM_CH = $S{isPlatFormCh}" ,field = "IS_PLAT_FORM_CH")
   private String isPlatFormCh;
   @GraphQLField(kkhtml = "KFieldText", label = "是否政府融资平台(手工)", sql = "IS_PLAT_FORM_SG = $S{isPlatFormSg}" ,field = "IS_PLAT_FORM_SG")
   private String isPlatFormSg;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构所属行业", sql = "CC_INDUSTRY_ISSUER = $S{ccIndustryIssuer}" ,field = "CC_INDUSTRY_ISSUER")
   private String ccIndustryIssuer;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构类型（按规模划分）", sql = "ISU_ORG_TYP_SCALE_SIZ = $S{isuOrgTypScaleSiz}" ,field = "ISU_ORG_TYP_SCALE_SIZ")
   private String isuOrgTypScaleSiz;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构类型（按技术领域划分）", sql = "ISU_ORG_TYP_TCHNO = $S{isuOrgTypTchno}" ,field = "ISU_ORG_TYP_TCHNO")
   private String isuOrgTypTchno;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构类型（按经济类型划分）", sql = "ISU_ORG_TYP_ECN = $S{isuOrgTypEcn}" ,field = "ISU_ORG_TYP_ECN")
   private String isuOrgTypEcn;
   @GraphQLField(kkhtml = "KFieldText", label = "规模", sql = "CC_INSTITUTE_TYPE_SCALE = $S{ccInstituteTypeScale}" ,field = "CC_INSTITUTE_TYPE_SCALE")
   private String ccInstituteTypeScale;
   @GraphQLField(kkhtml = "KFieldText", label = "行业一级分类", sql = "ORG_FRS_CTG = $S{orgFrsCtg}" ,field = "ORG_FRS_CTG")
   private String orgFrsCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "行业二级分类", sql = "ORG_SEC_CTG = $S{orgSecCtg}" ,field = "ORG_SEC_CTG")
   private String orgSecCtg;
   @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "VERSION = $S{version}" ,field = "VERSION")
   private String version;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "CRT_USER = $S{crtUser}" ,field = "CRT_USER")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "更改人", sql = "UPD_USER = $S{updUser}" ,field = "UPD_USER")
   private String updUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "CRT_DATE = $S{crtDt}" ,field = "CRT_DATE")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "UPD_DATE = $S{updDt}" ,field = "UPD_DATE")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "REMARK = $S{remark}" ,field = "REMARK")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "DEAL_DATE = $S{dealDate}" ,field = "DEAL_DATE")
   private String dealDate;
   @GraphQLField(kkhtml="KFieldText", label = "创建时间", sql="CRT_TIME=$S{crtTime}",field = "CRT_TIME")
   private String crtTime;

}