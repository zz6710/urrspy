package com.kayak.rpt.zz.historyInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodIssuanceRegistInfohService",table = "app_prod_issuance_regist_info_h")
public class ProdIssuanceRegistInfoh {
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "发行单位代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品代码", sql = "prod_ident_code = $S{prodIdentCode}" ,field = "prod_ident_code")
   private String prodIdentCode;
   @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期", sql = "subscription_start_date = $S{subscriptionStartDate}" ,field = "subscription_start_date")
   private String subscriptionStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "募集结束日期", sql = "subscription_end_date = $S{subscriptionEndDate}" ,field = "subscription_end_date")
   private String subscriptionEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品起始日期", sql = "prod_value_date = $S{prodValueDate}" ,field = "prod_value_date")
   private String prodValueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品终止日期", sql = "prod_maturity_date = $S{prodMaturityDate}" ,field = "prod_maturity_date")
   private String prodMaturityDate;
   @GraphQLField(kkhtml = "KFieldText", label = "管理方式", sql = "management_method = $S{managementMethod}" ,field = "management_method")
   private String managementMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为结构化产品", sql = "structured_prod = $S{structuredProd}" ,field = "structured_prod")
   private String structuredProd;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准说明", sql = "details_per_rate = $S{detailsPerRate}" ,field = "details_per_rate")
   private String detailsPerRate;
   @GraphQLField(kkhtml = "KFieldText", label = "开放模式", sql = "opening_mode = $S{openingMode}" ,field = "opening_mode")
   private String openingMode;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准上限", sql = "up_limit_per_rate = $S{upLimitPerRate}" ,field = "up_limit_per_rate")
   private Double upLimitPerRate;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准下限", sql = "low_limit_per_rate = $S{lowLimitPerRate}" ,field = "low_limit_per_rate")
   private Double lowLimitPerRate;
   @GraphQLField(kkhtml = "KFieldText", label = "规律开放周期", sql = "regular_open_period = $S{regularOpenPeriod}" ,field = "regular_open_period")
   private String regularOpenPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "其他规律开发周期", sql = "other_open_period = $S{otherOpenPeriod}" ,field = "other_open_period")
   private String otherOpenPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "无规律开放说明", sql = "disorder_open_period = $S{disorderOpenPeriod}" ,field = "disorder_open_period")
   private String disorderOpenPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "首次开放周期起始日", sql = "first_open_day = $S{firstOpenDay}" ,field = "first_open_day")
   private String firstOpenDay;
   @GraphQLField(kkhtml = "KFieldText", label = "节假日是否开放", sql = "holiday_open_type = $S{holidayOpenType}" ,field = "holiday_open_type")
   private String holidayOpenType;
   @GraphQLField(kkhtml = "KFieldText", label = "平均开放次数", sql = "average_open_no = $S{averageOpenNo}" ,field = "average_open_no")
   private Double averageOpenNo;
   @GraphQLField(kkhtml = "KFieldText", label = "开放期业务", sql = "busi_open_period = $S{busiOpenPeriod}" ,field = "busi_open_period")
   private String busiOpenPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "开放期业务说明", sql = "details_busi_op_period = $S{detailsBusiOpPeriod}" ,field = "details_busi_op_period")
   private String detailsBusiOpPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "资金托管账号", sql = "custody_acct_no = $S{custodyAcctNo}" ,field = "custody_acct_no")
   private String custodyAcctNo;
   @GraphQLField(kkhtml = "KFieldText", label = "资金托管账户", sql = "custody_acct_name = $S{custodyAcctName}" ,field = "custody_acct_name")
   private String custodyAcctName;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(label = "导入日期开始时间")
    private String startDate;

    @GraphQLField(label = "导入日期截止时间")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "clsf_sto = $S{clsfSto}" ,field = "clsf_sto")
    private Double clsfSto;
    @GraphQLField(kkhtml = "KFieldText", label = "定期开放周期（天）", sql = "regular_open_period_day = $S{regularOpenPeriodDay}" ,field = "regular_open_period_day")
    private String regularOpenPeriodDay;
  	public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
  	public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
  	public String getProdIdentCode() {
        return prodIdentCode;
    }

    public void setProdIdentCode(String prodIdentCode) {
        this.prodIdentCode = prodIdentCode;
    }
  	public String getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(String subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }
  	public String getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(String subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }
  	public String getProdValueDate() {
        return prodValueDate;
    }

    public void setProdValueDate(String prodValueDate) {
        this.prodValueDate = prodValueDate;
    }
  	public String getProdMaturityDate() {
        return prodMaturityDate;
    }

    public void setProdMaturityDate(String prodMaturityDate) {
        this.prodMaturityDate = prodMaturityDate;
    }
  	public String getManagementMethod() {
        return managementMethod;
    }

    public void setManagementMethod(String managementMethod) {
        this.managementMethod = managementMethod;
    }
  	public String getStructuredProd() {
        return structuredProd;
    }

    public void setStructuredProd(String structuredProd) {
        this.structuredProd = structuredProd;
    }
  	public String getDetailsPerRate() {
        return detailsPerRate;
    }

    public void setDetailsPerRate(String detailsPerRate) {
        this.detailsPerRate = detailsPerRate;
    }
  	public String getOpeningMode() {
        return openingMode;
    }

    public void setOpeningMode(String openingMode) {
        this.openingMode = openingMode;
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
  	public Double getUpLimitPerRate() {
        return upLimitPerRate;
    }

    public void setUpLimitPerRate(Double upLimitPerRate) {
        this.upLimitPerRate = upLimitPerRate;
    }
  	public Double getLowLimitPerRate() {
        return lowLimitPerRate;
    }

    public void setLowLimitPerRate(Double lowLimitPerRate) {
        this.lowLimitPerRate = lowLimitPerRate;
    }
  	public String getRegularOpenPeriod() {
        return regularOpenPeriod;
    }

    public void setRegularOpenPeriod(String regularOpenPeriod) {
        this.regularOpenPeriod = regularOpenPeriod;
    }
  	public String getOtherOpenPeriod() {
        return otherOpenPeriod;
    }

    public void setOtherOpenPeriod(String otherOpenPeriod) {
        this.otherOpenPeriod = otherOpenPeriod;
    }
  	public String getDisorderOpenPeriod() {
        return disorderOpenPeriod;
    }

    public void setDisorderOpenPeriod(String disorderOpenPeriod) {
        this.disorderOpenPeriod = disorderOpenPeriod;
    }
  	public String getFirstOpenDay() {
        return firstOpenDay;
    }

    public void setFirstOpenDay(String firstOpenDay) {
        this.firstOpenDay = firstOpenDay;
    }
  	public String getHolidayOpenType() {
        return holidayOpenType;
    }

    public void setHolidayOpenType(String holidayOpenType) {
        this.holidayOpenType = holidayOpenType;
    }
  	public Double getAverageOpenNo() {
        return averageOpenNo;
    }

    public void setAverageOpenNo(Double averageOpenNo) {
        this.averageOpenNo = averageOpenNo;
    }
  	public String getBusiOpenPeriod() {
        return busiOpenPeriod;
    }

    public void setBusiOpenPeriod(String busiOpenPeriod) {
        this.busiOpenPeriod = busiOpenPeriod;
    }
  	public String getDetailsBusiOpPeriod() {
        return detailsBusiOpPeriod;
    }

    public void setDetailsBusiOpPeriod(String detailsBusiOpPeriod) {
        this.detailsBusiOpPeriod = detailsBusiOpPeriod;
    }
  	public String getCustodyAcctNo() {
        return custodyAcctNo;
    }

    public void setCustodyAcctNo(String custodyAcctNo) {
        this.custodyAcctNo = custodyAcctNo;
    }
  	public String getCustodyAcctName() {
        return custodyAcctName;
    }

    public void setCustodyAcctName(String custodyAcctName) {
        this.custodyAcctName = custodyAcctName;
    }

    public String getStartDate() {
        return startDate;
    }

    public Double getClsfSto() {
        return clsfSto;
    }

    public void setClsfSto(Double clsfSto) {
        this.clsfSto = clsfSto;
    }
}