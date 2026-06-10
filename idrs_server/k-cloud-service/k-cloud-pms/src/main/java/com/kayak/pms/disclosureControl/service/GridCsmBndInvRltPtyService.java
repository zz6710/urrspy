package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.GridCsmBndInvRltPtyDao;
import com.kayak.pms.disclosureControl.model.GridCsmBndInvRltPty;
import com.kayak.pms.disclosureControl.model.GridFbassetHoldAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "承销证券数据服务", model = GridCsmBndInvRltPty.class)
public class GridCsmBndInvRltPtyService {

	@Autowired
	private GridCsmBndInvRltPtyDao gridCsmBndInvRltPtyDao;


	@API(desc = "承销证券数据查询", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<GridCsmBndInvRltPty> findGridCsmBndInvRltPty(SqlParam<GridCsmBndInvRltPty> params) throws Exception {
		params.setMakeSql(true);
		return gridCsmBndInvRltPtyDao.findGridCsmBndInvRltPty(params);
	}

	@API(desc = "承销证券数据修改",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateGridCsmBndInvRltPty(SqlParam<GridCsmBndInvRltPty> params) throws Exception {
		return gridCsmBndInvRltPtyDao.updateGridCsmBndInvRltPty(params);
	}

	@API(desc = "承销证券数据删除",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteGridCsmBndInvRltPty(SqlParam<GridCsmBndInvRltPty> params) throws Exception {
		return gridCsmBndInvRltPtyDao.deleteGridCsmBndInvRltPty(params);
	}
	@API(desc = "查询资产代码与资产名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<GridCsmBndInvRltPty> findSecuritiesCodeAndSecuritiesName(SqlParam<GridCsmBndInvRltPty> params) throws Exception {
		return gridCsmBndInvRltPtyDao.findSecuritiesCodeAndSecuritiesName(params);
	}
}
