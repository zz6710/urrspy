package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "netReportRulesService")
public class NetReportRules {

    @GraphQLField
    private String id;
    @GraphQLField
    private String operationMode;
    @GraphQLField
    private String regularOpenCycle;
    @GraphQLField
    private String reportRules;
    @GraphQLField
    private String reportFreqVal;
    @GraphQLField
    private String reportFreq;
    @GraphQLField
    private String lengthFreq;
    @GraphQLField
    private String specificDate;
    @GraphQLField
    private String specificDateVal;
    @GraphQLField
    private String reportDate;
    @GraphQLField
    private String reportMonth;
    @GraphQLField
    private String reportConfirmDate;
    @GraphQLField
    private String isBaseDay;
    @GraphQLField
    private String crtDate;
    @GraphQLField
    private String crtTime;
    @GraphQLField
    private String crtUser;
    @GraphQLField
    private String updDate;
    @GraphQLField
    private String updTime;
    @GraphQLField
    private String updUser;



}
