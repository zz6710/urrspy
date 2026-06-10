package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "assetEndDateService",table = "dws_asset_end_date")
@Data
public class AssetEndDate {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "i_code = $S{icode}" ,field = "icode")
   private String icode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "i_name = $S{iname}" ,field = "iname")
   private String iname;
   @GraphQLField(kkhtml = "KFieldText", label = "资产三类", sql = "asset_third_type = $S{assetThirdType}" ,field = "asset_third_type")
   private String assetThirdType;
   @GraphQLField(kkhtml = "KFieldText", label = "资产到期日", sql = "asset_end_date = $S{assetEndDate}" ,field = "asset_end_date")
   private String assetEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "资产期限特殊说明", sql = "asset_term_pj = $S{assetTermPj}" ,field = "asset_term_pj")
   private String assetTermPj;
   @GraphQLField(kkhtml = "KFieldText", label = "统计日期", sql = "statistic_date = $S{statisticDate}" ,field = "statistic_date")
   private String statisticDate;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "act_dt = $S{actDt}" ,field = "act_dt")
   private String actDt;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;

}