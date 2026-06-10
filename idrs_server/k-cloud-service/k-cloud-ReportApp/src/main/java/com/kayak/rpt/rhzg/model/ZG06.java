package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//资产收益权明细信息
@Data
@GraphQLModel(fetcher = "ZG06Service",table = "app_pbc_report_zg06")
public class ZG06 {

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

    @ExcelProperty(value = "资产负债项目")
    @GraphQLField(kkhtml = "KFieldText", label = "资产负债项目", sql = "asset_debt_project = $S{assetDebtProject}" ,field = "asset_debt_project")
    private String assetDebtProject;

    @ExcelProperty(value = "资产收益权内部编码")
    @GraphQLField(kkhtml = "KFieldText", label = "资产收益权内部编码", sql = "asset_income_code = $S{assetIncomeCode}" ,field = "asset_income_code")
    private String assetIncomeCode;

    @ExcelProperty(value = "基础资产出让机构名称")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产出让机构名称", sql = "base_asset_sale_org_nm = $S{baseAssetSaleOrgNm}" ,field = "base_asset_sale_org_nm")
    private String baseAssetSaleOrgNm;

    @ExcelProperty(value = "基础资产出让机构代码")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产出让机构代码", sql = "base_asset_sale_org_cd = $S{baseAssetSaleOrgCd}" ,field = "base_asset_sale_org_cd")
    private String baseAssetSaleOrgCd;

    @ExcelProperty(value = "基础资产出让机构类型")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产出让机构类型", sql = "base_asset_sale_org_typ = $S{baseAssetSaleOrgTyp}" ,field = "base_asset_sale_org_typ")
    private String baseAssetSaleOrgTyp;

    @ExcelProperty(value = "基础资产出让机构行业")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产出让机构行业", sql = "base_asset_sale_org_industry = $S{baseAssetSaleOrgIndustry}" ,field = "base_asset_sale_org_industry")
    private String baseAssetSaleOrgIndustry;

    @ExcelProperty(value = "基础资产出让机构注册地区")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产出让机构注册地区", sql = "base_asset_sale_org_reg_zone = $S{baseAssetSaleOrgRegZone}" ,field = "base_asset_sale_org_reg_zone")
    private String baseAssetSaleOrgRegZone;

    @ExcelProperty(value = "基础资产出让机构经济成分")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产出让机构经济成分", sql = "base_asset_sale_org_econo_sector = $S{baseAssetSaleOrgEconoSector}" ,field = "base_asset_sale_org_econo_sector")
    private String baseAssetSaleOrgEconoSector;

    @ExcelProperty(value = "基础资产出让机构规模")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产出让机构规模", sql = "base_asset_sale_org_scale = $S{baseAssetSaleOrgScale}" ,field = "base_asset_sale_org_scale")
    private String baseAssetSaleOrgScale;

    @ExcelProperty(value = "转让起始日期")
    @GraphQLField(kkhtml = "KFieldText", label = "转让起始日期", sql = "trans_dt = $S{transDt}" ,field = "trans_dt")
    private String transDt;

    @ExcelProperty(value = "转让预计终止日期")
    @GraphQLField(kkhtml = "KFieldText", label = "转让预计终止日期", sql = "trans_scheduled_end_dt = $S{transScheduledEndDt}" ,field = "trans_scheduled_end_dt")
    private String transScheduledEndDt;

    @ExcelProperty(value = "转让展期到期日期")
    @GraphQLField(kkhtml = "KFieldText", label = "转让展期到期日期", sql = "trans_extension_dt = $S{transExtensionDt}" ,field = "trans_extension_dt")
    private String transExtensionDt;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "转让实际终止日期", sql = "trans_real_end_dt = $S{transRealEndDt}" ,field = "trans_real_end_dt")
    private String transRealEndDt;

    @ExcelProperty(value = "基础资产类型")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产类型", sql = "base_asset_typ = $S{baseAssetTyp}" ,field = "base_asset_typ")
    private String baseAssetTyp;

    @ExcelProperty(value = "基础资产原始协议币种")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产原始协议币种", sql = "BASE_ASSET_ORI_PROT_CCY = $S{baseAssetOriProtCcy}" ,field = "BASE_ASSET_ORI_PROT_CCY")
    private String baseAssetOriProtCcy;

    @ExcelProperty(value = "基础资产原始协议金额")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产原始协议金额", sql = "base_asset_ori_prot_amt = $S{baseAssetOriProtAmt}" ,field = "base_asset_ori_prot_amt")
    private String baseAssetOriProtAmt;

    @ExcelProperty(value = "基础资产原始协议金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产原始协议金额折人民币", sql = "base_asset_ori_prot_amt_cny = $S{baseAssetOriProtAmtCny}" ,field = "base_asset_ori_prot_amt_cny")
    private String baseAssetOriProtAmtCny;

    @ExcelProperty(value = "基础资产转让币种")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产转让币种", sql = "base_asset_trans_ccy = $S{baseAssetTransCcy}" ,field = "base_asset_trans_ccy")
    private String baseAssetTransCcy;

    @ExcelProperty(value = "基础资产转让金额")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产转让金额", sql = "base_asset_trans_amt = $S{baseAssetTransAmt}" ,field = "base_asset_trans_amt")
    private String baseAssetTransAmt;

    @ExcelProperty(value = "基础资产转让金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产转让金额折人民币", sql = "base_asset_trans_amt_cny = $S{baseAssetTransAmtCny}" ,field = "base_asset_trans_amt_cny")
    private String baseAssetTransAmtCny;

    @ExcelProperty(value = "出让机构出表标识")
    @GraphQLField(kkhtml = "KFieldText", label = "出让机构出表标识", sql = "trans_org_out_table_f = $S{transOrgOutTableF}" ,field = "trans_org_out_table_f")
    private String transOrgOutTableF;

    @ExcelProperty(value = "出让机构回购标识")
    @GraphQLField(kkhtml = "KFieldText", label = "出让机构回购标识", sql = "trans_org_buy_back_f = $S{transOrgBuyBackF}" ,field = "trans_org_buy_back_f")
    private String transOrgBuyBackF;

    @ExcelProperty(value = "利率是否固定_资管06表")
    @GraphQLField(kkhtml = "KFieldText", label = "利率是否固定", sql = "is_fixed_rate = $S{isFixedRate}" ,field = "is_fixed_rate")
    private String isFixedRate;

    @ExcelProperty(value = "利率水平_资管06表")
    @GraphQLField(kkhtml = "KFieldText", label = "利率水平", sql = "rate_level = $S{rateLevel}" ,field = "rate_level")
    private String rateLevel;

    @ExcelProperty(value = "担保方式_资管06表")
    @GraphQLField(kkhtml = "KFieldText", label = "担保方式", sql = "guarantee_mode = $S{guaranteeMode}" ,field = "guarantee_mode")
    private String guaranteeMode;

    @ExcelProperty(value = "基础资产投向部门")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产投向部门", sql = "base_asset_trans_dep = $S{baseAssetTransDep}" ,field = "base_asset_trans_dep")
    private String baseAssetTransDep;

    @ExcelProperty(value = "基础资产期末币种")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产期末币种", sql = "base_asset_end_dt_ccy = $S{baseAssetEndDtCcy}" ,field = "base_asset_end_dt_ccy")
    private String baseAssetEndDtCcy;

    @ExcelProperty(value = "基础资产期末余额")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产期末余额", sql = "base_asset_end_dt_balance = $S{baseAssetEndDtBalance}" ,field = "base_asset_end_dt_balance")
    private String baseAssetEndDtBalance;

    @ExcelProperty(value = "基础资产期末余额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产期末余额折人民币", sql = "base_asset_end_dt_balance_cny = $S{baseAssetEndDtBalanceCny}" ,field = "base_asset_end_dt_balance_cny")
    private String baseAssetEndDtBalanceCny;

    @GraphQLField(kkhtml = "KFieldText" ,field = "zone_text")
    public String zoneText;

    @ExcelProperty(value = "登记交易场所")
    @GraphQLField(kkhtml = "KFieldText", label = "登记交易场所")
    private String registerTradingPlace;

    @ExcelProperty(value = "登记交易场所代码_资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "登记交易场所代码_资管产品")
    private String registerTradingCode;

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

    @ExcelProperty(value = "基础资产投向对象行业")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产投向对象行业", sql = "base_asset_inv_obj_idt = $S{baseAssetInvObjIdt}" ,field = "base_asset_inv_obj_idt")
    private String baseAssetInvObjIdt;

    @ExcelProperty(value = "基础资产投向对象规模")
    @GraphQLField(kkhtml = "KFieldText", label = "基础资产投向对象规模", sql = "base_asset_inv_obj_scale = $S{baseAssetInvObjScale}" ,field = "base_asset_inv_obj_scale")
    private String baseAssetInvObjScale;
}
