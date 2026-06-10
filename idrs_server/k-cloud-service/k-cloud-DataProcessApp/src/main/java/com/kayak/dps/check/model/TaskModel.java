package com.kayak.dps.check.model;

public class TaskModel {
    //节点
    private String node = "";
    //任务代码
    private String taskCode = "";
    //任务名称
    private String taskName = "";
    //执行任务类名
    private String className = "";
    //任务状态
    private String status = "";
    //任务是否允许跳过
    private String skip = "";
    //任务执行顺序
    private String order = "";
    //备注说明
    private String remark = "";

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSkip() {
        return skip;
    }

    public void setSkip(String skip) {
        this.skip = skip;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "node='" + node + '\'' +
                ", taskCode='" + taskCode + '\'' +
                ", taskName='" + taskName + '\'' +
                ", className='" + className + '\'' +
                ", status='" + status + '\'' +
                ", skip='" + skip + '\'' +
                ", order='" + order + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
