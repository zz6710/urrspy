package com.kayak.dps.direct.enums;

/**
 * 直连报送日期格式更改枚举
 */
public enum DataFileDirectEnum {

    VALUATION_DATE("VALUATION_DATE", "估值日期"),
    TRADE_DATE("TRADE_DATE", "交易日"),
    ACTUAL_PROD_TER_DATE("ACTUAL_PROD_TER_DATE", "理财产品实际终止日期"),
    NAV_DT("NAV_DT", "净值日期"),
    SUBSCRIPTION_START_DATE("SUBSCRIPTION_START_DATE", "募集起始日期"),
    SUBSCRIPTION_END_DATE("SUBSCRIPTION_END_DATE", "募集结束日期"),
    PROD_VALUE_DATE("PROD_VALUE_DATE", "产品起始日期"),
    PROD_MATURITY_DATE("PROD_MATURITY_DATE", "产品终止日期"),
    BUSINESS_START_DATE("BUSINESS_START_DATE", "业务起始日期"),
    BUSINESS_END_DATE("BUSINESS_END_DATE", "业务结束日期"),
    REPORT_DATE("REPORT_DATE", "业务结束日期"),
    VALDATE("VALDATE", "产品状态统计日");

    private String value;
    private String desc;//描述

    DataFileDirectEnum(String value, String desc) {
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
