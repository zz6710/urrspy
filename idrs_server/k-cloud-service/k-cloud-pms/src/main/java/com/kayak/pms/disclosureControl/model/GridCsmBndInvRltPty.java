package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "gridCsmBndInvRltPtyService", table = "app_grid_bnd_inv_rlt_pty_base")
public class GridCsmBndInvRltPty {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
    private String prodCd;
    @GraphQLField(kkhtml = "KFieldText", label = "品种代码", sql = "bred_cd = $S{bredCd}" ,field = "bred_cd")
    private String bredCd;
    @GraphQLField(kkhtml = "KFieldText", label = "关联方名称", sql = "affiliate_name LIKE '%$U{affiliateName}%'" ,field = "affiliate_name")
    private String affiliateName;
    @GraphQLField(kkhtml = "KFieldText", label = "证券代码", sql = "securities_code = $S{securitiesCode}" ,field = "securities_code")
    private String securitiesCode;
    @GraphQLField(kkhtml = "KFieldText", label = "证券简称", sql = "securities_name  LIKE '%$U{securitiesName}%'" ,field = "securities_name")
    private String securitiesName;
    @GraphQLField(kkhtml = "KFieldText", label = "交易金额（单位：元）", sql = "deal_amount = $S{dealAmount}" ,field = "deal_amount")
    private String dealAmount;
    @GraphQLField(kkhtml = "KFieldText", label = "费用类型", sql = "fee_type = $S{feeType}" ,field = "fee_type")
    private String feeType;
    @GraphQLField(kkhtml = "KFieldText", label = "发行方关联方式", sql = "party_relation = $S{partyRelation}" ,field = "party_relation")
    private String partyRelation;
    @GraphQLField(kkhtml = "KFieldText", label = "发布日期", sql = "pos_dt = $S{posDt}" ,field = "pos_dt")
    private String posDt;
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_dt = $S{dealDt}" ,field = "deal_dt")
    private String dealDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
}