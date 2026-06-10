package com.kayak.pms.opFlow.engine.busi;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ddai
 * @date 2019/10/24 11:28
 */
public class FormDetailConf {
    private String enName;
    private String cnName;
    private String dict;
    private boolean isDate;
    private Map<String, Object> attr = new HashMap<>();


    public FormDetailConf(String enName, String cnName) {
        this.enName = enName;
        this.cnName = cnName;
    }

    public FormDetailConf(String enName, String cnName, String dict) {
        this.enName = enName;
        this.cnName = cnName;
        this.dict = dict;
    }

    public FormDetailConf(String enName, String cnName, boolean isDate) {
        this.enName = enName;
        this.cnName = cnName;
        this.isDate = isDate;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public boolean isDate() {
        return isDate;
    }

    public void setDate(boolean date) {
        isDate = date;
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Object> attr) {
        this.attr = attr;
    }
}
