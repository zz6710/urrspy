package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureChangeNoticeStatusService",table = "idb_disclosure_notice_status_record")
public class DisclosureChangeNoticeStatus {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "信披公告id", sql = "t8_disclosure_notice_id = $S{t8DisclosureNoticeId}" ,field = "t8_disclosure_notice_id")
    private String t8DisclosureNoticeId;
    @GraphQLField(kkhtml = "KFieldText", label = "变更前状态", sql = "disclosure_status_ahead = $S{disclosureStatusAhead}" ,field = "disclosure_status_ahead")
    private String disclosureStatusAhead;
    @GraphQLField(kkhtml = "KFieldText", label = "变更后状态", sql = "disclosure_status_after = $S{disclosureStatusAfter}" ,field = "disclosure_status_after")
    private String disclosureStatusAfter;
    @GraphQLField(kkhtml = "KFieldText", label = "变更原因", sql = "change_reason = $S{changeReason}" ,field = "change_reason")
    private String changeReason;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}" ,field = "crt_user_id")
    private String crtUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{prodCode}" ,field = "crtUserName")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "信披类型", sql = "disclosure_type = $S{disclosureType}" ,field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", sql = "disclosure_son_type = $S{disclosureSonType}" ,field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "notice_version = $S{noticeVersion}" ,field = "notice_version")
    private String noticeVersion;
    @GraphQLField(kkhtml = "KFieldText", label = "公告标题", sql = "notice_title = $S{noticeTitle}" ,field = "notice_title")
    private String noticeTitle;
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "crt_date >= $S{beginDate}" ,field = "begin_date")
    private String beginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "crt_date <= $S{queryDate}" ,field = "query_date")
    private String queryDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getT8DisclosureNoticeId() {
        return t8DisclosureNoticeId;
    }

    public void setT8DisclosureNoticeId(String t8DisclosureNoticeId) {
        this.t8DisclosureNoticeId = t8DisclosureNoticeId;
    }

    public String getDisclosureStatusAhead() {
        return disclosureStatusAhead;
    }

    public void setDisclosureStatusAhead(String disclosureStatusAhead) {
        this.disclosureStatusAhead = disclosureStatusAhead;
    }

    public String getDisclosureStatusAfter() {
        return disclosureStatusAfter;
    }

    public void setDisclosureStatusAfter(String disclosureStatusAfter) {
        this.disclosureStatusAfter = disclosureStatusAfter;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
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

    public String getCrtUserId() {
        return crtUserId;
    }

    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    public String getCrtUserName() {
        return crtUserName;
    }

    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    public String getDisclosureType() {
        return disclosureType;
    }

    public void setDisclosureType(String disclosureType) {
        this.disclosureType = disclosureType;
    }

    public String getDisclosureSonType() {
        return disclosureSonType;
    }

    public void setDisclosureSonType(String disclosureSonType) {
        this.disclosureSonType = disclosureSonType;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getNoticeVersion() {
        return noticeVersion;
    }

    public void setNoticeVersion(String noticeVersion) {
        this.noticeVersion = noticeVersion;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }
}