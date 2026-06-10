package com.kayak.dps.check.service;

import com.kayak.dps.check.dao.T8SqlParamInfoDao;
import com.kayak.dps.check.model.T8SqlParamInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Service
@APIDefine(desc = "SQL语句参数配置服务", model = T8SqlParamInfo.class)
public class T8SqlParamInfoService {

	@Autowired
	private T8SqlParamInfoDao t8SqlParamInfoDao;

	@API(desc = "查询SQL语句参数配置信息", auth = APIAuth.YES)
	public SqlResult<T8SqlParamInfo> findT8SqlParamInfos(SqlParam<T8SqlParamInfo> params) throws Exception {
		return t8SqlParamInfoDao.findT8SqlParamInfos(params);
	}

	@API(desc = "添加SQL语句参数配置", params = "id,code,sqlstr,data_type,remark,status", auth = APIAuth.NO)
	public int addT8SqlParamInfo(SqlParam<T8SqlParamInfo> params) throws Exception {
		return t8SqlParamInfoDao.addT8SqlParamInfo(params).getEffect();
	}
	
	@API(desc = "修改SQL语句参数配置", params = "id,code,sqlstr,data_type,remark,status", auth = APIAuth.NO)
	public int updateT8SqlParamInfo(SqlParam<T8SqlParamInfo> params) throws Exception {
		return t8SqlParamInfoDao.updateT8SqlParamInfo(params).getEffect();
	}
	
	@API(desc = "删除SQL语句参数配置", params = "id,code,sqlstr,data_type,remark,status", auth = APIAuth.NO)
	public int deleteT8SqlParamInfo(SqlParam<T8SqlParamInfo> params) throws Exception {
		return t8SqlParamInfoDao.deleteT8SqlParamInfo(params).getEffect();
	}

}
