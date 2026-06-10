package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsG06BIIFbAssetService",table = "dws_g06b_non_info")
@Data
public class DwsG06BIIFbAssetInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;

   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;

   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "asset_code = $S{assetCode}" ,field = "asset_code")
   private String assetCode;

   @GraphQLField(kkhtml = "KFieldText", label = "资产三类", sql = "asset_thr_code = $S{assetThrCode}" ,field = "asset_thr_code")
   private String assetThrCode;

   @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "asset_name = $S{assetName}" ,field = "asset_name")
   private String assetName;

   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额", sql = "net_value = $S{netValue}" ,field = "net_value")
   private String netValue;

   @GraphQLField(kkhtml = "KFieldText", label = "g06穿透底层分类", sql = "g06_type = $S{g06Type}" ,field = "g06_type")
   private String g06Type;

   @GraphQLField(kkhtml = "KFieldText", label = "如填列1.5.16 非标，对股质类业务区分“1.5.16.a 场内”和“1.5.16.b 场外", sql = "non_place = $S{nonPlace}" ,field = "non_place")
   private String nonPlace;

   @GraphQLField(kkhtml = "KFieldText", label = "融资主体", sql = "fin_ent = $S{finEnt}" ,field = "fin_ent")
   private String finEnt;

   @GraphQLField(kkhtml = "KFieldText", label = "融资主体统一社会信用代码", sql = "fin_ent_cd = $S{finEntCd}" ,field = "fin_ent_cd")
   private String finEntCd;

   @GraphQLField(kkhtml = "KFieldText", label = "融资主体外部信用评级", sql = "fin_ent_rt = $S{finEntRt}" ,field = "fin_ent_rt")
   private String finEntRt;

   @GraphQLField(kkhtml = "KFieldText", label = "融资主体外部信用评级日期", sql = "fin_ent_dt = $S{finEntDt}" ,field = "fin_ent_dt")
   private String finEntDt;

   @GraphQLField(kkhtml = "KFieldText", label = "是否抵质押", sql = "if_ple = $S{ifPle}" ,field = "if_ple")
   private String ifPle;

   @GraphQLField(kkhtml = "KFieldText", label = "履约担保比", sql = "per_gua_rat = $S{perGuaRat}" ,field = "per_gua_rat")
   private String perGuaRat;

   @GraphQLField(kkhtml = "KFieldText", label = "是否足额抵质押", sql = "if_full_ple = $S{ifFullPle}" ,field = "if_full_ple")
   private String ifFullPle;

   @GraphQLField(kkhtml = "KFieldText", label = "是否保证类", sql = "if_war = $S{ifWar}" ,field = "if_war")
   private String ifWar;

   @GraphQLField(kkhtml = "KFieldText", label = "是否足额保证", sql = "if_full_war = $S{ifFullWar}" ,field = "if_full_war")
   private String ifFullWar;

   @GraphQLField(kkhtml = "KFieldText", label = "第三方保证人", sql = "war_nm = $S{warNm}" ,field = "war_nm")
   private String warNm;

   @GraphQLField(kkhtml = "KFieldText", label = "第三方保证人统一社会信用代码", sql = "war_cd = $S{warCd}" ,field = "war_cd")
   private String warCd;

   @GraphQLField(kkhtml = "KFieldText", label = "第三方保证人外部评级", sql = "war_rt = $S{warRt}" ,field = "war_rt")
   private String warRt;

   @GraphQLField(kkhtml = "KFieldText", label = "第三方保证人外部评级日期", sql = "war_dt = $S{warDt}" ,field = "war_dt")
   private String warDt;

   @GraphQLField(kkhtml = "KFieldText", label = "是否信用", sql = "if_cre = $S{ifCre}" ,field = "if_cre")
   private String ifCre;

   @GraphQLField(kkhtml = "KFieldText", label = "非标资产类别", sql = "non_asset_type = $S{nonAssetType}" ,field = "non_asset_type")
   private String nonAssetType;

   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;

}