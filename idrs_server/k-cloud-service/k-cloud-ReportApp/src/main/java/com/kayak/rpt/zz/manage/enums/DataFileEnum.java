package com.kayak.rpt.zz.manage.enums;

/**
 * @Author lll
 * @Date 2025/7/7 16:30
 * @Description TODO
 * @Version 1.0
 */
public enum DataFileEnum {
    APP_SUB_PRD_NAV_INF("app_sub_prd_nav_inf", "净值登记表"),
    EXTPID_118("118", "包ID"),
    REGISTER_TYPE_01("01", "母产品"),
    CNY("CNY", "人民币");

    private final String value;
    private final String desc;

    DataFileEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
