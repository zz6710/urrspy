package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "gridCombineRiskAnalysisService", table = "app_grid_combine_risk_analysis_base")
public class GridCombineRiskAnalysis {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
    private String prodCd;
    @GraphQLField(kkhtml = "KFieldText", label = "品种代码", sql = "bred_cd = $S{bredCd}" ,field = "bred_cd")
    private String bredCd;
    @GraphQLField(kkhtml = "KFieldText", label = "投资方式", sql = "invest_way = $S{investWay}" ,field = "invest_way")
    private String investWay;
    @GraphQLField(kkhtml = "KFieldText", label = "资产种类", sql = "invest_type = $S{investType}" ,field = "invest_type")
    private String investType;
    @GraphQLField(kkhtml = "KFieldText", label = "余额(元)", sql = "balance_amt  = $S{balanceAmt}" ,field = "balance_amt")
    private String balanceAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "发布日期", sql = "pos_dt  = $S{posDt}" ,field = "pos_dt")
    private String posDt;
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_dt = $S{dealDt}" ,field = "deal_dt")
    private String dealDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
}