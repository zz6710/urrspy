package com.kayak.system.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "systemOperationLogService", table = "sys_operation_log")
public class SystemOperationLog {

	@GraphQLField(key = true, label = "ID", sql = "id = $S{id}", field = "id")
	private String id;
	@GraphQLField(label = "操作人ID", sql = "userid = $S{userid}", field = "userid")
	private String userid;
	@GraphQLField(kkhtmlDefault = true, kkhtml = "KFieldText", label = "操作人姓名", sql = "username LIKE '%$U{username}%'", field = "username")
	private String username;
	@GraphQLField
	private String server;
	@GraphQLField(label = "操作服务", sql = "server_desc = $S{serverDesc}", field = "server_desc")
	private String serverDesc;
	@GraphQLField
	private String method;
	@GraphQLField(kkhtmlDefault = true, kkhtml = "KFieldText", label = "操作方法", sql = "method_desc LIKE '%$U{methodDesc}%'", field = "method_desc")
	private String methodDesc;
	@GraphQLField(label = "原数据", sql = "submit_old_data = $S{submitOldData}", field = "submit_old_data")
	private String submitOldData;
	@GraphQLField(label = "提交数据", sql = "submit_data = $S{submitData}", field = "submit_data")
	private String submitData;
	@GraphQLField(kkhtml = "KFieldText", label = "操作结果", sql = "result = $S{result}", field = "result")
	private String result;
	@GraphQLField(kkhtml = "KFieldText", label = "错误信息", sql = "error_msg = $S{errorMsg}", field = "error_msg")
	private String errorMsg;
	@GraphQLField(kkhtmlDefault = true, kkhtml = "KFieldDate", label = "操作时间", sql = "operation_date >= $S{operationDate}", field = "operation_date", kkhtmlExt = "{'data-type':'daterange',endDateFeild:'operationEndDate'}")
	private String operationDate;
	@GraphQLField(label = "操作时间", sql = "operation_date <= $S{operationEndDate}", field = "operation_date")
	private String operationEndDate;
	@GraphQLField(label = "操作时间", sql = "operation_time = $S{operationTime}", field = "operation_time")
	private String operationTime;

}
