package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.TrTerminationRegistInfo;
import com.kayak.rpt.zz.operate.model.SubseqSubscrRegist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.TerminationRegistDao;
import com.kayak.rpt.zz.operate.model.TerminationRegist;

@Service
@APIDefine(desc = "产品终止登记操作记录服务", model = TerminationRegist.class)
public class TerminationRegistService {

	@Autowired
	private TerminationRegistDao terminationRegistDao;



	@API(desc = "查询产品终止登记操作记录信息", auth = APIAuth.YES)
	public SqlResult<TerminationRegist> findTerminationRegists(SqlParam<TerminationRegist> params) throws Exception {
		params.setMakeSql(true);
		return terminationRegistDao.findTerminationRegists(params);
	}

	@API(desc = "添加产品终止登记操作记录", params = "prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addTerminationRegist(SqlParam<TrTerminationRegistInfo> params,String opType) throws Exception {
		TerminationRegist terminationRegist = BeanUtil.copyProperties(params.getModel(), TerminationRegist.class);
		terminationRegist.setOpType(opType);
		terminationRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return terminationRegistDao.addTerminationRegist(terminationRegist).getEffect();
	}
	
	@API(desc = "修改产品终止登记操作记录", params = "prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int updateTerminationRegist(SqlParam<TerminationRegist> params) throws Exception {
		return terminationRegistDao.updateTerminationRegist(params).getEffect();
	}
	
	@API(desc = "删除产品终止登记操作记录", params = "prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int deleteTerminationRegist(SqlParam<TerminationRegist> params) throws Exception {
		return terminationRegistDao.deleteTerminationRegist(params).getEffect();
	}

    public void addImportTrTerminationRegist(TrTerminationRegistInfo terminationRegistInfo, String opType) throws Exception {
		TerminationRegist terminationRegist = BeanUtil.copyProperties(terminationRegistInfo, TerminationRegist.class);
		terminationRegist.setOpType(opType);
		terminationRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		terminationRegistDao.addImportTerminationRegist(terminationRegist).getEffect();
    }
}
