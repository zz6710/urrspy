package com.kayak.web.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 任务代理对象 flow_surrogate
 *
 * @author yuanjinqiao
 * @date 2022-10-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_surrogate")
public class WfSurrogate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "ID")
    private Long id;
    /**
     * 流程key
     */
    private String processKey;
    /**
     * 流程名
     */
    private String processName;
    /**
     * 代理开始日期
     */
    private Date startDate;
    /**
     * 代理结束日期
     */
    private Date endDate;
    /**
     * 授权人
     */
    private String creator;
    /**
     * 授权人姓名
     */
    private String createName;
    /**
     * 代理人
     */
    private String surrogate;
    /**
     * 代理人名字
     */
    private String surrogateName;
    /**
     * 状态 1-启用  0-禁用
     */
    private String status;

}
