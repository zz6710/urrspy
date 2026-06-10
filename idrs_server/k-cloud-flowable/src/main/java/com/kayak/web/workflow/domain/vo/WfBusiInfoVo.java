package com.kayak.web.workflow.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 业务审批视图对象 flow_busi_info
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
@Data
@ApiModel("业务审批视图对象")
public class WfBusiInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 业务审批表主键
     */
    @ApiModelProperty("业务审批表主键")
    private Long busiId;

    /**
     * 服务(微服务为服务名，http请求为ip:port)
     */
    @ApiModelProperty("服务(微服务为服务名，http请求为ip:port)")
    private String server;

    /**
     * 业务回调地址
     */
    @ApiModelProperty("业务回调地址")
    private String url;

    /**
     * 业务回调参数类型
     */
    @ApiModelProperty("业务回调参数类型")
    private String contentType;
    /**
     * 业务唯一键，逗号隔开(不能存在同样的在途数据)
     */
    @ApiModelProperty("业务唯一键，逗号隔开(不能存在同样的在途数据)")
    private String keys;

    /**
     * 业务唯一值，逗号隔开
     */
    @ApiModelProperty("业务唯一值，逗号隔开")
    private String values;

    /**
     * 流程设计id
     */
    @ApiModelProperty("流程设计id")
    private String processKey;

    /**
     * 流程定义id
     */
    @ApiModelProperty("流程定义id")
    private String processDefinitionId;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id", required = true)
    private String processInstanceId;

    /**
     * 流程状态
     */
    @ApiModelProperty("流程状态")
    private String processStatus;

    /**
     * 业务执行状态
     */
    @ApiModelProperty("业务执行状态")
    private String busStatus;

    /**
     * 业务执行结果
     */
    @ApiModelProperty("业务执行结果")
    private String busReturnMsg;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 回调次数
     */
    @ApiModelProperty("回调次数")
    private Long callbackNum;

    /**
     * 返回报文校验规则id
     */
    @ApiModelProperty("返回报文校验规则id")
    private String validateId;

    /**
     * 提交的参数
     */
    @ApiModelProperty("提交的参数")
    private String submitData;

    /**
     * 表单字段显示json
     */
    @ApiModelProperty("提交的参数")
    private String labelInfo;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建者姓名
     */
    @ApiModelProperty(value = "创建者姓名")
    private String creatorName;

}
