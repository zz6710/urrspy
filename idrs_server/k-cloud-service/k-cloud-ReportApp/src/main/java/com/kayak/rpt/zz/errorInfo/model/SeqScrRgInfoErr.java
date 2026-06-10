package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "seqScrRgInfoErrService",table = "app_subseq_subscr_regist_info_erdesc")
public class SeqScrRgInfoErr {
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码错误", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "初始净值错误", sql = "initial_nav = $S{initialNav}" ,field = "initial_nav")
   private String initialNav;
   @GraphQLField(kkhtml = "KFieldText", label = "产品净值错误", sql = "nav = $S{nav}" ,field = "nav")
   private String nav;
   @GraphQLField(kkhtml = "KFieldText", label = "累计净值错误", sql = "aggregate_nav = $S{aggregateNav}" ,field = "aggregate_nav")
   private String aggregateNav;
   @GraphQLField(kkhtml = "KFieldText", label = "净值币种错误", sql = "nav_cur = $S{navCur}" ,field = "nav_cur")
   private String navCur;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币净值错误", sql = "convert_rmb_nav = $S{convertRmbNav}" ,field = "convert_rmb_nav")
   private String convertRmbNav;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币累计净值错误", sql = "convert_rmb_agg_nav = $S{convertRmbAggNav}" ,field = "convert_rmb_agg_nav")
   private String convertRmbAggNav;
   @GraphQLField(kkhtml = "KFieldText", label = "实现收益率%错误", sql = "realized_annual_return = $S{realizedAnnualReturn}" ,field = "realized_annual_return")
   private String realizedAnnualReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "最新预期收益率%错误", sql = "expected_annual_return = $S{expectedAnnualReturn}" ,field = "expected_annual_return")
   private String expectedAnnualReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "银行实现收益", sql = "inconme_bank = $S{inconmeBank}" ,field = "inconme_bank")
   private String inconmeBank;
   @GraphQLField(kkhtml = "KFieldText", label = "业务起始日错误", sql = "business_start_date = $S{businessStartDate}" ,field = "business_start_date")
   private String businessStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "业务结束日错误", sql = "business_end_date = $S{businessEndDate}" ,field = "business_end_date")
   private String businessEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "币种错误", sql = "cur = $S{cur}" ,field = "cur")
   private String cur;
   @GraphQLField(kkhtml = "KFieldText", label = "该币种累计申购金额", sql = "sub_amt_lass_period = $S{subAmtLassPeriod}" ,field = "sub_amt_lass_period")
   private String subAmtLassPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "该币种累计兑付金额", sql = "cur_principal_period = $S{curPrincipalPeriod}" ,field = "cur_principal_period")
   private String curPrincipalPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "该币种累计兑付收益金额", sql = "cur_pay_period = $S{curPayPeriod}" ,field = "cur_pay_period")
   private String curPayPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "累计申购份额错误", sql = "subscribed_latest_vol = $S{subscribedLatestVol}" ,field = "subscribed_latest_vol")
   private String subscribedLatestVol;
   @GraphQLField(kkhtml = "KFieldText", label = "累计赎回份额错误", sql = "redeemed_latest_vol = $S{redeemedLatestVol}" ,field = "redeemed_latest_vol")
   private String redeemedLatestVol;
   @GraphQLField(kkhtml = "KFieldText", label = "每万份份额分红错误", sql = "units_bonus = $S{unitsBonus}" ,field = "units_bonus")
   private String unitsBonus;
   @GraphQLField(kkhtml = "KFieldText", label = "每万份现金分红错误", sql = "cash_bonus = $S{cashBonus}" ,field = "cash_bonus")
   private String cashBonus;
   @GraphQLField(kkhtml = "KFieldText", label = "产品余额错误", sql = "prod_amt = $S{prodAmt}" ,field = "prod_amt")
   private String prodAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "产品份额错误", sql = "prod_vol = $S{prodVol}" ,field = "prod_vol")
   private String prodVol;
   @GraphQLField(kkhtml = "KFieldText", label = "备注错误", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币初始净值错误", sql = "convert_initial_nav = $S{convertInitialNav}" ,field = "convert_initial_nav")
   private String convertInitialNav;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
    @GraphQLField(label = "导入日期开始时间")
    private String impStartDate;

    @GraphQLField(label = "导入日期截止时间")
    private String impEndDate;
    @GraphQLField()
    private String registerDate;
    @GraphQLField()
    private String registerStatus;
    @GraphQLField()
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;


    @GraphQLField(kkhtml = "KFieldText", label = "报告日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;

  	public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
  	public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
  	public String getInitialNav() {
        return initialNav;
    }

    public void setInitialNav(String initialNav) {
        this.initialNav = initialNav;
    }
  	public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }
  	public String getAggregateNav() {
        return aggregateNav;
    }

    public void setAggregateNav(String aggregateNav) {
        this.aggregateNav = aggregateNav;
    }
  	public String getNavCur() {
        return navCur;
    }

    public void setNavCur(String navCur) {
        this.navCur = navCur;
    }
  	public String getConvertRmbNav() {
        return convertRmbNav;
    }

    public void setConvertRmbNav(String convertRmbNav) {
        this.convertRmbNav = convertRmbNav;
    }
  	public String getConvertRmbAggNav() {
        return convertRmbAggNav;
    }

    public void setConvertRmbAggNav(String convertRmbAggNav) {
        this.convertRmbAggNav = convertRmbAggNav;
    }
  	public String getRealizedAnnualReturn() {
        return realizedAnnualReturn;
    }

    public void setRealizedAnnualReturn(String realizedAnnualReturn) {
        this.realizedAnnualReturn = realizedAnnualReturn;
    }
  	public String getExpectedAnnualReturn() {
        return expectedAnnualReturn;
    }

    public void setExpectedAnnualReturn(String expectedAnnualReturn) {
        this.expectedAnnualReturn = expectedAnnualReturn;
    }
  	public String getInconmeBank() {
        return inconmeBank;
    }

    public void setInconmeBank(String inconmeBank) {
        this.inconmeBank = inconmeBank;
    }
  	public String getBusinessStartDate() {
        return businessStartDate;
    }

    public void setBusinessStartDate(String businessStartDate) {
        this.businessStartDate = businessStartDate;
    }
  	public String getBusinessEndDate() {
        return businessEndDate;
    }

    public void setBusinessEndDate(String businessEndDate) {
        this.businessEndDate = businessEndDate;
    }
  	public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }
  	public String getSubAmtLassPeriod() {
        return subAmtLassPeriod;
    }

    public void setSubAmtLassPeriod(String subAmtLassPeriod) {
        this.subAmtLassPeriod = subAmtLassPeriod;
    }
  	public String getCurPrincipalPeriod() {
        return curPrincipalPeriod;
    }

    public void setCurPrincipalPeriod(String curPrincipalPeriod) {
        this.curPrincipalPeriod = curPrincipalPeriod;
    }
  	public String getCurPayPeriod() {
        return curPayPeriod;
    }

    public void setCurPayPeriod(String curPayPeriod) {
        this.curPayPeriod = curPayPeriod;
    }
  	public String getSubscribedLatestVol() {
        return subscribedLatestVol;
    }

    public void setSubscribedLatestVol(String subscribedLatestVol) {
        this.subscribedLatestVol = subscribedLatestVol;
    }
  	public String getRedeemedLatestVol() {
        return redeemedLatestVol;
    }

    public void setRedeemedLatestVol(String redeemedLatestVol) {
        this.redeemedLatestVol = redeemedLatestVol;
    }
  	public String getUnitsBonus() {
        return unitsBonus;
    }

    public void setUnitsBonus(String unitsBonus) {
        this.unitsBonus = unitsBonus;
    }
  	public String getCashBonus() {
        return cashBonus;
    }

    public void setCashBonus(String cashBonus) {
        this.cashBonus = cashBonus;
    }
  	public String getProdAmt() {
        return prodAmt;
    }

    public void setProdAmt(String prodAmt) {
        this.prodAmt = prodAmt;
    }
  	public String getProdVol() {
        return prodVol;
    }

    public void setProdVol(String prodVol) {
        this.prodVol = prodVol;
    }
  	public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
  	public String getConvertInitialNav() {
        return convertInitialNav;
    }

    public void setConvertInitialNav(String convertInitialNav) {
        this.convertInitialNav = convertInitialNav;
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


}