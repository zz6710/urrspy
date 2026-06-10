package com.kayak.pms.opFlow.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回调返回规则校验
 * @author  xiamh
 * @date    2020-01-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateConfig {

    private String id;
    private String name;                // 规则名称
    private String rule;                // 规则（groovy脚本）
    private String remark;              // 备注

}