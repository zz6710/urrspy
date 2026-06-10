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
public class WfCopyTaskQueryBo {

    @ApiModelProperty("流程key")
    private String procKey;

    @ApiModelProperty("是否已阅")
    private String read;

    @ApiModelProperty("发起开始日期")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date createStartDate;

    @ApiModelProperty("发起结束日期")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private Date createEndDate;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("发起抄送的用户Id")
    private String launchCopyUserId;
}
