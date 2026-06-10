package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/9/1 16:34
 */
//理财产品风险评分实体类
@Data
@GraphQLModel(fetcher = "prodRiskRemarkService",table = "t8_risk_remark")
public class ProdRiskRemark {
    @GraphQLField
    private String id;//ID
    @GraphQLField
    private String t8ProdInfoId;
    @GraphQLField
    private String riskRemark;
    @GraphQLField
    private String inputuser;
    @GraphQLField
    private String crtDate;
    @GraphQLField
    private String crtTime;
    @GraphQLField
    private String uptDate;
    @GraphQLField
    private String uptTime;
}
