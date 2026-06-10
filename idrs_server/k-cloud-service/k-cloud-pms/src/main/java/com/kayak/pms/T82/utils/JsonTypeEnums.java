package com.kayak.pms.T82.utils;

public enum JsonTypeEnums {
    /**
     * C_字符型
     */
    STRING_C("C", "String"),
    /**
     * A_数字字符型 限于0—9
     */
    STRING_A("A", "String"),
    /**
     * N_数值型，并可参与数值计算
     */
    AMOUNT_N("N", "Amount");
    // TODO 不知道这个类型有没有,如果有的话模板也需要修改
    //TEXT("TEXT", "TEXT");

    /**
     * ZD_简码
     */
    private final String key;
    /**
     * 系统_类型
     */
    private final String value;

    JsonTypeEnums(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    /**
     * 传 key 得value
     *
     * @param IdType
     * @return
     */
    public static String getKeyToValue(String IdType) {
        JsonTypeEnums[] imageFormatTypes = values();
        for (JsonTypeEnums imageFormatType : imageFormatTypes) {
            if (imageFormatType.key.equals(IdType)) {
                // 转value
                return imageFormatType.getValue();
            }
        }
        return null;
    }

}
