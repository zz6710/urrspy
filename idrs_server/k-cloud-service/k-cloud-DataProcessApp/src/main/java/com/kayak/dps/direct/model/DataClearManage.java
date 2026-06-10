package com.kayak.dps.direct.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "dataClearManageService",table = "kbatch_report_task_info")
public class DataClearManage {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "TASK_ID", sql = "task_id = $S{taskId}" ,field = "task_id")
    private String taskId;
    @GraphQLField(kkhtml = "KFieldText", label = "任务名称", sql = "task_name=$S{taskName}" ,field = "task_name")
    private String taskName;
    @GraphQLField(kkhtml = "KFieldText", label = "数据报送日期", sql = "task_date=$S{taskDate}" ,field = "task_date")
    private String taskDate;
    @GraphQLField(kkhtml = "KFieldText", label = "执行日期", sql = "exec_date=$S{execDate}" ,field = "exec_date")
    private String execDate;
    @GraphQLField(kkhtml = "KFieldText", label = "执行开始时间", sql = "start_time=$S{startTime}" ,field = "start_time")
    private String startTime;
    @GraphQLField(kkhtml = "KFieldText", label = "执行结束时间", sql = "end_time=$S{endTime}" ,field = "end_time")
    private String endTime;
    @GraphQLField(kkhtml = "KFieldText", label = "执行状态", sql = "exec_status=$S{execStatus}" ,field = "exec_status")
    private String execStatus;

    @GraphQLField(kkhtml = "KFieldText", label = "执行结果", sql = "rtn_desc=$S{rtnDesc}" ,field = "rtn_desc")
    private String rtnDesc ;


}