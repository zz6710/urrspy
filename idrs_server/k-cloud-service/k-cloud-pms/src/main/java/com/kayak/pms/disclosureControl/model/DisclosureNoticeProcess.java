package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureNoticeProcessService", table = "idb_disclosure_notice_process")
public class DisclosureNoticeProcess {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "公告id", sql = "t8_disclosure_notice_id = $S{t8DisclosureNoticeId}", field = "t8_disclosure_notice_id")
    private String t8DisclosureNoticeId;
    @GraphQLField(kkhtml = "KFieldText", label = "补录人角色", sql = "role_id = $S{roleId}", field = "role_id")
    private String roleId;
    @GraphQLField(kkhtml = "KFieldText", label = "需补录人id", sql = "user_id = $S{userId}", field = "user_id")
    private String userId;
    @GraphQLField(kkhtml = "KFieldText", label = "被转交人id", sql = "to_user_id = $S{toUserId}", field = "to_user_id")
    private String toUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "录入状态", sql = "input_status = $S{inputStatus}", field = "input_status")
    private String inputStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}", field = "crt_user_id")
    private String crtUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}", field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user_id = $S{updUserId}", field = "upd_user_id")
    private String updUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}", field = "upd_user_name")
    private String updUserName;

    @GraphQLField(kkhtml = "KFieldText", label = "启用状态", sql = "open_status = $S{openStatus}", field = "open_status")
    private String openStatus;
    @GraphQLField
    private String jsonData;
    @GraphQLField
    private String prodCode;
    @GraphQLField
    private String roleIds;
    @GraphQLField
    private String version;
    @GraphQLField(label = "信披任务ID")
    private String taskId;
    @GraphQLField(label = "不包括的公告id")
    private String notInNoticeIds;
    @GraphQLField(label = "信披类型")
    private String disclosureType;
}