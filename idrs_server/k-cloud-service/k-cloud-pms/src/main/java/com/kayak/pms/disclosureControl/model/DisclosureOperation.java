package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * 信披待办任务
 */
@Data
@GraphQLModel(fetcher = "disclosureOperationService",table = "idb_disclosure_operation")
public class DisclosureOperation {
    public DisclosureOperation() {
    }

    public DisclosureOperation(String operationType, String disclosureType, String roleid, String userid, String status, String dealTable, String dealId, String crtDate, String crtTime, String crtUserId, String crtUserName, String endDate, String endTime, String remark, String prodCode, String notInNoticeIds, String roleIds, String taskId) {
        this.operationType = operationType;
        this.disclosureType = disclosureType;
        this.roleid = roleid;
        this.userid = userid;
        this.status = status;
        this.dealTable = dealTable;
        this.dealId = dealId;
        this.crtDate = crtDate;
        this.crtTime = crtTime;
        this.crtUserId = crtUserId;
        this.crtUserName = crtUserName;
        this.endDate = endDate;
        this.endTime = endTime;
        this.remark = remark;
        this.prodCode = prodCode;
        this.notInNoticeIds = notInNoticeIds;
        this.roleIds = roleIds;
        this.taskId = taskId;
    }

    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "待办类型", sql = "operation_type = $S{operationType}" ,field = "operation_type")
   private String operationType;
   @GraphQLField(kkhtml = "KFieldText", label = "信披类型", sql = "disclosure_type = $S{disclosureType}" ,field = "disclosure_type")
   private String disclosureType;
   @GraphQLField(kkhtml = "KFieldText", label = "待办角色ID", sql = "roleid = $S{roleid}" ,field = "roleid")
   private String roleid;
   @GraphQLField(kkhtml = "KFieldText", label = "待办用户ID", sql = "userid = $S{userid}" ,field = "userid")
   private String userid;
   @GraphQLField(kkhtml = "KFieldText", label = "待办状态", sql = "status = $S{status}" ,field = "status")
   private String status;
   @GraphQLField(kkhtml = "KFieldText", label = "对应业务主表", sql = "deal_table = $S{dealTable}" ,field = "deal_table")
   private String dealTable;
   @GraphQLField(kkhtml = "KFieldText", label = "业务流水id", sql = "deal_id = $S{dealId}" ,field = "deal_id")
   private String dealId;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}" ,field = "crt_user_id")
   private String crtUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}", field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "办结日期", sql = "end_date = $S{endDate}", field = "end_date")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", label = "办结时间", sql = "end_time = $S{endTime}", field = "end_time")
    private String endTime;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField
    private String prodCode;
    @GraphQLField(label = "不包括的公告id")
    private String notInNoticeIds;
    @GraphQLField
    private String roleIds;
    @GraphQLField(label = "信披任务id")
    private String taskId;
}