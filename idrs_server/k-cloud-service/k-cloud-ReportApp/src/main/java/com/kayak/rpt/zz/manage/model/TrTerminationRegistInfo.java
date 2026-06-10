package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "trTerminationRegistInfoService",table = "app_termination_regist_info")
public class TrTerminationRegistInfo {
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品实际终止日期", sql = "actual_prod_ter_date = $S{actualProdTerDate}" ,field = "actual_prod_ter_date")
   private String actualProdTerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益（元）", sql = "interest_payment = $S{interestPayment}" ,field = "interest_payment")
   private String interestPayment;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "银行实际实现收入（元）", sql = "realized_bank_income = $S{realizedBankIncome}" ,field = "realized_bank_income")
   private String realizedBankIncome;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付客户总金额（元）", sql = "payment = $S{payment}" ,field = "payment")
   private String payment;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付总份额", sql = "delivered_vol = $S{deliveredVol}" ,field = "delivered_vol")
   private String deliveredVol;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构托管费（元）", sql = "in_custodian_fee = $S{inCustodianFee}" ,field = "in_custodian_fee")
   private String inCustodianFee;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构管理费（元）", sql = "in_manage_fee = $S{inManageFee}" ,field = "in_manage_fee")
   private String inManageFee;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构销售手续费（元）", sql = "in_sales_commision = $S{inSalesCommision}" ,field = "in_sales_commision")
   private String inSalesCommision;
   @GraphQLField(kkhtml = "KFieldText", label = "本机构其他产品费用（元）", sql = "in_other_prod_fee = $S{inOtherProdFee}" ,field = "in_other_prod_fee")
   private String inOtherProdFee;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构托管费（元）", sql = "other_custodian_fee = $S{otherCustodianFee}" ,field = "other_custodian_fee")
   private String otherCustodianFee;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构管理费（元）", sql = "other_manage_fee = $S{otherManageFee}" ,field = "other_manage_fee")
   private String otherManageFee;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构销售手续费（元）", sql = "other_sales_comm = $S{otherSalesComm}" ,field = "other_sales_comm")
   private String otherSalesComm;
   @GraphQLField(kkhtml = "KFieldText", label = "投资顾问费用（元）", sql = "consult_fee = $S{consultFee}" ,field = "consult_fee")
   private String consultFee;
   @GraphQLField(kkhtml = "KFieldText", label = "其他机构其他产品费用（元）", sql = "other_prod_fee = $S{otherProdFee}" ,field = "other_prod_fee")
   private String otherProdFee;
   @GraphQLField(kkhtml = "KFieldText", label = "客户实际年化收益率%", sql = "annual_return_client_str = $S{annualReturnClientStr}" ,field = "annual_return_client_str")
   private String annualReturnClientStr;
   @GraphQLField(kkhtml = "KFieldText", sql = "annual_return_client = $S{annualReturnClient}" ,field = "annual_return_client")
   private Double annualReturnClient;
   @GraphQLField(kkhtml = "KFieldText", label = "产品实际年化收益率%", sql = "annual_return_prod_str = $S{annualReturnProdStr}" ,field = "annual_return_prod_str")
   private String annualReturnProdStr;
   @GraphQLField(kkhtml = "KFieldText", sql = "annual_return_prod = $S{annualReturnProd}" ,field = "annual_return_prod")
   private Double annualReturnProd;
    @GraphQLField(kkhtml = "KFieldText", label = "是否审核通过", sql = "is_error = $S{isError}" ,field = "isError")
    private String isError;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;
    @GraphQLField(kkhtml = "KFieldText")
    private String startDate;
    @GraphQLField(kkhtml = "KFieldText")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText")
    private String actualStartDate;
    @GraphQLField(kkhtml = "KFieldText")
    private String actualEndDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date >= $S{queryStartDate}" ,field = "queryStartDate")
    private String queryStartDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date <= $S{queryEndDate}" ,field = "queryEndDate")
    private String queryEndDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "audit_status = $S{auditStatus}" ,field = "auditStatus")
    private String auditStatus;
    @GraphQLField(kkhtml = "KFieldText",  label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "report_date >= $S{beginDate}" ,field = "begin_date")
    private String beginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "report_date <= $S{queryDate}" ,field = "query_date")
    private String queryDate;

    /*数据状态*/
    @GraphQLField(kkhtml = "KFieldText", label = "数据状态", sql = "sys_data_status <= $S{sysDataStatus}" ,field = "sys_data_status")
    private  String sysDataStatus;
    /*数据日期*/
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "sys_data_date <= $S{sysDataDate}" ,field = "sys_data_date")
    private  String sysDataDate;
    /*数据版本*/
    private  String sysDataVersion;
    /*数据源*/
    private  String sysDataSource;
}
