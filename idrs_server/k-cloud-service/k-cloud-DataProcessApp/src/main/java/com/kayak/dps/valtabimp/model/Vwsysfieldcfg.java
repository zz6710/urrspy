package com.kayak.dps.valtabimp.model;

public class Vwsysfieldcfg {
    private String field_code;
    private String field_name;
    private Integer data_type;
    private String data_fmt;
    private String group_field;
    private String date_len;
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
    public Integer getData_type() {
        return data_type;
    }
    public void setData_type(Integer data_type) {
        this.data_type = data_type;
    }
    public String getData_fmt() {
        return data_fmt;
    }
    public void setData_fmt(String data_fmt) {
        this.data_fmt = data_fmt;
    }
    public String getGroup_field() {
        return group_field;
    }
    public void setGroup_field(String group_field) {
        this.group_field = group_field;
    }
    public String getDate_len() {
        return date_len;
    }
    public void setDate_len(String date_len) {
        this.date_len = date_len;
    }
    public Vwsysfieldcfg(String field_code, String field_name,
                         Integer data_type, String data_fmt, String group_field) {
        super();
        this.field_code = field_code;
        this.field_name = field_name;
        this.data_type = data_type;
        this.data_fmt = data_fmt;
        this.group_field = group_field;
    }

    public Vwsysfieldcfg(){};
}
