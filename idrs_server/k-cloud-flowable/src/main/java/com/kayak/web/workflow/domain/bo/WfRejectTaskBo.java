package com.kayak.web.workflow.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yuanjinqiao
 * @createTime 2022/6/21 9:16
 */
@Data
@ApiModel("驳回任务节点对象")
public class WfRejectTaskBo {

    @ApiModelProperty(value = "流程xml", required = true)
    private String bpmnXml;

    @ApiModelProperty(value = "任务Key", required = true)
    private String taskDefKey;
}
