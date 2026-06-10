package com.kayak.rpt.rhzy.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzy.model.InterbankDepositInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class InterbankDepositInfoDao extends ComnDao {
	public SqlResult<InterbankDepositInfo> findInterbankDepositInfos(SqlParam<InterbankDepositInfo> params) throws Exception {
		String sql = "SELECT t.id,t.org_code,t.report_date,t.inner_org_code,t.busi_type,t.cntr_id_type,t.cntr_code,t.deposit_acco_code,t.deposit_protocol_code,t.protocol_start_date,t.protocol_end_date,t.cur,t.deposit_balance,t.deposit_balance_rmb,t.rate_level,t.deposit_type FROM app_interbank_deposit_info t WHERE 1=1 ";
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
		return super.findRows(sql, params);
	}
	public UpdateResult addInterbankDepositInfo(SqlParam<InterbankDepositInfo> params) throws Exception {
		return super.update("INSERT INTO app_interbank_deposit_info(org_code,inner_org_code,busi_type,cntr_id_type,cntr_code,deposit_acco_code,deposit_protocol_code,protocol_start_date,protocol_end_date,cur,deposit_balance,deposit_balance_rmb,rate_level,deposit_type) VALUES($S{orgCode},$S{innerOrgCode},$S{busiType},$S{cntrIdType},$S{cntrCode},$S{depositAccoCode},$S{depositProtocolCode},$S{protocolStartDate},$S{protocolEndDate},${cur},$S{depositBalance},$S{depositBalanceRmb},$S{rateLevel},$S{depositType})",
				params.getModel());
	}
	
	public UpdateResult updateInterbankDepositInfo(SqlParam<InterbankDepositInfo> params) throws Exception {
		return super.update("UPDATE app_interbank_deposit_info SET org_code=$S{orgCode} ,inner_org_code=$S{innerOrgCode} ,busi_type=$S{busiType} ,cntr_id_type=$S{cntrIdType} ,cntr_code=$S{cntrCode} ,deposit_acco_code=$S{depositAccoCode} ,deposit_protocol_code=$S{depositProtocolCode} ,protocol_start_date=$S{protocolStartDate} ,protocol_end_date=$S{protocolEndDate} ,cur=$S{cur} ,deposit_balance=$S{depositBalance} ,deposit_balance_rmb=$S{depositBalanceRmb} ,rate_level=$S{rateLevel} ,deposit_type=$S{depositType}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteInterbankDepositInfo(SqlParam<InterbankDepositInfo> params) throws Exception {
		return super.update("DELETE FROM app_interbank_deposit_info WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteInterbankDepositInfoByDate(Object params) throws Exception {

		return super.update("DELETE FROM app_interbank_deposit_info where report_date = $S{reportDate} ", params);
	}

}
