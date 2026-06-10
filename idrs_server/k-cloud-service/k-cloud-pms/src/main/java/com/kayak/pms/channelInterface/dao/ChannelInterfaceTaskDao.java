package com.kayak.pms.channelInterface.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.channelInterface.model.ChannelInterfaceTask;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChannelInterfaceTaskDao extends ComnDao {

	public SqlResult<ChannelInterfaceTask> findChannelInterfaceTasks(SqlParam<ChannelInterfaceTask> params) throws Exception {
		return super.findRows("SELECT id,task_code,task_name,param_model,is_enabled,issue_type,crt_user,crt_time FROM t8_channel_interface_task order by  crt_time desc", params);
	}

	public UpdateResult addChannelInterfaceTask(SqlParam<ChannelInterfaceTask> params) throws Exception {
		return super.update("INSERT INTO t8_channel_interface_task(id,task_code,task_name,param_model,is_enabled,issue_type,crt_user,crt_time) VALUES($AUTOIDS{id},$S{taskCode},$S{taskName},$S{paramModel},$S{isEnabled},$S{issueType},$S{crtUser},$S{crtTime})",
				params.getModel());
	}

	public UpdateResult addChannelInterfaceTask(ChannelInterfaceTask params) throws Exception {
		return super.update("INSERT INTO t8_channel_interface_task(id,task_code,task_name,param_model,is_enabled,issue_type,crt_user,crt_time) VALUES($AUTOIDS{id},$S{taskCode},$S{taskName},$S{paramModel},$S{isEnabled},$S{issueType},$S{crtUser},$S{crtTime})",
				params);
	}

	public UpdateResult updateChannelInterfaceTask(SqlParam<ChannelInterfaceTask> params) throws Exception {
		return super.update("UPDATE t8_channel_interface_task SET task_code=$S{taskCode} ,task_name=$S{taskName} ,param_model=$S{paramModel} ,is_enabled=$S{isEnabled} ,issue_type=$S{issueType} ,crt_user=$S{crtUser} ,crt_time=$S{crtTime}  WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult updateChannelInterfaceTask(ChannelInterfaceTask params) throws Exception {
		return super.update("UPDATE t8_channel_interface_task SET task_name=$S{taskName} ,param_model=$S{paramModel} ,is_enabled=$S{isEnabled} ,issue_type=$S{issueType}   WHERE  task_code=$S{taskCode} ",
				params);
	}

	public UpdateResult deleteChannelInterfaceTask(SqlParam<ChannelInterfaceTask> params) throws Exception {
		return super.update("DELETE FROM t8_channel_interface_task WHERE  id=$S{id} ",
				params.getModel());
	}
	public UpdateResult deleteChannelInterfaceTask(String taskCode) throws Exception {
		return super.update("DELETE FROM t8_channel_interface_task WHERE  task_code = $S{taskCode} ",
				taskCode);
	}

	public List<ChannelInterfaceTask> findChannelInterfaceTaskByTaskCode(String taskCode) throws Exception {
		String sql = "SELECT id,task_code,task_name,param_model,is_enabled,issue_type,crt_user,crt_time FROM t8_channel_interface_task where task_code = $S{taskCode}";
		return super.findRows(ChannelInterfaceTask.class,sql,0,taskCode);
	}

}
