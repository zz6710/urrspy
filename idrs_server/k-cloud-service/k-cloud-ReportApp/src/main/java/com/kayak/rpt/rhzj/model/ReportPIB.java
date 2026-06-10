package com.kayak.rpt.rhzj.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportPIBService",table = "app_rpt_pib")
public class ReportPIB {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "报送人行产品代码", sql = "peoplebank_submitcode LIKE '%$U{peoplebankSubmitcode}%'" ,field = "peoplebank_submitcode")
   private String peoplebankSubmitcode;
   @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "行内产品代码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "地区代码", sql = "area_code = $S{areaCode}" ,field = "area_code")
   private String areaCode;
   @GraphQLField(kkhtml = "KFieldText", label = "客户类型", sql = "cust_type = $S{custType}" ,field = "cust_type")
   private String custType;
   @GraphQLField(kkhtml = "KFieldText", label = "币种代码", sql = "cny = $S{cny}" ,field = "cny")
   private String cny;
   @GraphQLField(kkhtml = "KFieldText", label = "起始募集金额(元)", sql = "init_amount = $S{initAmount}" ,field = "init_amount")
   private String initAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "起始募集金额折人民币(元)", sql = "init_amount_rmb = $S{initAmountRmb}" ,field = "init_amount_rmb")
   private String initAmountRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "起始募集份额(元)", sql = "init_vol = $S{initVol}" ,field = "init_vol")
   private String initVol;

    @GraphQLField(label = "开始时间", sql = "report_date >= $S{beginDate}", field = "report_date")
    private String beginDate;
    @GraphQLField(label = "结束时间", sql = "report_date <= $S{queryDate}", field = "report_date")
    private String queryDate;

}