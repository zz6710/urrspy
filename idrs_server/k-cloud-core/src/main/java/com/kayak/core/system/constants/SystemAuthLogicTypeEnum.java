package com.kayak.core.system.constants;

public enum SystemAuthLogicTypeEnum {

    EQUALS("=="),UNEQUALS("!="),DAYU(">"),XIAOYU("<");

    private String  value;

    SystemAuthLogicTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
