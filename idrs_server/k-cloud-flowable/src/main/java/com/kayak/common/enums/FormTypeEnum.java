package com.kayak.common.enums;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-12-14 15:25
 **/
public enum FormTypeEnum {
    FORM_FIELD_ID("formFieldId", "表单"),
    DYNAMIC_FORM_ID("dynamicFormId", "动态表单"),
    FORM_COMPONENT_NAME("formComponentName", "表单组件");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    FormTypeEnum(String type, String remark) {
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


