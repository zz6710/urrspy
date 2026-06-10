package com.kayak.common.enums;

import java.util.ArrayList;

/**
 * 任务状态
 *
 * @author yuanjinqiao
 * @date 2021/4/19
 */
public enum TaskStatusEnum {

    COMPLETED("1", "已完成"),
    UNDER_APPROVAL("2", "审批中"),
    PENDING_APPROVAL("3", "待审批");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    TaskStatusEnum(String type, String remark) {
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
