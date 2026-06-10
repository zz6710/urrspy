package com.kayak.web.workflow.domain.vo;

import com.kayak.common.validate.AddGroup;
import com.kayak.common.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 单字段配置视图对象 flow_form_field
 *
 * @author yuanjinqiao
 * @date 2022-09-02
 */
@Data
@ApiModel("单字段配置视图对象")
public class WfFormFieldVo {

    private static final long serialVersionUID = 1L;

    /**
     * 字段id
     */
    @ApiModelProperty("字段id")
    private Long formFieldId;

    /**
     * 字段英文名称
     */
    @ApiModelProperty("字段英文名称")
    private String name;

    /**
     * 字段中文名称
     */
    @ApiModelProperty("字段中文名称")
    private String displayName;

    /**
     * 表单类型
     */
    @ApiModelProperty(value = "表单类型")
    private String formType;

    /**
     * 字段配置
     */
    @ApiModelProperty("字段配置")
    private String json;


}
