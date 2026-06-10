package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "disclosureNoticeVersionValueService",table = "idb_disclosure_notice_version_value")
public class DisclosureNoticeVersionValue {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "公告id", sql = "t8_disclosure_notice_id = $S{t8DisclosureNoticeId}" ,field = "t8_disclosure_notice_id")
   private String t8DisclosureNoticeId;
   @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "t8_disclosure_version_id = $S{t8DisclosureVersionId}" ,field = "t8_disclosure_version_id")
   private String t8DisclosureVersionId;
   @GraphQLField(kkhtml = "KFieldText", label = "公告版本Id", sql = "t8_disclosure_notice_version_id = $S{t8DisclosureNoticeVersionId}" ,field = "t8_disclosure_notice_version_id")
   private String t8DisclosureNoticeVersionId;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "data_date = $S{dataDate}" ,field = "data_date")
   private String dataDate;
   @GraphQLField(kkhtml = "KFieldText", label = "字段key", sql = "column_key = $S{columnKey}" ,field = "column_key")
   private String columnKey;
   @GraphQLField(kkhtml = "KFieldText", label = "字段对应值", sql = "column_value = $S{columnValue}" ,field = "column_value")
   private String columnValue;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}" ,field = "crt_user_id")
   private String crtUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}" ,field = "crt_user_name")
   private String crtUserName;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user_id = $S{updUserId}" ,field = "upd_user_id")
   private String updUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}" ,field = "upd_user_name")
   private String updUserName;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   
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
  	public String getT8DisclosureVersionId() {
        return t8DisclosureVersionId;
    }

    public void setT8DisclosureVersionId(String t8DisclosureVersionId) {
        this.t8DisclosureVersionId = t8DisclosureVersionId;
    }
  	public String getT8DisclosureNoticeVersionId() {
        return t8DisclosureNoticeVersionId;
    }

    public void setT8DisclosureNoticeVersionId(String t8DisclosureNoticeVersionId) {
        this.t8DisclosureNoticeVersionId = t8DisclosureNoticeVersionId;
    }
  	public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
  	public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }
  	public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }
  	public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
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
  	public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }
  	public String getUpdUserName() {
        return updUserName;
    }

    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }
  	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}