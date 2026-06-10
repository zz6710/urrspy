package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.TerminationRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class TerminationRegistDao extends ComnDao {

	public SqlResult<TerminationRegist> findTerminationRegists(SqlParam<TerminationRegist> params) throws Exception {
		String sql = "SELECT prod_code, bank_code, actual_prod_ter_date, interest_payment, register_serno, imp_date, register_date, register_status, realized_bank_income, payment, delivered_vol, in_custodian_fee, in_manage_fee, in_sales_commision, in_other_prod_fee, other_custodian_fee, other_manage_fee, other_sales_comm, consult_fee, other_prod_fee, annual_return_client, annual_return_prod, summit_user, create_date, create_time, op_type, id FROM app_termination_regist_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getTerStartDate())) {
			sql += " and DATE(actual_prod_ter_date) >= DATE($S{TerStartDate}) and DATE(actual_prod_ter_date) <= DATE($S{TerEndDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addTerminationRegist(TerminationRegist terminationRegist) throws Exception {
		return super.update("INSERT INTO app_termination_regist_remark(prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod,summit_user,create_date,create_time,op_type,report_date) VALUES($S{prodCode},$S{bankCode},$S{actualProdTerDate},$D{interestPayment},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{realizedBankIncome},$S{payment},$S{deliveredVol},$S{inCustodianFee},$D{inManageFee},$D{inSalesCommision},$S{inOtherProdFee},$D{otherCustodianFee},$S{otherManageFee},$S{otherSalesComm},$S{consultFee},$S{otherProdFee},$S{annualReturnClient},$D{annualReturnProd},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$S{reportDate})",
				DataSourceProperty.PUB,terminationRegist);
	}
	
	public UpdateResult updateTerminationRegist(SqlParam<TerminationRegist> params) throws Exception {
		return super.update("UPDATE app_termination_regist_remark SET prod_code=$S{prodCode} ,bank_code=$S{bankCode} ,actual_prod_ter_date=$S{actualProdTerDate} ,interest_payment=$D{interestPayment} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,realized_bank_income=$S{realizedBankIncome} ,payment=$S{payment} ,delivered_vol=$S{deliveredVol} ,in_custodian_fee=$S{inCustodianFee} ,in_manage_fee=$D{inManageFee} ,in_sales_commision=$D{inSalesCommision} ,in_other_prod_fee=$S{inOtherProdFee} ,other_custodian_fee=$D{otherCustodianFee} ,other_manage_fee=$S{otherManageFee} ,other_sales_comm=$S{otherSalesComm} ,consult_fee=$S{consultFee} ,other_prod_fee=$S{otherProdFee} ,annual_return_client=$S{annualReturnClient} ,annual_return_prod=$D{annualReturnProd} ,summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,op_type=$S{opType}  WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteTerminationRegist(SqlParam<TerminationRegist> params) throws Exception {
		return super.update("DELETE FROM app_termination_regist_remark WHERE ",
				DataSourceProperty.PUB,params.getModel());
	}

    public UpdateResult addImportTerminationRegist(TerminationRegist terminationRegist) throws Exception {
		return super.update("INSERT INTO app_termination_regist_remark(prod_code,bank_code,actual_prod_ter_date,interest_payment,register_serno,imp_date,register_date,register_status,realized_bank_income,payment,delivered_vol,in_custodian_fee,in_manage_fee,in_sales_commision,in_other_prod_fee,other_custodian_fee,other_manage_fee,other_sales_comm,consult_fee,other_prod_fee,annual_return_client,annual_return_prod,summit_user,create_date,create_time,op_type,report_date) VALUES($S{prodCode},$S{bankCode},$S{actualProdTerDate},$D{interestPayment},$S{registerSerno},date_format(CURDATE(),'%Y%m%d'),$S{registerDate},$S{registerStatus},$D{realizedBankIncome},$D{payment},$D{deliveredVol},$D{inCustodianFee},$D{inManageFee},$D{inSalesCommision},$D{inOtherProdFee},$D{otherCustodianFee},$D{otherManageFee},$D{otherSalesComm},$D{consultFee},$D{otherProdFee},$D{annualReturnClient},$D{annualReturnProd},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$S{reportDate})",
				DataSourceProperty.PUB,terminationRegist);
    }
}
