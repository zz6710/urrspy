package com.kayak.pms.channelInterface.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.channelInterface.model.ChannelFileInfo;
import com.kayak.pms.channelInterface.model.ChannelProdTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChannelProdTaskDao extends ComnDao {

	public SqlResult<ChannelProdTask> findChannelProdTasks(SqlParam<ChannelProdTask> params) throws Exception {
		String sql = "SELECT\n" +
				"\tt.id,\n" +
				"\tt.task_code,\n" +
				"\tt1.task_name,\n" +
				"\tt.prod_code,\n" +
				"\tt.prod_name,\n" +
				"\tt.task_status,\n" +
				"\tt.prod_flag,\n" +
				"\tt.param_code,\n" +
				"\tt.crt_user_id,\n" +
				"\tt.crt_user,\n" +
				"\tt.crt_time,\n" +
				"\tt.params \n" +
				"FROM\n" +
				"\tt8_channel_prod_task t \n" +
				"\tleft join t8_channel_interface_task t1 on t.task_code = t1.task_code where 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getTaskName()))
			sql += " and t1.task_name like '%" + params.getModel().getTaskName() + "%'";
		if (StringUtils.isNotBlank(params.getModel().getProdCode()))
			sql += " and t.prod_code = $S{prodCode}";
		if (StringUtils.isNotBlank(params.getModel().getParamModel()))
			sql += " and t1.param_model = $S{paramModel}";
		if (StringUtils.isNotBlank(params.getModel().getProdFlag()))
			sql += " and t1.prod_flag = $S{prodFlag}";

		sql += " order by crt_time desc";
		return super.findRows(sql, params);
	}

	public UpdateResult addChannelProdTask(SqlParam<ChannelProdTask> params) throws Exception {
		return super.update("INSERT INTO t8_channel_prod_task(id,task_code,prod_code,prod_name,task_status,prod_flag,param_code,crt_user_id,crt_user,crt_time,params) VALUES($AUTOIDS{id},$S{taskCode},$S{prodCode},$S{prodName},$S{taskStatus},$S{prodFlag},$S{paramCode},$S{crtUserId},$S{crtUser},$S{crtTime},$S{params})",
				params.getModel());
	}
	public UpdateResult addChannelProdTask(ChannelProdTask params) throws Exception {
		return super.update("INSERT INTO t8_channel_prod_task(id,task_code,prod_code,prod_name,task_status,prod_flag,param_code,crt_user_id,crt_user,crt_time,params) VALUES($AUTOIDS{id},$S{taskCode},$S{prodCode},$S{prodName},$S{taskStatus},$S{prodFlag},$S{paramCode},$S{crtUserId},$S{crtUser},$S{crtTime},$S{params})",
				params);
	}

	public UpdateResult updateChannelProdTask(SqlParam<ChannelProdTask> params) throws Exception {
		return super.update("UPDATE t8_channel_prod_task SET task_code=$S{taskCode} ,prod_code=$S{prodCode} ,prod_name=$S{prodName} ,task_status=$S{taskStatus} ,prod_flag=$S{prodFlag} ,param_code=$S{paramCode} ,crt_user_id=$S{crtUserId} ,crt_user=$S{crtUser} ,crt_time=$S{crtTime} ,params=$S{params}  WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteChannelProdTask(SqlParam<ChannelProdTask> params) throws Exception {
		return super.update("DELETE FROM t8_channel_prod_task WHERE  id=$S{id} ",
				params.getModel());
	}

	public List<ChannelFileInfo> findAllChannelFiles() throws Exception {
		String sql = "SELECT\n" +
				"\ttcpif.id,\n" +
				"\ttcpif.task_prod_id,\n" +
				"\ttcpif.interface_no,\n" +
				"\ttcpif.status,\n" +
				"\ttcpif.task_flag,\n" +
				"\ttcpif.file_path,\n" +
				"\ttcim.file_name_sql,\n" +
				"\ttcim.channel_seq_type,\n" +
				"\ttcpt.params,\n" +
				"\ttcim.select_sql,\n" +
				"\ttcim.full_query_condition,\n" +
				"\ttcim.incremental_query_condition,\n" +
				"\ttcim.file_path \n" +
				"FROM\n" +
				"\tt8_channel_prod_interface_file tcpif\n" +
				"\tLEFT JOIN t8_channel_prod_task tcpt ON tcpif.task_prod_id = tcpt.id\n" +
				"\tLEFT JOIN t8_channel_interface_manager tcim ON tcim.interface_no = tcpif.interface_no \n" +
				"WHERE\n" +
				"\tSTATUS = '0'";
		return super.findRows(ChannelFileInfo.class,sql,0,null);
	}

	public SqlRow getSeqByInterfaceNo(String interfaceNo) throws Exception {
		String sql = "select seq_no seqNo from t8_channel_sequence_info where channel_code = '"+interfaceNo+"'";
		return super.findRow(sql,interfaceNo);
	}

	public UpdateResult addInterfaceNoSeq(Map<String,Object> params) throws Exception {
		String sql = "insert into t8_channel_sequence_info (id,serial_no,channel_code,seq_no,crt_date,crt_time,data_date) values ($AUTOIDS{id},'',$S{channelCode},$S{seqNo},$S{crtDate},$S{crtTime},$S{dataDate})";
		return super.update(sql,params);
	}

	public UpdateResult updateInterfaceNoSeq(Map<String,Object> params) throws Exception {
		String sql = "update t8_channel_sequence_info set seq_no = $S{seqNo} where channel_code = $S{channelCode}";
		return super.update(sql,params);
	}

	public UpdateResult updateTaskStatus(String taskId,String taskStatus) throws Exception {
		String sql = "update t8_channel_prod_task set task_status = '"+taskStatus+"' where id = '"+taskId+"'";
		return super.update(sql,taskId);
	}

	public List<ChannelProdTask> findChannelProdTaskById(String taskId,String status) throws Exception {
		String sql = "select id,task_code,prod_code,prod_name,task_status,prod_flag,param_code,crt_user_id,crt_user,crt_time,params from t8_channel_prod_task where id = '"+taskId+"' and task_status = '"+status+"'";
		return super.findRows(ChannelProdTask.class,sql,0,taskId);
	}
}
