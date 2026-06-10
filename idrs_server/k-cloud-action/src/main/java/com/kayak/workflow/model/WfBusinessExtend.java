package com.kayak.workflow.model;

import com.kayak.graphql.annotation.GraphQLField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WfBusinessExtend {

    /**
     * 主键
     */
    @GraphQLField(key = true, sql = "extendId = $S{extend_id}", field = "extendId")
    private String extendId;

    /**
     * 对应sys_server_method表的server字段
     */
    @GraphQLField(sql = "server = $S{server}", field = "server")
    private String server;

    /**
     *  主键值
     */
    @GraphQLField(sql = "keys_value = $S{keysValue}", field = "keysValue")
    private String keysValue;

    /**
     * 进程id
     */
    @GraphQLField(sql = "process_Id = $S{processId}", field = "processId")
    private String processId;

    /**
     * 进程实例id
     */
    @GraphQLField(sql = "process_instance_id = $S{processInstanceId}", field = "processInstanceId")
    private String processInstanceId;

    /**
     * 进程状态
     */
    @GraphQLField(sql = "process_status = $S{processStatus}", field = "processStatus")
    private String processStatus;

    /**in ($U{busStatus})
     * 业务完成状态
     */
    @GraphQLField(sql = "bus_status = $S{busStatus}", field = "busStatus")
    private String busStatus;

    /**
     * 业务完成状态
     */
    @GraphQLField(sql = "bus_err = $S{busErr}", field = "busErr")
    private String busErr;

    /**
     * 操作用户id
     */
    @GraphQLField(sql = "userid = $S{userid}", field = "userid")
    private String userid;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 数据修改日期
     */
    private String updateDate;

    /**
     * 数据修改时间
     */
    private String updateTime;

    /**
     * 业务回调APP名称
     */
    private String appName;

    /**
     * 业务回调地址
     */
    private String url;
}