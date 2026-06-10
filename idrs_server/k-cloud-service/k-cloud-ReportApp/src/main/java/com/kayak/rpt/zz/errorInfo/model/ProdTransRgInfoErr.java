package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodTransRgInfoErrService",table = "app_prod_trans_regist_info_erdesc")
public class ProdTransRgInfoErr {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码错误", sql = "prod_code_desc = $S{prodCodeDesc}" ,field = "prod_code_desc")
   private String prodCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "行内交易编码错误", sql = "trans_code_desc = $S{transCodeDesc}" ,field = "trans_code_desc")
   private String transCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "资产负债编码错误", sql = "asset_code_desc = $S{assetCodeDesc}" ,field = "asset_code_desc")
   private String assetCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "币种错误", sql = "cur_desc = $S{curDesc}" ,field = "cur_desc")
   private String curDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "发生金额错误", sql = "amt_desc = $S{amtDesc}" ,field = "amt_desc")
   private String amtDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额", sql = "convert_rmb_desc = $S{convertRmbDesc}" ,field = "convert_rmb_desc")
   private String convertRmbDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "数量错误", sql = "quantity_desc = $S{quantityDesc}" ,field = "quantity_desc")
   private String quantityDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "资产计量方式错误", sql = "method_asset_measure_desc = $S{methodAssetMeasureDesc}" ,field = "method_asset_measure_desc")
   private String methodAssetMeasureDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "资金流动类型错误", sql = "cash_type_desc = $S{cashTypeDesc}" ,field = "cash_type_desc")
   private String cashTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "资金流动类型说明错误", sql = "detail_cash_type_desc = $S{detailCashTypeDesc}" ,field = "detail_cash_type_desc")
   private String detailCashTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "交易日错误", sql = "trade_date_desc = $S{tradeDateDesc}" ,field = "trade_date_desc")
   private String tradeDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手方错误", sql = "trade_counter_desc = $S{tradeCounterDesc}" ,field = "trade_counter_desc")
   private String tradeCounterDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手方类型错误", sql = "counter_type_desc = $S{counterTypeDesc}" ,field = "counter_type_desc")
   private String counterTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "单位成交价格", sql = "unit_price_full_desc = $S{unitPriceFullDesc}" ,field = "unit_price_full_desc")
   private String unitPriceFullDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "单位成交价格", sql = "unit_price_net_desc = $S{unitPriceNetDesc}" ,field = "unit_price_net_desc")
   private String unitPriceNetDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "到期收益率%错误", sql = "rate_annual_return_desc = $S{rateAnnualReturnDesc}" ,field = "rate_annual_return_desc")
   private String rateAnnualReturnDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "成交编号/合同号错误", sql = "trans_ident_code_desc = $S{transIdentCodeDesc}" ,field = "trans_ident_code_desc")
   private String transIdentCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "备注错误", sql = "details_desc = $S{detailsDesc}" ,field = "details_desc")
   private String detailsDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
    @GraphQLField(label = "导入日期开始时间")
    private String impStartDate;

    @GraphQLField(label = "导入日期截止时间")
    private String impEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;

    @GraphQLField(kkhtml = "KFieldText", label = "报告日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;

  	public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }
  	public String getProdCodeDesc() {
        return prodCodeDesc;
    }

    public void setProdCodeDesc(String prodCodeDesc) {
        this.prodCodeDesc = prodCodeDesc;
    }
  	public String getTransCodeDesc() {
        return transCodeDesc;
    }

    public void setTransCodeDesc(String transCodeDesc) {
        this.transCodeDesc = transCodeDesc;
    }
  	public String getAssetCodeDesc() {
        return assetCodeDesc;
    }

    public void setAssetCodeDesc(String assetCodeDesc) {
        this.assetCodeDesc = assetCodeDesc;
    }
  	public String getCurDesc() {
        return curDesc;
    }

    public void setCurDesc(String curDesc) {
        this.curDesc = curDesc;
    }
  	public String getAmtDesc() {
        return amtDesc;
    }

    public void setAmtDesc(String amtDesc) {
        this.amtDesc = amtDesc;
    }
  	public String getConvertRmbDesc() {
        return convertRmbDesc;
    }

    public void setConvertRmbDesc(String convertRmbDesc) {
        this.convertRmbDesc = convertRmbDesc;
    }
  	public String getQuantityDesc() {
        return quantityDesc;
    }

    public void setQuantityDesc(String quantityDesc) {
        this.quantityDesc = quantityDesc;
    }
  	public String getMethodAssetMeasureDesc() {
        return methodAssetMeasureDesc;
    }

    public void setMethodAssetMeasureDesc(String methodAssetMeasureDesc) {
        this.methodAssetMeasureDesc = methodAssetMeasureDesc;
    }
  	public String getCashTypeDesc() {
        return cashTypeDesc;
    }

    public void setCashTypeDesc(String cashTypeDesc) {
        this.cashTypeDesc = cashTypeDesc;
    }
  	public String getDetailCashTypeDesc() {
        return detailCashTypeDesc;
    }

    public void setDetailCashTypeDesc(String detailCashTypeDesc) {
        this.detailCashTypeDesc = detailCashTypeDesc;
    }
  	public String getTradeDateDesc() {
        return tradeDateDesc;
    }

    public void setTradeDateDesc(String tradeDateDesc) {
        this.tradeDateDesc = tradeDateDesc;
    }
  	public String getTradeCounterDesc() {
        return tradeCounterDesc;
    }

    public void setTradeCounterDesc(String tradeCounterDesc) {
        this.tradeCounterDesc = tradeCounterDesc;
    }
  	public String getCounterTypeDesc() {
        return counterTypeDesc;
    }

    public void setCounterTypeDesc(String counterTypeDesc) {
        this.counterTypeDesc = counterTypeDesc;
    }
  	public String getUnitPriceFullDesc() {
        return unitPriceFullDesc;
    }

    public void setUnitPriceFullDesc(String unitPriceFullDesc) {
        this.unitPriceFullDesc = unitPriceFullDesc;
    }
  	public String getUnitPriceNetDesc() {
        return unitPriceNetDesc;
    }

    public void setUnitPriceNetDesc(String unitPriceNetDesc) {
        this.unitPriceNetDesc = unitPriceNetDesc;
    }
  	public String getRateAnnualReturnDesc() {
        return rateAnnualReturnDesc;
    }

    public void setRateAnnualReturnDesc(String rateAnnualReturnDesc) {
        this.rateAnnualReturnDesc = rateAnnualReturnDesc;
    }
  	public String getTransIdentCodeDesc() {
        return transIdentCodeDesc;
    }

    public void setTransIdentCodeDesc(String transIdentCodeDesc) {
        this.transIdentCodeDesc = transIdentCodeDesc;
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


}