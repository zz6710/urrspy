package com.kayak.rpt.rhzy.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzy.model.SpvInvestInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class SpvInvestInfoDao extends ComnDao {

	public SqlResult<SpvInvestInfo> findSpvInvestInfos(SqlParam<SpvInvestInfo> params) throws Exception {
		String sql = "SELECT t.id,t.org_code,t.report_date,t.inner_org_code,t.spv_type,t.amps_code,t.spv_code,t.issuer_code,t.issuer_region_code,t.run_mode,t.subscrip_date,t.expire_date,t.cur,t.invest_balance,t.invest_balance_rmb FROM app_spv_invest_info t WHERE 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql = sql + " and  t.report_date like '%" + params.getModel().getReportDate() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSpvCode())) {
			sql = sql + " and  t.spv_code like '%" + params.getModel().getSpvCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSpvType())) {
			sql = sql + " and  t.spv_type = '" + params.getModel().getSpvType() + "'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addSpvInvestInfo(SqlParam<SpvInvestInfo> params) throws Exception {
		return super.update("INSERT INTO app_spv_invest_info(id,org_code,report_date,inner_org_code,spv_type,amps_code,spv_code,issuer_code,issuer_region_code,run_mode,subscrip_date,expire_date,cur,invest_balance,invest_balance_rmb) VALUES($AUTOIDI{id},$S{orgCode},$S{reportDate},$S{innerOrgCode},$S{spvType},$S{ampsCode},$S{spvCode},$S{issuerCode},$S{issuerRegionCode},$S{runMode},$S{subscripDate},$S{expireDate},$S{cur},$D{investBalance},$D{investBalanceRmb})",
				params.getModel());
	}
	
	public UpdateResult updateSpvInvestInfo(SqlParam<SpvInvestInfo> params) throws Exception {
		return super.update("UPDATE app_spv_invest_info SET org_code=$S{orgCode} ,report_date=$S{reportDate} ,inner_org_code=$S{innerOrgCode} ,spv_type=$S{spvType} ,amps_code=$S{ampsCode} ,spv_code=$S{spvCode} ,issuer_code=$S{issuerCode} ,issuer_region_code=$S{issuerRegionCode} ,run_mode=$S{runMode} ,subscrip_date=$S{subscripDate} ,expire_date=$S{expireDate} ,cur=$S{cur} ,invest_balance=$D{investBalance} ,invest_balance_rmb=$D{investBalanceRmb}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteSpvInvestInfo(SqlParam<SpvInvestInfo> params) throws Exception {
		return super.update("DELETE FROM app_spv_invest_info WHERE  id=$I{id} ",
				params.getModel());
	}

	public void deleteSpvInvestInfoByDate(Object params) throws Exception {
		super.update("DELETE FROM app_spv_invest_info where report_date = $S{reportDate} ", params);
	}


}
