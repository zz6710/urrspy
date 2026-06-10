package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.historyInfo.dao.SubseqSubscrRegistInfohDao;
import com.kayak.rpt.zz.historyInfo.model.SubseqSubscrRegistInfoh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品存续期历史信息服务", model = SubseqSubscrRegistInfoh.class)
public class SubseqSubscrRegistInfohService {

	@Autowired
	private SubseqSubscrRegistInfohDao subseqSubscrRegistInfohDao;

	@API(desc = "查询产品存续期历史信息信息", auth = APIAuth.YES)
	public SqlResult<SubseqSubscrRegistInfoh> findSubseqSubscrRegistInfos(SqlParam<SubseqSubscrRegistInfoh> params) throws Exception {
		params.setMakeSql(true);
		return subseqSubscrRegistInfohDao.findSubseqSubscrRegistInfohs(params);
	}

	@API(desc = "添加产品存续期历史信息", params = "bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav", auth = APIAuth.YES)
	public int addSubseqSubscrRegistInfo(SqlParam<SubseqSubscrRegistInfoh> params) throws Exception {
		return subseqSubscrRegistInfohDao.addSubseqSubscrRegistInfoh(params).getEffect();
	}
	
	@API(desc = "修改产品存续期历史信息", params = "bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav", auth = APIAuth.YES)
	public int updateSubseqSubscrRegistInfo(SqlParam<SubseqSubscrRegistInfoh> params) throws Exception {
		return subseqSubscrRegistInfohDao.updateSubseqSubscrRegistInfoh(params).getEffect();
	}
	
	@API(desc = "删除产品存续期历史信息", params = "bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav", auth = APIAuth.YES)
	public int deleteSubseqSubscrRegistInfo(SqlParam<SubseqSubscrRegistInfoh> params) throws Exception {
		return subseqSubscrRegistInfohDao.deleteSubseqSubscrRegistInfoh(params).getEffect();
	}

}
