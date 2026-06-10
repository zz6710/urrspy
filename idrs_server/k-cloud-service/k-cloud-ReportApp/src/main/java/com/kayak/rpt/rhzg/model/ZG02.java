package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//资管产品初始募集信息
@Data
@GraphQLModel(fetcher = "ZG02Service",table = "app_pbc_report_zg02")
public class ZG02 {

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

    @ExcelProperty(value = "币种_资管02表")
    @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "clc_ccy = $S{clcCcy}" ,field = "clc_ccy")
    private String clcCcy;

    @ExcelProperty(value = "地区")
    @GraphQLField(kkhtml = "KFieldText", label = "地区", sql = "clc_source_zon_cd = $S{clcSourceZonCd}" ,field = "clc_source_zon_cd")
    private String clcSourceZonCd;

    @ExcelProperty(value = "客户类型_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "客户类型", sql = "clc_source_cust_typ = $S{clcSourceCustTyp}" ,field = "clc_source_cust_typ")
    private String clcSourceCustTyp;

    @ExcelProperty(value = "初始募集金额")
    @GraphQLField(kkhtml = "KFieldText", label = "初始募集金额", sql = "clc_amt_begin = $S{clcAmtBegin}" ,field = "clc_amt_begin")
    private String clcAmtBegin;

    @ExcelProperty(value = "初始募集金额折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "初始募集金额折人民币", sql = "clc_amt_begin_cny = $S{clcAmtBeginCny}" ,field = "clc_amt_begin_cny")
    private String clcAmtBeginCny;

    @ExcelProperty(value = "初始募集份额")
    @GraphQLField(kkhtml = "KFieldText", label = "初始募集份额", sql = "clc_lot_begin = $S{clcLotBegin}" ,field = "clc_lot_begin")
    private String clcLotBegin;

    @ExcelProperty(value = "产品初始单位净值")
    @GraphQLField(kkhtml = "KFieldText", label = "产品初始单位净值", sql = "unt_nav = $S{untNav}" ,field = "unt_nav")
    private String untNav;

    @ExcelProperty(value = "产品初始单位净值折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "产品初始单位净值折人民币", sql = "unt_nav_cny = $S{untNavCny}" ,field = "unt_nav_ccy")
    private String untNavCny;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "theory_report_start_date >= $S{beginDate}" ,field = "begin_date")
    private String beginDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "theory_report_start_date <= $S{queryDate}" ,field = "query_date")
    private String queryDate;

    @GraphQLField(kkhtml = "KFieldText", label = "地区字典", field = "clc_source_zon_cd_text")
    public String clcSourceZonCdText;


    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

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

    public String getClcCcy() {
        return clcCcy;
    }

    public void setClcCcy(String clcCcy) {
        this.clcCcy = clcCcy;
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

    public String getClcAmtBegin() {
        return clcAmtBegin;
    }

    public void setClcAmtBegin(String clcAmtBegin) {
        this.clcAmtBegin = clcAmtBegin;
    }

    public String getClcAmtBeginCny() {
        return clcAmtBeginCny;
    }

    public void setClcAmtBeginCny(String clcAmtBeginCny) {
        this.clcAmtBeginCny = clcAmtBeginCny;
    }

    public String getClcLotBegin() {
        return clcLotBegin;
    }

    public void setClcLotBegin(String clcLotBegin) {
        this.clcLotBegin = clcLotBegin;
    }

    public String getClcSourceZonCdText() {
        return clcSourceZonCdText;
    }

    public void setClcSourceZonCdText(String clcSourceZonCdText) {
        this.clcSourceZonCdText = clcSourceZonCdText;
    }
}
