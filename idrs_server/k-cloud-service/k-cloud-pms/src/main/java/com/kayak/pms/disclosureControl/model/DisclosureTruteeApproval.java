package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureNoticeApprovalService",table = "idb_disclosure_notic_approval")
public class DisclosureTruteeApproval {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "定期报告表id", sql = "disclosure_notice_id = $S{disclosureNoticeId}" ,field = "disclosure_notice_id")
   private String disclosureNoticeId;
   @GraphQLField(kkhtml = "KFieldText", label = "托管行审批结果1通过0不通过", sql = "trutee_approval_result = $S{truteeApprovalResult}" ,field = "trutee_approval_result")
   private String truteeApprovalResult;
   @GraphQLField(kkhtml = "KFieldText", label = "托管机构附件地址", sql = "attachment_url = $S{attachmentUrl}" ,field = "attachment_url")
   private String attachmentUrl;
   @GraphQLField(kkhtml = "KFieldText", label = "托管机构报告", sql = "trutee_notice = $S{truteeNotice}" ,field = "trutee_notice")
   private String truteeNotice;
   @GraphQLField(kkhtml = "KFieldText", label = "审批状态 待发起 已发起 审批通过 审批拒绝", sql = "approval_status = $S{approvalStatus}" ,field = "approval_status")
   private String approvalStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "复核状态 待发起 已发起 审批通过 审批拒绝", sql = "recheck_status = $S{recheckStatus}" ,field = "recheck_status")
   private String recheckStatus;
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

    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "trutee_name = $S{truteeName}" ,field = "trutee_name")
    private String truteeName;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "trutee_email = $S{truteeEmail}" ,field = "trutee_email")
    private String truteeEmail;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "out_trutee_name = $S{outTruteeName}" ,field = "out_trutee_name")
    private String outTruteeName;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "out_trutee_email = $S{outTruteeEmail}" ,field = "out_trutee_email")
    private String outTruteeEmail;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "trustee_examine = $S{trusteeExamine}" ,field = "trustee_examine")
    private String trusteeExamine;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "upload_file_name = $S{uploadFileName}" ,field = "upload_file_name")
    private String uploadFileName;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "trutee_approval_result_desc = $S{truteeApprovalResultDesc}" ,field = "trutee_approval_result_desc")
    private String truteeApprovalResultDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "sysparam_trustee_approval = $S{sysParamTrusteeApproval}" ,field = "sysparam_trustee_approval")
    private String sysparamTrusteeApproval;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "recheck = $S{recheck}" ,field = "recheck")
    private String recheck;
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "trustee_approval_status = $S{trusteeApprovalStatus}" ,field = "trustee_approval_status")
    private String trusteeApprovalStatus;
    @GraphQLField
    private String operatingAgency;

    public String getTruteeName() {
        return truteeName;
    }

    public void setTruteeName(String truteeName) {
        this.truteeName = truteeName;
    }

    public String getTruteeEmail() {
        return truteeEmail;
    }

    public void setTruteeEmail(String truteeEmail) {
        this.truteeEmail = truteeEmail;
    }

    public String getOutTruteeName() {
        return outTruteeName;
    }

    public void setOutTruteeName(String outTruteeName) {
        this.outTruteeName = outTruteeName;
    }

    public String getOutTruteeEmail() {
        return outTruteeEmail;
    }

    public void setOutTruteeEmail(String outTruteeEmail) {
        this.outTruteeEmail = outTruteeEmail;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getTrusteeExamine() {
        return trusteeExamine;
    }

    public void setTrusteeExamine(String trusteeExamine) {
        this.trusteeExamine = trusteeExamine;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getTruteeApprovalResultDesc() {
        return truteeApprovalResultDesc;
    }

    public void setTruteeApprovalResultDesc(String truteeApprovalResultDesc) {
        this.truteeApprovalResultDesc = truteeApprovalResultDesc;
    }

    public String getSysparamTrusteeApproval() {
        return sysparamTrusteeApproval;
    }

    public void setSysparamTrusteeApproval(String sysparamTrusteeApproval) {
        this.sysparamTrusteeApproval = sysparamTrusteeApproval;
    }

    public String getRecheck() {
        return recheck;
    }

    public void setRecheck(String recheck) {
        this.recheck = recheck;
    }

    public String getTrusteeApprovalStatus() {
        return trusteeApprovalStatus;
    }

    public void setTrusteeApprovalStatus(String trusteeApprovalStatus) {
        this.trusteeApprovalStatus = trusteeApprovalStatus;
    }

    public String getOperatingAgency() {
        return operatingAgency;
    }

    public void setOperatingAgency(String operatingAgency) {
        this.operatingAgency = operatingAgency;
    }

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
  	public String getTruteeApprovalResult() {
        return truteeApprovalResult;
    }

    public void setTruteeApprovalResult(String truteeApprovalResult) {
        this.truteeApprovalResult = truteeApprovalResult;
    }
  	public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
  	public String getTruteeNotice() {
        return truteeNotice;
    }

    public void setTruteeNotice(String truteeNotice) {
        this.truteeNotice = truteeNotice;
    }
  	public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
  	public String getRecheckStatus() {
        return recheckStatus;
    }

    public void setRecheckStatus(String recheckStatus) {
        this.recheckStatus = recheckStatus;
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