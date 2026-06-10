package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * 文件名: TaClearTaskInfo.java
 * 描述:  清算任务信息
 * 创建人: zengzt
 * 创建时间:2020年5月9日下午4:54:38
 */
@Data
@GraphQLModel(fetcher = "kbatchTaskInfoService", table = "kbatch_task_info")
public class KbatchTaskInfo {

	@GraphQLField(kkhtml = "KFieldText",label = "任务ID",sql = " task_id like '%$U{taskId}%'",field = "taskId")
	private String taskId;
	@GraphQLField(kkhtml = "KFieldSelect",label = "清算类型",sql = " task_type = $S{taskType} ", field = "taskType",kkhtmlExt="{\"data-dict\":\"task_type\"}")
	private String taskType;
	@GraphQLField(kkhtml = "KFieldText",label = "任务名称",sql = " task_name like '%$U{taskName}%' ",field = "taskName")
	private String taskName;
	@GraphQLField(kkhtml = "KFieldSelect",label = "所属模块",sql = " task_model = $S{taskModel} ", field = "task_model",kkhtmlExt="{\"data-dict\":\"task_model\"}")
	private String taskModel;

	@GraphQLField(label = "业务系统编号",sql = " moduleid = $S{moduleid}", field = "moduleid")
	private String moduleid;
	@GraphQLField(label = "是否可重复执行（0否1是）",field = "canAgain")
	private String canAgain;
	@GraphQLField(label = "清算任务服务类",field = "serviceClass")
	private String serviceClass;
	@GraphQLField(label = "清算任务请求类",field = "reqClass")
	private String reqClass;
	@GraphQLField(label = "接口暴露入口类",field = "inClass")
	private String inClass;
	@GraphQLField(label = "所属清算流程块ID，清算流程页面按此ID展示",field = "flowId")
	private String simpleFlow;
	@GraphQLField(label = "产品清算所属的生命周期时段（01-募集、02-成立、03-存续、04-到期）",field = "lifecycleType")
	private String lifecycleType;
	
	/** 关联查询所需字段，非表中字段  **/
	@GraphQLField(label = "是否已经设置到清算任务配置(0-否，1-是)",field = "inTaskSet")
	private String inTaskSet;
	@GraphQLField(label = "清算组代码",field = "taskGroup")
	private String taskGroup;
	@GraphQLField(label = "产品形态",field = "prodMode")
	private String prodMode;
    @GraphQLField(label = "前置任务",field = "preTaskId")
    private String preTaskId;

    @GraphQLField(label = "清算任务组集合")
    private String taskGroups;

	@GraphQLField(label = "跑批模型",field = "batchMode")
	private String batchMode;


	
}
