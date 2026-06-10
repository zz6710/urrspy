package com.kayak.rpt.zz.errorInfo.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.errorInfo.model.ProdIssRgInfoErr;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ProdIssRgInfoErrDao extends ComnDao {

	public SqlResult<ProdIssRgInfoErr> findProdIssRgInfoErrs(SqlParam<ProdIssRgInfoErr> params) throws Exception {
		String sql = "SELECT REGISTER_SERNO, IMP_DATE, REGISTER_DATE, REGISTER_STATUS, PROD_CODE_DESC, BANK_CODE_DESC, PROD_IDENT_CODE_DESC, SUBSCRIPTION_START_DATE_DESC, SUBSCRIPTION_END_DATE_DESC, PROD_VALUE_DATE_DESC, PROD_MATURITY_DATE_DESC, MANAGEMENT_METHOD_DESC, STRUCTURED_PROD_DESC, DETAILS_PER_RATE_DESC, OPENING_MODE_DESC, UP_LIMIT_PER_RATE_DESC, LOW_LIMIT_PER_RATE_DESC, REGULAR_OPEN_PERIOD_DESC, OTHER_OPEN_PERIOD_DESC, DISORDER_OPEN_PERIOD_DESC, FIRST_OPEN_DAY_DESC, HOLIDAY_OPEN_TYPE_DESC, AVERAGE_OPEN_NO_DESC, BUSI_OPEN_PERIOD_DESC, DETAILS_BUSI_OP_PERIOD_DESC, CUSTODY_ACCT_NO_DESC, CUSTODY_ACCT_NAME_DESC, create_date, theory_report_start_date, theory_report_end_date, id, report_date\n" +
				"FROM app_prod_issuance_regist_info_erdesc  where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getImpDate())) {
			sql += " and (DATE(imp_date) >= DATE($S{startDate}) or DATE(imp_date) <= DATE($S{endDate}))";
		}

		return super.findRows(sql, params);
	}

	public UpdateResult addProdIssRgInfoErr(SqlParam<ProdIssRgInfoErr> params) throws Exception {
		return super.update("INSERT INTO app_prod_issuance_regist_info_erdesc(register_serno,imp_date,register_date,register_status,prod_code_desc,bank_code_desc,prod_ident_code_desc,subscription_start_date_desc,subscription_end_date_desc,prod_value_date_desc,prod_maturity_date_desc,management_method_desc,structured_prod_desc,details_per_rate_desc,opening_mode_desc,up_limit_per_rate_desc,low_limit_per_rate_desc,regular_open_period_desc,other_open_period_desc,disorder_open_period_desc,first_open_day_desc,holiday_open_type_desc,average_open_no_desc,busi_open_period_desc,details_busi_op_period_desc,custody_acct_no_desc,custody_acct_name_desc) VALUES($S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{prodCodeDesc},$S{bankCodeDesc},$S{prodIdentCodeDesc},$S{subscriptionStartDateDesc},$S{subscriptionEndDateDesc},$S{prodValueDateDesc},$S{prodMaturityDateDesc},$S{managementMethodDesc},$S{structuredProdDesc},$S{detailsPerRateDesc},$S{openingModeDesc},$S{upLimitPerRateDesc},$S{lowLimitPerRateDesc},$S{regularOpenPeriodDesc},$S{otherOpenPeriodDesc},$S{disorderOpenPeriodDesc},$S{firstOpenDayDesc},$S{holidayOpenTypeDesc},$S{averageOpenNoDesc},$S{busiOpenPeriodDesc},$S{detailsBusiOpPeriodDesc},$S{custodyAcctNoDesc},$S{custodyAcctNameDesc})",
				params.getModel());
	}
	
	public UpdateResult updateProdIssRgInfoErr(SqlParam<ProdIssRgInfoErr> params) throws Exception {
		return super.update("UPDATE app_prod_issuance_regist_info_erdesc SET register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,prod_code_desc=$S{prodCodeDesc} ,bank_code_desc=$S{bankCodeDesc} ,prod_ident_code_desc=$S{prodIdentCodeDesc} ,subscription_start_date_desc=$S{subscriptionStartDateDesc} ,subscription_end_date_desc=$S{subscriptionEndDateDesc} ,prod_value_date_desc=$S{prodValueDateDesc} ,prod_maturity_date_desc=$S{prodMaturityDateDesc} ,management_method_desc=$S{managementMethodDesc} ,structured_prod_desc=$S{structuredProdDesc} ,details_per_rate_desc=$S{detailsPerRateDesc} ,opening_mode_desc=$S{openingModeDesc} ,up_limit_per_rate_desc=$S{upLimitPerRateDesc} ,low_limit_per_rate_desc=$S{lowLimitPerRateDesc} ,regular_open_period_desc=$S{regularOpenPeriodDesc} ,other_open_period_desc=$S{otherOpenPeriodDesc} ,disorder_open_period_desc=$S{disorderOpenPeriodDesc} ,first_open_day_desc=$S{firstOpenDayDesc} ,holiday_open_type_desc=$S{holidayOpenTypeDesc} ,average_open_no_desc=$S{averageOpenNoDesc} ,busi_open_period_desc=$S{busiOpenPeriodDesc} ,details_busi_op_period_desc=$S{detailsBusiOpPeriodDesc} ,custody_acct_no_desc=$S{custodyAcctNoDesc} ,custody_acct_name_desc=$S{custodyAcctNameDesc}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteProdIssRgInfoErr(SqlParam<ProdIssRgInfoErr> params) throws Exception {
		return super.update("DELETE FROM app_prod_issuance_regist_info_erdesc WHERE ",
				params.getModel());
	}

}
