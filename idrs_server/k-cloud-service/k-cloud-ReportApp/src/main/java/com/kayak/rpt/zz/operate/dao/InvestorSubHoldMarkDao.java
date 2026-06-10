package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.InvestorSubHoldMark;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class InvestorSubHoldMarkDao extends ComnDao {

	public SqlResult<InvestorSubHoldMark> findInvestorSubHoldMarks(SqlParam<InvestorSubHoldMark> params) throws Exception {
		String sql = "select b.id, b.op_user, b.op_date, b.op_time, b.op_type, b.bank_code, b.prod_code, b.prod_code_m, b.prod_code_s, b.cust_no, b.hold_date, b.cur, b.hold_vol, b.hold_amt,\n" +
				"       b.convert_rmb, b.imp_date, b.register_date, b.register_status, b.register_serno, b.create_date, b.theory_report_start_date, b.theory_report_end_date, b.report_date, \n" +
				"       b.TA_ID, c.cust_type, c.channel_code, c.personal_id_type, c.organization_id_type, c.other_id_name, c.id_code \n" +
				"  from app_cust_vol_register_sub_remark b \n" +
				"  left join ods_cust_base_inf c on b.cust_no = c.cust_no " +
				" where 1 = 1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(op_date) >= DATE($S{startDate}) and DATE(op_date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getHoldStartDate())) {
			sql += " and DATE(hold_date) >= DATE($S{holdStartDate}) and DATE(hold_date) <= DATE($S{holdEndDate})";
		}
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	public UpdateResult addInvestorSubHoldMark(InvestorSubHoldMark params) throws Exception {
		return super.update("INSERT INTO app_cust_vol_register_sub_remark(op_user,op_date,op_time,op_type,bank_code,prod_code,prod_code_m,prod_code_s,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,theory_report_end_date,report_date,ta_id) VALUES($S{opUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$S{bankCode},$S{prodCode},$S{prodCodeM},$S{prodCodeS},$S{custNo},$S{holdDate},$S{cur},$D{holdVol},$D{holdAmt},$D{convertRmb},$S{impDate},$S{registerDate},'0',$S{registerSerno},$S{createDate},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{reportDate},$S{taId})",
				DataSourceProperty.PUB,params);
	}
	
	public UpdateResult updateInvestorSubHoldMark(SqlParam<InvestorSubHoldMark> params) throws Exception {
		return super.update("UPDATE app_cust_vol_register_sub_remark SET op_user=$S{opUser} ,op_date=$S{opDate} ,op_time=$S{opTime} ,op_type=$S{opType} ,bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,prod_code_m=$S{prodCodeM} ,prod_code_s=$S{prodCodeS} ,cust_no=$S{custNo} ,hold_date=$S{holdDate} ,cur=$S{cur} ,hold_vol=$D{holdVol} ,hold_amt=$D{holdAmt} ,convert_rmb=$D{convertRmb} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,register_serno=$S{registerSerno} ,create_date=$S{createDate} ,theory_report_start_date=$S{theoryReportStartDate} ,theory_report_end_date=$S{theoryReportEndDate} ,report_date=$S{reportDate} ,ta_id=$S{taId}  WHERE  id=$I{id} ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteInvestorSubHoldMark(SqlParam<InvestorSubHoldMark> params) throws Exception {
		return super.update("DELETE FROM app_cust_vol_register_sub_remark WHERE  id=$I{id} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportCustVolRegister(InvestorSubHoldMark investorSubHoldMark) throws Exception {
		return super.update("INSERT INTO app_cust_vol_register_sub_remark(op_user,op_date,op_time,op_type,bank_code,prod_code,prod_code_m,prod_code_s,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,create_date,theory_report_start_date,theory_report_end_date,report_date,ta_id) VALUES($S{opUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$S{bankCode},$S{prodCode},$S{prodCodeM},$S{prodCodeS},$S{custNo},$S{holdDate},$S{cur},$D{holdVol},$D{holdAmt},$D{convertRmb},$S{impDate},$S{registerDate},$S{registerStatus},$S{registerSerno},$S{createDate},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{reportDate},$S{taId})",
				DataSourceProperty.PUB, investorSubHoldMark);
	}

}
