package com.kayak.rpt.rhjrjgtj.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "appAssetA1413DepStrucService",table = "app_asset_a1413_dep_struc")
@Data
public class AppAssetA1413DepStruc {

   @GraphQLField(kkhtml = "KFieldText", label = "指标代码", sql = "met_code = $S{metCode}" ,field = "met_code")
   private String metCode;
   @GraphQLField(kkhtml = "KFieldText", label = "指标名称", sql = "met_name = $S{metName}" ,field = "met_name")
   private String metName;
   @GraphQLField(kkhtml = "KFieldText", label = "余额", sql = "met_bane = $S{metBane}" ,field = "met_bane")
   private String metBane;
   @GraphQLField(kkhtml = "KFieldText", label = "报表日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;

}