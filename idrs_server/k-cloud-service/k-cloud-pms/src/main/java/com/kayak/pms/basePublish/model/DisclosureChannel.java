package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * function:信披渠道实体
 */

@Data
@GraphQLModel(fetcher = "disclosureChannelService", table = "idb_disclosure_channel")
public class DisclosureChannel {

    @GraphQLField(label = "id", sql = "ID = '%$S{id}%'", field = "ID", key = true)
    private String id;

    @GraphQLField(label = "渠道名称", sql = "CHANNEL_NAME like '%$U{channelName}%'", field = "CHANNEL_NAME")
    private String channelName;

    @GraphQLField(label = "是否对接", sql = "IS_DOCKING = $S{isDocking}", field = "IS_DOCKING")
    private String isDocking;

    @GraphQLField(label = "对接方式", sql = "DOCKING_WAY = $S{dockingWay}", field = "DOCKING_WAY")
    private String dockingWay;

    @GraphQLField(label = "主机IP", sql = "HOST_IP=$S{hostIp}", field = "HOST_IP")
    private String hostIp;

    @GraphQLField(label = "协议", sql = "PROTOCOL=$S{protocol}", field = "PROTOCOL")
    private String protocol;

    @GraphQLField(label = "端口号", sql = "PORT_CODE=$S{portCode}", field = "PORT_CODE")
    private String portCode;

    @GraphQLField(label = "用户名", sql = "USER_NAME=$S{userName}", field = "USER_NAME")
    private String userName;

    @GraphQLField(label = "密码", sql = "PASSWORD=$S{password}", field = "PASSWORD")
    private String password;

    @GraphQLField(label = "文件路径" , sql = "FILE_PATH=$S{filePath}", field = "FILE_PATH")
    private String filePath;

    @GraphQLField(label = "状态", sql = "STATUS=$S{status}", field = "STATUS")
    private String status;

    @GraphQLField(label = "备注", sql = "REMARK=$S{remark}", field = "REMARK")
    private String remark;

    @GraphQLField(label = "创建日期", sql = "crt_date=$S{crtDate}", field = "crt_date")
    private String crtDate;

    @GraphQLField(label = "创建时间", sql = "crt_time=$S{crtTime}", field = "crt_time")
    private String crtTime;

    @GraphQLField(label = "创建人", sql = "crt_user_id=$S{crtUserId}", field = "crt_user_id")
    private String crtUserId;

    @GraphQLField(label = "创建人名称", sql = "crt_user_name=$S{crtUserName}", field = "crt_user_name")
    private String crtUserName;

    @GraphQLField(label = "更新日期", sql = "upd_date=$S{updDate}", field = "upd_date")
    private String updDate;

    @GraphQLField(label = "更新时间", sql = "upd_time=$S{updTime}", field = "upd_time")
    private String updTime;

    @GraphQLField(label = "更新人", sql = "upd_user_id=$S{updUserId}", field = "upd_user_id")
    private String updUserId;

    @GraphQLField(label = "更新人名称", sql = "upd_user_name=$S{updUserName}", field = "upd_user_name")
    private String updUserName;
    @GraphQLField
    private String initPassword;

    @GraphQLField(label = "确认文件后缀", sql = "suffix_file_name=$S{suffixFileName}", field = "suffix_file_name")
    private String suffixFileName;
}
