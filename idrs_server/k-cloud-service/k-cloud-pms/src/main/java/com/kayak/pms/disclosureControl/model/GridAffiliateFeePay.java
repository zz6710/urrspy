package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "gridAffiliateFeePayService", table = "app_grid_affiliate_fee_pay_base")
public class GridAffiliateFeePay {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
    private String prodCd;
    @GraphQLField(kkhtml = "KFieldText", label = "关联方名称" ,sql = "affiliate_name like '%$U{affiliateName}%'" ,field = "affiliate_name")
    private String affiliateName;
    @GraphQLField(kkhtml = "KFieldText", label = "费用类型", sql = "fee_type = $S{feeType}" ,field = "fee_type")
    private String feeType;
    @GraphQLField(kkhtml = "KFieldText", label = "发生金额（单位：元）", sql = "deal_amount = $S{dealAmount}" ,field = "deal_amount")
    private String dealAmount;
    @GraphQLField(kkhtml = "KFieldText", label = "发布日期", sql = "pos_dt = $S{posDt}" ,field = "pos_dt")
    private String posDt;
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_dt = $S{dealDt}" ,field = "deal_dt")
    private String dealDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
}