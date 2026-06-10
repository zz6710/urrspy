package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @BelongsProject: idrs3
 * @BelongsPackage: com.kayak.pms.basePublish.model
 * @Author: wangchenglin
 * @CreateTime: 2023/02/15  22:21
 * @Description:
 * @Version: 1.0
 */
@Data
@GraphQLModel(fetcher = "dpbReportInfoService", table = "base_report_result")
public class DpbReportInfo {

    @GraphQLField
    private String reportCatgory;
    @GraphQLField
    private String tableName;
    @GraphQLField
    private String registerStatus;
    @GraphQLField
    private String theoryReportStartDate;
    @GraphQLField
    private String registerDate;
    @GraphQLField
    private String theoryStartDate;
    @GraphQLField
    private String theoryEndDate;
    @GraphQLField
    private String realStartDate;
    @GraphQLField
    private String realEndDate;
    @GraphQLField
    private String resultOrder;
    @GraphQLField
    private String id;
    @GraphQLField
    private String indexCode;
    @GraphQLField
    private String indexName;
    @GraphQLField
    private String validateTable;
    @GraphQLField
    private String columnCode;
    @GraphQLField
    private String validateType;
    @GraphQLField
    private String validateResult;
    @GraphQLField
    private String reason;
    @GraphQLField
    private String validateColumn;
    @GraphQLField
    private String dealDate;
    @GraphQLField
    private String validateRow;
    @GraphQLField
    private String createDate;
    @GraphQLField
    private String createTime;
    @GraphQLField
    private String dataNum;
    @GraphQLField
    private String page;
    @GraphQLField
    private String holdingDate;

}
