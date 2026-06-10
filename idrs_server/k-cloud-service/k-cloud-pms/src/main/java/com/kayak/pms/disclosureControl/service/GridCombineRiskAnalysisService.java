package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.GridCombineRiskAnalysisDao;
import com.kayak.pms.disclosureControl.model.GridCombineRiskAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "投资组合资产配置情况及流动性风险分析数据服务", model = GridCombineRiskAnalysis.class)
public class GridCombineRiskAnalysisService {

	@Autowired
	private GridCombineRiskAnalysisDao gridCombineRiskAnalysisDao;


	@API(desc = "投资组合资产配置情况及流动性风险分析数据查询", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<GridCombineRiskAnalysis> findGridCombineRiskAnalysis(SqlParam<GridCombineRiskAnalysis> params) throws Exception {
		params.setMakeSql(true);
		return gridCombineRiskAnalysisDao.findGridCombineRiskAnalysis(params);
	}

	@API(desc = "投资组合资产配置情况及流动性风险分析数据修改",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateGridCombineRiskAnalysis(SqlParam<GridCombineRiskAnalysis> params) throws Exception {
		return gridCombineRiskAnalysisDao.updateGridCombineRiskAnalysis(params);
	}

	@API(desc = "投资组合资产配置情况及流动性风险分析数据删除",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteGridCombineRiskAnalysis(SqlParam<GridCombineRiskAnalysis> params) throws Exception {
		return gridCombineRiskAnalysisDao.deleteGridCombineRiskAnalysis(params);
	}

}
