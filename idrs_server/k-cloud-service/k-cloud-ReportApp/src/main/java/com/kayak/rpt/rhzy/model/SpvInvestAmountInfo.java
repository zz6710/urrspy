package com.kayak.rpt.rhzy.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "spvInvestAmountInfoService",table = "app_spv_invest_amount_info")
@Data
public class SpvInvestAmountInfo {

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

   @ExcelProperty(value="特定目的载体类型")
   @GraphQLField(kkhtml = "KFieldText", label = "特定目的载体类型", sql = "specific_aim_type = $S{specificAimType}" ,field = "specific_aim_type")
   private String specificAimType;

   @ExcelProperty(value="资管产品统计编码")
   @GraphQLField(kkhtml = "KFieldText", label = "资管产品统计编码", sql = "product_code = $S{productCode}" ,field = "product_code")
   private String productCode;

   @ExcelProperty(value="特定目的载体代码")
   @GraphQLField(kkhtml = "KFieldText", label = "特定目的载体代码", sql = "specific_aim_code = $S{specificAimCode}" ,field = "specific_aim_code")
   private String specificAimCode;

   @ExcelProperty(value="发行人代码")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人代码", sql = "issuer_code = $S{issuerCode}" ,field = "issuer_code")
   private String issuerCode;

   @ExcelProperty(value="发行人地区代码")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人地区代码", sql = "issuer_arear_code = $S{issuerArearCode}" ,field = "issuer_arear_code")
   private String issuerArearCode;

   @ExcelProperty(value="运行方式")
   @GraphQLField(kkhtml = "KFieldText", label = "运行方式", sql = "rnn_code = $S{rnnCode}" ,field = "rnn_code")
   private String rnnCode;

   @ExcelProperty(value="认购日期")
   @GraphQLField(kkhtml = "KFieldText", label = "认购日期", sql = "sub_date = $S{subDate}" ,field = "sub_date")
   private String subDate;

   @ExcelProperty(value="到期日期")
   @GraphQLField(kkhtml = "KFieldText", label = "到期日期", sql = "end_date = $S{endDate}" ,field = "end_date")
   private String endDate;

   @ExcelProperty(value="交易日期")
   @GraphQLField(kkhtml = "KFieldText", label = "交易日期", sql = "trade_date = $S{tradeDate}" ,field = "trade_date")
   private String tradeDate;

   @ExcelProperty(value="币种")
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cur = $S{cur}" ,field = "cur")
   private String cur;

   @ExcelProperty(value="交易金额")
   @GraphQLField(kkhtml = "KFieldText", label = "交易金额", sql = "trade_amount = $S{tradeAmount}" ,field = "trade_amount")
   private String tradeAmount;

   @ExcelProperty(value="交易金额折人民币")
   @GraphQLField(kkhtml = "KFieldText", label = "交易金额折人民币", sql = "trade_amount_rmb = $S{tradeAmountRmb}" ,field = "trade_amount_rmb")
   private String tradeAmountRmb;

   @ExcelProperty(value="交易方向")
   @GraphQLField(kkhtml = "KFieldText", label = "交易方向", sql = "trade_dire = $S{tradeDire}" ,field = "trade_dire")
   private String tradeDire;

}