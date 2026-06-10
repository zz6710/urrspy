package com.kayak.pms.global.constants;

/**
* @功能描述:募集方式字典
* @params:
* @return:
* @Athor:ouyifan
* @date:2022/6/30
*/
public enum DisclosureSonType {

    subject("0101", "报告主题文件"),
    assess("0102", "可行性评估报告"),
    saleDoc("0106", "销售文件"),
    instruct("0107", "说明书"),
    brochure("0108", "宣传资料"),
    quarter("0501", "产品季报"),
    seAnnual("0502", "产品半年报"),
    annual("0503", "产品年报"),
    month("0504", "产品月报"),
    comSeAnnual("0601", "公司半年报"),
    comAnnual("0602", "公司年报"),
    netNormal("0901", "净值单产品（普通）"),
    netSpecial("0902", "净值单产品（特殊）"),
    netValueEntity("0903", "净值整体公告");
    private final String itemKey;//输入类型
    private final String itemVal;//描述
    DisclosureSonType(String itemKey, String itemVal) {
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
