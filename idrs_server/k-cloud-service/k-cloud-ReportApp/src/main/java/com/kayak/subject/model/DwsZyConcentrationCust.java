package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsZyConcentrationCustService",table = "dws_zy_concentration_cust")
@Data
public class DwsZyConcentrationCust {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "序号", sql = "xh = $S{xh}" ,field = "xh")
   private String xh;
   @GraphQLField(kkhtml = "KFieldText", label = "客户名称", sql = "cust_name = $S{custName}" ,field = "cust_name")
   private String custName;
   @GraphQLField(kkhtml = "KFieldText", label = "业务类型", sql = "s_type = $S{stype}" ,field = "s_type")
   private String stype;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "act_dt = $S{actDt}" ,field = "act_dt")
   private String actDt;
   @GraphQLField(kkhtml = "KFieldText", label = "处理日期", sql = "deal_date = $S{dealDate}" ,field = "deal_date")
   private String dealDate;
   @GraphQLField(kkhtml = "KFieldText", label = "行业名称", sql = "indu_name = $S{induName}" ,field = "indu_name")
   private String induName;
   @GraphQLField(kkhtml = "KFieldText", label = "国家或地区名称", sql = "region_name = $S{regionName}" ,field = "region_name")
   private String regionName;

}