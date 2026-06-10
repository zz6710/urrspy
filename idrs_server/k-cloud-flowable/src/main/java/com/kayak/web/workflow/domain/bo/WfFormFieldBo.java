package com.kayak.web.workflow.domain.bo;

import com.kayak.common.entity.BaseEntity;
import com.kayak.common.validate.AddGroup;
import com.kayak.common.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 单字段配置业务对象 flow_form_field
 *
 * @author yuanjinqiao
 * @date 2022-09-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("单字段配置业务对象")
public class WfFormFieldBo extends BaseEntity {

    /**
     * 字段id
     */
    @ApiModelProperty(value = "字段id", required = true)
    @NotNull(message = "字段id不能为空", groups = {EditGroup.class})
    private Long formFieldId;

    /**
     * 字段英文名称
     */
    @ApiModelProperty(value = "字段英文名称")
    @NotBlank(message = "字段英文名称不能为空")
    private String name;

    /**
     * 字段中文名称
     */
    @ApiModelProperty(value = "字段中文名称", required = true)
    @NotBlank(message = "字段中文名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String displayName;

    /**
     * 表单类型
     */
    @ApiModelProperty(value = "表单类型", required = true)
    @NotBlank(message = "表单类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formType;

    /**
     * 字段配置
     */
    @ApiModelProperty(value = "字段配置", required = true)
    @NotBlank(message = "字段配置不能为空", groups = {AddGroup.class, EditGroup.class})
    private String json;
}
