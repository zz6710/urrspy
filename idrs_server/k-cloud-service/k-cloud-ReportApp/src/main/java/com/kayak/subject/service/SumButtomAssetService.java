package com.kayak.subject.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.SumButtomAssetDao;
import com.kayak.subject.model.SumButtomAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "底层估值明细表（调整前）服务", model = SumButtomAsset.class)
public class SumButtomAssetService {

	@Autowired
	private SumButtomAssetDao sumButtomAssetDao;

	@API(desc = "查询底层估值明细表（调整前）信息", auth = APIAuth.YES)
	public SqlResult<SumButtomAsset> findSumButtomAssets(SqlParam<SumButtomAsset> params) throws Exception {
		params.setMakeSql(true);
		return sumButtomAssetDao.findSumButtomAssets(params);
	}

	@API(desc = "添加底层估值明细表（调整前）", params = "id,comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code,i_name_c1,i_name_c2,a_type,m_type,asset_code,org_level,net_value,zz_report_type,g06_type,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,exchange_rate,pen_inv_f,inv_val_rate_pen,per_pen_inv_f,inv_val_rate_per_pen,inv_val_rate,non_grt_rate,non_grt_amt,trade_place,is_public,manage_mode,mark,data_insr_dt,deal_date", auth = APIAuth.NO)
	public int addSumButtomAsset(SqlParam<SumButtomAsset> params) throws Exception {
		return sumButtomAssetDao.addSumButtomAsset(params).getEffect();
	}
	
	@API(desc = "修改底层估值明细表（调整前）", params = "id,comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code,i_name_c1,i_name_c2,a_type,m_type,asset_code,org_level,net_value,zz_report_type,g06_type,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,exchange_rate,pen_inv_f,inv_val_rate_pen,per_pen_inv_f,inv_val_rate_per_pen,inv_val_rate,non_grt_rate,non_grt_amt,trade_place,is_public,manage_mode,mark,data_insr_dt,deal_date", auth = APIAuth.NO)
	public int updateSumButtomAsset(SqlParam<SumButtomAsset> params) throws Exception {
		return sumButtomAssetDao.updateSumButtomAsset(params).getEffect();
	}
	
	@API(desc = "删除底层估值明细表（调整前）", params = "id,comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code,i_name_c1,i_name_c2,a_type,m_type,asset_code,org_level,net_value,zz_report_type,g06_type,rat_level,is_ppp_part,is_mkt_bts_rlt,is_gov_fnc_part,is_fnc_stk,is_fnc_bnd,is_fnc_scd_bnd,is_fnc_tsf_bnd,is_oth_bnk_tls,is_gov_spc_bnd,exchange_rate,inv_val_rate,non_grt_rate,non_grt_amt,trade_place,is_public,manage_mode,mark,data_insr_dt,deal_date", auth = APIAuth.NO)
	public int deleteSumButtomAsset(SqlParam<SumButtomAsset> params) throws Exception {
		return sumButtomAssetDao.deleteSumButtomAsset(params).getEffect();
	}

}
