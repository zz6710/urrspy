package com.kayak.dps.app.model;

public class ReportColumnModel {
    //报送业务表名
    private String report_table;
    //字段类型:C-列 R-行
    private String field_type;
    //字段位置
    private String field_index;
    //字段代码
    private String field_code;
    //字段名称
    private String field_name;

    public ReportColumnModel(String report_table, String field_type, String field_index, String field_code, String field_name) {
        this.report_table = report_table;
        this.field_type = field_type;
        this.field_index = field_index;
        this.field_code = field_code;
        this.field_name = field_name;
    }

    public String getReport_table() {
        return report_table;
    }

    public void setReport_table(String report_table) {
        this.report_table = report_table;
    }

    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public String getField_index() {
        return field_index;
    }

    public void setField_index(String field_index) {
        this.field_index = field_index;
    }

    public String getField_code() {
        return field_code;
    }

    public void setField_code(String field_code) {
        this.field_code = field_code;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }
}