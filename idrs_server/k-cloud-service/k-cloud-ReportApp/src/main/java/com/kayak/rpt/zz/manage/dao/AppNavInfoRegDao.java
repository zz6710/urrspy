package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.zz.manage.model.AppNavInfoReg;
import com.kayak.rpt.zz.manage.model.AppSonShareDelReg;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.Map;

@Repository
public class AppNavInfoRegDao extends ComnDao {

	public SqlResult<AppNavInfoReg> findAppNavInfoRegs(SqlParam<AppNavInfoReg> params) throws Exception {
		String sql = "SELECT a.bank_code,a.prod_reg_enc,a.nav_reg_type,a.son_share_code,a.cny,cast(round(a.nav, 8) as char) as nav,cast(round(a.rmb_nav, 8) as char) as rmb_nav," +
				"            cast(round(a.dj_nav, 8) as char) as dj_nav,cast(round(a.total_nav, 8) as char) as total_nav,cast(round(a.rmb_total_nav, 8) as char) as rmb_total_nav," +
				"            cast(round(a.fq_nav, 8) as char) as fq_nav,cast(round(a.rmb_fq_nav, 8) as char) as rmb_fq_nav,a.nav_cal_type,cast(round(a.share, 5) as char) as share," +
				"            a.nav_date,a.disclosure_date,cast(round(a.remain_bal, 5) as char) as remain_bal,cast(round(a.rmb_remain_bal, 5) as char) as rmb_remain_bal,a.details,a.register_date," +
				"            a.register_serno,a.register_status,a.create_date,a.theory_report_start_date,a.theory_report_end_date,a.sys_data_status,a.sys_data_source,a.sys_data_version,a.report_date," +
				"            a.data_date,a.id,a.crt_user,a.upd_user,a.crt_dt,a.upd_dt,a.crt_time,a.upd_time," +
				"            a.mother_fund_code,a.open_type,a.vol_zero_flag,a.establish_date,a.end_date,a.jz_date,a.prfr_bnch,a.prfr_bnch_typ,a.intr_rt_upp,a.intr_rt_flr,a.prfr_bnch_typ_dscr," +
				"            a.en_dwjjsy/*万份收益*/,a.is_prod_transfer/*是否迁移产品*/,a.lst_jz_nav/*上一基准日单位净值*/,a.transfer_nav/*迁移产品迁移净值*/, a.lst_wkd_nav/*上一工作日单位净值*/, " +
				"            a.nav_dt_index/*净值日指数*/, a.lst_wkd_index/*净值日上一工作日指数*/,ifnull(ARS.audit_status,0) audit_status " +
				"       FROM app_nav_info_reg a LEFT JOIN base_report_data_audit_results ARS ON a.report_date=ARS.report_date and ARS.table_id = 'app_nav_info_reg'" +
				"      where 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and a.report_date >='" + params.getModel().getReportBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and a.report_date <='" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getNavBeginDate())) {
			sql = sql + " and a.nav_date >='" + params.getModel().getNavBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getNavEndDate())) {
			sql = sql + " and a.nav_date <='" + params.getModel().getNavEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdRegEnc())) {
			sql = sql + " and a.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareCode())) {
			sql = sql + " and a.son_share_code like '%" + params.getModel().getSonShareCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and a.register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and a.register_status in (" + SysUtil.inStr(params.getModel().getRegisterStatus()) + ")";
		}
		if (StringUtils.isNotBlank(params.getModel().getNavRegType())) {
			sql = sql + " and a.nav_reg_type like '%" + params.getModel().getNavRegType() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getMotherFundCode())) {
			sql = sql + " and a.mother_fund_code like '%" + params.getModel().getMotherFundCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getOpenType())) {
			sql = sql + " and a.open_type in (" + SysUtil.inStr(params.getModel().getOpenType()) + ")";
		}
		if (StringUtils.isNotBlank(params.getModel().getFilter0Vol())) {
			sql = sql + " and ifnull(a.vol_zero_flag,'99') <> '" + params.getModel().getFilter0Vol() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getVolZeroFlag())) {
			sql = sql + " and a.vol_zero_flag in (" + SysUtil.inStr(params.getModel().getVolZeroFlag()) + ")";
		}
		if (StringUtils.isNotBlank(params.getModel().getPrfrBnchTyp())) {
			sql = sql + " and a.prfr_bnch_typ in (" + SysUtil.inStr(params.getModel().getPrfrBnchTyp()) + ")";
		}
		sql = sql + " and a.sys_data_status in ('1','9') order by a.nav_date desc";
		return super.findRows(sql, params);
	}

	public int findAppNavInfoRegsCount(SqlParam<AppNavInfoReg> params) throws Exception {
		String sql = "SELECT count(1) FROM app_nav_info_reg a LEFT JOIN base_report_data_audit_results ARS ON a.report_date=ARS.report_date and ARS.table_id = 'app_nav_info_reg'" +
				"      where 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql = sql + " and a.report_date >='" + params.getModel().getReportBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql = sql + " and a.report_date <='" + params.getModel().getReportEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getNavBeginDate())) {
			sql = sql + " and a.nav_date >='" + params.getModel().getNavBeginDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getNavEndDate())) {
			sql = sql + " and a.nav_date <='" + params.getModel().getNavEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdRegEnc())) {
			sql = sql + " and a.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareCode())) {
			sql = sql + " and a.son_share_code like '%" + params.getModel().getSonShareCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and a.register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and a.register_status like '%" + params.getModel().getRegisterStatus() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getNavRegType())) {
			sql = sql + " and a.nav_reg_type like '%" + params.getModel().getNavRegType() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getMotherFundCode())) {
			sql = sql + " and a.mother_fund_code like '%" + params.getModel().getMotherFundCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getOpenType())) {
			sql = sql + " and a.open_type like '%" + params.getModel().getOpenType() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getFilter0Vol())) {
			sql = sql + " and ifnull(a.vol_zero_flag,'99') <> '" + params.getModel().getFilter0Vol() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getVolZeroFlag())) {
			sql = sql + " and a.vol_zero_flag like '%" + params.getModel().getVolZeroFlag() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getPrfrBnchTyp())) {
			sql = sql + " and a.prfr_bnch_typ like '%" + params.getModel().getPrfrBnchTyp() + "%'";
		}
		sql = sql + " and a.sys_data_status in ('1','9') order by a.nav_date desc";
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public UpdateResult addAppNavInfoReg(SqlParam<AppNavInfoReg> params) throws Exception {
		return super.update("INSERT INTO app_nav_info_reg(bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time) VALUES($S{bankCode},$S{prodRegEnc},$S{navRegType},$S{sonShareCode},$S{cny},$D{nav},$D{rmbNav},$D{djNav},$D{totalNav},$D{rmbTotalNav},$D{fqNav},$D{rmbFqNav},$S{navCalType},$D{share},$S{navDate},$S{disclosureDate},$D{remainBal},$D{rmbRemainBal},$S{details},$S{registerDate},$S{registerSerno},$S{registerStatus},$S{createDate},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{sysDataStatus},$S{sysDataSource},$S{sysDataVersion},$S{reportDate},$S{dataDate},$AUTOIDI{id},$S{crtUser},$S{updUser},$S{crtDt},$S{updDt},$S{crtTime},$S{updTime})",
				params.getModel());
	}
	
	public UpdateResult updateAppNavInfoReg(SqlParam<AppNavInfoReg> params) throws Exception {
		return super.update("UPDATE app_nav_info_reg SET bank_code=$S{bankCode} ,prod_reg_enc=$S{prodRegEnc} ,nav_reg_type=$S{navRegType} ,son_share_code=$S{sonShareCode} ,cny=$S{cny} ,nav=$D{nav} ,rmb_nav=$D{rmbNav} ,dj_nav=$D{djNav} ,total_nav=$D{totalNav} ,rmb_total_nav=$D{rmbTotalNav} ,fq_nav=$D{fqNav} ,rmb_fq_nav=$D{rmbFqNav} ,nav_cal_type=$S{navCalType} ,share=$D{share} ,nav_date=$S{navDate} ,disclosure_date=$S{disclosureDate} ,remain_bal=$D{remainBal} ,rmb_remain_bal=$D{rmbRemainBal} ,details=$S{details} ,register_date=$S{registerDate} ,register_serno=$S{registerSerno} ,create_date=$S{createDate} ,theory_report_start_date=$S{theoryReportStartDate} ,theory_report_end_date=$S{theoryReportEndDate} ,sys_data_status=$S{sysDataStatus} ,sys_data_source=$S{sysDataSource} ,sys_data_version=$S{sysDataVersion} ,report_date=$S{reportDate} ,data_date=$S{dataDate} ,crt_user=$S{crtUser} ,upd_user=$S{updUser} ,crt_dt=$S{crtDt} ,upd_dt=$S{updDt} ,crt_time=$S{crtTime} ,upd_time=$S{updTime}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteAppNavInfoReg(SqlParam<AppNavInfoReg> params) throws Exception {
		return super.update("DELETE FROM app_nav_info_reg WHERE  id=$I{id} ",
				params.getModel());
	}

	public int findAppNavInfoRegStatus(SqlParam<AppNavInfoReg> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(1) FROM app_nav_info_reg T1 LEFT JOIN base_report_data_audit_results ARS " +
				"ON ARS.table_id = 'app_nav_info_reg' LEFT JOIN dwd_prd_prd_bas_inf t2 on T1.prod_reg_enc = t2.PROD_REG_ENC  where T1.sys_data_status ='1'  " +
				"and T1.register_status in (0,1) ");
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and T1.report_date >='" + params.getModel().getReportBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql.append(" and T1.report_date <='" + params.getModel().getReportEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getNavBeginDate())) {
			sql.append(" and a.nav_date >='" + params.getModel().getNavBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getNavEndDate())) {
			sql.append(" and a.nav_date <='" + params.getModel().getNavEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdRegEnc())) {
			sql.append(" and  T1.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareCode())) {
			sql.append(" and  T1.son_share_code like '%" + params.getModel().getSonShareCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getNavRegType())) {
			sql.append(" and  T1.nav_reg_type like '%" + params.getModel().getNavRegType() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and  T1.register_status like '%" + params.getModel().getRegisterStatus() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getMotherFundCode())) {
			sql.append(" and  T1.mother_fund_code like '%" + params.getModel().getMotherFundCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getOpenType())) {
			sql.append(" and  T1.open_type like '%" + params.getModel().getOpenType() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getFilter0Vol())) {
			sql.append(" and ifnull(T1.vol_zero_flag,'99') <> '" + params.getModel().getFilter0Vol() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getVolZeroFlag())) {
			sql.append(" and  T1.vol_zero_flag like '%" + params.getModel().getVolZeroFlag() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getPrfrBnchTyp())) {
			sql.append(" and  T1.prfr_bnch_typ like '%" + params.getModel().getPrfrBnchTyp() + "%'");
		}
		sql.append(" and T1.sys_data_status in ('1','9') ");
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(), DataSourceProperty.PUB, params)));
	}

	public UpdateResult updateAppNavInfoRegStatus(SqlParam<AppNavInfoReg> params) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE app_nav_info_reg T1 SET register_status='3' WHERE T1.sys_data_status ='1' ");
		if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
			sql.append(" and T1.report_date >='" + params.getModel().getReportBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
			sql.append(" and T1.report_date <='" + params.getModel().getReportEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getNavBeginDate())) {
			sql.append(" and a.nav_date >='" + params.getModel().getNavBeginDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getNavEndDate())) {
			sql.append(" and a.nav_date <='" + params.getModel().getNavEndDate() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getProdRegEnc())) {
			sql.append(" and  T1.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getSonShareCode())) {
			sql.append(" and  T1.son_share_code like '%" + params.getModel().getSonShareCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getNavRegType())) {
			sql.append(" and  T1.nav_reg_type like '%" + params.getModel().getNavRegType() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getMotherFundCode())) {
			sql.append(" and  T1.mother_fund_code like '%" + params.getModel().getMotherFundCode() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getOpenType())) {
			sql.append(" and  T1.open_type like '%" + params.getModel().getOpenType() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getVolZeroFlag())) {
			sql.append(" and  T1.vol_zero_flag like '%" + params.getModel().getVolZeroFlag() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getPrfrBnchTyp())) {
			sql.append(" and  T1.prfr_bnch_typ like '%" + params.getModel().getPrfrBnchTyp() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and T1.register_status = '" + params.getModel().getRegisterStatus() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getFilter0Vol())) {
			sql.append(" and ifnull(T1.vol_zero_flag,'99') <> '" + params.getModel().getFilter0Vol() + "'");
		}
		sql.append(" and T1.sys_data_status in ('1','9')");
		return super.update(sql.toString(),
				DataSourceProperty.PUB,params.getModel());
	}

	public void updateBaseReportResultInfo(SqlParam<AppNavInfoReg> params) throws Exception {
		String sql="update base_report_result set register_date = theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_nav_info_reg' and theory_report_start_date in (select theory_report_start_date from app_nav_info_reg where report_date = $S{reportDate} ) ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}

	public UpdateResult addImportAppNavInfoReg(Object params) throws Exception {
		return super.update("INSERT INTO app_nav_info_reg(bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,data_date,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time) " +
						                             "VALUES($S{bankCode},$S{prodRegEnc},$S{navRegType},$S{sonShareCode},$S{cny},$D{nav},$D{rmbNav},$D{djNav},$D{totalNav},$D{rmbTotalNav},$D{fqNav},$D{rmbFqNav},$S{navCalType},$D{share},$S{navDate},$S{disclosureDate},$D{remainBal},$D{rmbRemainBal},$S{details},$S{registerDate},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),'0',$S{createDate},(select workday from sys_workday_set where pgmno='001' and workday<$S{reportDate} order by workday desc limit 1),$S{theoryReportEndDate},'1',$S{sysDataSource},'1.0',$S{reportDate},$S{dataDate},$S{crtUser},$S{updUser},date_format(CURDATE(),'%Y%m%d'),$S{updDt},date_format(now(),'%H%i%s'),$S{updTime})",
				params);
	}

	public UpdateResult deleteImportAppNavInfoReg(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_nav_info_reg WHERE  report_date=$I{reportDate} ",
				params);
	}

}
