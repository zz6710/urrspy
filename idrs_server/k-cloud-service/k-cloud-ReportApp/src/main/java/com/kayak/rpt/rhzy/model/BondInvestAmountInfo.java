package com.kayak.rpt.rhzy.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "bondInvestAmountInfoService",table = "app_bond_invest_amount_info")
@Data
public class BondInvestAmountInfo {

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

   @ExcelProperty(value="债券代码")
   @GraphQLField(kkhtml = "KFieldText", label = "债券代码", sql = "bond_code = $S{bondCode}" ,field = "bond_code")
   private String bondCode;

   @ExcelProperty(value="债券总托管机构")
   @GraphQLField(kkhtml = "KFieldText", label = "债券总托管机构", sql = "bond_trustsp_org = $S{bondTrustspOrg}" ,field = "bond_trustsp_org")
   private String bondTrustspOrg;

   @ExcelProperty(value="债券品种")
   @GraphQLField(kkhtml = "KFieldText", label = "债券品种", sql = "bond_cate = $S{bondCate}" ,field = "bond_cate")
   private String bondCate;

   @ExcelProperty(value="债券信用级别")
   @GraphQLField(kkhtml = "KFieldText", label = "债券信用级别", sql = "bond_credit_grade = $S{bondCreditGrade}" ,field = "bond_credit_grade")
   private String bondCreditGrade;

   @ExcelProperty(value="币种")
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cur = $S{cur}" ,field = "cur")
   private String cur;

   @ExcelProperty(value="债权债务登记日")
   @GraphQLField(kkhtml = "KFieldText", label = "债权债务登记日", sql = "debt_reg_date = $S{debtRegDate}" ,field = "debt_reg_date")
   private String debtRegDate;

   @ExcelProperty(value="起息日")
   @GraphQLField(kkhtml = "KFieldText", label = "起息日", sql = "value_date = $S{valueDate}" ,field = "value_date")
   private String valueDate;

   @ExcelProperty(value="兑付日期")
   @GraphQLField(kkhtml = "KFieldText", label = "兑付日期", sql = "redem_date = $S{redemDate}" ,field = "redem_date")
   private String redemDate;

   @ExcelProperty(value="票面利率")
   @GraphQLField(kkhtml = "KFieldText", label = "票面利率", sql = "coupon_rate = $S{couponRate}" ,field = "coupon_rate")
   private String couponRate;

   @ExcelProperty(value="发行人证件代码")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人证件代码", sql = "issuer_id_code = $S{issuerIdCode}" ,field = "issuer_id_code")
   private String issuerIdCode;

   @ExcelProperty(value="发行人地区代码")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人地区代码", sql = "issuer_region_code = $S{issuerRegionCode}" ,field = "issuer_region_code")
   private String issuerRegionCode;

   @ExcelProperty(value="发行人行业")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人行业", sql = "issuer_industry = $S{issuerIndustry}" ,field = "issuer_industry")
   private String issuerIndustry;

   @ExcelProperty(value="发行人企业规模")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人企业规模", sql = "issuer_entp_scale = $S{issuerEntpScale}" ,field = "issuer_entp_scale")
   private String issuerEntpScale;

   @ExcelProperty(value="发行人经济成分")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人经济成分", sql = "issuer_eco_sector = $S{issuerEcoSector}" ,field = "issuer_eco_sector")
   private String issuerEcoSector;

   @ExcelProperty(value="发行人国民经济部门")
   @GraphQLField(kkhtml = "KFieldText", label = "发行人国民经济部门", sql = "issuer_eco_dept = $S{issuerEcoDept}" ,field = "issuer_eco_dept")
   private String issuerEcoDept;

   @ExcelProperty(value="交易日期")
   @GraphQLField(kkhtml = "KFieldText", label = "交易日期", sql = "trade_date = $S{tradeDate}" ,field = "trade_date")
   private String tradeDate;

   @ExcelProperty(value="交易流水号")
   @GraphQLField(kkhtml = "KFieldText", label = "交易流水号", sql = "trade_ser_no = $S{tradeSerNo}" ,field = "trade_ser_no")
   private String tradeSerNo;

   @ExcelProperty(value="成交金额")
   @GraphQLField(kkhtml = "KFieldText", label = "成交金额", sql = "trade_amount = $S{tradeAmount}" ,field = "trade_amount")
   private String tradeAmount;

   @ExcelProperty(value="成交金额折人民币")
   @GraphQLField(kkhtml = "KFieldText", label = "成交金额折人民币", sql = "trade_amount_rmb = $S{tradeAmountRmb}" ,field = "trade_amount_rmb")
   private String tradeAmountRmb;

   @ExcelProperty(value="买入/卖出标志")
   @GraphQLField(kkhtml = "KFieldText", label = "买入/卖出标志", sql = "trade_flag = $S{tradeFlag}" ,field = "trade_flag")
   private String tradeFlag;

}