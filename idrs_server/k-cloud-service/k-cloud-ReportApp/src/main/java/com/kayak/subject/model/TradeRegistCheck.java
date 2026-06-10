package com.kayak.subject.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "tradeRegistCheckService",table = "app_trade_regist_check")
@Data
public class TradeRegistCheck {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc = $S{prodRegEnc}" ,field = "prod_reg_enc")
   private String prodRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "直接或间接投资 01：直接投资 02：间接投资", sql = "invest_type = $S{investType}" ,field = "invest_type")
   private String investType;
   @GraphQLField(kkhtml = "KFieldText", label = "底层资产/负债编码", sql = "bottom_code = $S{bottomCode}" ,field = "bottom_code")
   private String bottomCode;
   @GraphQLField(kkhtml = "KFieldText", label = "交易登记计算数量", sql = "trade_invamount = $S{tradeInvamount}" ,field = "trade_invamount")
   private String tradeInvamount;
   @GraphQLField(kkhtml = "KFieldText", label = "估值系统持仓", sql = "fa_amount = $S{faAmount}" ,field = "fa_amount")
   private String faAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "偏离数量（与估值系统比）", sql = "pl_amount = $S{plAmount}" ,field = "pl_amount")
   private String plAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "偏离度（%）（与估值系统比）", sql = "pl_rate = $S{plRate}" ,field = "pl_rate")
   private String plRate;
   @GraphQLField(kkhtml = "KFieldText", label = "资产持仓数量", sql = "at_amount = $S{atAmount}" ,field = "at_amount")
   private String atAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "偏离数量（与资产持仓登记比）", sql = "at_pl_amount = $S{atPlAmount}" ,field = "at_pl_amount")
   private String atPlAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "偏离度（%）（与资产持仓登记比）", sql = "at_pl_rate = $S{atPlRate}" ,field = "at_pl_rate")
   private String atPlRate;
   @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
   private String theoryReportEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0：未处理，1：已登记，2：登记失败）", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "状态 (0-失效，1-生效）", sql = "sys_data_status = $S{sysDataStatus}" ,field = "sys_data_status")
   private String sysDataStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "版本(从1.0开始自增)", sql = "sys_data_version = $S{sysDataVersion}" ,field = "sys_data_version")
   private String sysDataVersion;
   @GraphQLField(kkhtml = "KFieldText", label = "数据源(1-系统生成，2-人工导入)", sql = "sys_data_source = $S{sysDataSource}" ,field = "sys_data_source")
   private String sysDataSource;
   @GraphQLField(kkhtml = "KFieldText", label = "报表日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;

}