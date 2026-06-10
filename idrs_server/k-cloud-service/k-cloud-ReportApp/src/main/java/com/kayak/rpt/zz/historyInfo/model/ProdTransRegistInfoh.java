package com.kayak.rpt.zz.historyInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodTransRegistInfohService",table = "app_prod_trans_regist_info_h")
public class ProdTransRegistInfoh {
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "行内交易编码", sql = "trans_code = $S{transCode}" ,field = "trans_code")
   private String transCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资产负债编码", sql = "asset_code = $S{assetCode}" ,field = "asset_code")
   private String assetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cur = $S{cur}" ,field = "cur")
   private String cur;
   @GraphQLField(kkhtml = "KFieldText", label = "发生金额", sql = "amt = $S{amt}" ,field = "amt")
   private String amt;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额", sql = "convert_rmb = $S{convertRmb}" ,field = "convert_rmb")
   private String convertRmb;
   @GraphQLField(kkhtml = "KFieldText", label = "数量", sql = "quantity = $S{quantity}" ,field = "quantity")
   private String quantity;
   @GraphQLField(kkhtml = "KFieldText", label = "资产计量方式", sql = "method_asset_measure = $S{methodAssetMeasure}" ,field = "method_asset_measure")
   private String methodAssetMeasure;
   @GraphQLField(kkhtml = "KFieldText", label = "资金流动类型", sql = "cash_type = $S{cashType}" ,field = "cash_type")
   private String cashType;
   @GraphQLField(kkhtml = "KFieldText", label = "资金流动类型说明", sql = "detail_cash_type = $S{detailCashType}" ,field = "detail_cash_type")
   private String detailCashType;
   @GraphQLField(kkhtml = "KFieldText", label = "交易日", sql = "trade_date = $S{tradeDate}" ,field = "trade_date")
   private String tradeDate;
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手方", sql = "trade_counter = $S{tradeCounter}" ,field = "trade_counter")
   private String tradeCounter;
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手方类型", sql = "counter_type = $S{counterType}" ,field = "counter_type")
   private String counterType;
   @GraphQLField(kkhtml = "KFieldText", label = "单位成交价格", sql = "unit_price_full = $S{unitPriceFull}" ,field = "unit_price_full")
   private String unitPriceFull;
   @GraphQLField(kkhtml = "KFieldText", label = "单位成交价格", sql = "unit_price_net = $S{unitPriceNet}" ,field = "unit_price_net")
   private String unitPriceNet;
   @GraphQLField(kkhtml = "KFieldText", label = "到期收益率%", sql = "rate_annual_return = $S{rateAnnualReturn}" ,field = "rate_annual_return")
   private Double rateAnnualReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "成交编号/合同号", sql = "trans_ident_code = $S{transIdentCode}" ,field = "trans_ident_code")
   private String transIdentCode;
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
    @GraphQLField(label = "开始时间")
    private String startDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(label = "结束时间")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(label = "关联交易情况")
    private String relatedPartyTrans;
    @GraphQLField(label = "审批人身份证号")
    private String transApproveId;
    @GraphQLField(label = "审批人姓名")
    private String transApproveName;
    @GraphQLField(label = "交易人身份证号")
    private String traderId;
    @GraphQLField(label = "交易人姓名")
    private String traderName;
    @GraphQLField(label = "交易时间")
    private String transOriginTime;
    @GraphQLField
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "交易发起时间", sql = "trx_tm = $S{trxTm}" ,field = "trxTm")
    private String trxTm;
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
  	public String getTransCode() {
        return transCode;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }
  	public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
  	public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }
  	public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
  	public String getConvertRmb() {
        return convertRmb;
    }

    public void setConvertRmb(String convertRmb) {
        this.convertRmb = convertRmb;
    }
  	public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
  	public String getMethodAssetMeasure() {
        return methodAssetMeasure;
    }

    public void setMethodAssetMeasure(String methodAssetMeasure) {
        this.methodAssetMeasure = methodAssetMeasure;
    }
  	public String getCashType() {
        return cashType;
    }

    public void setCashType(String cashType) {
        this.cashType = cashType;
    }
  	public String getDetailCashType() {
        return detailCashType;
    }

    public void setDetailCashType(String detailCashType) {
        this.detailCashType = detailCashType;
    }
  	public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
  	public String getTradeCounter() {
        return tradeCounter;
    }

    public void setTradeCounter(String tradeCounter) {
        this.tradeCounter = tradeCounter;
    }
  	public String getCounterType() {
        return counterType;
    }

    public void setCounterType(String counterType) {
        this.counterType = counterType;
    }
  	public String getUnitPriceFull() {
        return unitPriceFull;
    }

    public void setUnitPriceFull(String unitPriceFull) {
        this.unitPriceFull = unitPriceFull;
    }
  	public String getUnitPriceNet() {
        return unitPriceNet;
    }

    public void setUnitPriceNet(String unitPriceNet) {
        this.unitPriceNet = unitPriceNet;
    }
  	public Double getRateAnnualReturn() {
        return rateAnnualReturn;
    }

    public void setRateAnnualReturn(Double rateAnnualReturn) {
        this.rateAnnualReturn = rateAnnualReturn;
    }
  	public String getTransIdentCode() {
        return transIdentCode;
    }

    public void setTransIdentCode(String transIdentCode) {
        this.transIdentCode = transIdentCode;
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

    public String getStartDate() {
  	    return startDate;
    }
}