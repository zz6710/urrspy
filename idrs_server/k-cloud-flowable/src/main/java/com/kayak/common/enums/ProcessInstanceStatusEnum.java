package com.kayak.common.enums;

/**
 * 工作流常量-流程实例状态
 *
 * @author yuanjinqiao
 * @date 20220929
 */
public enum ProcessInstanceStatusEnum {
    /**
     * 说明
     */
    RUNNING("1", "进行中"),
    FINISH("2", "已完成"),
    REFUSE("3", "拒绝"),

    BACK_TO_APPLY("4", "驳回到申请节点"),

    RE_APPLY("5", "重新提交申请");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    ProcessInstanceStatusEnum(String type, String remark) {
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
