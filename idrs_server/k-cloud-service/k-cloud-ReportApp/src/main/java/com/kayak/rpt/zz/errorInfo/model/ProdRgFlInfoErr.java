package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodRgFlInfoErrService",table = "app_prod_regist_filing_info_erdesc")
public class ProdRgFlInfoErr {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称错误描述", sql = "prod_name_desc = $S{prodNameDesc}" ,field = "prod_name_desc")
   private String prodNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "行内标识码错误描述", sql = "ident_code_desc = $S{identCodeDesc}" ,field = "ident_code_desc")
   private String identCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品品牌错误描述", sql = "prod_brand_desc = $S{prodBrandDesc}" ,field = "prod_brand_desc")
   private String prodBrandDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品期次错误描述", sql = "prod_term_no_desc = $S{prodTermNoDesc}" ,field = "prod_term_no_desc")
   private String prodTermNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误描述", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品审批人身份证号错误描述", sql = "approver_id_code_desc = $S{approverIdCodeDesc}" ,field = "approver_id_code_desc")
   private String approverIdCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品设计人身份证号错误描述", sql = "designer_id_code_desc = $S{designerIdCodeDesc}" ,field = "designer_id_code_desc")
   private String designerIdCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "投资经理身份证号错误描述", sql = "manager_id_code_desc = $S{managerIdCodeDesc}" ,field = "manager_id_code_desc")
   private String managerIdCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人姓名错误描述", sql = "contact_name_desc = $S{contactNameDesc}" ,field = "contact_name_desc")
   private String contactNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人座机错误描述", sql = "contact_telphone_desc = $S{contactTelphoneDesc}" ,field = "contact_telphone_desc")
   private String contactTelphoneDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人手机错误描述", sql = "contact_mobile_desc = $S{contactMobileDesc}" ,field = "contact_mobile_desc")
   private String contactMobileDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人邮箱错误描述", sql = "contact_email_desc = $S{contactEmailDesc}" ,field = "contact_email_desc")
   private String contactEmailDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品募集方式错误描述", sql = "type_collect_desc = $S{typeCollectDesc}" ,field = "type_collect_desc")
   private String typeCollectDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品收益类型错误描述", sql = "prod_retrun_type_desc = $S{prodRetrunTypeDesc}" ,field = "prod_retrun_type_desc")
   private String prodRetrunTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品期限错误描述", sql = "prod_term_desc = $S{prodTermDesc}" ,field = "prod_term_desc")
   private String prodTermDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "是否金融同业专属错误描述", sql = "fiancial_exclusive_desc = $S{fiancialExclusiveDesc}" ,field = "fiancial_exclusive_desc")
   private String fiancialExclusiveDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "资金投向地区错误描述", sql = "invert_region_desc = $S{invertRegionDesc}" ,field = "invert_region_desc")
   private String invertRegionDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资国家或地区错误描述", sql = "invert_country_desc = $S{invertCountryDesc}" ,field = "invert_country_desc")
   private String invertCountryDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财业务服务模式错误描述", sql = "service_mode_desc = $S{serviceModeDesc}" ,field = "service_mode_desc")
   private String serviceModeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品运作模式错误描述", sql = "operation_mode_desc = $S{operationModeDesc}" ,field = "operation_mode_desc")
   private String operationModeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "是否设置最短持有期限错误描述", sql = "min_hold_period_desc = $S{minHoldPeriodDesc}" ,field = "min_hold_period_desc")
   private String minHoldPeriodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "最短持有期限错误描述", sql = "min_hold_day_desc = $S{minHoldDayDesc}" ,field = "min_hold_day_desc")
   private String minHoldDayDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "最短持有期后是否自由赎回错误描述", sql = "option_redempt_period_desc = $S{optionRedemptPeriodDesc}" ,field = "option_redempt_period_desc")
   private String optionRedemptPeriodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "是否现金管理类错误描述", sql = "cash_manager_desc = $S{cashManagerDesc}" ,field = "cash_manager_desc")
   private String cashManagerDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品资产配置方式错误描述", sql = "asset_ac_method_desc = $S{assetAcMethodDesc}" ,field = "asset_ac_method_desc")
   private String assetAcMethodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品管理模式错误描述", sql = "prod_mana_mode_desc = $S{prodManaModeDesc}" ,field = "prod_mana_mode_desc")
   private String prodManaModeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "实际管理人名称错误描述", sql = "ac_mana_name_desc = $S{acManaNameDesc}" ,field = "ac_mana_name_desc")
   private String acManaNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品定价方式错误描述", sql = "price_method_desc = $S{priceMethodDesc}" ,field = "price_method_desc")
   private String priceMethodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资性质错误描述", sql = "invest_type_desc = $S{investTypeDesc}" ,field = "invest_type_desc")
   private String investTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "合作模式错误描述", sql = "cooperate_mode_desc = $S{cooperateModeDesc}" ,field = "cooperate_mode_desc")
   private String cooperateModeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "合作机构名称错误描述", sql = "cooperator_desc = $S{cooperatorDesc}" ,field = "cooperator_desc")
   private String cooperatorDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产种类及比例错误描述", sql = "invest_type_ratio_desc = $S{investTypeRatioDesc}" ,field = "invest_type_ratio_desc")
   private String investTypeRatioDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准错误描述", sql = "prod_benchmark_desc = $S{prodBenchmarkDesc}" ,field = "prod_benchmark_desc")
   private String prodBenchmarkDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品销售区域错误描述", sql = "prod_sales_region_desc = $S{prodSalesRegionDesc}" ,field = "prod_sales_region_desc")
   private String prodSalesRegionDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "募集币种错误描述", sql = "fund_cur_desc = $S{fundCurDesc}" ,field = "fund_cur_desc")
   private String fundCurDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付本金币种错误描述", sql = "principal_cur_desc = $S{principalCurDesc}" ,field = "principal_cur_desc")
   private String principalCurDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付收益币种错误描述", sql = "income_cur_desc = $S{incomeCurDesc}" ,field = "income_cur_desc")
   private String incomeCurDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "起点销售金额错误描述", sql = "invest_threshold_desc = $S{investThresholdDesc}" ,field = "invest_threshold_desc")
   private String investThresholdDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "计划募集金额", sql = "plan_fund_amt_desc = $S{planFundAmtDesc}" ,field = "plan_fund_amt_desc")
   private String planFundAmtDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期", sql = "start_date_earliest_desc = $S{startDateEarliestDesc}" ,field = "start_date_earliest_desc")
   private String startDateEarliestDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期", sql = "start_date_latest_desc = $S{startDateLatestDesc}" ,field = "start_date_latest_desc")
   private String startDateLatestDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "投资本金到帐日错误描述", sql = "principal_due_date_desc = $S{principalDueDateDesc}" ,field = "principal_due_date_desc")
   private String principalDueDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "投资收益到账日错误描述", sql = "income_due_date_desc = $S{incomeDueDateDesc}" ,field = "income_due_date_desc")
   private String incomeDueDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "销售手续费率", sql = "sales_commission_rate_desc = $S{salesCommissionRateDesc}" ,field = "sales_commission_rate_desc")
   private String salesCommissionRateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "投资管理费率", sql = "manage_fee_rate_desc = $S{manageFeeRateDesc}" ,field = "manage_fee_rate_desc")
   private String manageFeeRateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "境内托管机构名称错误描述", sql = "dc_cd_name_desc = $S{dcCdNameDesc}" ,field = "dc_cd_name_desc")
   private String dcCdNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "境内托管机构代码错误描述", sql = "dc_cd_ident_code_desc = $S{dcCdIdentCodeDesc}" ,field = "dc_cd_ident_code_desc")
   private String dcCdIdentCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构国别错误描述", sql = "seas_cd_nation_desc = $S{seasCdNationDesc}" ,field = "seas_cd_nation_desc")
   private String seasCdNationDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构名称错误描述", sql = "seas_cd_name_desc = $S{seasCdNameDesc}" ,field = "seas_cd_name_desc")
   private String seasCdNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "托管费率", sql = "cd_fee_rate_desc = $S{cdFeeRateDesc}" ,field = "cd_fee_rate_desc")
   private String cdFeeRateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品风险等级错误描述", sql = "risk_level_desc = $S{riskLevelDesc}" ,field = "risk_level_desc")
   private String riskLevelDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构提前终止权标识错误描述", sql = "early_tn_option_desc = $S{earlyTnOptionDesc}" ,field = "early_tn_option_desc")
   private String earlyTnOptionDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "客户赎回权标识错误描述", sql = "invest_rdm_option_desc = $S{investRdmOptionDesc}" ,field = "invest_rdm_option_desc")
   private String investRdmOptionDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品增信标识错误描述", sql = "prod_crt_enhance_desc = $S{prodCrtEnhanceDesc}" ,field = "prod_crt_enhance_desc")
   private String prodCrtEnhanceDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品增信机构类型错误描述", sql = "crt_ins_type_desc = $S{crtInsTypeDesc}" ,field = "crt_ins_type_desc")
   private String crtInsTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品增信形式错误描述", sql = "prod_crt_method_desc = $S{prodCrtMethodDesc}" ,field = "prod_crt_method_desc")
   private String prodCrtMethodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "备注错误描述", sql = "details_desc = $S{detailsDesc}" ,field = "details_desc")
   private String detailsDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "报告主文件错误描述", sql = "main_doc_desc = $S{mainDocDesc}" ,field = "main_doc_desc")
   private String mainDocDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品可信性评估报告错误描述", sql = "feasy_ass_report_desc = $S{feasyAssReportDesc}" ,field = "feasy_ass_report_desc")
   private String feasyAssReportDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "内部审核文件错误描述", sql = "inter_audit_doc_desc = $S{interAuditDocDesc}" ,field = "inter_audit_doc_desc")
   private String interAuditDocDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "对理财产品投资管理人托管人投资顾问等相关方的尽职调查错误描述", sql = "due_diligencr_doc_desc = $S{dueDiligencrDocDesc}" ,field = "due_diligencr_doc_desc")
   private String dueDiligencrDocDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "对理财产品投资管理人托管人投资顾问等相关方签署的法律文件错误描述", sql = "legal_doc_sifned_desc = $S{legalDocSifnedDesc}" ,field = "legal_doc_sifned_desc")
   private String legalDocSifnedDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品销售文件错误描述", sql = "prod_sale_doc_desc = $S{prodSaleDocDesc}" ,field = "prod_sale_doc_desc")
   private String prodSaleDocDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品说明书错误描述", sql = "prod_specifi_desc = $S{prodSpecifiDesc}" ,field = "prod_specifi_desc")
   private String prodSpecifiDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品宣传材料错误描述", sql = "prod_mark_doc_desc = $S{prodMarkDocDesc}" ,field = "prod_mark_doc_desc")
   private String prodMarkDocDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他材料错误描述", sql = "other_doc_desc = $S{otherDocDesc}" ,field = "other_doc_desc")
   private String otherDocDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品风险等级错误描述", sql = "risk_rate_desc = $S{riskRateDesc}" ,field = "risk_rate_desc")
   private String riskRateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "新老产品标识", sql = "new_prod_desc = $S{newProdDesc}" ,field = "new_prod_desc")
   private String newProdDesc;
    @GraphQLField(label = "导入日期开始时间")
    private String impStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(label = "导入日期截止时间")
    private String impEndDate;


    @GraphQLField(kkhtml = "KFieldText", label = "报告日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;



  	public String getProdNameDesc() {
        return prodNameDesc;
    }

    public void setProdNameDesc(String prodNameDesc) {
        this.prodNameDesc = prodNameDesc;
    }
  	public String getIdentCodeDesc() {
        return identCodeDesc;
    }

    public void setIdentCodeDesc(String identCodeDesc) {
        this.identCodeDesc = identCodeDesc;
    }
  	public String getProdBrandDesc() {
        return prodBrandDesc;
    }

    public void setProdBrandDesc(String prodBrandDesc) {
        this.prodBrandDesc = prodBrandDesc;
    }
  	public String getProdTermNoDesc() {
        return prodTermNoDesc;
    }

    public void setProdTermNoDesc(String prodTermNoDesc) {
        this.prodTermNoDesc = prodTermNoDesc;
    }
  	public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }
  	public String getApproverIdCodeDesc() {
        return approverIdCodeDesc;
    }

    public void setApproverIdCodeDesc(String approverIdCodeDesc) {
        this.approverIdCodeDesc = approverIdCodeDesc;
    }
  	public String getDesignerIdCodeDesc() {
        return designerIdCodeDesc;
    }

    public void setDesignerIdCodeDesc(String designerIdCodeDesc) {
        this.designerIdCodeDesc = designerIdCodeDesc;
    }
  	public String getManagerIdCodeDesc() {
        return managerIdCodeDesc;
    }

    public void setManagerIdCodeDesc(String managerIdCodeDesc) {
        this.managerIdCodeDesc = managerIdCodeDesc;
    }
  	public String getContactNameDesc() {
        return contactNameDesc;
    }

    public void setContactNameDesc(String contactNameDesc) {
        this.contactNameDesc = contactNameDesc;
    }
  	public String getContactTelphoneDesc() {
        return contactTelphoneDesc;
    }

    public void setContactTelphoneDesc(String contactTelphoneDesc) {
        this.contactTelphoneDesc = contactTelphoneDesc;
    }
  	public String getContactMobileDesc() {
        return contactMobileDesc;
    }

    public void setContactMobileDesc(String contactMobileDesc) {
        this.contactMobileDesc = contactMobileDesc;
    }
  	public String getContactEmailDesc() {
        return contactEmailDesc;
    }

    public void setContactEmailDesc(String contactEmailDesc) {
        this.contactEmailDesc = contactEmailDesc;
    }
  	public String getTypeCollectDesc() {
        return typeCollectDesc;
    }

    public void setTypeCollectDesc(String typeCollectDesc) {
        this.typeCollectDesc = typeCollectDesc;
    }
  	public String getProdRetrunTypeDesc() {
        return prodRetrunTypeDesc;
    }

    public void setProdRetrunTypeDesc(String prodRetrunTypeDesc) {
        this.prodRetrunTypeDesc = prodRetrunTypeDesc;
    }
  	public String getProdTermDesc() {
        return prodTermDesc;
    }

    public void setProdTermDesc(String prodTermDesc) {
        this.prodTermDesc = prodTermDesc;
    }
  	public String getFiancialExclusiveDesc() {
        return fiancialExclusiveDesc;
    }

    public void setFiancialExclusiveDesc(String fiancialExclusiveDesc) {
        this.fiancialExclusiveDesc = fiancialExclusiveDesc;
    }
  	public String getInvertRegionDesc() {
        return invertRegionDesc;
    }

    public void setInvertRegionDesc(String invertRegionDesc) {
        this.invertRegionDesc = invertRegionDesc;
    }
  	public String getInvertCountryDesc() {
        return invertCountryDesc;
    }

    public void setInvertCountryDesc(String invertCountryDesc) {
        this.invertCountryDesc = invertCountryDesc;
    }
  	public String getServiceModeDesc() {
        return serviceModeDesc;
    }

    public void setServiceModeDesc(String serviceModeDesc) {
        this.serviceModeDesc = serviceModeDesc;
    }
  	public String getOperationModeDesc() {
        return operationModeDesc;
    }

    public void setOperationModeDesc(String operationModeDesc) {
        this.operationModeDesc = operationModeDesc;
    }
  	public String getMinHoldPeriodDesc() {
        return minHoldPeriodDesc;
    }

    public void setMinHoldPeriodDesc(String minHoldPeriodDesc) {
        this.minHoldPeriodDesc = minHoldPeriodDesc;
    }
  	public String getMinHoldDayDesc() {
        return minHoldDayDesc;
    }

    public void setMinHoldDayDesc(String minHoldDayDesc) {
        this.minHoldDayDesc = minHoldDayDesc;
    }
  	public String getOptionRedemptPeriodDesc() {
        return optionRedemptPeriodDesc;
    }

    public void setOptionRedemptPeriodDesc(String optionRedemptPeriodDesc) {
        this.optionRedemptPeriodDesc = optionRedemptPeriodDesc;
    }
  	public String getCashManagerDesc() {
        return cashManagerDesc;
    }

    public void setCashManagerDesc(String cashManagerDesc) {
        this.cashManagerDesc = cashManagerDesc;
    }
  	public String getAssetAcMethodDesc() {
        return assetAcMethodDesc;
    }

    public void setAssetAcMethodDesc(String assetAcMethodDesc) {
        this.assetAcMethodDesc = assetAcMethodDesc;
    }
  	public String getProdManaModeDesc() {
        return prodManaModeDesc;
    }

    public void setProdManaModeDesc(String prodManaModeDesc) {
        this.prodManaModeDesc = prodManaModeDesc;
    }
  	public String getAcManaNameDesc() {
        return acManaNameDesc;
    }

    public void setAcManaNameDesc(String acManaNameDesc) {
        this.acManaNameDesc = acManaNameDesc;
    }
  	public String getPriceMethodDesc() {
        return priceMethodDesc;
    }

    public void setPriceMethodDesc(String priceMethodDesc) {
        this.priceMethodDesc = priceMethodDesc;
    }
  	public String getInvestTypeDesc() {
        return investTypeDesc;
    }

    public void setInvestTypeDesc(String investTypeDesc) {
        this.investTypeDesc = investTypeDesc;
    }
  	public String getCooperateModeDesc() {
        return cooperateModeDesc;
    }

    public void setCooperateModeDesc(String cooperateModeDesc) {
        this.cooperateModeDesc = cooperateModeDesc;
    }
  	public String getCooperatorDesc() {
        return cooperatorDesc;
    }

    public void setCooperatorDesc(String cooperatorDesc) {
        this.cooperatorDesc = cooperatorDesc;
    }
  	public String getInvestTypeRatioDesc() {
        return investTypeRatioDesc;
    }

    public void setInvestTypeRatioDesc(String investTypeRatioDesc) {
        this.investTypeRatioDesc = investTypeRatioDesc;
    }
  	public String getProdBenchmarkDesc() {
        return prodBenchmarkDesc;
    }

    public void setProdBenchmarkDesc(String prodBenchmarkDesc) {
        this.prodBenchmarkDesc = prodBenchmarkDesc;
    }
  	public String getProdSalesRegionDesc() {
        return prodSalesRegionDesc;
    }

    public void setProdSalesRegionDesc(String prodSalesRegionDesc) {
        this.prodSalesRegionDesc = prodSalesRegionDesc;
    }
  	public String getFundCurDesc() {
        return fundCurDesc;
    }

    public void setFundCurDesc(String fundCurDesc) {
        this.fundCurDesc = fundCurDesc;
    }
  	public String getPrincipalCurDesc() {
        return principalCurDesc;
    }

    public void setPrincipalCurDesc(String principalCurDesc) {
        this.principalCurDesc = principalCurDesc;
    }
  	public String getIncomeCurDesc() {
        return incomeCurDesc;
    }

    public void setIncomeCurDesc(String incomeCurDesc) {
        this.incomeCurDesc = incomeCurDesc;
    }
  	public String getInvestThresholdDesc() {
        return investThresholdDesc;
    }

    public void setInvestThresholdDesc(String investThresholdDesc) {
        this.investThresholdDesc = investThresholdDesc;
    }
  	public String getPlanFundAmtDesc() {
        return planFundAmtDesc;
    }

    public void setPlanFundAmtDesc(String planFundAmtDesc) {
        this.planFundAmtDesc = planFundAmtDesc;
    }
  	public String getStartDateEarliestDesc() {
        return startDateEarliestDesc;
    }

    public void setStartDateEarliestDesc(String startDateEarliestDesc) {
        this.startDateEarliestDesc = startDateEarliestDesc;
    }
  	public String getStartDateLatestDesc() {
        return startDateLatestDesc;
    }

    public void setStartDateLatestDesc(String startDateLatestDesc) {
        this.startDateLatestDesc = startDateLatestDesc;
    }
  	public String getPrincipalDueDateDesc() {
        return principalDueDateDesc;
    }

    public void setPrincipalDueDateDesc(String principalDueDateDesc) {
        this.principalDueDateDesc = principalDueDateDesc;
    }
  	public String getIncomeDueDateDesc() {
        return incomeDueDateDesc;
    }

    public void setIncomeDueDateDesc(String incomeDueDateDesc) {
        this.incomeDueDateDesc = incomeDueDateDesc;
    }
  	public String getSalesCommissionRateDesc() {
        return salesCommissionRateDesc;
    }

    public void setSalesCommissionRateDesc(String salesCommissionRateDesc) {
        this.salesCommissionRateDesc = salesCommissionRateDesc;
    }
  	public String getManageFeeRateDesc() {
        return manageFeeRateDesc;
    }

    public void setManageFeeRateDesc(String manageFeeRateDesc) {
        this.manageFeeRateDesc = manageFeeRateDesc;
    }
  	public String getDcCdNameDesc() {
        return dcCdNameDesc;
    }

    public void setDcCdNameDesc(String dcCdNameDesc) {
        this.dcCdNameDesc = dcCdNameDesc;
    }
  	public String getDcCdIdentCodeDesc() {
        return dcCdIdentCodeDesc;
    }

    public void setDcCdIdentCodeDesc(String dcCdIdentCodeDesc) {
        this.dcCdIdentCodeDesc = dcCdIdentCodeDesc;
    }
  	public String getSeasCdNationDesc() {
        return seasCdNationDesc;
    }

    public void setSeasCdNationDesc(String seasCdNationDesc) {
        this.seasCdNationDesc = seasCdNationDesc;
    }
  	public String getSeasCdNameDesc() {
        return seasCdNameDesc;
    }

    public void setSeasCdNameDesc(String seasCdNameDesc) {
        this.seasCdNameDesc = seasCdNameDesc;
    }
  	public String getCdFeeRateDesc() {
        return cdFeeRateDesc;
    }

    public void setCdFeeRateDesc(String cdFeeRateDesc) {
        this.cdFeeRateDesc = cdFeeRateDesc;
    }
  	public String getRiskLevelDesc() {
        return riskLevelDesc;
    }

    public void setRiskLevelDesc(String riskLevelDesc) {
        this.riskLevelDesc = riskLevelDesc;
    }
  	public String getEarlyTnOptionDesc() {
        return earlyTnOptionDesc;
    }

    public void setEarlyTnOptionDesc(String earlyTnOptionDesc) {
        this.earlyTnOptionDesc = earlyTnOptionDesc;
    }
  	public String getInvestRdmOptionDesc() {
        return investRdmOptionDesc;
    }

    public void setInvestRdmOptionDesc(String investRdmOptionDesc) {
        this.investRdmOptionDesc = investRdmOptionDesc;
    }
  	public String getProdCrtEnhanceDesc() {
        return prodCrtEnhanceDesc;
    }

    public void setProdCrtEnhanceDesc(String prodCrtEnhanceDesc) {
        this.prodCrtEnhanceDesc = prodCrtEnhanceDesc;
    }
  	public String getCrtInsTypeDesc() {
        return crtInsTypeDesc;
    }

    public void setCrtInsTypeDesc(String crtInsTypeDesc) {
        this.crtInsTypeDesc = crtInsTypeDesc;
    }
  	public String getProdCrtMethodDesc() {
        return prodCrtMethodDesc;
    }

    public void setProdCrtMethodDesc(String prodCrtMethodDesc) {
        this.prodCrtMethodDesc = prodCrtMethodDesc;
    }
  	public String getDetailsDesc() {
        return detailsDesc;
    }

    public void setDetailsDesc(String detailsDesc) {
        this.detailsDesc = detailsDesc;
    }
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
  	public String getMainDocDesc() {
        return mainDocDesc;
    }

    public void setMainDocDesc(String mainDocDesc) {
        this.mainDocDesc = mainDocDesc;
    }
  	public String getFeasyAssReportDesc() {
        return feasyAssReportDesc;
    }

    public void setFeasyAssReportDesc(String feasyAssReportDesc) {
        this.feasyAssReportDesc = feasyAssReportDesc;
    }
  	public String getInterAuditDocDesc() {
        return interAuditDocDesc;
    }

    public void setInterAuditDocDesc(String interAuditDocDesc) {
        this.interAuditDocDesc = interAuditDocDesc;
    }
  	public String getDueDiligencrDocDesc() {
        return dueDiligencrDocDesc;
    }

    public void setDueDiligencrDocDesc(String dueDiligencrDocDesc) {
        this.dueDiligencrDocDesc = dueDiligencrDocDesc;
    }
  	public String getLegalDocSifnedDesc() {
        return legalDocSifnedDesc;
    }

    public void setLegalDocSifnedDesc(String legalDocSifnedDesc) {
        this.legalDocSifnedDesc = legalDocSifnedDesc;
    }
  	public String getProdSaleDocDesc() {
        return prodSaleDocDesc;
    }

    public void setProdSaleDocDesc(String prodSaleDocDesc) {
        this.prodSaleDocDesc = prodSaleDocDesc;
    }
  	public String getProdSpecifiDesc() {
        return prodSpecifiDesc;
    }

    public void setProdSpecifiDesc(String prodSpecifiDesc) {
        this.prodSpecifiDesc = prodSpecifiDesc;
    }
  	public String getProdMarkDocDesc() {
        return prodMarkDocDesc;
    }

    public void setProdMarkDocDesc(String prodMarkDocDesc) {
        this.prodMarkDocDesc = prodMarkDocDesc;
    }
  	public String getOtherDocDesc() {
        return otherDocDesc;
    }

    public void setOtherDocDesc(String otherDocDesc) {
        this.otherDocDesc = otherDocDesc;
    }
  	public String getRiskRateDesc() {
        return riskRateDesc;
    }

    public void setRiskRateDesc(String riskRateDesc) {
        this.riskRateDesc = riskRateDesc;
    }
  	public String getNewProdDesc() {
        return newProdDesc;
    }

    public void setNewProdDesc(String newProdDesc) {
        this.newProdDesc = newProdDesc;
    }


}