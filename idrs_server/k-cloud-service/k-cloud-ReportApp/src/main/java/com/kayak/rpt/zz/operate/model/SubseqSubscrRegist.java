package com.kayak.rpt.zz.operate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "subseqSubscrRegistService",table = "app_subseq_subscr_regist_remark")
public class SubseqSubscrRegist {
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "初始净值", sql = "initial_nav = $S{initialNav}" ,field = "initial_nav")
   private String initialNav;
   @GraphQLField(kkhtml = "KFieldText", label = "产品净值", sql = "nav = $S{nav}" ,field = "nav")
   private String nav;
   @GraphQLField(kkhtml = "KFieldText", label = "累计净值", sql = "aggregate_nav = $S{aggregateNav}" ,field = "aggregate_nav")
   private String aggregateNav;
   @GraphQLField(kkhtml = "KFieldText", label = "净值币种", sql = "nav_cur = $S{navCur}" ,field = "nav_cur")
   private String navCur;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币净值", sql = "convert_rmb_nav = $S{convertRmbNav}" ,field = "convert_rmb_nav")
   private String convertRmbNav;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币累计净值", sql = "convert_rmb_agg_nav = $S{convertRmbAggNav}" ,field = "convert_rmb_agg_nav")
   private String convertRmbAggNav;
   @GraphQLField(kkhtml = "KFieldText", label = "实现收益率%", sql = "realized_annual_return = $S{realizedAnnualReturn}" ,field = "realized_annual_return")
   private Double realizedAnnualReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "最新预期收益率%", sql = "expected_annual_return = $S{expectedAnnualReturn}" ,field = "expected_annual_return")
   private Double expectedAnnualReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "银行实现收益", sql = "inconme_bank = $S{inconmeBank}" ,field = "inconme_bank")
   private String inconmeBank;
   @GraphQLField(kkhtml = "KFieldText", label = "业务起始日", sql = "business_start_date = $S{businessStartDate}" ,field = "business_start_date")
   private String businessStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "业务结束日", sql = "business_end_date = $S{businessEndDate}" ,field = "business_end_date")
   private String businessEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "累计申购份额", sql = "subscribed_latest_vol = $S{subscribedLatestVol}" ,field = "subscribed_latest_vol")
   private String subscribedLatestVol;
   @GraphQLField(kkhtml = "KFieldText", label = "累计赎回份额", sql = "redeemed_latest_vol = $S{redeemedLatestVol}" ,field = "redeemed_latest_vol")
   private String redeemedLatestVol;
   @GraphQLField(kkhtml = "KFieldText", label = "每万份份额分红", sql = "units_bonus = $S{unitsBonus}" ,field = "units_bonus")
   private String unitsBonus;
   @GraphQLField(kkhtml = "KFieldText", label = "每万份现金分红", sql = "cash_bonus = $S{cashBonus}" ,field = "cash_bonus")
   private String cashBonus;
   @GraphQLField(kkhtml = "KFieldText", label = "产品余额", sql = "prod_amt = $S{prodAmt}" ,field = "prod_amt")
   private String prodAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "产品份额", sql = "prod_vol = $S{prodVol}" ,field = "prod_vol")
   private String prodVol;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币初始净值", sql = "convert_initial_nav = $S{convertInitialNav}" ,field = "convert_initial_nav")
   private String convertInitialNav;
   @GraphQLField(kkhtml = "KFieldText", label = "操作人员", sql = "summit_user like '%$U{summitUser}%'" ,field = "summit_user")
   private String summitUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "数据操作类型（D", sql = "op_type = $S{opType}" ,field = "op_type")
   private String opType;
   @GraphQLField(kkhtml = "KFieldText", label = "净值日期", sql = "nav_dt = $S{navDt}" ,field = "nav_dt")
   private String navDt;
    @GraphQLField(label = "开始时间")
    private String startDate;

    @GraphQLField(label = "结束时间")
    private String endDate;
    @GraphQLField(label = "开始时间")
    private String busiStartDate;

    @GraphQLField(label = "结束时间")
    private String busiEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "币种和申购兑付信息", sql = "ccy_and_pch_rdm = $S{ccyAndPchRdm}" ,field = "ccyAndPchRdm")
    private String ccyAndPchRdm;
    @GraphQLField(label = "报送日期")
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
  	public Double getRealizedAnnualReturn() {
        return realizedAnnualReturn;
    }

    public void setRealizedAnnualReturn(Double realizedAnnualReturn) {
        this.realizedAnnualReturn = realizedAnnualReturn;
    }
  	public Double getExpectedAnnualReturn() {
        return expectedAnnualReturn;
    }

    public void setExpectedAnnualReturn(Double expectedAnnualReturn) {
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
  	public String getConvertInitialNav() {
        return convertInitialNav;
    }

    public void setConvertInitialNav(String convertInitialNav) {
        this.convertInitialNav = convertInitialNav;
    }
  	public String getSummitUser() {
        return summitUser;
    }

    public void setSummitUser(String summitUser) {
        this.summitUser = summitUser;
    }
  	public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
  	public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
  	public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

}