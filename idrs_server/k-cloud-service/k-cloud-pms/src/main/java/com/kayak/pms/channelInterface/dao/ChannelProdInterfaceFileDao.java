package com.kayak.pms.channelInterface.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.channelInterface.model.ChannelProdInterfaceFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelProdInterfaceFileDao extends ComnDao {

	public SqlResult<ChannelProdInterfaceFile> findChannelProdInterfaceFiles(SqlParam<ChannelProdInterfaceFile> params) throws Exception {
		return super.findRows("SELECT id,task_prod_id,interface_no,file_name,file_path,return_desc,status,crt_user,crt_time,task_flag FROM t8_channel_prod_interface_file", params);
	}

	public UpdateResult addChannelProdInterfaceFile(SqlParam<ChannelProdInterfaceFile> params) throws Exception {
		return super.update("INSERT INTO t8_channel_prod_interface_file(id,task_prod_id,interface_no,file_name,file_path,return_desc,status,crt_user,crt_time,task_flag) VALUES($AUTOIDS{id},$S{taskProdId},$S{interfaceNo},$S{fileName},$S{filePath},$S{returnDesc},$S{status},$S{crtUser},$S{crtTime},$S{taskFlag})",
				params.getModel());
	}
	public UpdateResult addChannelProdInterfaceFile(ChannelProdInterfaceFile params) throws Exception {
		return super.update("INSERT INTO t8_channel_prod_interface_file(id,task_prod_id,interface_no,file_name,file_path,return_desc,status,crt_user,crt_time,task_flag) VALUES($AUTOIDS{id},$S{taskProdId},$S{interfaceNo},$S{fileName},$S{filePath},$S{returnDesc},$S{status},$S{crtUser},$S{crtTime},$S{taskFlag})",
				params);
	}
	
	public UpdateResult updateChannelProdInterfaceFile(SqlParam<ChannelProdInterfaceFile> params) throws Exception {
		return super.update("UPDATE t8_channel_prod_interface_file SET task_prod_id=$S{taskProdId} ,interface_no=$S{interfaceNo} ,file_name=$S{fileName} ,file_path=$S{filePath} ,return_desc=$S{returnDesc} ,status=$S{status} ,crt_user=$S{crtUser} ,crt_time=$S{crtTime},task_flag = $S{taskFlag}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteChannelProdInterfaceFile(SqlParam<ChannelProdInterfaceFile> params) throws Exception {
		return super.update("DELETE FROM t8_channel_prod_interface_file WHERE  id=$S{id} ",
				params.getModel());
	}

	public SqlResult<ChannelProdInterfaceFile> findChannelProdInterfaceFiles1(SqlParam<ChannelProdInterfaceFile> params) throws Exception {
		String sql = "SELECT\n" +
				"\ttcpif.id,\n" +
				"\ttcpif.task_prod_id,\n" +
				"\ttcpt.task_code,\n" +
				"\ttcit.task_name,\n" +
				"\ttcpif.interface_no,\n" +
				"\ttcim.interface_name,\n" +
				"\ttcpt.prod_code,\n" +
				"\ttcpt.prod_name,\n" +
				"\ttcpif.file_name,\n" +
				"\ttcpif.file_path,\n" +
				"\ttcpif.return_desc,\n" +
				"\ttcpif.status,\n" +
				"\ttcpif.crt_user,\n" +
				"\ttcpif.crt_time \n" +
				"FROM\n" +
				"\tt8_channel_prod_interface_file tcpif\n" +
				"\tLEFT JOIN t8_channel_prod_task tcpt ON tcpif.task_prod_id = tcpt.id\n" +
				"\tLEFT JOIN t8_channel_interface_task tcit ON tcpt.task_code = tcit.task_code\n" +
				"\tleft join t8_channel_interface_manager tcim on tcim.interface_no = tcpif.interface_no\n" +
				"\twhere 1=1";
		if (StringUtils.isNotBlank(params.getModel().getTaskCode()))
			sql += " and tcpt.task_code = $S{taskCode}";
		if (StringUtils.isNotBlank(params.getModel().getInterfaceNo()))
			sql += " and tcpif.interface_no like '%"+params.getModel().getInterfaceNo()+"%'";
		if (StringUtils.isNotBlank(params.getModel().getInterfaceName()))
			sql += " and tcim.interface_name like '%"+params.getModel().getInterfaceName()+"%'";
		if (StringUtils.isNotBlank(params.getModel().getStatus()))
			sql += " and tcpif.status = $S{status}";
		sql += " order by tcpif.crt_time desc";
		return super.findRows(sql,params);
	}

	//更新文件名和文件路径
	public UpdateResult updateFileInfoById(ChannelProdInterfaceFile params) throws Exception {
		String sql = "UPDATE t8_channel_prod_interface_file SET file_name=$S{fileName} ,file_path=$S{filePath} ,status=$S{status}   WHERE  id=$S{id} ";
		return super.update(sql,params);
	}
}
