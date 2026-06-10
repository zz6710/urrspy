package com.kayak.web.workflow.domain.bo;

import com.kayak.common.entity.BaseEntity;
import com.kayak.common.validate.AddGroup;
import com.kayak.common.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 业务流程业务对象 flow_busi_config
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务流程业务对象")
public class WfBusConfigBo extends BaseEntity {

    /**
     * 服务
     */
    @ApiModelProperty(value = "服务", required = true)
    @NotBlank(message = "服务不能为空", groups = {EditGroup.class})
    private String server;

    /**
     * 流程标识
     */
    @ApiModelProperty(value = "流程标识", required = true)
    @NotBlank(message = "流程标识不能为空", groups = {AddGroup.class, EditGroup.class})
    private String processKey;

    /**
     * 业务主键
     */
    @ApiModelProperty(value = "业务主键")
    private String busKeys;

    /**
     * 业务主键名称
     */
    @ApiModelProperty(value = "业务主键名称")
    private String busName;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

}
