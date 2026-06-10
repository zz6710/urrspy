package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodIssRgInfoErrService",table = "app_prod_issuance_regist_info_erdesc")
public class ProdIssRgInfoErr {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码错误信息", sql = "prod_code_desc = $S{prodCodeDesc}" ,field = "prod_code_desc")
   private String prodCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "发行单位代码错误信息", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品代码错误信息", sql = "prod_ident_code_desc = $S{prodIdentCodeDesc}" ,field = "prod_ident_code_desc")
   private String prodIdentCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期错误信息", sql = "subscription_start_date_desc = $S{subscriptionStartDateDesc}" ,field = "subscription_start_date_desc")
   private String subscriptionStartDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "募集结束日期错误信息", sql = "subscription_end_date_desc = $S{subscriptionEndDateDesc}" ,field = "subscription_end_date_desc")
   private String subscriptionEndDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品起始日期错误信息", sql = "prod_value_date_desc = $S{prodValueDateDesc}" ,field = "prod_value_date_desc")
   private String prodValueDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品终止日期错误信息", sql = "prod_maturity_date_desc = $S{prodMaturityDateDesc}" ,field = "prod_maturity_date_desc")
   private String prodMaturityDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "管理方式错误信息", sql = "management_method_desc = $S{managementMethodDesc}" ,field = "management_method_desc")
   private String managementMethodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "是否为结构化产品错误信息", sql = "structured_prod_desc = $S{structuredProdDesc}" ,field = "structured_prod_desc")
   private String structuredProdDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准说明错误信息", sql = "details_per_rate_desc = $S{detailsPerRateDesc}" ,field = "details_per_rate_desc")
   private String detailsPerRateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "开放模式错误信息", sql = "opening_mode_desc = $S{openingModeDesc}" ,field = "opening_mode_desc")
   private String openingModeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准上限错误信息", sql = "up_limit_per_rate_desc = $S{upLimitPerRateDesc}" ,field = "up_limit_per_rate_desc")
   private String upLimitPerRateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准下限)错误信息", sql = "low_limit_per_rate_desc = $S{lowLimitPerRateDesc}" ,field = "low_limit_per_rate_desc")
   private String lowLimitPerRateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "规律开放周期错误信息", sql = "regular_open_period_desc = $S{regularOpenPeriodDesc}" ,field = "regular_open_period_desc")
   private String regularOpenPeriodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他规律开发周期错误信息", sql = "other_open_period_desc = $S{otherOpenPeriodDesc}" ,field = "other_open_period_desc")
   private String otherOpenPeriodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "无规律开放说明错误信息", sql = "disorder_open_period_desc = $S{disorderOpenPeriodDesc}" ,field = "disorder_open_period_desc")
   private String disorderOpenPeriodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "首次开放周期起始日错误信息", sql = "first_open_day_desc = $S{firstOpenDayDesc}" ,field = "first_open_day_desc")
   private String firstOpenDayDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "节假日是否开放错误信息", sql = "holiday_open_type_desc = $S{holidayOpenTypeDesc}" ,field = "holiday_open_type_desc")
   private String holidayOpenTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "平均开放次数错误信息", sql = "average_open_no_desc = $S{averageOpenNoDesc}" ,field = "average_open_no_desc")
   private String averageOpenNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "开放期业务错误信息", sql = "busi_open_period_desc = $S{busiOpenPeriodDesc}" ,field = "busi_open_period_desc")
   private String busiOpenPeriodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "开放期业务说明错误信息", sql = "details_busi_op_period_desc = $S{detailsBusiOpPeriodDesc}" ,field = "details_busi_op_period_desc")
   private String detailsBusiOpPeriodDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "资金托管账号错误信息", sql = "custody_acct_no_desc = $S{custodyAcctNoDesc}" ,field = "custody_acct_no_desc")
   private String custodyAcctNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "资金托管账户错误信息", sql = "custody_acct_name_desc = $S{custodyAcctNameDesc}" ,field = "custody_acct_name_desc")
   private String custodyAcctNameDesc;
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
  	public String getProdCodeDesc() {
        return prodCodeDesc;
    }

    public void setProdCodeDesc(String prodCodeDesc) {
        this.prodCodeDesc = prodCodeDesc;
    }
  	public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }
  	public String getProdIdentCodeDesc() {
        return prodIdentCodeDesc;
    }

    public void setProdIdentCodeDesc(String prodIdentCodeDesc) {
        this.prodIdentCodeDesc = prodIdentCodeDesc;
    }
  	public String getSubscriptionStartDateDesc() {
        return subscriptionStartDateDesc;
    }

    public void setSubscriptionStartDateDesc(String subscriptionStartDateDesc) {
        this.subscriptionStartDateDesc = subscriptionStartDateDesc;
    }
  	public String getSubscriptionEndDateDesc() {
        return subscriptionEndDateDesc;
    }

    public void setSubscriptionEndDateDesc(String subscriptionEndDateDesc) {
        this.subscriptionEndDateDesc = subscriptionEndDateDesc;
    }
  	public String getProdValueDateDesc() {
        return prodValueDateDesc;
    }

    public void setProdValueDateDesc(String prodValueDateDesc) {
        this.prodValueDateDesc = prodValueDateDesc;
    }
  	public String getProdMaturityDateDesc() {
        return prodMaturityDateDesc;
    }

    public void setProdMaturityDateDesc(String prodMaturityDateDesc) {
        this.prodMaturityDateDesc = prodMaturityDateDesc;
    }
  	public String getManagementMethodDesc() {
        return managementMethodDesc;
    }

    public void setManagementMethodDesc(String managementMethodDesc) {
        this.managementMethodDesc = managementMethodDesc;
    }
  	public String getStructuredProdDesc() {
        return structuredProdDesc;
    }

    public void setStructuredProdDesc(String structuredProdDesc) {
        this.structuredProdDesc = structuredProdDesc;
    }
  	public String getDetailsPerRateDesc() {
        return detailsPerRateDesc;
    }

    public void setDetailsPerRateDesc(String detailsPerRateDesc) {
        this.detailsPerRateDesc = detailsPerRateDesc;
    }
  	public String getOpeningModeDesc() {
        return openingModeDesc;
    }

    public void setOpeningModeDesc(String openingModeDesc) {
        this.openingModeDesc = openingModeDesc;
    }
  	public String getUpLimitPerRateDesc() {
        return upLimitPerRateDesc;
    }

    public void setUpLimitPerRateDesc(String upLimitPerRateDesc) {
        this.upLimitPerRateDesc = upLimitPerRateDesc;
    }
  	public String getLowLimitPerRateDesc() {
        return lowLimitPerRateDesc;
    }

    public void setLowLimitPerRateDesc(String lowLimitPerRateDesc) {
        this.lowLimitPerRateDesc = lowLimitPerRateDesc;
    }
  	public String getRegularOpenPeriodDesc() {
        return regularOpenPeriodDesc;
    }

    public void setRegularOpenPeriodDesc(String regularOpenPeriodDesc) {
        this.regularOpenPeriodDesc = regularOpenPeriodDesc;
    }
  	public String getOtherOpenPeriodDesc() {
        return otherOpenPeriodDesc;
    }

    public void setOtherOpenPeriodDesc(String otherOpenPeriodDesc) {
        this.otherOpenPeriodDesc = otherOpenPeriodDesc;
    }
  	public String getDisorderOpenPeriodDesc() {
        return disorderOpenPeriodDesc;
    }

    public void setDisorderOpenPeriodDesc(String disorderOpenPeriodDesc) {
        this.disorderOpenPeriodDesc = disorderOpenPeriodDesc;
    }
  	public String getFirstOpenDayDesc() {
        return firstOpenDayDesc;
    }

    public void setFirstOpenDayDesc(String firstOpenDayDesc) {
        this.firstOpenDayDesc = firstOpenDayDesc;
    }
  	public String getHolidayOpenTypeDesc() {
        return holidayOpenTypeDesc;
    }

    public void setHolidayOpenTypeDesc(String holidayOpenTypeDesc) {
        this.holidayOpenTypeDesc = holidayOpenTypeDesc;
    }
  	public String getAverageOpenNoDesc() {
        return averageOpenNoDesc;
    }

    public void setAverageOpenNoDesc(String averageOpenNoDesc) {
        this.averageOpenNoDesc = averageOpenNoDesc;
    }
  	public String getBusiOpenPeriodDesc() {
        return busiOpenPeriodDesc;
    }

    public void setBusiOpenPeriodDesc(String busiOpenPeriodDesc) {
        this.busiOpenPeriodDesc = busiOpenPeriodDesc;
    }
  	public String getDetailsBusiOpPeriodDesc() {
        return detailsBusiOpPeriodDesc;
    }

    public void setDetailsBusiOpPeriodDesc(String detailsBusiOpPeriodDesc) {
        this.detailsBusiOpPeriodDesc = detailsBusiOpPeriodDesc;
    }
  	public String getCustodyAcctNoDesc() {
        return custodyAcctNoDesc;
    }

    public void setCustodyAcctNoDesc(String custodyAcctNoDesc) {
        this.custodyAcctNoDesc = custodyAcctNoDesc;
    }
  	public String getCustodyAcctNameDesc() {
        return custodyAcctNameDesc;
    }

    public void setCustodyAcctNameDesc(String custodyAcctNameDesc) {
        this.custodyAcctNameDesc = custodyAcctNameDesc;
    }


}