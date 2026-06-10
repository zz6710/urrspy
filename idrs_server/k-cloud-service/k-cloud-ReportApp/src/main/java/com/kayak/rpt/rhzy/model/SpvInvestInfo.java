package com.kayak.rpt.rhzy.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "spvInvestInfoService",table = "app_spv_invest_info")
@Data
public class SpvInvestInfo {

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
   @GraphQLField(kkhtml = "KFieldText", label = "特定目的载体类型", sql = "spv_type = $S{spvType}" ,field = "spv_type")
   private String spvType;

   @ExcelProperty(value="资管产品统计编码")
   @GraphQLField(kkhtml = "KFieldText", label = "资管产品统计编码", sql = "amps_code = $S{ampsCode}" ,field = "amps_code")
   private String ampsCode;

   @ExcelProperty(value="特定目的载体代码")
   @GraphQLField(kkhtml = "KFieldText", label = "特定目的载体代码", sql = "spv_code = $S{spvCode}" ,field = "spv_code")
   private String spvCode;

   @ExcelProperty(value="发行人代码")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人代码", sql = "issuer_code = $S{issuerCode}" ,field = "issuer_code")
   private String issuerCode;

   @ExcelProperty(value="发行人地区代码")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人地区代码", sql = "issuer_region_code = $S{issuerRegionCode}" ,field = "issuer_region_code")
   private String issuerRegionCode;

   @ExcelProperty(value="运行方式")
   @GraphQLField(kkhtml = "KFieldText", label = "运行方式", sql = "run_mode = $S{runMode}" ,field = "run_mode")
   private String runMode;

   @ExcelProperty(value="认购日期")
   @GraphQLField(kkhtml = "KFieldText", label = "认购日期", sql = "subscrip_date = $S{subscripDate}" ,field = "subscrip_date")
   private String subscripDate;

   @ExcelProperty(value="到期日期")
   @GraphQLField(kkhtml = "KFieldText", label = "到期日期", sql = "expire_date = $S{expireDate}" ,field = "expire_date")
   private String expireDate;

   @ExcelProperty(value="币种")
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cur = $S{cur}" ,field = "cur")
   private String cur;

   @ExcelProperty(value="投资余额")
   @GraphQLField(kkhtml = "KFieldText", label = "投资余额", sql = "invest_balance = $S{investBalance}" ,field = "invest_balance")
   private String investBalance;

   @ExcelProperty(value="投资余额折人民币")
   @GraphQLField(kkhtml = "KFieldText", label = "投资余额折人民币", sql = "invest_balance_rmb = $S{investBalanceRmb}" ,field = "invest_balance_rmb")
   private String investBalanceRmb;

}