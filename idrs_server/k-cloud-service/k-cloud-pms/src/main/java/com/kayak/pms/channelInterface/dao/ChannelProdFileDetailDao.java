package com.kayak.pms.channelInterface.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.channelInterface.model.ChannelProdFileDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChannelProdFileDetailDao extends ComnDao {

	public SqlResult<ChannelProdFileDetail> findChannelProdFileDetails(SqlParam<ChannelProdFileDetail> params) throws Exception {
		return super.findRows("SELECT id,channel_interface_file_id,data_row_num,status,crt_user,crt_time,upd_time,return_desc FROM t8_channel_prod_file_detail", params);
	}

	public UpdateResult addChannelProdFileDetail(SqlParam<ChannelProdFileDetail> params) throws Exception {
		return super.update("INSERT INTO t8_channel_prod_file_detail(id,channel_interface_file_id,data_row_num,status,crt_user,crt_time,upd_time,return_desc) VALUES($AUTOIDS{id},$S{channelInterfaceFileId},$I{dataRowNum},$S{status},$S{crtUser},$S{crtTime},$S{updTime},$S{returnDesc})",
				params.getModel());
	}

	public UpdateResult addChannelProdFileDetail(ChannelProdFileDetail params) throws Exception {
		return super.update("INSERT INTO t8_channel_prod_file_detail(id,channel_interface_file_id,data_row_num,status,crt_user,crt_time,upd_time,return_desc) VALUES($AUTOIDS{id},$S{channelInterfaceFileId},$I{dataRowNum},$S{status},$S{crtUser},$S{crtTime},$S{updTime},$S{returnDesc})",
				params);
	}
	
	public UpdateResult updateChannelProdFileDetail(SqlParam<ChannelProdFileDetail> params) throws Exception {
		return super.update("UPDATE t8_channel_prod_file_detail SET channel_interface_file_id=$S{channelInterfaceFileId} ,data_row_num=$I{dataRowNum} ,status=$S{status} ,crt_user=$S{crtUser} ,crt_time=$S{crtTime} ,upd_time=$S{updTime},return_desc = $S{returnDesc}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteChannelProdFileDetail(SqlParam<ChannelProdFileDetail> params) throws Exception {
		return super.update("DELETE FROM t8_channel_prod_file_detail WHERE  id=$S{id} ",
				params.getModel());
	}


	public int findMaxDataRowNum() throws Exception {
		SqlRow row = super.findRow("select max(data_row_num) max from t8_channel_prod_file_detail",null);
		if (row != null && StringUtils.isNotBlank(row.getString("max")))
			return row.getInteger("max") + 1;
		return 1;
	}

	public void addChannelProdFileDetail(List<ChannelProdFileDetail> detailList) throws Exception {
		String sql = "INSERT INTO t8_channel_prod_file_detail(id,channel_interface_file_id,data_row_num,status,crt_user,crt_time,upd_time,return_desc) VALUES($AUTOIDS{id},$S{channelInterfaceFileId},$I{dataRowNum},$S{status},$S{crtUser},$S{crtTime},$S{updTime},$S{returnDesc})";
		String crtUser = (String) SysUtil.getSysUserParamValue("sys_user_username");
		String crtTime = DateUtil.getTimestamp19();

		for (ChannelProdFileDetail detail : detailList) {
			detail.setStatus("6");
			detail.setCrtUser(crtUser);
			detail.setCrtTime(crtTime);
			super.update(sql,detail);
		}
	}

	public SqlResult<ChannelProdFileDetail> findChannelProdFileDetails1(SqlParam<ChannelProdFileDetail> params) throws Exception {
		String sql = "SELECT\n" +
				"\ttcpfd.id,\n" +
				"\ttcpfd.channel_interface_file_id,\n" +
				"\ttcpfd.data_row_num,\n" +
				"\ttcpfd.status,\n" +
				"\ttcpfd.crt_user,\n" +
				"\ttcpfd.crt_time,\n" +
				"\ttcpfd.upd_time,\n" +
				"\ttcpfd.return_desc,\n" +
				"\ttcpif.file_name\n" +
				"FROM\n" +
				"\tt8_channel_prod_file_detail tcpfd left join t8_channel_prod_interface_file tcpif\n" +
				"\ton tcpfd.channel_interface_file_id = tcpif.id\n" +
				"\twhere 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getChannelInterfaceFileId()))
			sql += " and tcpif.id = $S{channelInterfaceFileId}";
		sql += "order by data_row_num asc";
		return super.findRows(sql,params);
	}
}
