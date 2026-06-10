package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "rmsOdsZyG06bService",table = "rms_ods_zy_g06b_i")
@Data
public class RmsOdsZyG06b {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "报表日期", sql = "data_date = $S{dataDate}" ,field = "data_date")
   private String dataDate;
   @GraphQLField(kkhtml = "KFieldText", label = "账龄1个月至3个月（含）", sql = "b09 = $S{b09}" ,field = "b09")
   private String b09;
   @GraphQLField(kkhtml = "KFieldText", label = "账龄3个月至6个月（含）", sql = "b10 = $S{b10}" ,field = "b10")
   private String b10;
   @GraphQLField(kkhtml = "KFieldText", label = "账龄6个月至1年（含）", sql = "b11 = $S{b11}" ,field = "b11")
   private String b11;
   @GraphQLField(kkhtml = "KFieldText", label = "账龄1年以上", sql = "b12 = $S{b12}" ,field = "b12")
   private String b12;

}