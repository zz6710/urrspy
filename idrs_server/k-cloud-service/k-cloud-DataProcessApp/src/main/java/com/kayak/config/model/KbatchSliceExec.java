package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "kbatchSliceExecService", table = "kbatch_slice_exec")
public class KbatchSliceExec {

    @GraphQLField(label = "分片任务ID",field = "sliceExecid")
    private String sliceExecid;
    @GraphQLField(label = "批量任务执行ID",field = "taskExecid", sql = "task_execid = $S{taskExecid}")
    private String taskExecid;
    @GraphQLField(label = "批量任务ID",field = "taskId")
    private String taskId;
    @GraphQLField(label = "步骤号",field = "stepNo",sql ="step_no = $S{stepNo}")
    private String stepNo;
    @GraphQLField(label = "系统模块ID",field = "moduleid")
    private String moduleid;
    @GraphQLField(label = "分片任务状态",field = "sliceStatus",sql ="slice_status = $S{sliceStatus}")
    private String sliceStatus;
    @GraphQLField(label = "分片任务类型",field = "sliceType")
    private String sliceType;
    @GraphQLField(label = "目标代码",field = "targetCode")
    private String targetCode;
    @GraphQLField(label = "产品代码",field = "prodCode")
    private String prodCode;
    @GraphQLField(label = "数据源",field = "datasource",sql ="datasource = $S{datasource}")
    private String datasource;
    @GraphQLField(label = "分片序列号",field = "sliceNo")
    private String sliceNo;
    @GraphQLField(label = "分片起始",field = "sliceStart")
    private String sliceStart;
    @GraphQLField(label = "分片结束",field = "sliceEnd")
    private String sliceEnd;
    @GraphQLField(label = "分片长度",field = "sliceLength")
    private String sliceLength;
    @GraphQLField(label = "业务参数",field = "busiParams")
    private String busiParams;
    @GraphQLField(label = "执行优先级",field = "execOrder")
    private String execOrder;
    @GraphQLField(label = "进入队列时间",field = "inQueueTime")
    private String inQueueTime;
    @GraphQLField(label = "执行起始日期",field = "execStartDate")
    private String execStartDate;
    @GraphQLField(label = "执行起始时间",field = "execStartTime")
    private String execStartTime;
    @GraphQLField(label = "执行结束日期",field = "execEndDate")
    private String execEndDate;
    @GraphQLField(label = "执行结束时间",field = "execEndTime")
    private String execEndTime;
    @GraphQLField(label = "应用服务器名称",field = "appName")
    private String appName;
    @GraphQLField(label = "线程号",field = "threadId")
    private String threadId;
    @GraphQLField(label = "分片任务执行UUID",field = "threadUuid")
    private String threadUuid;
    @GraphQLField(label = "节点号",field = "serverNode")
    private String serverNode;
    @GraphQLField(label = "服务器主机名",field = "serverName")
    private String serverName;
    @GraphQLField(label = "服务器IP",field = "serverIp", sql ="server_ip = $S{serverIp}")
    private String serverIp;
    @GraphQLField(label = "执行结束信息",field = "rtnDesc")
    private String rtnDesc;
    @GraphQLField(label = "创建日期",field = "crtTime")
    private String crtTime;
    @GraphQLField(label = "更新日期",field = "updTime")
    private String updTime;

}
