package com.kayak.pms.opFlow.engine.utils;

/**
 * @author ddai
 * @date 2019-05-10 14:37
 */
public class EnvExprModel {
    private String type;
    private String original;

    public String getOriginalTrimed() {
        return originalTrimed;
    }

    public void setOriginalTrimed(String originalTrimed) {
        this.originalTrimed = originalTrimed;
    }

    private String originalTrimed;
    private String key;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
