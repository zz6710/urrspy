package com.kayak.dps.check.enums;

public enum ReportCoordinateEnum {

    COOR_A("1", "dat一维报表"),
    COOR_B("2", "二维坐标报表(人行/G06)"),
    COOR_C("3", "dat一维报表"),
    COOR_D("4", "一维坐标报表(中债/资金信托/行内客制化报表等)");

    // 成员变量
    private String val;
    private String desc;

    // 构造方法
    private ReportCoordinateEnum(String val, String desc) {
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
