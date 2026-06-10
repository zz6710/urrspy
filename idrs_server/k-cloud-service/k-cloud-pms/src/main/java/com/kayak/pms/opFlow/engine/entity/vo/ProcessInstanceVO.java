package com.kayak.pms.opFlow.engine.entity.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 17/04/2017.
 * 用于前台界面的显示
 */
@Data
@Alias("processInstanceVO")
public class ProcessInstanceVO {
    private String processId;
    private String processName;
    private String processDisplayName;
    private String processInstanceId;
    private String applyUser;
    private String createDate;
    private String createTime;
    private String finishDate;
    private String finishTime;
    private String status;
    private String taskId;
    private String taskName;
    private String creator;

}
