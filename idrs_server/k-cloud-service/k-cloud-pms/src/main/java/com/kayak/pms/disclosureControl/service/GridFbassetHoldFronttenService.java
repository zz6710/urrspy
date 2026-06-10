package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.GridFbassetHoldFronttenDao;
import com.kayak.pms.disclosureControl.model.GridFbassetHoldAnalysis;
import com.kayak.pms.disclosureControl.model.GridFbassetHoldFrontten;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "前十项资产数据服务", model = GridFbassetHoldFrontten.class)
public class GridFbassetHoldFronttenService {

	@Autowired
	private GridFbassetHoldFronttenDao gridFbassetHoldFronttenDao;


	@API(desc = "前十项资产数据查询", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<GridFbassetHoldFrontten> findGridFbassetHoldFrontten(SqlParam<GridFbassetHoldFrontten> params) throws Exception {
		params.setMakeSql(true);
		return gridFbassetHoldFronttenDao.findGridFbassetHoldFrontten(params);
	}

	@API(desc = "前十项资产数据修改",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateGridFbassetHoldFrontten(SqlParam<GridFbassetHoldFrontten> params) throws Exception {
		return gridFbassetHoldFronttenDao.updateGridFbassetHoldFrontten(params);
	}

	@API(desc = "前十项资产数据删除",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteGridFbassetHoldFrontten(SqlParam<GridFbassetHoldFrontten> params) throws Exception {
		return gridFbassetHoldFronttenDao.deleteGridFbassetHoldFrontten(params);
	}

	@API(desc = "查询资产代码与资产名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<GridFbassetHoldFrontten> findScrIdAndscrNm(SqlParam<GridFbassetHoldFrontten> params) throws Exception {
		return gridFbassetHoldFronttenDao.findScrIdAndscrNm(params);
	}

}
