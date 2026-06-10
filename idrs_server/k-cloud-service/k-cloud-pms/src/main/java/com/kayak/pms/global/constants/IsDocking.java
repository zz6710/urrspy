package com.kayak.pms.global.constants;

/**
* @功能描述:是否对接渠道
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum IsDocking {

    yes("1", "对接渠道"),
    no("0", "不对接渠道");
    private final String itemKey;
    private final String itemVal;
    IsDocking(String itemKey, String itemVal) {
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
