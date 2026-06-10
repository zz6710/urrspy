package com.kayak.rpt.rhzy.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "interbankDepositAmountInfoService",table = "app_interbank_deposit_amount_info")
public class InterbankDepositAmountInfo {

   @ExcelIgnore
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键id", sql = "id = $S{id}" ,field = "id")
   private String id;

   @ExcelProperty(value="金融机构代码")
   @GraphQLField(kkhtml = "KFieldText", label = "金融机构代码", sql = "org_code = $S{orgCode}" ,field = "org_code")
   private String orgCode;

   @ExcelProperty(value="数据日期")
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;

   @ExcelProperty(value="内部机构号")
   @GraphQLField(kkhtml = "KFieldText", label = "内部机构号", sql = "inner_org_code = $S{innerOrgCode}" ,field = "inner_org_code")
   private String innerOrgCode;

   @ExcelProperty(value="业务类型")
   @GraphQLField(kkhtml = "KFieldText", label = "业务类型", sql = "busi_type = $S{busiType}" ,field = "busi_type")
   private String busiType;

   @ExcelProperty(value="交易对手证件类型")
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手证件类型", sql = "cntr_id_type = $S{cntrIdType}" ,field = "cntr_id_type")
   private String cntrIdType;

   @ExcelProperty(value="交易对手代码")
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手代码", sql = "cntr_code = $S{cntrCode}" ,field = "cntr_code")
   private String cntrCode;

   @ExcelProperty(value="存款账户编码")
   @GraphQLField(kkhtml = "KFieldText", label = "存款账户编码", sql = "deposit_acco_code = $S{depositAccoCode}" ,field = "deposit_acco_code")
   private String depositAccoCode;

   @ExcelProperty(value="存款协议代码")
   @GraphQLField(kkhtml = "KFieldText", label = "存款协议代码", sql = "deposit_protocol_code = $S{depositProtocolCode}" ,field = "deposit_protocol_code")
   private String depositProtocolCode;

   @ExcelProperty(value="协议起始日期")
   @GraphQLField(kkhtml = "KFieldText", label = "协议起始日期", sql = "protocol_start_date = $S{protocolStartDate}" ,field = "protocol_start_date")
   private String protocolStartDate;

   @ExcelProperty(value="协议到期日期")
   @GraphQLField(kkhtml = "KFieldText", label = "协议到期日期", sql = "protocol_end_date = $S{protocolEndDate}" ,field = "protocol_end_date")
   private String protocolEndDate;

   @ExcelProperty(value="币种")
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cur = $S{cur}" ,field = "cur")
   private String cur;

   @ExcelProperty(value="交易金额")
   @GraphQLField(kkhtml = "KFieldText", label = "交易金额", sql = "trade_amount = $S{tradeAmount}" ,field = "trade_amount")
   private String tradeAmount;

   @ExcelProperty(value="交易金额折人民币")
   @GraphQLField(kkhtml = "KFieldText", label = "交易金额折人民币", sql = "trade_amount_rmb = $S{tradeAmountRmb}" ,field = "trade_amount_rmb")
   private String tradeAmountRmb;

   @ExcelProperty(value="交易日期")
   @GraphQLField(kkhtml = "KFieldText", label = "交易日期", sql = "trade_date = $S{tradeDate}" ,field = "trade_date")
   private String tradeDate;

   @ExcelProperty(value="交易流水号")
   @GraphQLField(kkhtml = "KFieldText", label = "交易流水号", sql = "trade_ser_no = $S{tradeSerNo}" ,field = "trade_ser_no")
   private String tradeSerNo;

   @ExcelProperty(value="利率水平")
   @GraphQLField(kkhtml = "KFieldText", label = "利率水平", sql = "rate_level = $S{rateLevel}" ,field = "rate_level")
   private String rateLevel;

   @ExcelProperty(value="交易账户号")
   @GraphQLField(kkhtml = "KFieldText", label = "交易账户号", sql = "trade_acco_no = $S{tradeAccoNo}" ,field = "trade_acco_no")
   private String tradeAccoNo;

   @ExcelProperty(value="交易账户开户行号")
   @GraphQLField(kkhtml = "KFieldText", label = "交易账户开户行号", sql = "trade_acco_bank_no = $S{tradeAccoBankNo}" ,field = "trade_acco_bank_no")
   private String tradeAccoBankNo;

   @ExcelProperty(value="交易对手账户号")
   @GraphQLField(kkhtml = "KFieldText", label = "交易对手账户号", sql = "cntr_acco_no = $S{cntrAccoNo}" ,field = "cntr_acco_no")
   private String cntrAccoNo;

   @ExcelProperty(value="交易方向")
   @GraphQLField(kkhtml = "KFieldText", label = "交易方向", sql = "trade_dire = $S{tradeDire}" ,field = "trade_dire")
   private String tradeDire;

}