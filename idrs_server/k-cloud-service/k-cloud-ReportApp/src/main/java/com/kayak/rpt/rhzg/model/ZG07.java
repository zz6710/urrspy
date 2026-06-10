package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//除回购和拆借外贷款明细信息
@Data
@GraphQLModel(fetcher = "ZG07Service",table = "app_pbc_report_zg07")
public class ZG07 {

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

    @ExcelProperty(value = "贷款种类")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款种类", sql = "loan_typ = $S{loanTyp}" ,field = "loan_typ")
    private String loanTyp;

    @ExcelProperty(value = "贷款转让方机构代码")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款转让方机构代码", sql = "loan_trans_org_cd = $S{loanTransOrgCd}" ,field = "loan_trans_org_cd")
    private String loanTransOrgCd;

    @ExcelProperty(value = "贷款合同原始发放机构代码")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款合同原始发放机构代码", sql = "loan_contract_ori_cd = $S{loanContractOriCd}" ,field = "loan_contract_ori_cd")
    private String loanContractOriCd;

    @ExcelProperty(value = "贷款合同原始发放机构所在地代码")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款合同原始发放机构所在地代码", sql = "loan_contract_ori_zone = $S{loanContractOriZone}" ,field = "loan_contract_ori_zone")
    private String loanContractOriZone;

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

    @ExcelProperty(value = "贷款借据编码_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款借据编码", sql = "loan_receipt_cd = $S{loanReceiptCd}" ,field = "loan_receipt_cd")
    private String loanReceiptCd;

    @ExcelProperty(value = "贷款产品类别_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款产品类别", sql = "loan_prod_typ = $S{loanProdTyp}" ,field = "loan_prod_typ")
    private String loanProdTyp;

    @ExcelProperty(value = "贷款实际投向_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款实际投向", sql = "loan_real_trans = $S{loanRealTrans}" ,field = "loan_real_trans")
    private String loanRealTrans;

    @ExcelProperty(value = "贷款发放日期_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款发放日期", sql = "loan_issue_dt = $S{loanIssueDt}" ,field = "loan_issue_dt")
    private String loanIssueDt;

    @ExcelProperty(value = "贷款到期日期_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款到期日期", sql = "loan_end_dt = $S{loanEndDt}" ,field = "loan_end_dt")
    private String loanEndDt;

    @ExcelProperty(value = "贷款展期到期日期_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款展期到期日期", sql = "loan_extension_dt = $S{loanExtensionDt}" ,field = "loan_extension_dt")
    private String loanExtensionDt;

    @ExcelProperty(value = "利率是否固定_资管07表")
    @GraphQLField(kkhtml = "KFieldText", label = "利率是否固定", sql = "is_fixed_rate = $S{isFixedRate}" ,field = "is_fixed_rate")
    private String isFixedRate;

    @ExcelProperty(value = "利率水平_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "利率水平", sql = "rate_level = $S{rateLevel}" ,field = "rate_level")
    private String rateLevel;

    @ExcelProperty(value = "贷款担保方式_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款担保方式", sql = "guarantee_mode = $S{guaranteeMode}" ,field = "guarantee_mode")
    private String guaranteeMode;

    @ExcelProperty(value = "贷款质量_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款质量", sql = "loan_quality = $S{loanQuality}" ,field = "loan_quality")
    private String loanQuality;

    @ExcelProperty(value = "贷款状态_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款状态", sql = "loan_status = $S{loanStatus}" ,field = "loan_status")
    private String loanStatus;

    @ExcelProperty(value = "贷款转让折扣率")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款转让折扣率", sql = "loan_trans_discount_rate = $S{loanTransDiscountRate}" ,field = "loan_trans_discount_rate")
    private String loanTransDiscountRate;

    @ExcelProperty(value = "原始合同币种")
    @GraphQLField(kkhtml = "KFieldText", label = "原始合同币种", sql = "ori_contract_ccy = $S{oriContractCcy}" ,field = "ori_contract_ccy")
    private String oriContractCcy;

    @ExcelProperty(value = "原始合同金额")
    @GraphQLField(kkhtml = "KFieldText", label = "原始合同金额", sql = "ori_contract_amt = $S{oriContractAmt}" ,field = "ori_contract_amt")
    private String oriContractAmt;

    @ExcelProperty(value = "原始合同金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "原始合同金额折人民币", sql = "ori_contract_amt_cny = $S{oriContractAmtCny}" ,field = "ori_contract_amt_cny")
    private String oriContractAmtCny;

    @ExcelProperty(value = "贷款余额币种")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款余额币种", sql = "loan_balance_ccy = $S{loanBalanceCcy}" ,field = "loan_balance_ccy")
    private String loanBalanceCcy;

    @ExcelProperty(value = "贷款余额_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款余额", sql = "loan_balance = $S{loanBalance}" ,field = "loan_balance")
    private String loanBalance;

    @ExcelProperty(value = "贷款余额折人民币_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "贷款余额折人民币", sql = "loan_balance_cny = $S{loanBalanceCny}" ,field = "loan_balance_cny")
    private String loanBalanceCny;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdCd() {
        return prodCd;
    }

    public void setProdCd(String prodCd) {
        this.prodCd = prodCd;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getTheoryReportStartDate() {
        return theoryReportStartDate;
    }

    public void setTheoryReportStartDate(String theoryReportStartDate) {
        this.theoryReportStartDate = theoryReportStartDate;
    }

    public String getTheoryReportEndDate() {
        return theoryReportEndDate;
    }

    public void setTheoryReportEndDate(String theoryReportEndDate) {
        this.theoryReportEndDate = theoryReportEndDate;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getLoanTyp() {
        return loanTyp;
    }

    public void setLoanTyp(String loanTyp) {
        this.loanTyp = loanTyp;
    }

    public String getLoanTransOrgCd() {
        return loanTransOrgCd;
    }

    public void setLoanTransOrgCd(String loanTransOrgCd) {
        this.loanTransOrgCd = loanTransOrgCd;
    }

    public String getLoanContractOriCd() {
        return loanContractOriCd;
    }

    public void setLoanContractOriCd(String loanContractOriCd) {
        this.loanContractOriCd = loanContractOriCd;
    }

    public String getLoanContractOriZone() {
        return loanContractOriZone;
    }

    public void setLoanContractOriZone(String loanContractOriZone) {
        this.loanContractOriZone = loanContractOriZone;
    }

    public String getBorrowerTyp() {
        return borrowerTyp;
    }

    public void setBorrowerTyp(String borrowerTyp) {
        this.borrowerTyp = borrowerTyp;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getBorrowerCd() {
        return borrowerCd;
    }

    public void setBorrowerCd(String borrowerCd) {
        this.borrowerCd = borrowerCd;
    }

    public String getIndustryMsg() {
        return industryMsg;
    }

    public void setIndustryMsg(String industryMsg) {
        this.industryMsg = industryMsg;
    }

    public String getEnterSponsorEcoSector() {
        return enterSponsorEcoSector;
    }

    public void setEnterSponsorEcoSector(String enterSponsorEcoSector) {
        this.enterSponsorEcoSector = enterSponsorEcoSector;
    }

    public String getEnterScale() {
        return enterScale;
    }

    public void setEnterScale(String enterScale) {
        this.enterScale = enterScale;
    }

    public String getLoanReceiptCd() {
        return loanReceiptCd;
    }

    public void setLoanReceiptCd(String loanReceiptCd) {
        this.loanReceiptCd = loanReceiptCd;
    }

    public String getLoanProdTyp() {
        return loanProdTyp;
    }

    public void setLoanProdTyp(String loanProdTyp) {
        this.loanProdTyp = loanProdTyp;
    }

    public String getLoanRealTrans() {
        return loanRealTrans;
    }

    public void setLoanRealTrans(String loanRealTrans) {
        this.loanRealTrans = loanRealTrans;
    }

    public String getLoanIssueDt() {
        return loanIssueDt;
    }

    public void setLoanIssueDt(String loanIssueDt) {
        this.loanIssueDt = loanIssueDt;
    }

    public String getLoanEndDt() {
        return loanEndDt;
    }

    public void setLoanEndDt(String loanEndDt) {
        this.loanEndDt = loanEndDt;
    }

    public String getLoanExtensionDt() {
        return loanExtensionDt;
    }

    public void setLoanExtensionDt(String loanExtensionDt) {
        this.loanExtensionDt = loanExtensionDt;
    }

    public String getIsFixedRate() {
        return isFixedRate;
    }

    public void setIsFixedRate(String isFixedRate) {
        this.isFixedRate = isFixedRate;
    }

    public String getRateLevel() {
        return rateLevel;
    }

    public void setRateLevel(String rateLevel) {
        this.rateLevel = rateLevel;
    }

    public String getGuaranteeMode() {
        return guaranteeMode;
    }

    public void setGuaranteeMode(String guaranteeMode) {
        this.guaranteeMode = guaranteeMode;
    }

    public String getLoanQuality() {
        return loanQuality;
    }

    public void setLoanQuality(String loanQuality) {
        this.loanQuality = loanQuality;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanTransDiscountRate() {
        return loanTransDiscountRate;
    }

    public void setLoanTransDiscountRate(String loanTransDiscountRate) {
        this.loanTransDiscountRate = loanTransDiscountRate;
    }

    public String getOriContractCcy() {
        return oriContractCcy;
    }

    public void setOriContractCcy(String oriContractCcy) {
        this.oriContractCcy = oriContractCcy;
    }

    public String getOriContractAmt() {
        return oriContractAmt;
    }

    public void setOriContractAmt(String oriContractAmt) {
        this.oriContractAmt = oriContractAmt;
    }

    public String getOriContractAmtCny() {
        return oriContractAmtCny;
    }

    public void setOriContractAmtCny(String oriContractAmtCny) {
        this.oriContractAmtCny = oriContractAmtCny;
    }

    public String getLoanBalanceCcy() {
        return loanBalanceCcy;
    }

    public void setLoanBalanceCcy(String loanBalanceCcy) {
        this.loanBalanceCcy = loanBalanceCcy;
    }

    public String getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(String loanBalance) {
        this.loanBalance = loanBalance;
    }

    public String getLoanBalanceCny() {
        return loanBalanceCny;
    }

    public void setLoanBalanceCny(String loanBalanceCny) {
        this.loanBalanceCny = loanBalanceCny;
    }

    public String getZoneText() {
        return zoneText;
    }

    public void setZoneText(String zoneText) {
        this.zoneText = zoneText;
    }

    public String getTechFlag() {
        return techFlag;
    }

    public void setTechFlag(String techFlag) {
        this.techFlag = techFlag;
    }

    public String getGreenFlag() {
        return greenFlag;
    }

    public void setGreenFlag(String greenFlag) {
        this.greenFlag = greenFlag;
    }

    public String getSpecFlag() {
        return specFlag;
    }

    public void setSpecFlag(String specFlag) {
        this.specFlag = specFlag;
    }

    public String getAgedFlag() {
        return agedFlag;
    }

    public void setAgedFlag(String agedFlag) {
        this.agedFlag = agedFlag;
    }

    public String getNumCoreFlag() {
        return numCoreFlag;
    }

    public void setNumCoreFlag(String numCoreFlag) {
        this.numCoreFlag = numCoreFlag;
    }
}
