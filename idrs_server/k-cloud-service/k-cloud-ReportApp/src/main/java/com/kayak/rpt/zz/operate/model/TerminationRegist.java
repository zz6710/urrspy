package com.kayak.rpt.zz.operate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "terminationRegistService",table = "app_termination_regist_remark")
public class TerminationRegist {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品实际终止日期", sql = "actual_prod_ter_date = $S{actualProdTerDate}" ,field = "actual_prod_ter_date")
   private String actualProdTerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益", sql = "interest_payment = $S{interestPayment}" ,field = "interest_payment")
   private String interestPayment;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "银行实际实现收入", sql = "realized_bank_income = $S{realizedBankIncome}" ,field = "realized_bank_income")
   private String realizedBankIncome;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付客户总金额", sql = "payment = $S{payment}" ,field = "payment")
   private String payment;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付总份额", sql = "delivered_vol = $S{deliveredVol}" ,field = "delivered_vol")
   private String deliveredVol;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构托管费", sql = "in_custodian_fee = $S{inCustodianFee}" ,field = "in_custodian_fee")
   private String inCustodianFee;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构管理费", sql = "in_manage_fee = $S{inManageFee}" ,field = "in_manage_fee")
   private String inManageFee;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构销售手续费", sql = "in_sales_commision = $S{inSalesCommision}" ,field = "in_sales_commision")
   private String inSalesCommision;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构其他产品配用", sql = "in_other_prod_fee = $S{inOtherProdFee}" ,field = "in_other_prod_fee")
   private String inOtherProdFee;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构托管费", sql = "other_custodian_fee = $S{otherCustodianFee}" ,field = "other_custodian_fee")
   private String otherCustodianFee;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构管理费", sql = "other_manage_fee = $S{otherManageFee}" ,field = "other_manage_fee")
   private String otherManageFee;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构销售手续费", sql = "other_sales_comm = $S{otherSalesComm}" ,field = "other_sales_comm")
   private String otherSalesComm;
   @GraphQLField(kkhtml = "KFieldText", label = "投资顾问费用", sql = "consult_fee = $S{consultFee}" ,field = "consult_fee")
   private String consultFee;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构其他产品费用", sql = "other_prod_fee = $S{otherProdFee}" ,field = "other_prod_fee")
   private String otherProdFee;
   @GraphQLField(kkhtml = "KFieldText", label = "客户实际年化收益率%", sql = "annual_return_client = $S{annualReturnClient}" ,field = "annual_return_client")
   private Double annualReturnClient;
   @GraphQLField(kkhtml = "KFieldText", label = "产品实际年化收益率%", sql = "annual_return_prod = $S{annualReturnProd}" ,field = "annual_return_prod")
   private Double annualReturnProd;
   @GraphQLField(kkhtml = "KFieldText", label = "操作人员", sql = "summit_user  LIKE '%$U{summitUser}%'" ,field = "summit_user")
   private String summitUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "数据操作类型（D", sql = "op_type = $S{opType}" ,field = "op_type")
   private String opType;
    @GraphQLField(label = "开始时间")
    private String startDate;

    @GraphQLField(label = "结束时间")
    private String endDate;
    @GraphQLField(label = "理财产品实际终止日期开始时间")
    private String TerStartDate;

    @GraphQLField(label = "理财产品实际终止日期结束时间")
    private String TerEndDate;

    @GraphQLField(label = "报送日期")
    private String reportDate;
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
  	public String getActualProdTerDate() {
        return actualProdTerDate;
    }

    public void setActualProdTerDate(String actualProdTerDate) {
        this.actualProdTerDate = actualProdTerDate;
    }
  	public String getInterestPayment() {
        return interestPayment;
    }

    public void setInterestPayment(String interestPayment) {
        this.interestPayment = interestPayment;
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
  	public String getRealizedBankIncome() {
        return realizedBankIncome;
    }

    public void setRealizedBankIncome(String realizedBankIncome) {
        this.realizedBankIncome = realizedBankIncome;
    }
  	public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
  	public String getDeliveredVol() {
        return deliveredVol;
    }

    public void setDeliveredVol(String deliveredVol) {
        this.deliveredVol = deliveredVol;
    }
  	public String getInCustodianFee() {
        return inCustodianFee;
    }

    public void setInCustodianFee(String inCustodianFee) {
        this.inCustodianFee = inCustodianFee;
    }
  	public String getInManageFee() {
        return inManageFee;
    }

    public void setInManageFee(String inManageFee) {
        this.inManageFee = inManageFee;
    }
  	public String getInSalesCommision() {
        return inSalesCommision;
    }

    public void setInSalesCommision(String inSalesCommision) {
        this.inSalesCommision = inSalesCommision;
    }
  	public String getInOtherProdFee() {
        return inOtherProdFee;
    }

    public void setInOtherProdFee(String inOtherProdFee) {
        this.inOtherProdFee = inOtherProdFee;
    }
  	public String getOtherCustodianFee() {
        return otherCustodianFee;
    }

    public void setOtherCustodianFee(String otherCustodianFee) {
        this.otherCustodianFee = otherCustodianFee;
    }
  	public String getOtherManageFee() {
        return otherManageFee;
    }

    public void setOtherManageFee(String otherManageFee) {
        this.otherManageFee = otherManageFee;
    }
  	public String getOtherSalesComm() {
        return otherSalesComm;
    }

    public void setOtherSalesComm(String otherSalesComm) {
        this.otherSalesComm = otherSalesComm;
    }
  	public String getConsultFee() {
        return consultFee;
    }

    public void setConsultFee(String consultFee) {
        this.consultFee = consultFee;
    }
  	public String getOtherProdFee() {
        return otherProdFee;
    }

    public void setOtherProdFee(String otherProdFee) {
        this.otherProdFee = otherProdFee;
    }
  	public Double getAnnualReturnClient() {
        return annualReturnClient;
    }

    public void setAnnualReturnClient(Double annualReturnClient) {
        this.annualReturnClient = annualReturnClient;
    }
  	public Double getAnnualReturnProd() {
        return annualReturnProd;
    }

    public void setAnnualReturnProd(Double annualReturnProd) {
        this.annualReturnProd = annualReturnProd;
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