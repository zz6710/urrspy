package com.kayak.pms.global.constants;

/**
* @功能描述:募集方式字典
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum ApprovalStatus {

    lunch("0", "待复核"),
    down("1", "已复核");
    private final String itemKey;
    private final String itemVal;
    ApprovalStatus(String itemKey, String itemVal) {
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
