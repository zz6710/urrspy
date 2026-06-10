package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.CustRegisterInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class CustRegisterInfoErrDao extends ComnDao {

	public SqlResult<CustRegisterInfoErr> findCustRegisterInfos(SqlParam<CustRegisterInfoErr> params) throws Exception {
		String sql = "SELECT register_serno, bank_code_desc, is_belong_desc, iss_bank_name_desc, iss_bank_code_desc, in_out_sign_desc, iss_country_desc, data_type_desc, ori_cust_no_desc, cust_no_desc, cust_type_desc, personal_id_type_desc, organization_id_type_desc, other_id_name_desc, id_code_desc, spv_open_bank_desc, other_open_bank_desc, cust_name_desc, sex_desc, risk_level_desc, moble_desc, tel_phone_desc, email_desc, imp_date, remark_desc, create_date, theory_report_start_date, theory_report_end_date, id, report_date\n" +
				"FROM app_cust_register_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addCustRegisterInfo(SqlParam<CustRegisterInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_cust_register_info_erdesc(register_serno,bank_code_desc,is_belong_desc,iss_bank_name_desc,iss_bank_code_desc,in_out_sign_desc,iss_country_desc,data_type_desc,ori_cust_no_desc,cust_no_desc,cust_type_desc,personal_id_type_desc,organization_id_type_desc,other_id_name_desc,id_code_desc,spv_open_bank_desc,other_open_bank_desc,cust_name_desc,sex_desc,risk_level_desc,moble_desc,tel_phone_desc,email_desc,imp_date,remark_desc) VALUES($S{registerSerno},$S{bankCodeDesc},$S{isBelongDesc},$S{issBankNameDesc},$S{issBankCodeDesc},$S{inOutSignDesc},$S{issCountryDesc},$S{dataTypeDesc},$S{oriCustNoDesc},$S{custNoDesc},$S{custTypeDesc},$S{personalIdTypeDesc},$S{organizationIdTypeDesc},$S{otherIdNameDesc},$S{idCodeDesc},$S{spvOpenBankDesc},$S{otherOpenBankDesc},$S{custNameDesc},$S{sexDesc},$S{riskLevelDesc},$S{mobleDesc},$S{telPhoneDesc},$S{emailDesc},$S{impDate},$S{remarkDesc})",
				params.getModel());
	}
	
	public UpdateResult updateCustRegisterInfo(SqlParam<CustRegisterInfoErr> params) throws Exception {
		return super.update("UPDATE app_cust_register_info_erdesc SET register_serno=$S{registerSerno} ,bank_code_desc=$S{bankCodeDesc} ,is_belong_desc=$S{isBelongDesc} ,iss_bank_name_desc=$S{issBankNameDesc} ,iss_bank_code_desc=$S{issBankCodeDesc} ,in_out_sign_desc=$S{inOutSignDesc} ,iss_country_desc=$S{issCountryDesc} ,data_type_desc=$S{dataTypeDesc} ,ori_cust_no_desc=$S{oriCustNoDesc} ,cust_no_desc=$S{custNoDesc} ,cust_type_desc=$S{custTypeDesc} ,personal_id_type_desc=$S{personalIdTypeDesc} ,organization_id_type_desc=$S{organizationIdTypeDesc} ,other_id_name_desc=$S{otherIdNameDesc} ,id_code_desc=$S{idCodeDesc} ,spv_open_bank_desc=$S{spvOpenBankDesc} ,other_open_bank_desc=$S{otherOpenBankDesc} ,cust_name_desc=$S{custNameDesc} ,sex_desc=$S{sexDesc} ,risk_level_desc=$S{riskLevelDesc} ,moble_desc=$S{mobleDesc} ,tel_phone_desc=$S{telPhoneDesc} ,email_desc=$S{emailDesc} ,imp_date=$S{impDate} ,remark_desc=$S{remarkDesc}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteCustRegisterInfo(SqlParam<CustRegisterInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_cust_register_info_erdesc WHERE ",
				params.getModel());
	}

}
