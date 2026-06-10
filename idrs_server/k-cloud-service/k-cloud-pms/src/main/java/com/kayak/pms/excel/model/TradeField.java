package com.kayak.pms.excel.model;

import lombok.Data;

@Data
public class TradeField {

    private String id;

    /**
     * EXCEL导入配置主表_ID
     */
    private String sysExlimpId;
    /**
     * 字段
     */
    private String fieldName;
    /**
     * 资产名称
     */
    private String fieldLabel;
    /**
     * 是否能为空（0：能为空  1：不能为空）
     */
    private Integer isAllowblank;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 指定行
     */
    private String approwVal;
    /**
     * 指定列
     */
    private String appcolVal;

    /**
     * 备注
     */
    private String remark;

}
