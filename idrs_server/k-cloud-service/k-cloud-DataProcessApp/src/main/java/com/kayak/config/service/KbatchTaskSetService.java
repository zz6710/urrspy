package com.kayak.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.config.dao.KbatchTaskSetDao;
import com.kayak.config.model.KbatchTaskSet;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件名: TaClearTaskSetService.java
 * 描述:  清算任务配置操作服务
 * 创建人: zengzt
 * 创建时间:2020年5月11日上午9:55:14
 */
@Service
@APIDefine(desc = "清算任务配置操作服务", model = KbatchTaskSet.class)
public class KbatchTaskSetService {

	@Autowired
	private KbatchTaskSetDao taskSetDao;

	@API(desc = "查询清算任务信息", auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<KbatchTaskSet> findKbatchTaskSet(SqlParam<KbatchTaskSet> params) throws Exception {
		//不需要自动拼接sql
		params.setMakeSql(false);
		return taskSetDao.queryKbatchTaskSets(params);
	}
	
}
