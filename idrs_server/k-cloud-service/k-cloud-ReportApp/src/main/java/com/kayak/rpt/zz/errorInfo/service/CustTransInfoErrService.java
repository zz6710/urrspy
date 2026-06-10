package com.kayak.rpt.zz.errorInfo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.CustTransInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.CustTransInfoErr;

@Service
@APIDefine(desc = "投资者明细错误信息服务", model = CustTransInfoErr.class)
public class CustTransInfoErrService {

	@Autowired
	private CustTransInfoErrDao custTransInfoErrDao;

	@API(desc = "查询投资者明细错误信息信息", auth = APIAuth.YES)
	public SqlResult<CustTransInfoErr> findCustTransInfos(SqlParam<CustTransInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return custTransInfoErrDao.findCustTransInfos(params);
	}

	@API(desc = "添加投资者明细错误信息", params = "imp_date,register_serno,bank_code_desc,trans_serno_desc,contract_no_desc,fnc_trans_acct_no_desc,host_cust_no_desc,cust_no_desc,cust_name_desc,acct_no_desc,acct_loc_code_desc,is_agent_desc,agent_bank_code_desc,agent_bank_name_desc,agent_regu_code_desc,prod_code_desc,busi_code_desc,busi_regu_code_desc,ack_date_desc,ack_time_desc,cur_desc,ack_amt_desc,convert_rmb_desc,nav_desc,ack_vol_desc,fee_amt_desc,channel_flag_desc,inputuser_desc,remark_desc,register_serno_desc", auth = APIAuth.NO)
	public int addCustTransInfo(SqlParam<CustTransInfoErr> params) throws Exception {
		return custTransInfoErrDao.addCustTransInfo(params).getEffect();
	}
	
	@API(desc = "修改投资者明细错误信息", params = "imp_date,register_serno,bank_code_desc,trans_serno_desc,contract_no_desc,fnc_trans_acct_no_desc,host_cust_no_desc,cust_no_desc,cust_name_desc,acct_no_desc,acct_loc_code_desc,is_agent_desc,agent_bank_code_desc,agent_bank_name_desc,agent_regu_code_desc,prod_code_desc,busi_code_desc,busi_regu_code_desc,ack_date_desc,ack_time_desc,cur_desc,ack_amt_desc,convert_rmb_desc,nav_desc,ack_vol_desc,fee_amt_desc,channel_flag_desc,inputuser_desc,remark_desc,register_serno_desc", auth = APIAuth.NO)
	public int updateCustTransInfo(SqlParam<CustTransInfoErr> params) throws Exception {
		return custTransInfoErrDao.updateCustTransInfo(params).getEffect();
	}
	
	@API(desc = "删除投资者明细错误信息", params = "imp_date,register_serno,bank_code_desc,trans_serno_desc,contract_no_desc,fnc_trans_acct_no_desc,host_cust_no_desc,cust_no_desc,cust_name_desc,acct_no_desc,acct_loc_code_desc,is_agent_desc,agent_bank_code_desc,agent_bank_name_desc,agent_regu_code_desc,prod_code_desc,busi_code_desc,busi_regu_code_desc,ack_date_desc,ack_time_desc,cur_desc,ack_amt_desc,convert_rmb_desc,nav_desc,ack_vol_desc,fee_amt_desc,channel_flag_desc,inputuser_desc,remark_desc,register_serno_desc", auth = APIAuth.NO)
	public int deleteCustTransInfo(SqlParam<CustTransInfoErr> params) throws Exception {
		return custTransInfoErrDao.deleteCustTransInfo(params).getEffect();
	}

}
