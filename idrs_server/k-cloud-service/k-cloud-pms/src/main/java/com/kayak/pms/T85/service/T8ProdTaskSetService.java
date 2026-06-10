package com.kayak.pms.T85.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.T85.dao.SysDictItemDao;
import com.kayak.pms.T85.dao.T8ProdTaskSetDao;
import com.kayak.pms.T85.model.T8ProdTaskSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文件名: TaProdTaskSetService.java
 * 描述:  产品清算任务配置
 * 创建人: zengzt
 * 创建时间:2020年5月20日上午11:43:22
 */
@Service
@APIDefine(desc = "产品清算任务配置操作服务", model = T8ProdTaskSet.class)
public class T8ProdTaskSetService {

	@Autowired
	private T8ProdTaskSetDao t8ProdTaskSetDao;
	
	@Autowired
	private SysDictItemDao sysDictItemDao;
	
	@API(desc = "查询产品清算任务配置信息", auth = APIAuth.NO)
	public SqlResult<T8ProdTaskSet> findTaProdTaskSets(SqlParam<T8ProdTaskSet> params) throws Exception {
		
		params.setMakeSql(false);
		return t8ProdTaskSetDao.selectTaProdTaskSets(params);
	}
	
}
