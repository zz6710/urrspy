package com.kayak.jimureport.report.entity;

public class ProFileCfgObjectVO {

    Integer id;
    String fieldMapping;
    String fieldCode;
    String fieldName;
    String tableInfo;
    String dict;
    String data_fmt;
    Integer data_len;
    String expression;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(String fieldMapping) {
        this.fieldMapping = fieldMapping;
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

    public String getTableInfo() {
        return tableInfo;
    }

    public void setTableInfo(String tableInfo) {
        this.tableInfo = tableInfo;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public String getData_fmt() {
        return data_fmt;
    }

    public void setData_fmt(String data_fmt) {
        this.data_fmt = data_fmt;
    }

    public Integer getData_len() {
        return data_len;
    }

    public void setData_len(Integer data_len) {
        this.data_len = data_len;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

}
