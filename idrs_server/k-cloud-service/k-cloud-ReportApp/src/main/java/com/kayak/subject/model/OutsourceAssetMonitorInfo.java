package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "outsourceAssetMonitorInfoService",table = "outsource_asset_monitor_info")
@Data
public class OutsourceAssetMonitorInfo {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "asset_code like '%$U{assetCode}%'" ,field = "asset_code")
   private String assetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "asset_name = $S{assetName}" ,field = "asset_name")
   private String assetName;
   @GraphQLField(kkhtml = "KFieldText", label = "组合代码", sql = "combined_code = $S{combinedCode}" ,field = "combined_code")
   private String combinedCode;
   @GraphQLField(kkhtml = "KFieldText", label = "专户类型", sql = "a_type like '%$U{atype}%'" ,field = "a_type")
   private String atype;
   @GraphQLField(kkhtml = "KFieldText", label = "资产联系人", sql = "sponsor_linkman = $S{sponsorLinkman}" ,field = "sponsor_linkman")
   private String sponsorLinkman;
   @GraphQLField(kkhtml = "KFieldText", label = "是否接入数据", sql = "is_join_data like '%$U{isJoinData}%'" ,field = "is_join_data")
   private String isJoinData;
   @GraphQLField(kkhtml = "KFieldText", label = "未接入数据原因", sql = "no_join_reason = $S{noJoinReason}" ,field = "no_join_reason")
   private String noJoinReason;
   @GraphQLField(kkhtml = "KFieldText", label = "数据检测结果", sql = "moni_result = $S{moniResult}" ,field = "moni_result")
   private String moniResult;
   @GraphQLField(kkhtml = "KFieldText", label = "偏离金额", sql = "deviate_amt = $S{deviateAmt}" ,field = "deviate_amt")
   private String deviateAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "估值日期", sql = "input_date like '%$U{inputDate}%'" ,field = "input_date")
   private String inputDate;
   @GraphQLField(kkhtml = "KFieldText", label = "计算日期", sql = "calc_date like '%$U{calcDate}%'" ,field = "calc_date")
   private String calcDate;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_date like '%$U{dealDate}%'" ,field = "deal_date")
   private String dealDate;

}