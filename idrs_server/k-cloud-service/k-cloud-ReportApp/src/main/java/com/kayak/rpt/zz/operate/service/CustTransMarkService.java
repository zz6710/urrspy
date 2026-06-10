package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.TrCustTransInfo;
import com.kayak.rpt.zz.operate.model.CustVolRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.CustTransMarkDao;
import com.kayak.rpt.zz.operate.model.CustTransMark;

@Service
@APIDefine(desc = "投资者明细信息登记操作记录服务", model = CustTransMark.class)
public class CustTransMarkService {

	@Autowired
	private CustTransMarkDao custTransMarkDao;

	@API(desc = "查询投资者明细信息登记操作记录信息", auth = APIAuth.YES)
	public SqlResult<CustTransMark> findCustTransMarks(SqlParam<CustTransMark> params) throws Exception {
		params.setMakeSql(true);
		return custTransMarkDao.findCustTransMarks(params);
	}

	@API(desc = "添加投资者明细信息登记操作记录", params = "summit_user,create_date,create_time,bank_code,contract_no,trans_serno,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,op_type", auth = APIAuth.NO)
	public int addCustTransMark(SqlParam<TrCustTransInfo> params,String opType) throws Exception {
		CustTransMark custTransMark = BeanUtil.copyProperties(params.getModel(), CustTransMark.class);
		custTransMark.setOpType(opType);
		custTransMark.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return custTransMarkDao.addCustTransMark(custTransMark).getEffect();
	}
	
	@API(desc = "修改投资者明细信息登记操作记录", params = "summit_user,create_date,create_time,bank_code,contract_no,trans_serno,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,op_type", auth = APIAuth.NO)
	public int updateCustTransMark(SqlParam<CustTransMark> params) throws Exception {
		return custTransMarkDao.updateCustTransMark(params).getEffect();
	}
	
	@API(desc = "删除投资者明细信息登记操作记录", params = "summit_user,create_date,create_time,bank_code,contract_no,trans_serno,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,op_type", auth = APIAuth.NO)
	public int deleteCustTransMark(SqlParam<CustTransMark> params) throws Exception {
		return custTransMarkDao.deleteCustTransMark(params).getEffect();
	}

	public void addImportCustTrans(TrCustTransInfo custTransInfo, String opType) throws Exception {
		CustTransMark custTransMark = BeanUtil.copyProperties(custTransInfo, CustTransMark.class);
		custTransMark.setOpType(opType);
		custTransMark.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		custTransMarkDao.addImportCustTransMark(custTransMark).getEffect();
	}
}
