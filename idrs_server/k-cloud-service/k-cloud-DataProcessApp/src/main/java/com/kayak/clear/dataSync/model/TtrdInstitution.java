package com.kayak.clear.dataSync.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@GraphQLModel(fetcher = "ttrdInstitutionService",table = "stg_ttrd_relatedtrade_reflect")
public class TtrdInstitution {

    @GraphQLField(key = true , label = "id" ,field = "id")
    private String id;

    @GraphQLField(label = "交易对手ID" ,field = "party_id")
    private String partyId;

    @GraphQLField(label = "交易对手名称" ,field = "party_name")
    private String partyName;

    @GraphQLField(label = "关联交易情况" ,field = "relation_trade_descrption")
    private String relationTradeDescrption;

    @GraphQLField(label = "产品登记编码" ,field = "zzd_code")
    private String zzdCode;

    @GraphQLField(label = "备注" ,field = "memo")
    private String memo;

    @GraphQLField(label = "更新人" ,field = "update_user")
    private String updateUser;

    @GraphQLField(label = "更新时间" ,field = "update_time")
    private String updateTime;

    @GraphQLField(label = "待定" ,field = "vs_custodian_name")
    private String vsCustodianName;

    @GraphQLField(label = "录入方式" ,field = "partyFrom")
    private String party_from;

    @GraphQLField(label = "托管人名称" ,field = "vc_custodian_name")
    private String vcCustodianName;

    @GraphQLField(label = "托管人id" ,field = "vc_custodian_id")
    private String vcCustodianId;

    @GraphQLField(label = "处理日期" ,field = "deal_date")
    private String dealDate;

    @GraphQLField(label = "接口代码" ,field = "port_code")
    private String portCode;



}
