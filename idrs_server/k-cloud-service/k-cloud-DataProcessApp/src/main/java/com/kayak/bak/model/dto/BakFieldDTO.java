package com.kayak.bak.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BakFieldDTO {

    /**
     * 字段名称
     */
    private String colName;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段长度
     */
    private String size;

    /**
     * 小数位
     */
    private String digits;

    /**
     * 字段默认值
     */
    private String data;

    /**
     * 字段注释
     */
    private String remark;
}
