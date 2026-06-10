package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.TerminationRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class TerminationRegistInfohDao extends ComnDao {

	public SqlResult<TerminationRegistInfoh> findTerminationRegistInfohs(SqlParam<TerminationRegistInfoh> params) throws Exception {
		String sql = "SELECT prod_code, bank_code, actual_prod_ter_date, interest_payment, register_serno, imp_date, register_date, register_status, realized_bank_income, payment, delivered_vol, in_custodian_fee, in_manage_fee, in_sales_commision, in_other_prod_fee, other_custodian_fee, other_manage_fee, other_sales_comm, consult_fee, other_prod_fee, annual_return_client, annual_return_prod, create_date, theory_report_start_date, theory_report_end_date FROM app_termination_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getActProdTerStartDate())) {
			sql += " and DATE(actual_prod_ter_date) >= DATE($S{actProdTerStartDate}) and DATE(actual_prod_ter_date) <= DATE($S{actProdTerEndDate})";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addTerminationRegistInfoh(SqlParam<TerminationRegistInfoh> params) throws Exception {
		return super.update("INSERT INTO app_termination_regist_info_h(prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod) VALUES($S{prodCode},$S{bankCode},$S{actualProdTerDate},$S{interestPayment},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{realizedBankIncome},$S{payment},$S{deliveredVol},$S{inCustodianFee},$S{inManageFee},$S{inSalesCommision},$S{inOtherProdFee},$S{otherCustodianFee},$S{otherManageFee},$S{otherSalesComm},$S{consultFee},$S{otherProdFee},$S{annualReturnClient},$S{annualReturnProd})",
				params.getModel());
	}
	
	public UpdateResult updateTerminationRegistInfoh(SqlParam<TerminationRegistInfoh> params) throws Exception {
		return super.update("UPDATE app_termination_regist_info_h SET prod_code=$S{prodCode} ,bank_code=$S{bankCode} ,actual_prod_ter_date=$S{actualProdTerDate} ,interest_payment=$S{interestPayment} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,realized_bank_income=$S{realizedBankIncome} ,payment=$S{payment} ,delivered_vol=$S{deliveredVol} ,in_custodian_fee=$S{inCustodianFee} ,in_manage_fee=$S{inManageFee} ,in_sales_commision=$S{inSalesCommision} ,in_other_prod_fee=$S{inOtherProdFee} ,other_custodian_fee=$S{otherCustodianFee} ,other_manage_fee=$S{otherManageFee} ,other_sales_comm=$S{otherSalesComm} ,consult_fee=$S{consultFee} ,other_prod_fee=$S{otherProdFee} ,annual_return_client=$S{annualReturnClient} ,annual_return_prod=$S{annualReturnProd}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteTerminationRegistInfoh(SqlParam<TerminationRegistInfoh> params) throws Exception {
		return super.update("DELETE FROM app_termination_regist_info_h WHERE ",
				params.getModel());
	}

}
