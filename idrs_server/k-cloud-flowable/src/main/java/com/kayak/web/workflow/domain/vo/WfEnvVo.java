package com.kayak.web.workflow.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上下文配置视图对象 flow_env
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
@Data
@ApiModel("上下文配置视图对象")
public class WfEnvVo {

    private static final long serialVersionUID = 1L;

    /**
     * 上下文id
     */
    @ApiModelProperty("上下文id")
    private Long envId;

    /**
     * 上下文英文名称
     */
    @ApiModelProperty("上下文英文名称")
    private String name;

    /**
     * 上下文中文名称
     */
    @ApiModelProperty("上下文中文名称")
    private String displayName;

}
