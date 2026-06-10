package com.kayak.dps.app.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.AppBottomDeal;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class AppBottomDealDao extends ComnDao {

	public SqlResult<AppBottomDeal> findAppBottomDeals(SqlParam<AppBottomDeal> params) throws Exception {
		AppBottomDeal v = params.getModel();
		String sql = "SELECT t1.id,t2.reporttab_name,securitiesfirms_type,remark_par4,financ_org_code,invest_type,publisher_trade," +
				"issu_orgname,cooper_mode,fun_com_name,investment_stage,project_yield,pay_freq,structure_grade,base_assets_type," +
				"remark_par2,is_import,vouch,line_right_way,remark_par3,fee_par,ftool_code,is_bankprod,plan_org_code," +
				"capital_actual_invest,rate_desc_manage,rate_desc_service,oside_names,yield,in_butype_tec,base_rate_type," +
				"fdyz,first_exercise_date,perpetuity_clause_type,whether_deferred_interest,partial_redemption_mark," +
				"country_code,comm_ind_code,trade_places,iss_type_tec,type_par4,rate_desc_other,direction,comm2_ind_code," +
				"debt_way,excess_income_rate,fina_type_scale,relation_btw_gaf,score_par2,deferred_clause_type,cuy,iss_mod," +
				"cbc_type,cbc_subtype,begindate,depot,is_goverment_fundation,fun_man_name,interest_type,vouch_desc," +
				"credit_org_code,t8_sys_adtype_id,sub_level,cooper_trade,type_par3,rate_desc_trust,rate_desc_trans,basedays," +
				"is_buyback,mode_distribute,ratepare,contain_right_type,vesting_period,concrete_type,iss_type_scale,regist_par," +
				"account_code,industry,fun_cus_name,vol_value,legal_maturity,rule_pay_flag,nature_guarantee,score_par3," +
				"score_par4,first_repricing_date,credit_org_name,iss_type_eco,trustee_agency,trustee_agency_remark,is_jrcompany," +
				"trust_people,principal,org_type,invest_fund_type,invest_prop_type,in_butype_sca,in_butype_eco," +
				"debt_service_description,is_flow,installments_marked,oside_name,score_par1,fina_type_eco,sp_ind_code," +
				"fixed_exercise_date,executive_price,repricing_cycle,partial_redemption_rate,registration_code,manage_method," +
				"enddate,goverment_ways,is_jrcompany_fundation,fun_invest_asset,is_bzpj,is_lgfplatform,in_ben_type,out_par," +
				"fina_type_tec,sp_ind_desc,collateral_type,collateral_value,fb_zc_type,comm3_ind_code,report_asset_code," +
				"fbzc_type,gq_edate,is_pledge_financing,is_swap,structprod_subject,holding_purpose,is_financial_instruments," +
				"iss_country,term_days,asset_value,isinterbankloan FROM app_bottom_deal t1 " +
				"left join base_fa_reporttab t2 on t1.securitiesfirms_type = t2.id where 1 = 1";
		if (StringUtils.isNotBlank(v.getId())){
			sql += " and t1.id = $S{id}";
		}
		if (StringUtils.isNotBlank(v.getSecuritiesfirmsType())){
			sql += " and t1.securitiesfirms_type = $S{securitiesfirmsType}";
		}
		if (StringUtils.isNotBlank(v.getFtoolCode())){
			sql += " and t1.ftool_code like '%$U{ftoolCode}%'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addAppBottomDeal(SqlParam<AppBottomDeal> params) throws Exception {
		return super.update("INSERT INTO app_bottom_deal(id,securitiesfirms_type,remark_par4,financ_org_code,invest_type,publisher_trade,issu_orgname,cooper_mode,fun_com_name,investment_stage,project_yield,pay_freq,structure_grade,base_assets_type,remark_par2,is_import,vouch,line_right_way,remark_par3,fee_par,ftool_code,is_bankprod,plan_org_code,capital_actual_invest,rate_desc_manage,rate_desc_service,oside_names,yield,in_butype_tec,base_rate_type,fdyz,first_exercise_date,perpetuity_clause_type,whether_deferred_interest,partial_redemption_mark,country_code,comm_ind_code,trade_places,iss_type_tec,type_par4,rate_desc_other,direction,comm2_ind_code,debt_way,excess_income_rate,fina_type_scale,relation_btw_gaf,score_par2,deferred_clause_type,cuy,iss_mod,cbc_type,cbc_subtype,begindate,depot,is_goverment_fundation,fun_man_name,interest_type,vouch_desc,credit_org_code,t8_sys_adtype_id,sub_level,cooper_trade,type_par3,rate_desc_trust,rate_desc_trans,basedays,is_buyback,mode_distribute,ratepare,contain_right_type,vesting_period,concrete_type,iss_type_scale,regist_par,account_code,industry,fun_cus_name,vol_value,legal_maturity,rule_pay_flag,nature_guarantee,score_par3,score_par4,first_repricing_date,credit_org_name,iss_type_eco,trustee_agency,trustee_agency_remark,is_jrcompany,trust_people,principal,org_type,invest_fund_type,invest_prop_type,in_butype_sca,in_butype_eco,debt_service_description,is_flow,installments_marked,oside_name,score_par1,fina_type_eco,sp_ind_code,fixed_exercise_date,executive_price,repricing_cycle,partial_redemption_rate,registration_code,manage_method,enddate,goverment_ways,is_jrcompany_fundation,fun_invest_asset,is_bzpj,is_lgfplatform,in_ben_type,out_par,fina_type_tec,sp_ind_desc,collateral_type,collateral_value,fb_zc_type,comm3_ind_code,report_asset_code,fbzc_type,gq_edate,is_pledge_financing,is_swap,structprod_subject,holding_purpose,is_financial_instruments,iss_country,term_days,asset_value,isinterbankloan,inputuser,inp_date) VALUES($AUTOIDS{id},$S{securitiesfirmsType},$S{remarkPar4},$S{financOrgCode},$S{investType},$S{publisherTrade},$S{issuOrgname},$S{cooperMode},$S{funComName},$S{investmentStage},$S{projectYield},$S{payFreq},$S{structureGrade},$S{baseAssetsType},$S{remarkPar2},$S{isImport},$S{vouch},$S{lineRightWay},$S{remarkPar3},$S{feePar},$S{ftoolCode},$S{isBankprod},$S{planOrgCode},$S{capitalActualInvest},$S{rateDescManage},$S{rateDescService},$S{osideNames},$S{yield},$S{inButypeTec},$S{baseRateType},$S{fdyz},$S{firstExerciseDate},$S{perpetuityClauseType},$S{whetherDeferredInterest},$S{partialRedemptionMark},$S{countryCode},$S{commIndCode},$S{tradePlaces},$S{issTypeTec},$S{typePar4},$S{rateDescOther},$S{direction},$S{comm2IndCode},$S{debtWay},$S{excessIncomeRate},$S{finaTypeScale},$S{relationBtwGaf},$S{scorePar2},$S{deferredClauseType},$S{cuy},$S{issMod},$S{cbcType},$S{cbcSubtype},$S{begindate},$S{depot},$S{isGovermentFundation},$S{funManName},$S{interestType},$S{vouchDesc},$S{creditOrgCode},$S{t8SysAdtypeId},$S{subLevel},$S{cooperTrade},$S{typePar3},$S{rateDescTrust},$S{rateDescTrans},$S{basedays},$S{isBuyback},$S{modeDistribute},$S{ratepare},$S{containRightType},$S{vestingPeriod},$S{concreteType},$S{issTypeScale},$S{registPar},$S{accountCode},$S{industry},$S{funCusName},$S{volValue},$S{legalMaturity},$S{rulePayFlag},$S{natureGuarantee},$S{scorePar3},$S{scorePar4},$S{firstRepricingDate},$S{creditOrgName},$S{issTypeEco},$S{trusteeAgency},$S{trusteeAgencyRemark},$S{isJrcompany},$S{trustPeople},$S{principal},$S{orgType},$S{investFundType},$S{investPropType},$S{inButypeSca},$S{inButypeEco},$S{debtServiceDescription},$S{isFlow},$S{installmentsMarked},$S{osideName},$S{scorePar1},$S{finaTypeEco},$S{spIndCode},$S{fixedExerciseDate},$S{executivePrice},$S{repricingCycle},$S{partialRedemptionRate},$S{registrationCode},$S{manageMethod},$S{enddate},$S{govermentWays},$S{isJrcompanyFundation},$S{funInvestAsset},$S{isBzpj},$S{isLgfplatform},$S{inBenType},$S{outPar},$S{finaTypeTec},$S{spIndDesc},$S{collateralType},$S{collateralValue},$S{fbZcType},$S{comm3IndCode},$S{reportAssetCode},$S{fbzcType},$S{gqEdate},$S{isPledgeFinancing},$S{isSwap},$S{structprodSubject},$S{holdingPurpose},$S{isFinancialInstruments},$S{issCountry},$S{termDays},$S{assetValue},$S{isinterbankloan},$S{inputuser},$S{inpDate})",
				params.getModel());
	}
	
	public UpdateResult updateAppBottomDeal(SqlParam<AppBottomDeal> params) throws Exception {
		return super.update("UPDATE app_bottom_deal SET securitiesfirms_type=$S{securitiesfirmsType} ,remark_par4=$S{remarkPar4} ,financ_org_code=$S{financOrgCode} ,invest_type=$S{investType} ,publisher_trade=$S{publisherTrade} ,issu_orgname=$S{issuOrgname} ,cooper_mode=$S{cooperMode} ,fun_com_name=$S{funComName} ,investment_stage=$S{investmentStage} ,project_yield=$S{projectYield} ,pay_freq=$S{payFreq} ,structure_grade=$S{structureGrade} ,base_assets_type=$S{baseAssetsType} ,remark_par2=$S{remarkPar2} ,is_import=$S{isImport} ,vouch=$S{vouch} ,line_right_way=$S{lineRightWay} ,remark_par3=$S{remarkPar3} ,fee_par=$S{feePar} ,ftool_code=$S{ftoolCode} ,is_bankprod=$S{isBankprod} ,plan_org_code=$S{planOrgCode} ,capital_actual_invest=$S{capitalActualInvest} ,rate_desc_manage=$S{rateDescManage} ,rate_desc_service=$S{rateDescService} ,oside_names=$S{osideNames} ,yield=$S{yield} ,in_butype_tec=$S{inButypeTec} ,base_rate_type=$S{baseRateType} ,fdyz=$S{fdyz} ,first_exercise_date=$S{firstExerciseDate} ,perpetuity_clause_type=$S{perpetuityClauseType} ,whether_deferred_interest=$S{whetherDeferredInterest} ,partial_redemption_mark=$S{partialRedemptionMark} ,country_code=$S{countryCode} ,comm_ind_code=$S{commIndCode} ,trade_places=$S{tradePlaces} ,iss_type_tec=$S{issTypeTec} ,type_par4=$S{typePar4} ,rate_desc_other=$S{rateDescOther} ,direction=$S{direction} ,comm2_ind_code=$S{comm2IndCode} ,debt_way=$S{debtWay} ,excess_income_rate=$S{excessIncomeRate} ,fina_type_scale=$S{finaTypeScale} ,relation_btw_gaf=$S{relationBtwGaf} ,score_par2=$S{scorePar2} ,deferred_clause_type=$S{deferredClauseType} ,cuy=$S{cuy} ,iss_mod=$S{issMod} ,cbc_type=$S{cbcType} ,cbc_subtype=$S{cbcSubtype} ,begindate=$S{begindate} ,depot=$S{depot} ,is_goverment_fundation=$S{isGovermentFundation} ,fun_man_name=$S{funManName} ,interest_type=$S{interestType} ,vouch_desc=$S{vouchDesc} ,credit_org_code=$S{creditOrgCode} ,t8_sys_adtype_id=$S{t8SysAdtypeId} ,sub_level=$S{subLevel} ,cooper_trade=$S{cooperTrade} ,type_par3=$S{typePar3} ,rate_desc_trust=$S{rateDescTrust} ,rate_desc_trans=$S{rateDescTrans} ,basedays=$S{basedays} ,is_buyback=$S{isBuyback} ,mode_distribute=$S{modeDistribute} ,ratepare=$S{ratepare} ,contain_right_type=$S{containRightType} ,vesting_period=$S{vestingPeriod} ,concrete_type=$S{concreteType} ,iss_type_scale=$S{issTypeScale} ,regist_par=$S{registPar} ,account_code=$S{accountCode} ,industry=$S{industry} ,fun_cus_name=$S{funCusName} ,vol_value=$S{volValue} ,legal_maturity=$S{legalMaturity} ,rule_pay_flag=$S{rulePayFlag} ,nature_guarantee=$S{natureGuarantee} ,score_par3=$S{scorePar3} ,score_par4=$S{scorePar4} ,first_repricing_date=$S{firstRepricingDate} ,credit_org_name=$S{creditOrgName} ,iss_type_eco=$S{issTypeEco} ,trustee_agency=$S{trusteeAgency} ,trustee_agency_remark=$S{trusteeAgencyRemark} ,is_jrcompany=$S{isJrcompany} ,trust_people=$S{trustPeople} ,principal=$S{principal} ,org_type=$S{orgType} ,invest_fund_type=$S{investFundType} ,invest_prop_type=$S{investPropType} ,in_butype_sca=$S{inButypeSca} ,in_butype_eco=$S{inButypeEco} ,debt_service_description=$S{debtServiceDescription} ,is_flow=$S{isFlow} ,installments_marked=$S{installmentsMarked} ,oside_name=$S{osideName} ,score_par1=$S{scorePar1} ,fina_type_eco=$S{finaTypeEco} ,sp_ind_code=$S{spIndCode} ,fixed_exercise_date=$S{fixedExerciseDate} ,executive_price=$S{executivePrice} ,repricing_cycle=$S{repricingCycle} ,partial_redemption_rate=$S{partialRedemptionRate} ,registration_code=$S{registrationCode} ,manage_method=$S{manageMethod} ,enddate=$S{enddate} ,goverment_ways=$S{govermentWays} ,is_jrcompany_fundation=$S{isJrcompanyFundation} ,fun_invest_asset=$S{funInvestAsset} ,is_bzpj=$S{isBzpj} ,is_lgfplatform=$S{isLgfplatform} ,in_ben_type=$S{inBenType} ,out_par=$S{outPar} ,fina_type_tec=$S{finaTypeTec} ,sp_ind_desc=$S{spIndDesc} ,collateral_type=$S{collateralType} ,collateral_value=$S{collateralValue} ,fb_zc_type=$S{fbZcType} ,comm3_ind_code=$S{comm3IndCode} ,report_asset_code=$S{reportAssetCode} ,fbzc_type=$S{fbzcType} ,gq_edate=$S{gqEdate} ,is_pledge_financing=$S{isPledgeFinancing} ,is_swap=$S{isSwap} ,structprod_subject=$S{structprodSubject} ,holding_purpose=$S{holdingPurpose} ,is_financial_instruments=$S{isFinancialInstruments} ,iss_country=$S{issCountry} ,term_days=$S{termDays} ,asset_value=$S{assetValue} ,isinterbankloan=$S{isinterbankloan} ,inputuser=$S{inputuser} ,inp_date=$S{inpDate}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteAppBottomDeal(SqlParam<AppBottomDeal> params) throws Exception {
		return super.update("DELETE FROM app_bottom_deal WHERE  id=$S{id} ",
				params.getModel());
	}

}
