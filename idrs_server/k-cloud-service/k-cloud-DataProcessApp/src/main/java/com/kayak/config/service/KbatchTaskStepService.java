package com.kayak.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.config.dao.KbatchTaskStepDao;
import com.kayak.config.model.KbatchTaskStep;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件名: TaClearTaskStepService.java
 * 描述: 清算子步骤表
 * 创建人: zengzt
 * 创建时间:2020年6月6日下午4:46:44
 */
@Service
@APIDefine(desc = "清算子步骤操作服务", model = KbatchTaskStep.class)
public class KbatchTaskStepService {

	@Autowired
	private KbatchTaskStepDao batchTaskStepDao;

	@API(desc = "查询清算子步骤信息", auth = APIAuth.NO)
	public SqlResult<KbatchTaskStep> findKbatchTaskSteps(SqlParam<KbatchTaskStep> params) throws Exception {
		params.setMakeSql(true);
		return batchTaskStepDao.findKbatchTaskSteps(params);
	}

	@API(desc = "新增清算子步骤信息", auth = APIAuth.NO)
	public String insertKbatchTaskStep(SqlParam<KbatchTaskStep> params) throws Exception {
		params.setMakeSql(false);
		batchTaskStepDao.insertKbatchTaskStep(params);
		return RequestSupport.updateReturnJson(true, "新增清算子步骤信息成功", null).toString();
	}

	@API(desc = "修改清算子步骤信息", auth = APIAuth.NO)
	public String updateKbatchTaskStep(SqlParam<KbatchTaskStep> params) throws Exception {
		params.setMakeSql(false);
		batchTaskStepDao.updateKbatchTaskStep(params);
		return RequestSupport.updateReturnJson(true, "修改清算子步骤信息成功", null).toString();
	}

	@API(desc = "删除清算子步骤信息", auth = APIAuth.NO)
	public String deleteKbatchTaskStep(SqlParam<KbatchTaskStep> params) throws Exception {
		params.setMakeSql(false);
		batchTaskStepDao.deleteKbatchTaskStep(params);
		return RequestSupport.updateReturnJson(true, "删除清算子步骤信息成功", null).toString();
	}
	
}
