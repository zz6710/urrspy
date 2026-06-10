package com.kayak.web.workflow.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 流程部署视图对象
 *
 * @author yuanjinqiao
 * @date 2022-06-30
 */
@Data
@ApiModel("流程部署视图对象")
public class WfDeployVo {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义ID
     */
    @ApiModelProperty("流程定义ID")
    private String definitionId;

    /**
     * 流程名称
     */
    @ApiModelProperty("流程名称")
    private String processName;

    /**
     * 流程Key
     */
    @ApiModelProperty("流程Key")
    private String processKey;

    /**
     * 分类编码
     */
    @ApiModelProperty("分类编码")
    private String category;

    @ApiModelProperty("版本")
    private Integer version;

    /**
     * 表单ID
     */
    @ApiModelProperty("表单ID")
    private Long formId;

    /**
     * 是否可以删除
     */
    @ApiModelProperty("是否可以删除")
    private String deleteFlag;

    /**
     * 部署ID
     */
    @ApiModelProperty("部署ID")
    private String deploymentId;

    /**
     * 流程定义状态: 1:激活 , 2:中止
     */
    @ApiModelProperty("流程定义状态: 1:激活 , 2:中止")
    private Boolean suspended;

    /**
     * 部署时间
     */
    @ApiModelProperty("部署时间")
    private Date deploymentTime;
}
