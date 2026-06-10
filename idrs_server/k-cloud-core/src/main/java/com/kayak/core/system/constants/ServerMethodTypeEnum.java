package com.kayak.core.system.constants;

public enum ServerMethodTypeEnum {

    PARENT("1"), CHILD("2");

    private String value;

    ServerMethodTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
