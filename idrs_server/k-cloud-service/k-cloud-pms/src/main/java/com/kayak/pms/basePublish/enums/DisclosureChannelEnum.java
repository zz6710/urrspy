package com.kayak.pms.basePublish.enums;

/**
 * @author WangTao
 * @version 1.0
 * @date 2022/7/12
 */
public enum DisclosureChannelEnum {
    OFFICIAL_BANK("官网", "1"),
    HUNAN_INSURANCE_BUREAU("湖南银保监局", "2"),
    INTERNET_BANK("网银", "3"),
    E_BANK_PERSON("E钱庄(个人)", "4"),
    FINANCE_MANAGEMENT("理财销售", "5"),
    OLD_ASSET_MANAGEMENT("老资管", "6"),
    E_BANK_ENTERPRISE("E钱庄(企业)", "7");

    private final String name;//输入类型
    private final String value;//描述
    DisclosureChannelEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
