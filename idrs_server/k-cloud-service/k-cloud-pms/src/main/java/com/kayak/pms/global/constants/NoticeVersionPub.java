package com.kayak.pms.global.constants;

/**
* @功能描述:募集方式字典
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum NoticeVersionPub {
    waitPub("0", "待发布"),
    overSend("1", "发布成功"),
    closeSend("2", "取消发布");
    private final String itemKey;
    private final String itemVal;
    NoticeVersionPub(String itemKey, String itemVal) {
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
