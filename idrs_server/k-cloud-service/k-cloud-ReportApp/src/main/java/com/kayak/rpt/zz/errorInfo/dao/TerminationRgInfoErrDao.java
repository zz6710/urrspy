package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.TerminationRgInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class TerminationRgInfoErrDao extends ComnDao {

	public SqlResult<TerminationRgInfoErr> findTerminationRgInfos(SqlParam<TerminationRgInfoErr> params) throws Exception {
		String sql = "SELECT prod_code_desc, bank_code_desc, actual_prod_ter_date_desc, realized_bank_income_desc, interest_payment_desc, payment_desc, delivered_vol_desc, in_custodian_fee_desc, in_manage_fee_desc, in_sales_commision_desc, in_other_prod_fee_desc, other_custodian_fee_desc, other_manage_fee_desc, other_sales_comm_desc, consult_fee_desc, other_prod_fee_desc, annual_return_client_desc, annual_return_prod_desc, register_serno, imp_date, create_date, theory_report_start_date, theory_report_end_date, id, report_date\n" +
				"FROM app_termination_regist_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addTerminationRgInfo(SqlParam<TerminationRgInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_termination_regist_info_erdesc(prod_code_desc,bank_code_desc,actual_prod_ter_date_desc,realized_bank_income_desc,interest_payment_desc,payment_desc,delivered_vol_desc,in_custodian_fee_desc,in_manage_fee_desc,in_sales_commision_desc,in_other_prod_fee_desc,other_custodian_fee_desc,other_manage_fee_desc,other_sales_comm_desc,consult_fee_desc,other_prod_fee_desc,annual_return_client_desc,annual_return_prod_desc,register_serno,imp_date) VALUES($S{prodCodeDesc},$S{bankCodeDesc},$S{actualProdTerDateDesc},$S{realizedBankIncomeDesc},$S{interestPaymentDesc},$S{paymentDesc},$S{deliveredVolDesc},$S{inCustodianFeeDesc},$S{inManageFeeDesc},$S{inSalesCommisionDesc},$S{inOtherProdFeeDesc},$S{otherCustodianFeeDesc},$S{otherManageFeeDesc},$S{otherSalesCommDesc},$S{consultFeeDesc},$S{otherProdFeeDesc},$S{annualReturnClientDesc},$S{annualReturnProdDesc},$S{registerSerno},$S{impDate})",
				params.getModel());
	}
	
	public UpdateResult updateTerminationRgInfo(SqlParam<TerminationRgInfoErr> params) throws Exception {
		return super.update("UPDATE app_termination_regist_info_erdesc SET prod_code_desc=$S{prodCodeDesc} ,bank_code_desc=$S{bankCodeDesc} ,actual_prod_ter_date_desc=$S{actualProdTerDateDesc} ,realized_bank_income_desc=$S{realizedBankIncomeDesc} ,interest_payment_desc=$S{interestPaymentDesc} ,payment_desc=$S{paymentDesc} ,delivered_vol_desc=$S{deliveredVolDesc} ,in_custodian_fee_desc=$S{inCustodianFeeDesc} ,in_manage_fee_desc=$S{inManageFeeDesc} ,in_sales_commision_desc=$S{inSalesCommisionDesc} ,in_other_prod_fee_desc=$S{inOtherProdFeeDesc} ,other_custodian_fee_desc=$S{otherCustodianFeeDesc} ,other_manage_fee_desc=$S{otherManageFeeDesc} ,other_sales_comm_desc=$S{otherSalesCommDesc} ,consult_fee_desc=$S{consultFeeDesc} ,other_prod_fee_desc=$S{otherProdFeeDesc} ,annual_return_client_desc=$S{annualReturnClientDesc} ,annual_return_prod_desc=$S{annualReturnProdDesc} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteTerminationRgInfo(SqlParam<TerminationRgInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_termination_regist_info_erdesc WHERE ",
				params.getModel());
	}

}
