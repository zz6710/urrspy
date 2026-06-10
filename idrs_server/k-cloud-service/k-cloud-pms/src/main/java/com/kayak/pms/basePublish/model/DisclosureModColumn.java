package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureModColumnService",table = "idb_disclosure_mod_column")
public class DisclosureModColumn {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "disclosure_mod_version_id = $S{disclosureModVersionId}" ,field = "disclosure_mod_version_id")
   private String disclosureModVersionId;
   @GraphQLField(kkhtml = "KFieldText", label = "源字段id", sql = "t8_disclosure_source_id = $S{t8DisclosureSourceId}" ,field = "t8_disclosure_source_id")
   private String t8DisclosureSourceId;
   @GraphQLField(kkhtml = "KFieldText", label = "字段描述", sql = "column_label = $S{columnLabel}" ,field = "column_label")
   private String columnLabel;
   @GraphQLField(kkhtml = "KFieldText", label = "字段key", sql = "column_key = $S{columnKey}" ,field = "column_key")
   private String columnKey;
   @GraphQLField(kkhtml = "KFieldText", label = "字段默认值", sql = "column_value = $S{columnValue}" ,field = "column_value")
   private String columnValue;
   @GraphQLField(kkhtml = "KFieldText", label = "是否显示", sql = "isdisplay = $S{isdisplay}" ,field = "isdisplay")
   private String isdisplay;
   @GraphQLField(kkhtml = "KFieldText", label = "有输入权限的角色", sql = "roleids = $S{roleids}" ,field = "roleids")
   private String roleids;
   @GraphQLField(kkhtml = "KFieldText", label = "有输入权限的用户", sql = "userid = $S{userid}" ,field = "userid")
   private String userid;
   @GraphQLField(kkhtml = "KFieldText", label = "取值类型", sql = "is_sysvalue = $S{isSysvalue}" ,field = "is_sysvalue")
   private String isSysvalue;
   @GraphQLField(kkhtml = "KFieldText", label = "取值sql", sql = "sqls = $S{sqls}" ,field = "sqls")
   private String sqls;
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
   
   @GraphQLField(kkhtml = "KFieldText", label = "文件名称", sql = "file_name = $S{fileName}" ,field = "fileName")
   private String fileName;
   @GraphQLField(kkhtml = "KFieldText", label = "上传地址", sql = "upload_path = $S{uploadPath}" ,field = "uploadPath")
   private String uploadPath;
   @GraphQLField(kkhtml = "KFieldText", label = "浏览地址", sql = "view_url = $S{viewUrl}" ,field = "viewUrl")
   private String viewUrl;
    @GraphQLField(kkhtml = "KFieldText", label = "序号",field = "rowNum")
    private String rowNum;
   @GraphQLField
   private String datas;//前台传入的json字符串
   @GraphQLField
   private String t8ProdInfoId;//前台传入的json字符串
    @GraphQLField
    private String dataType;//数据类型,0 基本信息 1 投资信息
    @GraphQLField
    private String t8DisclosureNoticeId;//公告id
    @GraphQLField
    private String noticeVersionId;//公告版本id
    @GraphQLField
    private String userId;
    @GraphQLField
    private String prodCode;
    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getT8ProdInfoId() {
        return t8ProdInfoId;
    }

    public void setT8ProdInfoId(String t8ProdInfoId) {
        this.t8ProdInfoId = t8ProdInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDatas() {
	return datas;
}

public void setDatas(String datas) {
	this.datas = datas;
}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  	public String getT8DisclosureVersionId() {
        return disclosureModVersionId;
    }

    public void setT8DisclosureVersionId(String disclosureModVersionId) {
        this.disclosureModVersionId = disclosureModVersionId;
    }
  	public String getT8DisclosureSourceId() {
        return t8DisclosureSourceId;
    }

    public void setT8DisclosureSourceId(String t8DisclosureSourceId) {
        this.t8DisclosureSourceId = t8DisclosureSourceId;
    }
  	public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
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
  	public String getIsdisplay() {
        return isdisplay;
    }

    public void setIsdisplay(String isdisplay) {
        this.isdisplay = isdisplay;
    }
  	public String getRoleids() {
        return roleids;
    }

    public void setRoleids(String roleids) {
        this.roleids = roleids;
    }
  	public String getIsSysvalue() {
        return isSysvalue;
    }

    public void setIsSysvalue(String isSysvalue) {
        this.isSysvalue = isSysvalue;
    }
  	public String getSqls() {
        return sqls;
    }

    public void setSqls(String sqls) {
        this.sqls = sqls;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

}