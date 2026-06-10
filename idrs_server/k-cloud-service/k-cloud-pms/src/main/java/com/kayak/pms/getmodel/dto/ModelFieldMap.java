package com.kayak.pms.getmodel.dto;

public class ModelFieldMap {
    /*字段 name**/
    private String name;
    /*lable **/
    private String displayName;
    /*字段类型 **/
    private String fieldType;
    /* 数据字典**/
    private String dict;
    /* 字典字段值**/
    private String field_display;
    /* 字典字段名**/
    private String Field_value;
    /* 路径**/
    private String url;
    /* 数据验证**/
    private String validateJson;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public String getField_display() {
        return field_display;
    }

    public void setField_display(String field_display) {
        this.field_display = field_display;
    }

    public String getField_value() {
        return Field_value;
    }

    public void setField_value(String field_value) {
        Field_value = field_value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValidateJson() {
        return validateJson;
    }

    public void setValidateJson(String validateJson) {
        this.validateJson = validateJson;
    }
}
