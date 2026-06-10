package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "gridFbassetHoldAnalysisService", table = "app_grid_fbasset_holding_analysis_base")
public class GridFbassetHoldAnalysis {
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
    @GraphQLField(kkhtml = "KFieldText", label = "融资客户", sql = "finance_customer LIKE '%$U{financeCustomer}%'" ,field = "finance_customer")
    private String financeCustomer;
    @GraphQLField(kkhtml = "KFieldText", label = "项目名称", sql = "project_name  LIKE '%$U{projectName}%'" ,field = "project_name")
    private String projectName;
    @GraphQLField(kkhtml = "KFieldText", label = "剩余融资期限", sql = "left_days = $S{leftDays}" ,field = "left_days")
    private String leftDays;
    @GraphQLField(kkhtml = "KFieldText", label = "到期收益分配", sql = "income_allocate = $S{incomeAllocate}" ,field = "income_allocate")
    private String incomeAllocate;
    @GraphQLField(kkhtml = "KFieldText", label = "交易结构", sql = "deal_structure = $S{dealStructure}" ,field = "deal_structure")
    private String dealStructure;
    @GraphQLField(kkhtml = "KFieldText", label = "风险状况", sql = "risk_conditions = $S{riskConditions}" ,field = "risk_conditions")
    private String riskConditions;
    @GraphQLField(kkhtml = "KFieldText", label = "发布日期", sql = "pos_dt = $S{posDt}" ,field = "pos_dt")
    private String posDt;
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_dt = $S{dealDt}" ,field = "deal_dt")
    private String dealDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
}