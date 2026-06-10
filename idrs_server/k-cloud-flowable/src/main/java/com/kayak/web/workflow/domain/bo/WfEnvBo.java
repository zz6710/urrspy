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
 * 上下文配置业务对象 flow_env
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("上下文配置业务对象")
public class WfEnvBo extends BaseEntity {

    /**
     * 上下文id
     */
    @ApiModelProperty(value = "上下文id", required = true)
    @NotNull(message = "上下文id不能为空", groups = { EditGroup.class })
    private Long envId;

    /**
     * 上下文英文名称
     */
    @ApiModelProperty(value = "上下文英文名称", required = true)
    @NotBlank(message = "上下文英文名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 上下文中文名称
     */
    @ApiModelProperty(value = "上下文中文名称", required = true)
    @NotBlank(message = "上下文中文名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String displayName;


}
