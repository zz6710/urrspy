package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "ScheduleNavService",table = "t8_prod_base_nav")
public class T8ProdBaseNav {

  @GraphQLField(key = true, kkhtml = "KFieldText", label = "产品代码", sql = "PROD_CODE = $S{prodCode}" ,field = "PROD_CODE")
  private String prodCode;
  @GraphQLField(key = true, kkhtml = "KFieldText", label = "净值日期", sql = "NAV_DATE = $S{navDate}" ,field = "NAV_DATE")
  private String navDate;
  @GraphQLField(kkhtml = "KFieldText", label = "基准净值", sql = "BASE_NAV = $S{baseNav}" ,field = "BASE_NAV")
  private String baseNav;
  @GraphQLField(kkhtml = "KFieldText", label = "基准净值", sql = "CRT_DATE = $S{baseNav}" ,field = "CRT_DATE")
  private String crtDate;
  @GraphQLField(kkhtml = "KFieldText", label = "生成时间", sql = "CRT_TIME = $S{crtTime}" ,field = "CRT_TIME")
  private String crtTime;

}
