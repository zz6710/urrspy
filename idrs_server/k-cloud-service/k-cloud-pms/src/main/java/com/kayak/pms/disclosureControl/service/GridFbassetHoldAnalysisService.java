package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.GridFbassetHoldAnalysisDao;
import com.kayak.pms.disclosureControl.model.GridFbassetHoldAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "非标资产持仓情况服务", model = GridFbassetHoldAnalysis.class)
public class GridFbassetHoldAnalysisService {

	@Autowired
	private GridFbassetHoldAnalysisDao gridFbassetHoldAnalysisDao;


	@API(desc = "非标资产持仓情况查询", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<GridFbassetHoldAnalysis> findGridFbassetHoldAnalysis(SqlParam<GridFbassetHoldAnalysis> params) throws Exception {
		params.setMakeSql(true);
		return gridFbassetHoldAnalysisDao.findGridFbassetHoldAnalysis(params);
	}

	@API(desc = "非标资产持仓情况修改",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateGridFbassetHoldAnalysis(SqlParam<GridFbassetHoldAnalysis> params) throws Exception {
		return gridFbassetHoldAnalysisDao.updateGridFbassetHoldAnalysis(params);
	}

	@API(desc = "非标资产持仓情况删除",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteGridFbassetHoldAnalysis(SqlParam<GridFbassetHoldAnalysis> params) throws Exception {
		return gridFbassetHoldAnalysisDao.deleteGridFbassetHoldAnalysis(params);
	}

	@API(desc = "查询资产代码与资产名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<GridFbassetHoldAnalysis> findScrIdAndscrNm(SqlParam<GridFbassetHoldAnalysis> params) throws Exception {
		return gridFbassetHoldAnalysisDao.findScrIdAndscrNm(params);
	}
}
