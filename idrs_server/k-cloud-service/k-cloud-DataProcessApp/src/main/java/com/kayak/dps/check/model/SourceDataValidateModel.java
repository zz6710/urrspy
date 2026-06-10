package com.kayak.dps.check.model;

public class SourceDataValidateModel {
    //校验表名
    private String tableName = "";
    //字段代码
    private String columnCode = "";
    //字段名称
    private String columnName = "";
    //校验类别:01-非空校验/02-值域校验/03-字段格式及长度校验/04-文件格式及大小校验/05-字段联动校验/06-数字校验/07-重复性校验/08-身份证校验/09-计算校验
    private String indexType = "";
    //校验公式
    private String express = "";
    //说明
    private String remark = "";

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SourceDataValidateModel{" +
                "tableName='" + tableName + '\'' +
                ", columnCode='" + columnCode + '\'' +
                ", columnName='" + columnName + '\'' +
                ", indexType='" + indexType + '\'' +
                ", express='" + express + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
