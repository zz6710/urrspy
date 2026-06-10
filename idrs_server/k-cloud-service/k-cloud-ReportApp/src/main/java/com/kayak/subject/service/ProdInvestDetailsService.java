package com.kayak.subject.service;

import com.kayak.base.dao.ComnDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.ProdInvestDetailsDao;
import com.kayak.subject.model.ProdInvestDetails;


@Service
@APIDefine(desc = "理财产品投资情况明细表服务", model = ProdInvestDetails.class)
public class ProdInvestDetailsService {

	@Autowired
	private ProdInvestDetailsDao prodInvestDetailsDao;

	@Autowired
	private ComnDao comnDao;

	@Autowired
	private DwsProdTTRDBefService taskService;


	@API(desc = "重新生成报表", auth = APIAuth.YES)
	public String generateFormAgainTaskApp(SqlParam<ProdInvestDetails> params) throws Exception {
		String reportDate = params.getModel().getDealDate();
		String paraId = "90000061003";
		return taskService.execTaskApp(reportDate, paraId);
	}

	@API(desc = "查询理财产品投资情况明细表信息", auth = APIAuth.YES)
	public SqlResult<ProdInvestDetails> findProdInvestDetailss(SqlParam<ProdInvestDetails> params) throws Exception {
		return prodInvestDetailsDao.findProdInvestDetailss(params);
	}

	@API(desc = "添加理财产品投资情况明细表", params = "id,product_code,i_code,asset_third_type,i_name_c1,i_name_c2,bottom_code,item_name,comcode,cost,amount,inv_val_rate_csh,currency,orderfreemanage,org_classific,g06_type,zxb_second_type,zxb_first_type,zxb_third_type,prodmonrate,pordownrate,mycompnyamount,investmonamount,investownamount,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,inv_val_rate,non_grt_rate,non_grt_amt,mark,asset_end_date,asset_term_pj,bg_date,over_day,investmonrate,investownrate,prod_type,act_dt,deal_date", auth = APIAuth.NO)
	public int addProdInvestDetails(SqlParam<ProdInvestDetails> params) throws Exception {
		return prodInvestDetailsDao.addProdInvestDetails(params).getEffect();
	}
	
	@API(desc = "修改理财产品投资情况明细表", params = "id,product_code,i_code,asset_third_type,i_name_c1,i_name_c2,bottom_code,item_name,comcode,cost,amount,inv_val_rate_csh,currency,orderfreemanage,org_classific,g06_type,zxb_second_type,zxb_first_type,zxb_third_type,prodmonrate,pordownrate,mycompnyamount,investmonamount,investownamount,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,inv_val_rate,non_grt_rate,non_grt_amt,mark,asset_end_date,asset_term_pj,bg_date,over_day,investmonrate,investownrate,prod_type,act_dt,deal_date", auth = APIAuth.NO)
	public int updateProdInvestDetails(SqlParam<ProdInvestDetails> params) throws Exception {
		return prodInvestDetailsDao.updateProdInvestDetails(params).getEffect();
	}
	
	@API(desc = "删除理财产品投资情况明细表", params = "id,product_code,i_code,asset_third_type,i_name_c1,i_name_c2,bottom_code,item_name,comcode,cost,amount,inv_val_rate_csh,currency,orderfreemanage,org_classific,g06_type,zxb_second_type,zxb_first_type,zxb_third_type,prodmonrate,pordownrate,mycompnyamount,investmonamount,investownamount,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,inv_val_rate,non_grt_rate,non_grt_amt,mark,asset_end_date,asset_term_pj,bg_date,over_day,investmonrate,investownrate,prod_type,act_dt,deal_date", auth = APIAuth.NO)
	public int deleteProdInvestDetails(SqlParam<ProdInvestDetails> params) throws Exception {
		return prodInvestDetailsDao.deleteProdInvestDetails(params).getEffect();
	}
}
