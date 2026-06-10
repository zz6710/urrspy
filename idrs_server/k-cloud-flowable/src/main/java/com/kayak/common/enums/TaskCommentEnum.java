package com.kayak.common.enums;

import java.util.ArrayList;

/**
 * 流程意见类型
 *
 * @author yuanjinqiao
 * @date 2021/4/19
 */
public enum TaskCommentEnum {

    /**
     * 说明
     */
    APPLY("0", "申请"),
    PASS("1", "通过"),
    RETURN("2", "退回"),
    REJECT("3", "驳回"),
    DELEGATE("4", "委派"),
    TRANSFER("5", "转办"),
    REFUSE("6", "拒绝");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    TaskCommentEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

    public static ArrayList<String> getAllType() {
        ArrayList<String> ret = new ArrayList<>();
        TaskCommentEnum[] values = TaskCommentEnum.values();
        for (TaskCommentEnum value : values) {
            ret.add(value.getType());
        }
       return ret;
    }
}
