package com.kayak.dps.check.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "portFieldManageInfoService",table = "base_port_field_manage")
public class PortFieldManageInfo {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID ", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "接口代码 ", sql = "port_code = $S{portCode}" ,field = "port_code")
   private String portCode;
   @GraphQLField(kkhtml = "KFieldText", label = "字段代码 ", sql = "field_code = $S{fieldCode}" ,field = "field_code")
   private String fieldCode;
   @GraphQLField(kkhtml = "KFieldText", label = "字段名称 ", sql = "field_name like '%$U{fieldName}%'" ,field = "field_name")
   private String fieldName;
   @GraphQLField(kkhtml = "KFieldText", label = "字段类型 ", sql = "field_type = $S{fieldType}" ,field = "field_type")
   private String fieldType;
   @GraphQLField(kkhtml = "KFieldText", label = "字段长度 ", sql = "field_length = $S{fieldLength}" ,field = "field_length")
   private String fieldLength;
   @GraphQLField(kkhtml = "KFieldText", label = "字段小数位 ", sql = "field_dights = $S{fieldDights}" ,field = "field_dights")
   private String fieldDights;
   @GraphQLField(kkhtml = "KFieldText", label = "字段序号 ", sql = "field_seq = $S{fieldSeq}" ,field = "field_seq")
   private String fieldSeq;
   @GraphQLField(kkhtml = "KFieldText", label = "文件字段代码 ", sql = "file_field_code = $S{fileFieldCode}" ,field = "file_field_code")
   private String fileFieldCode;
   @GraphQLField(kkhtml = "KFieldText", label = "录入柜员 ", sql = "inputuser like '%$U{inputuser}%'" ,field = "inputuser")
   private String inputuser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期 ", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间 ", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期 ", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间 ", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;

  	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  	public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }
  	public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }
  	public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
  	public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
  	public String getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(String fieldLength) {
        this.fieldLength = fieldLength;
    }
  	public String getFieldDights() {
        return fieldDights;
    }

    public void setFieldDights(String fieldDights) {
        this.fieldDights = fieldDights;
    }
  	public String getFieldSeq() {
        return fieldSeq;
    }

    public void setFieldSeq(String fieldSeq) {
        this.fieldSeq = fieldSeq;
    }
  	public String getInputuser() {
        return inputuser;
    }

    public void setInputuser(String inputuser) {
        this.inputuser = inputuser;
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

    public String getFileFieldCode() {
        return fileFieldCode;
    }

    public void setFileFieldCode(String fileFieldCode) {
        this.fileFieldCode = fileFieldCode;
    }
}
