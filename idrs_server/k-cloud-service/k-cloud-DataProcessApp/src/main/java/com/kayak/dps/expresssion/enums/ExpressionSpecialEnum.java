package com.kayak.dps.expresssion.enums;

/**
 * 特殊定制函数校验类型
 */
public enum ExpressionSpecialEnum {

    NULL_N("null_n", "必填校验"),
    NUMBER_X("number_x", "长度及位数校验校验,调用number_x函数"),
    NUMBER_Z("number_z", "长度及位数校验校验,校验必须为数字并且大于等于0"),
    REPEAT_X("repeat_x", "重复性校验,校验值域可多选不可重复"),
    DICT("dict", "值域校验,调用dict函数"),
    COMPARE("compare", "比较大小"),
    SUM("sum", "累计数值和");



    ExpressionSpecialEnum(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    private String val;
    private String desc;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
