package com.kayak.pms.channelInterface.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.channelInterface.model.ChannelInterfaceTaskInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChannelInterfaceTaskInfoDao extends ComnDao {

	public SqlResult<ChannelInterfaceTaskInfo> findChannelInterfaceTaskInfos(SqlParam<ChannelInterfaceTaskInfo> params) throws Exception {
		return super.findRows("SELECT id,task_code,channel_no,channel_name,interface_no,crt_user,crt_time FROM t8_channel_interface_task_info", params);
	}

	public UpdateResult addChannelInterfaceTaskInfo(SqlParam<ChannelInterfaceTaskInfo> params) throws Exception {
		return super.update("INSERT INTO t8_channel_interface_task_info(id,task_code,channel_no,channel_name,interface_no,crt_user,crt_time) VALUES($AUTOIDS{id},$S{taskCode},$S{channelNo},$S{channelName},$S{interfaceNo},$S{crtUser},$S{crtTime})",
				params.getModel());
	}

	public UpdateResult addChannelInterfaceTaskInfo(ChannelInterfaceTaskInfo params) throws Exception {
		return super.update("INSERT INTO t8_channel_interface_task_info(id,task_code,channel_no,channel_name,interface_no,crt_user,crt_time) VALUES($AUTOIDS{id},$S{taskCode},$S{channelNo},$S{channelName},$S{interfaceNo},$S{crtUser},$S{crtTime})",
				params);
	}
	
	public UpdateResult updateChannelInterfaceTaskInfo(SqlParam<ChannelInterfaceTaskInfo> params) throws Exception {
		return super.update("UPDATE t8_channel_interface_task_info SET task_code=$S{taskCode} ,channel_no=$S{channelNo} ,channel_name=$S{channelName} ,interface_no=$S{interfaceNo} ,crt_user=$S{crtUser} ,crt_time=$S{crtTime}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteChannelInterfaceTaskInfo(SqlParam<ChannelInterfaceTaskInfo> params) throws Exception {
		return super.update("DELETE FROM t8_channel_interface_task_info WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteChannelInterfaceTaskInfo(String taskCode) throws Exception {
		return super.update("DELETE FROM t8_channel_interface_task_info WHERE  task_code = $S{taskCode} ",
				taskCode);
	}

	public List<ChannelInterfaceTaskInfo> findChannelInterfaceTaskInfoByTaskCode(String taskCode) throws Exception {
		String sql = "select task_code,channel_no,channel_name,group_concat(interface_no) interface_no from t8_channel_interface_task_info where task_code = $S{taskCode} group by task_code";
		return super.findRows(ChannelInterfaceTaskInfo.class,sql,0,taskCode);
	}

	public List<ChannelInterfaceTaskInfo> findChannelInterfaceTaskInfoByTaskCode1(String taskCode) throws Exception {
		String sql = "select task_code,channel_no,channel_name,interface_no from t8_channel_interface_task_info where task_code = $S{taskCode}";
		return super.findRows(ChannelInterfaceTaskInfo.class,sql,0,taskCode);
	}
}
