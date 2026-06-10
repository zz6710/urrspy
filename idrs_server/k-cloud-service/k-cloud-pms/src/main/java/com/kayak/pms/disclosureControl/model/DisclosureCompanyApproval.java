package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureCompanyApprovalService",table = "idb_disclosure_company_approval")
public class DisclosureCompanyApproval {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "信披公告表id", sql = "disclosure_notice_id = $S{disclosureNoticeId}" ,field = "disclosure_notice_id")
   private String disclosureNoticeId;
   @GraphQLField(kkhtml = "KFieldText", label = "流程审批条件", sql = "flow_approval_condition = $S{flowApprovalCondition}" ,field = "flow_approval_condition")
   private String flowApprovalCondition;
   @GraphQLField(kkhtml = "KFieldText", label = "需关注点", sql = "concerns = $S{concerns}" ,field = "concerns")
   private String concerns;
   @GraphQLField(kkhtml = "KFieldText", label = "审批状态", sql = "approval_status = $S{approvalStatus}" ,field = "approval_status")
   private String approvalStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "update_time = $S{updateTime}" ,field = "update_time")
   private String updateTime;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "create_user_id = $S{createUserId}" ,field = "create_user_id")
   private String createUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "create_user_name = $S{createUserName}" ,field = "create_user_name")
   private String createUserName;

    @GraphQLField()
    private String t8ProdInfoId;

  	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  	public String getDisclosureNoticeId() {
        return disclosureNoticeId;
    }

    public void setDisclosureNoticeId(String disclosureNoticeId) {
        this.disclosureNoticeId = disclosureNoticeId;
    }
  	public String getFlowApprovalCondition() {
        return flowApprovalCondition;
    }

    public void setFlowApprovalCondition(String flowApprovalCondition) {
        this.flowApprovalCondition = flowApprovalCondition;
    }
  	public String getConcerns() {
        return concerns;
    }

    public void setConcerns(String concerns) {
        this.concerns = concerns;
    }
  	public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
  	public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
  	public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
  	public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
  	public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
  	public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
  	public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

}