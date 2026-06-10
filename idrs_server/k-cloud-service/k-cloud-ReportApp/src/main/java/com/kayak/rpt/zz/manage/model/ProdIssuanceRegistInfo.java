package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodIssuanceRegistInfoService",table = "app_prod_issuance_regist_info")
public class ProdIssuanceRegistInfo {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "prod_code")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
    private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "理财产品代码", sql = "prod_ident_code like '%$U{prodIdentCode}%'" ,field = "prod_ident_code")
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
    @GraphQLField(kkhtml = "KFieldText", label = "是否为结构化（分级）产品", sql = "structured_prod = $S{structuredProd}" ,field = "structured_prod")
    private String structuredProd;
    @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准说明", sql = "details_per_rate = $S{detailsPerRate}" ,field = "details_per_rate")
    private String detailsPerRate;
    @GraphQLField(kkhtml = "KFieldText", label = "开放模式", sql = "opening_mode = $S{openingMode}" ,field = "opening_mode")
    private String openingMode;
    @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
    private String registerSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
    private String impDate;
    @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
    private String registerDate;
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准上限%", sql = "up_limit_per_rate_str = $S{upLimitPerRateStr}" ,field = "up_limit_per_rate_str")
    private String upLimitPerRateStr;
    @GraphQLField(kkhtml = "KFieldText",  sql = "up_limit_per_rate = $S{upLimitPerRate}" ,field = "up_limit_per_rate")
    private String upLimitPerRate;
    @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准下限%", sql = "low_limit_per_rate_str = $S{lowLimitPerRateStr}" ,field = "low_limit_per_rate_str")
    private String lowLimitPerRateStr;
    @GraphQLField(kkhtml = "KFieldText",  sql = "low_limit_per_rate = $S{lowLimitPerRate}" ,field = "low_limit_per_rate")
    private String lowLimitPerRate;
    @GraphQLField(kkhtml = "KFieldText", label = "规律开放周期", sql = "regular_open_period = $S{regularOpenPeriod}" ,field = "regular_open_period")
    private String regularOpenPeriod;
    @GraphQLField(kkhtml = "KFieldText", label = "定期开放周期(天)", sql = "regular_open_period_day = $S{regularOpenPeriodDay}" ,field = "regular_open_period_day")
    private String regularOpenPeriodDay;
    @GraphQLField(kkhtml = "KFieldText", label = "其他规律开放周期 (天)", sql = "other_open_period = $S{otherOpenPeriod}" ,field = "other_open_period")
    private String otherOpenPeriod;
    @GraphQLField(kkhtml = "KFieldText", label = "无规律开放说明", sql = "disorder_open_period = $S{disorderOpenPeriod}" ,field = "disorder_open_period")
    private String disorderOpenPeriod;
    @GraphQLField(kkhtml = "KFieldText", label = "首次开放周期起始日", sql = "first_open_day = $S{firstOpenDay}" ,field = "first_open_day")
    private String firstOpenDay;
    @GraphQLField(kkhtml = "KFieldText", label = "节假日是否开放", sql = "holiday_open_type = $S{holidayOpenType}" ,field = "holiday_open_type")
    private String holidayOpenType;
    @GraphQLField(kkhtml = "KFieldText", label = "平均开放次数（年化）", sql = "average_open_no_str = $S{averageOpenNoStr}" ,field = "average_open_no_str")
    private String averageOpenNoStr;
    @GraphQLField(kkhtml = "KFieldText", sql = "average_open_no = $S{averageOpenNo}" ,field = "average_open_no")
    private String averageOpenNo;
    @GraphQLField(kkhtml = "KFieldText", label = "开放期业务", sql = "busi_open_period = $S{busiOpenPeriod}" ,field = "busi_open_period")
    private String busiOpenPeriod;
    @GraphQLField(kkhtml = "KFieldText", label = "开放期业务说明", sql = "details_busi_op_period = $S{detailsBusiOpPeriod}" ,field = "details_busi_op_period")
    private String detailsBusiOpPeriod;
    @GraphQLField(kkhtml = "KFieldText", label = "资金托管账号", sql = "custody_acct_no = $S{custodyAcctNo}" ,field = "custody_acct_no")
    private String custodyAcctNo;
    @GraphQLField(kkhtml = "KFieldText", label = "资金托管账户", sql = "custody_acct_name = $S{custodyAcctName}" ,field = "custody_acct_name")
    private String custodyAcctName;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "实际募集金额", sql = "actual_subscribed_amt = $S{actualSubscribedAmt}" ,field = "actual_subscribed_amt")
    private String actualSubscribedAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "分级比例", sql = "clsf_sto_str = $S{clsfStoStr}" ,field = "clsf_sto_str")
    private String clsfStoStr;
    @GraphQLField(kkhtml = "KFieldText", sql = "clsf_sto = $S{clsfSto}" ,field = "clsf_sto")
    private Double clsfSto;
    @GraphQLField(kkhtml = "KFieldText",  label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;
//    @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "report_date >= $S{beginDate}" ,field = "begin_date")
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始")
    private String beginDate;
//    @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "report_date <= $S{queryDate}" ,field = "query_date")
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止")
    private String queryDate;
    @GraphQLField(kkhtml = "KFieldText")
    private String queryStartDate;
    @GraphQLField(kkhtml = "KFieldText")
    private String queryEndDate;
    private String auditStatus;
    /*数据状态*/
    private  String sysDataStatus;
    /*数据日期*/
    private  String sysDataDate;
    /*数据版本*/
    private  String sysDataVersion;
    /*数据源*/
    private  String sysDataSource;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期起始", sql = "create_date >= $S{beginCrtDate}" ,field = "begin_crt_date")
    private String beginCrtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期终止", sql = "create_date <= $S{endCrtDate}" ,field = "end_crt_date")
    private String endCrtDate;

    @GraphQLField(kkhtml = "KFieldText", label = "报送日期起始", sql = "report_date >= $S{reportBeginDate}" ,field = "report_begin_date")
    private String reportBeginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期终止", sql = "report_date <= $S{reportEndDate}" ,field = "report_end_date")
    private String reportEndDate;
}