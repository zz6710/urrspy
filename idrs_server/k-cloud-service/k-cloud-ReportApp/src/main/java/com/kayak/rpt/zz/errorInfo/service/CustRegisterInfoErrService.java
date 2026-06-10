package com.kayak.rpt.zz.errorInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.CustRegisterInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.CustRegisterInfoErr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "投资者登记错误信息服务", model = CustRegisterInfoErr.class)
public class CustRegisterInfoErrService {

	@Autowired
	private CustRegisterInfoErrDao custRegisterInfoErrDao;

	@API(desc = "查询投资者登记错误信息信息", auth = APIAuth.YES)
	public SqlResult<CustRegisterInfoErr> findCustRegisterInfos(SqlParam<CustRegisterInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return custRegisterInfoErrDao.findCustRegisterInfos(params);
	}

	@API(desc = "添加投资者登记错误信息", params = "register_serno,bank_code_desc,is_belong_desc,iss_bank_name_desc,iss_bank_code_desc,in_out_sign_desc,iss_country_desc,data_type_desc,ori_cust_no_desc,cust_no_desc,cust_type_desc,personal_id_type_desc,organization_id_type_desc,other_id_name_desc,id_code_desc,spv_open_bank_desc,other_open_bank_desc,cust_name_desc,sex_desc,risk_level_desc,moble_desc,tel_phone_desc,email_desc,imp_date,remark_desc", auth = APIAuth.NO)
	public int addCustRegisterInfo(SqlParam<CustRegisterInfoErr> params) throws Exception {
		return custRegisterInfoErrDao.addCustRegisterInfo(params).getEffect();
	}
	
	@API(desc = "修改投资者登记错误信息", params = "register_serno,bank_code_desc,is_belong_desc,iss_bank_name_desc,iss_bank_code_desc,in_out_sign_desc,iss_country_desc,data_type_desc,ori_cust_no_desc,cust_no_desc,cust_type_desc,personal_id_type_desc,organization_id_type_desc,other_id_name_desc,id_code_desc,spv_open_bank_desc,other_open_bank_desc,cust_name_desc,sex_desc,risk_level_desc,moble_desc,tel_phone_desc,email_desc,imp_date,remark_desc", auth = APIAuth.NO)
	public int updateCustRegisterInfo(SqlParam<CustRegisterInfoErr> params) throws Exception {
		return custRegisterInfoErrDao.updateCustRegisterInfo(params).getEffect();
	}
	
	@API(desc = "删除投资者登记错误信息", params = "register_serno,bank_code_desc,is_belong_desc,iss_bank_name_desc,iss_bank_code_desc,in_out_sign_desc,iss_country_desc,data_type_desc,ori_cust_no_desc,cust_no_desc,cust_type_desc,personal_id_type_desc,organization_id_type_desc,other_id_name_desc,id_code_desc,spv_open_bank_desc,other_open_bank_desc,cust_name_desc,sex_desc,risk_level_desc,moble_desc,tel_phone_desc,email_desc,imp_date,remark_desc", auth = APIAuth.NO)
	public int deleteCustRegisterInfo(SqlParam<CustRegisterInfoErr> params) throws Exception {
		return custRegisterInfoErrDao.deleteCustRegisterInfo(params).getEffect();
	}

}
