package com.kayak.common.enums;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-10-11 11:01
 **/
public enum ParamTypeEnum {
    /**
     * 说明
     */
    FORM_FIELD("1", "表单字段"),
    SQL("2", "SQL"),
    URL("3", "URl");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    ParamTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
