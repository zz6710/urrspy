package com.kayak.rpt.zz.errorInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.UnderAssetRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.UnderAssetRgInfoErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "底层资产错误信息服务", model = UnderAssetRgInfoErr.class)
public class UnderAssetRgInfoErrService {

	@Autowired
	private UnderAssetRgInfoErrDao underAssetRgInfoErrDao;

	@API(desc = "查询底层资产错误信息信息", auth = APIAuth.YES)
	public SqlResult<UnderAssetRgInfoErr> findUnderAssetRgInfos(SqlParam<UnderAssetRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return underAssetRgInfoErrDao.findUnderAssetRgInfos(params);
	}

	@API(desc = "添加底层资产错误信息", params = "bank_code_desc,asset_manager_code_desc,convert_sum_amt_desc,asset_sum_number_desc,non_invested_amt_desc,under_asset_code_desc,under_asset_sum_desc,under_convert_sum_amt_desc,report_date_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int addUnderAssetRgInfo(SqlParam<UnderAssetRgInfoErr> params) throws Exception {
		return underAssetRgInfoErrDao.addUnderAssetRgInfo(params).getEffect();
	}
	
	@API(desc = "修改底层资产错误信息", params = "bank_code_desc,asset_manager_code_desc,convert_sum_amt_desc,asset_sum_number_desc,non_invested_amt_desc,under_asset_code_desc,under_asset_sum_desc,under_convert_sum_amt_desc,report_date_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int updateUnderAssetRgInfo(SqlParam<UnderAssetRgInfoErr> params) throws Exception {
		return underAssetRgInfoErrDao.updateUnderAssetRgInfo(params).getEffect();
	}
	
	@API(desc = "删除底层资产错误信息", params = "bank_code_desc,asset_manager_code_desc,convert_sum_amt_desc,asset_sum_number_desc,non_invested_amt_desc,under_asset_code_desc,under_asset_sum_desc,under_convert_sum_amt_desc,report_date_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int deleteUnderAssetRgInfo(SqlParam<UnderAssetRgInfoErr> params) throws Exception {
		return underAssetRgInfoErrDao.deleteUnderAssetRgInfo(params).getEffect();
	}

}
