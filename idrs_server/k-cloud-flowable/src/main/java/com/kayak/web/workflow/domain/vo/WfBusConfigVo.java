package com.kayak.web.workflow.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务流程视图对象 flow_busi_config
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
@Data
@ApiModel("业务流程视图对象")
public class WfBusConfigVo {

    private static final long serialVersionUID = 1L;

    /**
     * 服务
     */
    @ApiModelProperty("服务")
    private String server;

    /**
     * 流程标识
     */
    @ApiModelProperty("流程标识")
    private String processKey;

    /**
     * 业务主键
     */
    @ApiModelProperty("业务主键")
    private String busKeys;

    /**
     * 主键名称
     */
    @ApiModelProperty("主键名称")
    private String busName;

    /**
     * 流程名称
     */
    @ApiModelProperty("流程名称")
    private String processName;

    /**
     * 业务操作
     */
    @ApiModelProperty("业务操作")
    private String serverDesc;

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    private Integer status;
}
