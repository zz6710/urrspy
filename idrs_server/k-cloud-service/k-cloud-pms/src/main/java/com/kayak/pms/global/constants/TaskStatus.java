package com.kayak.pms.global.constants;

public enum TaskStatus {

    forGenerate("1", "未生成"),
    alreadyGenerate("2", "已生成"),
    generateFailed("-1", "生成失败");
    private final String itemKey;
    private final String itemVal;
    TaskStatus(String itemKey, String itemVal) {
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
