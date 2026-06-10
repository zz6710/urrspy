package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.UnderAssetRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.UnderAssetRegistInfoh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "底层资产历史信息服务", model = UnderAssetRegistInfoh.class)
public class UnderAssetRegistInfohService {

	@Autowired
	private UnderAssetRegistInfohDao underAssetRegistInfohDao;

	@API(desc = "查询底层资产历史信息信息", auth = APIAuth.YES)
	public SqlResult<UnderAssetRegistInfoh> findUnderAssetRegistInfos(SqlParam<UnderAssetRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return underAssetRegistInfohDao.findUnderAssetRegistInfohs(params);
	}

	@API(desc = "添加底层资产历史信息", params = "bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int addUnderAssetRegistInfo(SqlParam<UnderAssetRegistInfoh> params) throws Exception {
		return underAssetRegistInfohDao.addUnderAssetRegistInfoh(params).getEffect();
	}
	
	@API(desc = "修改底层资产历史信息", params = "bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int updateUnderAssetRegistInfo(SqlParam<UnderAssetRegistInfoh> params) throws Exception {
		return underAssetRegistInfohDao.updateUnderAssetRegistInfoh(params).getEffect();
	}
	
	@API(desc = "删除底层资产历史信息", params = "bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int deleteUnderAssetRegistInfo(SqlParam<UnderAssetRegistInfoh> params) throws Exception {
		return underAssetRegistInfohDao.deleteUnderAssetRegistInfoh(params).getEffect();
	}

}
