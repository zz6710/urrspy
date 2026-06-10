package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.AppSonShareInfoReg;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class AppSonShareInfoRegDao extends ComnDao {

	public SqlResult<AppSonShareInfoReg> findAppSonShareInfoRegs(SqlParam<AppSonShareInfoReg> params) throws Exception {
		String sql = "SELECT bank_code,prod_reg_enc,son_share_code,son_share_name,son_share_task_type,son_share_reg_enc,son_share_sale_obj,task_date,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time FROM app_son_share_info_reg WHERE 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and  report_date >='" + params.getModel().getReportBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and  report_date <='" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdRegEnc())) {
			sql = sql + " and  prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareCode())) {
			sql = sql + " and  son_share_code like '%" + params.getModel().getSonShareCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  register_status like '%" + params.getModel().getRegisterStatus() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareTaskType())) {
			sql = sql + " and  son_share_task_type like '%" + params.getModel().getSonShareTaskType() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTaskDate())) {
			sql = sql + " and  task_date like '%" + params.getModel().getTaskDate() + "%'";
		}
		sql = sql + " and sys_data_status = '1'";
		return super.findRows(sql, params);
	}

	public int findAppSonShareInfoRegsCount(SqlParam<AppSonShareInfoReg> params) throws Exception {
		String sql = "SELECT count(1) FROM app_son_share_info_reg WHERE 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and  report_date >='" + params.getModel().getReportBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and  report_date <='" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdRegEnc())) {
			sql = sql + " and  prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareCode())) {
			sql = sql + " and  son_share_code like '%" + params.getModel().getSonShareCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  register_status like '%" + params.getModel().getRegisterStatus() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareTaskType())) {
			sql = sql + " and  son_share_task_type like '%" + params.getModel().getSonShareTaskType() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTaskDate())) {
			sql = sql + " and  task_date like '%" + params.getModel().getTaskDate() + "%'";
		}
		sql = sql + " and sys_data_status = '1'";
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params)));
	}

	public UpdateResult addAppSonShareInfoReg(SqlParam<AppSonShareInfoReg> params) throws Exception {
		return super.update("INSERT INTO app_son_share_info_reg(bank_code,prod_reg_enc,son_share_code,son_share_name,son_share_task_type,son_share_reg_enc,son_share_sale_obj,task_date,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time) VALUES($S{bankCode},$S{bankName},$S{prodRegEnc},$S{prodName},$S{sonShareCode},$S{sonShareName},$S{sonShareTaskType},$S{sonShareRegEnc},$S{sonShareSaleObj},$S{taskDate},$S{details},$S{registerDate},$S{registerSerno},$S{registerStatus},$S{createDate},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{sysDataStatus},$S{sysDataSource},$S{sysDataVersion},$S{reportDate},$AUTOIDI{id},$S{crtUser},$S{updUser},$S{crtDt},$S{updDt},$S{crtTime},$S{updTime})",
				params.getModel());
	}

	public UpdateResult updateAppSonShareInfoReg(SqlParam<AppSonShareInfoReg> params) throws Exception {
		return super.update("UPDATE app_son_share_info_reg SET bank_code=$S{bankCode} ,prod_reg_enc=$S{prodRegEnc},son_share_code=$S{sonShareCode} ,son_share_name=$S{sonShareName} ,son_share_task_type=$S{sonShareTaskType} ,son_share_reg_enc=$S{sonShareRegEnc} ,son_share_sale_obj=$S{sonShareSaleObj} ,task_date=$S{taskDate} ,details=$S{details} ,register_date=$S{registerDate} ,register_serno=$S{registerSerno} ,register_status=$S{registerStatus} ,create_date=$S{createDate} ,theory_report_start_date=$S{theoryReportStartDate} ,theory_report_end_date=$S{theoryReportEndDate} ,sys_data_status=$S{sysDataStatus} ,sys_data_source=$S{sysDataSource} ,sys_data_version=$S{sysDataVersion} ,report_date=$S{reportDate} ,crt_user=$S{crtUser} ,upd_user=$S{updUser} ,crt_dt=$S{crtDt} ,upd_dt=$S{updDt} ,crt_time=$S{crtTime} ,upd_time=$S{updTime}  WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteAppSonShareInfoReg(SqlParam<AppSonShareInfoReg> params) throws Exception {
		return super.update("DELETE FROM app_son_share_info_reg WHERE  id=$I{id} ",
				params.getModel());
	}

	public int findAppSonShareInfoRegStatus(SqlParam<AppSonShareInfoReg> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(1) FROM app_son_share_info_reg T1 LEFT JOIN base_report_data_audit_results ARS " +
				"ON ARS.table_id = 'app_son_share_info_reg' LEFT JOIN dwd_prd_prd_bas_inf t2 on t1.prod_reg_enc = t2.PROD_REG_ENC  where sys_data_status ='1'  " +
				"and T1.register_status in (0,1) ");
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and  T1.report_date >= '" + params.getModel().getReportBeginDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql.append(" and  T1.report_date <= '" + params.getModel().getReportEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdRegEnc())) {
			sql.append(" and  T1.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareCode())) {
			sql.append(" and  T1.son_share_code like '%" + params.getModel().getSonShareCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and  T1.register_status like '%" + params.getModel().getRegisterStatus() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareTaskType())) {
			sql.append(" and  T1.son_share_task_type like '%" + params.getModel().getSonShareTaskType() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getTaskDate())) {
			sql.append(" and  T1.task_date like '%" + params.getModel().getTaskDate() + "%'");
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params)));
	}

	public UpdateResult updateAppSonShareInfoRegStatus(SqlParam<AppSonShareInfoReg> params) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE app_son_share_info_reg T1 SET register_status='3' WHERE sys_data_status='1' ");
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and  T1.report_date >= '" + params.getModel().getReportBeginDate()+ "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql.append(" and  T1.report_date <= '" + params.getModel().getReportEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and  T1.register_status = '" + params.getModel().getRegisterStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdRegEnc())) {
			sql.append(" and  T1.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareCode())) {
			sql.append(" and  T1.son_share_code like '%" + params.getModel().getSonShareCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareTaskType())) {
			sql.append(" and  T1.son_share_task_type like '%" + params.getModel().getSonShareTaskType() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getTaskDate())) {
			sql.append(" and  T1.task_date like '%" + params.getModel().getTaskDate() + "%'");
		}
		return super.update(sql.toString(),
				DataSourceProperty.PUB,params.getModel());
	}

	public void updateBaseReportResultInfo(SqlParam<AppSonShareInfoReg> params) throws Exception {
		String sql="update base_report_result set register_date = theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_son_share_info_reg' and theory_report_start_date in (select theory_report_start_date from app_son_share_info_reg where report_date between $S{reportBeginDate} and $S{reportEndDate}) ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}

	public List<SqlRow> findSonShareProdInfo(Map<String, Object> params) throws Exception {
		return super.findRows("SELECT PROD_CODE,ESTABLISH_DATE FROM ods_prod_base_info where mother_fund_flag='2'", params);
	}

	public UpdateResult updateAppSonShareInfoReg(Object params) throws Exception {
		return super.update("update app_son_share_info_reg set son_share_reg_enc=$S{sonShareRegEnc}, upd_user=$S{updUser}, upd_dt=$S{updDt}, upd_time=$S{updTime} where son_share_code=$S{sonShareCode}",
				DataSourceProperty.PUB, params);
	}

}
