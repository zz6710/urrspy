package com.kayak.rpt.zz.manage.service;

import com.kayak.core.sql.SqlRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.AppOverseasInvInfoDao;
import com.kayak.rpt.zz.manage.model.AppOverseasInvInfo;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "境外投资情况明细表服务", model = AppOverseasInvInfo.class)
public class AppOverseasInvInfoService {

	@Autowired
	private AppOverseasInvInfoDao appOverseasInvInfoDao;

	@API(desc = "查询境外投资情况明细表信息", auth = APIAuth.NO)
	public SqlResult<AppOverseasInvInfo> findAppOverseasInvInfos(SqlParam<AppOverseasInvInfo> params) throws Exception {
		params.setMakeSql(true);
		return appOverseasInvInfoDao.findAppOverseasInvInfos(params);
	}

	public List<SqlRow> findAppOverseasInvInfos(Map<String, Object> params) throws Exception {
		return appOverseasInvInfoDao.findAppOverseasInvInfos(params);
	}

	@API(desc = "添加境外投资情况明细表", params = "report_date,num,comp_nm,fund_sour_type,fund_sour_over,prod_cd,prod_reg_enc,inv_type,prod_ccy,f_inv_tm,inv_type_f,inv_type_s,over_sove_bd,fx_type,inv_seas_f,inv_seas_s,inv_seas_t,inv_asst_f,inv_asst_s,inv_asst_t,qdii_fund,inv_asst_nm,tran_count,inv_asst_cost,inv_asst_mkt,bd_rmai_type,risk_envn,risk_pj_amt,depr_rdy_amt,depr_amt,note", auth = APIAuth.NO)
	public int addAppOverseasInvInfo(SqlParam<AppOverseasInvInfo> params) throws Exception {
		return appOverseasInvInfoDao.addAppOverseasInvInfo(params).getEffect();
	}
	
	@API(desc = "修改境外投资情况明细表", params = "report_date,num,comp_nm,fund_sour_type,fund_sour_over,prod_cd,prod_reg_enc,inv_type,prod_ccy,f_inv_tm,inv_type_f,inv_type_s,over_sove_bd,fx_type,inv_seas_f,inv_seas_s,inv_seas_t,inv_asst_f,inv_asst_s,inv_asst_t,qdii_fund,inv_asst_nm,tran_count,inv_asst_cost,inv_asst_mkt,bd_rmai_type,risk_envn,risk_pj_amt,depr_rdy_amt,depr_amt,note", auth = APIAuth.NO)
	public int updateAppOverseasInvInfo(SqlParam<AppOverseasInvInfo> params) throws Exception {
		return appOverseasInvInfoDao.updateAppOverseasInvInfo(params).getEffect();
	}
	
	@API(desc = "删除境外投资情况明细表", params = "report_date,num,comp_nm,fund_sour_type,fund_sour_over,prod_cd,prod_reg_enc,inv_type,prod_ccy,f_inv_tm,inv_type_f,inv_type_s,over_sove_bd,fx_type,inv_seas_f,inv_seas_s,inv_seas_t,inv_asst_f,inv_asst_s,inv_asst_t,qdii_fund,inv_asst_nm,tran_count,inv_asst_cost,inv_asst_mkt,bd_rmai_type,risk_envn,risk_pj_amt,depr_rdy_amt,depr_amt,note", auth = APIAuth.NO)
	public int deleteAppOverseasInvInfo(SqlParam<AppOverseasInvInfo> params) throws Exception {
		return appOverseasInvInfoDao.deleteAppOverseasInvInfo(params).getEffect();
	}

}
