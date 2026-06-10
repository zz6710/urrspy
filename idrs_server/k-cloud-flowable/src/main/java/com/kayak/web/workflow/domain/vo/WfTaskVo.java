package com.kayak.web.workflow.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.flowable.engine.task.Comment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 工作流任务视图对象
 *
 * @author yuanjinqiao
 * @createTime 2022/3/10 00:12
 */
@Data
@ApiModel("工作流任务视图对象")
public class WfTaskVo implements Serializable {

    @ApiModelProperty("任务编号")
    private String taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("任务Key")
    private String taskDefKey;

    @ApiModelProperty("任务状态")
    private String taskStatus;

    @ApiModelProperty("任务执行人Id")
    private String assigneeId;

    @ApiModelProperty("任务执行人名称")
    private String assigneeName;

    @ApiModelProperty("流程发起人Id")
    private String startUserId;

    @ApiModelProperty("流程发起人名称")
    private String startUserName;

    @ApiModelProperty("流程发起人机构")
    private String startUserOrgNo;

    @ApiModelProperty("流程部署编号")
    private String deployId;

    @ApiModelProperty("流程ID")
    private String procDefId;

    @ApiModelProperty("流程key")
    private String procDefKey;

    @ApiModelProperty("流程定义名称")
    private String procDefName;

    @ApiModelProperty("流程定义内置使用版本")
    private int procDefVersion;

    @ApiModelProperty("流程实例ID")
    private String procInsId;

    @ApiModelProperty("任务耗时")
    private String duration;

    @ApiModelProperty("任务意见")
    private String message;

    @ApiModelProperty("意见类型")
    private String messageType;

    @ApiModelProperty("意见提交人")
    private String messageUserId;

    @ApiModelProperty("意见时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date messageTime;

    @ApiModelProperty("候选执行人")
    private String candidate;

    @ApiModelProperty("候选执行人")
    private List<String> candidateIds;

    @ApiModelProperty("任务创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("任务完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    @ApiModelProperty("流程发起时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date procStartTime;

    @ApiModelProperty("流程结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date procEndTime;

    @ApiModelProperty("流程状态")
    private String procStatus;

    @ApiModelProperty("授权人")
    private String authorizeName;

    @ApiModelProperty("是否代理任务")
    private String surrogateFlag;

    @ApiModelProperty("关键词")
    private String values;

    @ApiModelProperty("关键词名称")
    private String valuesName;
}
