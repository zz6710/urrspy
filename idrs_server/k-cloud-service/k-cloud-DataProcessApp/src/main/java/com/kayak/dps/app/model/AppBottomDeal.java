package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "appBottomDealService",table = "app_bottom_deal")
public class AppBottomDeal {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "securitiesfirms_type = $S{securitiesfirmsType}" ,field = "securitiesfirms_type")
   private String securitiesfirmsType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "remark_par4 = $S{remarkPar4}" ,field = "remark_par4")
   private String remarkPar4;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "financ_org_code = $S{financOrgCode}" ,field = "financ_org_code")
   private String financOrgCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "invest_type = $S{investType}" ,field = "invest_type")
   private String investType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "publisher_trade = $S{publisherTrade}" ,field = "publisher_trade")
   private String publisherTrade;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "issu_orgname = $S{issuOrgname}" ,field = "issu_orgname")
   private String issuOrgname;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "cooper_mode = $S{cooperMode}" ,field = "cooper_mode")
   private String cooperMode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fun_com_name = $S{funComName}" ,field = "fun_com_name")
   private String funComName;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "investment_stage = $S{investmentStage}" ,field = "investment_stage")
   private String investmentStage;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "project_yield = $S{projectYield}" ,field = "project_yield")
   private String projectYield;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "pay_freq = $S{payFreq}" ,field = "pay_freq")
   private String payFreq;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "structure_grade = $S{structureGrade}" ,field = "structure_grade")
   private String structureGrade;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "base_assets_type = $S{baseAssetsType}" ,field = "base_assets_type")
   private String baseAssetsType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "remark_par2 = $S{remarkPar2}" ,field = "remark_par2")
   private String remarkPar2;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_import = $S{isImport}" ,field = "is_import")
   private String isImport;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "vouch = $S{vouch}" ,field = "vouch")
   private String vouch;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "line_right_way = $S{lineRightWay}" ,field = "line_right_way")
   private String lineRightWay;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "remark_par3 = $S{remarkPar3}" ,field = "remark_par3")
   private String remarkPar3;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fee_par = $S{feePar}" ,field = "fee_par")
   private String feePar;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "ftool_code = $S{ftoolCode}" ,field = "ftool_code")
   private String ftoolCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_bankprod = $S{isBankprod}" ,field = "is_bankprod")
   private String isBankprod;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "plan_org_code = $S{planOrgCode}" ,field = "plan_org_code")
   private String planOrgCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "capital_actual_invest = $S{capitalActualInvest}" ,field = "capital_actual_invest")
   private String capitalActualInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "rate_desc_manage = $S{rateDescManage}" ,field = "rate_desc_manage")
   private String rateDescManage;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "rate_desc_service = $S{rateDescService}" ,field = "rate_desc_service")
   private String rateDescService;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "oside_names = $S{osideNames}" ,field = "oside_names")
   private String osideNames;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "yield = $S{yield}" ,field = "yield")
   private String yield;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "in_butype_tec = $S{inButypeTec}" ,field = "in_butype_tec")
   private String inButypeTec;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "base_rate_type = $S{baseRateType}" ,field = "base_rate_type")
   private String baseRateType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fdyz = $S{fdyz}" ,field = "fdyz")
   private String fdyz;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "first_exercise_date = $S{firstExerciseDate}" ,field = "first_exercise_date")
   private String firstExerciseDate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "perpetuity_clause_type = $S{perpetuityClauseType}" ,field = "perpetuity_clause_type")
   private String perpetuityClauseType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "whether_deferred_interest = $S{whetherDeferredInterest}" ,field = "whether_deferred_interest")
   private String whetherDeferredInterest;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "partial_redemption_mark = $S{partialRedemptionMark}" ,field = "partial_redemption_mark")
   private String partialRedemptionMark;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "country_code = $S{countryCode}" ,field = "country_code")
   private String countryCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "comm_ind_code = $S{commIndCode}" ,field = "comm_ind_code")
   private String commIndCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "trade_places = $S{tradePlaces}" ,field = "trade_places")
   private String tradePlaces;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "iss_type_tec = $S{issTypeTec}" ,field = "iss_type_tec")
   private String issTypeTec;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "type_par4 = $S{typePar4}" ,field = "type_par4")
   private String typePar4;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "rate_desc_other = $S{rateDescOther}" ,field = "rate_desc_other")
   private String rateDescOther;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "direction = $S{direction}" ,field = "direction")
   private String direction;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "comm2_ind_code = $S{comm2IndCode}" ,field = "comm2_ind_code")
   private String comm2IndCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "debt_way = $S{debtWay}" ,field = "debt_way")
   private String debtWay;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "excess_income_rate = $S{excessIncomeRate}" ,field = "excess_income_rate")
   private String excessIncomeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fina_type_scale = $S{finaTypeScale}" ,field = "fina_type_scale")
   private String finaTypeScale;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "relation_btw_gaf = $S{relationBtwGaf}" ,field = "relation_btw_gaf")
   private String relationBtwGaf;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "score_par2 = $S{scorePar2}" ,field = "score_par2")
   private String scorePar2;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "deferred_clause_type = $S{deferredClauseType}" ,field = "deferred_clause_type")
   private String deferredClauseType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "cuy = $S{cuy}" ,field = "cuy")
   private String cuy;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "iss_mod = $S{issMod}" ,field = "iss_mod")
   private String issMod;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "cbc_type = $S{cbcType}" ,field = "cbc_type")
   private String cbcType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "cbc_subtype = $S{cbcSubtype}" ,field = "cbc_subtype")
   private String cbcSubtype;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "begindate = $S{begindate}" ,field = "begindate")
   private String begindate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "depot = $S{depot}" ,field = "depot")
   private String depot;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_goverment_fundation = $S{isGovermentFundation}" ,field = "is_goverment_fundation")
   private String isGovermentFundation;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fun_man_name = $S{funManName}" ,field = "fun_man_name")
   private String funManName;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "interest_type = $S{interestType}" ,field = "interest_type")
   private String interestType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "vouch_desc = $S{vouchDesc}" ,field = "vouch_desc")
   private String vouchDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "credit_org_code = $S{creditOrgCode}" ,field = "credit_org_code")
   private String creditOrgCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "t8_sys_adtype_id = $S{t8SysAdtypeId}" ,field = "t8_sys_adtype_id")
   private String t8SysAdtypeId;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "sub_level = $S{subLevel}" ,field = "sub_level")
   private String subLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "cooper_trade = $S{cooperTrade}" ,field = "cooper_trade")
   private String cooperTrade;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "type_par3 = $S{typePar3}" ,field = "type_par3")
   private String typePar3;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "rate_desc_trust = $S{rateDescTrust}" ,field = "rate_desc_trust")
   private String rateDescTrust;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "rate_desc_trans = $S{rateDescTrans}" ,field = "rate_desc_trans")
   private String rateDescTrans;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "basedays = $S{basedays}" ,field = "basedays")
   private String basedays;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_buyback = $S{isBuyback}" ,field = "is_buyback")
   private String isBuyback;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "mode_distribute = $S{modeDistribute}" ,field = "mode_distribute")
   private String modeDistribute;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "ratepare = $S{ratepare}" ,field = "ratepare")
   private String ratepare;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "contain_right_type = $S{containRightType}" ,field = "contain_right_type")
   private String containRightType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "vesting_period = $S{vestingPeriod}" ,field = "vesting_period")
   private String vestingPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "concrete_type = $S{concreteType}" ,field = "concrete_type")
   private String concreteType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "iss_type_scale = $S{issTypeScale}" ,field = "iss_type_scale")
   private String issTypeScale;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "regist_par = $S{registPar}" ,field = "regist_par")
   private String registPar;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "account_code = $S{accountCode}" ,field = "account_code")
   private String accountCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "industry = $S{industry}" ,field = "industry")
   private String industry;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fun_cus_name = $S{funCusName}" ,field = "fun_cus_name")
   private String funCusName;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "vol_value = $S{volValue}" ,field = "vol_value")
   private String volValue;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "legal_maturity = $S{legalMaturity}" ,field = "legal_maturity")
   private String legalMaturity;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "rule_pay_flag = $S{rulePayFlag}" ,field = "rule_pay_flag")
   private String rulePayFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "nature_guarantee = $S{natureGuarantee}" ,field = "nature_guarantee")
   private String natureGuarantee;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "score_par3 = $S{scorePar3}" ,field = "score_par3")
   private String scorePar3;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "score_par4 = $S{scorePar4}" ,field = "score_par4")
   private String scorePar4;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "first_repricing_date = $S{firstRepricingDate}" ,field = "first_repricing_date")
   private String firstRepricingDate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "credit_org_name = $S{creditOrgName}" ,field = "credit_org_name")
   private String creditOrgName;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "iss_type_eco = $S{issTypeEco}" ,field = "iss_type_eco")
   private String issTypeEco;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "trustee_agency = $S{trusteeAgency}" ,field = "trustee_agency")
   private String trusteeAgency;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "trustee_agency_remark = $S{trusteeAgencyRemark}" ,field = "trustee_agency_remark")
   private String trusteeAgencyRemark;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_jrcompany = $S{isJrcompany}" ,field = "is_jrcompany")
   private String isJrcompany;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "trust_people = $S{trustPeople}" ,field = "trust_people")
   private String trustPeople;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "principal = $S{principal}" ,field = "principal")
   private String principal;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "org_type = $S{orgType}" ,field = "org_type")
   private String orgType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "invest_fund_type = $S{investFundType}" ,field = "invest_fund_type")
   private String investFundType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "invest_prop_type = $S{investPropType}" ,field = "invest_prop_type")
   private String investPropType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "in_butype_sca = $S{inButypeSca}" ,field = "in_butype_sca")
   private String inButypeSca;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "in_butype_eco = $S{inButypeEco}" ,field = "in_butype_eco")
   private String inButypeEco;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "debt_service_description = $S{debtServiceDescription}" ,field = "debt_service_description")
   private String debtServiceDescription;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_flow = $S{isFlow}" ,field = "is_flow")
   private String isFlow;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "installments_marked = $S{installmentsMarked}" ,field = "installments_marked")
   private String installmentsMarked;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "oside_name = $S{osideName}" ,field = "oside_name")
   private String osideName;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "score_par1 = $S{scorePar1}" ,field = "score_par1")
   private String scorePar1;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fina_type_eco = $S{finaTypeEco}" ,field = "fina_type_eco")
   private String finaTypeEco;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "sp_ind_code = $S{spIndCode}" ,field = "sp_ind_code")
   private String spIndCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fixed_exercise_date = $S{fixedExerciseDate}" ,field = "fixed_exercise_date")
   private String fixedExerciseDate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "executive_price = $S{executivePrice}" ,field = "executive_price")
   private String executivePrice;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "repricing_cycle = $S{repricingCycle}" ,field = "repricing_cycle")
   private String repricingCycle;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "partial_redemption_rate = $S{partialRedemptionRate}" ,field = "partial_redemption_rate")
   private String partialRedemptionRate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "registration_code = $S{registrationCode}" ,field = "registration_code")
   private String registrationCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "manage_method = $S{manageMethod}" ,field = "manage_method")
   private String manageMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "enddate = $S{enddate}" ,field = "enddate")
   private String enddate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "goverment_ways = $S{govermentWays}" ,field = "goverment_ways")
   private String govermentWays;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_jrcompany_fundation = $S{isJrcompanyFundation}" ,field = "is_jrcompany_fundation")
   private String isJrcompanyFundation;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fun_invest_asset = $S{funInvestAsset}" ,field = "fun_invest_asset")
   private String funInvestAsset;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_bzpj = $S{isBzpj}" ,field = "is_bzpj")
   private String isBzpj;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_lgfplatform = $S{isLgfplatform}" ,field = "is_lgfplatform")
   private String isLgfplatform;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "in_ben_type = $S{inBenType}" ,field = "in_ben_type")
   private String inBenType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "out_par = $S{outPar}" ,field = "out_par")
   private String outPar;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fina_type_tec = $S{finaTypeTec}" ,field = "fina_type_tec")
   private String finaTypeTec;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "sp_ind_desc = $S{spIndDesc}" ,field = "sp_ind_desc")
   private String spIndDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "collateral_type = $S{collateralType}" ,field = "collateral_type")
   private String collateralType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "collateral_value = $S{collateralValue}" ,field = "collateral_value")
   private String collateralValue;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fb_zc_type = $S{fbZcType}" ,field = "fb_zc_type")
   private String fbZcType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "comm3_ind_code = $S{comm3IndCode}" ,field = "comm3_ind_code")
   private String comm3IndCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "report_asset_code = $S{reportAssetCode}" ,field = "report_asset_code")
   private String reportAssetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "fbzc_type = $S{fbzcType}" ,field = "fbzc_type")
   private String fbzcType;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "gq_edate = $S{gqEdate}" ,field = "gq_edate")
   private String gqEdate;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_pledge_financing = $S{isPledgeFinancing}" ,field = "is_pledge_financing")
   private String isPledgeFinancing;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_swap = $S{isSwap}" ,field = "is_swap")
   private String isSwap;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "structprod_subject = $S{structprodSubject}" ,field = "structprod_subject")
   private String structprodSubject;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "holding_purpose = $S{holdingPurpose}" ,field = "holding_purpose")
   private String holdingPurpose;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "is_financial_instruments = $S{isFinancialInstruments}" ,field = "is_financial_instruments")
   private String isFinancialInstruments;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "iss_country = $S{issCountry}" ,field = "iss_country")
   private String issCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "term_days = $S{termDays}" ,field = "term_days")
   private String termDays;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "asset_value = $S{assetValue}" ,field = "asset_value")
   private String assetValue;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "isinterbankloan = $S{isinterbankloan}" ,field = "isinterbankloan")
   private String isinterbankloan;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
   private String inputuser;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "inp_date = $S{inpDate}" ,field = "inp_date")
   private String inpDate;

   @GraphQLField
   private String  reporttabName;

   public String getReporttabName() {
      return reporttabName;
   }
   public void setReporttabName(String reporttabName) {
      this.reporttabName = reporttabName;
   }
  	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
  	public String getSecuritiesfirmsType() {
        return securitiesfirmsType;
    }

    public void setSecuritiesfirmsType(String securitiesfirmsType) {
        this.securitiesfirmsType = securitiesfirmsType;
    }
  	public String getRemarkPar4() {
        return remarkPar4;
    }

    public void setRemarkPar4(String remarkPar4) {
        this.remarkPar4 = remarkPar4;
    }
  	public String getFinancOrgCode() {
        return financOrgCode;
    }

    public void setFinancOrgCode(String financOrgCode) {
        this.financOrgCode = financOrgCode;
    }
  	public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType;
    }
  	public String getPublisherTrade() {
        return publisherTrade;
    }

    public void setPublisherTrade(String publisherTrade) {
        this.publisherTrade = publisherTrade;
    }
  	public String getIssuOrgname() {
        return issuOrgname;
    }

    public void setIssuOrgname(String issuOrgname) {
        this.issuOrgname = issuOrgname;
    }
  	public String getCooperMode() {
        return cooperMode;
    }

    public void setCooperMode(String cooperMode) {
        this.cooperMode = cooperMode;
    }
  	public String getFunComName() {
        return funComName;
    }

    public void setFunComName(String funComName) {
        this.funComName = funComName;
    }
  	public String getInvestmentStage() {
        return investmentStage;
    }

    public void setInvestmentStage(String investmentStage) {
        this.investmentStage = investmentStage;
    }
  	public String getProjectYield() {
        return projectYield;
    }

    public void setProjectYield(String projectYield) {
        this.projectYield = projectYield;
    }
  	public String getPayFreq() {
        return payFreq;
    }

    public void setPayFreq(String payFreq) {
        this.payFreq = payFreq;
    }
  	public String getStructureGrade() {
        return structureGrade;
    }

    public void setStructureGrade(String structureGrade) {
        this.structureGrade = structureGrade;
    }
  	public String getBaseAssetsType() {
        return baseAssetsType;
    }

    public void setBaseAssetsType(String baseAssetsType) {
        this.baseAssetsType = baseAssetsType;
    }
  	public String getRemarkPar2() {
        return remarkPar2;
    }

    public void setRemarkPar2(String remarkPar2) {
        this.remarkPar2 = remarkPar2;
    }
  	public String getIsImport() {
        return isImport;
    }

    public void setIsImport(String isImport) {
        this.isImport = isImport;
    }
  	public String getVouch() {
        return vouch;
    }

    public void setVouch(String vouch) {
        this.vouch = vouch;
    }
  	public String getLineRightWay() {
        return lineRightWay;
    }

    public void setLineRightWay(String lineRightWay) {
        this.lineRightWay = lineRightWay;
    }
  	public String getRemarkPar3() {
        return remarkPar3;
    }

    public void setRemarkPar3(String remarkPar3) {
        this.remarkPar3 = remarkPar3;
    }
  	public String getFeePar() {
        return feePar;
    }

    public void setFeePar(String feePar) {
        this.feePar = feePar;
    }
  	public String getFtoolCode() {
        return ftoolCode;
    }

    public void setFtoolCode(String ftoolCode) {
        this.ftoolCode = ftoolCode;
    }
  	public String getIsBankprod() {
        return isBankprod;
    }

    public void setIsBankprod(String isBankprod) {
        this.isBankprod = isBankprod;
    }
  	public String getPlanOrgCode() {
        return planOrgCode;
    }

    public void setPlanOrgCode(String planOrgCode) {
        this.planOrgCode = planOrgCode;
    }
  	public String getCapitalActualInvest() {
        return capitalActualInvest;
    }

    public void setCapitalActualInvest(String capitalActualInvest) {
        this.capitalActualInvest = capitalActualInvest;
    }
  	public String getRateDescManage() {
        return rateDescManage;
    }

    public void setRateDescManage(String rateDescManage) {
        this.rateDescManage = rateDescManage;
    }
  	public String getRateDescService() {
        return rateDescService;
    }

    public void setRateDescService(String rateDescService) {
        this.rateDescService = rateDescService;
    }
  	public String getOsideNames() {
        return osideNames;
    }

    public void setOsideNames(String osideNames) {
        this.osideNames = osideNames;
    }
  	public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }
  	public String getInButypeTec() {
        return inButypeTec;
    }

    public void setInButypeTec(String inButypeTec) {
        this.inButypeTec = inButypeTec;
    }
  	public String getBaseRateType() {
        return baseRateType;
    }

    public void setBaseRateType(String baseRateType) {
        this.baseRateType = baseRateType;
    }
  	public String getFdyz() {
        return fdyz;
    }

    public void setFdyz(String fdyz) {
        this.fdyz = fdyz;
    }
  	public String getFirstExerciseDate() {
        return firstExerciseDate;
    }

    public void setFirstExerciseDate(String firstExerciseDate) {
        this.firstExerciseDate = firstExerciseDate;
    }
  	public String getPerpetuityClauseType() {
        return perpetuityClauseType;
    }

    public void setPerpetuityClauseType(String perpetuityClauseType) {
        this.perpetuityClauseType = perpetuityClauseType;
    }
  	public String getWhetherDeferredInterest() {
        return whetherDeferredInterest;
    }

    public void setWhetherDeferredInterest(String whetherDeferredInterest) {
        this.whetherDeferredInterest = whetherDeferredInterest;
    }
  	public String getPartialRedemptionMark() {
        return partialRedemptionMark;
    }

    public void setPartialRedemptionMark(String partialRedemptionMark) {
        this.partialRedemptionMark = partialRedemptionMark;
    }
  	public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
  	public String getCommIndCode() {
        return commIndCode;
    }

    public void setCommIndCode(String commIndCode) {
        this.commIndCode = commIndCode;
    }
  	public String getTradePlaces() {
        return tradePlaces;
    }

    public void setTradePlaces(String tradePlaces) {
        this.tradePlaces = tradePlaces;
    }
  	public String getIssTypeTec() {
        return issTypeTec;
    }

    public void setIssTypeTec(String issTypeTec) {
        this.issTypeTec = issTypeTec;
    }
  	public String getTypePar4() {
        return typePar4;
    }

    public void setTypePar4(String typePar4) {
        this.typePar4 = typePar4;
    }
  	public String getRateDescOther() {
        return rateDescOther;
    }

    public void setRateDescOther(String rateDescOther) {
        this.rateDescOther = rateDescOther;
    }
  	public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
  	public String getComm2IndCode() {
        return comm2IndCode;
    }

    public void setComm2IndCode(String comm2IndCode) {
        this.comm2IndCode = comm2IndCode;
    }
  	public String getDebtWay() {
        return debtWay;
    }

    public void setDebtWay(String debtWay) {
        this.debtWay = debtWay;
    }
  	public String getExcessIncomeRate() {
        return excessIncomeRate;
    }

    public void setExcessIncomeRate(String excessIncomeRate) {
        this.excessIncomeRate = excessIncomeRate;
    }
  	public String getFinaTypeScale() {
        return finaTypeScale;
    }

    public void setFinaTypeScale(String finaTypeScale) {
        this.finaTypeScale = finaTypeScale;
    }
  	public String getRelationBtwGaf() {
        return relationBtwGaf;
    }

    public void setRelationBtwGaf(String relationBtwGaf) {
        this.relationBtwGaf = relationBtwGaf;
    }
  	public String getScorePar2() {
        return scorePar2;
    }

    public void setScorePar2(String scorePar2) {
        this.scorePar2 = scorePar2;
    }
  	public String getDeferredClauseType() {
        return deferredClauseType;
    }

    public void setDeferredClauseType(String deferredClauseType) {
        this.deferredClauseType = deferredClauseType;
    }
  	public String getCuy() {
        return cuy;
    }

    public void setCuy(String cuy) {
        this.cuy = cuy;
    }
  	public String getIssMod() {
        return issMod;
    }

    public void setIssMod(String issMod) {
        this.issMod = issMod;
    }
  	public String getCbcType() {
        return cbcType;
    }

    public void setCbcType(String cbcType) {
        this.cbcType = cbcType;
    }
  	public String getCbcSubtype() {
        return cbcSubtype;
    }

    public void setCbcSubtype(String cbcSubtype) {
        this.cbcSubtype = cbcSubtype;
    }
  	public String getBegindate() {
        return begindate;
    }

    public void setBegindate(String begindate) {
        this.begindate = begindate;
    }
  	public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }
  	public String getIsGovermentFundation() {
        return isGovermentFundation;
    }

    public void setIsGovermentFundation(String isGovermentFundation) {
        this.isGovermentFundation = isGovermentFundation;
    }
  	public String getFunManName() {
        return funManName;
    }

    public void setFunManName(String funManName) {
        this.funManName = funManName;
    }
  	public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }
  	public String getVouchDesc() {
        return vouchDesc;
    }

    public void setVouchDesc(String vouchDesc) {
        this.vouchDesc = vouchDesc;
    }
  	public String getCreditOrgCode() {
        return creditOrgCode;
    }

    public void setCreditOrgCode(String creditOrgCode) {
        this.creditOrgCode = creditOrgCode;
    }
  	public String getT8SysAdtypeId() {
        return t8SysAdtypeId;
    }

    public void setT8SysAdtypeId(String t8SysAdtypeId) {
        this.t8SysAdtypeId = t8SysAdtypeId;
    }
  	public String getSubLevel() {
        return subLevel;
    }

    public void setSubLevel(String subLevel) {
        this.subLevel = subLevel;
    }
  	public String getCooperTrade() {
        return cooperTrade;
    }

    public void setCooperTrade(String cooperTrade) {
        this.cooperTrade = cooperTrade;
    }
  	public String getTypePar3() {
        return typePar3;
    }

    public void setTypePar3(String typePar3) {
        this.typePar3 = typePar3;
    }
  	public String getRateDescTrust() {
        return rateDescTrust;
    }

    public void setRateDescTrust(String rateDescTrust) {
        this.rateDescTrust = rateDescTrust;
    }
  	public String getRateDescTrans() {
        return rateDescTrans;
    }

    public void setRateDescTrans(String rateDescTrans) {
        this.rateDescTrans = rateDescTrans;
    }
  	public String getBasedays() {
        return basedays;
    }

    public void setBasedays(String basedays) {
        this.basedays = basedays;
    }
  	public String getIsBuyback() {
        return isBuyback;
    }

    public void setIsBuyback(String isBuyback) {
        this.isBuyback = isBuyback;
    }
  	public String getModeDistribute() {
        return modeDistribute;
    }

    public void setModeDistribute(String modeDistribute) {
        this.modeDistribute = modeDistribute;
    }
  	public String getRatepare() {
        return ratepare;
    }

    public void setRatepare(String ratepare) {
        this.ratepare = ratepare;
    }
  	public String getContainRightType() {
        return containRightType;
    }

    public void setContainRightType(String containRightType) {
        this.containRightType = containRightType;
    }
  	public String getVestingPeriod() {
        return vestingPeriod;
    }

    public void setVestingPeriod(String vestingPeriod) {
        this.vestingPeriod = vestingPeriod;
    }
  	public String getConcreteType() {
        return concreteType;
    }

    public void setConcreteType(String concreteType) {
        this.concreteType = concreteType;
    }
  	public String getIssTypeScale() {
        return issTypeScale;
    }

    public void setIssTypeScale(String issTypeScale) {
        this.issTypeScale = issTypeScale;
    }
  	public String getRegistPar() {
        return registPar;
    }

    public void setRegistPar(String registPar) {
        this.registPar = registPar;
    }
  	public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }
  	public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
  	public String getFunCusName() {
        return funCusName;
    }

    public void setFunCusName(String funCusName) {
        this.funCusName = funCusName;
    }
  	public String getVolValue() {
        return volValue;
    }

    public void setVolValue(String volValue) {
        this.volValue = volValue;
    }
  	public String getLegalMaturity() {
        return legalMaturity;
    }

    public void setLegalMaturity(String legalMaturity) {
        this.legalMaturity = legalMaturity;
    }
  	public String getRulePayFlag() {
        return rulePayFlag;
    }

    public void setRulePayFlag(String rulePayFlag) {
        this.rulePayFlag = rulePayFlag;
    }
  	public String getNatureGuarantee() {
        return natureGuarantee;
    }

    public void setNatureGuarantee(String natureGuarantee) {
        this.natureGuarantee = natureGuarantee;
    }
  	public String getScorePar3() {
        return scorePar3;
    }

    public void setScorePar3(String scorePar3) {
        this.scorePar3 = scorePar3;
    }
  	public String getScorePar4() {
        return scorePar4;
    }

    public void setScorePar4(String scorePar4) {
        this.scorePar4 = scorePar4;
    }
  	public String getFirstRepricingDate() {
        return firstRepricingDate;
    }

    public void setFirstRepricingDate(String firstRepricingDate) {
        this.firstRepricingDate = firstRepricingDate;
    }
  	public String getCreditOrgName() {
        return creditOrgName;
    }

    public void setCreditOrgName(String creditOrgName) {
        this.creditOrgName = creditOrgName;
    }
  	public String getIssTypeEco() {
        return issTypeEco;
    }

    public void setIssTypeEco(String issTypeEco) {
        this.issTypeEco = issTypeEco;
    }
  	public String getTrusteeAgency() {
        return trusteeAgency;
    }

    public void setTrusteeAgency(String trusteeAgency) {
        this.trusteeAgency = trusteeAgency;
    }
  	public String getTrusteeAgencyRemark() {
        return trusteeAgencyRemark;
    }

    public void setTrusteeAgencyRemark(String trusteeAgencyRemark) {
        this.trusteeAgencyRemark = trusteeAgencyRemark;
    }
  	public String getIsJrcompany() {
        return isJrcompany;
    }

    public void setIsJrcompany(String isJrcompany) {
        this.isJrcompany = isJrcompany;
    }
  	public String getTrustPeople() {
        return trustPeople;
    }

    public void setTrustPeople(String trustPeople) {
        this.trustPeople = trustPeople;
    }
  	public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
  	public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }
  	public String getInvestFundType() {
        return investFundType;
    }

    public void setInvestFundType(String investFundType) {
        this.investFundType = investFundType;
    }
  	public String getInvestPropType() {
        return investPropType;
    }

    public void setInvestPropType(String investPropType) {
        this.investPropType = investPropType;
    }
  	public String getInButypeSca() {
        return inButypeSca;
    }

    public void setInButypeSca(String inButypeSca) {
        this.inButypeSca = inButypeSca;
    }
  	public String getInButypeEco() {
        return inButypeEco;
    }

    public void setInButypeEco(String inButypeEco) {
        this.inButypeEco = inButypeEco;
    }
  	public String getDebtServiceDescription() {
        return debtServiceDescription;
    }

    public void setDebtServiceDescription(String debtServiceDescription) {
        this.debtServiceDescription = debtServiceDescription;
    }
  	public String getIsFlow() {
        return isFlow;
    }

    public void setIsFlow(String isFlow) {
        this.isFlow = isFlow;
    }
  	public String getInstallmentsMarked() {
        return installmentsMarked;
    }

    public void setInstallmentsMarked(String installmentsMarked) {
        this.installmentsMarked = installmentsMarked;
    }
  	public String getOsideName() {
        return osideName;
    }

    public void setOsideName(String osideName) {
        this.osideName = osideName;
    }
  	public String getScorePar1() {
        return scorePar1;
    }

    public void setScorePar1(String scorePar1) {
        this.scorePar1 = scorePar1;
    }
  	public String getFinaTypeEco() {
        return finaTypeEco;
    }

    public void setFinaTypeEco(String finaTypeEco) {
        this.finaTypeEco = finaTypeEco;
    }
  	public String getSpIndCode() {
        return spIndCode;
    }

    public void setSpIndCode(String spIndCode) {
        this.spIndCode = spIndCode;
    }
  	public String getFixedExerciseDate() {
        return fixedExerciseDate;
    }

    public void setFixedExerciseDate(String fixedExerciseDate) {
        this.fixedExerciseDate = fixedExerciseDate;
    }
  	public String getExecutivePrice() {
        return executivePrice;
    }

    public void setExecutivePrice(String executivePrice) {
        this.executivePrice = executivePrice;
    }
  	public String getRepricingCycle() {
        return repricingCycle;
    }

    public void setRepricingCycle(String repricingCycle) {
        this.repricingCycle = repricingCycle;
    }
  	public String getPartialRedemptionRate() {
        return partialRedemptionRate;
    }

    public void setPartialRedemptionRate(String partialRedemptionRate) {
        this.partialRedemptionRate = partialRedemptionRate;
    }
  	public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }
  	public String getManageMethod() {
        return manageMethod;
    }

    public void setManageMethod(String manageMethod) {
        this.manageMethod = manageMethod;
    }
  	public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
  	public String getGovermentWays() {
        return govermentWays;
    }

    public void setGovermentWays(String govermentWays) {
        this.govermentWays = govermentWays;
    }
  	public String getIsJrcompanyFundation() {
        return isJrcompanyFundation;
    }

    public void setIsJrcompanyFundation(String isJrcompanyFundation) {
        this.isJrcompanyFundation = isJrcompanyFundation;
    }
  	public String getFunInvestAsset() {
        return funInvestAsset;
    }

    public void setFunInvestAsset(String funInvestAsset) {
        this.funInvestAsset = funInvestAsset;
    }
  	public String getIsBzpj() {
        return isBzpj;
    }

    public void setIsBzpj(String isBzpj) {
        this.isBzpj = isBzpj;
    }
  	public String getIsLgfplatform() {
        return isLgfplatform;
    }

    public void setIsLgfplatform(String isLgfplatform) {
        this.isLgfplatform = isLgfplatform;
    }
  	public String getInBenType() {
        return inBenType;
    }

    public void setInBenType(String inBenType) {
        this.inBenType = inBenType;
    }
  	public String getOutPar() {
        return outPar;
    }

    public void setOutPar(String outPar) {
        this.outPar = outPar;
    }
  	public String getFinaTypeTec() {
        return finaTypeTec;
    }

    public void setFinaTypeTec(String finaTypeTec) {
        this.finaTypeTec = finaTypeTec;
    }
  	public String getSpIndDesc() {
        return spIndDesc;
    }

    public void setSpIndDesc(String spIndDesc) {
        this.spIndDesc = spIndDesc;
    }
  	public String getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
    }
  	public String getCollateralValue() {
        return collateralValue;
    }

    public void setCollateralValue(String collateralValue) {
        this.collateralValue = collateralValue;
    }
  	public String getFbZcType() {
        return fbZcType;
    }

    public void setFbZcType(String fbZcType) {
        this.fbZcType = fbZcType;
    }
  	public String getComm3IndCode() {
        return comm3IndCode;
    }

    public void setComm3IndCode(String comm3IndCode) {
        this.comm3IndCode = comm3IndCode;
    }
  	public String getReportAssetCode() {
        return reportAssetCode;
    }

    public void setReportAssetCode(String reportAssetCode) {
        this.reportAssetCode = reportAssetCode;
    }
  	public String getFbzcType() {
        return fbzcType;
    }

    public void setFbzcType(String fbzcType) {
        this.fbzcType = fbzcType;
    }
  	public String getGqEdate() {
        return gqEdate;
    }

    public void setGqEdate(String gqEdate) {
        this.gqEdate = gqEdate;
    }
  	public String getIsPledgeFinancing() {
        return isPledgeFinancing;
    }

    public void setIsPledgeFinancing(String isPledgeFinancing) {
        this.isPledgeFinancing = isPledgeFinancing;
    }
  	public String getIsSwap() {
        return isSwap;
    }

    public void setIsSwap(String isSwap) {
        this.isSwap = isSwap;
    }
  	public String getStructprodSubject() {
        return structprodSubject;
    }

    public void setStructprodSubject(String structprodSubject) {
        this.structprodSubject = structprodSubject;
    }
  	public String getHoldingPurpose() {
        return holdingPurpose;
    }

    public void setHoldingPurpose(String holdingPurpose) {
        this.holdingPurpose = holdingPurpose;
    }
  	public String getIsFinancialInstruments() {
        return isFinancialInstruments;
    }

    public void setIsFinancialInstruments(String isFinancialInstruments) {
        this.isFinancialInstruments = isFinancialInstruments;
    }
  	public String getIssCountry() {
        return issCountry;
    }

    public void setIssCountry(String issCountry) {
        this.issCountry = issCountry;
    }
  	public String getTermDays() {
        return termDays;
    }

    public void setTermDays(String termDays) {
        this.termDays = termDays;
    }
  	public String getAssetValue() {
        return assetValue;
    }

    public void setAssetValue(String assetValue) {
        this.assetValue = assetValue;
    }
  	public String getIsinterbankloan() {
        return isinterbankloan;
    }

    public void setIsinterbankloan(String isinterbankloan) {
        this.isinterbankloan = isinterbankloan;
    }
  	public String getInputuser() {
        return inputuser;
    }

    public void setInputuser(String inputuser) {
        this.inputuser = inputuser;
    }
  	public String getInpDate() {
        return inpDate;
    }

    public void setInpDate(String inpDate) {
        this.inpDate = inpDate;
    }

}