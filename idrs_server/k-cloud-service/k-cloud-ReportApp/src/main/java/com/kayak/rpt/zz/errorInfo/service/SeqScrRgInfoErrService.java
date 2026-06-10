package com.kayak.rpt.zz.errorInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.SeqScrRgInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.SeqScrRgInfoErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品存续期错误信息服务", model = SeqScrRgInfoErr.class)
public class SeqScrRgInfoErrService {

	@Autowired
	private SeqScrRgInfoErrDao seqScrRgInfoErrDao;

	@API(desc = "查询产品存续期错误信息信息", auth = APIAuth.YES)
	public SqlResult<SeqScrRgInfoErr> findSeqScrRgInfos(SqlParam<SeqScrRgInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return seqScrRgInfoErrDao.findSeqScrRgInfos(params);
	}

	@API(desc = "添加产品存续期错误信息", params = "bank_code_desc,prod_code_desc,initial_nav_desc,nav_desc,aggregate_nav_desc,nav_cur_desc,convert_rmb_nav_desc,convert_rmb_agg_nav_desc,realized_annual_return_desc,expected_annual_return_desc,inconme_bank_desc,business_start_date_desc,business_end_date_desc,cur_desc,sub_amt_lass_period_desc,cur_principal_period_desc,cur_pay_period_desc,subscribed_latest_vol_desc,redeemed_latest_vol_desc,units_bonus_desc,cash_bonus_desc,prod_amt_desc,prod_vol_desc,details_desc,convert_initial_nav_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int addSeqScrRgInfo(SqlParam<SeqScrRgInfoErr> params) throws Exception {
		return seqScrRgInfoErrDao.addSeqScrRgInfo(params).getEffect();
	}
	
	@API(desc = "修改产品存续期错误信息", params = "bank_code_desc,prod_code_desc,initial_nav_desc,nav_desc,aggregate_nav_desc,nav_cur_desc,convert_rmb_nav_desc,convert_rmb_agg_nav_desc,realized_annual_return_desc,expected_annual_return_desc,inconme_bank_desc,business_start_date_desc,business_end_date_desc,cur_desc,sub_amt_lass_period_desc,cur_principal_period_desc,cur_pay_period_desc,subscribed_latest_vol_desc,redeemed_latest_vol_desc,units_bonus_desc,cash_bonus_desc,prod_amt_desc,prod_vol_desc,details_desc,convert_initial_nav_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int updateSeqScrRgInfo(SqlParam<SeqScrRgInfoErr> params) throws Exception {
		return seqScrRgInfoErrDao.updateSeqScrRgInfo(params).getEffect();
	}
	
	@API(desc = "删除产品存续期错误信息", params = "bank_code_desc,prod_code_desc,initial_nav_desc,nav_desc,aggregate_nav_desc,nav_cur_desc,convert_rmb_nav_desc,convert_rmb_agg_nav_desc,realized_annual_return_desc,expected_annual_return_desc,inconme_bank_desc,business_start_date_desc,business_end_date_desc,cur_desc,sub_amt_lass_period_desc,cur_principal_period_desc,cur_pay_period_desc,subscribed_latest_vol_desc,redeemed_latest_vol_desc,units_bonus_desc,cash_bonus_desc,prod_amt_desc,prod_vol_desc,details_desc,convert_initial_nav_desc,register_serno,imp_date", auth = APIAuth.NO)
	public int deleteSeqScrRgInfo(SqlParam<SeqScrRgInfoErr> params) throws Exception {
		return seqScrRgInfoErrDao.deleteSeqScrRgInfo(params).getEffect();
	}

}
