package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodTransRegistInfoService",table = "app_prod_trans_regist_info")
public class ProdTransRegistInfo {
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "行内交易编码", sql = "trans_code = $S{transCode}" ,field = "trans_code")
   private String transCode;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "行内资产/负债编码", sql = "asset_code like '%$U{assetCode}%'" ,field = "asset_code")
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
   @GraphQLField(kkhtml = "KFieldText", label = "单位成交价格(全价)", sql = "unit_price_full = $S{unitPriceFull}" ,field = "unit_price_full")
   private String unitPriceFull;
   @GraphQLField(kkhtml = "KFieldText", label = "单位成交价格(净价)", sql = "unit_price_net = $S{unitPriceNet}" ,field = "unit_price_net")
   private String unitPriceNet;
    @GraphQLField(kkhtml = "KFieldText", label = "到期收益率%", sql = "rate_annual_return_str = $S{rateAnnualReturnStr}" ,field = "rate_annual_return_str")
    private String rateAnnualReturnStr;
   @GraphQLField(kkhtml = "KFieldText", sql = "rate_annual_return = $S{rateAnnualReturn}" ,field = "rate_annual_return")
   private Double rateAnnualReturn;
   @GraphQLField(kkhtml = "KFieldText", label = "成交编号/合同号", sql = "trans_ident_code = $S{transIdentCode}" ,field = "trans_ident_code")
   private String transIdentCode;
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
    @GraphQLField(label = "关联交易情况")
    private String relatedPartyTrans;
    @GraphQLField(label = "交易审批人身份证号")
    private String transApproveId;
    @GraphQLField(label = "交易审批人身份证号")
    private String initTransApproveId;
    @GraphQLField(label = "交易审批人姓名")
    private String transApproveName;
    @GraphQLField(label = "交易员身份证号")
    private String traderId;
    @GraphQLField(label = "交易员身份证号")
    private String initTraderId;
    @GraphQLField(label = "交易员姓名")
    private String traderName;
    @GraphQLField(kkhtml = "KFieldText", label = "交易时间", sql = "trans_origin_time = $S{transOriginTime}" ,field = "trans_origin_time")
    private String transOriginTime;
    @GraphQLField(kkhtml = "KFieldText", label = "交易发起时间", sql = "trx_tm = $S{trxTm}" ,field = "trxTm")
    private String trxTm;
    @GraphQLField(kkhtml = "KFieldText", sql = "TRADE_DATE >= $S{queryStartDate}" ,field = "queryStartDate")
    private String queryStartDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "TRADE_DATE <= $S{queryEndDate}" ,field = "queryEndDate")
    private String queryEndDate;
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
    @GraphQLField(label = "报表日期")
    private String reportDate;
    @GraphQLField(label = "是否覆盖数据")
    private String isCover;
}