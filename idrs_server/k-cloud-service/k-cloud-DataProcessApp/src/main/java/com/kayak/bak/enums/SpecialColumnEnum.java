package com.kayak.bak.enums;

/**
 * 特殊的字段类型单独处理
 */
public enum SpecialColumnEnum {
    LONG_TEXT("LONGTEXT"),
    TIMESTAMP("TIMESTAMP"),
    TDate("DATE");

    private String value;

    SpecialColumnEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 检索对应策略
     * @param type
     * @return
     * @throws Exception
     */
    public static Boolean containVal(String type) throws Exception {
        for (SpecialColumnEnum val : SpecialColumnEnum.values()) {
            if (val.getValue().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
