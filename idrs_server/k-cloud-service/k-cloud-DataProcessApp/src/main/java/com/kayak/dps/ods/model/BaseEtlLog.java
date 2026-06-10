package com.kayak.dps.ods.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8OdsTaskLogService",table = "t8_ods_task_log")
public class BaseEtlLog {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "同步表名称", sql = "table_name = $S{tableName}" ,field = "table_name")
    private String tableName;
    @GraphQLField(kkhtml = "KFieldText", label = "同步表名称", sql = "select_condition = $S{selectCondition}" ,field = "select_condition")
    private String selectCondition;
    @GraphQLField(kkhtml = "KFieldText", label = "执行开始时间", sql = "exec_start_time = $S{execStartTime}" ,field = "exec_start_time")
    private String execStartTime;
    @GraphQLField(kkhtml = "KFieldText", label = "执行结束时间", sql = "exec_end_time = $S{execEndTime}" ,field = "exec_end_time")
    private String execEndTime;
    @GraphQLField(kkhtml = "KFieldText", label = "上次同步耗时", sql = "cost_time = $S{costTime}" ,field = "cost_time")
    private String costTime;
    @GraphQLField(kkhtml = "KFieldText", label = "运行状态", sql = "task_status = $S{taskStatus}" ,field = "task_status")
    private String taskStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "update_date = $S{updateDate}" ,field = "update_date")
    private String updateDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "update_time = $S{updateTime}" ,field = "update_time")
    private String updateTime;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getExecStartTime() {
        return execStartTime;
    }

    public void setExecStartTime(String execStartTime) {
        this.execStartTime = execStartTime;
    }
    public String getExecEndTime() {
        return execEndTime;
    }

    public void setExecEndTime(String execEndTime) {
        this.execEndTime = execEndTime;
    }
    public String getCostTime() {
        return costTime;
    }

    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}