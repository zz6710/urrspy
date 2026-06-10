package com.kayak.rpt.rhzy.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzy.model.SpvInvestAmountInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class SpvInvestAmountInfoDao extends ComnDao {

	public SqlResult<SpvInvestAmountInfo> findSpvInvestAmountInfos(SqlParam<SpvInvestAmountInfo> params) throws Exception {
		String sql = "SELECT id,org_code,report_date,inner_org_code,specific_aim_type,product_code,specific_aim_code,issuer_code,issuer_arear_code,rnn_code,sub_date,end_date,trade_date,cur,trade_amount,trade_amount_rmb,trade_dire FROM app_spv_invest_amount_info WHERE 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql = sql + " and report_date like '%" + params.getModel().getReportDate() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSpecificAimType())) {
			sql = sql + " and specific_aim_type = '" + params.getModel().getSpecificAimType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSpecificAimCode())) {
			sql = sql + " and specific_aim_code = '%" + params.getModel().getSpecificAimCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTradeDire())) {
			sql = sql + " and trade_dire = '" + params.getModel().getTradeDire() + "'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addSpvInvestAmountInfo(SqlParam<SpvInvestAmountInfo> params) throws Exception {
		return super.update("INSERT INTO app_spv_invest_amount_info(id,org_code,report_date,inner_org_code,specific_aim_type,product_code,specific_aim_code,issuer_code,issuer_arear_code,rnn_code,sub_date,end_date,trade_date,cur,trade_amount,trade_amount_rmb,trade_dire) VALUES($AUTOIDI{id},$S{orgCode},$S{reportDate},$S{innerOrgCode},$S{specificAimType},$S{productCode},$S{specificAimCode},$S{issuerCode},$S{issuerArearCode},$S{rnnCode},$S{subDate},$S{endDate},$S{tradeDate},$S{cur},$D{tradeAmount},$D{tradeAmountRmb},$S{tradeDire})",
				params.getModel());
	}
	
	public UpdateResult updateSpvInvestAmountInfo(SqlParam<SpvInvestAmountInfo> params) throws Exception {
		return super.update("UPDATE app_spv_invest_amount_info SET org_code=$S{orgCode} ,report_date=$S{reportDate} ,inner_org_code=$S{innerOrgCode} ,specific_aim_type=$S{specificAimType} ,product_code=$S{productCode} ,specific_aim_code=$S{specificAimCode} ,issuer_code=$S{issuerCode} ,issuer_arear_code=$S{issuerArearCode} ,rnn_code=$S{rnnCode} ,sub_date=$S{subDate} ,end_date=$S{endDate} ,trade_date=$S{tradeDate} ,cur=$S{cur} ,trade_amount=$D{tradeAmount} ,trade_amount_rmb=$D{tradeAmountRmb} ,trade_dire=$S{tradeDire}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteSpvInvestAmountInfo(SqlParam<SpvInvestAmountInfo> params) throws Exception {
		return super.update("DELETE FROM app_spv_invest_amount_info WHERE  id=$I{id} ",
				params.getModel());
	}

	public void deleteSpvInvestAmountInfoByDate(Object params) throws Exception {
		super.update("DELETE FROM app_spv_invest_amount_Info where report_date = $S{reportDate} ", params);
	}

}
