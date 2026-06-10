package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "terminationRgInfoErrService",table = "app_termination_regist_info_erdesc")
public class TerminationRgInfoErr {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码错误", sql = "prod_code_desc = $S{prodCodeDesc}" ,field = "prod_code_desc")
   private String prodCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品实际终止日期错误", sql = "actual_prod_ter_date_desc = $S{actualProdTerDateDesc}" ,field = "actual_prod_ter_date_desc")
   private String actualProdTerDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "银行实际实现收入错误", sql = "realized_bank_income_desc = $S{realizedBankIncomeDesc}" ,field = "realized_bank_income_desc")
   private String realizedBankIncomeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益", sql = "interest_payment_desc = $S{interestPaymentDesc}" ,field = "interest_payment_desc")
   private String interestPaymentDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付客户总金额错误", sql = "payment_desc = $S{paymentDesc}" ,field = "payment_desc")
   private String paymentDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付总份额错误", sql = "delivered_vol_desc = $S{deliveredVolDesc}" ,field = "delivered_vol_desc")
   private String deliveredVolDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构托管费", sql = "in_custodian_fee_desc = $S{inCustodianFeeDesc}" ,field = "in_custodian_fee_desc")
   private String inCustodianFeeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构管理费", sql = "in_manage_fee_desc = $S{inManageFeeDesc}" ,field = "in_manage_fee_desc")
   private String inManageFeeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构销售手续费", sql = "in_sales_commision_desc = $S{inSalesCommisionDesc}" ,field = "in_sales_commision_desc")
   private String inSalesCommisionDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构其他产品配用", sql = "in_other_prod_fee_desc = $S{inOtherProdFeeDesc}" ,field = "in_other_prod_fee_desc")
   private String inOtherProdFeeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构托管费", sql = "other_custodian_fee_desc = $S{otherCustodianFeeDesc}" ,field = "other_custodian_fee_desc")
   private String otherCustodianFeeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构管理费", sql = "other_manage_fee_desc = $S{otherManageFeeDesc}" ,field = "other_manage_fee_desc")
   private String otherManageFeeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构销售手续费", sql = "other_sales_comm_desc = $S{otherSalesCommDesc}" ,field = "other_sales_comm_desc")
   private String otherSalesCommDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "投资顾问费用", sql = "consult_fee_desc = $S{consultFeeDesc}" ,field = "consult_fee_desc")
   private String consultFeeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构其他产品费用", sql = "other_prod_fee_desc = $S{otherProdFeeDesc}" ,field = "other_prod_fee_desc")
   private String otherProdFeeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "客户实际年化收益率%错误", sql = "annual_return_client_desc = $S{annualReturnClientDesc}" ,field = "annual_return_client_desc")
   private String annualReturnClientDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品实际年化收益率%错误", sql = "annual_return_prod_desc = $S{annualReturnProdDesc}" ,field = "annual_return_prod_desc")
   private String annualReturnProdDesc;
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
  	public String getActualProdTerDateDesc() {
        return actualProdTerDateDesc;
    }

    public void setActualProdTerDateDesc(String actualProdTerDateDesc) {
        this.actualProdTerDateDesc = actualProdTerDateDesc;
    }
  	public String getRealizedBankIncomeDesc() {
        return realizedBankIncomeDesc;
    }

    public void setRealizedBankIncomeDesc(String realizedBankIncomeDesc) {
        this.realizedBankIncomeDesc = realizedBankIncomeDesc;
    }
  	public String getInterestPaymentDesc() {
        return interestPaymentDesc;
    }

    public void setInterestPaymentDesc(String interestPaymentDesc) {
        this.interestPaymentDesc = interestPaymentDesc;
    }
  	public String getPaymentDesc() {
        return paymentDesc;
    }

    public void setPaymentDesc(String paymentDesc) {
        this.paymentDesc = paymentDesc;
    }
  	public String getDeliveredVolDesc() {
        return deliveredVolDesc;
    }

    public void setDeliveredVolDesc(String deliveredVolDesc) {
        this.deliveredVolDesc = deliveredVolDesc;
    }
  	public String getInCustodianFeeDesc() {
        return inCustodianFeeDesc;
    }

    public void setInCustodianFeeDesc(String inCustodianFeeDesc) {
        this.inCustodianFeeDesc = inCustodianFeeDesc;
    }
  	public String getInManageFeeDesc() {
        return inManageFeeDesc;
    }

    public void setInManageFeeDesc(String inManageFeeDesc) {
        this.inManageFeeDesc = inManageFeeDesc;
    }
  	public String getInSalesCommisionDesc() {
        return inSalesCommisionDesc;
    }

    public void setInSalesCommisionDesc(String inSalesCommisionDesc) {
        this.inSalesCommisionDesc = inSalesCommisionDesc;
    }
  	public String getInOtherProdFeeDesc() {
        return inOtherProdFeeDesc;
    }

    public void setInOtherProdFeeDesc(String inOtherProdFeeDesc) {
        this.inOtherProdFeeDesc = inOtherProdFeeDesc;
    }
  	public String getOtherCustodianFeeDesc() {
        return otherCustodianFeeDesc;
    }

    public void setOtherCustodianFeeDesc(String otherCustodianFeeDesc) {
        this.otherCustodianFeeDesc = otherCustodianFeeDesc;
    }
  	public String getOtherManageFeeDesc() {
        return otherManageFeeDesc;
    }

    public void setOtherManageFeeDesc(String otherManageFeeDesc) {
        this.otherManageFeeDesc = otherManageFeeDesc;
    }
  	public String getOtherSalesCommDesc() {
        return otherSalesCommDesc;
    }

    public void setOtherSalesCommDesc(String otherSalesCommDesc) {
        this.otherSalesCommDesc = otherSalesCommDesc;
    }
  	public String getConsultFeeDesc() {
        return consultFeeDesc;
    }

    public void setConsultFeeDesc(String consultFeeDesc) {
        this.consultFeeDesc = consultFeeDesc;
    }
  	public String getOtherProdFeeDesc() {
        return otherProdFeeDesc;
    }

    public void setOtherProdFeeDesc(String otherProdFeeDesc) {
        this.otherProdFeeDesc = otherProdFeeDesc;
    }
  	public String getAnnualReturnClientDesc() {
        return annualReturnClientDesc;
    }

    public void setAnnualReturnClientDesc(String annualReturnClientDesc) {
        this.annualReturnClientDesc = annualReturnClientDesc;
    }
  	public String getAnnualReturnProdDesc() {
        return annualReturnProdDesc;
    }

    public void setAnnualReturnProdDesc(String annualReturnProdDesc) {
        this.annualReturnProdDesc = annualReturnProdDesc;
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