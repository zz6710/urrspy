package com.kayak.rpt.zz.manage.enums;

/**
 * @author XueJ
 * @version 1.0.0
 * @ClassName excelEnum.java
 * @Description TODO
 * @createTime 2022年05月27日 15:05:00
 */
public enum ExcelEnum {


    TEXT("TEXT", "文本"),
    ENUM("ENUM", "枚举"),
    DATE("DATE", "日期"),
    NUM("NUM", "数字");

    ExcelEnum(String val, String desc) {
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

    public static ExcelEnum getNoticeType (String val) {
        for (ExcelEnum type : ExcelEnum.values()) {
            if (val.equals(type.getVal()))
                return type;
        }
        return null;
    }

}
