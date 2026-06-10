package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureNoticeChannelService",table = "idb_disclosure_notice_channel")
public class DisclosureNoticeChannel {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键id", sql = "id = $S{id}" ,field = "id")
    private String id;

    @GraphQLField(kkhtml = "KFieldText", label = "公告表id", sql = "disclosure_notice_id = $S{disclosureNoticeId}" ,field = "disclosure_notice_id")
    private String disclosureNoticeId;

    @GraphQLField(kkhtml = "KFieldText", label = "公告相关渠道表id", sql = "disclosure_notice_channel_id = $S{disclosureNoticeChannelId}" ,field = "disclosure_notice_channel_id")
    private String disclosureNoticeChannelId;

    @GraphQLField(kkhtml = "KFieldText", label = "渠道名称", sql = "channel_name = $S{channelName}" ,field = "channel_name")
    private String channelName;

    @GraphQLField(kkhtml = "KFieldText", label = "是否对接", sql = "is_docking = $S{isDocking}" ,field = "is_docking")
    private String isDocking;

    @GraphQLField(kkhtml = "KFieldText", label = "对接方式", sql = "docking_way = $S{dockingWay}" ,field = "docking_way")
    private String dockingWay;

    @GraphQLField(kkhtml = "KFieldText", label = "主机IP", sql = "host_ip = $S{hostIp}" ,field = "host_ip")
    private String hostIp;

    @GraphQLField(kkhtml = "KFieldText", label = "协议", sql = "protocol = $S{protocol}" ,field = "protocol")
    private String protocol;

    @GraphQLField(kkhtml = "KFieldText", label = "端口号", sql = "port_code = $S{portCode}" ,field = "port_code")
    private String portCode;

    @GraphQLField(kkhtml = "KFieldText", label = "用户名", sql = "user_name = $S{userName}" ,field = "user_name")
    private String userName;

    @GraphQLField(kkhtml = "KFieldText", label = "密码", sql = "password = $S{password}" ,field = "password")
    private String password;

    @GraphQLField(kkhtml = "KFieldText", label = "文件路径", sql = "file_path = $S{filePath}" ,field = "file_path")
    private String filePath;

    @GraphQLField(kkhtml = "KFieldText", label = "渠道状态", sql = "status = $S{status}" ,field = "status")
    private String status;

    @GraphQLField(kkhtml = "KFieldText", label = "渠道备注", sql = "remark = $S{remark}" ,field = "remark")
    private String remark;

    @GraphQLField(kkhtml = "KFieldText", label = "发布时间", sql = "channel_public_date = $S{channelPublicDate}" ,field = "channel_public_date")
    private String channelPublicDate;

    @GraphQLField(kkhtml = "KFieldText", label = "发布状态", sql = "notice_channel_public_status = $S{noticeChannelPublicStatus}" ,field = "notice_channel_public_status")
    private String noticeChannelPublicStatus;

    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
    private String createDate;

    @GraphQLField(kkhtml = "KFieldText", label = "创建人id", sql = "create_user_id = $S{createUserId}" ,field = "create_user_id")
    private String createUserId;

    @GraphQLField(kkhtml = "KFieldText", label = "信披文档版本", sql = "version = $S{version}" ,field = "version")
    private String version;

    @GraphQLField(kkhtml = "KFieldText", label = "信披公告版本id",sql = "disclosure_notice_version_id = $S{disclosureNoticeVersionId}" ,field = "disclosure_notice_version_id")
    private String noticeVersionId;

    @GraphQLField(kkhtml = "KFieldText", label = "发布日期",sql = "update_date = $S{updateDate}" ,field = "update_date")
    private String updateDate;

    @GraphQLField(kkhtml = "KFieldText", label = "发布时间",sql = "update_time = $S{updateTime}" ,field = "update_time")
    private String updateTime;

    @GraphQLField(kkhtml = "KFieldText", label = "发布时间",sql = "create_time = $S{createTime}" ,field = "create_time")
    private String createTime;

    @GraphQLField(kkhtml = "KFieldText", label = "发布时间",sql = "update_user_id = $S{updateUserId}" ,field = "update_user_id")
    private String updateUserId;

    @GraphQLField(kkhtml = "KFieldText", label = "发布时间",sql = "create_user_name = $S{createUserName}" ,field = "create_user_name")
    private String createUserName;

    @GraphQLField(kkhtml = "KFieldText", label = "模版名称")
    private String modName;
}