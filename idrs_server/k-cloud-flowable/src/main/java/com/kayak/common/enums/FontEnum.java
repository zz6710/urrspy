package com.kayak.common.enums;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-09-30 14:31
 **/
public enum FontEnum {

    SIM_SUN("SimSun","宋体", "fonts\\simsun.ttf");

    // 字体名称
    private String id;
    // 字体中文名称
    private String chName;
    // 字体文件路径
    private String path;

    private FontEnum(String id, String name, String path) {
        this.id = id;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
