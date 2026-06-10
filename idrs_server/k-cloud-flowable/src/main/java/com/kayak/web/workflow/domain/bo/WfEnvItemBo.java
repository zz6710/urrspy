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
 * 上下文项配置业务对象 flow_env_item
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("上下文项配置业务对象")
public class WfEnvItemBo extends BaseEntity {


    /**
     * 上下文id
     */
    @ApiModelProperty(value = "上下文项id", required = true)
    @NotNull(message = "上下文项id不能为空", groups = { EditGroup.class })
    private Long envItemId;
    /**
     * 上下文id
     */
    @ApiModelProperty(value = "上下文id", required = true)
    @NotNull(message = "上下文id不能为空", groups = { EditGroup.class })
    private Long envId;

    /**
     * 键
     */
    @ApiModelProperty(value = "键", required = true)
    @NotBlank(message = "键不能为空", groups = { EditGroup.class })
    private String itemKey;

    /**
     * 值
     */
    @ApiModelProperty(value = "值", required = true)
    @NotBlank(message = "值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String itemValue;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", required = true)
    @NotBlank(message = "类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String itemType;


}
