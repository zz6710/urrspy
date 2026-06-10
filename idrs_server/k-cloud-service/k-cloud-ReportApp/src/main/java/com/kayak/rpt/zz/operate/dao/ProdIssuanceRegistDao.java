package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.ProdIssuanceRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ProdIssuanceRegistDao extends ComnDao {

	public SqlResult<ProdIssuanceRegist> findProdIssuanceRegists(SqlParam<ProdIssuanceRegist> params) throws Exception {
		String sql = "SELECT PROD_CODE, BANK_CODE, PROD_IDENT_CODE, SUBSCRIPTION_START_DATE, SUBSCRIPTION_END_DATE, PROD_VALUE_DATE, PROD_MATURITY_DATE, MANAGEMENT_METHOD, STRUCTURED_PROD, DETAILS_PER_RATE, OPENING_MODE, REGISTER_SERNO, IMP_DATE, REGISTER_DATE, REGISTER_STATUS, UP_LIMIT_PER_RATE, LOW_LIMIT_PER_RATE, REGULAR_OPEN_PERIOD, regular_open_period_day, OTHER_OPEN_PERIOD, DISORDER_OPEN_PERIOD, FIRST_OPEN_DAY, HOLIDAY_OPEN_TYPE, AVERAGE_OPEN_NO, BUSI_OPEN_PERIOD, DETAILS_BUSI_OP_PERIOD, CUSTODY_ACCT_NO, CUSTODY_ACCT_NAME, SUMMIT_USER, CREATE_DATE, CREATE_TIME, OP_TYPE, ID, CLSF_STO FROM app_prod_issuance_regist_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql, DataSourceProperty.PUB, params);
		}

	public UpdateResult addProdIssuanceRegist(ProdIssuanceRegist prodIssuanceRegist) throws Exception {
		return super.update("INSERT INTO app_prod_issuance_regist_remark(prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,regular_open_period_day,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name,summit_user,create_date,create_time,op_type,clsf_sto,report_date) VALUES($S{prodCode},$S{bankCode},$S{prodIdentCode},$S{subscriptionStartDate},$S{subscriptionEndDate},$S{prodValueDate},$S{prodMaturityDate},$S{managementMethod},$S{structuredProd},$S{detailsPerRate},$S{openingMode},$S{registerSerno},$S{impDate},$S{registerDate},$S{registerStatus},$D{upLimitPerRate},$D{lowLimitPerRate},$S{regularOpenPeriod},$D{regularOpenPeriodDay},$D{otherOpenPeriod},$S{disorderOpenPeriod},$S{firstOpenDay},$S{holidayOpenType},$D{averageOpenNo},$S{busiOpenPeriod},$S{detailsBusiOpPeriod},$S{custodyAcctNo},$S{custodyAcctName},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$D{clsfSto},$S{reportDate})",
				DataSourceProperty.PUB,prodIssuanceRegist);
	}
	
	public UpdateResult updateProdIssuanceRegist(SqlParam<ProdIssuanceRegist> params) throws Exception {
		return super.update("UPDATE app_prod_issuance_regist_remark SET bank_code=$S{bankCode} ,prod_ident_code=$S{prodIdentCode} ,subscription_start_date=$S{subscriptionStartDate} ,subscription_end_date=$S{subscriptionEndDate} ,prod_value_date=$S{prodValueDate} ,prod_maturity_date=$S{prodMaturityDate} ,management_method=$S{managementMethod} ,structured_prod=$S{structuredProd} ,details_per_rate=$S{detailsPerRate} ,opening_mode=$S{openingMode} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,up_limit_per_rate=$D{upLimitPerRate} ,low_limit_per_rate=$D{lowLimitPerRate} ,regular_open_period=$S{regularOpenPeriod} ,regular_open_period_day=$D{regularOpenPeriodDay},other_open_period=$D{otherOpenPeriod} ,disorder_open_period=$S{disorderOpenPeriod} ,first_open_day=$S{firstOpenDay} ,holiday_open_type=$S{holidayOpenType} ,average_open_no=$D{averageOpenNo} ,busi_open_period=$S{busiOpenPeriod} ,details_busi_op_period=$S{detailsBusiOpPeriod} ,custody_acct_no=$S{custodyAcctNo} ,custody_acct_name=$S{custodyAcctName} ,summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,op_type=$S{opType},clsf_sto=$D{clsfSto} WHERE  prod_code=$S{prodCode} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult deleteProdIssuanceRegist(SqlParam<ProdIssuanceRegist> params) throws Exception {
		return super.update("DELETE FROM app_prod_issuance_regist_remark WHERE  prod_code=$S{prodCode} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportProdIssuanceRegist(ProdIssuanceRegist prodIssuanceRegist) throws Exception {
		return super.update("INSERT INTO app_prod_issuance_regist_remark(prod_code,bank_code,prod_ident_code,subscription_start_date,subscription_end_date,prod_value_date,prod_maturity_date,management_method,structured_prod,details_per_rate,opening_mode,register_serno,imp_date,register_date,register_status,up_limit_per_rate,low_limit_per_rate,regular_open_period,regular_open_period_day,other_open_period,disorder_open_period,first_open_day,holiday_open_type,average_open_no,busi_open_period,details_busi_op_period,custody_acct_no,custody_acct_name,summit_user,create_date,create_time,op_type,clsf_sto,report_date) VALUES($S{prodCode},$S{bankCode},$S{prodIdentCode},$S{subscriptionStartDate},$S{subscriptionEndDate},$S{prodValueDate},$S{prodMaturityDate},$S{managementMethod},$S{structuredProd},$S{detailsPerRate},$S{openingMode},$S{registerSerno},date_format(CURDATE(),'%Y%m%d'),$S{registerDate},$S{registerStatus},$D{upLimitPerRate},$D{lowLimitPerRate},$S{regularOpenPeriod},$D{regularOpenPeriodDay},$D{otherOpenPeriod},$S{disorderOpenPeriod},$S{firstOpenDay},$S{holidayOpenType},$D{averageOpenNo},$S{busiOpenPeriod},$S{detailsBusiOpPeriod},$S{custodyAcctNo},$S{custodyAcctName},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$D{clsfSto},$S{reportDate})",
				DataSourceProperty.PUB,prodIssuanceRegist);
	}
}
