package com.kayak.rpt.rhzy.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "interbankDepositInfoService",table = "app_interbank_deposit_info")
public class InterbankDepositInfo {
   @ExcelIgnore
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "主键id", sql = "id = $S{id}" ,field = "id")
   private String id;

   @ExcelProperty(value="数据日期")
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
   private String reportDate;

   @ExcelProperty(value="金融机构代码")
   @GraphQLField(kkhtml = "KFieldText", label = "金融机构代码", sql = "org_code = $S{orgCode}" ,field = "org_code")
   private String orgCode;

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

   @ExcelProperty(value="存款余额")
   @GraphQLField(kkhtml = "KFieldText", label = "存款余额", sql = "deposit_balance = $S{depositBalance}" ,field = "deposit_balance")
   private String depositBalance;

   @ExcelProperty(value="存款余额折人民币")
   @GraphQLField(kkhtml = "KFieldText", label = "存款余额折人民币", sql = "deposit_balance_rmb = $S{depositBalanceRmb}" ,field = "deposit_balance_rmb")
   private String depositBalanceRmb;

   @ExcelProperty(value="利率水平")
   @GraphQLField(kkhtml = "KFieldText", label = "利率水平", sql = "rate_level = $S{rateLevel}" ,field = "rate_level")
   private String rateLevel;

   @ExcelProperty(value="缴存准备金方式")
   @GraphQLField(kkhtml = "KFieldText", label = "缴存准备金方式", sql = "deposit_type = $S{depositType}" ,field = "deposit_type")
   private String depositType;

}