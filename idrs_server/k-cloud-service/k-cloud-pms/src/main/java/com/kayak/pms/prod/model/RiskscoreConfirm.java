package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/7/15 15:59
 */

/**
 * 风险评分确认,因为框架权限架构问题，风险评分与风险评分确认实体类、service分开定义
 */
@Data
@GraphQLModel(fetcher = "riskscoreConfirmServer",table = "t8_prod_risk_score")
public class RiskscoreConfirm {
    @GraphQLField(field = "id")
    private String id;

    @GraphQLField(field = "t8_prod_info_id")
    private String t8ProdInfoId;

    @GraphQLField(field = "prodCode",label="产品代码",kkhtmlDefault = true)
    private String prodCode;

    @GraphQLField(field = "risk_score",label="产品风险评分",kkhtmlDefault = true)
    private String riskScore;

    @GraphQLField(field = "prod_risk_level",label="产品风险评级",kkhtmlDefault = true,kkhtmlExt="{\"data-dict\":\"risklevel\"}")
    private String prodRiskLevel;

    @GraphQLField(field = "prod_risk_level",label="产品风险星级",kkhtmlDefault = true,kkhtmlExt="{\"data-dict\":\"t8_risk_score_status\"}")
    private String riskLevel;

    @GraphQLField(field = "is_confirm",label="是否确认",kkhtmlDefault = true,kkhtmlExt="{\"data-dict\":\"t8_is_confirm\"}")
    private String isConfirm;

    @GraphQLField(field = "risk_score_status",label="风险评分状态",kkhtmlDefault = true,kkhtmlExt="{\"data-dict\":\"t8_risk_score_status\"}")
    private String riskScoreStatus;

    @GraphQLField(field = "inputuser")
    private String inputuser;

    @GraphQLField(field = "updateuser")
    private String updateuser;

    @GraphQLField(field = "crt_date")
    private String crtDate;

    @GraphQLField(field = "crt_time")
    private String crtTime;

    @GraphQLField(field = "upd_date")
    private String updDate;

    @GraphQLField(field = "upd_time")
    private String updTime;

    @GraphQLField(field = "upd_time")
    private String isRecycleCode;
}
