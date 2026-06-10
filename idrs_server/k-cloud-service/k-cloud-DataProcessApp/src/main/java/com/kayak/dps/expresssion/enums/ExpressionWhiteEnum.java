package com.kayak.dps.expresssion.enums;

/**
 * 白名单枚举
 */
public enum ExpressionWhiteEnum {
    //汉字白名单
    WHITE_CH("ch", "whiteCh"),
    //汉字字符白名单
    WHITE_CH_CODE("chcode", "whiteChCode"),
    //英文白名单
    WHITE_EN("en", "whiteEn"),
    //英文字符白名单
    WHITE_EN_CODE("encode", "whiteEnCode"),
    //罗马数字白名单
    WHITE_ROM("row", "whiteRom"),
    //数字白名单
    WHITE_NUMBER("number", "whiteNumber");

    ExpressionWhiteEnum(String val, String desc) {
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
