package com.kayak.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.config.dao.SysParamDao;
import com.kayak.config.model.DpsSysParam;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件名: SysParamService.java
 * 描述:
 * 创建人: zengzt
 * 创建时间:2020年5月25日上午11:07:49
 */
@Service
@APIDefine(desc = "清算流程", model = DpsSysParam.class)
public class DpsSysParamService {

	@Autowired
	private SysParamDao sysParamDao;

	@API(desc = "查询清算任务信息", auth = APIAuth.YES)
	public SqlResult<DpsSysParam> findSysParam(SqlParam<DpsSysParam> params) throws Exception {
		return sysParamDao.queryParam(params);
	}

	@API(desc = "检查清算任务是否已经自动执行", auth = APIAuth.YES)
	public SqlResult<DpsSysParam> findAutoExec(SqlParam<DpsSysParam> params) throws Exception {
		params.setMakeSql(false);
		return sysParamDao.findAutoExec(params);
	}

	@API(desc = "修改清算执行状态", auth = APIAuth.YES)
	public String autoExec(SqlParam<DpsSysParam> params) throws Exception {
		//1.自动拼接SQL关闭
		params.setMakeSql(false);

		//2.修改为自动更新
		try {
			sysParamDao.updateSysParamAutoExec(params);
			return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
		} catch (Exception e) {
			return RequestSupport.updateReturnJson(false, "修改失败", null).toString();
		}
	}

}
