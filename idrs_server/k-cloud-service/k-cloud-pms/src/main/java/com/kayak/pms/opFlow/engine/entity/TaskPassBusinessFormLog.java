package com.kayak.pms.opFlow.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskPassBusinessFormLog {

    private String id;

    private String processInstanceId;

    private String taskId;

    private String taskDisplayName;

    private String createDate;

    /**
     * 操作用户名称
     */
    private String optUserName;

    private String createTime;

    /**
     * 更改的数据信息
     */
    private String data;
}
