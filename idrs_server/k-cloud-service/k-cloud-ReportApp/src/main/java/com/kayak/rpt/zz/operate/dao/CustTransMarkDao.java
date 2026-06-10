package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.CustTransMark;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class CustTransMarkDao extends ComnDao {

	public SqlResult<CustTransMark> findCustTransMarks(SqlParam<CustTransMark> params) throws Exception {
		String sql = "SELECT id, SUMMIT_USER, CREATE_DATE, CREATE_TIME, BANK_CODE, CONTRACT_NO, TRANS_SERNO, FNC_TRANS_ACCT_NO, HOST_CUST_NO, CUST_NO, CUST_NAME, DEAL_NO, ACCT_NO, ACCT_BANK_NO, ACCT_BANK_NAME, ACCT_LOC_CODE, IS_AGENT, AGENT_BANK_CODE, AGENT_BANK_NAME, AGENT_REGU_CODE, PROD_CODE, BUSI_CODE, BUSI_REGU_CODE, ACK_DATE, ACK_TIME, CUR, ACK_AMT, CONVERT_RMB, NAV, ACK_VOL, FEE_AMT, CHANNEL_FLAG, INPUTUSER, REMARK, SON_SHARE_CODE,SPE_CHANNEL_FLAG, REGISTER_SERNO, IMP_DATE, REGISTER_DATE, REGISTER_STATUS, OP_TYPE FROM app_cust_trans_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addCustTransMark(CustTransMark custTransMark) throws Exception {
		return super.update("INSERT INTO app_cust_trans_remark(deal_No,acct_Bank_No,acct_Bank_Name,summit_user,create_date,create_time,bank_code,contract_no,trans_serno,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,op_type,report_date) VALUES($S{dealNo},$S{acctBankNo},$S{acctBankName},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{bankCode},$S{contractNo},$S{transSerno},$S{fncTransAcctNo},$S{hostCustNo},$S{custNo},$S{custName},$S{acctNo},$S{acctLocCode},$S{isAgent},$S{agentBankCode},$S{agentBankName},$S{agentReguCode},$S{prodCode},$S{busiCode},$S{busiReguCode},$S{ackDate},$S{ackTime},$S{cur},$D{ackAmt},$D{convertRmb},$D{nav},$D{ackVol},$D{feeAmt},$S{channelFlag},$S{inputuser},$S{remark},$S{sonShareCode},$S{speChannelFlag},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{opType},$S{reportDate})",
				DataSourceProperty.PUB,custTransMark);
	}
	
	public UpdateResult updateCustTransMark(SqlParam<CustTransMark> params) throws Exception {
		return super.update("UPDATE app_cust_trans_remark SET summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,bank_code=$S{bankCode} ,contract_no=$S{contractNo} ,trans_serno=$S{transSerno} ,fnc_trans_acct_no=$S{fncTransAcctNo} ,host_cust_no=$S{hostCustNo} ,cust_no=$S{custNo} ,cust_name=$S{custName} ,acct_no=$S{acctNo} ,acct_loc_code=$S{acctLocCode} ,is_agent=$S{isAgent} ,agent_bank_code=$S{agentBankCode} ,agent_bank_name=$S{agentBankName} ,agent_regu_code=$S{agentReguCode} ,prod_code=$S{prodCode} ,busi_code=$S{busiCode} ,busi_regu_code=$S{busiReguCode} ,ack_date=$S{ackDate} ,ack_time=$S{ackTime} ,cur=$S{cur} ,ack_amt=$D{ackAmt} ,convert_rmb=$D{convertRmb} ,nav=$D{nav} ,ack_vol=$D{ackVol} ,fee_amt=$D{feeAmt} ,channel_flag=$S{channelFlag} ,inputuser=$S{inputuser} ,remark=$S{remark} ,son_share_code=$S{sonShareCode} ,spe_channel_flag=$S{speChannelFlag} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,op_type=$S{opType}  WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteCustTransMark(SqlParam<CustTransMark> params) throws Exception {
		return super.update("DELETE FROM app_cust_trans_remark WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportCustTransMark(CustTransMark custTransMark) throws Exception {
		return super.update("INSERT INTO app_cust_trans_remark(deal_No,acct_Bank_No,acct_Bank_Name,summit_user,create_date,create_time,bank_code,contract_no,trans_serno,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,op_type,report_date) VALUES($S{dealNo},$S{acctBankNo},$S{acctBankName},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{bankCode},$S{contractNo},$S{transSerno},$S{fncTransAcctNo},$S{hostCustNo},$S{custNo},$S{custName},$S{acctNo},$S{acctLocCode},$S{isAgent},$S{agentBankCode},$S{agentBankName},$S{agentReguCode},$S{prodCode},$S{busiCode},$S{busiReguCode},$S{ackDate},$S{ackTime},$S{cur},$D{ackAmt},$D{convertRmb},$D{nav},$D{ackVol},$D{feeAmt},$S{channelFlag},$S{inputuser},$S{remark},$S{sonShareCode},$S{speChannelFlag},$S{registerSerno},date_format(CURDATE(),'%Y%m%d'),$S{registerDate},$S{registerStatus},$S{opType},$S{reportDate})",
				DataSourceProperty.PUB,custTransMark);
	}
}
