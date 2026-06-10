package com.kayak.pms.global.constants;

/**
* @功能描述:募集方式字典
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum TaskStart {

    auto("1", "自动发起"),
    manual("2", "手动发起"),
    busChange("0", "业务变更发起");
    private final String itemKey;
    private final String itemVal;
    TaskStart(String itemKey, String itemVal) {
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
