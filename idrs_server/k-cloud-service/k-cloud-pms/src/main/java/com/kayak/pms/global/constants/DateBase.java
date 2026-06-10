package com.kayak.pms.global.constants;

/**
* @功能描述:募集方式字典
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum DateBase {

    startRise("01", "募集起始日期"),
    establish("02", "成立日期"),
    realEnd("03", "实际终止日期"),
    eveWkWed("04", "每周三"),
    eveWkLast("05", "每周最后一个工作日"),
    eveMtLast("06", "每月最后一个自然日"),
    eveQtLast("07", "每季度最后一个自然日"),
    eveHyLast("08", "每半年最后一个自然日"),
    eveWyLast("09", "每年最后一个自然日"),
    everyDay("10", "每个工作日"),
    comDay("11", "申购赎回确认日"),
    bonusDay("12", "分红确认日"),
    busDay("99", "业务发生日");
    private final String itemKey;
    private final String itemVal;
    DateBase(String itemKey, String itemVal) {
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
