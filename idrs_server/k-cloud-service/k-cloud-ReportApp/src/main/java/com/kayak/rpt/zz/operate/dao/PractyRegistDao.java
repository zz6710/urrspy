package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.PractyRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class PractyRegistDao extends ComnDao {

	public SqlResult<PractyRegist> findPractyRegists(SqlParam<PractyRegist> params) throws Exception {
		String sql = "SELECT id, profession, name, sex, bank_code, id_code, iss_branch_type, region, firm_name, department, post, education, degree, career_start_date, wealth_start_date, profess_qualy_level, wealth_cer, regist_cer_no, reward, telphone, mobile, email, register_classify, regist_type, details, register_serno, imp_date, register_date, register_status, SUMMIT_USER, create_date, create_time, op_type FROM app_practy_regist_remark	 where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addPractyRegist(PractyRegist practyRegist) throws Exception {
		return super.update("INSERT INTO app_practy_regist_remark(profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type) VALUES($S{profession},$S{name},$S{sex},$S{bankCode},$S{idCode},$S{issBranchType},$S{region},$S{firmName},$S{department},$S{post},$S{education},$S{degree},$S{careerStartDate},$S{wealthStartDate},$S{professQualyLevel},$S{wealthCer},$S{registCerNo},$S{reward},$S{telphone},$S{mobile},$S{email},$S{registerClassify},$S{registType},$S{details},$S{registerSerno},$S{impDate},$S{registerDate},(case when $S{opType} in ('0','3') then '0' else $S{registerStatus} end),$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType})",
				DataSourceProperty.PUB,practyRegist);
	}
	
	public UpdateResult updatePractyRegist(SqlParam<PractyRegist> params) throws Exception {
		return super.update("UPDATE app_practy_regist_remark SET profession=$S{profession} ,name=$S{name} ,sex=$S{sex} ,bank_code=$S{bankCode} ,id_code=$S{idCode} ,iss_branch_type=$S{issBranchType} ,region=$S{region} ,firm_name=$S{firmName} ,department=$S{department} ,post=$S{post} ,education=$S{education} ,degree=$S{degree} ,career_start_date=$S{careerStartDate} ,wealth_start_date=$S{wealthStartDate} ,profess_qualy_level=$S{professQualyLevel} ,wealth_cer=$S{wealthCer} ,regist_cer_no=$S{registCerNo} ,reward=$S{reward} ,telphone=$S{telphone} ,mobile=$S{mobile} ,email=$S{email} ,register_classify=$S{registerClassify} ,regist_type=$S{registType} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,op_type=$S{opType}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deletePractyRegist(SqlParam<PractyRegist> params) throws Exception {
		return super.update("DELETE FROM app_practy_regist_remark WHERE ",
				params.getModel());
	}

}
