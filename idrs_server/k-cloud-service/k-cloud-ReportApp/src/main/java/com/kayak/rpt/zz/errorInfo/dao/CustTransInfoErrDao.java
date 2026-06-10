package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.CustTransInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class CustTransInfoErrDao extends ComnDao {

	public SqlResult<CustTransInfoErr> findCustTransInfos(SqlParam<CustTransInfoErr> params) throws Exception{
		String sql = "SELECT IMP_DATE, REGISTER_SERNO, BANK_CODE_DESC, TRANS_SERNO_DESC, CONTRACT_NO_DESC, FNC_TRANS_ACCT_NO_DESC, HOST_CUST_NO_DESC, CUST_NO_DESC, CUST_NAME_DESC, DEAL_NO_DESC, ACCT_NO_DESC, ACCT_BANK_NO_DESC, ACCT_BANK_NAME_DESC, ACCT_LOC_CODE_DESC, IS_AGENT_DESC, AGENT_BANK_CODE_DESC, AGENT_BANK_NAME_DESC, AGENT_REGU_CODE_DESC, PROD_CODE_DESC, BUSI_CODE_DESC, BUSI_REGU_CODE_DESC, ACK_DATE_DESC, ACK_TIME_DESC, CUR_DESC, ACK_AMT_DESC, CONVERT_RMB_DESC, NAV_DESC, ACK_VOL_DESC, FEE_AMT_DESC, CHANNEL_FLAG_DESC, INPUTUSER_DESC, REMARK_DESC, REGISTER_SERNO_DESC, create_date, theory_report_start_date, theory_report_end_date, ID, report_date\n" +
				"FROM app_cust_trans_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addCustTransInfo(SqlParam<CustTransInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_cust_trans_info_erdesc(imp_date,register_serno,bank_code_desc,trans_serno_desc,contract_no_desc,fnc_trans_acct_no_desc,host_cust_no_desc,cust_no_desc,cust_name_desc,acct_no_desc,acct_loc_code_desc,is_agent_desc,agent_bank_code_desc,agent_bank_name_desc,agent_regu_code_desc,prod_code_desc,busi_code_desc,busi_regu_code_desc,ack_date_desc,ack_time_desc,cur_desc,ack_amt_desc,convert_rmb_desc,nav_desc,ack_vol_desc,fee_amt_desc,channel_flag_desc,inputuser_desc,remark_desc,register_serno_desc,deal_no_desc,acct_bank_no_desc,acct_bank_name_desc) VALUES($S{impDate},$S{registerSerno},$S{bankCodeDesc},$S{transSernoDesc},$S{contractNoDesc},$S{fncTransAcctNoDesc},$S{hostCustNoDesc},$S{custNoDesc},$S{custNameDesc},$S{acctNoDesc},$S{acctLocCodeDesc},$S{isAgentDesc},$S{agentBankCodeDesc},$S{agentBankNameDesc},$S{agentReguCodeDesc},$S{prodCodeDesc},$S{busiCodeDesc},$S{busiReguCodeDesc},$S{ackDateDesc},$S{ackTimeDesc},$S{curDesc},$S{ackAmtDesc},$S{convertRmbDesc},$S{navDesc},$S{ackVolDesc},$S{feeAmtDesc},$S{channelFlagDesc},$S{inputuserDesc},$S{remarkDesc},$S{registerSernoDesc},$S{deslNoDesc},$S{acctBankNoDesc},$S{acctBankNameDesc})",
				params.getModel());
	}
	
	public UpdateResult updateCustTransInfo(SqlParam<CustTransInfoErr> params) throws Exception {
		return super.update("UPDATE app_cust_trans_info_erdesc SET imp_date=$S{impDate} ,register_serno=$S{registerSerno} ,bank_code_desc=$S{bankCodeDesc} ,trans_serno_desc=$S{transSernoDesc} ,contract_no_desc=$S{contractNoDesc} ,fnc_trans_acct_no_desc=$S{fncTransAcctNoDesc} ,host_cust_no_desc=$S{hostCustNoDesc} ,cust_no_desc=$S{custNoDesc} ,cust_name_desc=$S{custNameDesc} ,acct_no_desc=$S{acctNoDesc} ,acct_loc_code_desc=$S{acctLocCodeDesc} ,is_agent_desc=$S{isAgentDesc} ,agent_bank_code_desc=$S{agentBankCodeDesc} ,agent_bank_name_desc=$S{agentBankNameDesc} ,agent_regu_code_desc=$S{agentReguCodeDesc} ,prod_code_desc=$S{prodCodeDesc} ,busi_code_desc=$S{busiCodeDesc} ,busi_regu_code_desc=$S{busiReguCodeDesc} ,ack_date_desc=$S{ackDateDesc} ,ack_time_desc=$S{ackTimeDesc} ,cur_desc=$S{curDesc} ,ack_amt_desc=$S{ackAmtDesc} ,convert_rmb_desc=$S{convertRmbDesc} ,nav_desc=$S{navDesc} ,ack_vol_desc=$S{ackVolDesc} ,fee_amt_desc=$S{feeAmtDesc} ,channel_flag_desc=$S{channelFlagDesc} ,inputuser_desc=$S{inputuserDesc} ,remark_desc=$S{remarkDesc} ,register_serno_desc=$S{registerSernoDesc},deal_no_desc=$S{deslNoDesc},acct_bank_no_desc=$S{acctBankNoDesc},acct_bank_name_desc=$S{acctBankNameDesc}  WHERE register_serno_desc=$S{registerSernoDesc}",
				params.getModel());
	}
	
	public UpdateResult deleteCustTransInfo(SqlParam<CustTransInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_cust_trans_info_erdesc WHERE ",
				params.getModel());
	}

}
