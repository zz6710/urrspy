package com.kayak.config.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.Comparator;

/**
 * @author: lfzh
 * @date: 2020-12-24 14:29
 */
@Data
@GraphQLModel(fetcher = "ta5014Service")
public class Ta5014 implements Comparator<Ta5014> {
    @GraphQLField(label = "清算批次", field = "task_group")
    private String taskGroup;

    @GraphQLField(label = "依赖的上一个清算任务组", field = "last_task_group")
    private String lastTaskGroup;

    @GraphQLField(label = "应执行时间", field = "should_exec_time")
    private String shouldExecTime;

    @GraphQLField(label = "跑批日期类型", field = "running_type")
    @Pattern(regexp = "[01]", message = "跑批类型错误")
    private String runningType;

    @GraphQLField(label = "清算组执行类别", field = "exec_task_type")
    private String execTaskType;

    @GraphQLField(label = "清算组名称", field = "task_group_name")
    private String taskGroupName;

    @GraphQLField(label = "按钮是否展示", field = "button_is_display")
    private String buttonIsDisplay;

    @GraphQLField(label = "清算组时间", field = "group_order")
    private String queryTaskDate;

    @GraphQLField(label = "未执行", field = "NON_EXECUTION")
    private String nonExecution;

    @GraphQLField(label = "未注册", field = "NON_REGISTRY")
    private String nonRegistry;

    @GraphQLField(label = "执行中", field = "EXECUTING")
    private String executing;

    @GraphQLField(label = "执行成功", field = "SUCCESS")
    private String success;

    @GraphQLField(label = "执行失败", field = "FAILED")
    private String failed;

    @GraphQLField(label = "前置批组", field = "PRE_TASK_GROUP")
    @JsonIgnore
    private String preTaskGroup;

    @GraphQLField(label = "任务执行ID")
    private String taskId;

    @GraphQLField(label = "批量任务执行ID")
    private String taskExecid;

    @GraphQLField(label = "排序row_num")
    private String rowNum;
    @Override
    public int compare(Ta5014 o1, Ta5014 o2) {
        return o1.getPreTaskGroup().split(",").length - o2.getPreTaskGroup().split(",").length;
    }
}
