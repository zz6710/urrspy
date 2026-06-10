package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.T85.dao.T8ClearTaskStepDao;
import com.kayak.pms.T85.model.T8ClearTaskStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件名: TaClearTaskStepService.java
 * 描述: 清算子步骤表
 * 创建人: zengzt
 * 创建时间:2020年6月6日下午4:46:44
 */
@Service
@APIDefine(desc = "清算子步骤操作服务", model = T8ClearTaskStep.class)
public class T8ClearTaskStepService {

	@Autowired
	private T8ClearTaskStepDao t8ClearTaskStepDao;

	@API(desc = "查询清算子步骤信息", auth = APIAuth.YES)
	public SqlResult<T8ClearTaskStep> findTaClearTaskSteps(SqlParam<T8ClearTaskStep> params) throws Exception {
		params.setMakeSql(true);
		return t8ClearTaskStepDao.findTaClearTaskSteps(params);
	}

	@API(desc = "新增清算子步骤信息", auth = APIAuth.NO)
	public String insertTaClearTaskStep(SqlParam<T8ClearTaskStep> params) throws Exception {
		params.setMakeSql(false);
		t8ClearTaskStepDao.insertTaClearTaskStep(params);
		return RequestSupport.updateReturnJson(true, "新增清算子步骤信息成功", null).toString();
	}

	@API(desc = "修改清算子步骤信息", auth = APIAuth.NO)
	public String updateTaClearTaskStep(SqlParam<T8ClearTaskStep> params) throws Exception {
		params.setMakeSql(false);
		t8ClearTaskStepDao.updateTaClearTaskStep(params);
		return RequestSupport.updateReturnJson(true, "修改清算子步骤信息成功", null).toString();
	}

	@API(desc = "删除清算子步骤信息", auth = APIAuth.NO)
	public String deleteTaClearTaskStep(SqlParam<T8ClearTaskStep> params) throws Exception {
		params.setMakeSql(false);
		t8ClearTaskStepDao.deleteTaClearTaskStep(params);
		return RequestSupport.updateReturnJson(true, "删除清算子步骤信息成功", null).toString();
	}
	
}
