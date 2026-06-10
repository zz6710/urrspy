package com.kayak.pms.excel.model;

import lombok.Data;

@Data
public class Excel {

    private String id;
    /**
     * 模板名称
     * */
    private String modName;
    /**
     * 备注
     * */
    private String remark;
    /**
     * 数据起始行
     * */
    private Integer firstrow;
    /**
     * 标题行
     * */
    private Integer headerrow;
    /**
     * 是否指定
     * */
    private Integer isAppointVal;

    /**
     * 存儲表
     * */
    private String tableName;
}
