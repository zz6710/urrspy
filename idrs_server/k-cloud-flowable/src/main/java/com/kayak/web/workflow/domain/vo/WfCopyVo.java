package com.kayak.web.workflow.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 流程抄送视图对象 flow_copy
 *
 * @author yuanjinqiao
 * @date 2022-05-19
 */
@Data
@ApiModel("流程抄送视图对象")
public class WfCopyVo {

    private static final long serialVersionUID = 1L;

    /**
     * 抄送主键
     */
    @ApiModelProperty(value = "抄送主键")
    private Long copyId;
    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String taskName;
    /**
     * 流程版本
     */
    @ApiModelProperty(value = "流程版本")
    private int procDefVersion;
    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    private String procInsId;
    /**
     * 流程名称
     */
    @ApiModelProperty(value = "流程名称")
    private String procDefName;
    /**
     * 流程定义id
     */
    @ApiModelProperty(value = "流程定义id")
    private String procDefId;
    /**
     * 流程key
     */
    @ApiModelProperty(value = "流程key")
    private String procKey;
    /**
     * 任务主键
     */
    @ApiModelProperty(value = "任务主键")
    private String taskId;
    /**
     * 任务定义key
     */
    @ApiModelProperty(value = "任务定义key")
    private String taskDefKey;
    /**
     * 用户主键
     */
    @ApiModelProperty(value = "用户主键")
    private String userId;
    /**
     * 发起抄送的用户Id
     */
    @ApiModelProperty(value = "发起抄送的用户Id")
    private String launchCopyUserId;
    /**
     * 发起抄送的用户名
     */
    @ApiModelProperty(value = "发起抄送的用户名")
    private String launchCopyUserName;

    @ApiModelProperty("抄送时间")
    private Date createTime;
    /**
     * 是否已阅，1为是，0为否
     */
    @ApiModelProperty(value = "是否已阅")
    private String read;
}
