package com.kayak.web.workflow.domain.bo;

import com.kayak.common.entity.BaseEntity;
import com.kayak.common.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;



/**
 * 业务审批业务对象 flow_busi_info
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("业务审批业务对象")
public class WfBusiInfoBo extends BaseEntity {

    /**
     * 业务审批表主键
     */
    @ApiModelProperty(value = "业务审批表主键", required = true)
    @NotNull(message = "业务审批表主键不能为空", groups = { EditGroup.class })
    private Long busiId;

    /**
     * 服务(微服务为服务名，http请求为ip:port)
     */
    @ApiModelProperty(value = "服务(微服务为服务名，http请求为ip:port)", required = true)
    private String server;

    /**
     * 业务回调地址
     */
    @ApiModelProperty(value = "业务回调地址", required = true)
    private String url;

    /**
     * 业务唯一键，逗号隔开(不能存在同样的在途数据)
     */
    @ApiModelProperty(value = "业务唯一键，逗号隔开(不能存在同样的在途数据)", required = true)
    private String keys;

    /**
     * 业务唯一值，逗号隔开
     */
    @ApiModelProperty(value = "业务唯一值，逗号隔开", required = true)
    private String values;

    /**
     * 流程设计id
     */
    @ApiModelProperty(value = "流程设计id", required = true)
    private String processKey;

    /**
     * 流程定义id
     */
    @ApiModelProperty(value = "流程定义id", required = true)
    private String processDefinitionId;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id", required = true)
    private String processInstanceId;

    /**
     * 流程状态
     */
    @ApiModelProperty(value = "流程状态", required = true)
    private String processStatus;

    /**
     * 业务执行状态
     */
    @ApiModelProperty(value = "业务执行状态", required = true)
    private String busStatus;

    /**
     * 业务执行结果
     */
    @ApiModelProperty(value = "业务执行结果", required = true)
    private String busReturnMsg;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    /**
     * 回调次数
     */
    @ApiModelProperty(value = "回调次数", required = true)
    private Long callbackNum;

    /**
     * 返回报文校验规则id
     */
    @ApiModelProperty(value = "返回报文校验规则id", required = true)
    private String validateId;


}
