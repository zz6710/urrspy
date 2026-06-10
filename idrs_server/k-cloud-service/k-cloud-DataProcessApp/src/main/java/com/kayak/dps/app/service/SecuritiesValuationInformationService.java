package com.kayak.dps.app.service;


import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.SecuritiesValuationInformationDao;
import com.kayak.dps.app.model.ChinaDebtValuation;
import com.kayak.dps.app.model.SecuritiesValuationInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;


@Service
@APIDefine(desc = "中证估值服务", model = SecuritiesValuationInformation.class)
public class SecuritiesValuationInformationService {
	private static final int ONE = 1;
	private static final int ZERO = 0;
	@Autowired
	private SecuritiesValuationInformationDao securitiesValuationInformationDao;

	@API(desc = "查询中证估值信息", auth = APIAuth.YES)
	public SqlResult<SecuritiesValuationInformation> findSecuritiesValuationInformations(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		params.setMakeSql(true);
		return securitiesValuationInformationDao.findSecuritiesValuationInformations(params);
	}

	@API(desc = "添加中证估值", params = "scr_id,scr_cd,trx_mkt,trx_dt,crd_typ,isu_mkt_qty,calc_prc,calc_ern_rat,modi_dura,cvxt,net_prc,acr_intr,crt_dt,upd_dt", auth = APIAuth.YES)
	public String addSecuritiesValuationInformation(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		boolean b = checkSecuritiesValuationInformation(params, ZERO);
		if (b) {
			return RequestSupport.updateReturnJson(false,  "添加失败,有相同中证估值信息！", null).toString();
		} else {
			securitiesValuationInformationDao.addSecuritiesValuationInformation(params);
			return RequestSupport.updateReturnJson(true,  "添加成功！", null).toString();
		}

	}
	
	@API(desc = "修改中证估值", params = "scr_id,scr_cd,trx_mkt,trx_dt,crd_typ,isu_mkt_qty,calc_prc,calc_ern_rat,modi_dura,cvxt,net_prc,acr_intr,crt_dt,upd_dt", auth = APIAuth.YES)
	public String updateSecuritiesValuationInformation(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		boolean b = checkSecuritiesValuationInformation(params, ONE);
		if (b) {
			return RequestSupport.updateReturnJson(false,  "修改失败,有相同中证估值信息！", null).toString();
		} else {
			securitiesValuationInformationDao.updateSecuritiesValuationInformation(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		}
	}
	
	@API(desc = "删除中证估值", params = "scr_id,scr_cd,trx_mkt,trx_dt,crd_typ,isu_mkt_qty,calc_prc,calc_ern_rat,modi_dura,cvxt,net_prc,acr_intr,crt_dt,upd_dt", auth = APIAuth.YES)
	public int deleteSecuritiesValuationInformation(SqlParam<SecuritiesValuationInformation> params) throws Exception {
		return securitiesValuationInformationDao.deleteSecuritiesValuationInformation(params).getEffect();
	}

	@API(desc = "查询债券信息代码与名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<SecuritiesValuationInformation> findSecuritiesValuationInformationsCdAndNm(SqlParam<SecuritiesValuationInformation> params) throws Exception {
//        params.setMakeSql(true);
		return securitiesValuationInformationDao.findBondInfoCdAndNm(params);
	}
	// 查询能否新增或修改
	private boolean checkSecuritiesValuationInformation(SqlParam<SecuritiesValuationInformation> params, int i) throws Exception {
		SqlRow sqlRow  = securitiesValuationInformationDao.findCounts(params);
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
