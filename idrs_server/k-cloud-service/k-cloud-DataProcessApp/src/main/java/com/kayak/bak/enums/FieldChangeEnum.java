package com.kayak.bak.enums;

/**
 * 归档表字段变动处理
 */
public enum FieldChangeEnum {

    FIELD_AUTO("1", "自动处理"),
    FIELD_NOT("2", "非自动处理");

    FieldChangeEnum(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    private String val;
    private String desc;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
