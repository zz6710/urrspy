package com.kayak.pms.T82.model;

import com.alibaba.fastjson.annotation.JSONField;

public class BizBodyConfig {
    /**
     * 字段名_英文
     */
    @JSONField(ordinal = 1)
    private String name;
    /**
     * 字段描述
     */
    @JSONField(ordinal = 2)
    private String desc;
    /**
     * 字段长度+精度 格式 [长度,精度]
     */
    @JSONField(ordinal = 3)
    private String range;
    /**
     * 字段精度
     */
    @JSONField(ordinal = 4)
    private String accuracy;
    /**
     * 字段类型
     */
    @JSONField(ordinal = 5)
    private String type;
    /**
     * 必填选择
     */
    @JSONField(ordinal = 6)
    private Boolean required;
    /**
     * 字段校验正则
     */
    @JSONField(ordinal = 7)
    private String pattern;
    /**
     * 最大长度
     */
    @JSONField(ordinal = 8)
    private Integer maxLen;
    /**
     * 最小长度
     */
    @JSONField(ordinal = 9)
    private Integer minLen;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(Integer maxLen) {
        this.maxLen = maxLen;
    }

    public Integer getMinLen() {
        return minLen;
    }

    public void setMinLen(Integer minLen) {
        this.minLen = minLen;
    }
}
