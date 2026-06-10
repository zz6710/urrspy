package com.kayak.pms.channelInterface.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.channelInterface.model.ChannelInterfaceManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChannelInterfaceManagerDao extends ComnDao {

	public SqlResult<ChannelInterfaceManager> findChannelInterfaceManagers(SqlParam<ChannelInterfaceManager> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,channel_no,channel_name,interface_no,interface_name,file_path,select_sql,interface_action,interface_receive_action,full_query_condition,incremental_query_condition,remark,crt_user,crt_time,channel_seq_type,file_name_sql FROM t8_channel_interface_manager where 1=1 ");
		if (StringUtils.isNotBlank(params.getModel().getChannelNo()))
			sql.append(" and channel_no like '%").append(params.getModel().getChannelNo()).append("%'");
		if (StringUtils.isNotBlank(params.getModel().getChannelName()))
			sql.append(" and channel_name like '%").append(params.getModel().getChannelName()).append("%'");
		if (StringUtils.isNotBlank(params.getModel().getInterfaceNo()))
			sql.append(" and interface_no like '%").append(params.getModel().getInterfaceNo()).append("%'");
		if (StringUtils.isNotBlank(params.getModel().getInterfaceName()))
			sql.append(" and interface_name like '%").append(params.getModel().getInterfaceName()).append("%'");
		sql.append(" order by crt_time desc");
		return super.findRows(sql.toString(), params);
	}

	public UpdateResult addChannelInterfaceManager(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return super.update("INSERT INTO t8_channel_interface_manager(id,channel_no,channel_name,interface_no,interface_name,file_path,select_sql,interface_action,interface_receive_action,full_query_condition,incremental_query_condition,remark,crt_user,crt_time,channel_seq_type,file_name_sql) VALUES($AUTOIDS{id},$S{channelNo},$S{channelName},$S{interfaceNo},$S{interfaceName},$S{filePath},$S{selectSql},$S{interfaceAction},$S{interfaceReceiveAction},$S{fullQueryCondition},$S{incrementalQueryCondition},$S{remark},$S{crtUser},$S{crtTime},$S{channelSeqType},$S{fileNameSql})",
				params.getModel());
	}
	
	public UpdateResult updateChannelInterfaceManager(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return super.update("UPDATE t8_channel_interface_manager SET channel_no=$S{channelNo} ,channel_name=$S{channelName} ,interface_no=$S{interfaceNo} ,interface_name=$S{interfaceName} ,file_path=$S{filePath} ,select_sql=$S{selectSql} ,interface_action=$S{interfaceAction} ,interface_receive_action=$S{interfaceReceiveAction} ,full_query_condition = $S{fullQueryCondition},incremental_query_condition = $S{incrementalQueryCondition} ,remark=$S{remark} ,crt_user=$S{crtUser} ,crt_time=$S{crtTime},channel_seq_type = $S{channelSeqType},file_name_sql = $S{fileNameSql}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteChannelInterfaceManager(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return super.update("DELETE FROM t8_channel_interface_manager WHERE  id=$S{id} ",
				params.getModel());
	}

	public List<ChannelInterfaceManager> findChannelInterfaceManagerByNo(String channelNo, String interfaceNo) throws Exception {
		String sql = "SELECT id,channel_no,channel_name,interface_no,interface_name,file_path,select_sql,interface_action,interface_receive_action,full_query_condition,incremental_query_condition,remark,crt_user,crt_time,channel_seq_type,file_name_sql FROM t8_channel_interface_manager where interface_no = '" + interfaceNo + "'";
		return super.findRows(ChannelInterfaceManager.class,sql,0,channelNo);
	}

	public SqlResult<ChannelInterfaceManager> findAllChannelInterfaceManager(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return super.findRows("select channel_no,channel_name from t8_channel_interface_manager group by channel_no",params);
	}

	public SqlResult<ChannelInterfaceManager> findChannelInterfaceManagerByChannelNo(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return super.findRows("select channel_no,channel_name,interface_no,interface_name from t8_channel_interface_manager where  channel_no = $S{channelNo}  and FIND_IN_SET(interface_no,(select interface_no from t8_channel_interface_task where param_model = $S{paramModel}))",params);
	}

	public SqlResult<ChannelInterfaceManager> findChannelInterfaceManagerByChannelNo1(SqlParam<ChannelInterfaceManager> params) throws Exception {
		return super.findRows("select channel_no,channel_name,interface_no,interface_name from t8_channel_interface_manager where  channel_no = $S{channelNo}",params);
	}

	public List<SqlRow> findChannelInterfaceManagerByChannelNo(String channelNo) throws Exception {
		return super.findRows("select channel_no channelNo,channel_name channelName,interface_no interfaceNo,interface_name interfaceName from t8_channel_interface_manager where  channel_no = $S{channelNo}",channelNo);
	}

	public List<SqlRow> findChannelInterfaceManagerByChannelNo(String channelNo,String taskCode) throws Exception {
		String sql = "SELECT\n" +
				"\tt.channel_no channelNo,\n" +
				"\tt.channel_name channelName,\n" +
				"\tt.interface_no interfaceNo,\n" +
				"\tt.interface_name interfaceName \n" +
				"FROM\n" +
				"\tt8_channel_interface_manager t\n" +
				"\tleft join t8_channel_interface_task_info t1 on t.channel_no = t1.channel_no and t.interface_no = t1.interface_no\n" +
				"WHERE\n" +
				"\tt1.channel_no = '"+channelNo+"' and t1.task_code = '"+taskCode+"'";
		return super.findRows(sql);
	}
}
