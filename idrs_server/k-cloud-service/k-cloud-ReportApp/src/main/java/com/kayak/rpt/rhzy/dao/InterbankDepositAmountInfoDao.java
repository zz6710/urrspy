package com.kayak.rpt.rhzy.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzy.model.InterbankDepositAmountInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class InterbankDepositAmountInfoDao extends ComnDao {

	public SqlResult<InterbankDepositAmountInfo> findInterbankDepositAmountInfos(SqlParam<InterbankDepositAmountInfo> params) throws Exception {
		String sql = "SELECT t.id,t.org_code,t.report_date,t.inner_org_code,t.busi_type,t.cntr_id_type,t.cntr_code,t.deposit_acco_code,t.deposit_protocol_code,t.protocol_start_date,t.protocol_end_date,t.cur,t.trade_amount,t.trade_amount_rmb,t.trade_date,t.trade_ser_no,t.rate_level,t.trade_acco_no,t.trade_acco_bank_no,t.cntr_acco_no,t.trade_dire FROM app_interbank_deposit_amount_info t WHERE 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql = sql + " and  t.report_date like '%" + params.getModel().getReportDate() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBusiType())) {
			sql = sql + " and  t.busi_type = '" + params.getModel().getBusiType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getDepositAccoCode())) {
			sql = sql + " and  t.deposit_acco_code = '" + params.getModel().getDepositAccoCode() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getDepositProtocolCode())) {
			sql = sql + " and  t.deposit_protocol_code = '" + params.getModel().getDepositProtocolCode() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTradeSerNo())) {
			sql = sql + " and  t.trade_ser_no = '" + params.getModel().getTradeSerNo() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTradeDire())) {
			sql = sql + " and  t.trade_dire = '" + params.getModel().getTradeDire() + "'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addInterbankDepositAmountInfo(SqlParam<InterbankDepositAmountInfo> params) throws Exception {
		return super.update("INSERT INTO app_interbank_deposit_amount_info(id,org_code,report_date,inner_org_code,busi_type,cntr_id_type,cntr_code,deposit_acco_code,deposit_protocol_code,protocol_start_date,protocol_end_date,cur,trade_amount,trade_amount_rmb,trade_date,trade_ser_no,rate_level,trade_acco_no,trade_acco_bank_no,cntr_acco_no,trade_dire) VALUES($AUTOIDI{id},$S{orgCode},$S{reportDate},$S{innerOrgCode},$S{busiType},$S{cntrIdType},$S{cntrCode},$S{depositAccoCode},$S{depositProtocolCode},$S{protocolStartDate},$S{protocolEndDate},$S{cur},$D{tradeAmount},$D{tradeAmountRmb},$S{tradeDate},$S{tradeSerNo},$D{rateLevel},$S{tradeAccoNo},$S{tradeAccoBankNo},$S{cntrAccoNo},$S{tradeAccoDire})",
				params.getModel());
	}
	
	public UpdateResult updateInterbankDepositAmountInfo(SqlParam<InterbankDepositAmountInfo> params) throws Exception {
		return super.update("UPDATE app_interbank_deposit_amount_info SET org_code=$S{orgCode} ,report_date=$S{reportDate} ,inner_org_code=$S{innerOrgCode} ,busi_type=$S{busiType} ,cntr_id_type=$S{cntrIdType} ,cntr_code=$S{cntrCode} ,deposit_acco_code=$S{depositAccoCode} ,deposit_protocol_code=$S{depositProtocolCode} ,protocol_start_date=$S{protocolStartDate} ,protocol_end_date=$S{protocolEndDate} ,cur=$S{cur} ,trade_amount=$D{tradeAmount} ,trade_amount_rmb=$D{tradeAmountRmb} ,trade_date=$S{tradeDate} ,trade_ser_no=$S{tradeSerNo} ,rate_level=$D{rateLevel} ,trade_acco_no=$S{tradeAccoNo} ,trade_acco_bank_no=$S{tradeAccoBankNo} ,cntr_acco_no=$S{cntrAccoNo} ,trade_dire=$S{tradeAccoDire}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteInterbankDepositAmountInfo(SqlParam<InterbankDepositAmountInfo> params) throws Exception {
		return super.update("DELETE FROM app_interbank_deposit_amount_info WHERE  id=$S{id} ",
				params.getModel());
	}

	public void deleteInterbankDepositAmountInfoByDate(Object params) throws Exception {
		super.update("DELETE FROM app_interbank_deposit_amount_info where report_date = $S{reportDate} ", params);
	}

}
