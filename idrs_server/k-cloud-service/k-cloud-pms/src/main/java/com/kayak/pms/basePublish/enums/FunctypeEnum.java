package com.kayak.pms.basePublish.enums;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/5/13 11:10
 */
public enum FunctypeEnum {
    FUNCTYPE_VARCHAR("varchar", "字符串", "k-field-text"),
    FUNCTYPE_INT("int", "整数", "k-field-text"),
    FUNCTYPE_NUMBER("number", "数字", "k-field-text"),
    FUNCTYPE_SELECT("select", "下拉单选", "k-field-select"),
    FUNCTYPE_MSELECT("mselect", "下拉多选","k-field-select "),
    FUNCTYPE_DATE("date", "日期", "k-field-date"),
    FUNCTYPE_TIIME("time", "时间", "k-field-time");
    private final String type;//输入类型
    private final String desc;//描述
    private final String componentName;//对应组件类型
    FunctypeEnum(String type, String desc, String componentName) {
        this.type = type;
        this.desc = desc;
        this.componentName = componentName;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getComponentName() {
        return componentName;
    }

}
