package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.GridAffiliateOtherAnalysisDao;
import com.kayak.pms.disclosureControl.model.GridAffiliateOtherAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "其他交易关联方数据服务", model = GridAffiliateOtherAnalysis.class)
public class GridAffiliateOtherAnalysisService {

	@Autowired
	private GridAffiliateOtherAnalysisDao gridAffiliateOtherAnalysisDao;


	@API(desc = "其他交易关联方数据查询", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<GridAffiliateOtherAnalysis> findGridAffiliateOtherAnalysis(SqlParam<GridAffiliateOtherAnalysis> params) throws Exception {
		params.setMakeSql(true);
		return gridAffiliateOtherAnalysisDao.findGridAffiliateOtherAnalysis(params);
	}

	@API(desc = "其他交易关联方数据修改",  auth = APIAuth.YES, operation = APIOperation.UPDATE)
	public int updateGridAffiliateOtherAnalysis(SqlParam<GridAffiliateOtherAnalysis> params) throws Exception {
		return gridAffiliateOtherAnalysisDao.updateGridAffiliateOtherAnalysis(params);
	}

	@API(desc = "其他交易关联方数据删除",  auth = APIAuth.YES, operation = APIOperation.DELETE)
	public int deleteGridAffiliateOtherAnalysis(SqlParam<GridAffiliateOtherAnalysis> params) throws Exception {
		return gridAffiliateOtherAnalysisDao.deleteGridAffiliateOtherAnalysis(params);
	}
	@API(desc = "查询资产代码与资产名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<GridAffiliateOtherAnalysis> findScrIdAndscrNm(SqlParam<GridAffiliateOtherAnalysis> params) throws Exception {
		return gridAffiliateOtherAnalysisDao.findScrIdAndscrNm(params);
	}
}
