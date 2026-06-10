package com.kayak.rpt.zz.errorInfo.service;

import com.kayak.aspect.annotations.APIOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.errorInfo.dao.PractyRegistInfoErrDao;
import com.kayak.rpt.zz.errorInfo.model.PractyRegistInfoErr;

@Service
@APIDefine(desc = "从业人员登记错误信息服务", model = PractyRegistInfoErr.class)
public class PractyRegistInfoErrService {

	@Autowired
	private PractyRegistInfoErrDao practyRegistInfoErrDao;

	@API(desc = "查询从业人员登记错误信息信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
	public SqlResult<PractyRegistInfoErr> findPractyRegistInfos(SqlParam<PractyRegistInfoErr> params) throws Exception {
		params.setMakeSql(true);
		return practyRegistInfoErrDao.findPractyRegistInfos(params);
	}

	@API(desc = "添加从业人员登记错误信息", params = "profession_desc,name_desc,sex_desc,bank_code_desc,id_code_desc,iss_branch_type_desc,region_desc,firm_name_desc,department_desc,post_desc,education_desc,degree_desc,career_start_date_desc,wealth_start_date_desc,profess_qualy_level_desc,wealth_cer_desc,regist_cer_no_desc,reward_desc,telphone_desc,mobile_desc,email_desc,register_classify_desc,regist_type_desc,details_desc,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int addPractyRegistInfo(SqlParam<PractyRegistInfoErr> params) throws Exception {
		return practyRegistInfoErrDao.addPractyRegistInfo(params).getEffect();
	}
	
	@API(desc = "修改从业人员登记错误信息", params = "profession_desc,name_desc,sex_desc,bank_code_desc,id_code_desc,iss_branch_type_desc,region_desc,firm_name_desc,department_desc,post_desc,education_desc,degree_desc,career_start_date_desc,wealth_start_date_desc,profess_qualy_level_desc,wealth_cer_desc,regist_cer_no_desc,reward_desc,telphone_desc,mobile_desc,email_desc,register_classify_desc,regist_type_desc,details_desc,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int updatePractyRegistInfo(SqlParam<PractyRegistInfoErr> params) throws Exception {
		return practyRegistInfoErrDao.updatePractyRegistInfo(params).getEffect();
	}
	
	@API(desc = "删除从业人员登记错误信息", params = "profession_desc,name_desc,sex_desc,bank_code_desc,id_code_desc,iss_branch_type_desc,region_desc,firm_name_desc,department_desc,post_desc,education_desc,degree_desc,career_start_date_desc,wealth_start_date_desc,profess_qualy_level_desc,wealth_cer_desc,regist_cer_no_desc,reward_desc,telphone_desc,mobile_desc,email_desc,register_classify_desc,regist_type_desc,details_desc,register_serno,imp_date,register_date,register_status", auth = APIAuth.NO)
	public int deletePractyRegistInfo(SqlParam<PractyRegistInfoErr> params) throws Exception {
		return practyRegistInfoErrDao.deletePractyRegistInfo(params).getEffect();
	}

}
