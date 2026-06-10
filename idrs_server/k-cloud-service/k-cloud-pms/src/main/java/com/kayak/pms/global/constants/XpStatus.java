package com.kayak.pms.global.constants;

/**
* @功能描述:募集方式字典
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum XpStatus {

    stop("0", "停用"),
    start("1", "启用");
    private final String itemKey;
    private final String itemVal;
    XpStatus(String itemKey, String itemVal) {
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
