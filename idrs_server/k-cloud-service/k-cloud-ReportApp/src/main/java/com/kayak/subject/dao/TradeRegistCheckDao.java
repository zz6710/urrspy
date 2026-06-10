package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.TradeRegistCheck;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class TradeRegistCheckDao extends ComnDao {

	public SqlResult<TradeRegistCheck> findTradeRegistChecks(SqlParam<TradeRegistCheck> params) throws Exception {
		String sql = "SELECT id,prod_code,prod_reg_enc,invest_type,bottom_code,trade_invamount,fa_amount,pl_amount,concat(pl_rate) as pl_rate,at_amount,at_pl_amount,concat(at_pl_rate) as at_pl_rate,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,report_date " +
				"   FROM app_trade_regist_check where 1=1";
		if ("0".equals(params.getModel().getPlRate())) {
			sql += " and pl_rate = 0";
		} else if ("1".equals(params.getModel().getPlRate())) {
			sql += " and pl_rate <> 0";
		}
		if ("0".equals(params.getModel().getAtPlRate())) {
			sql += " and at_pl_rate = 0";
		} else if ("1".equals(params.getModel().getAtPlRate())) {
			sql += " and at_pl_rate <> 0";
		}
		params.getModel().setPlRate(null);
		return super.findRows(sql, params);
	}

	public UpdateResult addTradeRegistCheck(SqlParam<TradeRegistCheck> params) throws Exception {
		return super.update("INSERT INTO app_trade_regist_check(id,prod_code,prod_reg_enc,invest_type,bottom_code,trade_invamount,fa_amount,pl_amount,pl_rate,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,report_date) VALUES($AUTOIDI{id},$S{prodCode},$S{prodRegEnc},$S{investType},$S{bottomCode},$D{tradeInvamount},$D{faAmount},$D{plAmount},$D{plRate},$S{createDate},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{impDate},$S{registerStatus},$S{sysDataStatus},$S{sysDataVersion},$S{sysDataSource},$S{reportDate})",
				params.getModel());
	}
	
	public UpdateResult updateTradeRegistCheck(SqlParam<TradeRegistCheck> params) throws Exception {
		return super.update("UPDATE app_trade_regist_check SET prod_code=$S{prodCode} ,prod_reg_enc=$S{prodRegEnc} ,invest_type=$S{investType} ,bottom_code=$S{bottomCode} ,trade_invamount=$D{tradeInvamount} ,fa_amount=$D{faAmount} ,pl_amount=$D{plAmount} ,pl_rate=$D{plRate} ,create_date=$S{createDate} ,theory_report_start_date=$S{theoryReportStartDate} ,theory_report_end_date=$S{theoryReportEndDate} ,imp_date=$S{impDate} ,register_status=$S{registerStatus} ,sys_data_status=$S{sysDataStatus} ,sys_data_version=$S{sysDataVersion} ,sys_data_source=$S{sysDataSource} ,report_date=$S{reportDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteTradeRegistCheck(SqlParam<TradeRegistCheck> params) throws Exception {
		return super.update("DELETE FROM app_trade_regist_check WHERE  id=$I{id} ",
				params.getModel());
	}

}
