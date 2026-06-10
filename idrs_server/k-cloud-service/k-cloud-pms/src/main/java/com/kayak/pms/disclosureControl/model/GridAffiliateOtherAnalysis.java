package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "gridAffiliateOtherAnalysisService", table = "app_grid_affiliate_other_analysis_base")
public class GridAffiliateOtherAnalysis {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
    private String prodCd;
    @GraphQLField(kkhtml = "KFieldText", label = "品种代码", sql = "bred_cd = $S{bredCd}" ,field = "bred_cd")
    private String bredCd;
    @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "scr_id = $S{scrId}" ,field = "scr_id")
    private String scrId;
    @GraphQLField(kkhtml = "KFieldText", label = "资产名称", sql = "scr_nm = $S{scrNm}" ,field = "scr_nm")
    private String scrNm;
    @GraphQLField(kkhtml = "KFieldText", label = "关联方名称", sql = "affiliate_name LIKE '%$U{affiliateName}%'" ,field = "affiliate_name")
    private String affiliateName;
    @GraphQLField(kkhtml = "KFieldText", label = "交易类型", sql = "deal_type = $S{dealType}" ,field = "deal_type")
    private String dealType;
    @GraphQLField(kkhtml = "KFieldText", label = "交易金额（单位：元）", sql = "deal_amount = $S{dealAmount}" ,field = "deal_amount")
    private String dealAmount;
    @GraphQLField(kkhtml = "KFieldText", label = "发布日期", sql = "pos_dt = $S{posDt}" ,field = "pos_dt")
    private String posDt;
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_dt = $S{dealDt}" ,field = "deal_dt")
    private String dealDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
}