package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "subseqSubscrRegistInfoService",table = "app_subseq_subscr_regist_info")
public class SubseqSubscrRegistInfo {
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
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
   @GraphQLField(kkhtml = "KFieldText", label = "实现收益率%", sql = "realized_annual_return_str = $S{realizedAnnualReturnStr}" ,field = "realized_annual_return_str")
   private String realizedAnnualReturnStr;
   @GraphQLField(kkhtml = "KFieldText", label = "收益率", sql = "realized_annual_return = $S{realizedAnnualReturn}" ,field = "realized_annual_return")
   private Double realizedAnnualReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "最新预期收益率%", sql = "expected_annual_return_str = $S{expectedAnnualReturnStr}" ,field = "expected_annual_return_str")
   private String expectedAnnualReturnStr;
   @GraphQLField(kkhtml = "KFieldText", label = "预期收益率", sql = "expected_annual_return = $S{expectedAnnualReturn}" ,field = "expected_annual_return")
   private Double expectedAnnualReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "银行实现收益(元)", sql = "inconme_bank = $S{inconmeBank}" ,field = "inconme_bank")
   private String inconmeBank;
   @GraphQLField(kkhtml = "KFieldText", label = "业务起始日",field = "business_start_date")
   private String businessStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "业务结束日",field = "business_end_date")
   private String businessEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "累计申购份额", sql = "subscribed_latest_vol = $S{subscribedLatestVol}" ,field = "subscribed_latest_vol")
   private String subscribedLatestVol;
   @GraphQLField(kkhtml = "KFieldText", label = "累计赎回份额", sql = "redeemed_latest_vol = $S{redeemedLatestVol}" ,field = "redeemed_latest_vol")
   private String redeemedLatestVol;
   @GraphQLField(kkhtml = "KFieldText", label = "每万份份额分红", sql = "units_bonus = $S{unitsBonus}" ,field = "units_bonus")
   private String unitsBonus;
   @GraphQLField(kkhtml = "KFieldText", label = "每万份现金分红", sql = "cash_bonus = $S{cashBonus}" ,field = "cash_bonus")
   private String cashBonus;
   @GraphQLField(kkhtml = "KFieldText", label = "产品余额(元)", sql = "prod_amt = $S{prodAmt}" ,field = "prod_amt")
   private String prodAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "产品份额", sql = "prod_vol = $S{prodVol}" ,field = "prod_vol")
   private String prodVol;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "流水序号",field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币初始净值", sql = "convert_initial_nav = $S{convertInitialNav}" ,field = "convert_initial_nav")
   private String convertInitialNav;
//    @GraphQLField(label = "开始时间1", sql = "DATE(business_start_date) >= DATE($S{startDate1})" ,field = "startDate1")
    @GraphQLField(kkhtml = "KFieldText", label = "开始时间1")
    private String startDate1;

//    @GraphQLField(label = "结束时间1" ,sql = "DATE(business_start_date) <= DATE($S{endDate1})" ,field = "endDate1")
    @GraphQLField(kkhtml = "KFieldText", label = "结束时间1")
    private String endDate1;
//    @GraphQLField(label = "开始时间2",sql = "DATE(business_end_date) >= DATE($S{startDate2})" ,field = "startDate2")
    @GraphQLField(kkhtml = "KFieldText", label = "开始时间2")
    private String startDate2;

//    @GraphQLField(label = "结束时间2",sql = "DATE(business_end_date) <= DATE($S{endDate2})" ,field = "endDate2")
    @GraphQLField(kkhtml = "KFieldText", label = "结束时间2")
    private String endDate2;
   @GraphQLField(kkhtml = "KFieldText", label = "币种和申购兑付信息", sql = "ccy_and_pch_rdm = $S{ccyAndPchRdm}" ,field = "ccy_and_pch_rdm")
    private String ccyAndPchRdm;
   @GraphQLField(kkhtml = "KFieldText", label = "净值日期" , sql = "nav_dt = $S{navDt}" ,field = "nav_dt")
   private String navDt;
   @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
   private String theoryReportEndDate;
//   @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date >= $S{queryStartDate}" ,field = "queryStartDate")
   @GraphQLField(kkhtml = "KFieldText")
   private String queryStartDate;
//   @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date <= $S{queryEndDate}" ,field = "queryEndDate")
   @GraphQLField(kkhtml = "KFieldText")
   private String queryEndDate;
   @GraphQLField(kkhtml = "KFieldText", sql = "audit_status = $S{auditStatus}" ,field = "audit_status")
   private String auditStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
//   @GraphQLField(label = "数据开始日期", sql = "DATE(theory_report_start_date) >= DATE($S{reportDateStart})" ,field = "reportDateStart")
   @GraphQLField(kkhtml = "KFieldText", label = "数据开始日期")
   private String reportDateStart;
//   @GraphQLField(label = "数据结束日期", sql = "DATE(theory_report_start_date) <= DATE($S{reportDateEnd})" ,field = "report_date_end")
   @GraphQLField(kkhtml = "KFieldText", label = "数据结束日期")
   private String reportDateEnd;
   /*数据状态*/
   private  String sysDataStatus;
   /*数据日期*/
   private  String sysDataDate;
   /*数据版本*/
   private  String sysDataVersion;
   /*数据源*/
   private  String sysDataSource;
}