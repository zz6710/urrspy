package com.kayak.pms.global.constants;

/**
* @功能描述:是否对接渠道
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum IsSysvalue {

    sys("1", "自动取值"),
    hand("2", "手工维护");
    private final String itemKey;
    private final String itemVal;
    IsSysvalue(String itemKey, String itemVal) {
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
