package com.kayak.web.workflow.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yuanjinqiao
 * @description
 * @create 2023-04-03 15:50
 **/
@Data
@ApiModel("附件视图对象")
public class WfAttachmentVo {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("附件名字")
    private String name;
    @ApiModelProperty("附件地址")
    private String url;
    @ApiModelProperty("上传时间")
    private Date time;
    @ApiModelProperty("附件描述")
    private String description;
    @ApiModelProperty("上传人id")
    private String uploadUserId;
    @ApiModelProperty("上传人名字")
    private String uploadUserName;
    @ApiModelProperty("附件类型")
    private String type;
}
