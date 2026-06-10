package com.kayak.pms.opFlow.engine.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * Created by daniel on 19/04/2017.
 */
@Data
@Alias("taskDetail")
public class TaskDetail {

    private String approvalUser;
    private String createDate;
    private String createTime;
    private String finishDate;
    private String finishTime;
    private String taskName;
    private String taskDisplayName;
    private String opinion;
    private String result;
    private String resultText;
    private String applyUser;

    private String operator;
    private String submitUser;

}
