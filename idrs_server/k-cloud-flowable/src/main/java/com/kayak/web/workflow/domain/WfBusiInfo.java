package com.kayak.web.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 业务审批对象 flow_busi_info
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_busi_info")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WfBusiInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 业务审批表主键
     */
    @TableId(value = "busi_id")
    private Long busiId;
    /**
     * 服务(微服务为服务名，http请求为ip:port)
     */
    private String server;
    /**
     * 业务回调地址
     */
    private String url;

    /**
     * 业务回调参数类型
     */
    private String contentType;
    /**
     * 业务唯一键，逗号隔开(不能存在同样的在途数据)
     */
    @TableField("`keys`")
    private String keys;
    /**
     * 业务唯一值，逗号隔开
     */
    @TableField("`values`")
    private String values;

    /**
     * 业务唯一值，逗号隔开
     */
    @TableField("values_name")
    private String valuesName;

    /**
     * 流程设计id
     */
    private String processKey;
    /**
     * 流程定义id
     */
    private String processDefinitionId;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 流程状态
     */
    private String processStatus;
    /**
     * 业务执行状态
     */
    private String busStatus;
    /**
     * 业务执行结果
     */
    private String busReturnMsg;
    /**
     * 备注
     */
    private String remark;
    /**
     * 回调次数
     */
    private Long callbackNum;
    /**
     * 返回报文校验规则id
     */
    private String validateId;

    /**
     * 提交的参数
     */
    private String submitData;

    /**
     * 表单字段显示json
     */
    private String labelInfo;

}
