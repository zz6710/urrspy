package com.kayak.dps.app.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "counterPartyService",table = "ods_supply_counter_party")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounterPartyModel {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "关联法人信息id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "机构编码", sql = "org_cd = $S{orgCd}" ,field = "org_cd")
   private String orgCd;
   @GraphQLField(kkhtml = "KFieldText", label = "机构名称", sql = "ORG_NM like '%$U{orgNm}%'" ,field = "ORG_NM")
   private String orgNm;
   @GraphQLField(kkhtml = "KFieldText", label = "统一社会信用代码", sql = "counter_party_cd like '%$U{counterPartyCd}%'" ,field = "counter_party_cd")
   private String counterPartyCd;
   @GraphQLField(kkhtml = "KFieldText", label = "债券发行人", sql = "counter_party_nm like '%$U{counterPartyNm}%'" ,field = "counter_party_nm")
   private String counterPartyNm;
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手简称", sql = "counter_party_sht_nm like '%$U{counterPartyShtNm}%'" ,field = "counter_party_sht_nm")
   private String counterPartyShtNm;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
   private String updDt;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "SPV机构编码", sql = "spv_org_enc = $S{spvOrgEnc}" ,field = "spv_org_enc")
   private String spvOrgEnc;
   @ExcelIgnore
   @GraphQLField(kkhtml = "KFieldText", label = "SPV产品登记编码", sql = "spv_prod_reg_enc = $S{spvProdRegEnc}" ,field = "spv_prod_reg_enc")
   private String spvProdRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "SPV人行编码", sql = "spv_pban_enc = $S{spvPbanEnc}" ,field = "SPV_PBAN_ENC")
   private String spvPbanEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "VERSION = $S{version}" ,field = "VERSION")
   private String version;
   @GraphQLField(kkhtml = "KFieldText", label = "机构种类", sql = "ORG_TYP = $S{orgTyp}" ,field = "ORG_TYP")
   private String orgTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手类型", sql = "counter_party_type = $S{counterPartyType}" ,field = "counter_party_type")
   private String counterPartyType;
   @GraphQLField(kkhtml = "KFieldText", label = "企业规模", sql = "ENTERP_SCALE = $S{enterpScale}" ,field = "ENTERP_SCALE")
   private String enterpScale;
   @GraphQLField(kkhtml = "KFieldText", label = "企业规模是否为空")
   private String enterpScaleFu;
   @GraphQLField(kkhtml = "KFieldText", label = "发行人注册地")
   private String registerArea;
   @GraphQLField(kkhtml = "KFieldText", label = "发行人注册地为空")
   private String registerAreaFu;

}