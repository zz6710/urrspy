package com.kayak.pms.email.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@GraphQLModel(fetcher = "m8DisclosureManualService",table = "sggg")
public class M8DisclosureManual {

    @GraphQLField(label = "公告ID", sql = "id = $S{id}", field = "id")
    private String id;

    @GraphQLField(label = "创建时间", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;

    @GraphQLField(label = "创建人", sql = "crt_user = $S{crtUser}", field = "crt_user")
    private String crtUser;

    @GraphQLField(label = "公告标题", sql = "title = $S{title}", field = "title")
    private String title;

    @GraphQLField(label = "公告文件名", sql = "file_name = $S{fileName}", field = "file_name")
    private String fileName;

    @GraphQLField(label = "公告文件path", sql = "file_path = $S{filePath}", field = "file_path")
    private String filePath;

    @GraphQLField(label = "信披类型", sql = "type = $S{type}", field = "type")
    private String type;

    @GraphQLField(label = "信披类型", sql = "son_type = $S{sonType}", field = "son_type")
    private String sonType;

    @GraphQLField(label = "选择产品", sql = "prod_code = $S{prodCode}", field = "prod_code")
    private String prodCode;

    @GraphQLField(label = "计划发布日期", sql = "start_establishdate = $S{startDstablishdate}", field = "start_establishdate")
    private String startDstablishdate;

    @GraphQLField(label = "发送邮箱", sql = "sendmail = $S{sendmail}", field = "sendmail")
    private String sendmail;

    @GraphQLField(label = "信披渠道", sql = "channel = $S{channel}", field = "channel")
    private String channel;


    @GraphQLField(label = "备注说明", sql = "node = $S{note}", field = "node")
    private String note;

    @GraphQLField(label = "产品名称", sql = "prod_name = $S{prodName1}", field = "prod_name")
    private String prodName1;

    @GraphQLField(label = "计划发布日期", sql = "plan_fb_date = $S{planFbDate}", field = "plan_fb_date")
    private String planFbDate;

    @GraphQLField(label = "创建人id", sql = "crt_user_id = $S{crtUserId}", field = "crt_user_id")
    private String crtUserId;

    @GraphQLField(label = "渠道列表", field = "channel_list")
    private String channelList;

    @GraphQLField(label = "文件列表", field = "file_list")
    private String fileList;

    @GraphQLField(label = "是否手工公告", field = "is_manual_notice")
    private String isManualNotice;

    @GraphQLField(label = "产品系列代码", field = "PROD_SER_CD")
    private String prodSerCd;

    @GraphQLField(label = "审批状态", field = "review_status")
    private String reviewStatus;


}
