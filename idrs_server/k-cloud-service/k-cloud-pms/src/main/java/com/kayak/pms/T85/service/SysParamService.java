package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.T85.dao.SysParamDao;
import com.kayak.pms.T85.model.SysParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件名: SysParamService.java
 * 描述: 
 * 创建人: zengzt
 * 创建时间:2020年5月25日上午11:07:49
 */
@Service
@APIDefine(desc = "清算流程", model = SysParam.class)
public class SysParamService {

	@Autowired
	private SysParamDao sysParamDao;

	@API(desc = "查询清算任务信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SysParam> findSysParam(SqlParam<SysParam> params) throws Exception {
		
		return sysParamDao.queryParam(params);
		
	}
	
}
