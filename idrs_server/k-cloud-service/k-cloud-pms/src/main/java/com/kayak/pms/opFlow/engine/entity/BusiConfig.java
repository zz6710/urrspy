package com.kayak.pms.opFlow.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 业务审批表
 * @author  xiamh
 * @date    2020-01-25
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusiConfig {

    private String server;
    private String processName;
    private String busKeys;
    private String tableName;

    private String serverDesc;
    private String displayName;

}