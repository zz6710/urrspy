package com.kayak.pms.email.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@GraphQLModel(fetcher = "t8DisChannelInfoService",table = "idb_disclosure_channel_info")
public class T8DisChannelInfo {

    @GraphQLField(label = "id", sql = "id = $S{id}", field = "id")
    private String id;

    @GraphQLField(label = "渠道类型", sql = "channel_type = $S{channelType}", field = "channel_type")
    private String channelType;

    @GraphQLField(label = "渠道代码", sql = "channel_code = $S{channelCode}", field = "channel_code")
    private String channelCode;

    @GraphQLField(label = "渠道名称", sql = "channel_name = $S{channelName}", field = "channel_name")
    private String channelName;

    @GraphQLField(label = "邮箱", sql = "emails = $S{emails}", field = "emails")
    private String emails;

    @GraphQLField(label = "邮箱密码", sql = "email_passwd = $S{emailPasswd}", field = "email_passwd")
    private String emailPasswd;

    @GraphQLField(label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;

    @GraphQLField(label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;

    @GraphQLField(label = "创建人", sql = "crt_user_id = $S{crtUserId}", field = "crt_user_id")
    private String crtUserId;

    @GraphQLField(label = "创建人名称", sql = "crt_user_name = $S{crtUserName}", field = "crt_user_name")
    private String crtUserName;

    @GraphQLField(label = "更新日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;

    @GraphQLField(label = "更新时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;

    @GraphQLField(label = "更新人", sql = "upd_user_id = $S{updUserId}", field = "upd_user_id")
    private String updUserId;

    @GraphQLField(label = "更新人名称", sql = "upd_user_name = $S{updUserName}", field = "upd_user_name")
    private String updUserName;

    @GraphQLField(label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    //自定义渠道id  用于根据多给自定义渠道id查询渠道信息 rennannan
    @GraphQLField(label = "自定义渠道id", sql = "FIND_IN_SET(id,$S{customChannels})")
    private String customChannels;

    @GraphQLField(label = "状态", sql = "status = $S{status}", field = "status")
    private String status;
    
    @GraphQLField
    private String disclosureType;
    
    @GraphQLField
    private String disclosureSonType;

}
