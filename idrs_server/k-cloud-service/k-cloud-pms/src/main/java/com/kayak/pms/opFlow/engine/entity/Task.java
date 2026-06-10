package com.kayak.pms.opFlow.engine.entity;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 任务实体类
 * Created by daniel on 20/03/2017.
 */
@Data
@GraphQLModel(fetcher = "taskService")
public class Task implements Serializable {
    /**
     * 主键ID
     */
    @GraphQLField
    private String id;
    /**
     * 流程实例ID
     */
    private String processInstanceId;

    // 流程ID
    private String processId;
    private String processVersion;
    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务显示名称
     */
    private String displayName;
    /**
     * 参与方式（0：普通任务；1：参与者会签任务）
     */
    private Integer performType;

    /**
     * 在审核表中,有审核人，这里是一个冗余字段
     */
    private String operator;

    /**
     * 任务创建时间
     */
    private String createDate;
    private String createTime;

    /**
     * 任务完成时间
     */
    private String finishDate;
    private String finishTime;

    /**
     * 任务类型, 默认为 0
     * 1 为审批任务
     */
    private String taskType = "0";

    /**
     * 定时任务表达式
     */
    private String quartzExp;

    /**
     * 父任务Id
     */
    private String parentTaskId;
    /**
     * 节点绑定的功能
     */
    private String busiId;

    private String submitUser;

    private String processDisplayName;
    private String processName;
    private String taskId;
    private String taskDisplayName;
    private String hisTaskId;
    private String taskCreateDate;
    private String applyUser;
    private String processInstanceCreateDate;
    private String processInstanceCreateTime;
    private String taskName;
    private String taskCreateTime;

    private String oldId;

    private String prodCode;
    private String prodName;

}
