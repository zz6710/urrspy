package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "initialSubRegistInfoService",table = "app_initial_sub_regist_info")
public class InitialSubRegistInfo {
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "个人投资者总数", sql = "number_indiv_invest = $S{numberIndivInvest}" ,field = "number_indiv_invest")
   private String numberIndivInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "法人投资者总数", sql = "number_corpor_invest = $S{numberCorporInvest}" ,field = "number_corpor_invest")
   private String numberCorporInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "非法人投资者总数", sql = "number_ucor_invest = $S{numberUcorInvest}" ,field = "number_ucor_invest")
   private String numberUcorInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有其他机构代销", sql = "other_distribut_agents = $S{otherDistributAgents}" ,field = "other_distribut_agents")
   private String otherDistributAgents;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "实际募集金额(元)", sql = "actual_subscribed_amt = $S{actualSubscribedAmt}" ,field = "actual_subscribed_amt")
   private String actualSubscribedAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "募集总份额", sql = "subscribed_vol = $S{subscribedVol}" ,field = "subscribed_vol")
   private String subscribedVol;
   @GraphQLField(kkhtml = "KFieldText", label = "代销总金额", sql = "amt_other_db_agents = $S{amtOtherDbAgents}" ,field = "amt_other_db_agents")
   private String amtOtherDbAgents;
    @GraphQLField(kkhtml = "KFieldText",label = "资金托管账号")
    private String fndTrstActNbr;
    @GraphQLField(kkhtml = "KFieldText")
    private String navDt;
    @GraphQLField(kkhtml = "KFieldText", label = "产品销售区域及募集金额")
    private String zonClcAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "认购币种" , sql = "prod_ccy = $S{prodCcy}" ,field = "prod_ccy")
    private String prodCcy;
    @GraphQLField(kkhtml = "KFieldText", label = "成立日期")
    private String foundDt;
    @GraphQLField(kkhtml = "KFieldText" ,label = "资金托管账户")
    private String fndTrstAct;
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
    @GraphQLField(kkhtml = "KFieldText", label = "区域及募集金额")
    private String zonClcInfo;
    @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date >= $S{queryStartDate}" ,field = "queryStartDate")
    private String queryStartDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date <= $S{queryEndDate}" ,field = "queryEndDate")
    private String queryEndDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "audit_status = $S{auditStatus}" ,field = "audit_status")
    private String auditStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "report_date >= $S{beginDate}" ,field = "begin_date")
    private String beginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "report_date <= $S{queryDate}" ,field = "query_date")
    private String queryDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期起始", sql = "create_date >= $S{beginCrtDate}" ,field = "begin_crt_date")
    private String beginCrtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期终止", sql = "create_date <= $S{endCrtDate}" ,field = "end_crt_date")
    private String endCrtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期起始", sql = "report_date >= $S{reportBeginDate}" ,field = "report_begin_date")
    private String reportBeginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期终止", sql = "report_date <= $S{reportEndDate}" ,field = "report_end_date")
    private String reportEndDate;
    /*数据状态*/
    private  String sysDataStatus;
    /*数据日期*/
    private  String sysDataDate;
    /*数据版本*/
    private  String sysDataVersion;
    /*数据源*/
    private  String sysDataSource;

}