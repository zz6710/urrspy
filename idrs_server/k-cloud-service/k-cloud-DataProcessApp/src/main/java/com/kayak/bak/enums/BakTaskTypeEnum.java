package com.kayak.bak.enums;

import java.rmi.ServerException;

/**
 * 备份任务周期分类
 */
public enum BakTaskTypeEnum {

    TYPE_DAY("1", "按天备份", "BakTypeDayExt"),
    TYPE_MON("2", "按月备份", "BakTypeMonthExt"),
    TYPE_WEEK("3", "按周备份", "BakTypeWeekExt");

    BakTaskTypeEnum(String val, String desc, String server) {
        this.val = val;
        this.desc = desc;
        this.server = server;
    }

    private String val;
    private String desc;
    private String server;

    /**
     * 检索对应策略
     * @param type
     * @return
     * @throws Exception
     */
    public static String getServer(String type) throws Exception {
        for (BakTaskTypeEnum val : BakTaskTypeEnum.values()) {
            if (val.getVal().equals(type)) {
                return val.getServer();
            }
        }
        throw new ServerException("BakTaskTypeEnum中未找到该类型的归档配置对应策略：" + type);
    }


    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
