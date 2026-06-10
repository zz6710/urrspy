package com.kayak.schedule.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "quartzInfoService", table = "base_disclosure_quartz_info")
public class QuartzInfo {
    @GraphQLField(kkhtml = "KFieldText", label = "任务ID", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "任务名称", sql = "JOB_NAME like '%$U{jobName}%'", field = "JOB_NAME")
    private String jobName;
    @GraphQLField(kkhtml = "KFieldText", label = "任务类路径", sql = "JOB_CLASSPATH = $S{jobClasspath}", field = "JOB_CLASSPATH")
    private String jobClasspath;
    @GraphQLField(kkhtml = "KFieldText", label = "任务分组", sql = "JOB_GROUP like '%$U{jobGroup}%'", field = "JOB_GROUP")
    private String jobGroup;
    @GraphQLField(kkhtml = "KFieldText", label = "任务状态  0禁用 1启用 */", sql = "status = $S{status}", field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "任务运行规则", sql = "QUARTZ_RULE = $S{quartzRule}", field = "QUARTZ_RULE")
    private String quartzRule;
    @GraphQLField(kkhtml = "KFieldText", label = "任务运行表达式", sql = "CRON_EXPRESSION = $S{cronExpression}", field = "CRON_EXPRESSION")
    private String cronExpression;
    @GraphQLField(kkhtml = "KFieldText", label = "任务描述", sql = "description = $S{description}", field = "description")
    private String description;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "CREATE_TIME = $S{createTime}", field = "CREATE_TIME")
    private String createTime;
    @GraphQLField(kkhtml = "KFieldText", label = "修改时间", sql = "MODIFY_TIME = $S{modifyTime}", field = "MODIFY_TIME")
    private String modifyTime;
    @GraphQLField(kkhtml = "KFieldText", label = "前置任务ID", sql = "PRE_TASK_ID = $S{preTaskId}", field = "PRE_TASK_ID")
    private String preTaskId;
    @GraphQLField(label = "前置任务名", field = "PRE_JOB_NAME")
    private String preJobName;      //前置任务名
    @GraphQLField(label = "前置任务分组", field = "PRE_JOB_GROUP")
    private String preJobGroup;     //前置任务分组
    @GraphQLField(label = "前置任务路径", field = "PRE_JOB_CLASS_PATH")
    private String preJobClassPath; //前置任务路径
    @GraphQLField(label = "taskTime")
    private String taskTime;
    @GraphQLField(label = "freqSecond")
    private String freqSecond;
    @GraphQLField(label = "freqMinute")
    private String freqMinute;
    @GraphQLField(label = "freqHour")
    private String freqHour;
    @GraphQLField(label = "day")
    private String day;
    @GraphQLField(label = "week")
    private String week;
    @GraphQLField(label = "quarter")
    private String quarter;
    @GraphQLField(label = "execIp")
    private String execIp;
}