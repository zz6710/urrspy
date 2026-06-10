package com.kayak.pms.prod.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//会议创设会议实体类
@Data
@GraphQLModel(fetcher = "meetCreateService",table = "t8_meet_create")
public class MeetCreate {
    @GraphQLField(kkhtml = "KFieldText",label = "会议名称", sql = "meet_name like '%$U{meetName}%'" ,field = "meet_name",kkhtmlDefault = true)
    private String meetName;
    @GraphQLField(label = "ID")
    private String id;
    @GraphQLField(label = "会议地点")
    private String meetSite;
    @GraphQLField(kkhtml = "KFieldDate",label = "会议日期", sql = "meet_date = $S{meetDate}" ,field = "meet_date",kkhtmlDefault = true,  kkhtmlExt = "{'data-type':'daterange'}")
    private String meetDate;
    @GraphQLField(label = "会议时间", sql = "meet_time = $S{meetTime}" ,field = "meet_time")
    private String meetTime;
    @GraphQLField(label = "会议人")
    private String username;
    @GraphQLField(label = "审批状态")
    private String approvalStatus;
    @GraphQLField(label = "创建日期")
    private String crtDate;
    @GraphQLField(label = "创建时间")
    private String crtTime;
    @GraphQLField(label = "创建人")
    private String crtUser;
    @GraphQLField(label = "更新日期")
    private String updDate;
    @GraphQLField(label = "更新时间")
    private String updTime;
    @GraphQLField(label = "更新人")
    private String updUser;

    @GraphQLField(label = "查询会议开始时间")
    private String createStartDate;
    @GraphQLField(label = "查询会议结束时间")
    private String createEndDate;




}
