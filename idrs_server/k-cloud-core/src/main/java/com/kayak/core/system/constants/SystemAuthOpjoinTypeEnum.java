package com.kayak.core.system.constants;

public enum SystemAuthOpjoinTypeEnum {
    AND("AND"), OR("OR");
    private String  value;

    SystemAuthOpjoinTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
