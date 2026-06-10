package com.kayak.web.workflow.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 任务代理视图对象 flow_surrogate
 *
 * @author yuanjinqiao
 * @date 2022-10-03
 */
@Data
@ApiModel("任务代理视图对象")
public class WfSurrogateVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 流程key
     */
    @ApiModelProperty("流程key")
    private String processKey;

    /**
     * 流程名
     */
    @ApiModelProperty("流程名")
    private String processName;

    /**
     * 代理开始日期
     */
    @ApiModelProperty("代理开始日期")
    @JsonFormat(pattern = "yyyyMMdd", timezone = "GMT+8")
    private Date startDate;

    /**
     * 代理结束日期
     */
    @ApiModelProperty("代理结束日期")
    @JsonFormat(pattern = "yyyyMMdd", timezone = "GMT+8")
    private Date endDate;

    /**
     * 授权人
     */
    @ApiModelProperty("授权人")
    private String creator;

    /**
     * 授权人姓名
     */
    @ApiModelProperty("授权人姓名")
    private String createName;

    /**
     * 代理人
     */
    @ApiModelProperty("代理人")
    private String surrogate;

    /**
     * 代理人名字
     */
    @ApiModelProperty("代理人名字")
    private String surrogateName;

    /**
     * 状态 1-启用  0-禁用
     */
    @ApiModelProperty("状态 1-启用  0-禁用")
    private String status;

}
