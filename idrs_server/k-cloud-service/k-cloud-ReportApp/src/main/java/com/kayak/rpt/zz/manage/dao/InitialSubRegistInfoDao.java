package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.InitialSubRegistInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class InitialSubRegistInfoDao extends ComnDao {

	public SqlResult<InitialSubRegistInfo> findInitialSubRegistInfos(SqlParam<InitialSubRegistInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT T1.id, T1.bank_code, T1.prod_code, T1.number_indiv_invest, T1.number_corpor_invest, T1.number_ucor_invest, T1.other_distribut_agents, T1.details, T1.register_serno, T1.imp_date, T1.register_date, T1.register_status, T1.actual_subscribed_amt, T1.subscribed_vol, T1.amt_other_db_agents, T1.FND_TRST_ACT_NBR, T1.FND_TRST_ACT, T1.zon_clc_amt, T1.prod_ccy, ifnull(T1.FOUND_DT,t2.FOUND_DT) FOUND_DT, T1.create_date, T1.theory_report_start_date, T1.theory_report_end_date, T1.sys_data_source, T1.sys_data_status, T1.sys_data_version, T1.REPORT_DATE,ifnull(ARS.audit_status,0) audit_status FROM app_initial_sub_regist_info T1 LEFT JOIN base_report_data_audit_results ARS " +
				"ON T1.report_date = ARS.report_date and ARS.table_id = 'app_initial_sub_regist_info' LEFT JOIN dwd_prd_prd_bas_inf t2 on t1.prod_code = t2.PROD_REG_ENC where sys_data_status ='1'");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and  ifnull(T1.FOUND_DT,t2.FOUND_DT) >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and  ifnull(T1.FOUND_DT,t2.FOUND_DT) <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql.append(" and  T1.create_date >= '" + params.getModel().getBeginCrtDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql.append(" and  T1.create_date <= '" + params.getModel().getEndCrtDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and  T1.report_date >= '" + params.getModel().getReportBeginDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql.append(" and  T1.report_date <= '" + params.getModel().getReportEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and  T1.prod_code like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and T1.register_status = '" + params.getModel().getRegisterStatus() + "'");
		}
		return super.findRows(sql.toString(),DataSourceProperty.PUB, params);
	}

	public int findInitialSubRegistInfosCount(SqlParam<InitialSubRegistInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(1) FROM app_initial_sub_regist_info T1 LEFT JOIN base_report_data_audit_results ARS " +
				"ON T1.report_date = ARS.report_date and ARS.table_id = 'app_initial_sub_regist_info' LEFT JOIN dwd_prd_prd_bas_inf t2 on t1.prod_code = t2.PROD_REG_ENC where sys_data_status ='1'");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and  ifnull(T1.FOUND_DT,t2.FOUND_DT) >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and  ifnull(T1.FOUND_DT,t2.FOUND_DT) <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql.append(" and  T1.create_date >= '" + params.getModel().getBeginCrtDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql.append(" and  T1.create_date <= '" + params.getModel().getEndCrtDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and  T1.report_date >= '" + params.getModel().getReportBeginDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql.append(" and  T1.report_date <= '" + params.getModel().getReportEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and  T1.prod_code like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and T1.register_status = '" + params.getModel().getRegisterStatus() + "'");
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params)));
	}

	public SqlResult<InitialSubRegistInfo> findInitialSubRegistInfoSingleById(SqlParam<InitialSubRegistInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT T1.ID,T1.bank_code, T1.prod_code, T1.number_indiv_invest, T1.number_corpor_invest, T1.number_ucor_invest, T1.other_distribut_agents, T1.details, T1.register_serno, T1.imp_date, T1.register_date, T1.register_status, T1.actual_subscribed_amt, T1.subscribed_vol, T1.amt_other_db_agents, T1.FND_TRST_ACT_NBR, T1.FND_TRST_ACT, T1.zon_clc_amt, T1.prod_ccy, ifnull(T1.FOUND_DT,t2.FOUND_DT) FOUND_DT, T1.create_date, T1.theory_report_start_date, T1.theory_report_end_date, T1.sys_data_source, T1.sys_data_status, T1.sys_data_version, T1.REPORT_DATE,ifnull(ARS.audit_status,0) audit_status FROM app_initial_sub_regist_info T1 LEFT JOIN base_report_data_audit_results ARS " +
				"ON T1.report_date = ARS.report_date and ARS.table_id = 'app_initial_sub_regist_info' LEFT JOIN dwd_prd_prd_bas_inf t2 on t1.prod_code = t2.PROD_REG_ENC where sys_data_status ='1'");
		if (StringUtils.isNotBlank(params.getModel().getId())) {
			sql.append(" and T1.id = '" + params.getModel().getId() + "'");
		}
		return super.findRows(sql.toString(),DataSourceProperty.PUB, params);
	}
	public SqlResult<InitialSubRegistInfo> findInitialSubRegistByprod(String prods, SqlParam<InitialSubRegistInfo> params) throws Exception {
		params.setStart(0);
		params.setLimit(2000);

		String sql = "SELECT T1.id, T1.bank_code, T1.prod_code, T1.number_indiv_invest, T1.number_corpor_invest, T1.number_ucor_invest, T1.other_distribut_agents," +
				" T1.details, T1.register_serno, T1.imp_date, T1.register_date, T1.register_status, T1.actual_subscribed_amt, T1.subscribed_vol, T1.amt_other_db_agents," +
				" T1.FND_TRST_ACT_NBR, T1.FND_TRST_ACT, T1.zon_clc_amt, T1.prod_ccy, ifnull(T1.FOUND_DT,t2.FOUND_DT) FOUND_DT, T1.create_date, T1.theory_report_start_date, " +
				" T1.theory_report_end_date, T1.sys_data_source, T1.sys_data_status, T1.sys_data_version, T1.REPORT_DATE,ARS.audit_status " +
				" FROM app_initial_sub_regist_info T1 LEFT JOIN base_report_data_audit_results ARS ON ARS.table_id = 'app_initial_sub_regist_info' " +
				" LEFT JOIN dwd_prd_prd_bas_inf t2 on t1.prod_code = t2.PROD_REG_ENC where sys_data_status ='1'";
		if(StringUtils.isNotBlank(prods)){
			sql = sql + " and  T1.prod_code in (" + prods + ")";
		}
		SqlResult<InitialSubRegistInfo> r1 = super.findRows(sql, DataSourceProperty.PUB, params);
		return r1;
	}

	public int findInitialSubRegistInfoFailStatus(SqlParam<InitialSubRegistInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(1) FROM app_initial_sub_regist_info T1 LEFT JOIN base_report_data_audit_results ARS " +
				"ON ARS.table_id = 'app_initial_sub_regist_info' LEFT JOIN dwd_prd_prd_bas_inf t2 on t1.prod_code = t2.PROD_REG_ENC  where sys_data_status ='1'  " +
				"and T1.register_status in (0,1) ");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and   ifnull(T1.FOUND_DT,t2.FOUND_DT) >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and   ifnull(T1.FOUND_DT,t2.FOUND_DT) <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql.append(" and  T1.create_date >= '" + params.getModel().getBeginCrtDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql.append(" and  T1.create_date <= '" + params.getModel().getEndCrtDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and  T1.report_date >= '" + params.getModel().getReportBeginDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql.append(" and  T1.report_date <= '" + params.getModel().getReportEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and  T1.report_date >= '" + params.getModel().getReportBeginDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and  T1.prod_code like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and T1.register_status = '" + params.getModel().getRegisterStatus() + "'");
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params)));
	}

	public UpdateResult addInitialSubRegistInfo(SqlParam<InitialSubRegistInfo> params) throws Exception {
		return super.update("INSERT INTO app_initial_sub_regist_info(fnd_trst_act_nbr,fnd_trst_act,zon_clc_amt,bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents,report_date,prod_ccy) VALUES($S{fndTrstActNbr}," +
						"$S{fndTrstAct},$S{zonClcAmt},$S{bankCode},$S{prodCode},$D{numberIndivInvest},$D{numberCorporInvest},$D{numberUcorInvest},$S{otherDistributAgents},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},'','0',$D{actualSubscribedAmt},$D{subscribedVol},$D{amtOtherDbAgents},date_format(CURDATE(),'%Y%m%d'),$S{prodCcy})",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateInitialSubRegistInfo(SqlParam<InitialSubRegistInfo> params) throws Exception {
		return super.update("UPDATE app_initial_sub_regist_info SET bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,number_indiv_invest=$D{numberIndivInvest} ,number_corpor_invest=$D{numberCorporInvest} ,number_ucor_invest=$D{numberUcorInvest} ,other_distribut_agents=$S{otherDistributAgents} ,details=$S{details} ,fnd_trst_act_nbr =$S{fndTrstActNbr},fnd_trst_act =$S{fndTrstAct},zon_clc_amt=$S{zonClcAmt},register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,actual_subscribed_amt=$D{actualSubscribedAmt} ,subscribed_vol=$D{subscribedVol} ,amt_other_db_agents=$D{amtOtherDbAgents},prod_ccy=$S{prodCcy}  WHERE register_serno=$S{registerSerno}",
				DataSourceProperty.PUB,params.getModel());
	}

	public String getProdCode(SqlParam<InitialSubRegistInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT distinct T1.prod_code FROM app_initial_sub_regist_info T1 LEFT JOIN base_report_data_audit_results ARS " +
				"ON ARS.table_id = 'app_initial_sub_regist_info' LEFT JOIN dwd_prd_prd_bas_inf t2 on t1.prod_code = t2.PROD_REG_ENC where sys_data_status ='1'");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and  ifnull(T1.FOUND_DT,t2.FOUND_DT) >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and  ifnull(T1.FOUND_DT,t2.FOUND_DT) <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql.append(" and  T1.create_date >= '" + params.getModel().getBeginCrtDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql.append(" and  T1.create_date <= '" + params.getModel().getEndCrtDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and  T1.report_date >= '" + params.getModel().getReportBeginDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql.append(" and  T1.report_date <= '" + params.getModel().getReportEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and  T1.prod_code like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and T1.register_status = '" + params.getModel().getRegisterStatus() + "'");
		}

		StringBuffer ss = new StringBuffer();
		List<SqlRow> list = super.findRows(sql.toString(), DataSourceProperty.PUB, params.getModel());

		if (list != null && list.size() >0 ){
			for(int i = 0;i<list.size();i++){
				if(i == list.size()-1){
					ss.append("'"+list.get(i).get("prod_code")+"'");
				}else{
					ss.append("'"+list.get(i).get("prod_code")+"',");
				}
			}
		}
		return ss.toString();
	}

	public UpdateResult updateInitialSubRegistInfoStatus(SqlParam<InitialSubRegistInfo> params, String prod_code) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE app_initial_sub_regist_info T1 SET register_status='3' WHERE sys_data_status='1' ");
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
			sql.append(" and  T1.FOUND_DT >= '" + params.getModel().getBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql.append(" and  T1.FOUND_DT <= '" + params.getModel().getQueryDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())) {
			sql.append(" and  T1.create_date >= '" + params.getModel().getBeginCrtDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql.append(" and  T1.create_date <= '" + params.getModel().getEndCrtDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and  T1.report_date >= '" + params.getModel().getReportBeginDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql.append(" and  T1.report_date <= '" + params.getModel().getReportEndDate() + "'");
		}
//		if (StringUtils.isNotBlank(prod_code)) {
//			sql.append(" and  T1.prod_code in (" + prod_code + ")");
//		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql.append(" and  T1.prod_code like '%" + params.getModel().getProdCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and T1.register_status = '" + params.getModel().getRegisterStatus() + "'");
		}
		return super.update(sql.toString(),
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateProdStat(SqlParam<InitialSubRegistInfo> params, String prod_code) throws Exception {
		String sql = "UPDATE ods_prod_base_info t left join app_initial_sub_regist_info a on a.prod_code = t.CHECK_INON SET t.PROD_STATUS='06' where t.PROD_STATUS = '04' ";
		if (StringUtils.isNotBlank(params.getModel().getBeginDate())&&StringUtils.isNotBlank(params.getModel().getQueryDate())) {
			sql += " and  a.FOUND_DT>='" +params.getModel().getBeginDate()+"' and a.FOUND_DT<='" +params.getModel().getQueryDate()+"' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getBeginCrtDate())&&StringUtils.isNotBlank(params.getModel().getEndCrtDate())) {
			sql += " and a.create_date>='" +params.getModel().getBeginCrtDate()+"' and a.create_date<='" +params.getModel().getEndCrtDate()+"' ";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql +=  " and  a.report_date >= '" + params.getModel().getReportBeginDate()+ "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql +=  " and  a.report_date <= '" + params.getModel().getReportEndDate() + "'";
		}
		if(StringUtils.isNotBlank(prod_code)){
			sql = sql + " and t.CHECK_INON in ("+prod_code+") ";
		}
		return super.update(sql, DataSourceProperty.PUB);
	}
	public void updateBaseReportResultInfo(SqlParam<InitialSubRegistInfo> params) throws Exception {
		String sql="update base_report_result set register_date = theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_initial_sub_regist_info' and theory_report_start_date in (select theory_report_start_date from app_initial_sub_regist_info where report_date between $S{reportBeginDate} and $S{reportEndDate}) ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}

	public void updateProdDataFlag(SqlParam<InitialSubRegistInfo> params, String prod_code) throws Exception {
//		String sql1 = "UPDATE ods_prod_base_info set data_flag_old=data_flag where upd_dt = date_format(now(),'%Y%m%d') and data_flag_old is null ";
		String sql2 = "UPDATE ods_prod_base_info set data_flag_old=data_flag , " +
				" data_flag=trim(both ',' from replace(concat(data_flag,','),'2,','')) " +
				" where upd_dt = date_format(now(),'%Y%m%d') " +
				" and exists (select 1 from app_initial_sub_regist_info t1 where t1.prod_code = ods_prod_base_info.check_inon " +
				" and register_status != '3' and report_date  = date_format(now(),'%Y%m%d') )";

		if(StringUtils.isNotBlank(prod_code)){
			sql2 = sql2 + " and CHECK_INON in ("+prod_code+") ";
		}

//		super.update(sql1, DataSourceProperty.PUB,params);
		super.update(sql2, DataSourceProperty.PUB, params);
	}

	public UpdateResult deleteInitialSubRegistInfo(SqlParam<InitialSubRegistInfo> params) throws Exception {
		return super.update("DELETE FROM app_initial_sub_regist_info WHERE register_serno=$S{registerSerno}",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportInitialSubRegistInfo(Object param) throws Exception {

		return super.update("INSERT INTO app_initial_sub_regist_info(fnd_trst_act_nbr,fnd_trst_act,zon_clc_amt,prod_ccy,bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents,report_date,found_dt,theory_report_start_date,sys_data_version) VALUES($S{fndTrstActNbr},$S{fndTrstAct},$S{zonClcAmt},$S{prodCcy},$S{bankCode},$S{prodCode},$D{numberIndivInvest},$D{numberCorporInvest},$D{numberUcorInvest},$S{otherDistributAgents},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),'','0',$D{actualSubscribedAmt},$D{subscribedVol},$D{amtOtherDbAgents},$S{reportDate},(select ESTABLISH_DATE  from  ods_prod_base_info  where mother_fund_flag  in('0','1') and CHECK_INON  =$S{prodCode} limit 1),(select workday  from sys_workday_set  where workday <$S{reportDate} order by workday desc limit 1),'1.0')",
				DataSourceProperty.PUB,param);
	}

	public UpdateResult deleteImportInitialSubRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_initial_sub_regist_info where REPORT_DATE=$D{reportDate} ", params);
	}


}
