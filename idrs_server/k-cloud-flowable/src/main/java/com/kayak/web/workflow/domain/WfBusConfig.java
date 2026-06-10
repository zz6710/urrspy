package com.kayak.web.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务流程对象 flow_busi_config
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_busi_config")
public class WfBusConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 服务
     */
    @TableId(value = "server", type = IdType.AUTO)
    private String server;
    /**
     * 流程标识
     */
    private String processKey;
    /**
     * 业务主键
     */
    private String busKeys;

    /**
     * 业务主键名称
     */
    private String busName;

    /**
     * 状态
     */
    private Integer status;

}
