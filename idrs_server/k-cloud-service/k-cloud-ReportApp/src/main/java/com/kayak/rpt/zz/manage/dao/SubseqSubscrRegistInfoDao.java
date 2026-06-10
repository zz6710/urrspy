package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.SubseqSubscrRegistInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.Map;

@Repository
public class SubseqSubscrRegistInfoDao extends ComnDao {

	public SqlResult<SubseqSubscrRegistInfo> findSubseqSubscrRegistInfos(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		String sql = "SELECT T1.*,ifnull(ARS.audit_status,0) audit_status " +
				"       FROM app_subseq_subscr_regist_info T1 " +
				"       LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_subseq_subscr_regist_info' " +
				"      where sys_data_status ='1'";
		if (StringUtils.isNotBlank(params.getModel().getReportDateStart()) && StringUtils.isNotBlank(params.getModel().getReportDateEnd())) {
			sql = sql + " and DATE(T1.theory_report_start_date) >= "+params.getModel().getReportDateStart()+" and DATE(T1.theory_report_start_date) <= "+params.getModel().getReportDateEnd();
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and T1.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getStartDate1()) && StringUtils.isNotBlank(params.getModel().getEndDate1())) {
			sql = sql + "  and DATE(business_start_date) >= "+params.getModel().getStartDate1()+" and DATE(business_start_date) <= "+params.getModel().getEndDate1();
		}
		if (StringUtils.isNotBlank(params.getModel().getStartDate2()) && StringUtils.isNotBlank(params.getModel().getEndDate2())) {
			sql = sql + "  and DATE(business_end_date) >= "+params.getModel().getStartDate2()+" and DATE(business_end_date) <= "+params.getModel().getEndDate2();
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + "  and  register_serno = $S{registerSerno}";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public int findSubseqSubscrRegistInfosCount(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		String sql = "SELECT count(1) FROM app_subseq_subscr_regist_info T1 " +
				"       LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_subseq_subscr_regist_info' " +
				"      where sys_data_status ='1'";
		if (StringUtils.isNotBlank(params.getModel().getReportDateStart()) && StringUtils.isNotBlank(params.getModel().getReportDateEnd())) {
			sql = sql + " and DATE(T1.report_date) >= "+params.getModel().getReportDateStart()+" and DATE(T1.report_date) <= "+params.getModel().getReportDateEnd();
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and T1.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getStartDate1()) && StringUtils.isNotBlank(params.getModel().getEndDate1())) {
			sql = sql + "  and DATE(business_start_date) >= "+params.getModel().getStartDate1()+" and DATE(business_start_date) <= "+params.getModel().getEndDate1();
		}
		if (StringUtils.isNotBlank(params.getModel().getStartDate2()) && StringUtils.isNotBlank(params.getModel().getEndDate2())) {
			sql = sql + "  and DATE(business_end_date) >= "+params.getModel().getStartDate2()+" and DATE(business_end_date) <= "+params.getModel().getEndDate2();
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + "  and  register_serno = $S{registerSerno}";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public int findSubseqSubscrRegistInfoFailStatus(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		String sql = "SELECT count(1) FROM app_subseq_subscr_regist_info T1 " +
				"LEFT JOIN base_report_data_audit_results ARS ON T1.report_date = ARS.report_date AND ARS.table_id = 'app_subseq_subscr_regist_info' " +
				"where sys_data_status ='1' " +
				" and T1.register_status in (0,1) ";
		if (StringUtils.isNotBlank(params.getModel().getReportDateStart()) && StringUtils.isNotBlank(params.getModel().getReportDateEnd())) {
			sql = sql + " and DATE(T1.report_date) >= "+params.getModel().getReportDateStart()+" and DATE(T1.report_date) <= "+params.getModel().getReportDateEnd();
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and T1.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getStartDate1()) && StringUtils.isNotBlank(params.getModel().getEndDate1())) {
			sql = sql + "  and DATE(business_start_date) >= "+params.getModel().getStartDate1()+" and DATE(business_start_date) <= "+params.getModel().getEndDate1();
		}
		if (StringUtils.isNotBlank(params.getModel().getStartDate2()) && StringUtils.isNotBlank(params.getModel().getEndDate2())) {
			sql = sql + "  and DATE(business_end_date) >= "+params.getModel().getStartDate2()+" and DATE(business_end_date) <= "+params.getModel().getEndDate2();
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + "  and  register_serno = $S{registerSerno}";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public UpdateResult addSubseqSubscrRegistInfo(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		return super.update("INSERT INTO app_subseq_subscr_regist_info(bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav,nav_dt,report_date,ccy_and_pch_rdm) VALUES($S{bankCode},$S{prodCode},$D{initialNav},$D{nav},$D{aggregateNav},$S{navCur},$D{convertRmbNav},$D{convertRmbAggNav},$D{realizedAnnualReturn},$D{expectedAnnualReturn},$D{inconmeBank},$S{businessStartDate},$S{businessEndDate},$D{subscribedLatestVol},$D{redeemedLatestVol},$D{unitsBonus},$D{cashBonus},$D{prodAmt},$D{prodVol},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$D{convertInitialNav},$S{navDt},date_format(CURDATE(),'%Y%m%d'),$S{ccyAndPchRdm})",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateSubseqSubscrRegistInfo(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		return super.update("UPDATE app_subseq_subscr_regist_info SET bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,initial_nav=$D{initialNav} ,nav=$D{nav} ,aggregate_nav=$D{aggregateNav} ,nav_cur=$S{navCur} ,convert_rmb_nav=$D{convertRmbNav} ,convert_rmb_agg_nav=$D{convertRmbAggNav} ,realized_annual_return=$D{realizedAnnualReturn} ,expected_annual_return=$D{expectedAnnualReturn} ,inconme_bank=$D{inconmeBank} ,business_start_date=$S{businessStartDate} ,business_end_date=$S{businessEndDate} ,subscribed_latest_vol=$D{subscribedLatestVol} ,redeemed_latest_vol=$D{redeemedLatestVol} ,units_bonus=$D{unitsBonus} ,cash_bonus=$D{cashBonus} ,prod_amt=$D{prodAmt} ,prod_vol=$D{prodVol} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,convert_initial_nav=$D{convertInitialNav},ccy_and_pch_rdm=$S{ccyAndPchRdm},nav_dt=$S{navDt}  WHERE register_serno=$S{registerSerno} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateSubseqSubscrRegistInfoStatus(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		String sql = "UPDATE app_subseq_subscr_regist_info SET register_status='3' WHERE sys_data_status='1' ";
		if (StringUtils.isNotBlank(params.getModel().getReportDateStart()) && StringUtils.isNotBlank(params.getModel().getReportDateEnd())) {
			sql = sql + " and DATE(report_date) >= "+params.getModel().getReportDateStart()+" and DATE(report_date) <= "+params.getModel().getReportDateEnd();
		}
		if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
			sql = sql + " and prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getStartDate1()) && StringUtils.isNotBlank(params.getModel().getEndDate1())) {
			sql = sql + "  and DATE(business_start_date) >= "+params.getModel().getStartDate1()+" and DATE(business_start_date) <= "+params.getModel().getEndDate1();
		}
		if (StringUtils.isNotBlank(params.getModel().getStartDate2()) && StringUtils.isNotBlank(params.getModel().getEndDate2())) {
			sql = sql + "  and DATE(business_end_date) >= "+params.getModel().getStartDate2()+" and DATE(business_end_date) <= "+params.getModel().getEndDate2();
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + "  and register_serno = $S{registerSerno}";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		return super.update(sql,
				DataSourceProperty.PUB,params.getModel());
	}

	public void updateBaseReportResultInfo(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		String sql="update base_report_result set register_date = theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_subseq_subscr_regist_info' and theory_report_start_date in (select theory_report_start_date from app_subseq_subscr_regist_info where report_date = $S{reportDate}) ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}

	public UpdateResult deleteSubseqSubscrRegistInfo(SqlParam<SubseqSubscrRegistInfo> params) throws Exception {
		return super.update("DELETE FROM app_subseq_subscr_regist_info WHERE register_serno=$S{registerSerno}",
				DataSourceProperty.PUB,params.getModel());
	}

    public UpdateResult addImportSubseqSubscrRegistInfo(Object param) throws Exception {
		return super.update("INSERT INTO app_subseq_subscr_regist_info(bank_code,prod_code,initial_nav,nav,aggregate_nav,nav_cur,convert_rmb_nav,convert_rmb_agg_nav,realized_annual_return,expected_annual_return,inconme_bank,business_start_date,business_end_date,subscribed_latest_vol,redeemed_latest_vol,units_bonus,cash_bonus,prod_amt,prod_vol,details,register_serno,imp_date,register_date,register_status,convert_initial_nav,nav_dt,report_date,ccy_and_pch_rdm,sys_data_status,theory_report_start_date) VALUES($S{bankCode},$S{prodCode},$D{initialNav},$D{nav},$D{aggregateNav},$S{navCur},$D{convertRmbNav},$D{convertRmbAggNav},$D{realizedAnnualReturn},$D{expectedAnnualReturn},$D{inconmeBank},$S{businessStartDate},$S{businessEndDate},$D{subscribedLatestVol},$D{redeemedLatestVol},$D{unitsBonus},$D{cashBonus},$D{prodAmt},$D{prodVol},$S{details},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',$D{convertInitialNav},$S{navDt},$S{reportDate},$S{ccyAndPchRdm},'1',$S{reportDate})",
				DataSourceProperty.PUB,param);
    }

    public UpdateResult deleteImportSubseqSubscrRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_subseq_subscr_regist_info where register_serno=$S{registerSerno} ", params);
    }
}
