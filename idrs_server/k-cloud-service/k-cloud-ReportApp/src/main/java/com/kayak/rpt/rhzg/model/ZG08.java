package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//特定目的载体交易对手明细信息
@Data
@GraphQLModel(fetcher = "ZG08Service",table = "app_pbc_report_zg08")
public class ZG08 {

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

    @ExcelProperty(value = "交易对手产品种类")
    @GraphQLField(kkhtml = "KFieldText", label = "交易对手产品种类", sql = "counterpart_prod_typ = $S{counterpartProdTyp}" ,field = "counterpart_prod_typ")
    private String counterpartProdTyp;

    @ExcelProperty(value = "交易对手机构编码")
    @GraphQLField(kkhtml = "KFieldText", label = "交易对手机构编码", sql = "counterpart_org_cd = $S{counterpartOrgCd}" ,field = "counterpart_org_cd")
    private String counterpartOrgCd;

    @ExcelProperty(value = "交易对手产品代码")
    @GraphQLField(kkhtml = "KFieldText", label = "交易对手产品代码", sql = "counterpart_prod_cd = $S{counterpartProdCd}" ,field = "counterpart_prod_cd")
    private String counterpartProdCd;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "交易对手产品名称", sql = "counterpart_prod_nm = $S{counterpartProdNm}" ,field = "counterpart_prod_nm")
    private String counterpartProdNm;

    @ExcelProperty(value = "币种_资管08表")
    @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "ccy = $S{ccy}" ,field = "ccy")
    private String ccy;

    @ExcelProperty(value = "期末金额")
    @GraphQLField(kkhtml = "KFieldText", label = "期末金额", sql = "end_dt_amt = $S{endDtAmt}" ,field = "end_dt_amt")
    private String endDtAmt;

    @ExcelProperty(value = "期末金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "期末金额折人民币", sql = "end_dt_amt_cny = $S{endDtAmtCny}" ,field = "end_dt_amt_cny")
    private String endDtAmtCny;

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

    public String getAssetDebtProject() {
        return assetDebtProject;
    }

    public void setAssetDebtProject(String assetDebtProject) {
        this.assetDebtProject = assetDebtProject;
    }

    public String getCounterpartProdTyp() {
        return counterpartProdTyp;
    }

    public void setCounterpartProdTyp(String counterpartProdTyp) {
        this.counterpartProdTyp = counterpartProdTyp;
    }

    public String getCounterpartOrgCd() {
        return counterpartOrgCd;
    }

    public void setCounterpartOrgCd(String counterpartOrgCd) {
        this.counterpartOrgCd = counterpartOrgCd;
    }

    public String getCounterpartProdCd() {
        return counterpartProdCd;
    }

    public void setCounterpartProdCd(String counterpartProdCd) {
        this.counterpartProdCd = counterpartProdCd;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getEndDtAmt() {
        return endDtAmt;
    }

    public void setEndDtAmt(String endDtAmt) {
        this.endDtAmt = endDtAmt;
    }

    public String getEndDtAmtCny() {
        return endDtAmtCny;
    }

    public void setEndDtAmtCny(String endDtAmtCny) {
        this.endDtAmtCny = endDtAmtCny;
    }

    public String getCounterpartProdNm() {
        return counterpartProdNm;
    }

    public void setCounterpartProdNm(String counterpartProdNm) {
        this.counterpartProdNm = counterpartProdNm;
    }
}
