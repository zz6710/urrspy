package com.kayak.dps.app.service;


import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.ChinaDebtValuationDao;
import com.kayak.dps.app.model.BondInfoModel;
import com.kayak.dps.app.model.ChinaDebtValuation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;


@Service
@APIDefine(desc = "ChinaDebtValuation服务", model = ChinaDebtValuation.class)
public class ChinaDebtValuationService {

	private static final int ONE = 1;
	private static final int ZERO = 0;
	@Autowired
	private ChinaDebtValuationDao chinaDebtValuationDao;

	@API(desc = "查询中债登估值信息", auth = APIAuth.YES)
	public SqlResult<ChinaDebtValuation> findChinaDebtValuations(SqlParam<ChinaDebtValuation> params) throws Exception {
		params.setMakeSql(true);
		return chinaDebtValuationDao.findChinaDebtValuations(params);
	}

	@API(desc = "添加中债登估值信息", params = "scr_id,scr_cd,trx_mkt,trx_dt,cps_prd,eval_full_prc,acr_intr,eval_net_prc,eval_ern_rat,eval_modi_dura,eval_cvxt,eval_bas_pnt_val,eval_sprd_dura,eval_sprd_cvxt,cls_acr_intr,mkt_full_prc,mkt_net_prc,mkt_ern_rat,mkt_modi_dura,mkt_cvxt,mkt_bas_pnt_val,mkt_sprd_dura,mkt_sprd_cvxt,eval_rat_dura,eval_rat_cvxt,mkt_rat_dura,mkt_rat_cvxt,cls_eval_full_prc,sprd_ern_rat,crt_dt,upd_dt", auth = APIAuth.YES)
	public String addChinaDebtValuation(SqlParam<ChinaDebtValuation> params) throws Exception {
		boolean b = checkChinaDebtValuation(params,ZERO);
		if (b) {
			return RequestSupport.updateReturnJson(false,  "添加失败,有相同中债登估值信息！", null).toString();
		} else {
			chinaDebtValuationDao.addChinaDebtValuation(params);
			return RequestSupport.updateReturnJson(true,  "添加成功！", null).toString();
		}

	}



	@API(desc = "修改中债登估值信息", params = "scr_id,scr_cd,trx_mkt,trx_dt,cps_prd,eval_full_prc,acr_intr,eval_net_prc,eval_ern_rat,eval_modi_dura,eval_cvxt,eval_bas_pnt_val,eval_sprd_dura,eval_sprd_cvxt,cls_acr_intr,mkt_full_prc,mkt_net_prc,mkt_ern_rat,mkt_modi_dura,mkt_cvxt,mkt_bas_pnt_val,mkt_sprd_dura,mkt_sprd_cvxt,eval_rat_dura,eval_rat_cvxt,mkt_rat_dura,mkt_rat_cvxt,cls_eval_full_prc,sprd_ern_rat,crt_dt,upd_dt", auth = APIAuth.YES)
	public String updateChinaDebtValuation(SqlParam<ChinaDebtValuation> params) throws Exception {
		boolean b = checkChinaDebtValuation(params,ONE);
		if (b) {
			return RequestSupport.updateReturnJson(false,  "修改失败,有相同中债登估值信息！", null).toString();
		} else {
			chinaDebtValuationDao.updateChinaDebtValuation(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		}
	}
	
	@API(desc = "删除中债登估值信息", params = "scr_id,scr_cd,trx_mkt,trx_dt,cps_prd,eval_full_prc,acr_intr,eval_net_prc,eval_ern_rat,eval_modi_dura,eval_cvxt,eval_bas_pnt_val,eval_sprd_dura,eval_sprd_cvxt,cls_acr_intr,mkt_full_prc,mkt_net_prc,mkt_ern_rat,mkt_modi_dura,mkt_cvxt,mkt_bas_pnt_val,mkt_sprd_dura,mkt_sprd_cvxt,eval_rat_dura,eval_rat_cvxt,mkt_rat_dura,mkt_rat_cvxt,cls_eval_full_prc,sprd_ern_rat,crt_dt,upd_dt", auth = APIAuth.YES)
	public int deleteChinaDebtValuation(SqlParam<ChinaDebtValuation> params) throws Exception {
		return chinaDebtValuationDao.deleteChinaDebtValuation(params).getEffect();
	}

	@API(desc = "查询债券信息代码与名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<ChinaDebtValuation> findBondInfoModelsCdAndNm(SqlParam<ChinaDebtValuation> params) throws Exception {
//        params.setMakeSql(true);
		return chinaDebtValuationDao.findBondInfoCdAndNm(params);
	}
	// 查询能否新增或修改
	private boolean checkChinaDebtValuation(SqlParam<ChinaDebtValuation> params, int i) throws Exception {
		SqlRow sqlRow  = chinaDebtValuationDao.findCounts(params);
		if (i == ONE) {
			// 如果是更新 查询数量-1
			long con = (long) sqlRow.get("con");
			return con - ONE > ZERO;
		} else {
			long con = (long) sqlRow.get("con");
			return con > ZERO;
		}
	}

}
