package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.util.Comparator;
import java.util.List;

/**
 * @author: lfzh
 * @date: 2020-12-31 9:46
 */
@GraphQLModel(fetcher = "ta5015Service")
@Data
public class Ta5015 implements Comparator<Ta5015> {
    @GraphQLField(label = "清算批次" ,field = "taskGroup")
    private String taskGroup;

    @GraphQLField(label = "依赖的上一个清算任务组", field = "lastTaskGroup")
    private String lastTaskGroup;

    @GraphQLField(label = "应执行时间", field = "shouldExecTime")
    private String shouldExecTime;

    @GraphQLField(label = "跑批日期类型" ,field = "runningType")
    @Pattern(regexp = "[01]",message = "跑批类型错误")
    private String runningType;

    @GraphQLField(label = "清算组名称",field = "taskGroupName")
    private String taskGroupName;

    @GraphQLField(label = "前置批组",field = "preTaskGroup")
    private String preTaskGroup;

    @GraphQLField(label = "清算组类型",field = "execTaskType")
    private String execTaskType;

    @GraphQLField(field = "已选成员")
    private List<TaClearGroupMember> member;

    @GraphQLField(field = "已选任务")
    private List<KbatchTaskInfo> existTaskInfos;

    @GraphQLField
    private String taskModel;
    @Override
    public int compare(Ta5015 o1, Ta5015 o2) {
        return o1.getPreTaskGroup().split(",").length - o2.getPreTaskGroup().split(",").length;
    }

}
