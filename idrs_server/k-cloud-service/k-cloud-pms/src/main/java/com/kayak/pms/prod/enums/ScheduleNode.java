package com.kayak.pms.prod.enums;

/**
 * com.kayak.pms.prod.enums
 * user:rennannan
 * date:2021/3/15 13:43
 * function:产品进度节点    用于产品进度表功能使用     在一些关键节点将信息插入产品进度表
 * 过创设会、会后参数确认、申报参数确认、产品说明书法审、产品说明书定稿、报备材料法审、一次报备、发行参数确认、二次报备、产品参数设置
 */
public enum ScheduleNode {

    associate_meet("01","过创设会"),
    parameter_confirm_after_meet("02","会后参数确认"),
    register_parameter_confirm("03","申报参数确认"),
    manual_law_examination("04","产品说明书法审"),
    manual_finalize("05","产品说明书定稿"),
    register_doc_law_examination("06","报备材料法审"),
    register("07","一次报备"),
    issue_parameter_confirm("08","发行参数确认"),
    issue("09","二次报备"),
    parameter_finalize("10","产品参数设置");

    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ScheduleNode(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
