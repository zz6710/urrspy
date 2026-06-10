package com.kayak.config.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

/**
 * 文件名: Ta5014Detail.java
 * 描述:  任务执行表MODEL
 * 创建人: xiaofu
 * 创建时间:2020年4月26日下午2:20:39
 */

@GraphQLModel(fetcher = "ta5014DetailService",table = "kbatch_task_exec")
public class Ta5014Detail {

	@GraphQLField(kkhtml = "KFieldText", label = "任务名称", field = "task_name", sql = "a.TASK_NAME = $S{taskName}")
	private String taskName;

	@GraphQLField(label = "清算执行日期" ,field = "query_task_date")
	private String queryTaskDate;

	@GraphQLField(label = "清算批次", sql = "a.TASK_GROUP = $S{taskGroup}" ,field = "task_group")
	private String taskGroup;
	@GraphQLField(label = "清算批次名称",field = "task_group_name")
	private String taskGroupName;

	@GraphQLField(kkhtml = "KFieldSelect",label = "产品代码", sql = "a.PROD_CODE = $S{prodCode}" ,field = "prod_code",
			kkhtmlExt="{\"data-action\":\"TaDict.findTaProdInfos\",\"data-display-field\":\"prodName\",\"data-value-field\":\"prodCode\",\"v-if\":\"execTaskType==2\"}")
	private String prodCode;
	@GraphQLField(label = "产品名称", field = "prod_name" )
	private String prodName;


	@GraphQLField(kkhtml = "KFieldSelect",label = "销售商代码", sql = "a.TARGET_CODE = $S{targetCode}" ,field = "target_code",
			kkhtmlExt="{\"data-action\":\"TaDict.findTaDistributorInfos\",\"data-display-field\":\"distributorName\",\"data-value-field\":\"distributorCode\"}")
	private String targetCode;
	@GraphQLField(label = "销售商名称",field = "distributor_name")
	private String distributorName;

	//执行状态到DAO层自己拼接SQL了
	@GraphQLField(kkhtml = "KFieldSelect",label = "执行状态", field = "exec_status", kkhtmlExt="{\"data-dict\":\"exec_status\",\"data-displaykeyvalue\":\"true\"}")
	private String execStatus;

	@GraphQLField(label = "清算流程", field = "simple_flow")
	private String simpleFlow;

	@GraphQLField(label = "按钮是否展示", field = "button_is_display")
	private String buttonIsDisplay;
	@GraphQLField(label = "表格展示ID", field = "exec_grid_id")
	private String execGridId;
	@GraphQLField(label = "表格展示上级ID", field = "parent_exec_grid_id")
	private String parentExecGridId;
	@GraphQLField(label = "首行展示描述", field = "head_desc")
	private String headDesc;
	@GraphQLField(label = "任务执行ID", field = "task_execid")
	private String taskExecid;
	@GraphQLField(label = "模块代码", field = "moduleid")
	private String moduleid;
	@GraphQLField(label = "任务ID", field = "task_id")
	private String taskId;
	@GraphQLField(label = "执行日期", field = "exec_date")
	private String execDate;
	@GraphQLField(label = "应执行日期", field = "should_exec_date")
	private String shouldExecDate;
	@GraphQLField(label = "应执行时间", field = "should_exec_time")
	private String shouldExecTime;
	@GraphQLField(label = "超时警告时间", field = "alarm_time")
	private String alarmTime;
	@GraphQLField(label = "执行开始时间", field = "start_time")
	private String startTime;
	@GraphQLField(label = "执行结束时间", field = "end_time")
	private String endTime;
	@GraphQLField(label = "任务执行UUID", field = "thread_uuid")
	private String threadUuid;
	@GraphQLField(label = "服务器节点信息", field = "server_node")
	private String serverNode;
	@GraphQLField(label = "服务器主机名", field = "server_name")
	private String serverName;
	@GraphQLField(label = "服务器主机IP", field = "server_ip")
	private String serverIp;
	@GraphQLField(label = "返回码", field = "rtn_code")
	private String rtnCode;
	@GraphQLField(label = "返回信息", field = "rtn_desc")
	private String rtnDesc;
	@GraphQLField(label = "任务参数", field = "task_params")
	private String taskParams;
	@GraphQLField(label = "前置任务ID", field = "pre_task_id")
	private String preTaskId;
	@GraphQLField(label = "任务日期", field = "task_date")
	private String taskDate;
	@GraphQLField(label = "是否可重复执行", field = "can_again")
	private String canAgain;
	@GraphQLField(label = "任务类型", field = "task_type")
	private String taskType;
	@GraphQLField(label = "销售商文件批次号", field = "distributor_batch")
	private String distributorBatch;

	@GraphQLField(label = "组排序", field = "groupOrder")
	private String groupOrder;
	@GraphQLField(label = "任务排序", field = "taskOrder")
	private String taskOrder;

	@GraphQLField(label="所属清算组类型")
	private String execTaskType;

    @GraphQLField(label="当前页")
    private Integer currentPage;

    @GraphQLField(label="一页显示条数")
    private Integer limit;

    @GraphQLField(label="总条数")
    private String total;

    @GraphQLField(label = "执行步骤",field = "stepNo")
    private String stepNo;

    public String getStepNo() {
        return stepNo;
    }

    public void setStepNo(String stepNo) {
        this.stepNo = stepNo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getExecTaskType() {
		return execTaskType;
	}

	public void setExecTaskType(String execTaskType) {
		this.execTaskType = execTaskType;
	}

	public String getGroupOrder() {
		return groupOrder;
	}
	public void setGroupOrder(String groupOrder) {
		this.groupOrder = groupOrder;
	}
	public String getTaskOrder() {
		return taskOrder;
	}
	public void setTaskOrder(String taskOrder) {
		this.taskOrder = taskOrder;
	}
	public String getTaskGroupName() {
		return taskGroupName;
	}
	public void setTaskGroupName(String taskGroupName) {
		this.taskGroupName = taskGroupName;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	public String getSimpleFlow() {
		return simpleFlow;
	}
	public void setSimpleFlow(String simpleFlow) {
		this.simpleFlow = simpleFlow;
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
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getTargetCode() {
		return targetCode;
	}
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}
	public String getExecStatus() {
		return execStatus;
	}
	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}
	public String getTaskExecid() {
		return taskExecid;
	}
	public void setTaskExecid(String taskExecid) {
		this.taskExecid = taskExecid;
	}
	public String getModuleid() {
		return moduleid;
	}
	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getExecDate() {
		return execDate;
	}
	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}
	public String getShouldExecDate() {
		return shouldExecDate;
	}
	public void setShouldExecDate(String shouldExecDate) {
		this.shouldExecDate = shouldExecDate;
	}
	public String getShouldExecTime() {
		return shouldExecTime;
	}
	public void setShouldExecTime(String shouldExecTime) {
		this.shouldExecTime = shouldExecTime;
	}
	public String getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getThreadUuid() {
		return threadUuid;
	}
	public void setThreadUuid(String threadUuid) {
		this.threadUuid = threadUuid;
	}
	public String getServerNode() {
		return serverNode;
	}
	public void setServerNode(String serverNode) {
		this.serverNode = serverNode;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getRtnCode() {
		return rtnCode;
	}
	public void setRtnCode(String rtnCode) {
		this.rtnCode = rtnCode;
	}
	public String getRtnDesc() {
		return rtnDesc;
	}
	public void setRtnDesc(String rtnDesc) {
		this.rtnDesc = rtnDesc;
	}
	public String getTaskParams() {
		return taskParams;
	}
	public void setTaskParams(String taskParams) {
		this.taskParams = taskParams;
	}
	public String getPreTaskId() {
		return preTaskId;
	}
	public void setPreTaskId(String preTaskId) {
		this.preTaskId = preTaskId;
	}
	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getCanAgain() {
		return canAgain;
	}
	public void setCanAgain(String canAgain) {
		this.canAgain = canAgain;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getExecGridId() {
		return execGridId;
	}
	public void setExecGridId(String execGridId) {
		this.execGridId = execGridId;
	}
	public String getParentExecGridId() {
		return parentExecGridId;
	}
	public void setParentExecGridId(String parentExecGridId) {
		this.parentExecGridId = parentExecGridId;
	}
	public String getHeadDesc() {
		return headDesc;
	}
	public void setHeadDesc(String headDesc) {
		this.headDesc = headDesc;
	}
	public String getButtonIsDisplay() {
		return buttonIsDisplay;
	}
	public void setButtonIsDisplay(String buttonIsDisplay) {
		this.buttonIsDisplay = buttonIsDisplay;
	}
	public String getDistributorBatch() {
		return distributorBatch;
	}
	public void setDistributorBatch(String distributorBatch) {
		this.distributorBatch = distributorBatch;
	}
	
}
