package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8DisclosureNoticeChannelService",table = "idb_disclosure_notice_channel")
public class T8DisclosureNoticeChannel {
    @GraphQLField(kkhtml = "KFieldText", label = "渠道上传文件名称", sql = "upload_file_name = $S{uploadFileName}" ,field = "upload_file_name")
    private String uploadFileName;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "公告表id", sql = "disclosure_notice_id = $S{disclosureNoticeId}" ,field = "disclosure_notice_id")
   private String disclosureNoticeId;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码",field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称",field = "prod_name")
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "渠道名称",field = "channel_name")
    private String channelName;
   @GraphQLField(kkhtml = "KFieldText", label = "公告相关渠道id", sql = "disclosure_notice_channel_id = $S{disclosureNoticeChannelId}" ,field = "disclosure_notice_channel_id")
   private String disclosureNoticeChannelId;
   @GraphQLField(kkhtml = "KFieldText", label = "发布状态", sql = "notice_channel_public_status = $S{noticeChannelPublicStatus}" ,field = "notice_channel_public_status")
   private String noticeChannelPublicStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "发布时间", sql = "channel_public_date = $S{channelPublicDate}" ,field = "channel_public_date")
   private String channelPublicDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "update_time = $S{updateTime}" ,field = "update_time")
   private String updateTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人id", sql = "create_user_id = $S{createUserId}" ,field = "create_user_id")
   private String createUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人id", sql = "update_user_id = $S{updateUserId}" ,field = "update_user_id")
   private String updateUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人姓名", sql = "create_user_name = $S{createUserName}" ,field = "create_user_name")
   private String createUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "信披类型", field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(kkhtml = "KFieldText", label = "基准日期", sql = "prod_base_date = $S{prodBaseDate}" ,field = "prod_base_date")
    private String prodBaseDate;
    @GraphQLField(kkhtml = "KFieldText", label = "基准日期", field = "public_date_start")
    private String publicDateStart;
    @GraphQLField(kkhtml = "KFieldText", label = "基准日期", field = "public_date_end")
    private String publicDateEnd;
    @GraphQLField(kkhtml = "KFieldText", label = "公告版本表id", field = "disclosure_notice_version_id")
    private String disclosureNoticeVersionId;
   
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
  	public String getDisclosureNoticeChannelId() {
        return disclosureNoticeChannelId;
    }

    public void setDisclosureNoticeChannelId(String disclosureNoticeChannelId) {
        this.disclosureNoticeChannelId = disclosureNoticeChannelId;
    }
  	public String getNoticeChannelPublicStatus() {
        return noticeChannelPublicStatus;
    }

    public void setNoticeChannelPublicStatus(String noticeChannelPublicStatus) {
        this.noticeChannelPublicStatus = noticeChannelPublicStatus;
    }
  	public String getChannelPublicDate() {
        return channelPublicDate;
    }

    public void setChannelPublicDate(String channelPublicDate) {
        this.channelPublicDate = channelPublicDate;
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
  	public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
  	public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

}