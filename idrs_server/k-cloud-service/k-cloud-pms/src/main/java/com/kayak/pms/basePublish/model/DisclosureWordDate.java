package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/5/14 15:06
 */
@Data
@GraphQLModel(fetcher = "disclosureWordDateService",table = "idb_disclosure_word_date")
public class DisclosureWordDate {
    @GraphQLField(key = true ,field = "id")
    private String id;
    @GraphQLField(field = "prod_code")
    private String prodCode;
    @GraphQLField(field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(field = "report_date")
    private String reportDate;
    @GraphQLField(field = "column_key")
    private String columnKey;
    @GraphQLField(field = "column_value")
    private String columnValue;
    @GraphQLField(field = "crt_date")
    private String crtDate;
    @GraphQLField(field = "crt_time")
    private String crtTime;
    @GraphQLField(field = "crt_user")
    private String crtUser;
    @GraphQLField(field = "upd_date")
    private String updDate;
    @GraphQLField(field = "upd_time")
    private String updTime;
    @GraphQLField(field = "upd_user")
    private String updUser;
    @GraphQLField
    private String jsonData;
    @GraphQLField
    private String t8DisclosureNoticeId;
    @GraphQLField()
    private String filFormData;
    @GraphQLField()
    private String filInvestFormData;
    @GraphQLField()
    private String userId;
    @GraphQLField()
    private String currentStageStatus;
    @GraphQLField()
    private String approvalStatus;
    @GraphQLField()
    private String prodBaseDate;
    @GraphQLField()
    private String noticeReportDate;
    @GraphQLField()
    private String shareName;
    @GraphQLField()
    private String t8DisclosureVersionId;
    @GraphQLField()
    private String disclosureVersion;
}
