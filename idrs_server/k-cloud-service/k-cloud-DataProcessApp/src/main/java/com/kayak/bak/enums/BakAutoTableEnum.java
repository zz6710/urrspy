package com.kayak.bak.enums;

/**
 * 是否自动建表
 */
public enum BakAutoTableEnum {

    AUTO_TYPE("1", "自动建表"),
    NOT_AUTO_TYPE("2", "手动建表");

    BakAutoTableEnum(String val, String desc) {
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
