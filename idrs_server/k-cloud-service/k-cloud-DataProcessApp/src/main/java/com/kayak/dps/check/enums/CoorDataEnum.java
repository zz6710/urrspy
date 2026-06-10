package com.kayak.dps.check.enums;

public enum CoorDataEnum {

    COOR_DATA_TAG("[?]", "二维报表通用日志数据占位符");

    // 成员变量
    private String val;
    private String desc;

    // 构造方法
    private CoorDataEnum(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }

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
