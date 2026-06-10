package com.kayak.core.system.constants;

public enum RoleAuthorityTypeEnum {

    MENU("1"), SERVER("2");

    private String value;

    RoleAuthorityTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
