package com.kayak.dps.check.model;

import lombok.Data;

@Data
public class ExpressDictDTO {

    /**
     * 字典key值
     */
    private String dictKey;

    /**
     * 字典中文值
     */
    private String dictVal;

    /**
     * key + “ ” + value 的字典值形式
     */
    private String dictKv;
}
