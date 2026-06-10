package com.kayak.rpt.zz.historyInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodRegistFilingInfohService",table = "app_prod_regist_filing_info_h")
public class ProdRegistFilingInfoh {
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
   private String prodName;
   @GraphQLField(kkhtml = "KFieldText", label = "行内标识码", sql = "ident_code = $S{identCode}" ,field = "ident_code")
   private String identCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品品牌", sql = "prod_brand = $S{prodBrand}" ,field = "prod_brand")
   private String prodBrand;
   @GraphQLField(kkhtml = "KFieldText", label = "产品期次", sql = "prod_term_no = $S{prodTermNo}" ,field = "prod_term_no")
   private String prodTermNo;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品审批人身份证号", sql = "approver_id_code = $S{approverIdCode}" ,field = "approver_id_code")
   private String approverIdCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品设计人身份证号", sql = "designer_id_code = $S{designerIdCode}" ,field = "designer_id_code")
   private String designerIdCode;
   @GraphQLField(kkhtml = "KFieldText", label = "投资经理身份证号", sql = "manager_id_code = $S{managerIdCode}" ,field = "manager_id_code")
   private String managerIdCode;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人姓名", sql = "contact_name = $S{contactName}" ,field = "contact_name")
   private String contactName;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人座机", sql = "contact_telphone = $S{contactTelphone}" ,field = "contact_telphone")
   private String contactTelphone;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人手机", sql = "contact_mobile = $S{contactMobile}" ,field = "contact_mobile")
   private String contactMobile;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人邮箱", sql = "contact_email = $S{contactEmail}" ,field = "contact_email")
   private String contactEmail;
   @GraphQLField(kkhtml = "KFieldText", label = "产品募集方式", sql = "type_collect = $S{typeCollect}" ,field = "type_collect")
   private String typeCollect;
   @GraphQLField(kkhtml = "KFieldText", label = "产品收益类型", sql = "prod_retrun_type = $S{prodRetrunType}" ,field = "prod_retrun_type")
   private String prodRetrunType;
   @GraphQLField(kkhtml = "KFieldText", label = "产品期限", sql = "prod_term = $S{prodTerm}" ,field = "prod_term")
   private String prodTerm;
   @GraphQLField(kkhtml = "KFieldText", label = "是否金融同业专属", sql = "fiancial_exclusive = $S{fiancialExclusive}" ,field = "fiancial_exclusive")
   private String fiancialExclusive;
   @GraphQLField(kkhtml = "KFieldText", label = "资金投向地区", sql = "invert_region = $S{invertRegion}" ,field = "invert_region")
   private String invertRegion;
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资国家或地区", sql = "invert_country = $S{invertCountry}" ,field = "invert_country")
   private String invertCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "理财业务服务模式", sql = "service_mode = $S{serviceMode}" ,field = "service_mode")
   private String serviceMode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品运作模式", sql = "operation_mode = $S{operationMode}" ,field = "operation_mode")
   private String operationMode;
   @GraphQLField(kkhtml = "KFieldText", label = "是否设置最短持有期限", sql = "min_hold_period = $S{minHoldPeriod}" ,field = "min_hold_period")
   private String minHoldPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "最短持有期限", sql = "min_hold_day = $S{minHoldDay}" ,field = "min_hold_day")
   private String minHoldDay;
   @GraphQLField(kkhtml = "KFieldText", label = "最短持有期后是否自由赎回", sql = "option_redempt_period = $S{optionRedemptPeriod}" ,field = "option_redempt_period")
   private String optionRedemptPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "是否现金管理类", sql = "cash_manager = $S{cashManager}" ,field = "cash_manager")
   private String cashManager;
   @GraphQLField(kkhtml = "KFieldText", label = "产品资产配置方式", sql = "asset_ac_method = $S{assetAcMethod}" ,field = "asset_ac_method")
   private String assetAcMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "产品管理模式", sql = "prod_mana_mode = $S{prodManaMode}" ,field = "prod_mana_mode")
   private String prodManaMode;
   @GraphQLField(kkhtml = "KFieldText", label = "实际管理人名称", sql = "ac_mana_name = $S{acManaName}" ,field = "ac_mana_name")
   private String acManaName;
   @GraphQLField(kkhtml = "KFieldText", label = "产品定价方式", sql = "price_method = $S{priceMethod}" ,field = "price_method")
   private String priceMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资性质", sql = "invest_type = $S{investType}" ,field = "invest_type")
   private String investType;
   @GraphQLField(kkhtml = "KFieldText", label = "合作模式", sql = "cooperate_mode = $S{cooperateMode}" ,field = "cooperate_mode")
   private String cooperateMode;
   @GraphQLField(kkhtml = "KFieldText", label = "合作机构名称", sql = "cooperator = $S{cooperator}" ,field = "cooperator")
   private String cooperator;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产种类及比例", sql = "invest_type_ratio = $S{investTypeRatio}" ,field = "invest_type_ratio")
   private String investTypeRatio;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准", sql = "prod_benchmark = $S{prodBenchmark}" ,field = "prod_benchmark")
   private Double prodBenchmark;
   @GraphQLField(kkhtml = "KFieldText", label = "产品风险等级", sql = "risk_level = $S{riskLevel}" ,field = "risk_level")
   private String riskLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "", sql = "prod_sales_region = $S{prodSalesRegion}" ,field = "prod_sales_region")
   private String prodSalesRegion;
   @GraphQLField(kkhtml = "KFieldText", label = "募集币种", sql = "fund_cur = $S{fundCur}" ,field = "fund_cur")
   private String fundCur;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付本金币种", sql = "principal_cur = $S{principalCur}" ,field = "principal_cur")
   private String principalCur;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付收益币种", sql = "income_cur = $S{incomeCur}" ,field = "income_cur")
   private String incomeCur;
   @GraphQLField(kkhtml = "KFieldText", label = "起点销售金额", sql = "invest_threshold = $S{investThreshold}" ,field = "invest_threshold")
   private Double investThreshold;
   @GraphQLField(kkhtml = "KFieldText", label = "计划募集金额", sql = "plan_fund_amt = $S{planFundAmt}" ,field = "plan_fund_amt")
   private String planFundAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期", sql = "start_date_earliest = $S{startDateEarliest}" ,field = "start_date_earliest")
   private String startDateEarliest;
   @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期", sql = "start_date_latest = $S{startDateLatest}" ,field = "start_date_latest")
   private String startDateLatest;
   @GraphQLField(kkhtml = "KFieldText", label = "投资本金到帐日", sql = "principal_due_date = $S{principalDueDate}" ,field = "principal_due_date")
   private String principalDueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资收益到账日", sql = "income_due_date = $S{incomeDueDate}" ,field = "income_due_date")
   private String incomeDueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "销售手续费率", sql = "sales_commission_rate = $S{salesCommissionRate}" ,field = "sales_commission_rate")
   private Double salesCommissionRate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资管理费率", sql = "manage_fee_rate = $S{manageFeeRate}" ,field = "manage_fee_rate")
   private Double manageFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "境内托管机构名称", sql = "dc_cd_name = $S{dcCdName}" ,field = "dc_cd_name")
   private String dcCdName;
   @GraphQLField(kkhtml = "KFieldText", label = "境内托管机构代码", sql = "dc_cd_ident_code = $S{dcCdIdentCode}" ,field = "dc_cd_ident_code")
   private String dcCdIdentCode;
   @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构国别", sql = "seas_cd_nation = $S{seasCdNation}" ,field = "seas_cd_nation")
   private String seasCdNation;
   @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构名称", sql = "seas_cd_name = $S{seasCdName}" ,field = "seas_cd_name")
   private String seasCdName;
   @GraphQLField(kkhtml = "KFieldText", label = "托管费率", sql = "cd_fee_rate = $S{cdFeeRate}" ,field = "cd_fee_rate")
   private Double cdFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品风险等级", sql = "risk_rate = $S{riskRate}" ,field = "risk_rate")
   private String riskRate;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构提前终止权标识", sql = "early_tn_option = $S{earlyTnOption}" ,field = "early_tn_option")
   private String earlyTnOption;
   @GraphQLField(kkhtml = "KFieldText", label = "客户赎回权标识", sql = "invest_rdm_option = $S{investRdmOption}" ,field = "invest_rdm_option")
   private String investRdmOption;
   @GraphQLField(kkhtml = "KFieldText", label = "产品增信标识", sql = "prod_crt_enhance = $S{prodCrtEnhance}" ,field = "prod_crt_enhance")
   private String prodCrtEnhance;
   @GraphQLField(kkhtml = "KFieldText", label = "产品增信机构类型", sql = "crt_ins_type = $S{crtInsType}" ,field = "crt_ins_type")
   private String crtInsType;
   @GraphQLField(kkhtml = "KFieldText", label = "产品增信形式", sql = "prod_crt_method = $S{prodCrtMethod}" ,field = "prod_crt_method")
   private String prodCrtMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "报告主文件", sql = "main_doc = $S{mainDoc}" ,field = "main_doc")
   private String mainDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品可信性评估报告", sql = "feasy_ass_report = $S{feasyAssReport}" ,field = "feasy_ass_report")
   private String feasyAssReport;
   @GraphQLField(kkhtml = "KFieldText", label = "内部审核文件", sql = "inter_audit_doc = $S{interAuditDoc}" ,field = "inter_audit_doc")
   private String interAuditDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "对理财产品投资管理人托管人投资顾问等相关方的尽职调查", sql = "due_diligencr_doc = $S{dueDiligencrDoc}" ,field = "due_diligencr_doc")
   private String dueDiligencrDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "对理财产品投资管理人托管人投资顾问等相关方签署的法律文件", sql = "legal_doc_sifned = $S{legalDocSifned}" ,field = "legal_doc_sifned")
   private String legalDocSifned;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品销售文件", sql = "prod_sale_doc = $S{prodSaleDoc}" ,field = "prod_sale_doc")
   private String prodSaleDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品说明书", sql = "prod_specifi = $S{prodSpecifi}" ,field = "prod_specifi")
   private String prodSpecifi;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品宣传材料", sql = "prod_mark_doc = $S{prodMarkDoc}" ,field = "prod_mark_doc")
   private String prodMarkDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他材料", sql = "other_doc = $S{otherDoc}" ,field = "other_doc")
   private String otherDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "新老产品标识", sql = "new_prod = $S{newProd}" ,field = "new_prod")
   private String newProd;
    @GraphQLField(label = "导入日期开始时间")
    private String startDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "产品审批人姓名", sql = "PROD_APRV_NM = $S{prodAprvNm}" ,field = "PROD_APRV_NM")
    private String prodAprvNm;
    @GraphQLField(kkhtml = "KFieldText", label = "产品设计人姓名", sql = "PROD_DSN_NM = $S{prodDsnNm}" ,field = "PROD_DSN_NM")
    private String prodDsnNm;
    @GraphQLField(kkhtml = "KFieldText", label = "投资经理姓名", sql = "INV_MNG_NM = $S{invMngNm}" ,field = "INV_MNG_NM")
    private String invMngNm;
    @GraphQLField(label = "成立日期")
    private String foundDt;
    @GraphQLField(label = "产品特殊属性")
    private String prodEspPrpt;

    @GraphQLField(label = "导入日期截止时间")
    private String endDate;
  	public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
  	public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }
  	public String getProdBrand() {
        return prodBrand;
    }

    public void setProdBrand(String prodBrand) {
        this.prodBrand = prodBrand;
    }
  	public String getProdTermNo() {
        return prodTermNo;
    }

    public void setProdTermNo(String prodTermNo) {
        this.prodTermNo = prodTermNo;
    }
  	public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
  	public String getApproverIdCode() {
        return approverIdCode;
    }

    public void setApproverIdCode(String approverIdCode) {
        this.approverIdCode = approverIdCode;
    }
  	public String getDesignerIdCode() {
        return designerIdCode;
    }

    public void setDesignerIdCode(String designerIdCode) {
        this.designerIdCode = designerIdCode;
    }
  	public String getManagerIdCode() {
        return managerIdCode;
    }

    public void setManagerIdCode(String managerIdCode) {
        this.managerIdCode = managerIdCode;
    }
  	public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
  	public String getContactTelphone() {
        return contactTelphone;
    }

    public void setContactTelphone(String contactTelphone) {
        this.contactTelphone = contactTelphone;
    }
  	public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }
  	public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
  	public String getTypeCollect() {
        return typeCollect;
    }

    public void setTypeCollect(String typeCollect) {
        this.typeCollect = typeCollect;
    }
  	public String getProdRetrunType() {
        return prodRetrunType;
    }

    public void setProdRetrunType(String prodRetrunType) {
        this.prodRetrunType = prodRetrunType;
    }
  	public String getProdTerm() {
        return prodTerm;
    }

    public void setProdTerm(String prodTerm) {
        this.prodTerm = prodTerm;
    }
  	public String getFiancialExclusive() {
        return fiancialExclusive;
    }

    public void setFiancialExclusive(String fiancialExclusive) {
        this.fiancialExclusive = fiancialExclusive;
    }
  	public String getInvertRegion() {
        return invertRegion;
    }

    public void setInvertRegion(String invertRegion) {
        this.invertRegion = invertRegion;
    }
  	public String getInvertCountry() {
        return invertCountry;
    }

    public void setInvertCountry(String invertCountry) {
        this.invertCountry = invertCountry;
    }
  	public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }
  	public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }
  	public String getMinHoldPeriod() {
        return minHoldPeriod;
    }

    public void setMinHoldPeriod(String minHoldPeriod) {
        this.minHoldPeriod = minHoldPeriod;
    }
  	public String getMinHoldDay() {
        return minHoldDay;
    }

    public void setMinHoldDay(String minHoldDay) {
        this.minHoldDay = minHoldDay;
    }
  	public String getOptionRedemptPeriod() {
        return optionRedemptPeriod;
    }

    public void setOptionRedemptPeriod(String optionRedemptPeriod) {
        this.optionRedemptPeriod = optionRedemptPeriod;
    }
  	public String getCashManager() {
        return cashManager;
    }

    public void setCashManager(String cashManager) {
        this.cashManager = cashManager;
    }
  	public String getAssetAcMethod() {
        return assetAcMethod;
    }

    public void setAssetAcMethod(String assetAcMethod) {
        this.assetAcMethod = assetAcMethod;
    }
  	public String getProdManaMode() {
        return prodManaMode;
    }

    public void setProdManaMode(String prodManaMode) {
        this.prodManaMode = prodManaMode;
    }
  	public String getAcManaName() {
        return acManaName;
    }

    public void setAcManaName(String acManaName) {
        this.acManaName = acManaName;
    }
  	public String getPriceMethod() {
        return priceMethod;
    }

    public void setPriceMethod(String priceMethod) {
        this.priceMethod = priceMethod;
    }
  	public String getInvestType() {
        return investType;
    }

    public void setInvestType(String investType) {
        this.investType = investType;
    }
  	public String getCooperateMode() {
        return cooperateMode;
    }

    public void setCooperateMode(String cooperateMode) {
        this.cooperateMode = cooperateMode;
    }
  	public String getCooperator() {
        return cooperator;
    }

    public void setCooperator(String cooperator) {
        this.cooperator = cooperator;
    }
  	public String getInvestTypeRatio() {
        return investTypeRatio;
    }

    public void setInvestTypeRatio(String investTypeRatio) {
        this.investTypeRatio = investTypeRatio;
    }
  	public Double getProdBenchmark() {
        return prodBenchmark;
    }

    public void setProdBenchmark(Double prodBenchmark) {
        this.prodBenchmark = prodBenchmark;
    }
  	public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
  	public String getProdSalesRegion() {
        return prodSalesRegion;
    }

    public void setProdSalesRegion(String prodSalesRegion) {
        this.prodSalesRegion = prodSalesRegion;
    }
  	public String getFundCur() {
        return fundCur;
    }

    public void setFundCur(String fundCur) {
        this.fundCur = fundCur;
    }
  	public String getPrincipalCur() {
        return principalCur;
    }

    public void setPrincipalCur(String principalCur) {
        this.principalCur = principalCur;
    }
  	public String getIncomeCur() {
        return incomeCur;
    }

    public void setIncomeCur(String incomeCur) {
        this.incomeCur = incomeCur;
    }
  	public Double getInvestThreshold() {
        return investThreshold;
    }

    public void setInvestThreshold(Double investThreshold) {
        this.investThreshold = investThreshold;
    }
  	public String getPlanFundAmt() {
        return planFundAmt;
    }

    public void setPlanFundAmt(String planFundAmt) {
        this.planFundAmt = planFundAmt;
    }
  	public String getStartDateEarliest() {
        return startDateEarliest;
    }

    public void setStartDateEarliest(String startDateEarliest) {
        this.startDateEarliest = startDateEarliest;
    }
  	public String getStartDateLatest() {
        return startDateLatest;
    }

    public void setStartDateLatest(String startDateLatest) {
        this.startDateLatest = startDateLatest;
    }
  	public String getPrincipalDueDate() {
        return principalDueDate;
    }

    public void setPrincipalDueDate(String principalDueDate) {
        this.principalDueDate = principalDueDate;
    }
  	public String getIncomeDueDate() {
        return incomeDueDate;
    }

    public void setIncomeDueDate(String incomeDueDate) {
        this.incomeDueDate = incomeDueDate;
    }
  	public Double getSalesCommissionRate() {
        return salesCommissionRate;
    }

    public void setSalesCommissionRate(Double salesCommissionRate) {
        this.salesCommissionRate = salesCommissionRate;
    }
  	public Double getManageFeeRate() {
        return manageFeeRate;
    }

    public void setManageFeeRate(Double manageFeeRate) {
        this.manageFeeRate = manageFeeRate;
    }
  	public String getDcCdName() {
        return dcCdName;
    }

    public void setDcCdName(String dcCdName) {
        this.dcCdName = dcCdName;
    }
  	public String getDcCdIdentCode() {
        return dcCdIdentCode;
    }

    public void setDcCdIdentCode(String dcCdIdentCode) {
        this.dcCdIdentCode = dcCdIdentCode;
    }
  	public String getSeasCdNation() {
        return seasCdNation;
    }

    public void setSeasCdNation(String seasCdNation) {
        this.seasCdNation = seasCdNation;
    }
  	public String getSeasCdName() {
        return seasCdName;
    }

    public void setSeasCdName(String seasCdName) {
        this.seasCdName = seasCdName;
    }
  	public Double getCdFeeRate() {
        return cdFeeRate;
    }

    public void setCdFeeRate(Double cdFeeRate) {
        this.cdFeeRate = cdFeeRate;
    }
  	public String getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(String riskRate) {
        this.riskRate = riskRate;
    }
  	public String getEarlyTnOption() {
        return earlyTnOption;
    }

    public void setEarlyTnOption(String earlyTnOption) {
        this.earlyTnOption = earlyTnOption;
    }
  	public String getInvestRdmOption() {
        return investRdmOption;
    }

    public void setInvestRdmOption(String investRdmOption) {
        this.investRdmOption = investRdmOption;
    }
  	public String getProdCrtEnhance() {
        return prodCrtEnhance;
    }

    public void setProdCrtEnhance(String prodCrtEnhance) {
        this.prodCrtEnhance = prodCrtEnhance;
    }
  	public String getCrtInsType() {
        return crtInsType;
    }

    public void setCrtInsType(String crtInsType) {
        this.crtInsType = crtInsType;
    }
  	public String getProdCrtMethod() {
        return prodCrtMethod;
    }

    public void setProdCrtMethod(String prodCrtMethod) {
        this.prodCrtMethod = prodCrtMethod;
    }
  	public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
  	public String getMainDoc() {
        return mainDoc;
    }

    public void setMainDoc(String mainDoc) {
        this.mainDoc = mainDoc;
    }
  	public String getFeasyAssReport() {
        return feasyAssReport;
    }

    public void setFeasyAssReport(String feasyAssReport) {
        this.feasyAssReport = feasyAssReport;
    }
  	public String getInterAuditDoc() {
        return interAuditDoc;
    }

    public void setInterAuditDoc(String interAuditDoc) {
        this.interAuditDoc = interAuditDoc;
    }
  	public String getDueDiligencrDoc() {
        return dueDiligencrDoc;
    }

    public void setDueDiligencrDoc(String dueDiligencrDoc) {
        this.dueDiligencrDoc = dueDiligencrDoc;
    }
  	public String getLegalDocSifned() {
        return legalDocSifned;
    }

    public void setLegalDocSifned(String legalDocSifned) {
        this.legalDocSifned = legalDocSifned;
    }
  	public String getProdSaleDoc() {
        return prodSaleDoc;
    }

    public void setProdSaleDoc(String prodSaleDoc) {
        this.prodSaleDoc = prodSaleDoc;
    }
  	public String getProdSpecifi() {
        return prodSpecifi;
    }

    public void setProdSpecifi(String prodSpecifi) {
        this.prodSpecifi = prodSpecifi;
    }
  	public String getProdMarkDoc() {
        return prodMarkDoc;
    }

    public void setProdMarkDoc(String prodMarkDoc) {
        this.prodMarkDoc = prodMarkDoc;
    }
  	public String getOtherDoc() {
        return otherDoc;
    }

    public void setOtherDoc(String otherDoc) {
        this.otherDoc = otherDoc;
    }
  	public String getNewProd() {
        return newProd;
    }

    public void setNewProd(String newProd) {
        this.newProd = newProd;
    }

    public String getStartDate() {
  	    return startDate;
    }
}