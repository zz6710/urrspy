package com.kayak.web.workflow.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上下文项配置视图对象 flow_env_item
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
@Data
@ApiModel("上下文项配置视图对象")
public class WfEnvItemVo {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("上下文项id")
    private Long envItemId;
    /**
     * 上下文id
     */
    @ApiModelProperty("上下文id")
    private Long envId;

    /**
     * 键
     */
    @ApiModelProperty("键")
    private String itemKey;

    /**
     * 值
     */
    @ApiModelProperty("值")
    private String itemValue;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", required = true)
    private String itemType;

}
