package com.kayak.pms.global.constants;

/**
* @功能描述:募集方式字典
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum RuleDataSource {

    auto("1", "自动生成"),
    manual("2", "手工新增"),
    copy("3", "复制新增");
    private final String itemKey;
    private final String itemVal;
    RuleDataSource(String itemKey, String itemVal) {
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
