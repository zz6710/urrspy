package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//除资产收益权外其他债权明细信息
@Data
@GraphQLModel(fetcher = "ZG12Service",table = "app_pbc_report_zg12")
public class ZG12 {

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
    private String id;

    @ExcelProperty(value = "产品代码_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd like '%$U{prodCd}%'" ,field = "prod_cd")
    private String prodCd;

    @ExcelProperty(value = "数据日期")
    @GraphQLField(kkhtml = "KFieldText", label = "实际报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
    private String theoryReportStartDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;

    @ExcelProperty(value = "借款人类型")
    @GraphQLField(kkhtml = "KFieldText", label = "借款人类型", sql = "borrower_typ = $S{borrowerTyp}" ,field = "borrower_typ")
    private String borrowerTyp;

    @ExcelProperty(value = "地区代码_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "地区代码", sql = "zone = $S{zone}" ,field = "zone")
    private String zone;

    @ExcelProperty(value = "借款人代码")
    @GraphQLField(kkhtml = "KFieldText", label = "借款人代码", sql = "borrower_cd = $S{borrowerCd}" ,field = "borrower_cd")
    private String borrowerCd;

    @ExcelProperty(value = "行业信息")
    @GraphQLField(kkhtml = "KFieldText", label = "行业信息", sql = "industry_msg = $S{industryMsg}" ,field = "industry_msg")
    private String industryMsg;

    @ExcelProperty(value = "企业出资人经济成分")
    @GraphQLField(kkhtml = "KFieldText", label = "企业出资人经济成分", sql = "enter_sponsor_eco_sector = $S{enterSponsorEcoSector}" ,field = "enter_sponsor_eco_sector")
    private String enterSponsorEcoSector;

    @ExcelProperty(value = "企业规模_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "企业规模", sql = "enter_scale = $S{enterScale}" ,field = "enter_scale")
    private String enterScale;

    @ExcelProperty(value = "除资产收益权外其他债权内部编码")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款借据编码", sql = "borrower_asset_cd = $S{borrowerAssetCd}" ,field = "borrower_asset_cd")
    private String borrowerAssetCd;

    @ExcelProperty(value = "除资产收益权外其他债权实际投向")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款实际投向", sql = "borrower_real_trans = $S{borrowerRealTrans}" ,field = "borrower_real_trans")
    private String borrowerRealTrans;

    @ExcelProperty(value = "除资产收益权外其他债权起始日期")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款发放日期", sql = "borrower_issue_dt = $S{borrowerIssueDt}" ,field = "borrower_issue_dt")
    private String borrowerIssueDt;

    @ExcelProperty(value = "除资产收益权外其他债权预计到期日期")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款到期日期", sql = "borrower_end_dt = $S{borrowerEndDt}" ,field = "borrower_end_dt")
    private String borrowerEndDt;

    @ExcelProperty(value = "除资产收益权外其他债权展期到期日期")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款展期到期日期", sql = "borrower_extension_dt = $S{borrowerExtensionDt}" ,field = "borrower_extension_dt")
    private String borrowerExtensionDt;

    @ExcelProperty(value = "利率是否固定_资管12表")
    @GraphQLField(kkhtml = "KFieldText", label = "利率是否固定", sql = "is_fixed_rate = $S{isFixedRate}" ,field = "is_fixed_rate")
    private String isFixedRate;

    @ExcelProperty(value = "利率水平_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "利率水平", sql = "rate_level = $S{rateLevel}" ,field = "rate_level")
    private String rateLevel;

    @ExcelProperty(value = "担保方式_资管12表")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款担保方式", sql = "guarantee_mode = $S{guaranteeMode}" ,field = "guarantee_mode")
    private String guaranteeMode;

    @ExcelProperty(value = "原始合同币种")
    @GraphQLField(kkhtml = "KFieldText", label = "原始合同币种", sql = "ori_contract_ccy = $S{oriContractCcy}" ,field = "ori_contract_ccy")
    private String oriContractCcy;

    @ExcelProperty(value = "原始合同金额")
    @GraphQLField(kkhtml = "KFieldText", label = "原始合同金额", sql = "ori_contract_amt = $S{oriContractAmt}" ,field = "ori_contract_amt")
    private String oriContractAmt;

    @ExcelProperty(value = "原始合同金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "原始合同金额折人民币", sql = "ori_contract_amt_cny = $S{oriContractAmtCny}" ,field = "ori_contract_amt_cny")
    private String oriContractAmtCny;

    @ExcelProperty(value = "除资产收益权外其他债权余额币种")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款余额币种", sql = "borrower_balance_ccy = $S{borrowerBalanceCcy}" ,field = "borrower_balance_ccy")
    private String borrowerBalanceCcy;

    @ExcelProperty(value = "除资产收益权外其他债权余额")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款余额", sql = "borrower_balance = $S{borrowerBalance}" ,field = "borrower_balance")
    private String borrowerBalance;

    @ExcelProperty(value = "除资产收益权外其他债权余额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款余额折人民币", sql = "borrower_balance_cny = $S{borrowerBalanceCny}" ,field = "borrower_balance_cny")
    private String borrowerBalanceCny;

    @ExcelProperty(value = "债权类型")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款余额折人民币", sql = "type_of_debt = $S{typeOfDebt}" ,field = "type_of_debt")
    private String typeOfDebt;

    @ExcelProperty(value = "登记交易场所")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款余额折人民币", sql = "register_trading_place = $S{registerTradingPlace}" ,field = "register_trading_place")
    private String registerTradingPlace;

    @ExcelProperty(value = "登记交易场所代码_资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款余额折人民币", sql = "register_trading_code = $S{registerTradingCode}" ,field = "register_trading_code")
    private String registerTradingCode;

    @GraphQLField(kkhtml = "KFieldText" ,field = "zone_text")
    public String zoneText;

    @ExcelProperty(value = "科技相关产业标识")
    @GraphQLField(kkhtml = "KFieldText", label = "科技相关产业标识", sql = "tech_flag = $S{techFlag}" ,field = "tech_flag")
    private String techFlag;

    @ExcelProperty(value = "绿色领域标识")
    @GraphQLField(kkhtml = "KFieldText", label = "绿色领域标识", sql = "green_flag = $S{greenFlag}" ,field = "green_flag")
    private String greenFlag;

    @ExcelProperty(value = "普惠领域标识")
    @GraphQLField(kkhtml = "KFieldText", label = "普惠领域标识", sql = "spec_flag = $S{specFlag}" ,field = "spec_flag")
    private String specFlag;

    @ExcelProperty(value = "养老产业标识")
    @GraphQLField(kkhtml = "KFieldText", label = "养老产业标识", sql = "aged_flag = $S{agedFlag}" ,field = "aged_flag")
    private String agedFlag;

    @ExcelProperty(value = "数字经济核心产业标识")
    @GraphQLField(kkhtml = "KFieldText", label = "数字经济核心产业标识", sql = "num_core_flag = $S{numCoreFlag}" ,field = "num_core_flag")
    private String numCoreFlag;
}
