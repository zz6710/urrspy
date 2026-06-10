package com.kayak.code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.code.dao.DbTableDao;
import com.kayak.code.model.DbTable;
import com.kayak.code.model.DbTableField;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Service
@APIDefine(desc = "表服务", model = DbTable.class)
public class DbTableService {

	@Autowired
	private DbTableDao dbTableDao;

	@API(desc = "获取表列表", operation = APIOperation.SELECT, auth = APIAuth.YES)
	public SqlResult<DbTable> findTables(SqlParam<DbTable> params) throws Exception {
		return dbTableDao.findTables(params);
	}
	
	@API(desc = "获取表字段列表", operation = APIOperation.SELECT, auth = APIAuth.YES)
	public SqlResult<DbTableField> findTableFields(SqlParam<DbTableField> params) throws Exception {
		return dbTableDao.findTableFields(params);
	}

}
