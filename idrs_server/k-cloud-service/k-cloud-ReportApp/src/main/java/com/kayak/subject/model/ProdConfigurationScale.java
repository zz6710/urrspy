package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "prodConfigurationScaleService",table = "dws_prod_configuration_scale")
@Data
public class ProdConfigurationScale {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "i_code = $S{icode}" ,field = "i_code")
   private String icode;
   @GraphQLField(kkhtml = "KFieldText", label = "母行划转产品投资市值", sql = "investmonamount = $S{investmonamount}" ,field = "investmonamount")
   private String investmonamount;
   @GraphQLField(kkhtml = "KFieldText", label = "自主发行产品投资市值", sql = "investownamount = $S{investownamount}" ,field = "investownamount")
   private String investownamount;
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资市值合计", sql = "investcountamount = $S{investcountamount}" ,field = "investcountamount")
   private String investcountamount;
   @GraphQLField(kkhtml = "KFieldText", label = "母行划转产品投资占比", sql = "investmonrate = $S{investmonrate}" ,field = "investmonrate")
   private String investmonrate;
   @GraphQLField(kkhtml = "KFieldText", label = "自主发行产品投资占比", sql = "investownrate = $S{investownrate}" ,field = "investownrate")
   private String investownrate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资占比合计", sql = "investcountrate = $S{investcountrate}" ,field = "investcountrate")
   private String investcountrate;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "act_dt = $S{actDt}" ,field = "act_dt")
   private String actDt;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;

}