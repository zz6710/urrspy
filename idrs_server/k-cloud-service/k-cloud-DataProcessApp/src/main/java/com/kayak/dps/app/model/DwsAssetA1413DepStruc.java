package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsAssetA1413DepStrucService",table = "dws_asset_a1413_dep_struc")
@Data
public class DwsAssetA1413DepStruc {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "单位和个人定期及其他存款_1年以内（含）-余额（元）", sql = "org_one = $S{orgOne}" ,field = "org_one")
   private String orgOne;
   @GraphQLField(kkhtml = "KFieldText", label = "单位和个人定期及其他存款_1至2年（含）-余额（元）", sql = "org_one_two = $S{orgOneTwo}" ,field = "org_one_two")
   private String orgOneTwo;
   @GraphQLField(kkhtml = "KFieldText", label = "单位和个人定期及其他存款_2至3年（含）-余额（元）", sql = "org_two_thr = $S{orgTwoThr}" ,field = "org_two_thr")
   private String orgTwoThr;
   @GraphQLField(kkhtml = "KFieldText", label = "单位和个人定期及其他存款_3年以上-余额（元）", sql = "org_thr = $S{orgThr}" ,field = "org_thr")
   private String orgThr;
   @GraphQLField(kkhtml = "KFieldText", label = "境内非存款类金融机构定期及其他存款_1年以内（含）-余额（元）", sql = "dom_one = $S{domOne}" ,field = "dom_one")
   private String domOne;
   @GraphQLField(kkhtml = "KFieldText", label = "境内非存款类金融机构定期及其他存款_1至2年（含）-余额（元）", sql = "dom_one_two = $S{domOneTwo}" ,field = "dom_one_two")
   private String domOneTwo;
   @GraphQLField(kkhtml = "KFieldText", label = "境内非存款类金融机构定期及其他存款_2至3年（含）-余额（元）", sql = "dom_two_thr = $S{domTwoThr}" ,field = "dom_two_thr")
   private String domTwoThr;
   @GraphQLField(kkhtml = "KFieldText", label = "境内非存款类金融机构定期及其他存款_3年以上-余额（元）", sql = "dom_thr = $S{domThr}" ,field = "dom_thr")
   private String domThr;
   @GraphQLField(kkhtml = "KFieldText", label = "现金管理类理财存款-余额（元）", sql = "cra_dep = $S{craDep}" ,field = "cra_dep")
   private String craDep;
   @GraphQLField(kkhtml = "KFieldText", label = "现金管理类理财份额投资-余额（元）", sql = "cra_inv = $S{craInv}" ,field = "cra_inv")
   private String craInv;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "act_dt like '%$U{actDt}%'" ,field = "act_dt")
   private String actDt;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @GraphQLField(kkhtml = "KFieldText", label = "日期范围", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dateRange;

}