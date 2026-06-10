package com.kayak.pms.global.constants;

/**
* @功能描述:信披子类型字典枚举
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum DisclosureType {
    preSale("1", "售前信息登记"),
    issEst("2", "发行成立公告"),
    expire("3", "到期公告"),
    operate("4", "运作公告"),
    regular("5", "定期报告"),
    ensemble("6", "整体报告"),
    major("7", "重大事项报告"),
    provision("8", "临时公告"),
    net("9", "净值报告"),
    bonus("10", "分红公告"),
    manual("11", "手工报告"),
    sale("12", "销售文档"),
    purchase("13", "申购赎回公告");
    private final String itemKey;//输入类型
    private final String itemVal;//描述
    DisclosureType(String itemKey, String itemVal) {
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
