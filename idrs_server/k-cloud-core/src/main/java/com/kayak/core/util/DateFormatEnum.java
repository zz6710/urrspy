package com.kayak.core.util;

public enum DateFormatEnum {
    DATE_FORMAT("yyyyMMdd"),
    TIME_FORMAT("HHmmss");

    private String value;

    DateFormatEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
