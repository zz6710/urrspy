package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "gridFbassetHoldFronttenService", table = "app_grid_fbasset_holding_frontten_base")
public class GridFbassetHoldFrontten {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
    private String prodCd;
    @GraphQLField(kkhtml = "KFieldText", label = "品种代码", sql = "bred_cd = $S{bredCd}" ,field = "bred_cd")
    private String bredCd;
    @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "scr_id = $S{scrId}" ,field = "scr_id")
    private String scrId;
    @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "scr_nm LIKE '%$U{scrNm}%'" ,field = "scr_nm")
    private String scrNm;
    @GraphQLField(kkhtml = "KFieldText", label = "余额(万元)", sql = "bal_amt = $S{balAmt}" ,field = "bal_amt")
    private String balAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "发布日期", sql = "pos_dt = $S{posDt}" ,field = "pos_dt")
    private String posDt;
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_dt = $S{dealDt}" ,field = "deal_dt")
    private String dealDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
}