package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 04/04/2017.
 */
@Data
@Alias("hisCompleteTask")
public class HisCompleteTask {

    private String approvalId;
    private String taskId;
    private String taskName;
    private String taskDisplayName;
    private String taskFinishDate;
    private String taskFinishTime;
    private String taskCreateDate;
    private String taskCreateTime;
    private String processInstanceCreateDate;
    private String processInstanceCreateTime;
    private String applyUser;
    private String processDisplayName;
    private String processName;
    private String processInstanceId;
    private String processId;

}
