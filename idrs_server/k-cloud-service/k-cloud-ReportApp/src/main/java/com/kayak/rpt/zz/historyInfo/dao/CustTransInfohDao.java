package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.zz.historyInfo.model.CustTransInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class CustTransInfohDao extends ComnDao {

	public SqlResult<CustTransInfoh> findCustTransInfohs(SqlParam<CustTransInfoh> params) throws Exception {
		String sql = "SELECT id, BANK_CODE, TRANS_SERNO, CONTRACT_NO, FNC_TRANS_ACCT_NO, HOST_CUST_NO, CUST_NO, CUST_NAME, deal_No, " +
				     "       ACCT_NO, acct_Bank_No, acct_Bank_NAME, ACCT_LOC_CODE, IS_AGENT, AGENT_BANK_CODE, AGENT_BANK_NAME, AGENT_REGU_CODE, " +
				     "       PROD_CODE, BUSI_CODE, BUSI_REGU_CODE, ACK_DATE, ACK_TIME, CUR, ACK_AMT, CONVERT_RMB, NAV, ACK_VOL, FEE_AMT, CHANNEL_FLAG, " +
				     "       INPUTUSER, REMARK, SON_SHARE_CODE, SPE_CHANNEL_FLAG, register_serno, IMP_DATE, REGISTER_DATE, REGISTER_STATUS, create_date, theory_report_start_date, " +
				     "       theory_report_end_date, report_date, cust_name as cust_name_display, acct_no as acct_no_display " +
				     "  FROM app_cust_trans_info_h " +
				     " where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getReportDate())) {
			sql += " and report_date = $S{reportDate}";
		}
		if (Strings.isNotBlank(params.getModel().getHostCustNo())) {
			sql += " and HOST_CUST_NO like '%" + params.getModel().getHostCustNo() + "%'";
		}
		if (Strings.isNotBlank(params.getModel().getCustNo())) {
			sql += " and CUST_NO like '%" + params.getModel().getCustNo() + "%'";
		}
		if (Strings.isNotBlank(params.getModel().getBusiCode())) {
			sql += " and BUSI_CODE in (" + SysUtil.inStr(params.getModel().getBusiCode()) + ") ";
		}
		if (Strings.isNotBlank(params.getModel().getProdCode())) {
			sql += " and PROD_CODE like '%" + params.getModel().getProdCode() + "%'";
		}
		if (Strings.isNotBlank(params.getModel().getAgentBankCode())) {
			sql += " and AGENT_BANK_CODE like '%" + params.getModel().getAgentBankCode() + "%'";
		}
		if (Strings.isNotBlank(params.getModel().getAgentBankName())) {
			sql += " and AGENT_BANK_NAME like '%" + params.getModel().getAgentBankName() + "%'";
		}
		return super.findRows(sql, params);
	}

	public SqlResult<CustTransInfoh> findCustTransRemark(SqlParam<CustTransInfoh> params) throws Exception {
		String sql = "SELECT id, BANK_CODE, TRANS_SERNO, CONTRACT_NO, FNC_TRANS_ACCT_NO, HOST_CUST_NO, CUST_NO, CUST_NAME, deal_No, " +
				"       ACCT_NO, acct_Bank_No, acct_Bank_NAME, ACCT_LOC_CODE, IS_AGENT, AGENT_BANK_CODE, AGENT_BANK_NAME, AGENT_REGU_CODE, " +
				"       PROD_CODE, BUSI_CODE, BUSI_REGU_CODE, ACK_DATE, ACK_TIME, CUR, ACK_AMT, CONVERT_RMB, NAV, ACK_VOL, FEE_AMT, CHANNEL_FLAG, " +
				"       INPUTUSER, REMARK, SON_SHARE_CODE, SPE_CHANNEL_FLAG, register_serno, IMP_DATE, REGISTER_DATE, REGISTER_STATUS, create_date, theory_report_start_date, " +
				"       theory_report_end_date, report_date, cust_name as cust_name_display, acct_no as acct_no_display " +
				"  FROM app_cust_trans_info_record " +
				" where order_id = '" + params.getParamsDirect().get("order_id") + "' and MRG_TYP = '" + params.getParamsDirect().get("mrg_typ") + "' ";
		sql += " order by ACK_DATE desc, cust_no, prod_code " ;
		return super.findRows(sql, params);
	}

	public UpdateResult addCustTransInfoh(SqlParam<CustTransInfoh> params) throws Exception {
		return super.update("INSERT INTO app_cust_trans_info_h(bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status) VALUES($S{bankCode},$S{transSerno},$S{contractNo},$S{fncTransAcctNo},$S{hostCustNo},$S{custNo},$S{custName},$S{acctNo},$S{acctLocCode},$S{isAgent},$S{agentBankCode},$S{agentBankName},$S{agentReguCode},$S{prodCode},$S{busiCode},$S{busiReguCode},$S{ackDate},$S{ackTime},$S{cur},$D{ackAmt},$S{convertRmb},$S{nav},$S{ackVol},$S{feeAmt},$S{channelFlag},$S{inputuser},$S{remark},$S{sonShareCode},$S{speChannelFlag},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus})",
				params.getModel());
	}
	
	public UpdateResult updateCustTransInfoh(SqlParam<CustTransInfoh> params) throws Exception {
		return super.update("UPDATE app_cust_trans_info_h " +
						"           SET bank_code=$S{bankCode} ,trans_serno=$S{transSerno} ,contract_no=$S{contractNo} ,fnc_trans_acct_no=$S{fncTransAcctNo} ," +
						"               host_cust_no=$S{hostCustNo} ,cust_no=$S{custNo} ,cust_name=$S{custNameDisplay} ,acct_no=$S{acctNoDisplay} ," +
						"               acct_loc_code=$S{acctLocCode} ,is_agent=$S{isAgent} ,agent_bank_code=$S{agentBankCode} ,agent_bank_name=$S{agentBankName} ," +
						"               agent_regu_code=$S{agentReguCode} ,prod_code=$S{prodCode} ,busi_code=$S{busiCode} ,busi_regu_code=$S{busiReguCode} ," +
						"               ack_date=$S{ackDate} ,ack_time=$S{ackTime} ,cur=$S{cur} ,ack_amt=$D{ackAmt} ,convert_rmb=$S{convertRmb} ,nav=$S{nav} ," +
						"               ack_vol=$S{ackVol} ,fee_amt=$S{feeAmt} ,channel_flag=$S{channelFlag} ,inputuser=$S{inputuser} ,remark=$S{remark} ,son_share_code=$S{sonShareCode}, spe_channel_flag=$S{speChannelFlag} ," +
						"               register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ," +
						"               report_date = $S{reportDate} " +
						"         WHERE id = $S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteCustTransInfoh(SqlParam<CustTransInfoh> params) throws Exception {
		return super.update("DELETE FROM app_cust_trans_info_h WHERE ",
				params.getModel());
	}

}
