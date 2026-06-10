package com.kayak.rpt.zz.manage.enums;

public enum OperatorEnum {


    CREATE("0", "新增"),
    UPDATE("1", "修改"),
    DELETE("2", "删除"),
    IMPORT("3", "导入"),
    EXPORT("4", "导出"),
    MODIFY("5", "变更"),
    ;

    OperatorEnum(String val, String desc) {
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

    public static String getType(String val) {
        for (OperatorEnum typeEnum : OperatorEnum.values()) {
            if (typeEnum.getVal() == val)
                return typeEnum.getDesc();
        }
        return null;
    }

}
