package com.kayak.pms.global.constants;

/**
* @功能描述:信披子类型字典枚举
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum MoneyFormat {
    small("1", "小写金额"),
    big("2", "大写金额");
    private final String itemKey;//输入类型
    private final String itemVal;//描述
    MoneyFormat(String itemKey, String itemVal) {
        this.itemKey = itemKey;
        this.itemVal = itemVal;
    }

    public String getItemKey() {
        return itemKey;
    }

    public String getItemVal() {
        return itemVal;
    }
}
