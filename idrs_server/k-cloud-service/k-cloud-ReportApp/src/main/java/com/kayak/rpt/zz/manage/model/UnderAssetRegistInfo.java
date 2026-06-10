package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "underAssetRegistInfoService",table = "app_under_asset_regist_info")
public class UnderAssetRegistInfo {
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "对应资管及委外资产行内资产/负债编码", sql = "asset_manager_code like '%$U{assetManagerCode}%'" ,field = "asset_manager_code")
   private String assetManagerCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资管及委外资产当前总折算人民币金额(元)", sql = "convert_sum_amt = $S{convertSumAmt}" ,field = "convert_sum_amt")
   private String convertSumAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "资管及委外资产当前总数量", sql = "asset_sum_number = $S{assetSumNumber}" ,field = "asset_sum_number")
   private String assetSumNumber;
   @GraphQLField(kkhtml = "KFieldText", label = "资管及委外资产未投资头寸(元)", sql = "non_invested_amt = $S{nonInvestedAmt}" ,field = "non_invested_amt")
   private String nonInvestedAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "底层资产行内资产/负债编码", sql = "under_asset_code like '%$U{underAssetCode}%'" ,field = "under_asset_code")
   private String underAssetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "底层资产持仓数量", sql = "under_asset_sum = $S{underAssetSum}" ,field = "under_asset_sum")
   private String underAssetSum;
   @GraphQLField(kkhtml = "KFieldText", label = "底层资产折算人民币市值(元)", sql = "under_convert_sum_amt = $S{underConvertSumAmt}" ,field = "under_convert_sum_amt")
   private String underConvertSumAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "持仓日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
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
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;
    @GraphQLField(label = "结束时间")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "audit_status = $S{auditStatus}" ,field = "auditStatus")
    private String auditStatus;

    /*数据状态*/
    private  String sysDataStatus;
    /*数据日期*/
    private  String sysDataDate;
    /*数据版本*/
    private  String sysDataVersion;
    /*数据源*/
    private  String sysDataSource;
}