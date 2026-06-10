package com.kayak.web.workflow.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yuanjinqiao
 * @createTime 2022/6/21 9:16
 */
@Data
@ApiModel("流程模型视图对象")
public class WfModelVo {

    @ApiModelProperty("模型ID")
    private String modelId;

    @ApiModelProperty("模型名称")
    private String modelName;

    @ApiModelProperty("模型Key")
    private String modelKey;

    @ApiModelProperty("分类编码")
    private String category;

    @ApiModelProperty("版本")
    private Integer version;

    @ApiModelProperty("表单类型")
    private Integer formType;

    @ApiModelProperty("表单ID")
    private Long formId;

    @ApiModelProperty("模型描述")
    private String description;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("更新人")
    private String updateUserName;

    @ApiModelProperty("流程xml")
    private String bpmnXml;

    @ApiModelProperty("表单内容")
    private String content;
}
