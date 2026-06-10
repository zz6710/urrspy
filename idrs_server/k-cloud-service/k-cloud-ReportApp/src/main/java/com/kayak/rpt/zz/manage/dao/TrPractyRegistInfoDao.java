package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.TrPractyRegistInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TrPractyRegistInfoDao extends ComnDao {

	public SqlResult<TrPractyRegistInfo> findTrPractyRegistInfos(SqlParam<TrPractyRegistInfo> params) throws Exception {
		return super.findRows("SELECT profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status,create_date,theory_report_start_date,theory_report_end_date FROM app_practy_regist_info", params);
	}

	public SqlResult<TrPractyRegistInfo> findTrPractyRegistInfosAndIsError(SqlParam<TrPractyRegistInfo> params) throws Exception {
		String sql = "SELECT  create_date,profession, NAME,jobnumber, sex, bank_code, id_code, iss_branch_type, region, firm_name, department, post, \n" +
				"education, degree, career_start_date, wealth_start_date, profess_qualy_level, wealth_cer, regist_cer_no, reward, \n" +
				"telphone, mobile, email, register_classify, regist_type, details, register_serno, imp_date, register_date, \n" +
				"register_status, is_error,theory_report_start_date,AA.audit_status \n" +
				"FROM  (  \n" +
				"SELECT DISTINCT T1.*, ARS.audit_status, (case when t2.register_serno is null then '0' else '1' end) AS is_error \n" +
				"FROM app_practy_regist_info  T1 \n" +
				"LEFT JOIN  app_practy_regist_info_erdesc  T2  ON T2.register_serno = T1.register_serno \n" +
				"LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_practy_regist_info'\n" +
				")  \n" +
				"AA  where 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addTrPractyRegistInfo(SqlParam<TrPractyRegistInfo> params) throws Exception {
		return super.update("INSERT INTO app_practy_regist_info(profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status,create_date,theory_report_start_date,jobnumber) VALUES($S{profession},$S{name},$S{sex},$S{bankCode},$S{idCode},$S{issBranchType},$S{region},$S{firmName},$S{department},$S{post},$S{education},$S{degree},$S{careerStartDate},$S{wealthStartDate},$S{professQualyLevel},$S{wealthCer},$S{registCerNo},$S{reward},$S{telphone},$S{mobile},$S{email},$S{registerClassify},$S{registType},$S{details},  (select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',date_format(CURDATE(),'%Y%m%d'),date_format(CURDATE(),'%Y%m%d'),$S{jobnumber})",
				params.getModel());
	}

	public void addTrPractyRegistInfoBatch(List<Map<String, Object>> mapList) throws Exception {
		Date date = new Date();
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(date);

		String  sql  = "INSERT INTO app_practy_regist_info(profession,name,sex,bank_code,id_code,iss_branch_type,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_date,register_status,create_date,theory_report_start_date) VALUES($S{profession},$S{name},$S{sex},$S{bankCode},$S{idCode},$S{issBranchType},$S{region},$S{firmName},$S{department},$S{post},$S{education},$S{degree},$S{careerStartDate},$S{wealthStartDate},$S{professQualyLevel},$S{wealthCer},$S{registCerNo},$S{reward},$S{telphone},$S{mobile},$S{email},$S{registerClassify},$S{registType},$S{details},  (select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',date_format(CURDATE(),'%Y%m%d'),date_format(CURDATE(),'%Y%m%d'))";
		for (Map<String, Object> map : mapList) {
			map.put("impDate",dateStr);
			super.update(sql,map);
		}
	}

	public UpdateResult updateTrPractyRegistInfo(SqlParam<TrPractyRegistInfo> params) throws Exception {
		String sql = "UPDATE app_practy_regist_info SET profession=$S{profession} ,name=$S{name} ,sex=$S{sex} ,bank_code=$S{bankCode} ,";
		if(!params.getModel().getInitIdCode().equals(params.getModel().getIdCode())){
			sql += "id_code=$S{idCode} ,";
		}
		sql += "iss_branch_type=$S{issBranchType} ,region=$S{region} ,firm_name=$S{firmName} ,department=$S{department} ,post=$S{post} ,education=$S{education} ,degree=$S{degree} ,career_start_date=$S{careerStartDate} ,wealth_start_date=$S{wealthStartDate} ,profess_qualy_level=$S{professQualyLevel} ,wealth_cer=$S{wealthCer} ,regist_cer_no=$S{registCerNo} ,reward=$S{reward} ,telphone=$S{telphone} ,mobile=$S{mobile} ,email=$S{email} ,register_classify=$S{registerClassify} ,regist_type=$S{registType} ,details=$S{details}  ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus},theory_report_start_date=date_format(CURDATE(),'%Y%m%d'),jobnumber=$S{jobnumber}  WHERE  register_serno=$S{registerSerno}";
		return super.update(sql,params.getModel());
	}

	public UpdateResult deleteTrPractyRegistInfo(SqlParam<TrPractyRegistInfo> params) throws Exception {
		return super.update("DELETE FROM app_practy_regist_info WHERE  register_serno=$S{registerSerno}  ",
				params.getModel());
	}


	public UpdateResult addImporTrPractyRegistInfo(TrPractyRegistInfo trPractyRegist) throws Exception {
		return super.update("insert into app_practy_regist_remark (profession,name,sex,bank_code,iss_branch_type,id_code,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,mobile,email,register_classify,regist_type,details,register_serno,imp_date,register_status,SUMMIT_USER,create_date,create_time,op_type) values ($S{profession},$S{name},$S{sex},$S{bankCode},$S{issBranchType},$S{idCode},$S{region},$S{firmName},$S{department},$S{post},$S{education},$S{degree},$S{careerStartDate},$S{wealthStartDate},$S{professQualyLevel},$S{wealthCer},$S{registCerNo},$S{reward},$S{mobile},$S{email},$S{registerClassify},$S{registType},$S{details},$S{registerSerno},date_format(CURDATE(),'%Y%m%d'),'0',$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType})",
				DataSourceProperty.PUB,trPractyRegist);
	}


	public UpdateResult deleteImportTrPractyRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_practy_regist_info where theory_report_start_date between $S{beginDate} and $S{queryDate} ", params);
	}

	public UpdateResult addImportTrPractyRegistInfo(Object map) throws Exception {
		return super.update("insert into app_practy_regist_info (profession,name,sex,bank_code,iss_branch_type,id_code,region,firm_name,department,post,education,degree,career_start_date,wealth_start_date,profess_qualy_level,wealth_cer,regist_cer_no,reward,telphone,mobile,email,register_classify,regist_type,details,register_serno,register_date,register_status,theory_report_start_date,create_date,jobnumber) values ($S{profession},$S{name},$S{sex},$S{bankCode},$S{issBranchType},$S{idCode},$S{region},$S{firmName},$S{department},$S{post},$S{education},$S{degree},$S{careerStartDate},$S{wealthStartDate},$S{professQualyLevel},$S{wealthCer},$S{registCerNo},$S{reward},$S{telphone},$S{mobile},$S{email},$S{registerClassify},$S{registType},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{registerDate},'0',date_format(CURDATE(),'%Y%m%d'),date_format(CURDATE(),'%Y%m%d'),$S{jobnumber})",
				DataSourceProperty.PUB,map);
	}
}
