package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//资产负债剩余期限信息
@Data
@GraphQLModel(fetcher = "ZG13Service",table = "app_pbc_report_zg13")
public class ZG13 {

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "id = $S{id}", field = "id")
    private String id;

    @ExcelProperty(value = "数据日期")
    @GraphQLField(kkhtml = "KFieldText", label = "实际报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "theory_report_start_date = $S{theoryReportStartDate}", field = "theory_report_start_date")
    private String theoryReportStartDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;

    @ExcelProperty(value = "产品代码_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd like '%$U{ProdCd}%'", field = "prod_cd")
    private String prodCd;

    @ExcelProperty(value = "资产负债项目")
    @GraphQLField(kkhtml = "KFieldText", label = "资产负债项目", sql = "asset_debt_project = $S{assetDebtProject}", field = "asset_debt_project")
    private String assetDebtProject;
    @ExcelProperty(value = "其他股权投资内部编码")
    @GraphQLField(kkhtml = "KFieldText", label = "其他股权投资内部编码", sql = "scr_cd = $S{scrCd}", field = "scr_cd")
    private String scrCd;
    @ExcelProperty(value = "标的企业名称")
    @GraphQLField(kkhtml = "KFieldText", label = "标的企业名称", sql = "scr_org_nm = $S{scrOrgNm}", field = "scr_org_nm")
    private String scrOrgNm;
    @ExcelProperty(value = "标的企业代码")
    @GraphQLField(kkhtml = "KFieldText", label = "标的企业代码", sql = "scr_org_cd = $S{scrOrgCd}", field = "scr_org_cd")
    private String scrOrgCd;
    @ExcelProperty(value = "地区代码_资管13表")
    @GraphQLField(kkhtml = "KFieldText", label = "地区代码_资管13表", sql = "org_blg_zon = $S{orgBlgZon}", field = "org_blg_zon")
    private String orgBlgZon;
    @ExcelProperty(value = "行业信息")
    @GraphQLField(kkhtml = "KFieldText", label = "行业信息", sql = "org_blg_industry = $S{orgBlgIndustry}", field = "org_blg_industry")
    private String orgBlgIndustry;
    @ExcelProperty(value = "企业出资人经济成分")
    @GraphQLField(kkhtml = "KFieldText", label = "企业出资人经济成分", sql = "org_typ_ecn = $S{orgTypEcn}", field = "org_typ_ecn")
    private String orgTypEcn;
    @ExcelProperty(value = "企业规模_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "企业规模", sql = "org_typ_scale = $S{orgTypScale}", field = "org_typ_scale")
    private String orgTypScale;
    @ExcelProperty(value = "股权投资方式")
    @GraphQLField(kkhtml = "KFieldText", label = "股权投资方式", sql = "right_invest_way = $S{rightInvestWay}", field = "right_invest_way")
    private String rightInvestWay;
    @ExcelProperty(value = "股权出让方代码")
    @GraphQLField(kkhtml = "KFieldText", label = "股权出让方代码", sql = "right_org_cd = $S{rightOrgCd}", field = "right_org_cd")
    private String rightOrgCd;
    @ExcelProperty(value = "股权出让方名称")
    @GraphQLField(kkhtml = "KFieldText", label = "股权出让方名称", sql = "right_org_nm = $S{rightOrgNm}", field = "right_org_nm")
    private String rightOrgNm;
    @ExcelProperty(value = "合同币种")
    @GraphQLField(kkhtml = "KFieldText", label = "合同币种", sql = "ccy_cd = $S{ccyCd}", field = "ccy_cd")
    private String ccyCd;
    @ExcelProperty(value = "合同金额")
    @GraphQLField(kkhtml = "KFieldText", label = "合同金额", sql = "amount = $S{amount}", field = "amount")
    private String amount;
    @ExcelProperty(value = "合同金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "合同金额折人民币", sql = "amount_cny = $S{amountCny}", field = "amount_cny")
    private String amountCny;
    @ExcelProperty(value = "其他股权余额币种")
    @GraphQLField(kkhtml = "KFieldText", label = "其他股权余额币种", sql = "right_ccy_cd = $S{rightCcyCd}", field = "right_ccy_cd")
    private String rightCcyCd;
    @ExcelProperty(value = "其他股权余额")
    @GraphQLField(kkhtml = "KFieldText", label = "其他股权余额", sql = "right_amount = $S{rightAmount}", field = "right_amount")
    private String rightAmount;
    @ExcelProperty(value = "其他股权余额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "其他股权余额折人民币", sql = "right_amount_cny = $S{rightAmountCny}", field = "right_amount_cny")
    private String rightAmountCny;
    @ExcelProperty(value = "持股比例")
    @GraphQLField(kkhtml = "KFieldText", label = "持股比例", sql = "pos_rat = $S{posRat}", field = "pos_rat")
    private String posRat;
    @ExcelProperty(value = "投资退出方式")
    @GraphQLField(kkhtml = "KFieldText", label = "投资退出方式", sql = "invest_ext_way = $S{investExtWay}", field = "invest_ext_way")
    private String investExtWay;
    @ExcelProperty(value = "合同起始日期_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "合同起始日期", sql = "bgn_dt = $S{bgnDt}", field = "bgn_dt")
    private String bgnDt;
    @ExcelProperty(value = "合同预计终止日期")
    @GraphQLField(kkhtml = "KFieldText", label = "合同预计终止日期", sql = "mtu_dt = $S{mtuDt}", field = "mtu_dt")
    private String mtuDt;
    @ExcelProperty(value = "合同展期到期日期")
    @GraphQLField(kkhtml = "KFieldText", label = "合同展期到期日期", sql = "defer_mtu_dt = $S{deferMtuDt}", field = "defer_mtu_dt")
    private String deferMtuDt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getTheoryReportEndDate() {
        return theoryReportEndDate;
    }

    public void setTheoryReportEndDate(String theoryReportEndDate) {
        this.theoryReportEndDate = theoryReportEndDate;
    }

    public String getTheoryReportStartDate() {
        return theoryReportStartDate;
    }

    public void setTheoryReportStartDate(String theoryReportStartDate) {
        this.theoryReportStartDate = theoryReportStartDate;
    }

    public String getProdCd() {
        return prodCd;
    }

    public void setProdCd(String prodCd) {
        this.prodCd = prodCd;
    }

    public String getAssetDebtProject() {
        return assetDebtProject;
    }

    public void setAssetDebtProject(String assetDebtProject) {
        this.assetDebtProject = assetDebtProject;
    }

    public String getScrCd() {
        return scrCd;
    }

    public void setScrCd(String scrCd) {
        this.scrCd = scrCd;
    }

    public String getScrOrgNm() {
        return scrOrgNm;
    }

    public void setScrOrgNm(String scrOrgNm) {
        this.scrOrgNm = scrOrgNm;
    }

    public String getScrOrgCd() {
        return scrOrgCd;
    }

    public void setScrOrgCd(String scrOrgCd) {
        this.scrOrgCd = scrOrgCd;
    }

    public String getOrgBlgZon() {
        return orgBlgZon;
    }

    public void setOrgBlgZon(String orgBlgZon) {
        this.orgBlgZon = orgBlgZon;
    }

    public String getOrgBlgIndustry() {
        return orgBlgIndustry;
    }

    public void setOrgBlgIndustry(String orgBlgIndustry) {
        this.orgBlgIndustry = orgBlgIndustry;
    }

    public String getOrgTypEcn() {
        return orgTypEcn;
    }

    public void setOrgTypEcn(String orgTypEcn) {
        this.orgTypEcn = orgTypEcn;
    }

    public String getOrgTypScale() {
        return orgTypScale;
    }

    public void setOrgTypScale(String orgTypScale) {
        this.orgTypScale = orgTypScale;
    }

    public String getRightInvestWay() {
        return rightInvestWay;
    }

    public void setRightInvestWay(String rightInvestWay) {
        this.rightInvestWay = rightInvestWay;
    }

    public String getRightOrgCd() {
        return rightOrgCd;
    }

    public void setRightOrgCd(String rightOrgCd) {
        this.rightOrgCd = rightOrgCd;
    }

    public String getRightOrgNm() {
        return rightOrgNm;
    }

    public void setRightOrgNm(String rightOrgNm) {
        this.rightOrgNm = rightOrgNm;
    }

    public String getCcyCd() {
        return ccyCd;
    }

    public void setCcyCd(String ccyCd) {
        this.ccyCd = ccyCd;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountCny() {
        return amountCny;
    }

    public void setAmountCny(String amountCny) {
        this.amountCny = amountCny;
    }

    public String getRightCcyCd() {
        return rightCcyCd;
    }

    public void setRightCcyCd(String rightCcyCd) {
        this.rightCcyCd = rightCcyCd;
    }

    public String getRightAmount() {
        return rightAmount;
    }

    public void setRightAmount(String rightAmount) {
        this.rightAmount = rightAmount;
    }

    public String getRightAmountCny() {
        return rightAmountCny;
    }

    public void setRightAmountCny(String rightAmountCny) {
        this.rightAmountCny = rightAmountCny;
    }

    public String getPosRat() {
        return posRat;
    }

    public void setPosRat(String posRat) {
        this.posRat = posRat;
    }

    public String getInvestExtWay() {
        return investExtWay;
    }

    public void setInvestExtWay(String investExtWay) {
        this.investExtWay = investExtWay;
    }

    public String getBgnDt() {
        return bgnDt;
    }

    public void setBgnDt(String bgnDt) {
        this.bgnDt = bgnDt;
    }

    public String getMtuDt() {
        return mtuDt;
    }

    public void setMtuDt(String mtuDt) {
        this.mtuDt = mtuDt;
    }

    public String getDeferMtuDt() {
        return deferMtuDt;
    }

    public void setDeferMtuDt(String deferMtuDt) {
        this.deferMtuDt = deferMtuDt;
    }
}