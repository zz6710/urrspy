package com.kayak.bak.enums;

/**
 * 是否保留源数据
 */
public enum SourceDataEnum {

    HAS_DATA("1", "删除"),
    DEL_DATA("2", "保留");

    SourceDataEnum(String val, String desc) {
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
