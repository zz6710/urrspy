package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//资管产品存续期募集信息
@Data
@GraphQLModel(fetcher = "ZG04Service",table = "app_pbc_report_zg04")
public class ZG04 {

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

    @ExcelProperty(value = "地区")
    @GraphQLField(kkhtml = "KFieldText", label = "地区", sql = "clc_source_zon_cd = $S{clcSourceZonCd}" ,field = "clc_source_zon_cd")
    private String clcSourceZonCd;

    @ExcelProperty(value = "客户类型_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "客户类型", sql = "clc_source_cust_typ = $S{clcSourceCustTyp}" ,field = "clc_source_cust_typ")
    private String clcSourceCustTyp;

    @ExcelProperty(value = "币种_资管0304表")
    @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "trans_ccy = $S{transCcy}" ,field = "trans_ccy")
    private String transCcy;

    @ExcelProperty(value = "当期申购金额")
    @GraphQLField(kkhtml = "KFieldText", label = "当期申购金额", sql = "cur_pch_amt = $S{curPchAmt}" ,field = "cur_pch_amt")
    private String curPchAmt;

    @ExcelProperty(value = "当期申购金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "当期申购金额折人民币", sql = "cur_pch_amt_cny = $S{curPchAmtCny}" ,field = "cur_pch_amt_cny")
    private String curPchAmtCny;

    @ExcelProperty(value = "当期申购份额")
    @GraphQLField(kkhtml = "KFieldText", label = "当期申购份额", sql = "cur_pch_lot = $S{curPchLot}" ,field = "cur_pch_lot")
    private String curPchLot;

    @ExcelProperty(value = "当期兑付/赎回金额")
    @GraphQLField(kkhtml = "KFieldText", label = "当期兑付/赎回金额", sql = "cur_call_amt = $S{curCallAmt}" ,field = "cur_call_amt")
    private String curCallAmt;

    @ExcelProperty(value = "当期兑付/赎回金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "当期兑付/赎回金额折人民币", sql = "cur_call_amt_cny = $S{curCallAmtCny}" ,field = "cur_call_amt_cny")
    private String curCallAmtCny;

    @ExcelProperty(value = "当期兑付/赎回份额")
    @GraphQLField(kkhtml = "KFieldText", label = "当期兑付/赎回份额", sql = "cur_call_lot = $S{curCallLot}" ,field = "cur_call_lot")
    private String curCallLot;

    @ExcelProperty(value = "期末产品金额")
    @GraphQLField(kkhtml = "KFieldText", label = "期末产品金额", sql = "end_prod_amt = $S{endProdAmt}" ,field = "end_prod_amt")
    private String endProdAmt;

    @ExcelProperty(value = "期末产品金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "期末产品金额折人民币", sql = "end_prod_amt_cny = $S{endProdAmtCny}" ,field = "end_prod_amt_cny")
    private String endProdAmtCny;

    @ExcelProperty(value = "期末产品份额")
    @GraphQLField(kkhtml = "KFieldText", label = "期末产品份额", sql = "end_prod_lot = $S{endProdLot}" ,field = "end_prod_lot")
    private String endProdLot;

    @ExcelProperty(value = "净值型产品期末净值")
    @GraphQLField(kkhtml = "KFieldText", label = "净值型产品期末净值", sql = "netval_prod_end_nav = $S{netvalProdEndNav}" ,field = "netval_prod_end_nav")
    private String netvalProdEndNav;

    @ExcelProperty(value = "净值型产品期末净值折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "净值型产品期末净值折人民币", sql = "netval_prod_end_nav_cny = $S{netvalProdEndNavCny}" ,field = "netval_prod_end_nav_cny")
    private String netvalProdEndNavCny;

    @ExcelProperty(value = "净值型产品期末累计净值")
    @GraphQLField(kkhtml = "KFieldText", label = "净值型产品期末累计净值", sql = "netval_prod_end_acm_nav = $S{netvalProdEndAcmNav}" ,field = "netval_prod_end_acm_nav")
    private String netvalProdEndAcmNav;

    @ExcelProperty(value = "净值型产品期末累计净值折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "净值型产品期末累计净值折人民币", sql = "netval_prod_end_acm_nav_cny = $S{netvalProdEndAcmNavCny}" ,field = "netval_prod_end_acm_nav_cny")
    private String netvalProdEndAcmNavCny;

    @ExcelProperty(value = "产品期末业绩表现")
    @GraphQLField(kkhtml = "KFieldText", label = "产品期末业绩表现", sql = "prod_end_anl_yld = $S{prodEndAnlYld}" ,field = "prod_end_anl_yld")
    private String prodEndAnlYld;
    @ExcelProperty(value = "当月年化收益率")
    @GraphQLField(kkhtml = "KFieldText", label = "当月年化收益率", sql = "month_end_anl_yld = $S{monthEndAnlYld}" ,field = "month_end_anl_yld")
    private String monthEndAnlYld;

    @GraphQLField(kkhtml = "KFieldText", label = "地区字典", field = "clc_source_zon_cd_text")
    public String clcSourceZonCdText;

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

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getClcSourceZonCd() {
        return clcSourceZonCd;
    }

    public void setClcSourceZonCd(String clcSourceZonCd) {
        this.clcSourceZonCd = clcSourceZonCd;
    }

    public String getClcSourceCustTyp() {
        return clcSourceCustTyp;
    }

    public void setClcSourceCustTyp(String clcSourceCustTyp) {
        this.clcSourceCustTyp = clcSourceCustTyp;
    }

    public String getTransCcy() {
        return transCcy;
    }

    public void setTransCcy(String transCcy) {
        this.transCcy = transCcy;
    }

    public String getCurPchAmt() {
        return curPchAmt;
    }

    public void setCurPchAmt(String curPchAmt) {
        this.curPchAmt = curPchAmt;
    }

    public String getCurPchAmtCny() {
        return curPchAmtCny;
    }

    public void setCurPchAmtCny(String curPchAmtCny) {
        this.curPchAmtCny = curPchAmtCny;
    }

    public String getCurPchLot() {
        return curPchLot;
    }

    public void setCurPchLot(String curPchLot) {
        this.curPchLot = curPchLot;
    }

    public String getCurCallAmt() {
        return curCallAmt;
    }

    public void setCurCallAmt(String curCallAmt) {
        this.curCallAmt = curCallAmt;
    }

    public String getCurCallAmtCny() {
        return curCallAmtCny;
    }

    public void setCurCallAmtCny(String curCallAmtCny) {
        this.curCallAmtCny = curCallAmtCny;
    }

    public String getCurCallLot() {
        return curCallLot;
    }

    public void setCurCallLot(String curCallLot) {
        this.curCallLot = curCallLot;
    }

    public String getEndProdAmt() {
        return endProdAmt;
    }

    public void setEndProdAmt(String endProdAmt) {
        this.endProdAmt = endProdAmt;
    }

    public String getEndProdAmtCny() {
        return endProdAmtCny;
    }

    public void setEndProdAmtCny(String endProdAmtCny) {
        this.endProdAmtCny = endProdAmtCny;
    }

    public String getEndProdLot() {
        return endProdLot;
    }

    public void setEndProdLot(String endProdLot) {
        this.endProdLot = endProdLot;
    }

    public String getNetvalProdEndNav() {
        return netvalProdEndNav;
    }

    public void setNetvalProdEndNav(String netvalProdEndNav) {
        this.netvalProdEndNav = netvalProdEndNav;
    }

    public String getNetvalProdEndNavCny() {
        return netvalProdEndNavCny;
    }

    public void setNetvalProdEndNavCny(String netvalProdEndNavCny) {
        this.netvalProdEndNavCny = netvalProdEndNavCny;
    }

    public String getNetvalProdEndAcmNav() {
        return netvalProdEndAcmNav;
    }

    public void setNetvalProdEndAcmNav(String netvalProdEndAcmNav) {
        this.netvalProdEndAcmNav = netvalProdEndAcmNav;
    }

    public String getNetvalProdEndAcmNavCny() {
        return netvalProdEndAcmNavCny;
    }

    public void setNetvalProdEndAcmNavCny(String netvalProdEndAcmNavCny) {
        this.netvalProdEndAcmNavCny = netvalProdEndAcmNavCny;
    }

    public String getProdEndAnlYld() {
        return prodEndAnlYld;
    }

    public void setProdEndAnlYld(String prodEndAnlYld) {
        this.prodEndAnlYld = prodEndAnlYld;
    }

    public String getClcSourceZonCdText() {
        return clcSourceZonCdText;
    }

    public void setClcSourceZonCdText(String clcSourceZonCdText) {
        this.clcSourceZonCdText = clcSourceZonCdText;
    }
}
