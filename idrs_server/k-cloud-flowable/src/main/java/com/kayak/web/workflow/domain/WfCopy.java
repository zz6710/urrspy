package com.kayak.web.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程抄送对象 flow_copy
 *
 * @author yuanjinqiao
 * @date 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_copy")
public class WfCopy extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 抄送主键
     */
    @TableId(value = "copy_id")
    private Long copyId;
    /**
     * 抄送标题
     */
    private String taskName;
    /**
     * 流程版本
     */
    private int procDefVersion;
    /**
     * 流程实例id
     */
    private String procInsId;
    /**
     * 流程名称
     */
    private String procDefName;
    /**
     * 流程key
     */
    private String procKey;
    /**
     * 流程定义id
     */
    private String procDefId;
    /**
     * 任务主键
     */
    private String taskId;
    /**
     * 任务定义key
     */
    private String taskDefKey;
    /**
     * 用户主键
     */
    private String userId;
    /**
     * 发起抄送的用户Id
     */
    private String launchCopyUserId;
    /**
     * 是否已阅，1为是，0为否
     */
    @TableField(value = "`read`")
    private String read;

}
