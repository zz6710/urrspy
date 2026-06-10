package com.kayak.common.enums;

/**
 * @author yuanjinqiao
 * @description 多实例数字类型
 * @create 2022-10-13 18:05
 **/
public enum MultiInstanceNumberTypeEnum {

    NUMBER("number", "数字"),

    SCALE("scale", "百分比");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    MultiInstanceNumberTypeEnum(String type, String remark) {
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
