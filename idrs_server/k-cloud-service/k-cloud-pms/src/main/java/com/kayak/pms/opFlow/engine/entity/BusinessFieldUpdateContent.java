package com.kayak.pms.opFlow.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessFieldUpdateContent {

    /**
     * 修改之前的数据
     */
    private String before;

    /**
     * 修改之后的数据
     */
    private String after;

    /**
     * 字段显示名称
     */
    private String label;

    /**
     * 字段名称
     */
    private String name;

}
