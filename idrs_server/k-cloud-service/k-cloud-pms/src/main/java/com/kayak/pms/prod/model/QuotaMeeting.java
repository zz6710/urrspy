package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "quotaMeetingService",table = "t8_quota_meeting")
public class QuotaMeeting {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品id", sql = "t8_prod_info_id = $S{t8ProdInfoId}" ,field = "t8_prod_info_id")
   private String t8ProdInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "会议名称", sql = "meeting_name = $S{meetingName}" ,field = "meeting_name")
   private String meetingName;
   @GraphQLField(kkhtml = "KFieldText", label = "会议日期", sql = "meeting_date = $S{meetingDate}" ,field = "meeting_date")
   private String meetingDate;
   @GraphQLField(kkhtml = "KFieldText", label = "会议时间", sql = "meeting_time = $S{meetingTime}" ,field = "meeting_time")
   private String meetingTime;
   @GraphQLField(kkhtml = "KFieldText", label = "会议地址", sql = "meeting_addr = $S{meetingAddr}" ,field = "meeting_addr")
   private String meetingAddr;
   @GraphQLField(kkhtml = "KFieldText", label = "决策日期", sql = "decision_date = $S{decisionDate}" ,field = "decision_date")
   private String decisionDate;
   @GraphQLField(kkhtml = "KFieldText", label = "决策人", sql = "decision_maker = $S{decisionMaker}" ,field = "decision_maker")
   private String decisionMaker;
   @GraphQLField(kkhtml = "KFieldText", label = "与会人", sql = "participant = $S{participant}" ,field = "participant")
   private String participant;
   @GraphQLField(kkhtml = "KFieldText", label = "会议状态(1", sql = "meeting_state = $S{meetingState}" ,field = "meeting_state")
   private String meetingState;
   @GraphQLField(kkhtml = "KFieldSelect", label = "决策类型", sql = "type = $S{type}" ,field = "type",kkhtmlExt = "{\"data-dict\":\"decision_type\"}")
   private String type;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "提交人员", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
   private String inputuser;
   @GraphQLField(kkhtml = "KFieldText", label = "修改人员", sql = "updateuser = $S{updateuser}" ,field = "updateuser")
   private String updateuser;
   @GraphQLField(kkhtml = "KFieldText", label = "临时与会决策人员", sql = "temp_users = $S{tempUsers}" ,field = "temp_users")
   private String tempUsers;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;



   //用于删除附件
    @GraphQLField(field = "parentId")
    private String parentId;
    @GraphQLField(field = "prod_document_id")
    private String prodDocumentId;
    @GraphQLField(field = "path")
    private String path;
    @GraphQLField(field = "file_name")
    private String fileName;
    @GraphQLField(field = "attachment_type")
    private String attachmentType;
    @GraphQLField(field = "distributor_code")
    private String distributorCode;
    @GraphQLField(field = "t8_trutee_info_id")
    private String t8TruteeInfoId;
    @GraphQLField
    private String documentType;


  	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  	public String getT8ProdInfoId() {
        return t8ProdInfoId;
    }

    public void setT8ProdInfoId(String t8ProdInfoId) {
        this.t8ProdInfoId = t8ProdInfoId;
    }
  	public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
    }
  	public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }
  	public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }
  	public String getMeetingAddr() {
        return meetingAddr;
    }

    public void setMeetingAddr(String meetingAddr) {
        this.meetingAddr = meetingAddr;
    }
  	public String getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(String decisionDate) {
        this.decisionDate = decisionDate;
    }
  	public String getDecisionMaker() {
        return decisionMaker;
    }

    public void setDecisionMaker(String decisionMaker) {
        this.decisionMaker = decisionMaker;
    }
  	public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }
  	public String getMeetingState() {
        return meetingState;
    }

    public void setMeetingState(String meetingState) {
        this.meetingState = meetingState;
    }
  	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
  	public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
  	public String getInputuser() {
        return inputuser;
    }

    public void setInputuser(String inputuser) {
        this.inputuser = inputuser;
    }
  	public String getUpdateuser() {
        return updateuser;
    }

    public void setUpdateuser(String updateuser) {
        this.updateuser = updateuser;
    }
  	public String getCrtDate() {
        return crtDate;
    }

    public void setCrtDate(String crtDate) {
        this.crtDate = crtDate;
    }
  	public String getCrtTime() {
        return crtTime;
    }

    public void setCrtTime(String crtTime) {
        this.crtTime = crtTime;
    }
  	public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }
  	public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

}