package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 04/04/2017.
 */
@Data
@Alias("activeTask")
public class ActiveTask {
    private String processId;
    private String processDisplayName;
    private String processName;
    private String processInstanceId;
    private String taskId;
    private String taskName;
    private String taskDisplayName;
    private String hisTaskId;
    private String taskCreateTime;
    private String taskCreateDate;
    private String taskFormId;
    private String taskFormUrl;
    private String applyUser;
    private String processInstanceFormId;
    private String processInstanceFormUrl;
    private String processInstanceCreateDate;
    private String processInstanceCreateTime;
    private String creator;

}
