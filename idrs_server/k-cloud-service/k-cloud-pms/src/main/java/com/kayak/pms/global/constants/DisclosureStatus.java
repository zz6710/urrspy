package com.kayak.pms.global.constants;

/**
* @功能描述:募集方式字典
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum DisclosureStatus {

    close("-2", "取消发布"),
    failure("-1", "发布失败"),
    overDown("0", "已生成"),
    clearing("1", "生成待补录"),
    waitPub("2", "生成待发布"),
    overSend("8", "发布成功"),
    creatFailed("9", "生成失败");
    private final String itemKey;
    private final String itemVal;
    DisclosureStatus(String itemKey, String itemVal) {
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
