package com.kayak.rpt.zz.historyInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assetDebtRegisterInfohService",table = "app_asset_debt_register_info_h")
public class AssetDebtRegisterInfoh {
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "行内资产/负债编码", sql = "asset_code = $S{assetCode}" ,field = "asset_code")
   private String assetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产/负债类别", sql = "ass_debt_type = $S{assDebtType}" ,field = "ass_debt_type")
   private String assDebtType;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cur = $S{cur}" ,field = "cur")
   private String cur;
   @GraphQLField(kkhtml = "KFieldText", label = "交易流通场所", sql = "trade_venue = $S{tradeVenue}" ,field = "trade_venue")
   private String tradeVenue;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "资金存入银行", sql = "bb_deposit_bank = $S{bbDepositBank}" ,field = "bb_deposit_bank")
   private String bbDepositBank;
   @GraphQLField(kkhtml = "KFieldText", label = "存款金额", sql = "bb_deposit_amt = $S{bbDepositAmt}" ,field = "bb_deposit_amt")
   private String bbDepositAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "bb_value_date = $S{bbValueDate}" ,field = "bb_value_date")
   private String bbValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "bb_maturity_date = $S{bbMaturityDate}" ,field = "bb_maturity_date")
   private String bbMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "年利率%", sql = "bb_annual_rate = $S{bbAnnualRate}" ,field = "bb_annual_rate")
   private String bbAnnualRate;
   @GraphQLField(kkhtml = "KFieldText", label = "计息基础", sql = "bb_interest_basis = $S{bbInterestBasis}" ,field = "bb_interest_basis")
   private String bbInterestBasis;
   @GraphQLField(kkhtml = "KFieldText", label = "存款类型", sql = "bb_deposit_type = $S{bbDepositType}" ,field = "bb_deposit_type")
   private String bbDepositType;
   @GraphQLField(kkhtml = "KFieldText", label = "结构性存款标的类别", sql = "bb_struct_deposit_type = $S{bbStructDepositType}" ,field = "bb_struct_deposit_type")
   private String bbStructDepositType;
   @GraphQLField(kkhtml = "KFieldText", label = "结构性存款挂钩标的", sql = "bb_struct_deposit = $S{bbStructDeposit}" ,field = "bb_struct_deposit")
   private String bbStructDeposit;
   @GraphQLField(kkhtml = "KFieldText", label = "代码", sql = "cc_ident_code = $S{ccIdentCode}" ,field = "cc_ident_code")
   private String ccIdentCode;
   @GraphQLField(kkhtml = "KFieldText", label = "名称", sql = "cc_name = $S{ccName}" ,field = "cc_name")
   private String ccName;
   @GraphQLField(kkhtml = "KFieldText", label = "具体类别", sql = "cc_specific_bond_type = $S{ccSpecificBondType}" ,field = "cc_specific_bond_type")
   private String ccSpecificBondType;
   @GraphQLField(kkhtml = "KFieldText", label = "发行方式", sql = "cc_iss_mode_bond = $S{ccIssModeBond}" ,field = "cc_iss_mode_bond")
   private String ccIssModeBond;
   @GraphQLField(kkhtml = "KFieldText", label = "主体评级", sql = "cc_iss_rate_part = $S{ccIssRatePart}" ,field = "cc_iss_rate_part")
   private String ccIssRatePart;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构类型", sql = "cc_institute_type_scale = $S{ccInstituteTypeScale}" ,field = "cc_institute_type_scale")
   private String ccInstituteTypeScale;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构类型", sql = "cc_institute_type_tech = $S{ccInstituteTypeTech}" ,field = "cc_institute_type_tech")
   private String ccInstituteTypeTech;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构类型", sql = "cc_institute_type_economic = $S{ccInstituteTypeEconomic}" ,field = "cc_institute_type_economic")
   private String ccInstituteTypeEconomic;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构所属行业", sql = "cc_industry_issuer = $S{ccIndustryIssuer}" ,field = "cc_industry_issuer")
   private String ccIndustryIssuer;
   @GraphQLField(kkhtml = "KFieldText", label = "登记托管机构", sql = "cc_regist_deposit = $S{ccRegistDeposit}" ,field = "cc_regist_deposit")
   private String ccRegistDeposit;
   @GraphQLField(kkhtml = "KFieldText", label = "登记托管机构说明", sql = "cc_details_regist_deposit = $S{ccDetailsRegistDeposit}" ,field = "cc_details_regist_deposit")
   private String ccDetailsRegistDeposit;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "dd_value_date = $S{ddValueDate}" ,field = "dd_value_date")
   private String ddValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "dd_maturity_date = $S{ddMaturityDate}" ,field = "dd_maturity_date")
   private String ddMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "对手方", sql = "dd_counterparty = $S{ddCounterparty}" ,field = "dd_counterparty")
   private String ddCounterparty;
   @GraphQLField(kkhtml = "KFieldText", label = "对手方类型", sql = "dd_counterparty_type = $S{ddCounterpartyType}" ,field = "dd_counterparty_type")
   private String ddCounterpartyType;
   @GraphQLField(kkhtml = "KFieldText", label = "年利率%", sql = "dd_annal_interest_rate = $S{ddAnnalInterestRate}" ,field = "dd_annal_interest_rate")
   private String ddAnnalInterestRate;
   @GraphQLField(kkhtml = "KFieldText", label = "计息基础", sql = "dd_interest_basis = $S{ddInterestBasis}" ,field = "dd_interest_basis")
   private String ddInterestBasis;
   @GraphQLField(kkhtml = "KFieldText", label = "回购标的类别", sql = "dd_collateral_type = $S{ddCollateralType}" ,field = "dd_collateral_type")
   private String ddCollateralType;
   @GraphQLField(kkhtml = "KFieldText", label = "回购标的金额", sql = "dd_collateral_value = $S{ddCollateralValue}" ,field = "dd_collateral_value")
   private String ddCollateralValue;
   @GraphQLField(kkhtml = "KFieldText", label = "名称", sql = "ee_name = $S{eeName}" ,field = "ee_name")
   private String eeName;
   @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "ee_amt = $S{eeAmt}" ,field = "ee_amt")
   private String eeAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "份额面值", sql = "ee_unit_par_value = $S{eeUnitParValue}" ,field = "ee_unit_par_value")
   private String eeUnitParValue;
   @GraphQLField(kkhtml = "KFieldText", label = "收/受权益类型", sql = "ee_ownership_type = $S{eeOwnershipType}" ,field = "ee_ownership_type")
   private String eeOwnershipType;
   @GraphQLField(kkhtml = "KFieldText", label = "是否属于买入反售", sql = "ee_buyback = $S{eeBuyback}" ,field = "ee_buyback")
   private String eeBuyback;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "ee_value_date = $S{eeValueDate}" ,field = "ee_value_date")
   private String eeValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "ee_maturity_date = $S{eeMaturityDate}" ,field = "ee_maturity_date")
   private String eeMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "法定到期日", sql = "ee_statutory_maturity_date = $S{eeStatutoryMaturityDate}" ,field = "ee_statutory_maturity_date")
   private String eeStatutoryMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有预期收益率", sql = "ee_expected_return = $S{eeExpectedReturn}" ,field = "ee_expected_return")
   private String eeExpectedReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "项目收益率", sql = "ee_project_annaul_return = $S{eeProjectAnnaulReturn}" ,field = "ee_project_annaul_return")
   private String eeProjectAnnaulReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "计息类型", sql = "ee_coupon_type = $S{eeCouponType}" ,field = "ee_coupon_type")
   private String eeCouponType;
   @GraphQLField(kkhtml = "KFieldText", label = "规则付息标识", sql = "ee_regualr_interest_pay = $S{eeRegualrInterestPay}" ,field = "ee_regualr_interest_pay")
   private String eeRegualrInterestPay;
   @GraphQLField(kkhtml = "KFieldText", label = "付息频率", sql = "ee_interest_pay_frequency = $S{eeInterestPayFrequency}" ,field = "ee_interest_pay_frequency")
   private String eeInterestPayFrequency;
   @GraphQLField(kkhtml = "KFieldText", label = "利息分布方式", sql = "ee_coupon_allocation_type = $S{eeCouponAllocationType}" ,field = "ee_coupon_allocation_type")
   private String eeCouponAllocationType;
   @GraphQLField(kkhtml = "KFieldText", label = "还本付息情况说明", sql = "ee_detail_princ_interest = $S{eeDetailPrincInterest}" ,field = "ee_detail_princ_interest")
   private String eeDetailPrincInterest;
   @GraphQLField(kkhtml = "KFieldText", label = "计息基础", sql = "ee_interest_basis = $S{eeInterestBasis}" ,field = "ee_interest_basis")
   private String eeInterestBasis;
   @GraphQLField(kkhtml = "KFieldText", label = "基准利率种类", sql = "ee_bench_rate_type = $S{eeBenchRateType}" ,field = "ee_bench_rate_type")
   private String eeBenchRateType;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有浮动因子", sql = "ee_float_factor = $S{eeFloatFactor}" ,field = "ee_float_factor")
   private String eeFloatFactor;
   @GraphQLField(kkhtml = "KFieldText", label = "浮动因子%", sql = "ee_float_rate = $S{eeFloatRate}" ,field = "ee_float_rate")
   private String eeFloatRate;
   @GraphQLField(kkhtml = "KFieldText", label = "利差", sql = "ee_yield_spread_bp = $S{eeYieldSpreadBp}" ,field = "ee_yield_spread_bp")
   private String eeYieldSpreadBp;
   @GraphQLField(kkhtml = "KFieldText", label = "结构档次", sql = "ee_struct_grade = $S{eeStructGrade}" ,field = "ee_struct_grade")
   private String eeStructGrade;
   @GraphQLField(kkhtml = "KFieldText", label = "还本方式", sql = "ee_princ_payment_type = $S{eePrincPaymentType}" ,field = "ee_princ_payment_type")
   private String eePrincPaymentType;
   @GraphQLField(kkhtml = "KFieldText", label = "分期还本条款标识", sql = "ee_install_repay_type = $S{eeInstallRepayType}" ,field = "ee_install_repay_type")
   private String eeInstallRepayType;
   @GraphQLField(kkhtml = "KFieldText", label = "基础资产类型", sql = "ee_base_asset_type = $S{eeBaseAssetType}" ,field = "ee_base_asset_type")
   private String eeBaseAssetType;
   @GraphQLField(kkhtml = "KFieldText", label = "超额收益分配比例%", sql = "ee_percent_exc_in_allot = $S{eePercentExcInAllot}" ,field = "ee_percent_exc_in_allot")
   private String eePercentExcInAllot;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人", sql = "ee_debtor = $S{eeDebtor}" ,field = "ee_debtor")
   private String eeDebtor;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人内部信用评级", sql = "ee_deptor_rate = $S{eeDeptorRate}" ,field = "ee_deptor_rate")
   private String eeDeptorRate;
   @GraphQLField(kkhtml = "KFieldText", label = "外部评级机构名称及对融资人评级结果", sql = "ee_rate_agency_iss = $S{eeRateAgencyIss}" ,field = "ee_rate_agency_iss")
   private String eeRateAgencyIss;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人类型", sql = "ee_debtor_type_scale = $S{eeDebtorTypeScale}" ,field = "ee_debtor_type_scale")
   private String eeDebtorTypeScale;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人类型", sql = "ee_debtor_type_tech = $S{eeDebtorTypeTech}" ,field = "ee_debtor_type_tech")
   private String eeDebtorTypeTech;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人类型", sql = "ee_debtor_type_economic = $S{eeDebtorTypeEconomic}" ,field = "ee_debtor_type_economic")
   private String eeDebtorTypeEconomic;
   @GraphQLField(kkhtml = "KFieldText", label = "融资项目", sql = "ee_project = $S{eeProject}" ,field = "ee_project")
   private String eeProject;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人所属行业", sql = "ee_industry_debtor = $S{eeIndustryDebtor}" ,field = "ee_industry_debtor")
   private String eeIndustryDebtor;
   @GraphQLField(kkhtml = "KFieldText", label = "融资项目所属行业", sql = "ee_industry_project = $S{eeIndustryProject}" ,field = "ee_industry_project")
   private String eeIndustryProject;
   @GraphQLField(kkhtml = "KFieldText", label = "项目是否属于重点监控行业和领域", sql = "ee_monitor_indus_type = $S{eeMonitorIndusType}" ,field = "ee_monitor_indus_type")
   private String eeMonitorIndusType;
   @GraphQLField(kkhtml = "KFieldText", label = "重点监控行业和领域类别", sql = "ee_monitor_industry_type = $S{eeMonitorIndustryType}" ,field = "ee_monitor_industry_type")
   private String eeMonitorIndustryType;
   @GraphQLField(kkhtml = "KFieldText", label = "重点监控行业和领域类别说明", sql = "ee_details_monitory_type = $S{eeDetailsMonitoryType}" ,field = "ee_details_monitory_type")
   private String eeDetailsMonitoryType;
   @GraphQLField(kkhtml = "KFieldText", label = "担保方式", sql = "ee_guarantee_method = $S{eeGuaranteeMethod}" ,field = "ee_guarantee_method")
   private String eeGuaranteeMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "担保情况说明", sql = "ee_detail_guarantee_status = $S{eeDetailGuaranteeStatus}" ,field = "ee_detail_guarantee_status")
   private String eeDetailGuaranteeStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "抵质押物类型", sql = "ee_pledge_type = $S{eePledgeType}" ,field = "ee_pledge_type")
   private String eePledgeType;
   @GraphQLField(kkhtml = "KFieldText", label = "抵质押物价值", sql = "ee_pledge_value = $S{eePledgeValue}" ,field = "ee_pledge_value")
   private String eePledgeValue;
   @GraphQLField(kkhtml = "KFieldText", label = "担保性质", sql = "ee_guarantee_type = $S{eeGuaranteeType}" ,field = "ee_guarantee_type")
   private String eeGuaranteeType;
   @GraphQLField(kkhtml = "KFieldText", label = "担保人与融资人关系", sql = "ee_guarantor_type = $S{eeGuarantorType}" ,field = "ee_guarantor_type")
   private String eeGuarantorType;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人主体评级", sql = "ee_debtor_rate = $S{eeDebtorRate}" ,field = "ee_debtor_rate")
   private String eeDebtorRate;
   @GraphQLField(kkhtml = "KFieldText", label = "资产内部评级", sql = "ee_inter_asset_rate = $S{eeInterAssetRate}" ,field = "ee_inter_asset_rate")
   private String eeInterAssetRate;
   @GraphQLField(kkhtml = "KFieldText", label = "资产外部评级", sql = "ee_out_asset_rate = $S{eeOutAssetRate}" ,field = "ee_out_asset_rate")
   private String eeOutAssetRate;
   @GraphQLField(kkhtml = "KFieldText", label = "含权类型", sql = "ee_option_type = $S{eeOptionType}" ,field = "ee_option_type")
   private String eeOptionType;
   @GraphQLField(kkhtml = "KFieldText", label = "行权方式", sql = "ee_exercise_date_type = $S{eeExerciseDateType}" ,field = "ee_exercise_date_type")
   private String eeExerciseDateType;
   @GraphQLField(kkhtml = "KFieldText", label = "固定行权日", sql = "ee_fixed_exercise_date = $S{eeFixedExerciseDate}" ,field = "ee_fixed_exercise_date")
   private String eeFixedExerciseDate;
   @GraphQLField(kkhtml = "KFieldText", label = "首次行权日期", sql = "ee_first_exercise_date = $S{eeFirstExerciseDate}" ,field = "ee_first_exercise_date")
   private String eeFirstExerciseDate;
   @GraphQLField(kkhtml = "KFieldText", label = "行权周期", sql = "ee_exercise_period = $S{eeExercisePeriod}" ,field = "ee_exercise_period")
   private String eeExercisePeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "行权价格", sql = "ee_exercise_price = $S{eeExercisePrice}" ,field = "ee_exercise_price")
   private String eeExercisePrice;
   @GraphQLField(kkhtml = "KFieldText", label = "永续条款类型", sql = "ee_perpetual_type = $S{eePerpetualType}" ,field = "ee_perpetual_type")
   private String eePerpetualType;
   @GraphQLField(kkhtml = "KFieldText", label = "利息递延条款类型", sql = "ee_deferre_interest_type = $S{eeDeferreInterestType}" ,field = "ee_deferre_interest_type")
   private String eeDeferreInterestType;
   @GraphQLField(kkhtml = "KFieldText", label = "递延利息是否计息", sql = "ee_interest_deferred = $S{eeInterestDeferred}" ,field = "ee_interest_deferred")
   private String eeInterestDeferred;
   @GraphQLField(kkhtml = "KFieldText", label = "首次重定价日期", sql = "ee_first_reprice_date = $S{eeFirstRepriceDate}" ,field = "ee_first_reprice_date")
   private String eeFirstRepriceDate;
   @GraphQLField(kkhtml = "KFieldText", label = "重定价周期", sql = "ee_reprice_period = $S{eeRepricePeriod}" ,field = "ee_reprice_period")
   private String eeRepricePeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "部分赎回标识", sql = "ee_partial_redemption = $S{eePartialRedemption}" ,field = "ee_partial_redemption")
   private String eePartialRedemption;
   @GraphQLField(kkhtml = "KFieldText", label = "部分赎回比例%", sql = "ee_partial_redemption_rate = $S{eePartialRedemptionRate}" ,field = "ee_partial_redemption_rate")
   private String eePartialRedemptionRate;
   @GraphQLField(kkhtml = "KFieldText", label = "选择权", sql = "ee_option_right = $S{eeOptionRight}" ,field = "ee_option_right")
   private String eeOptionRight;
   @GraphQLField(kkhtml = "KFieldText", label = "行权条件说明", sql = "ee_details_exercise_term = $S{eeDetailsExerciseTerm}" ,field = "ee_details_exercise_term")
   private String eeDetailsExerciseTerm;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人所属地区", sql = "ee_region_debtor = $S{eeRegionDebtor}" ,field = "ee_region_debtor")
   private String eeRegionDebtor;
   @GraphQLField(kkhtml = "KFieldText", label = "增信机构代码", sql = "ee_enhance_institute_code = $S{eeEnhanceInstituteCode}" ,field = "ee_enhance_institute_code")
   private String eeEnhanceInstituteCode;
   @GraphQLField(kkhtml = "KFieldText", label = "增信机构名称", sql = "ee_enhance_institute_name = $S{eeEnhanceInstituteName}" ,field = "ee_enhance_institute_name")
   private String eeEnhanceInstituteName;
   @GraphQLField(kkhtml = "KFieldText", label = "融资总费率", sql = "ee_total_fee_rate = $S{eeTotalFeeRate}" ,field = "ee_total_fee_rate")
   private Double eeTotalFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人组织机构", sql = "ee_organization_code = $S{eeOrganizationCode}" ,field = "ee_organization_code")
   private String eeOrganizationCode;
   @GraphQLField(kkhtml = "KFieldText", label = "是否收/受益权", sql = "ff_ownership = $S{ffOwnership}" ,field = "ff_ownership")
   private String ffOwnership;
   @GraphQLField(kkhtml = "KFieldText", label = "是否买入反售", sql = "ff_buyback = $S{ffBuyback}" ,field = "ff_buyback")
   private String ffBuyback;
   @GraphQLField(kkhtml = "KFieldText", label = "类型", sql = "ff_type = $S{ffType}" ,field = "ff_type")
   private String ffType;
   @GraphQLField(kkhtml = "KFieldText", label = "数量", sql = "ff_quantity = $S{ffQuantity}" ,field = "ff_quantity")
   private String ffQuantity;
   @GraphQLField(kkhtml = "KFieldText", label = "合计金额", sql = "ff_aggregate_amt = $S{ffAggregateAmt}" ,field = "ff_aggregate_amt")
   private String ffAggregateAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "加权剩余期限", sql = "ff_weight_remain_day = $S{ffWeightRemainDay}" ,field = "ff_weight_remain_day")
   private String ffWeightRemainDay;
   @GraphQLField(kkhtml = "KFieldText", label = "最长剩余期限", sql = "ff_max_remain_day = $S{ffMaxRemainDay}" ,field = "ff_max_remain_day")
   private String ffMaxRemainDay;
   @GraphQLField(kkhtml = "KFieldText", label = "最短剩余期限", sql = "ff_min_remain_fay = $S{ffMinRemainFay}" ,field = "ff_min_remain_fay")
   private String ffMinRemainFay;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "ff_maturity_date = $S{ffMaturityDate}" ,field = "ff_maturity_date")
   private String ffMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "ff_value_date = $S{ffValueDate}" ,field = "ff_value_date")
   private String ffValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "贴现利率%", sql = "ff_discount_rate = $S{ffDiscountRate}" ,field = "ff_discount_rate")
   private String ffDiscountRate;
   @GraphQLField(kkhtml = "KFieldText", label = "行业", sql = "ff_industry = $S{ffIndustry}" ,field = "ff_industry")
   private String ffIndustry;
   @GraphQLField(kkhtml = "KFieldText", label = "股票代码", sql = "gg_stock_code = $S{ggStockCode}" ,field = "gg_stock_code")
   private String ggStockCode;
   @GraphQLField(kkhtml = "KFieldText", label = "股票/企业名称", sql = "gg_name = $S{ggName}" ,field = "gg_name")
   private String ggName;
   @GraphQLField(kkhtml = "KFieldText", label = "股票类型", sql = "gg_stock_type = $S{ggStockType}" ,field = "gg_stock_type")
   private String ggStockType;
   @GraphQLField(kkhtml = "KFieldText", label = "行业", sql = "gg_industry = $S{ggIndustry}" ,field = "gg_industry")
   private String ggIndustry;
   @GraphQLField(kkhtml = "KFieldText", label = "投资阶段", sql = "gg_invest_stage = $S{ggInvestStage}" ,field = "gg_invest_stage")
   private String ggInvestStage;
   @GraphQLField(kkhtml = "KFieldText", label = "股权退出安排", sql = "gg_equity_out_date = $S{ggEquityOutDate}" ,field = "gg_equity_out_date")
   private String ggEquityOutDate;
   @GraphQLField(kkhtml = "KFieldText", label = "企业类型", sql = "gg_enter_type_scale = $S{ggEnterTypeScale}" ,field = "gg_enter_type_scale")
   private String ggEnterTypeScale;
   @GraphQLField(kkhtml = "KFieldText", label = "企业类型", sql = "gg_enter_type_tech = $S{ggEnterTypeTech}" ,field = "gg_enter_type_tech")
   private String ggEnterTypeTech;
   @GraphQLField(kkhtml = "KFieldText", label = "企业类型", sql = "gg_enter_type_economic = $S{ggEnterTypeEconomic}" ,field = "gg_enter_type_economic")
   private String ggEnterTypeEconomic;
   @GraphQLField(kkhtml = "KFieldText", label = "是否质押融资", sql = "gg_pledged_finace = $S{ggPledgedFinace}" ,field = "gg_pledged_finace")
   private String ggPledgedFinace;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为债转股", sql = "gg_debt_equity_swap = $S{ggDebtEquitySwap}" ,field = "gg_debt_equity_swap")
   private String ggDebtEquitySwap;
   @GraphQLField(kkhtml = "KFieldText", label = "名称", sql = "hh_name = $S{hhName}" ,field = "hh_name")
   private String hhName;
   @GraphQLField(kkhtml = "KFieldText", label = "名义本金", sql = "hh_nominal_principal = $S{hhNominalPrincipal}" ,field = "hh_nominal_principal")
   private String hhNominalPrincipal;
   @GraphQLField(kkhtml = "KFieldText", label = "标的类别", sql = "hh_under_asset_type = $S{hhUnderAssetType}" ,field = "hh_under_asset_type")
   private String hhUnderAssetType;
   @GraphQLField(kkhtml = "KFieldText", label = "持有目的", sql = "hh_hold_objective = $S{hhHoldObjective}" ,field = "hh_hold_objective")
   private String hhHoldObjective;
   @GraphQLField(kkhtml = "KFieldText", label = "所属国家或地区", sql = "ii_county_region = $S{iiCountyRegion}" ,field = "ii_county_region")
   private String iiCountyRegion;
   @GraphQLField(kkhtml = "KFieldText", label = "债券名称", sql = "ii_bond_name = $S{iiBondName}" ,field = "ii_bond_name")
   private String iiBondName;
   @GraphQLField(kkhtml = "KFieldText", label = "债券代码", sql = "ii_bond_ident_code = $S{iiBondIdentCode}" ,field = "ii_bond_ident_code")
   private String iiBondIdentCode;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构", sql = "ii_issuer = $S{iiIssuer}" ,field = "ii_issuer")
   private String iiIssuer;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构所属行业", sql = "ii_industry_issuer = $S{iiIndustryIssuer}" ,field = "ii_industry_issuer")
   private String iiIndustryIssuer;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "ii_value_date = $S{iiValueDate}" ,field = "ii_value_date")
   private String iiValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "ii_maturity_date = $S{iiMaturityDate}" ,field = "ii_maturity_date")
   private String iiMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "期限", sql = "ii_term_maturity = $S{iiTermMaturity}" ,field = "ii_term_maturity")
   private String iiTermMaturity;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构主体信用评级", sql = "ii_issuer_rate_bond = $S{iiIssuerRateBond}" ,field = "ii_issuer_rate_bond")
   private String iiIssuerRateBond;
   @GraphQLField(kkhtml = "KFieldText", label = "债券信用评级", sql = "ii_bond_rate = $S{iiBondRate}" ,field = "ii_bond_rate")
   private String iiBondRate;
   @GraphQLField(kkhtml = "KFieldText", label = "票面利率%", sql = "ii_coup_rate = $S{iiCoupRate}" ,field = "ii_coup_rate")
   private String iiCoupRate;
   @GraphQLField(kkhtml = "KFieldText", label = "付息频率", sql = "ii_interest_pay_quency = $S{iiInterestPayQuency}" ,field = "ii_interest_pay_quency")
   private String iiInterestPayQuency;
   @GraphQLField(kkhtml = "KFieldText", label = "担保情况说明", sql = "ii_details_assure_status = $S{iiDetailsAssureStatus}" ,field = "ii_details_assure_status")
   private String iiDetailsAssureStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "是否含权等特殊条款情况说明", sql = "ii_details_special_terms = $S{iiDetailsSpecialTerms}" ,field = "ii_details_special_terms")
   private String iiDetailsSpecialTerms;
   @GraphQLField(kkhtml = "KFieldText", label = "所属国家或地区", sql = "jj_country = $S{jjCountry}" ,field = "jj_country")
   private String jjCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "jj_value_date = $S{jjValueDate}" ,field = "jj_value_date")
   private String jjValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "jj_maturity_date = $S{jjMaturityDate}" ,field = "jj_maturity_date")
   private String jjMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "对手方", sql = "jj_counterparty = $S{jjCounterparty}" ,field = "jj_counterparty")
   private String jjCounterparty;
   @GraphQLField(kkhtml = "KFieldText", label = "年利率%", sql = "jj_interest_rate = $S{jjInterestRate}" ,field = "jj_interest_rate")
   private String jjInterestRate;
   @GraphQLField(kkhtml = "KFieldText", label = "计息基础", sql = "jj_interest_basis = $S{jjInterestBasis}" ,field = "jj_interest_basis")
   private String jjInterestBasis;
   @GraphQLField(kkhtml = "KFieldText", label = "所属国家或地区", sql = "kk_country = $S{kkCountry}" ,field = "kk_country")
   private String kkCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "股票/基金代码", sql = "kk_ident_code = $S{kkIdentCode}" ,field = "kk_ident_code")
   private String kkIdentCode;
   @GraphQLField(kkhtml = "KFieldText", label = "股票/基金名称", sql = "kk_name = $S{kkName}" ,field = "kk_name")
   private String kkName;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构", sql = "kk_issuer = $S{kkIssuer}" ,field = "kk_issuer")
   private String kkIssuer;
   @GraphQLField(kkhtml = "KFieldText", label = "行业", sql = "kk_industry = $S{kkIndustry}" ,field = "kk_industry")
   private String kkIndustry;
   @GraphQLField(kkhtml = "KFieldText", label = "所属国家或地区", sql = "ll_country = $S{llCountry}" ,field = "ll_country")
   private String llCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "合约名称", sql = "ll_contract_name = $S{llContractName}" ,field = "ll_contract_name")
   private String llContractName;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "ll_value_date = $S{llValueDate}" ,field = "ll_value_date")
   private String llValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "ll_maturity_date = $S{llMaturityDate}" ,field = "ll_maturity_date")
   private String llMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "票面利率%", sql = "ll_coupon_rate = $S{llCouponRate}" ,field = "ll_coupon_rate")
   private String llCouponRate;
   @GraphQLField(kkhtml = "KFieldText", label = "付息频率", sql = "ll_interest_frequency = $S{llInterestFrequency}" ,field = "ll_interest_frequency")
   private String llInterestFrequency;
   @GraphQLField(kkhtml = "KFieldText", label = "固定收益部分所占比例%", sql = "ll_percent_fix = $S{llPercentFix}" ,field = "ll_percent_fix")
   private String llPercentFix;
   @GraphQLField(kkhtml = "KFieldText", label = "衍生金融工具所占比例%", sql = "ll_percent_derivate = $S{llPercentDerivate}" ,field = "ll_percent_derivate")
   private String llPercentDerivate;
   @GraphQLField(kkhtml = "KFieldText", label = "衍生金融工具具体投资方式", sql = "ll_derivate_invet_type = $S{llDerivateInvetType}" ,field = "ll_derivate_invet_type")
   private String llDerivateInvetType;
   @GraphQLField(kkhtml = "KFieldText", label = "衍生金融工具挂钩标的资产", sql = "ll_under_asset = $S{llUnderAsset}" ,field = "ll_under_asset")
   private String llUnderAsset;
   @GraphQLField(kkhtml = "KFieldText", label = "利息结算方式", sql = "ll_details_proceeds = $S{llDetailsProceeds}" ,field = "ll_details_proceeds")
   private String llDetailsProceeds;
   @GraphQLField(kkhtml = "KFieldText", label = "含权情况说明", sql = "ll_details_option = $S{llDetailsOption}" ,field = "ll_details_option")
   private String llDetailsOption;
   @GraphQLField(kkhtml = "KFieldText", label = "结构性票据最高收益率%", sql = "ll_max_note_return = $S{llMaxNoteReturn}" ,field = "ll_max_note_return")
   private String llMaxNoteReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "机构性票据最低收益率%", sql = "ll_min_note_return = $S{llMinNoteReturn}" ,field = "ll_min_note_return")
   private String llMinNoteReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "挂钩标的资产基准价格", sql = "ll_strike_under_asset = $S{llStrikeUnderAsset}" ,field = "ll_strike_under_asset")
   private String llStrikeUnderAsset;
   @GraphQLField(kkhtml = "KFieldText", label = "挂钩标的资产登记日价格", sql = "ll_under_rg_price = $S{llUnderRgPrice}" ,field = "ll_under_rg_price")
   private String llUnderRgPrice;
   @GraphQLField(kkhtml = "KFieldText", label = "交易费", sql = "ll_trans_costs = $S{llTransCosts}" ,field = "ll_trans_costs")
   private String llTransCosts;
   @GraphQLField(kkhtml = "KFieldText", label = "资管计划名称", sql = "mm_manage_plan_name = $S{mmManagePlanName}" ,field = "mm_manage_plan_name")
   private String mmManagePlanName;
   @GraphQLField(kkhtml = "KFieldText", label = "是否由金融资产投资公司发行", sql = "mm_issued_asset_company = $S{mmIssuedAssetCompany}" ,field = "mm_issued_asset_company")
   private String mmIssuedAssetCompany;
   @GraphQLField(kkhtml = "KFieldText", label = "资管计划发起人机构编码", sql = "mm_plan_issuer_code = $S{mmPlanIssuerCode}" ,field = "mm_plan_issuer_code")
   private String mmPlanIssuerCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资管计划登记编码", sql = "mm_asset_plan_rg_code = $S{mmAssetPlanRgCode}" ,field = "mm_asset_plan_rg_code")
   private String mmAssetPlanRgCode;
   @GraphQLField(kkhtml = "KFieldText", label = "管理人", sql = "mm_manager = $S{mmManager}" ,field = "mm_manager")
   private String mmManager;
   @GraphQLField(kkhtml = "KFieldText", label = "托管人", sql = "mm_custodian = $S{mmCustodian}" ,field = "mm_custodian")
   private String mmCustodian;
   @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "mm_amt = $S{mmAmt}" ,field = "mm_amt")
   private String mmAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "资金实际投向", sql = "mm_actual_direct = $S{mmActualDirect}" ,field = "mm_actual_direct")
   private String mmActualDirect;
   @GraphQLField(kkhtml = "KFieldText", label = "资金运用方式", sql = "mm_details_invest = $S{mmDetailsInvest}" ,field = "mm_details_invest")
   private String mmDetailsInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "资金运用行业", sql = "mm_industry_invest = $S{mmIndustryInvest}" ,field = "mm_industry_invest")
   private String mmIndustryInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "资管计划成立日期", sql = "mm_plan_start_date = $S{mmPlanStartDate}" ,field = "mm_plan_start_date")
   private String mmPlanStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "资管计划终止日期", sql = "mm_plan_maturity_date = $S{mmPlanMaturityDate}" ,field = "mm_plan_maturity_date")
   private String mmPlanMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "资管计划属性", sql = "mm_plan_type = $S{mmPlanType}" ,field = "mm_plan_type")
   private String mmPlanType;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有预期收益率", sql = "mm_expected_return = $S{mmExpectedReturn}" ,field = "mm_expected_return")
   private String mmExpectedReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "预期最高收益率%", sql = "mm_max_expected_return = $S{mmMaxExpectedReturn}" ,field = "mm_max_expected_return")
   private Double mmMaxExpectedReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "预期最低收益率%", sql = "mm_min_expected_return = $S{mmMinExpectedReturn}" ,field = "mm_min_expected_return")
   private Double mmMinExpectedReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "购买结构", sql = "mm_invest_structure = $S{mmInvestStructure}" ,field = "mm_invest_structure")
   private String mmInvestStructure;
   @GraphQLField(kkhtml = "KFieldText", label = "管理方式", sql = "mm_manager_type = $S{mmManagerType}" ,field = "mm_manager_type")
   private String mmManagerType;
   @GraphQLField(kkhtml = "KFieldText", label = "管理费率%", sql = "mm_manager_fee_rate = $S{mmManagerFeeRate}" ,field = "mm_manager_fee_rate")
   private Double mmManagerFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "托管费率%", sql = "mm_custodian_fee_rate = $S{mmCustodianFeeRate}" ,field = "mm_custodian_fee_rate")
   private Double mmCustodianFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "交易相关合计费率%", sql = "mm_trans_cost_rate = $S{mmTransCostRate}" ,field = "mm_trans_cost_rate")
   private Double mmTransCostRate;
   @GraphQLField(kkhtml = "KFieldText", label = "中介服务机构合计费率%", sql = "mm_inter_fee_rate = $S{mmInterFeeRate}" ,field = "mm_inter_fee_rate")
   private Double mmInterFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "其他合计费率%", sql = "mm_other_expense_rate = $S{mmOtherExpenseRate}" ,field = "mm_other_expense_rate")
   private String mmOtherExpenseRate;
   @GraphQLField(kkhtml = "KFieldText", label = "所属国家或地区", sql = "nn_country = $S{nnCountry}" ,field = "nn_country")
   private String nnCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "名称", sql = "nn_name = $S{nnName}" ,field = "nn_name")
   private String nnName;
   @GraphQLField(kkhtml = "KFieldText", label = "期限", sql = "nn_term_days = $S{nnTermDays}" ,field = "nn_term_days")
   private String nnTermDays;
   @GraphQLField(kkhtml = "KFieldText", label = "资产价值", sql = "nn_asset_value = $S{nnAssetValue}" ,field = "nn_asset_value")
   private String nnAssetValue;
   @GraphQLField(kkhtml = "KFieldText", label = "资产收益率%", sql = "nn_asset_return = $S{nnAssetReturn}" ,field = "nn_asset_return")
   private String nnAssetReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "所属国家或地区", sql = "oo_country = $S{ooCountry}" ,field = "oo_country")
   private String ooCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "名称", sql = "oo_name = $S{ooName}" ,field = "oo_name")
   private String ooName;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "oo_value_date = $S{ooValueDate}" ,field = "oo_value_date")
   private String ooValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "oo_maturity_date = $S{ooMaturityDate}" ,field = "oo_maturity_date")
   private String ooMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "资产价值", sql = "oo_asset_value = $S{ooAssetValue}" ,field = "oo_asset_value")
   private String ooAssetValue;
   @GraphQLField(kkhtml = "KFieldText", label = "资产收益率%", sql = "oo_asset_return = $S{ooAssetReturn}" ,field = "oo_asset_return")
   private String ooAssetReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "基金代码", sql = "pp_fund_code = $S{ppFundCode}" ,field = "pp_fund_code")
   private String ppFundCode;
   @GraphQLField(kkhtml = "KFieldText", label = "基金名称", sql = "pp_fund_name = $S{ppFundName}" ,field = "pp_fund_name")
   private String ppFundName;
   @GraphQLField(kkhtml = "KFieldText", label = "是否由金融资产投资公司发行", sql = "pp_issued_asset_company = $S{ppIssuedAssetCompany}" ,field = "pp_issued_asset_company")
   private String ppIssuedAssetCompany;
   @GraphQLField(kkhtml = "KFieldText", label = "行业", sql = "pp_industry = $S{ppIndustry}" ,field = "pp_industry")
   private String ppIndustry;
   @GraphQLField(kkhtml = "KFieldText", label = "登记备案机构", sql = "pp_regist_agency = $S{ppRegistAgency}" ,field = "pp_regist_agency")
   private String ppRegistAgency;
   @GraphQLField(kkhtml = "KFieldText", label = "是否属于政府投资基金", sql = "pp_govern_invest_fund = $S{ppGovernInvestFund}" ,field = "pp_govern_invest_fund")
   private String ppGovernInvestFund;
   @GraphQLField(kkhtml = "KFieldText", label = "政府投资基金投向", sql = "pp_direct_govern_fund = $S{ppDirectGovernFund}" ,field = "pp_direct_govern_fund")
   private String ppDirectGovernFund;
   @GraphQLField(kkhtml = "KFieldText", label = "基金公司名称", sql = "pp_ta_name = $S{ppTaName}" ,field = "pp_ta_name")
   private String ppTaName;
   @GraphQLField(kkhtml = "KFieldText", label = "基金管理机构名称", sql = "pp_manager_fund_name = $S{ppManagerFundName}" ,field = "pp_manager_fund_name")
   private String ppManagerFundName;
   @GraphQLField(kkhtml = "KFieldText", label = "基金托管机构名称", sql = "pp_custodian_fund_name = $S{ppCustodianFundName}" ,field = "pp_custodian_fund_name")
   private String ppCustodianFundName;
   @GraphQLField(kkhtml = "KFieldText", label = "投资阶段", sql = "pp_invest_stage = $S{ppInvestStage}" ,field = "pp_invest_stage")
   private String ppInvestStage;
   @GraphQLField(kkhtml = "KFieldText", label = "投资企业类型", sql = "pp_enter_type_scale = $S{ppEnterTypeScale}" ,field = "pp_enter_type_scale")
   private String ppEnterTypeScale;
   @GraphQLField(kkhtml = "KFieldText", label = "投资企业类型", sql = "pp_enter_type_tech = $S{ppEnterTypeTech}" ,field = "pp_enter_type_tech")
   private String ppEnterTypeTech;
   @GraphQLField(kkhtml = "KFieldText", label = "投资企业类型", sql = "pp_enter_type_economic = $S{ppEnterTypeEconomic}" ,field = "pp_enter_type_economic")
   private String ppEnterTypeEconomic;
   @GraphQLField(kkhtml = "KFieldText", label = "基金投资资产", sql = "pp_invest_assets = $S{ppInvestAssets}" ,field = "pp_invest_assets")
   private String ppInvestAssets;
   @GraphQLField(kkhtml = "KFieldText", label = "委外投资协议名称", sql = "qq_out_agreement_name = $S{qqOutAgreementName}" ,field = "qq_out_agreement_name")
   private String qqOutAgreementName;
   @GraphQLField(kkhtml = "KFieldText", label = "委外投资协议编号", sql = "qq_out_agreement_code = $S{qqOutAgreementCode}" ,field = "qq_out_agreement_code")
   private String qqOutAgreementCode;
   @GraphQLField(kkhtml = "KFieldText", label = "受托人", sql = "qq_trustee = $S{qqTrustee}" ,field = "qq_trustee")
   private String qqTrustee;
   @GraphQLField(kkhtml = "KFieldText", label = "实际管理人", sql = "qq_actual_manager = $S{qqActualManager}" ,field = "qq_actual_manager")
   private String qqActualManager;
   @GraphQLField(kkhtml = "KFieldText", label = "托管人", sql = "qq_custodian = $S{qqCustodian}" ,field = "qq_custodian")
   private String qqCustodian;
   @GraphQLField(kkhtml = "KFieldText", label = "委托投资金额", sql = "qq_out_amt = $S{qqOutAmt}" ,field = "qq_out_amt")
   private String qqOutAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "资金实际投向", sql = "qq_actual_direction = $S{qqActualDirection}" ,field = "qq_actual_direction")
   private String qqActualDirection;
   @GraphQLField(kkhtml = "KFieldText", label = "资金运用方式", sql = "qq_details_invest = $S{qqDetailsInvest}" ,field = "qq_details_invest")
   private String qqDetailsInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "资金运用行业", sql = "qq_industry_invest = $S{qqIndustryInvest}" ,field = "qq_industry_invest")
   private String qqIndustryInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "投资运作起始日期", sql = "qq_value_date = $S{qqValueDate}" ,field = "qq_value_date")
   private String qqValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资运作终止日期", sql = "qq_maturity_date = $S{qqMaturityDate}" ,field = "qq_maturity_date")
   private String qqMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "委外投资属性", sql = "qq_out_type = $S{qqOutType}" ,field = "qq_out_type")
   private String qqOutType;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有预期收益率", sql = "qq_expected_return = $S{qqExpectedReturn}" ,field = "qq_expected_return")
   private String qqExpectedReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "预期最高收益率%", sql = "qq_max_expected_return = $S{qqMaxExpectedReturn}" ,field = "qq_max_expected_return")
   private String qqMaxExpectedReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "预期最低收益率%", sql = "qq_min_expected_return = $S{qqMinExpectedReturn}" ,field = "qq_min_expected_return")
   private String qqMinExpectedReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "管理费率%", sql = "qq_manager_fee_rate = $S{qqManagerFeeRate}" ,field = "qq_manager_fee_rate")
   private String qqManagerFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "托管费率%", sql = "qq_custodian_fee_rate = $S{qqCustodianFeeRate}" ,field = "qq_custodian_fee_rate")
   private String qqCustodianFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "交易相关合计费率%", sql = "qq_trans_cost_rate = $S{qqTransCostRate}" ,field = "qq_trans_cost_rate")
   private String qqTransCostRate;
   @GraphQLField(kkhtml = "KFieldText", label = "中介服务机构合计费率%", sql = "qq_inter_fee_rate = $S{qqInterFeeRate}" ,field = "qq_inter_fee_rate")
   private String qqInterFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "其他合计费率%", sql = "qq_other_expenses_rate = $S{qqOtherExpensesRate}" ,field = "qq_other_expenses_rate")
   private String qqOtherExpensesRate;
   @GraphQLField(kkhtml = "KFieldText", label = "所属国家或地区", sql = "rr_country = $S{rrCountry}" ,field = "rr_country")
   private String rrCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "名称", sql = "rr_name = $S{rrName}" ,field = "rr_name")
   private String rrName;
   @GraphQLField(kkhtml = "KFieldText", label = "期限", sql = "rr_term_maturity = $S{rrTermMaturity}" ,field = "rr_term_maturity")
   private String rrTermMaturity;
   @GraphQLField(kkhtml = "KFieldText", label = "负债规模", sql = "rr_liability_amt = $S{rrLiabilityAmt}" ,field = "rr_liability_amt")
   private String rrLiabilityAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "利率%", sql = "rr_interest_rate = $S{rrInterestRate}" ,field = "rr_interest_rate")
   private String rrInterestRate;
   @GraphQLField(kkhtml = "KFieldText", label = "名称", sql = "ss_name = $S{ssName}" ,field = "ss_name")
   private String ssName;
   @GraphQLField(kkhtml = "KFieldText", label = "行内资产类别", sql = "ss_asset_type = $S{ssAssetType}" ,field = "ss_asset_type")
   private String ssAssetType;
   @GraphQLField(kkhtml = "KFieldText", label = "行内资产类别说明", sql = "ss_details_asset_type = $S{ssDetailsAssetType}" ,field = "ss_details_asset_type")
   private String ssDetailsAssetType;
   @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "ss_amt = $S{ssAmt}" ,field = "ss_amt")
   private String ssAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "ss_value_date = $S{ssValueDate}" ,field = "ss_value_date")
   private String ssValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期日", sql = "ss_maturity_date = $S{ssMaturityDate}" ,field = "ss_maturity_date")
   private String ssMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "所属国家或地区", sql = "ss_country = $S{ssCountry}" ,field = "ss_country")
   private String ssCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有预期收益率", sql = "ss_expected_return = $S{ssExpectedReturn}" ,field = "ss_expected_return")
   private String ssExpectedReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "项目预期收益率", sql = "ss_annual_return = $S{ssAnnualReturn}" ,field = "ss_annual_return")
   private Double ssAnnualReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "付息频率", sql = "ss_interest_frequency = $S{ssInterestFrequency}" ,field = "ss_interest_frequency")
   private String ssInterestFrequency;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人", sql = "ss_debtor = $S{ssDebtor}" ,field = "ss_debtor")
   private String ssDebtor;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人组织结构", sql = "ss_organ_code = $S{ssOrganCode}" ,field = "ss_organ_code")
   private String ssOrganCode;
   @GraphQLField(kkhtml = "KFieldText", label = "外部评级机构名称及对融资人评级结果", sql = "ss_rate_agency_iss = $S{ssRateAgencyIss}" ,field = "ss_rate_agency_iss")
   private String ssRateAgencyIss;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人类型", sql = "ss_debtor_type_scale = $S{ssDebtorTypeScale}" ,field = "ss_debtor_type_scale")
   private String ssDebtorTypeScale;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人类型", sql = "ss_debtor_type_tech = $S{ssDebtorTypeTech}" ,field = "ss_debtor_type_tech")
   private String ssDebtorTypeTech;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人类型", sql = "ss_debtor_type_economic = $S{ssDebtorTypeEconomic}" ,field = "ss_debtor_type_economic")
   private String ssDebtorTypeEconomic;
   @GraphQLField(kkhtml = "KFieldText", label = "融资项目", sql = "ss_project = $S{ssProject}" ,field = "ss_project")
   private String ssProject;
   @GraphQLField(kkhtml = "KFieldText", label = "融资人所属行业", sql = "ss_industry_debtor = $S{ssIndustryDebtor}" ,field = "ss_industry_debtor")
   private String ssIndustryDebtor;
   @GraphQLField(kkhtml = "KFieldText", label = "融资项目所属行业", sql = "ss_industry_project = $S{ssIndustryProject}" ,field = "ss_industry_project")
   private String ssIndustryProject;
   @GraphQLField(kkhtml = "KFieldText", label = "项目是否属于重点监控行业和领域", sql = "ss_monitory_industry = $S{ssMonitoryIndustry}" ,field = "ss_monitory_industry")
   private String ssMonitoryIndustry;
   @GraphQLField(kkhtml = "KFieldText", label = "重点监控行业和领域类别", sql = "ss_monitory_industry_type = $S{ssMonitoryIndustryType}" ,field = "ss_monitory_industry_type")
   private String ssMonitoryIndustryType;
   @GraphQLField(kkhtml = "KFieldText", label = "重点监控行业和领域类别说明", sql = "ss_details_monitory_type = $S{ssDetailsMonitoryType}" ,field = "ss_details_monitory_type")
   private String ssDetailsMonitoryType;
   @GraphQLField(kkhtml = "KFieldText", label = "对应资产外部评级", sql = "ss_internal_asset_rate = $S{ssInternalAssetRate}" ,field = "ss_internal_asset_rate")
   private String ssInternalAssetRate;
   @GraphQLField(kkhtml = "KFieldText", label = "担保方式", sql = "ss_guarantee_method = $S{ssGuaranteeMethod}" ,field = "ss_guarantee_method")
   private String ssGuaranteeMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "担保情况说明", sql = "ss_details_guarantee = $S{ssDetailsGuarantee}" ,field = "ss_details_guarantee")
   private String ssDetailsGuarantee;
   @GraphQLField(kkhtml = "KFieldText", label = "抵质押物类型", sql = "ss_pledge_type = $S{ssPledgeType}" ,field = "ss_pledge_type")
   private String ssPledgeType;
   @GraphQLField(kkhtml = "KFieldText", label = "抵质押物价值", sql = "ss_pledge_value = $S{ssPledgeValue}" ,field = "ss_pledge_value")
   private String ssPledgeValue;
   @GraphQLField(kkhtml = "KFieldText", label = "担保性质", sql = "ss_guarantee_type = $S{ssGuaranteeType}" ,field = "ss_guarantee_type")
   private String ssGuaranteeType;
   @GraphQLField(kkhtml = "KFieldText", label = "担保人与融资人关系", sql = "ss_guarantor_type = $S{ssGuarantorType}" ,field = "ss_guarantor_type")
   private String ssGuarantorType;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为债转股", sql = "ss_debt_equity_swap = $S{ssDebtEquitySwap}" ,field = "ss_debt_equity_swap")
   private String ssDebtEquitySwap;
   @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
   private String theoryReportEndDate;
   @GraphQLField(label = "开始时间")
   private String startDate;

   @GraphQLField(label = "截止时间")
   private String endDate;
   @GraphQLField(kkhtml = "KFieldText", label = "存款账号", sql = "bb_account_no = $S{bbAccountNo}" ,field = "bb_account_no")
   private String bbAccountNo;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为固定收益类", sql = "pp_fixed_income = $S{ppFixedIncome}" ,field = "pp_fixed_income")
   private String ppFixedIncome;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "ee_asset_code = $S{eeAssetCode}" ,field = "ee_asset_code")
   private String eeAssetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为银行理财产品", sql = "mm_manage_product = $S{mmManageProduct}" ,field = "mm_manage_product")
   private String mmManageProduct;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品登记编码", sql = "mm_product_code = $S{mmProductCode}" ,field = "mm_product_code")
   private String mmProductCode;
  	public String getRegisterSerno() {
        return registerSerno;
    }

    public void setRegisterSerno(String registerSerno) {
        this.registerSerno = registerSerno;
    }
  	public String getImpDate() {
        return impDate;
    }

    public void setImpDate(String impDate) {
        this.impDate = impDate;
    }
  	public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
  	public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }
  	public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
  	public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
  	public String getAssDebtType() {
        return assDebtType;
    }

    public void setAssDebtType(String assDebtType) {
        this.assDebtType = assDebtType;
    }
  	public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }
  	public String getTradeVenue() {
        return tradeVenue;
    }

    public void setTradeVenue(String tradeVenue) {
        this.tradeVenue = tradeVenue;
    }
  	public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
  	public String getBbDepositBank() {
        return bbDepositBank;
    }

    public void setBbDepositBank(String bbDepositBank) {
        this.bbDepositBank = bbDepositBank;
    }
  	public String getBbDepositAmt() {
        return bbDepositAmt;
    }

    public void setBbDepositAmt(String bbDepositAmt) {
        this.bbDepositAmt = bbDepositAmt;
    }
  	public String getBbValueDate() {
        return bbValueDate;
    }

    public void setBbValueDate(String bbValueDate) {
        this.bbValueDate = bbValueDate;
    }
  	public String getBbMaturityDate() {
        return bbMaturityDate;
    }

    public void setBbMaturityDate(String bbMaturityDate) {
        this.bbMaturityDate = bbMaturityDate;
    }
  	public String getBbAnnualRate() {
        return bbAnnualRate;
    }

    public void setBbAnnualRate(String bbAnnualRate) {
        this.bbAnnualRate = bbAnnualRate;
    }
  	public String getBbInterestBasis() {
        return bbInterestBasis;
    }

    public void setBbInterestBasis(String bbInterestBasis) {
        this.bbInterestBasis = bbInterestBasis;
    }
  	public String getBbDepositType() {
        return bbDepositType;
    }

    public void setBbDepositType(String bbDepositType) {
        this.bbDepositType = bbDepositType;
    }
  	public String getBbStructDepositType() {
        return bbStructDepositType;
    }

    public void setBbStructDepositType(String bbStructDepositType) {
        this.bbStructDepositType = bbStructDepositType;
    }
  	public String getBbStructDeposit() {
        return bbStructDeposit;
    }

    public void setBbStructDeposit(String bbStructDeposit) {
        this.bbStructDeposit = bbStructDeposit;
    }
  	public String getCcIdentCode() {
        return ccIdentCode;
    }

    public void setCcIdentCode(String ccIdentCode) {
        this.ccIdentCode = ccIdentCode;
    }
  	public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName;
    }
  	public String getCcSpecificBondType() {
        return ccSpecificBondType;
    }

    public void setCcSpecificBondType(String ccSpecificBondType) {
        this.ccSpecificBondType = ccSpecificBondType;
    }
  	public String getCcIssModeBond() {
        return ccIssModeBond;
    }

    public void setCcIssModeBond(String ccIssModeBond) {
        this.ccIssModeBond = ccIssModeBond;
    }
  	public String getCcIssRatePart() {
        return ccIssRatePart;
    }

    public void setCcIssRatePart(String ccIssRatePart) {
        this.ccIssRatePart = ccIssRatePart;
    }
  	public String getCcInstituteTypeScale() {
        return ccInstituteTypeScale;
    }

    public void setCcInstituteTypeScale(String ccInstituteTypeScale) {
        this.ccInstituteTypeScale = ccInstituteTypeScale;
    }
  	public String getCcInstituteTypeTech() {
        return ccInstituteTypeTech;
    }

    public void setCcInstituteTypeTech(String ccInstituteTypeTech) {
        this.ccInstituteTypeTech = ccInstituteTypeTech;
    }
  	public String getCcInstituteTypeEconomic() {
        return ccInstituteTypeEconomic;
    }

    public void setCcInstituteTypeEconomic(String ccInstituteTypeEconomic) {
        this.ccInstituteTypeEconomic = ccInstituteTypeEconomic;
    }
  	public String getCcIndustryIssuer() {
        return ccIndustryIssuer;
    }

    public void setCcIndustryIssuer(String ccIndustryIssuer) {
        this.ccIndustryIssuer = ccIndustryIssuer;
    }
  	public String getCcRegistDeposit() {
        return ccRegistDeposit;
    }

    public void setCcRegistDeposit(String ccRegistDeposit) {
        this.ccRegistDeposit = ccRegistDeposit;
    }
  	public String getCcDetailsRegistDeposit() {
        return ccDetailsRegistDeposit;
    }

    public void setCcDetailsRegistDeposit(String ccDetailsRegistDeposit) {
        this.ccDetailsRegistDeposit = ccDetailsRegistDeposit;
    }
  	public String getDdValueDate() {
        return ddValueDate;
    }

    public void setDdValueDate(String ddValueDate) {
        this.ddValueDate = ddValueDate;
    }
  	public String getDdMaturityDate() {
        return ddMaturityDate;
    }

    public void setDdMaturityDate(String ddMaturityDate) {
        this.ddMaturityDate = ddMaturityDate;
    }
  	public String getDdCounterparty() {
        return ddCounterparty;
    }

    public void setDdCounterparty(String ddCounterparty) {
        this.ddCounterparty = ddCounterparty;
    }
  	public String getDdCounterpartyType() {
        return ddCounterpartyType;
    }

    public void setDdCounterpartyType(String ddCounterpartyType) {
        this.ddCounterpartyType = ddCounterpartyType;
    }
  	public String getDdAnnalInterestRate() {
        return ddAnnalInterestRate;
    }

    public void setDdAnnalInterestRate(String ddAnnalInterestRate) {
        this.ddAnnalInterestRate = ddAnnalInterestRate;
    }
  	public String getDdInterestBasis() {
        return ddInterestBasis;
    }

    public void setDdInterestBasis(String ddInterestBasis) {
        this.ddInterestBasis = ddInterestBasis;
    }
  	public String getDdCollateralType() {
        return ddCollateralType;
    }

    public void setDdCollateralType(String ddCollateralType) {
        this.ddCollateralType = ddCollateralType;
    }
  	public String getDdCollateralValue() {
        return ddCollateralValue;
    }

    public void setDdCollateralValue(String ddCollateralValue) {
        this.ddCollateralValue = ddCollateralValue;
    }
  	public String getEeName() {
        return eeName;
    }

    public void setEeName(String eeName) {
        this.eeName = eeName;
    }
  	public String getEeAmt() {
        return eeAmt;
    }

    public void setEeAmt(String eeAmt) {
        this.eeAmt = eeAmt;
    }
  	public String getEeUnitParValue() {
        return eeUnitParValue;
    }

    public void setEeUnitParValue(String eeUnitParValue) {
        this.eeUnitParValue = eeUnitParValue;
    }
  	public String getEeOwnershipType() {
        return eeOwnershipType;
    }

    public void setEeOwnershipType(String eeOwnershipType) {
        this.eeOwnershipType = eeOwnershipType;
    }
  	public String getEeBuyback() {
        return eeBuyback;
    }

    public void setEeBuyback(String eeBuyback) {
        this.eeBuyback = eeBuyback;
    }
  	public String getEeValueDate() {
        return eeValueDate;
    }

    public void setEeValueDate(String eeValueDate) {
        this.eeValueDate = eeValueDate;
    }
  	public String getEeMaturityDate() {
        return eeMaturityDate;
    }

    public void setEeMaturityDate(String eeMaturityDate) {
        this.eeMaturityDate = eeMaturityDate;
    }
  	public String getEeStatutoryMaturityDate() {
        return eeStatutoryMaturityDate;
    }

    public void setEeStatutoryMaturityDate(String eeStatutoryMaturityDate) {
        this.eeStatutoryMaturityDate = eeStatutoryMaturityDate;
    }
  	public String getEeExpectedReturn() {
        return eeExpectedReturn;
    }

    public void setEeExpectedReturn(String eeExpectedReturn) {
        this.eeExpectedReturn = eeExpectedReturn;
    }
  	public String getEeProjectAnnaulReturn() {
        return eeProjectAnnaulReturn;
    }

    public void setEeProjectAnnaulReturn(String eeProjectAnnaulReturn) {
        this.eeProjectAnnaulReturn = eeProjectAnnaulReturn;
    }
  	public String getEeCouponType() {
        return eeCouponType;
    }

    public void setEeCouponType(String eeCouponType) {
        this.eeCouponType = eeCouponType;
    }
  	public String getEeRegualrInterestPay() {
        return eeRegualrInterestPay;
    }

    public void setEeRegualrInterestPay(String eeRegualrInterestPay) {
        this.eeRegualrInterestPay = eeRegualrInterestPay;
    }
  	public String getEeInterestPayFrequency() {
        return eeInterestPayFrequency;
    }

    public void setEeInterestPayFrequency(String eeInterestPayFrequency) {
        this.eeInterestPayFrequency = eeInterestPayFrequency;
    }
  	public String getEeCouponAllocationType() {
        return eeCouponAllocationType;
    }

    public void setEeCouponAllocationType(String eeCouponAllocationType) {
        this.eeCouponAllocationType = eeCouponAllocationType;
    }
  	public String getEeDetailPrincInterest() {
        return eeDetailPrincInterest;
    }

    public void setEeDetailPrincInterest(String eeDetailPrincInterest) {
        this.eeDetailPrincInterest = eeDetailPrincInterest;
    }
  	public String getEeInterestBasis() {
        return eeInterestBasis;
    }

    public void setEeInterestBasis(String eeInterestBasis) {
        this.eeInterestBasis = eeInterestBasis;
    }
  	public String getEeBenchRateType() {
        return eeBenchRateType;
    }

    public void setEeBenchRateType(String eeBenchRateType) {
        this.eeBenchRateType = eeBenchRateType;
    }
  	public String getEeFloatFactor() {
        return eeFloatFactor;
    }

    public void setEeFloatFactor(String eeFloatFactor) {
        this.eeFloatFactor = eeFloatFactor;
    }
  	public String getEeFloatRate() {
        return eeFloatRate;
    }

    public void setEeFloatRate(String eeFloatRate) {
        this.eeFloatRate = eeFloatRate;
    }
  	public String getEeYieldSpreadBp() {
        return eeYieldSpreadBp;
    }

    public void setEeYieldSpreadBp(String eeYieldSpreadBp) {
        this.eeYieldSpreadBp = eeYieldSpreadBp;
    }
  	public String getEeStructGrade() {
        return eeStructGrade;
    }

    public void setEeStructGrade(String eeStructGrade) {
        this.eeStructGrade = eeStructGrade;
    }
  	public String getEePrincPaymentType() {
        return eePrincPaymentType;
    }

    public void setEePrincPaymentType(String eePrincPaymentType) {
        this.eePrincPaymentType = eePrincPaymentType;
    }
  	public String getEeInstallRepayType() {
        return eeInstallRepayType;
    }

    public void setEeInstallRepayType(String eeInstallRepayType) {
        this.eeInstallRepayType = eeInstallRepayType;
    }
  	public String getEeBaseAssetType() {
        return eeBaseAssetType;
    }

    public void setEeBaseAssetType(String eeBaseAssetType) {
        this.eeBaseAssetType = eeBaseAssetType;
    }
  	public String getEePercentExcInAllot() {
        return eePercentExcInAllot;
    }

    public void setEePercentExcInAllot(String eePercentExcInAllot) {
        this.eePercentExcInAllot = eePercentExcInAllot;
    }
  	public String getEeDebtor() {
        return eeDebtor;
    }

    public void setEeDebtor(String eeDebtor) {
        this.eeDebtor = eeDebtor;
    }
  	public String getEeDeptorRate() {
        return eeDeptorRate;
    }

    public void setEeDeptorRate(String eeDeptorRate) {
        this.eeDeptorRate = eeDeptorRate;
    }
  	public String getEeRateAgencyIss() {
        return eeRateAgencyIss;
    }

    public void setEeRateAgencyIss(String eeRateAgencyIss) {
        this.eeRateAgencyIss = eeRateAgencyIss;
    }
  	public String getEeDebtorTypeScale() {
        return eeDebtorTypeScale;
    }

    public void setEeDebtorTypeScale(String eeDebtorTypeScale) {
        this.eeDebtorTypeScale = eeDebtorTypeScale;
    }
  	public String getEeDebtorTypeTech() {
        return eeDebtorTypeTech;
    }

    public void setEeDebtorTypeTech(String eeDebtorTypeTech) {
        this.eeDebtorTypeTech = eeDebtorTypeTech;
    }
  	public String getEeDebtorTypeEconomic() {
        return eeDebtorTypeEconomic;
    }

    public void setEeDebtorTypeEconomic(String eeDebtorTypeEconomic) {
        this.eeDebtorTypeEconomic = eeDebtorTypeEconomic;
    }
  	public String getEeProject() {
        return eeProject;
    }

    public void setEeProject(String eeProject) {
        this.eeProject = eeProject;
    }
  	public String getEeIndustryDebtor() {
        return eeIndustryDebtor;
    }

    public void setEeIndustryDebtor(String eeIndustryDebtor) {
        this.eeIndustryDebtor = eeIndustryDebtor;
    }
  	public String getEeIndustryProject() {
        return eeIndustryProject;
    }

    public void setEeIndustryProject(String eeIndustryProject) {
        this.eeIndustryProject = eeIndustryProject;
    }
  	public String getEeMonitorIndusType() {
        return eeMonitorIndusType;
    }

    public void setEeMonitorIndusType(String eeMonitorIndusType) {
        this.eeMonitorIndusType = eeMonitorIndusType;
    }
  	public String getEeMonitorIndustryType() {
        return eeMonitorIndustryType;
    }

    public void setEeMonitorIndustryType(String eeMonitorIndustryType) {
        this.eeMonitorIndustryType = eeMonitorIndustryType;
    }
  	public String getEeDetailsMonitoryType() {
        return eeDetailsMonitoryType;
    }

    public void setEeDetailsMonitoryType(String eeDetailsMonitoryType) {
        this.eeDetailsMonitoryType = eeDetailsMonitoryType;
    }
  	public String getEeGuaranteeMethod() {
        return eeGuaranteeMethod;
    }

    public void setEeGuaranteeMethod(String eeGuaranteeMethod) {
        this.eeGuaranteeMethod = eeGuaranteeMethod;
    }
  	public String getEeDetailGuaranteeStatus() {
        return eeDetailGuaranteeStatus;
    }

    public void setEeDetailGuaranteeStatus(String eeDetailGuaranteeStatus) {
        this.eeDetailGuaranteeStatus = eeDetailGuaranteeStatus;
    }
  	public String getEePledgeType() {
        return eePledgeType;
    }

    public void setEePledgeType(String eePledgeType) {
        this.eePledgeType = eePledgeType;
    }
  	public String getEePledgeValue() {
        return eePledgeValue;
    }

    public void setEePledgeValue(String eePledgeValue) {
        this.eePledgeValue = eePledgeValue;
    }
  	public String getEeGuaranteeType() {
        return eeGuaranteeType;
    }

    public void setEeGuaranteeType(String eeGuaranteeType) {
        this.eeGuaranteeType = eeGuaranteeType;
    }
  	public String getEeGuarantorType() {
        return eeGuarantorType;
    }

    public void setEeGuarantorType(String eeGuarantorType) {
        this.eeGuarantorType = eeGuarantorType;
    }
  	public String getEeDebtorRate() {
        return eeDebtorRate;
    }

    public void setEeDebtorRate(String eeDebtorRate) {
        this.eeDebtorRate = eeDebtorRate;
    }
  	public String getEeInterAssetRate() {
        return eeInterAssetRate;
    }

    public void setEeInterAssetRate(String eeInterAssetRate) {
        this.eeInterAssetRate = eeInterAssetRate;
    }
  	public String getEeOutAssetRate() {
        return eeOutAssetRate;
    }

    public void setEeOutAssetRate(String eeOutAssetRate) {
        this.eeOutAssetRate = eeOutAssetRate;
    }
  	public String getEeOptionType() {
        return eeOptionType;
    }

    public void setEeOptionType(String eeOptionType) {
        this.eeOptionType = eeOptionType;
    }
  	public String getEeExerciseDateType() {
        return eeExerciseDateType;
    }

    public void setEeExerciseDateType(String eeExerciseDateType) {
        this.eeExerciseDateType = eeExerciseDateType;
    }
  	public String getEeFixedExerciseDate() {
        return eeFixedExerciseDate;
    }

    public void setEeFixedExerciseDate(String eeFixedExerciseDate) {
        this.eeFixedExerciseDate = eeFixedExerciseDate;
    }
  	public String getEeFirstExerciseDate() {
        return eeFirstExerciseDate;
    }

    public void setEeFirstExerciseDate(String eeFirstExerciseDate) {
        this.eeFirstExerciseDate = eeFirstExerciseDate;
    }
  	public String getEeExercisePeriod() {
        return eeExercisePeriod;
    }

    public void setEeExercisePeriod(String eeExercisePeriod) {
        this.eeExercisePeriod = eeExercisePeriod;
    }
  	public String getEeExercisePrice() {
        return eeExercisePrice;
    }

    public void setEeExercisePrice(String eeExercisePrice) {
        this.eeExercisePrice = eeExercisePrice;
    }
  	public String getEePerpetualType() {
        return eePerpetualType;
    }

    public void setEePerpetualType(String eePerpetualType) {
        this.eePerpetualType = eePerpetualType;
    }
  	public String getEeDeferreInterestType() {
        return eeDeferreInterestType;
    }

    public void setEeDeferreInterestType(String eeDeferreInterestType) {
        this.eeDeferreInterestType = eeDeferreInterestType;
    }
  	public String getEeInterestDeferred() {
        return eeInterestDeferred;
    }

    public void setEeInterestDeferred(String eeInterestDeferred) {
        this.eeInterestDeferred = eeInterestDeferred;
    }
  	public String getEeFirstRepriceDate() {
        return eeFirstRepriceDate;
    }

    public void setEeFirstRepriceDate(String eeFirstRepriceDate) {
        this.eeFirstRepriceDate = eeFirstRepriceDate;
    }
  	public String getEeRepricePeriod() {
        return eeRepricePeriod;
    }

    public void setEeRepricePeriod(String eeRepricePeriod) {
        this.eeRepricePeriod = eeRepricePeriod;
    }
  	public String getEePartialRedemption() {
        return eePartialRedemption;
    }

    public void setEePartialRedemption(String eePartialRedemption) {
        this.eePartialRedemption = eePartialRedemption;
    }
  	public String getEePartialRedemptionRate() {
        return eePartialRedemptionRate;
    }

    public void setEePartialRedemptionRate(String eePartialRedemptionRate) {
        this.eePartialRedemptionRate = eePartialRedemptionRate;
    }
  	public String getEeOptionRight() {
        return eeOptionRight;
    }

    public void setEeOptionRight(String eeOptionRight) {
        this.eeOptionRight = eeOptionRight;
    }
  	public String getEeDetailsExerciseTerm() {
        return eeDetailsExerciseTerm;
    }

    public void setEeDetailsExerciseTerm(String eeDetailsExerciseTerm) {
        this.eeDetailsExerciseTerm = eeDetailsExerciseTerm;
    }
  	public String getEeRegionDebtor() {
        return eeRegionDebtor;
    }

    public void setEeRegionDebtor(String eeRegionDebtor) {
        this.eeRegionDebtor = eeRegionDebtor;
    }
  	public String getEeEnhanceInstituteCode() {
        return eeEnhanceInstituteCode;
    }

    public void setEeEnhanceInstituteCode(String eeEnhanceInstituteCode) {
        this.eeEnhanceInstituteCode = eeEnhanceInstituteCode;
    }
  	public String getEeEnhanceInstituteName() {
        return eeEnhanceInstituteName;
    }

    public void setEeEnhanceInstituteName(String eeEnhanceInstituteName) {
        this.eeEnhanceInstituteName = eeEnhanceInstituteName;
    }
  	public Double getEeTotalFeeRate() {
        return eeTotalFeeRate;
    }

    public void setEeTotalFeeRate(Double eeTotalFeeRate) {
        this.eeTotalFeeRate = eeTotalFeeRate;
    }
  	public String getEeOrganizationCode() {
        return eeOrganizationCode;
    }

    public void setEeOrganizationCode(String eeOrganizationCode) {
        this.eeOrganizationCode = eeOrganizationCode;
    }
  	public String getFfOwnership() {
        return ffOwnership;
    }

    public void setFfOwnership(String ffOwnership) {
        this.ffOwnership = ffOwnership;
    }
  	public String getFfBuyback() {
        return ffBuyback;
    }

    public void setFfBuyback(String ffBuyback) {
        this.ffBuyback = ffBuyback;
    }
  	public String getFfType() {
        return ffType;
    }

    public void setFfType(String ffType) {
        this.ffType = ffType;
    }
  	public String getFfQuantity() {
        return ffQuantity;
    }

    public void setFfQuantity(String ffQuantity) {
        this.ffQuantity = ffQuantity;
    }
  	public String getFfAggregateAmt() {
        return ffAggregateAmt;
    }

    public void setFfAggregateAmt(String ffAggregateAmt) {
        this.ffAggregateAmt = ffAggregateAmt;
    }
  	public String getFfWeightRemainDay() {
        return ffWeightRemainDay;
    }

    public void setFfWeightRemainDay(String ffWeightRemainDay) {
        this.ffWeightRemainDay = ffWeightRemainDay;
    }
  	public String getFfMaxRemainDay() {
        return ffMaxRemainDay;
    }

    public void setFfMaxRemainDay(String ffMaxRemainDay) {
        this.ffMaxRemainDay = ffMaxRemainDay;
    }
  	public String getFfMinRemainFay() {
        return ffMinRemainFay;
    }

    public void setFfMinRemainFay(String ffMinRemainFay) {
        this.ffMinRemainFay = ffMinRemainFay;
    }
  	public String getFfMaturityDate() {
        return ffMaturityDate;
    }

    public void setFfMaturityDate(String ffMaturityDate) {
        this.ffMaturityDate = ffMaturityDate;
    }
  	public String getFfValueDate() {
        return ffValueDate;
    }

    public void setFfValueDate(String ffValueDate) {
        this.ffValueDate = ffValueDate;
    }
  	public String getFfDiscountRate() {
        return ffDiscountRate;
    }

    public void setFfDiscountRate(String ffDiscountRate) {
        this.ffDiscountRate = ffDiscountRate;
    }
  	public String getFfIndustry() {
        return ffIndustry;
    }

    public void setFfIndustry(String ffIndustry) {
        this.ffIndustry = ffIndustry;
    }
  	public String getGgStockCode() {
        return ggStockCode;
    }

    public void setGgStockCode(String ggStockCode) {
        this.ggStockCode = ggStockCode;
    }
  	public String getGgName() {
        return ggName;
    }

    public void setGgName(String ggName) {
        this.ggName = ggName;
    }
  	public String getGgStockType() {
        return ggStockType;
    }

    public void setGgStockType(String ggStockType) {
        this.ggStockType = ggStockType;
    }
  	public String getGgIndustry() {
        return ggIndustry;
    }

    public void setGgIndustry(String ggIndustry) {
        this.ggIndustry = ggIndustry;
    }
  	public String getGgInvestStage() {
        return ggInvestStage;
    }

    public void setGgInvestStage(String ggInvestStage) {
        this.ggInvestStage = ggInvestStage;
    }
  	public String getGgEquityOutDate() {
        return ggEquityOutDate;
    }

    public void setGgEquityOutDate(String ggEquityOutDate) {
        this.ggEquityOutDate = ggEquityOutDate;
    }
  	public String getGgEnterTypeScale() {
        return ggEnterTypeScale;
    }

    public void setGgEnterTypeScale(String ggEnterTypeScale) {
        this.ggEnterTypeScale = ggEnterTypeScale;
    }
  	public String getGgEnterTypeTech() {
        return ggEnterTypeTech;
    }

    public void setGgEnterTypeTech(String ggEnterTypeTech) {
        this.ggEnterTypeTech = ggEnterTypeTech;
    }
  	public String getGgEnterTypeEconomic() {
        return ggEnterTypeEconomic;
    }

    public void setGgEnterTypeEconomic(String ggEnterTypeEconomic) {
        this.ggEnterTypeEconomic = ggEnterTypeEconomic;
    }
  	public String getGgPledgedFinace() {
        return ggPledgedFinace;
    }

    public void setGgPledgedFinace(String ggPledgedFinace) {
        this.ggPledgedFinace = ggPledgedFinace;
    }
  	public String getGgDebtEquitySwap() {
        return ggDebtEquitySwap;
    }

    public void setGgDebtEquitySwap(String ggDebtEquitySwap) {
        this.ggDebtEquitySwap = ggDebtEquitySwap;
    }
  	public String getHhName() {
        return hhName;
    }

    public void setHhName(String hhName) {
        this.hhName = hhName;
    }
  	public String getHhNominalPrincipal() {
        return hhNominalPrincipal;
    }

    public void setHhNominalPrincipal(String hhNominalPrincipal) {
        this.hhNominalPrincipal = hhNominalPrincipal;
    }
  	public String getHhUnderAssetType() {
        return hhUnderAssetType;
    }

    public void setHhUnderAssetType(String hhUnderAssetType) {
        this.hhUnderAssetType = hhUnderAssetType;
    }
  	public String getHhHoldObjective() {
        return hhHoldObjective;
    }

    public void setHhHoldObjective(String hhHoldObjective) {
        this.hhHoldObjective = hhHoldObjective;
    }
  	public String getIiCountyRegion() {
        return iiCountyRegion;
    }

    public void setIiCountyRegion(String iiCountyRegion) {
        this.iiCountyRegion = iiCountyRegion;
    }
  	public String getIiBondName() {
        return iiBondName;
    }

    public void setIiBondName(String iiBondName) {
        this.iiBondName = iiBondName;
    }
  	public String getIiBondIdentCode() {
        return iiBondIdentCode;
    }

    public void setIiBondIdentCode(String iiBondIdentCode) {
        this.iiBondIdentCode = iiBondIdentCode;
    }
  	public String getIiIssuer() {
        return iiIssuer;
    }

    public void setIiIssuer(String iiIssuer) {
        this.iiIssuer = iiIssuer;
    }
  	public String getIiIndustryIssuer() {
        return iiIndustryIssuer;
    }

    public void setIiIndustryIssuer(String iiIndustryIssuer) {
        this.iiIndustryIssuer = iiIndustryIssuer;
    }
  	public String getIiValueDate() {
        return iiValueDate;
    }

    public void setIiValueDate(String iiValueDate) {
        this.iiValueDate = iiValueDate;
    }
  	public String getIiMaturityDate() {
        return iiMaturityDate;
    }

    public void setIiMaturityDate(String iiMaturityDate) {
        this.iiMaturityDate = iiMaturityDate;
    }
  	public String getIiTermMaturity() {
        return iiTermMaturity;
    }

    public void setIiTermMaturity(String iiTermMaturity) {
        this.iiTermMaturity = iiTermMaturity;
    }
  	public String getIiIssuerRateBond() {
        return iiIssuerRateBond;
    }

    public void setIiIssuerRateBond(String iiIssuerRateBond) {
        this.iiIssuerRateBond = iiIssuerRateBond;
    }
  	public String getIiBondRate() {
        return iiBondRate;
    }

    public void setIiBondRate(String iiBondRate) {
        this.iiBondRate = iiBondRate;
    }
  	public String getIiCoupRate() {
        return iiCoupRate;
    }

    public void setIiCoupRate(String iiCoupRate) {
        this.iiCoupRate = iiCoupRate;
    }
  	public String getIiInterestPayQuency() {
        return iiInterestPayQuency;
    }

    public void setIiInterestPayQuency(String iiInterestPayQuency) {
        this.iiInterestPayQuency = iiInterestPayQuency;
    }
  	public String getIiDetailsAssureStatus() {
        return iiDetailsAssureStatus;
    }

    public void setIiDetailsAssureStatus(String iiDetailsAssureStatus) {
        this.iiDetailsAssureStatus = iiDetailsAssureStatus;
    }
  	public String getIiDetailsSpecialTerms() {
        return iiDetailsSpecialTerms;
    }

    public void setIiDetailsSpecialTerms(String iiDetailsSpecialTerms) {
        this.iiDetailsSpecialTerms = iiDetailsSpecialTerms;
    }
  	public String getJjCountry() {
        return jjCountry;
    }

    public void setJjCountry(String jjCountry) {
        this.jjCountry = jjCountry;
    }
  	public String getJjValueDate() {
        return jjValueDate;
    }

    public void setJjValueDate(String jjValueDate) {
        this.jjValueDate = jjValueDate;
    }
  	public String getJjMaturityDate() {
        return jjMaturityDate;
    }

    public void setJjMaturityDate(String jjMaturityDate) {
        this.jjMaturityDate = jjMaturityDate;
    }
  	public String getJjCounterparty() {
        return jjCounterparty;
    }

    public void setJjCounterparty(String jjCounterparty) {
        this.jjCounterparty = jjCounterparty;
    }
  	public String getJjInterestRate() {
        return jjInterestRate;
    }

    public void setJjInterestRate(String jjInterestRate) {
        this.jjInterestRate = jjInterestRate;
    }
  	public String getJjInterestBasis() {
        return jjInterestBasis;
    }

    public void setJjInterestBasis(String jjInterestBasis) {
        this.jjInterestBasis = jjInterestBasis;
    }
  	public String getKkCountry() {
        return kkCountry;
    }

    public void setKkCountry(String kkCountry) {
        this.kkCountry = kkCountry;
    }
  	public String getKkIdentCode() {
        return kkIdentCode;
    }

    public void setKkIdentCode(String kkIdentCode) {
        this.kkIdentCode = kkIdentCode;
    }
  	public String getKkName() {
        return kkName;
    }

    public void setKkName(String kkName) {
        this.kkName = kkName;
    }
  	public String getKkIssuer() {
        return kkIssuer;
    }

    public void setKkIssuer(String kkIssuer) {
        this.kkIssuer = kkIssuer;
    }
  	public String getKkIndustry() {
        return kkIndustry;
    }

    public void setKkIndustry(String kkIndustry) {
        this.kkIndustry = kkIndustry;
    }
  	public String getLlCountry() {
        return llCountry;
    }

    public void setLlCountry(String llCountry) {
        this.llCountry = llCountry;
    }
  	public String getLlContractName() {
        return llContractName;
    }

    public void setLlContractName(String llContractName) {
        this.llContractName = llContractName;
    }
  	public String getLlValueDate() {
        return llValueDate;
    }

    public void setLlValueDate(String llValueDate) {
        this.llValueDate = llValueDate;
    }
  	public String getLlMaturityDate() {
        return llMaturityDate;
    }

    public void setLlMaturityDate(String llMaturityDate) {
        this.llMaturityDate = llMaturityDate;
    }
  	public String getLlCouponRate() {
        return llCouponRate;
    }

    public void setLlCouponRate(String llCouponRate) {
        this.llCouponRate = llCouponRate;
    }
  	public String getLlInterestFrequency() {
        return llInterestFrequency;
    }

    public void setLlInterestFrequency(String llInterestFrequency) {
        this.llInterestFrequency = llInterestFrequency;
    }
  	public String getLlPercentFix() {
        return llPercentFix;
    }

    public void setLlPercentFix(String llPercentFix) {
        this.llPercentFix = llPercentFix;
    }
  	public String getLlPercentDerivate() {
        return llPercentDerivate;
    }

    public void setLlPercentDerivate(String llPercentDerivate) {
        this.llPercentDerivate = llPercentDerivate;
    }
  	public String getLlDerivateInvetType() {
        return llDerivateInvetType;
    }

    public void setLlDerivateInvetType(String llDerivateInvetType) {
        this.llDerivateInvetType = llDerivateInvetType;
    }
  	public String getLlUnderAsset() {
        return llUnderAsset;
    }

    public void setLlUnderAsset(String llUnderAsset) {
        this.llUnderAsset = llUnderAsset;
    }
  	public String getLlDetailsProceeds() {
        return llDetailsProceeds;
    }

    public void setLlDetailsProceeds(String llDetailsProceeds) {
        this.llDetailsProceeds = llDetailsProceeds;
    }
  	public String getLlDetailsOption() {
        return llDetailsOption;
    }

    public void setLlDetailsOption(String llDetailsOption) {
        this.llDetailsOption = llDetailsOption;
    }
  	public String getLlMaxNoteReturn() {
        return llMaxNoteReturn;
    }

    public void setLlMaxNoteReturn(String llMaxNoteReturn) {
        this.llMaxNoteReturn = llMaxNoteReturn;
    }
  	public String getLlMinNoteReturn() {
        return llMinNoteReturn;
    }

    public void setLlMinNoteReturn(String llMinNoteReturn) {
        this.llMinNoteReturn = llMinNoteReturn;
    }
  	public String getLlStrikeUnderAsset() {
        return llStrikeUnderAsset;
    }

    public void setLlStrikeUnderAsset(String llStrikeUnderAsset) {
        this.llStrikeUnderAsset = llStrikeUnderAsset;
    }
  	public String getLlUnderRgPrice() {
        return llUnderRgPrice;
    }

    public void setLlUnderRgPrice(String llUnderRgPrice) {
        this.llUnderRgPrice = llUnderRgPrice;
    }
  	public String getLlTransCosts() {
        return llTransCosts;
    }

    public void setLlTransCosts(String llTransCosts) {
        this.llTransCosts = llTransCosts;
    }
  	public String getMmManagePlanName() {
        return mmManagePlanName;
    }

    public void setMmManagePlanName(String mmManagePlanName) {
        this.mmManagePlanName = mmManagePlanName;
    }
  	public String getMmIssuedAssetCompany() {
        return mmIssuedAssetCompany;
    }

    public void setMmIssuedAssetCompany(String mmIssuedAssetCompany) {
        this.mmIssuedAssetCompany = mmIssuedAssetCompany;
    }
  	public String getMmPlanIssuerCode() {
        return mmPlanIssuerCode;
    }

    public void setMmPlanIssuerCode(String mmPlanIssuerCode) {
        this.mmPlanIssuerCode = mmPlanIssuerCode;
    }
  	public String getMmAssetPlanRgCode() {
        return mmAssetPlanRgCode;
    }

    public void setMmAssetPlanRgCode(String mmAssetPlanRgCode) {
        this.mmAssetPlanRgCode = mmAssetPlanRgCode;
    }
  	public String getMmManager() {
        return mmManager;
    }

    public void setMmManager(String mmManager) {
        this.mmManager = mmManager;
    }
  	public String getMmCustodian() {
        return mmCustodian;
    }

    public void setMmCustodian(String mmCustodian) {
        this.mmCustodian = mmCustodian;
    }
  	public String getMmAmt() {
        return mmAmt;
    }

    public void setMmAmt(String mmAmt) {
        this.mmAmt = mmAmt;
    }
  	public String getMmActualDirect() {
        return mmActualDirect;
    }

    public void setMmActualDirect(String mmActualDirect) {
        this.mmActualDirect = mmActualDirect;
    }
  	public String getMmDetailsInvest() {
        return mmDetailsInvest;
    }

    public void setMmDetailsInvest(String mmDetailsInvest) {
        this.mmDetailsInvest = mmDetailsInvest;
    }
  	public String getMmIndustryInvest() {
        return mmIndustryInvest;
    }

    public void setMmIndustryInvest(String mmIndustryInvest) {
        this.mmIndustryInvest = mmIndustryInvest;
    }
  	public String getMmPlanStartDate() {
        return mmPlanStartDate;
    }

    public void setMmPlanStartDate(String mmPlanStartDate) {
        this.mmPlanStartDate = mmPlanStartDate;
    }
  	public String getMmPlanMaturityDate() {
        return mmPlanMaturityDate;
    }

    public void setMmPlanMaturityDate(String mmPlanMaturityDate) {
        this.mmPlanMaturityDate = mmPlanMaturityDate;
    }
  	public String getMmPlanType() {
        return mmPlanType;
    }

    public void setMmPlanType(String mmPlanType) {
        this.mmPlanType = mmPlanType;
    }
  	public String getMmExpectedReturn() {
        return mmExpectedReturn;
    }

    public void setMmExpectedReturn(String mmExpectedReturn) {
        this.mmExpectedReturn = mmExpectedReturn;
    }
  	public Double getMmMaxExpectedReturn() {
        return mmMaxExpectedReturn;
    }

    public void setMmMaxExpectedReturn(Double mmMaxExpectedReturn) {
        this.mmMaxExpectedReturn = mmMaxExpectedReturn;
    }
  	public Double getMmMinExpectedReturn() {
        return mmMinExpectedReturn;
    }

    public void setMmMinExpectedReturn(Double mmMinExpectedReturn) {
        this.mmMinExpectedReturn = mmMinExpectedReturn;
    }
  	public String getMmInvestStructure() {
        return mmInvestStructure;
    }

    public void setMmInvestStructure(String mmInvestStructure) {
        this.mmInvestStructure = mmInvestStructure;
    }
  	public String getMmManagerType() {
        return mmManagerType;
    }

    public void setMmManagerType(String mmManagerType) {
        this.mmManagerType = mmManagerType;
    }
  	public Double getMmManagerFeeRate() {
        return mmManagerFeeRate;
    }

    public void setMmManagerFeeRate(Double mmManagerFeeRate) {
        this.mmManagerFeeRate = mmManagerFeeRate;
    }
  	public Double getMmCustodianFeeRate() {
        return mmCustodianFeeRate;
    }

    public void setMmCustodianFeeRate(Double mmCustodianFeeRate) {
        this.mmCustodianFeeRate = mmCustodianFeeRate;
    }
  	public Double getMmTransCostRate() {
        return mmTransCostRate;
    }

    public void setMmTransCostRate(Double mmTransCostRate) {
        this.mmTransCostRate = mmTransCostRate;
    }
  	public Double getMmInterFeeRate() {
        return mmInterFeeRate;
    }

    public void setMmInterFeeRate(Double mmInterFeeRate) {
        this.mmInterFeeRate = mmInterFeeRate;
    }
  	public String getMmOtherExpenseRate() {
        return mmOtherExpenseRate;
    }

    public void setMmOtherExpenseRate(String mmOtherExpenseRate) {
        this.mmOtherExpenseRate = mmOtherExpenseRate;
    }
  	public String getNnCountry() {
        return nnCountry;
    }

    public void setNnCountry(String nnCountry) {
        this.nnCountry = nnCountry;
    }
  	public String getNnName() {
        return nnName;
    }

    public void setNnName(String nnName) {
        this.nnName = nnName;
    }
  	public String getNnTermDays() {
        return nnTermDays;
    }

    public void setNnTermDays(String nnTermDays) {
        this.nnTermDays = nnTermDays;
    }
  	public String getNnAssetValue() {
        return nnAssetValue;
    }

    public void setNnAssetValue(String nnAssetValue) {
        this.nnAssetValue = nnAssetValue;
    }
  	public String getNnAssetReturn() {
        return nnAssetReturn;
    }

    public void setNnAssetReturn(String nnAssetReturn) {
        this.nnAssetReturn = nnAssetReturn;
    }
  	public String getOoCountry() {
        return ooCountry;
    }

    public void setOoCountry(String ooCountry) {
        this.ooCountry = ooCountry;
    }
  	public String getOoName() {
        return ooName;
    }

    public void setOoName(String ooName) {
        this.ooName = ooName;
    }
  	public String getOoValueDate() {
        return ooValueDate;
    }

    public void setOoValueDate(String ooValueDate) {
        this.ooValueDate = ooValueDate;
    }
  	public String getOoMaturityDate() {
        return ooMaturityDate;
    }

    public void setOoMaturityDate(String ooMaturityDate) {
        this.ooMaturityDate = ooMaturityDate;
    }
  	public String getOoAssetValue() {
        return ooAssetValue;
    }

    public void setOoAssetValue(String ooAssetValue) {
        this.ooAssetValue = ooAssetValue;
    }
  	public String getOoAssetReturn() {
        return ooAssetReturn;
    }

    public void setOoAssetReturn(String ooAssetReturn) {
        this.ooAssetReturn = ooAssetReturn;
    }
  	public String getPpFundCode() {
        return ppFundCode;
    }

    public void setPpFundCode(String ppFundCode) {
        this.ppFundCode = ppFundCode;
    }
  	public String getPpFundName() {
        return ppFundName;
    }

    public void setPpFundName(String ppFundName) {
        this.ppFundName = ppFundName;
    }
  	public String getPpIssuedAssetCompany() {
        return ppIssuedAssetCompany;
    }

    public void setPpIssuedAssetCompany(String ppIssuedAssetCompany) {
        this.ppIssuedAssetCompany = ppIssuedAssetCompany;
    }
  	public String getPpIndustry() {
        return ppIndustry;
    }

    public void setPpIndustry(String ppIndustry) {
        this.ppIndustry = ppIndustry;
    }
  	public String getPpRegistAgency() {
        return ppRegistAgency;
    }

    public void setPpRegistAgency(String ppRegistAgency) {
        this.ppRegistAgency = ppRegistAgency;
    }
  	public String getPpGovernInvestFund() {
        return ppGovernInvestFund;
    }

    public void setPpGovernInvestFund(String ppGovernInvestFund) {
        this.ppGovernInvestFund = ppGovernInvestFund;
    }
  	public String getPpDirectGovernFund() {
        return ppDirectGovernFund;
    }

    public void setPpDirectGovernFund(String ppDirectGovernFund) {
        this.ppDirectGovernFund = ppDirectGovernFund;
    }
  	public String getPpTaName() {
        return ppTaName;
    }

    public void setPpTaName(String ppTaName) {
        this.ppTaName = ppTaName;
    }
  	public String getPpManagerFundName() {
        return ppManagerFundName;
    }

    public void setPpManagerFundName(String ppManagerFundName) {
        this.ppManagerFundName = ppManagerFundName;
    }
  	public String getPpCustodianFundName() {
        return ppCustodianFundName;
    }

    public void setPpCustodianFundName(String ppCustodianFundName) {
        this.ppCustodianFundName = ppCustodianFundName;
    }
  	public String getPpInvestStage() {
        return ppInvestStage;
    }

    public void setPpInvestStage(String ppInvestStage) {
        this.ppInvestStage = ppInvestStage;
    }
  	public String getPpEnterTypeScale() {
        return ppEnterTypeScale;
    }

    public void setPpEnterTypeScale(String ppEnterTypeScale) {
        this.ppEnterTypeScale = ppEnterTypeScale;
    }
  	public String getPpEnterTypeTech() {
        return ppEnterTypeTech;
    }

    public void setPpEnterTypeTech(String ppEnterTypeTech) {
        this.ppEnterTypeTech = ppEnterTypeTech;
    }
  	public String getPpEnterTypeEconomic() {
        return ppEnterTypeEconomic;
    }

    public void setPpEnterTypeEconomic(String ppEnterTypeEconomic) {
        this.ppEnterTypeEconomic = ppEnterTypeEconomic;
    }
  	public String getPpInvestAssets() {
        return ppInvestAssets;
    }

    public void setPpInvestAssets(String ppInvestAssets) {
        this.ppInvestAssets = ppInvestAssets;
    }
  	public String getQqOutAgreementName() {
        return qqOutAgreementName;
    }

    public void setQqOutAgreementName(String qqOutAgreementName) {
        this.qqOutAgreementName = qqOutAgreementName;
    }
  	public String getQqOutAgreementCode() {
        return qqOutAgreementCode;
    }

    public void setQqOutAgreementCode(String qqOutAgreementCode) {
        this.qqOutAgreementCode = qqOutAgreementCode;
    }
  	public String getQqTrustee() {
        return qqTrustee;
    }

    public void setQqTrustee(String qqTrustee) {
        this.qqTrustee = qqTrustee;
    }
  	public String getQqActualManager() {
        return qqActualManager;
    }

    public void setQqActualManager(String qqActualManager) {
        this.qqActualManager = qqActualManager;
    }
  	public String getQqCustodian() {
        return qqCustodian;
    }

    public void setQqCustodian(String qqCustodian) {
        this.qqCustodian = qqCustodian;
    }
  	public String getQqOutAmt() {
        return qqOutAmt;
    }

    public void setQqOutAmt(String qqOutAmt) {
        this.qqOutAmt = qqOutAmt;
    }
  	public String getQqActualDirection() {
        return qqActualDirection;
    }

    public void setQqActualDirection(String qqActualDirection) {
        this.qqActualDirection = qqActualDirection;
    }
  	public String getQqDetailsInvest() {
        return qqDetailsInvest;
    }

    public void setQqDetailsInvest(String qqDetailsInvest) {
        this.qqDetailsInvest = qqDetailsInvest;
    }
  	public String getQqIndustryInvest() {
        return qqIndustryInvest;
    }

    public void setQqIndustryInvest(String qqIndustryInvest) {
        this.qqIndustryInvest = qqIndustryInvest;
    }
  	public String getQqValueDate() {
        return qqValueDate;
    }

    public void setQqValueDate(String qqValueDate) {
        this.qqValueDate = qqValueDate;
    }
  	public String getQqMaturityDate() {
        return qqMaturityDate;
    }

    public void setQqMaturityDate(String qqMaturityDate) {
        this.qqMaturityDate = qqMaturityDate;
    }
  	public String getQqOutType() {
        return qqOutType;
    }

    public void setQqOutType(String qqOutType) {
        this.qqOutType = qqOutType;
    }
  	public String getQqExpectedReturn() {
        return qqExpectedReturn;
    }

    public void setQqExpectedReturn(String qqExpectedReturn) {
        this.qqExpectedReturn = qqExpectedReturn;
    }
  	public String getQqMaxExpectedReturn() {
        return qqMaxExpectedReturn;
    }

    public void setQqMaxExpectedReturn(String qqMaxExpectedReturn) {
        this.qqMaxExpectedReturn = qqMaxExpectedReturn;
    }
  	public String getQqMinExpectedReturn() {
        return qqMinExpectedReturn;
    }

    public void setQqMinExpectedReturn(String qqMinExpectedReturn) {
        this.qqMinExpectedReturn = qqMinExpectedReturn;
    }
  	public String getQqManagerFeeRate() {
        return qqManagerFeeRate;
    }

    public void setQqManagerFeeRate(String qqManagerFeeRate) {
        this.qqManagerFeeRate = qqManagerFeeRate;
    }
  	public String getQqCustodianFeeRate() {
        return qqCustodianFeeRate;
    }

    public void setQqCustodianFeeRate(String qqCustodianFeeRate) {
        this.qqCustodianFeeRate = qqCustodianFeeRate;
    }
  	public String getQqTransCostRate() {
        return qqTransCostRate;
    }

    public void setQqTransCostRate(String qqTransCostRate) {
        this.qqTransCostRate = qqTransCostRate;
    }
  	public String getQqInterFeeRate() {
        return qqInterFeeRate;
    }

    public void setQqInterFeeRate(String qqInterFeeRate) {
        this.qqInterFeeRate = qqInterFeeRate;
    }
  	public String getQqOtherExpensesRate() {
        return qqOtherExpensesRate;
    }

    public void setQqOtherExpensesRate(String qqOtherExpensesRate) {
        this.qqOtherExpensesRate = qqOtherExpensesRate;
    }
  	public String getRrCountry() {
        return rrCountry;
    }

    public void setRrCountry(String rrCountry) {
        this.rrCountry = rrCountry;
    }
  	public String getRrName() {
        return rrName;
    }

    public void setRrName(String rrName) {
        this.rrName = rrName;
    }
  	public String getRrTermMaturity() {
        return rrTermMaturity;
    }

    public void setRrTermMaturity(String rrTermMaturity) {
        this.rrTermMaturity = rrTermMaturity;
    }
  	public String getRrLiabilityAmt() {
        return rrLiabilityAmt;
    }

    public void setRrLiabilityAmt(String rrLiabilityAmt) {
        this.rrLiabilityAmt = rrLiabilityAmt;
    }
  	public String getRrInterestRate() {
        return rrInterestRate;
    }

    public void setRrInterestRate(String rrInterestRate) {
        this.rrInterestRate = rrInterestRate;
    }
  	public String getSsName() {
        return ssName;
    }

    public void setSsName(String ssName) {
        this.ssName = ssName;
    }
  	public String getSsAssetType() {
        return ssAssetType;
    }

    public void setSsAssetType(String ssAssetType) {
        this.ssAssetType = ssAssetType;
    }
  	public String getSsDetailsAssetType() {
        return ssDetailsAssetType;
    }

    public void setSsDetailsAssetType(String ssDetailsAssetType) {
        this.ssDetailsAssetType = ssDetailsAssetType;
    }
  	public String getSsAmt() {
        return ssAmt;
    }

    public void setSsAmt(String ssAmt) {
        this.ssAmt = ssAmt;
    }
  	public String getSsValueDate() {
        return ssValueDate;
    }

    public void setSsValueDate(String ssValueDate) {
        this.ssValueDate = ssValueDate;
    }
  	public String getSsMaturityDate() {
        return ssMaturityDate;
    }

    public void setSsMaturityDate(String ssMaturityDate) {
        this.ssMaturityDate = ssMaturityDate;
    }
  	public String getSsCountry() {
        return ssCountry;
    }

    public void setSsCountry(String ssCountry) {
        this.ssCountry = ssCountry;
    }
  	public String getSsExpectedReturn() {
        return ssExpectedReturn;
    }

    public void setSsExpectedReturn(String ssExpectedReturn) {
        this.ssExpectedReturn = ssExpectedReturn;
    }
  	public Double getSsAnnualReturn() {
        return ssAnnualReturn;
    }

    public void setSsAnnualReturn(Double ssAnnualReturn) {
        this.ssAnnualReturn = ssAnnualReturn;
    }
  	public String getSsInterestFrequency() {
        return ssInterestFrequency;
    }

    public void setSsInterestFrequency(String ssInterestFrequency) {
        this.ssInterestFrequency = ssInterestFrequency;
    }
  	public String getSsDebtor() {
        return ssDebtor;
    }

    public void setSsDebtor(String ssDebtor) {
        this.ssDebtor = ssDebtor;
    }
  	public String getSsOrganCode() {
        return ssOrganCode;
    }

    public void setSsOrganCode(String ssOrganCode) {
        this.ssOrganCode = ssOrganCode;
    }
  	public String getSsRateAgencyIss() {
        return ssRateAgencyIss;
    }

    public void setSsRateAgencyIss(String ssRateAgencyIss) {
        this.ssRateAgencyIss = ssRateAgencyIss;
    }
  	public String getSsDebtorTypeScale() {
        return ssDebtorTypeScale;
    }

    public void setSsDebtorTypeScale(String ssDebtorTypeScale) {
        this.ssDebtorTypeScale = ssDebtorTypeScale;
    }
  	public String getSsDebtorTypeTech() {
        return ssDebtorTypeTech;
    }

    public void setSsDebtorTypeTech(String ssDebtorTypeTech) {
        this.ssDebtorTypeTech = ssDebtorTypeTech;
    }
  	public String getSsDebtorTypeEconomic() {
        return ssDebtorTypeEconomic;
    }

    public void setSsDebtorTypeEconomic(String ssDebtorTypeEconomic) {
        this.ssDebtorTypeEconomic = ssDebtorTypeEconomic;
    }
  	public String getSsProject() {
        return ssProject;
    }

    public void setSsProject(String ssProject) {
        this.ssProject = ssProject;
    }
  	public String getSsIndustryDebtor() {
        return ssIndustryDebtor;
    }

    public void setSsIndustryDebtor(String ssIndustryDebtor) {
        this.ssIndustryDebtor = ssIndustryDebtor;
    }
  	public String getSsIndustryProject() {
        return ssIndustryProject;
    }

    public void setSsIndustryProject(String ssIndustryProject) {
        this.ssIndustryProject = ssIndustryProject;
    }
  	public String getSsMonitoryIndustry() {
        return ssMonitoryIndustry;
    }

    public void setSsMonitoryIndustry(String ssMonitoryIndustry) {
        this.ssMonitoryIndustry = ssMonitoryIndustry;
    }
  	public String getSsMonitoryIndustryType() {
        return ssMonitoryIndustryType;
    }

    public void setSsMonitoryIndustryType(String ssMonitoryIndustryType) {
        this.ssMonitoryIndustryType = ssMonitoryIndustryType;
    }
  	public String getSsDetailsMonitoryType() {
        return ssDetailsMonitoryType;
    }

    public void setSsDetailsMonitoryType(String ssDetailsMonitoryType) {
        this.ssDetailsMonitoryType = ssDetailsMonitoryType;
    }
  	public String getSsInternalAssetRate() {
        return ssInternalAssetRate;
    }

    public void setSsInternalAssetRate(String ssInternalAssetRate) {
        this.ssInternalAssetRate = ssInternalAssetRate;
    }
  	public String getSsGuaranteeMethod() {
        return ssGuaranteeMethod;
    }

    public void setSsGuaranteeMethod(String ssGuaranteeMethod) {
        this.ssGuaranteeMethod = ssGuaranteeMethod;
    }
  	public String getSsDetailsGuarantee() {
        return ssDetailsGuarantee;
    }

    public void setSsDetailsGuarantee(String ssDetailsGuarantee) {
        this.ssDetailsGuarantee = ssDetailsGuarantee;
    }
  	public String getSsPledgeType() {
        return ssPledgeType;
    }

    public void setSsPledgeType(String ssPledgeType) {
        this.ssPledgeType = ssPledgeType;
    }
  	public String getSsPledgeValue() {
        return ssPledgeValue;
    }

    public void setSsPledgeValue(String ssPledgeValue) {
        this.ssPledgeValue = ssPledgeValue;
    }
  	public String getSsGuaranteeType() {
        return ssGuaranteeType;
    }

    public void setSsGuaranteeType(String ssGuaranteeType) {
        this.ssGuaranteeType = ssGuaranteeType;
    }
  	public String getSsGuarantorType() {
        return ssGuarantorType;
    }

    public void setSsGuarantorType(String ssGuarantorType) {
        this.ssGuarantorType = ssGuarantorType;
    }
  	public String getSsDebtEquitySwap() {
        return ssDebtEquitySwap;
    }

    public void setSsDebtEquitySwap(String ssDebtEquitySwap) {
        this.ssDebtEquitySwap = ssDebtEquitySwap;
    }
  	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}