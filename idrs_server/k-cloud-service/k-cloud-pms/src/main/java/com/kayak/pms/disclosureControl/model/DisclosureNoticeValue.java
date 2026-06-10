package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureNoticeValueService",table = "idb_disclosure_notice_value")
public class DisclosureNoticeValue {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "公告id", sql = "t8_disclosure_notice_id = $S{t8DisclosureNoticeId}" ,field = "t8_disclosure_notice_id")
    private String t8DisclosureNoticeId;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "t8_disclosure_version_id = $S{t8DisclosureVersionId}" ,field = "t8_disclosure_version_id")
    private String t8DisclosureVersionId;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "data_date = $S{dataDate}" ,field = "data_date")
    private String dataDate;
    @GraphQLField(kkhtml = "KFieldText", label = "字段描述", sql = "column_label = $S{columnLabel}" ,field = "column_label")
    private String columnLabel;
    @GraphQLField(kkhtml = "KFieldText", label = "字段key", sql = "column_key = $S{columnKey}" ,field = "column_key")
    private String columnKey;
    @GraphQLField(kkhtml = "KFieldText", label = "字段对应值", sql = "column_value = $S{columnValue}" ,field = "column_value")
    private String columnValue;
    @GraphQLField(kkhtml = "KFieldText", label = "是否显示", sql = "isdisplay = $S{isdisplay}" ,field = "isdisplay")
    private String isdisplay;
    @GraphQLField(kkhtml = "KFieldText", label = "有输入权限的角色", sql = "roleids = $S{roleids}" ,field = "roleids")
    private String roleids;
    @GraphQLField(kkhtml = "KFieldText", label = "取值类型", sql = "is_sysvalue = $S{isSysvalue}" ,field = "is_sysvalue")
    private String isSysvalue;
    @GraphQLField(kkhtml = "KFieldText", label = "转换的数据字典", sql = "dict = $S{dict}" ,field = "dict")
    private String dict;
    @GraphQLField(kkhtml = "KFieldText", label = "输入类型", sql = "functype = $S{functype}" ,field = "functype")
    private String functype;
    @GraphQLField(kkhtml = "KFieldText", label = "输入类型", sql = "data_type = $S{dataType}" ,field = "data_type")
    private String dataType;
    @GraphQLField(kkhtml = "KFieldText", label = "前端组件属性配置，需要JSON格式", sql = "confoption = $S{confoption}" ,field = "confoption")
    private String confoption;
    @GraphQLField(kkhtml = "KFieldText", label = "字段来源 0-基本信息 1-投资信息", sql = "source_type = $S{sourceType}" ,field = "source_type")
    private String sourceType;
    @GraphQLField(kkhtml = "KFieldText", label = "序号", sql = "seq_numbers = $S{seqNumbers}" ,field = "seq_numbers")
    private String seqNumbers;
    @GraphQLField(kkhtml = "KFieldText", label = "金额格式", sql = "money_format = $S{moneyFormat}" ,field = "money_format")
    private String moneyFormat;
    @GraphQLField(kkhtml = "KFieldText", label = "计算方式", sql = "computed_expression = $S{computedExpression}" ,field = "computed_expression")
    private String computedExpression;
    @GraphQLField(kkhtml = "KFieldText", label = "数据长度", sql = "data_length = $S{dataLength}" ,field = "data_length")
    private String dataLength;
    @GraphQLField(kkhtml = "KFieldText", label = "sql参数", sql = "sql_parameter = $S{sqlParameter}" ,field = "sql_parameter")
    private String sqlParameter;
    @GraphQLField(kkhtml = "KFieldText", label = "取值sql", sql = "value_sql = $S{valueSql}" ,field = "value_sql")
    private String valueSql;
    @GraphQLField(kkhtml = "KFieldText", label = "数据库数据源 0-PUB-公共信息库 1-IDB-信息披露库 2-SRB-监管报送库", sql = "data_source = $S{dataSource}" ,field = "data_source")
    private String dataSource;
    @GraphQLField(kkhtml = "KFieldText", label = "文件名称", sql = "file_name = $S{fileName}" ,field = "file_name")
    private String fileName;
    @GraphQLField(kkhtml = "KFieldText", label = "上传地址", sql = "upload_path = $S{uploadPath}" ,field = "upload_path")
    private String uploadPath;
    @GraphQLField(kkhtml = "KFieldText", label = "浏览地址", sql = "view_url = $S{viewUrl}" ,field = "view_url")
    private String viewUrl;
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
    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }
    public String getFunctype() {
        return functype;
    }

    public void setFunctype(String functype) {
        this.functype = functype;
    }
  	public String getConfoption() {
        return confoption;
    }

    public void setConfoption(String confoption) {
        this.confoption = confoption;
    }
  	public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
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