package com.kayak.dps.check.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.check.dao.T8SQLConfigModelDao;
import com.kayak.dps.check.model.T8SQLConfigModel;
import com.kayak.dps.check.util.NextVersionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

import java.util.ArrayList;


@Service
@APIDefine(desc = "接口sql配置服务", model = T8SQLConfigModel.class)
public class T8SQLConfigModelService {

	@Autowired
	private T8SQLConfigModelDao t8SQLConfigModelDao;

	@API(desc = "查询接口sql配置信息", auth = APIAuth.YES)
	public SqlResult<T8SQLConfigModel> findT8SQLConfigModels(SqlParam<T8SQLConfigModel> params) throws Exception {
		params.setMakeSql(false);
		return t8SQLConfigModelDao.findT8SQLConfigModels(params);
	}

	@API(desc = "添加接口sql配置", params = "exeid,sqlid,desc,sqlstr", auth = APIAuth.YES)
	public int addT8SQLConfigModel(SqlParam<T8SQLConfigModel> params) throws Exception {
		return t8SQLConfigModelDao.addT8SQLConfigModel(params).getEffect();
	}
	
	@API(desc = "修改接口sql配置", params = "exeid,sqlid,desc,sqlstr", auth = APIAuth.YES)
	public int updateT8SQLConfigModel(SqlParam<T8SQLConfigModel> params) throws Exception {
		params.getModel().setUserid(SysUtil.getLoginUserid());
		params.getModel().setOperationDate(DateUtil.getNowDate());
		params.getModel().setOperationTime(DateUtil.getNowTime());
		return t8SQLConfigModelDao.updateT8SQLConfigModel(params).getEffect();
	}
	
	@API(desc = "删除接口sql配置", params = "exeid,sqlid,desc,sqlstr", auth = APIAuth.YES)
	public int deleteT8SQLConfigModel(SqlParam<T8SQLConfigModel> params) throws Exception {
		return t8SQLConfigModelDao.deleteT8SQLConfigModel(params).getEffect();
	}

	@API(desc = "获取下一版本最大版本号",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8SQLConfigModel> getMaxVersion(SqlParam<T8SQLConfigModel> param) throws Exception {
		T8SQLConfigModel t8sqlconfigModel = t8SQLConfigModelDao.getMaxVersion(param.getModel().getTaskId(),param.getModel().getExeid());
		String newVersion= NextVersionUtil.getNextVersion(t8sqlconfigModel.getVersion());
		t8sqlconfigModel.setVersion(newVersion);
		SqlResult<T8SQLConfigModel> sqlResult = new SqlResult<>();
		ArrayList<T8SQLConfigModel> list = new ArrayList<>();
		list.add(t8sqlconfigModel);
		sqlResult.setRows(list);
		sqlResult.setResults(list.size());
		sqlResult.setDesensitized(false);
		return sqlResult;
	}

}
