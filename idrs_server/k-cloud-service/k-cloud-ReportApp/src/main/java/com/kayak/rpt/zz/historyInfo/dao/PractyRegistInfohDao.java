package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.PractyRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class PractyRegistInfohDao extends ComnDao {

	public SqlResult<PractyRegistInfoh> findPractyRegistInfohs(SqlParam<PractyRegistInfoh> params) throws Exception {
		String sql = "SELECT profession, name, sex, bank_code, id_code, iss_branch_type, region, firm_name, department, post, education, degree, career_start_date, wealth_start_date, profess_qualy_level, wealth_cer, regist_cer_no, reward, telphone, mobile, email, register_classify, regist_type, details, register_serno, imp_date, register_date, register_status, create_date, theory_report_start_date, theory_report_end_date FROM app_practy_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addPractyRegistInfoh(SqlParam<PractyRegistInfoh> params) throws Exception {
		return super.update("INSERT INTO app_practy_regist_info_h(profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status) VALUES($S{profession},$S{name},$S{sex},$S{bankCode},$S{idCode},$S{issBranchType},$S{region},$S{firmName},$S{department},$S{post},$S{education},$S{degree},$S{careerStartDate},$S{wealthStartDate},$S{professQualyLevel},$S{wealthCer},$S{registCerNo},$S{reward},$S{telphone},$S{mobile},$S{email},$S{registerClassify},$S{registType},$S{details},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus})",
				params.getModel());
	}
	
	public UpdateResult updatePractyRegistInfoh(SqlParam<PractyRegistInfoh> params) throws Exception {
		return super.update("UPDATE app_practy_regist_info_h SET profession=$S{profession} ,name=$S{name} ,sex=$S{sex} ,bank_code=$S{bankCode} ,id_code=$S{idCode} ,iss_branch_type=$S{issBranchType} ,region=$S{region} ,firm_name=$S{firmName} ,department=$S{department} ,post=$S{post} ,education=$S{education} ,degree=$S{degree} ,career_start_date=$S{careerStartDate} ,wealth_start_date=$S{wealthStartDate} ,profess_qualy_level=$S{professQualyLevel} ,wealth_cer=$S{wealthCer} ,regist_cer_no=$S{registCerNo} ,reward=$S{reward} ,telphone=$S{telphone} ,mobile=$S{mobile} ,email=$S{email} ,register_classify=$S{registerClassify} ,regist_type=$S{registType} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deletePractyRegistInfoh(SqlParam<PractyRegistInfoh> params) throws Exception {
		return super.update("DELETE FROM app_practy_regist_info_h WHERE ",
				params.getModel());
	}

}
