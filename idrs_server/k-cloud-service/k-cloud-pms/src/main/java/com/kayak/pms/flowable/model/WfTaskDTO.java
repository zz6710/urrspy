package com.kayak.pms.flowable.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Ty
 * @since 2023-05-16 19:21:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WfTaskDTO {

    private String taskId;
    private String taskName;
    private String taskDefKey;
    private String taskStatus;
    private String assigneeId;
    private String assigneeName;
    private String startUserId;
    private String startUserName;
    private String startUserOrgNo;
    private String deployId;
    private String procDefId;
    private String procDefKey;
    private String procDefName;
    private int procDefVersion;
    private String procInsId;
    private String duration;
    private String message;
    private String messageType;
    private String messageUserId;
    private Date messageTime;
    private String candidate;
    private List<String> candidateIds;
    private Date createTime;
    private Date finishTime;
    private Date procStartTime;
    private Date procEndTime;
    private String procStatus;
    private String authorizeName;
    private String surrogateFlag;
}
