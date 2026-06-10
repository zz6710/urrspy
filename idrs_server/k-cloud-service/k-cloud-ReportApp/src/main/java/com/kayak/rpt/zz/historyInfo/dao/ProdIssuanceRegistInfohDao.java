package com.kayak.rpt.zz.historyInfo.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.historyInfo.model.ProdIssuanceRegistInfoh;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class ProdIssuanceRegistInfohDao extends ComnDao {

	public SqlResult<ProdIssuanceRegistInfoh> findProdIssuanceRegistInfohs(SqlParam<ProdIssuanceRegistInfoh> params) throws Exception {
		String sql = "SELECT prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name,create_date,theory_report_start_date,theory_report_end_date,clsf_sto,regular_open_period_day FROM app_prod_issuance_regist_info_h where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(register_Date) >= DATE($S{startDate}) and DATE(register_Date) <= DATE($S{endDate})";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addProdIssuanceRegistInfoh(SqlParam<ProdIssuanceRegistInfoh> params) throws Exception {
		return super.update("INSERT INTO app_prod_issuance_regist_info_h(prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name) VALUES($S{prodCode},$S{bankCode},$S{prodIdentCode},$S{subscriptionStartDate},$S{subscriptionEndDate},$S{prodValueDate},$S{prodMaturityDate},$S{managementMethod},$S{structuredProd},$S{detailsPerRate},$S{openingMode},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$S{upLimitPerRate},$S{lowLimitPerRate},$S{regularOpenPeriod},$S{otherOpenPeriod},$S{disorderOpenPeriod},$S{firstOpenDay},$S{holidayOpenType},$S{averageOpenNo},$S{busiOpenPeriod},$S{detailsBusiOpPeriod},$S{custodyAcctNo},$S{custodyAcctName})",
				params.getModel());
	}
	
	public UpdateResult updateProdIssuanceRegistInfoh(SqlParam<ProdIssuanceRegistInfoh> params) throws Exception {
		return super.update("UPDATE app_prod_issuance_regist_info_h SET prod_code=$S{prodCode} ,bank_code=$S{bankCode} ,prod_ident_code=$S{prodIdentCode} ,subscription_start_date=$S{subscriptionStartDate} ,subscription_end_date=$S{subscriptionEndDate} ,prod_value_date=$S{prodValueDate} ,prod_maturity_date=$S{prodMaturityDate} ,management_method=$S{managementMethod} ,structured_prod=$S{structuredProd} ,details_per_rate=$S{detailsPerRate} ,opening_mode=$S{openingMode} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,up_limit_per_rate=$S{upLimitPerRate} ,low_limit_per_rate=$S{lowLimitPerRate} ,regular_open_period=$S{regularOpenPeriod} ,other_open_period=$S{otherOpenPeriod} ,disorder_open_period=$S{disorderOpenPeriod} ,first_open_day=$S{firstOpenDay} ,holiday_open_type=$S{holidayOpenType} ,average_open_no=$S{averageOpenNo} ,busi_open_period=$S{busiOpenPeriod} ,details_busi_op_period=$S{detailsBusiOpPeriod} ,custody_acct_no=$S{custodyAcctNo} ,custody_acct_name=$S{custodyAcctName}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteProdIssuanceRegistInfoh(SqlParam<ProdIssuanceRegistInfoh> params) throws Exception {
		return super.update("DELETE FROM app_prod_issuance_regist_info_h WHERE ",
				params.getModel());
	}

}
