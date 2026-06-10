package com.kayak.web.workflow.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@ApiModel("表单配置视图对象")
public class WfFormConfVO {

    @ApiModelProperty("审批表单类型")
    private String formType;

    @ApiModelProperty("审批表单key")
    private String formKey;

    @ApiModelProperty("业务表单类型")
    private String busiFormType;

    @ApiModelProperty("业务表单key")
    private String busiFormKey;

    @ApiModelProperty("任务定义id")
    private String taskDefKey;

    @ApiModelProperty("表单数据")
    private Map<String, Object> variables;

    @ApiModelProperty("名称")
    private String taskName;

}
