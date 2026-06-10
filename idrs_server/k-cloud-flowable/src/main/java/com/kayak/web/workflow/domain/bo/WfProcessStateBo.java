package com.kayak.web.workflow.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 流程业务状态对象
 *
 * @author yuanjinqiao
 * @createTime 2022/6/11 01:15
 */
@Data
@ApiModel("流程业务状态对象")
public class WfProcessStateBo {

    @ApiModelProperty("流程定义ID")
    @NotNull(message = "流程定义ID")
    private String definitionId;

    @ApiModelProperty("状态")
    @NotNull(message = "状态")
    private String state;
}
