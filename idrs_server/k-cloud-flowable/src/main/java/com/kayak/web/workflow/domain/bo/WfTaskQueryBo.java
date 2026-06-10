package com.kayak.web.workflow.domain.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 流程任务业务对象
 *
 * @author yuanjinqiao
 * @createTime 2022/3/10 00:12
 */
@Data
@ApiModel("流程任务业务对象")
public class WfTaskQueryBo {

    @ApiModelProperty("流程key")
    private String processKey;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("发起开始日期")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date createStartDate;

    @ApiModelProperty("发起结束日期")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date createEndDate;

    @ApiModelProperty("结束开始日期")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date finishStartDate;

    @ApiModelProperty("结束结束日期")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date finishEndDate;

    @ApiModelProperty("流程发起人")
    private String applyUser;

    @ApiModelProperty("是否代理任务")
    private String surrogateFlag;

    @ApiModelProperty("业务主键")
    private String values;
}
