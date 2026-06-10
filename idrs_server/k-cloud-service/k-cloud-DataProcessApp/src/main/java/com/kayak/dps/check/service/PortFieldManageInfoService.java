package com.kayak.dps.check.service;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.check.dao.PortFieldManageInfoDao;
import com.kayak.dps.check.model.PortFieldManageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;


@Service
@APIDefine(desc = "接口文件字段信息服务", model = PortFieldManageInfo.class)
public class PortFieldManageInfoService {

	@Autowired
	private PortFieldManageInfoDao portFieldManageInfoDao;

	@API(desc = "查询接口文件字段信息信息", auth = APIAuth.NO)
	public SqlResult<PortFieldManageInfo> findPortFieldManageInfos(SqlParam<PortFieldManageInfo> params) throws Exception {
		params.setMakeSql(true);

		return portFieldManageInfoDao.findPortFieldManageInfos(params);
	}

	@API(desc = "添加接口文件字段信息", params = "id,port_code,field_code,field_name,field_type,field_length,field_dights,field_seq,file_field_code,inputuser,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.YES)
	public int addPortFieldManageInfo(SqlParam<PortFieldManageInfo> params) throws Exception {
		params.getModel().setInputuser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return portFieldManageInfoDao.addPortFieldManageInfo(params).getEffect();
	}

	@API(desc = "修改接口文件字段信息", params = "id,port_code,field_code,field_name,field_type,field_length,field_dights,field_seq,file_field_code,inputuser,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.YES)
	public int updatePortFieldManageInfo(SqlParam<PortFieldManageInfo> params) throws Exception {
		params.getModel().setInputuser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return portFieldManageInfoDao.updatePortFieldManageInfo(params).getEffect();
	}

	@API(desc = "删除接口文件字段信息", params = "id,port_code,field_code,field_name,field_type,field_length,field_dights,field_seq,inputuser,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.YES)
	public int deletePortFieldManageInfo(SqlParam<PortFieldManageInfo> params) throws Exception {
		return portFieldManageInfoDao.deletePortFieldManageInfo(params).getEffect();
	}

}
