package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.TrCustVolRegisterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.CustVolRegisterDao;
import com.kayak.rpt.zz.operate.model.CustVolRegister;

@Service
@APIDefine(desc = "投资者持有信息操作记录服务", model = CustVolRegister.class)
public class CustVolRegisterService {

	@Autowired
	private CustVolRegisterDao custVolRegisterDao;

	@API(desc = "查询投资者持有信息操作记录信息", auth = APIAuth.YES)
	public SqlResult<CustVolRegister> findCustVolRegisters(SqlParam<CustVolRegister> params) throws Exception {
		params.setMakeSql(true);
		return custVolRegisterDao.findCustVolRegisters(params);
	}

	@API(desc = "添加投资者持有信息操作记录", params = "summit_user,create_date,create_time,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id,op_type", auth = APIAuth.NO)
	public int addCustVolRegister(SqlParam<TrCustVolRegisterInfo> params,String opType) throws Exception {
		CustVolRegister custVolRegister = BeanUtil.copyProperties(params.getModel(), CustVolRegister.class);
		custVolRegister.setOpType(opType);
		custVolRegister.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return custVolRegisterDao.addCustVolRegister(custVolRegister).getEffect();
	}
	
	@API(desc = "修改投资者持有信息操作记录", params = "summit_user,create_date,create_time,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id,op_type", auth = APIAuth.NO)
	public int updateCustVolRegister(SqlParam<CustVolRegister> params) throws Exception {
		return custVolRegisterDao.updateCustVolRegister(params).getEffect();
	}
	
	@API(desc = "删除投资者持有信息操作记录", params = "summit_user,create_date,create_time,bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id,op_type", auth = APIAuth.NO)
	public int deleteCustVolRegister(SqlParam<CustVolRegister> params) throws Exception {
		return custVolRegisterDao.deleteCustVolRegister(params).getEffect();
	}

	public int addImportCustVolRegister(TrCustVolRegisterInfo custVolRegisterInfo, String opType) throws Exception {
		CustVolRegister custVolRegister = BeanUtil.copyProperties(custVolRegisterInfo, CustVolRegister.class);
		custVolRegister.setOpType(opType);
		custVolRegister.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return custVolRegisterDao.addImportCustVolRegister(custVolRegister).getEffect();

	}
}
