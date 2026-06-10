package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * @author: lfzh
 * @date: 2021-03-30 10:12
 */
@GraphQLModel(fetcher = "ta5014DetailService")
public class Ta5014DetailRootStatus {
    @GraphQLField(label = "清算执行日期" ,field = "query_task_date")
    private String queryTaskDate;
    @GraphQLField(label = "清算任务组" ,field = "task_group")
    private String taskGroup;
    @GraphQLField(label = "成功数量" ,field = "success")
    private String success;
    @GraphQLField(label = "失败数量" ,field = "failure")
    private String failure;
    @GraphQLField(label = "执行中的数量" ,field = "excuting")
    private String excuting;
    @GraphQLField(label = "未执行的数量" ,field = "no_excute")
    private String noExcute;
    @GraphQLField(label = "未注册数量" ,field = "no_registry")
    private String noRegistry;
    @GraphQLField(label = "用于存储map的key" ,field = "map_key")
    private String mapKey;

    public String getMapKey() {
        return mapKey;
    }

    public void setMapKey(String mapKey) {
        this.mapKey = mapKey;
    }

    public String getQueryTaskDate() {
        return queryTaskDate;
    }

    public void setQueryTaskDate(String queryTaskDate) {
        this.queryTaskDate = queryTaskDate;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public String getExcuting() {
        return excuting;
    }

    public void setExcuting(String excuting) {
        this.excuting = excuting;
    }

    public String getNoExcute() {
        return noExcute;
    }

    public void setNoExcute(String noExcute) {
        this.noExcute = noExcute;
    }

    public String getNoRegistry() {
        return noRegistry;
    }

    public void setNoRegistry(String noRegistry) {
        this.noRegistry = noRegistry;
    }
}
