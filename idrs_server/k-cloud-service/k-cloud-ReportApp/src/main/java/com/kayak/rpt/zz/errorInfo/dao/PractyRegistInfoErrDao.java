package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.PractyRegistInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class PractyRegistInfoErrDao extends ComnDao {

	public SqlResult<PractyRegistInfoErr> findPractyRegistInfos(SqlParam<PractyRegistInfoErr> params) throws Exception {
		String sql = "SELECT profession_desc,name_desc,sex_desc,bank_code_desc,id_code_desc,iss_branch_type_desc,region_desc,firm_name_desc,department_desc,post_desc,education_desc,degree_desc,career_start_date_desc,wealth_start_date_desc,profess_qualy_level_desc,wealth_cer_desc,regist_cer_no_desc,reward_desc,telphone_desc,mobile_desc,email_desc,register_classify_desc,regist_type_desc,details_desc,register_serno,imp_date,register_date,register_status " +
				" FROM app_practy_regist_info_erdesc where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addPractyRegistInfo(SqlParam<PractyRegistInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_practy_regist_info_erdesc(profession_desc,name_desc,sex_desc,bank_code_desc,id_code_desc,iss_branch_type_desc,region_desc,firm_name_desc,department_desc,post_desc,education_desc,degree_desc,career_start_date_desc,wealth_start_date_desc,profess_qualy_level_desc,wealth_cer_desc,regist_cer_no_desc,reward_desc,telphone_desc,mobile_desc,email_desc,register_classify_desc,regist_type_desc,details_desc,register_serno,imp_date,register_date,register_status) VALUES($S{professionDesc},$S{nameDesc},$S{sexDesc},$S{bankCodeDesc},$S{idCodeDesc},$S{issBranchTypeDesc},$S{regionDesc},$S{firmNameDesc},$S{departmentDesc},$S{postDesc},$S{educationDesc},$S{degreeDesc},$S{careerStartDateDesc},$S{wealthStartDateDesc},$S{professQualyLevelDesc},$S{wealthCerDesc},$S{registCerNoDesc},$S{rewardDesc},$S{telphoneDesc},$S{mobileDesc},$S{emailDesc},$S{registerClassifyDesc},$S{registTypeDesc},$S{detailsDesc},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus})",
				params.getModel());
	}
	
	public UpdateResult updatePractyRegistInfo(SqlParam<PractyRegistInfoErr> params) throws Exception {
		return super.update("UPDATE app_practy_regist_info_erdesc SET profession_desc=$S{professionDesc} ,name_desc=$S{nameDesc} ,sex_desc=$S{sexDesc} ,bank_code_desc=$S{bankCodeDesc} ,id_code_desc=$S{idCodeDesc} ,iss_branch_type_desc=$S{issBranchTypeDesc} ,region_desc=$S{regionDesc} ,firm_name_desc=$S{firmNameDesc} ,department_desc=$S{departmentDesc} ,post_desc=$S{postDesc} ,education_desc=$S{educationDesc} ,degree_desc=$S{degreeDesc} ,career_start_date_desc=$S{careerStartDateDesc} ,wealth_start_date_desc=$S{wealthStartDateDesc} ,profess_qualy_level_desc=$S{professQualyLevelDesc} ,wealth_cer_desc=$S{wealthCerDesc} ,regist_cer_no_desc=$S{registCerNoDesc} ,reward_desc=$S{rewardDesc} ,telphone_desc=$S{telphoneDesc} ,mobile_desc=$S{mobileDesc} ,email_desc=$S{emailDesc} ,register_classify_desc=$S{registerClassifyDesc} ,regist_type_desc=$S{registTypeDesc} ,details_desc=$S{detailsDesc} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deletePractyRegistInfo(SqlParam<PractyRegistInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_practy_regist_info_erdesc WHERE ",
				params.getModel());
	}

}
