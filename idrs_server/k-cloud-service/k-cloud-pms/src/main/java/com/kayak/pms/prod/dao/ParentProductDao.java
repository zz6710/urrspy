package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.ParentProduct;
import org.springframework.stereotype.Repository;

@Repository
public class ParentProductDao extends ComnDao {

	public SqlResult<ParentProduct> findParentProducts(SqlParam<ParentProduct> params) throws Exception {
		return super.findRows("SELECT product_code,custodian_code,asset_code,fund_manager_code,product_type,product_name,currency_type,cash_flag,face_value,issue_price,perfor_compare_norm,ipo_type,ipo_start_date,ipo_end_date,allot_client_type,large_redeem_radio,product_lifecycle_status,transaction_status,risk_level,workday_suite_code,establish_date,due_date,auto_due,clean_date,open_type,target_product_type,product_regist_code,product_days,self_support_flag,first_min_amount_individual,first_min_amount_institution,first_min_amount_interbank,open_cycle_type,open_cycle_index,first_open_day,start_from_last,have_holiday,netvalue,netvalue_date,confirm_days,perfor_compare_norm_text,update_date,operator_no,checker_no FROM a_fp_p_parent_product", params);
	}

	public UpdateResult addParentProduct(SqlParam<ParentProduct> params) throws Exception {
		return super.update("INSERT INTO a_fp_p_parent_product(product_code,custodian_code,asset_code,fund_manager_code,product_type,product_name,currency_type,cash_flag,face_value,issue_price,perfor_compare_norm,ipo_type,ipo_start_date,ipo_end_date,allot_client_type,large_redeem_radio,product_lifecycle_status,transaction_status,risk_level,workday_suite_code,establish_date,due_date,auto_due,clean_date,open_type,target_product_type,product_regist_code,product_days,self_support_flag,first_min_amount_individual,first_min_amount_institution,first_min_amount_interbank,open_cycle_type,open_cycle_index,first_open_day,start_from_last,have_holiday,netvalue,netvalue_date,confirm_days,perfor_compare_norm_text,update_date,operator_no,checker_no) VALUES($S{productCode},$S{custodianCode},$S{assetCode},$S{fundManagerCode},$S{productType},$S{productName},$S{currencyType},$S{cashFlag},$S{faceValue},$S{issuePrice},$S{perforCompareNorm},$S{ipoType},$S{ipoStartDate},$S{ipoEndDate},$S{allotClientType},$S{largeRedeemRadio},$S{productLifecycleStatus},$S{transactionStatus},$S{riskLevel},$S{workdaySuiteCode},$S{establishDate},$S{dueDate},$S{autoDue},$S{cleanDate},$S{openType},$S{targetProductType},$S{productRegistCode},$S{productDays},$S{selfSupportFlag},$S{firstMinAmountIndividual},$S{firstMinAmountInstitution},$S{firstMinAmountInterbank},$S{openCycleType},$S{openCycleIndex},$S{firstOpenDay},$S{startFromLast},$S{haveHoliday},$S{netvalue},$S{netvalueDate},$S{confirmDays},$S{perforCompareNormText},$S{updateDate},$S{operatorNo},$S{checkerNo})",
				params.getModel());
	}
	
	public UpdateResult updateParentProduct(SqlParam<ParentProduct> params) throws Exception {
		return super.update("UPDATE a_fp_p_parent_product SET product_code=$S{productCode} ,custodian_code=$S{custodianCode} ,asset_code=$S{assetCode} ,fund_manager_code=$S{fundManagerCode} ,product_type=$S{productType} ,product_name=$S{productName} ,currency_type=$S{currencyType} ,cash_flag=$S{cashFlag} ,face_value=$S{faceValue} ,issue_price=$S{issuePrice} ,perfor_compare_norm=$S{perforCompareNorm} ,ipo_type=$S{ipoType} ,ipo_start_date=$S{ipoStartDate} ,ipo_end_date=$S{ipoEndDate} ,allot_client_type=$S{allotClientType} ,large_redeem_radio=$S{largeRedeemRadio} ,product_lifecycle_status=$S{productLifecycleStatus} ,transaction_status=$S{transactionStatus} ,risk_level=$S{riskLevel} ,workday_suite_code=$S{workdaySuiteCode} ,establish_date=$S{establishDate} ,due_date=$S{dueDate} ,auto_due=$S{autoDue} ,clean_date=$S{cleanDate} ,open_type=$S{openType} ,target_product_type=$S{targetProductType} ,product_regist_code=$S{productRegistCode} ,product_days=$S{productDays} ,self_support_flag=$S{selfSupportFlag} ,first_min_amount_individual=$S{firstMinAmountIndividual} ,first_min_amount_institution=$S{firstMinAmountInstitution} ,first_min_amount_interbank=$S{firstMinAmountInterbank} ,open_cycle_type=$S{openCycleType} ,open_cycle_index=$S{openCycleIndex} ,first_open_day=$S{firstOpenDay} ,start_from_last=$S{startFromLast} ,have_holiday=$S{haveHoliday} ,netvalue=$S{netvalue} ,netvalue_date=$S{netvalueDate} ,confirm_days=$S{confirmDays} ,perfor_compare_norm_text=$S{perforCompareNormText} ,update_date=$S{updateDate} ,operator_no=$S{operatorNo} ,checker_no=$S{checkerNo}  WHERE ",
				params.getModel());
	}
	
	public UpdateResult deleteParentProduct(SqlParam<ParentProduct> params) throws Exception {
		return super.update("DELETE FROM a_fp_p_parent_product WHERE ",
				params.getModel());
	}

}
