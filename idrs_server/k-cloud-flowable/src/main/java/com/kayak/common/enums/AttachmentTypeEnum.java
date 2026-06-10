package com.kayak.common.enums;

/**
 * @author yuanjinqiao
 * @description
 * @create 2023-04-03 16:08
 **/
public enum AttachmentTypeEnum {
    BUSINESS("business", "业务附件"),

    PROCESS("process", "流程附件");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    AttachmentTypeEnum(String type, String remark) {
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
