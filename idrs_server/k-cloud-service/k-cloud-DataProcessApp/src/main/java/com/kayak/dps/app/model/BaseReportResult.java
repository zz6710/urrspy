package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "baseReportResultDesktopService",table = "base_report_result")
public class BaseReportResult {

    @GraphQLField
    private String id;
    @GraphQLField
    private String reportType;
    @GraphQLField
    private String reportTable;
    @GraphQLField
    private String reportTableName;
    @GraphQLField
    private String theoryReportStartDate;
    @GraphQLField
    private String theoryReportEndDate;
    @GraphQLField
    private String registerDate;
    @GraphQLField
    private String total;
    @GraphQLField
    private String reportSuccessNumber;
    @GraphQLField
    private String status;
    @GraphQLField
    private String registerStatus;
    @GraphQLField
    private String createDate;
    @GraphQLField
    private String createTime;
    @GraphQLField
    private String updateDate;
    @GraphQLField
    private String updateTime;
    @GraphQLField
    private String workDay;
    @GraphQLField
    private String needTotal;
    @GraphQLField
    private String checkType;

}
