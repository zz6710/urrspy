package com.kayak.bak.enums;

/**
 * 不同的时间格式
 */
public enum DateFormatEnum {
    DATE_FORMAT("yyyyMMdd"),
    DATE_FORMAT_T("yyyy-MM-dd"),
    TIME_FORMAT("HHmmss"),
    TIME_FORMAT_T("HH:mm:ss");

    private String value;

    DateFormatEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
