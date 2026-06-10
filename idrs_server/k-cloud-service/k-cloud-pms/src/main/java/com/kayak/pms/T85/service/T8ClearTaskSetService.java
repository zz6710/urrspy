package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.T85.dao.T8ClearTaskSetDao;
import com.kayak.pms.T85.model.T8ClearTaskSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件名: TaClearTaskSetService.java
 * 描述:  清算任务配置操作服务
 * 创建人: zengzt
 * 创建时间:2020年5月11日上午9:55:14
 */
@Service
@APIDefine(desc = "清算任务配置操作服务", model = T8ClearTaskSet.class)
public class T8ClearTaskSetService {

	@Autowired
	private T8ClearTaskSetDao t8ClearTaskSetDao;

	@API(desc = "查询清算任务信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<T8ClearTaskSet> findTaClearTaskSet(SqlParam<T8ClearTaskSet> params) throws Exception {
	
		//不需要自动拼接sql
		params.setMakeSql(false);
		return t8ClearTaskSetDao.queryTaClearTaskSets(params);
	}
	
}
