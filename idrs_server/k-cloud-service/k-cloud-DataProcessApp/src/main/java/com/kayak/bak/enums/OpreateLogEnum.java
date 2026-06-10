package com.kayak.bak.enums;

/**
 * 归档相关操作记录
 */
public enum OpreateLogEnum {

    INSERT_BAK("1", "归档数据入库"),
    CREATE_BAK("2", "建表"),
    REDO_BAK("3", "还原数据"),
    DELETE_BAK("4", "删除数据");

    OpreateLogEnum(String val, String desc) {
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
