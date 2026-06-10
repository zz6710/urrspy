package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "baseIsoverdueInfoService",table = "base_isoverdue_info")
@Data
public class BaseIsoverdueInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "bond_code like '$U{bondCode}%'" ,field = "bond_code")
   private String bondCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "bond_name like '$U{bondName}%'" ,field = "bond_name")
   private String bondName;
   @GraphQLField(kkhtml = "KFieldText", label = "资产类型", sql = "asset_type = $S{assetType}" ,field = "asset_type")
   private String assetType;
   @GraphQLField(kkhtml = "KFieldText", label = "交易市场", sql = "bond_mkt = $S{bondMkt}" ,field = "bond_mkt")
   private String bondMkt;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "create_dt = $S{createDt}" ,field = "create_dt")
   private String createDt;

}