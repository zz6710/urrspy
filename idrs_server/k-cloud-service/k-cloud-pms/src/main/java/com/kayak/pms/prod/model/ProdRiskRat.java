package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//理财产品风险评分实体类
@GraphQLModel(fetcher = "prodRiskRatService",table = "t8_prod_risk_rat")
@Data
public class ProdRiskRat {
    @GraphQLField
    private long id;//ID
    @GraphQLField
    private String t8RiskTemplateVersionId;//模板ID
    @GraphQLField
    private String riskProject;//项目
    @GraphQLField
    private String coefficient;//风险系数(模板)
    @GraphQLField
    private String coefficientProd;//风险系数(项目)
    @GraphQLField
    private String weight;//权重
    @GraphQLField
    private String t8ProdInfoId;//理财产品ID
    @GraphQLField
    private String t8RiskProjectId;//项目ID
    @GraphQLField
    private String judge;//判断（0-1）
    @GraphQLField
    private String integral;//积分
    @GraphQLField
    private String datas;//前台传入的json字符串

    private String isDisabled;//是否禁用(前台控制输入框是否可输入)
    private String isShowInput;//是否显示输入框(前台控制输入框是否可显示)

    @GraphQLField
    private String processInstanceId;

    @GraphQLField
    private String approvalStatus;
    @GraphQLField
    private String riskRemark;
}
