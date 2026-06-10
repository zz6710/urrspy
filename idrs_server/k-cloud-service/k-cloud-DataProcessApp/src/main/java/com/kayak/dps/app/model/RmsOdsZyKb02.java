package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "rmsOdsZyKb02Service",table = "rms_ods_zy_kb02")
@Data
public class RmsOdsZyKb02 {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "data_date = $S{dataDate}" ,field = "data_date")
   private String dataDate;
   @GraphQLField(kkhtml = "KFieldText", label = "企业从业人员人工成本(本年累计/元)", sql = "d01 = $S{d01}" ,field = "d01")
   private String d01;

}