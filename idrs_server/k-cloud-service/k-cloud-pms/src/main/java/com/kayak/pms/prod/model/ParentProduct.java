package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "parentProductService",table = "a_fp_p_parent_product")
public class ParentProduct {
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "product_code = $S{productCode}" ,field = "product_code")
   private String productCode;
   @GraphQLField(kkhtml = "KFieldText", label = "托管人代码", sql = "custodian_code = $S{custodianCode}" ,field = "custodian_code")
   private String custodianCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资管系统代码", sql = "asset_code = $S{assetCode}" ,field = "asset_code")
   private String assetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "基金经理代码", sql = "fund_manager_code = $S{fundManagerCode}" ,field = "fund_manager_code")
   private String fundManagerCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品分类代码", sql = "product_type = $S{productType}" ,field = "product_type")
   private String productType;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "product_name = $S{productName}" ,field = "product_name")
   private String productName;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "currency_type = $S{currencyType}" ,field = "currency_type")
   private String currencyType;
   @GraphQLField(kkhtml = "KFieldText", label = "钞汇标志", sql = "cash_flag = $S{cashFlag}" ,field = "cash_flag")
   private String cashFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "面值", sql = "face_value = $S{faceValue}" ,field = "face_value")
   private String faceValue;
   @GraphQLField(kkhtml = "KFieldText", label = "发行价格", sql = "issue_price = $S{issuePrice}" ,field = "issue_price")
   private String issuePrice;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准", sql = "perfor_compare_norm = $S{perforCompareNorm}" ,field = "perfor_compare_norm")
   private String perforCompareNorm;
   @GraphQLField(kkhtml = "KFieldText", label = "募集类型", sql = "ipo_type = $S{ipoType}" ,field = "ipo_type")
   private String ipoType;
   @GraphQLField(kkhtml = "KFieldText", label = "募集开始日期", sql = "ipo_start_date = $S{ipoStartDate}" ,field = "ipo_start_date")
   private String ipoStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "募集结束日期", sql = "ipo_end_date = $S{ipoEndDate}" ,field = "ipo_end_date")
   private String ipoEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "允许购买客户类型", sql = "allot_client_type = $S{allotClientType}" ,field = "allot_client_type")
   private String allotClientType;
   @GraphQLField(kkhtml = "KFieldText", label = "巨额赎回比例", sql = "large_redeem_radio = $S{largeRedeemRadio}" ,field = "large_redeem_radio")
   private String largeRedeemRadio;
   @GraphQLField(kkhtml = "KFieldText", label = "生命周期状态", sql = "product_lifecycle_status = $S{productLifecycleStatus}" ,field = "product_lifecycle_status")
   private String productLifecycleStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "产品交易状态", sql = "transaction_status = $S{transactionStatus}" ,field = "transaction_status")
   private String transactionStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "风险等级", sql = "risk_level = $S{riskLevel}" ,field = "risk_level")
   private String riskLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "工作日方案代码", sql = "workday_suite_code = $S{workdaySuiteCode}" ,field = "workday_suite_code")
   private String workdaySuiteCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品成立日", sql = "establish_date = $S{establishDate}" ,field = "establish_date")
   private String establishDate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品到期日", sql = "due_date = $S{dueDate}" ,field = "due_date")
   private String dueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期是否自动清盘", sql = "auto_due = $S{autoDue}" ,field = "auto_due")
   private String autoDue;
   @GraphQLField(kkhtml = "KFieldText", label = "产品清盘日，手工清盘日", sql = "clean_date = $S{cleanDate}" ,field = "clean_date")
   private String cleanDate;
   @GraphQLField(kkhtml = "KFieldText", label = "开放类型", sql = "open_type = $S{openType}" ,field = "open_type")
   private String openType;
   @GraphQLField(kkhtml = "KFieldText", label = "按目标客户划分产品类型（只维护，没有业务逻辑）", sql = "target_product_type = $S{targetProductType}" ,field = "target_product_type")
   private String targetProductType;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "product_regist_code = $S{productRegistCode}" ,field = "product_regist_code")
   private String productRegistCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品天数", sql = "product_days = $S{productDays}" ,field = "product_days")
   private String productDays;
   @GraphQLField(kkhtml = "KFieldText", label = "自营或代理标志   0-代理，1-自营", sql = "self_support_flag = $S{selfSupportFlag}" ,field = "self_support_flag")
   private String selfSupportFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "个人首次最低投资金额", sql = "first_min_amount_individual = $S{firstMinAmountIndividual}" ,field = "first_min_amount_individual")
   private String firstMinAmountIndividual;
   @GraphQLField(kkhtml = "KFieldText", label = "法人首次最低投资金额", sql = "first_min_amount_institution = $S{firstMinAmountInstitution}" ,field = "first_min_amount_institution")
   private String firstMinAmountInstitution;
   @GraphQLField(kkhtml = "KFieldText", label = "同业首次最低投资金额", sql = "first_min_amount_interbank = $S{firstMinAmountInterbank}" ,field = "first_min_amount_interbank")
   private String firstMinAmountInterbank;
   @GraphQLField(kkhtml = "KFieldText", label = "开放周期类型", sql = "open_cycle_type = $S{openCycleType}" ,field = "open_cycle_type")
   private String openCycleType;
   @GraphQLField(kkhtml = "KFieldText", label = "开放周期值，根据周期类型为1按日开放则为天数", sql = "open_cycle_index = $S{openCycleIndex}" ,field = "open_cycle_index")
   private String openCycleIndex;
   @GraphQLField(kkhtml = "KFieldText", label = "首次开放日", sql = "first_open_day = $S{firstOpenDay}" ,field = "first_open_day")
   private String firstOpenDay;
   @GraphQLField(kkhtml = "KFieldText", label = "从周期末推算值，如果指定了大于零的值，表示从周期末开始向前推算多少天", sql = "start_from_last = $S{startFromLast}" ,field = "start_from_last")
   private String startFromLast;
   @GraphQLField(kkhtml = "KFieldText", label = "遇节假日规则", sql = "have_holiday = $S{haveHoliday}" ,field = "have_holiday")
   private String haveHoliday;
   @GraphQLField(kkhtml = "KFieldText", label = "产品当前净值", sql = "netvalue = $S{netvalue}" ,field = "netvalue")
   private String netvalue;
   @GraphQLField(kkhtml = "KFieldText", label = "产品当前净值日", sql = "netvalue_date = $S{netvalueDate}" ,field = "netvalue_date")
   private String netvalueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "份额确认天数", sql = "confirm_days = $S{confirmDays}" ,field = "confirm_days")
   private String confirmDays;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准", sql = "perfor_compare_norm_text = $S{perforCompareNormText}" ,field = "perfor_compare_norm_text")
   private String perforCompareNormText;
   @GraphQLField(kkhtml = "KFieldText", label = "最后更新日期", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;
   @GraphQLField(kkhtml = "KFieldText", label = "操作员", sql = "operator_no = $S{operatorNo}" ,field = "operator_no")
   private String operatorNo;
   @GraphQLField(kkhtml = "KFieldText", label = "复核员", sql = "checker_no = $S{checkerNo}" ,field = "checker_no")
   private String checkerNo;
   
  	public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
  	public String getCustodianCode() {
        return custodianCode;
    }

    public void setCustodianCode(String custodianCode) {
        this.custodianCode = custodianCode;
    }
  	public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
  	public String getFundManagerCode() {
        return fundManagerCode;
    }

    public void setFundManagerCode(String fundManagerCode) {
        this.fundManagerCode = fundManagerCode;
    }
  	public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
  	public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
  	public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
  	public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }
  	public String getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(String faceValue) {
        this.faceValue = faceValue;
    }
  	public String getIssuePrice() {
        return issuePrice;
    }

    public void setIssuePrice(String issuePrice) {
        this.issuePrice = issuePrice;
    }
  	public String getPerforCompareNorm() {
        return perforCompareNorm;
    }

    public void setPerforCompareNorm(String perforCompareNorm) {
        this.perforCompareNorm = perforCompareNorm;
    }
  	public String getIpoType() {
        return ipoType;
    }

    public void setIpoType(String ipoType) {
        this.ipoType = ipoType;
    }
  	public String getIpoStartDate() {
        return ipoStartDate;
    }

    public void setIpoStartDate(String ipoStartDate) {
        this.ipoStartDate = ipoStartDate;
    }
  	public String getIpoEndDate() {
        return ipoEndDate;
    }

    public void setIpoEndDate(String ipoEndDate) {
        this.ipoEndDate = ipoEndDate;
    }
  	public String getAllotClientType() {
        return allotClientType;
    }

    public void setAllotClientType(String allotClientType) {
        this.allotClientType = allotClientType;
    }
  	public String getLargeRedeemRadio() {
        return largeRedeemRadio;
    }

    public void setLargeRedeemRadio(String largeRedeemRadio) {
        this.largeRedeemRadio = largeRedeemRadio;
    }
  	public String getProductLifecycleStatus() {
        return productLifecycleStatus;
    }

    public void setProductLifecycleStatus(String productLifecycleStatus) {
        this.productLifecycleStatus = productLifecycleStatus;
    }
  	public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
  	public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
  	public String getWorkdaySuiteCode() {
        return workdaySuiteCode;
    }

    public void setWorkdaySuiteCode(String workdaySuiteCode) {
        this.workdaySuiteCode = workdaySuiteCode;
    }
  	public String getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(String establishDate) {
        this.establishDate = establishDate;
    }
  	public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
  	public String getAutoDue() {
        return autoDue;
    }

    public void setAutoDue(String autoDue) {
        this.autoDue = autoDue;
    }
  	public String getCleanDate() {
        return cleanDate;
    }

    public void setCleanDate(String cleanDate) {
        this.cleanDate = cleanDate;
    }
  	public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }
  	public String getTargetProductType() {
        return targetProductType;
    }

    public void setTargetProductType(String targetProductType) {
        this.targetProductType = targetProductType;
    }
  	public String getProductRegistCode() {
        return productRegistCode;
    }

    public void setProductRegistCode(String productRegistCode) {
        this.productRegistCode = productRegistCode;
    }
  	public String getProductDays() {
        return productDays;
    }

    public void setProductDays(String productDays) {
        this.productDays = productDays;
    }
  	public String getSelfSupportFlag() {
        return selfSupportFlag;
    }

    public void setSelfSupportFlag(String selfSupportFlag) {
        this.selfSupportFlag = selfSupportFlag;
    }
  	public String getFirstMinAmountIndividual() {
        return firstMinAmountIndividual;
    }

    public void setFirstMinAmountIndividual(String firstMinAmountIndividual) {
        this.firstMinAmountIndividual = firstMinAmountIndividual;
    }
  	public String getFirstMinAmountInstitution() {
        return firstMinAmountInstitution;
    }

    public void setFirstMinAmountInstitution(String firstMinAmountInstitution) {
        this.firstMinAmountInstitution = firstMinAmountInstitution;
    }
  	public String getFirstMinAmountInterbank() {
        return firstMinAmountInterbank;
    }

    public void setFirstMinAmountInterbank(String firstMinAmountInterbank) {
        this.firstMinAmountInterbank = firstMinAmountInterbank;
    }
  	public String getOpenCycleType() {
        return openCycleType;
    }

    public void setOpenCycleType(String openCycleType) {
        this.openCycleType = openCycleType;
    }
  	public String getOpenCycleIndex() {
        return openCycleIndex;
    }

    public void setOpenCycleIndex(String openCycleIndex) {
        this.openCycleIndex = openCycleIndex;
    }
  	public String getFirstOpenDay() {
        return firstOpenDay;
    }

    public void setFirstOpenDay(String firstOpenDay) {
        this.firstOpenDay = firstOpenDay;
    }
  	public String getStartFromLast() {
        return startFromLast;
    }

    public void setStartFromLast(String startFromLast) {
        this.startFromLast = startFromLast;
    }
  	public String getHaveHoliday() {
        return haveHoliday;
    }

    public void setHaveHoliday(String haveHoliday) {
        this.haveHoliday = haveHoliday;
    }
  	public String getNetvalue() {
        return netvalue;
    }

    public void setNetvalue(String netvalue) {
        this.netvalue = netvalue;
    }
  	public String getNetvalueDate() {
        return netvalueDate;
    }

    public void setNetvalueDate(String netvalueDate) {
        this.netvalueDate = netvalueDate;
    }
  	public String getConfirmDays() {
        return confirmDays;
    }

    public void setConfirmDays(String confirmDays) {
        this.confirmDays = confirmDays;
    }
  	public String getPerforCompareNormText() {
        return perforCompareNormText;
    }

    public void setPerforCompareNormText(String perforCompareNormText) {
        this.perforCompareNormText = perforCompareNormText;
    }
  	public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
  	public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }
  	public String getCheckerNo() {
        return checkerNo;
    }

    public void setCheckerNo(String checkerNo) {
        this.checkerNo = checkerNo;
    }

}