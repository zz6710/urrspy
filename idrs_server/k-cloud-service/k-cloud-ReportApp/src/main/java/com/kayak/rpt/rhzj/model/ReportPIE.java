package com.kayak.rpt.rhzj.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportPIEService",table = "app_rpt_pie")
public class ReportPIE {
   @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code LIKE '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资金池代码", sql = "pbc_assetscode LIKE '%$U{pbcAssetscode}%'" ,field = "pbc_assetscode")
   private String pbcAssetscode;
   @GraphQLField(kkhtml = "KFieldText", label = "报送人行产品代码", sql = "peoplebank_submitcode LIKE '%$U{peoplebankSubmitcode}%'" ,field = "peoplebank_submitcode")
   private String peoplebankSubmitcode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品实际终止日期", sql = "end_date_real = $S{endDateReal}" ,field = "end_date_real")
   private String endDateReal;
   @GraphQLField(kkhtml = "KFieldText", label = "币种代码", sql = "cny = $S{cny}" ,field = "cny")
   private String cny;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构实现收入", sql = "org_ern = $S{orgErn}" ,field = "org_ern")
   private String orgErn;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构实现收入折人民币(元)", sql = "org_ern_rmb = $S{orgErnRmb}" ,field = "org_ern_rmb")
   private String orgErnRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益", sql = "cust_ern = $S{custErn}" ,field = "cust_ern")
   private String custErn;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益折人民币(元)", sql = "cust_ern_rmb = $S{custErnRmb}" ,field = "cust_ern_rmb")
   private String custErnRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益率", sql = "cust_ern_yld = $S{custErnYld}" ,field = "cust_ern_yld")
   private String custErnYld;
    @GraphQLField(label = "开始时间", sql = "end_date_real >= $S{beginDate}", field = "end_date_real")
    private String beginDate;
    @GraphQLField(label = "结束时间", sql = "end_date_real <= $S{queryDate}", field = "end_date_real")
    private String queryDate;


}