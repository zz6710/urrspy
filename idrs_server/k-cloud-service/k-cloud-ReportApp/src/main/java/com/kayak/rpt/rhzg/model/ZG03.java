package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//资管产品终止信息
@Data
@GraphQLModel(fetcher = "ZG03Service",table = "app_pbc_report_zg03")
public class ZG03 {

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

    @ExcelProperty(value = "产品实际终止日期")
    @GraphQLField(kkhtml = "KFieldText", label = "产品实际终止日期", sql = "prod_rel_end_dt = $S{prodRelEndDt}" ,field = "prod_rel_end_dt")
    private String prodRelEndDt;

    @ExcelProperty(value = "币种_资管0304表")
    @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "trans_ccy = $S{transCcy}" ,field = "trans_ccy")
    private String transCcy;

    @ExcelProperty(value = "发行机构实现收入")
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构实现收入", sql = "isu_org_ern = $S{isuOrgErn}" ,field = "isu_org_ern")
    private String isuOrgErn;

    @ExcelProperty(value = "发行机构实现收入折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构实现收入折人民币", sql = "isu_org_ern_cny = $S{isuOrgErnCny}" ,field = "isu_org_ern_cny")
    private String isuOrgErnCny;

    @ExcelProperty(value = "兑付客户收益")
    @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益", sql = "cust_call_ern = $S{custCallErn}" ,field = "cust_call_ern")
    private String custCallErn;

    @ExcelProperty(value = "兑付客户收益折人民币")
    @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益折人民币", sql = "cust_call_ern_cny = $S{custCallErnCny}" ,field = "cust_call_ern_cny")
    private String custCallErnCny;

    @ExcelProperty(value = "兑付客户收益率")
    @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益率", sql = "cust_call_ern_rate = $S{custCallErnRate}" ,field = "cust_call_ern_rate")
    private String custCallErnRate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "theory_report_start_date >= $S{beginDate}" ,field = "begin_date")
    private String beginDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "theory_report_start_date <= $S{queryDate}" ,field = "query_date")
    private String queryDate;


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

    public String getProdRelEndDt() {
        return prodRelEndDt;
    }

    public void setProdRelEndDt(String prodRelEndDt) {
        this.prodRelEndDt = prodRelEndDt;
    }

    public String getTransCcy() {
        return transCcy;
    }

    public void setTransCcy(String transCcy) {
        this.transCcy = transCcy;
    }

    public String getIsuOrgErn() {
        return isuOrgErn;
    }

    public void setIsuOrgErn(String isuOrgErn) {
        this.isuOrgErn = isuOrgErn;
    }

    public String getIsuOrgErnCny() {
        return isuOrgErnCny;
    }

    public void setIsuOrgErnCny(String isuOrgErnCny) {
        this.isuOrgErnCny = isuOrgErnCny;
    }

    public String getCustCallErn() {
        return custCallErn;
    }

    public void setCustCallErn(String custCallErn) {
        this.custCallErn = custCallErn;
    }

    public String getCustCallErnCny() {
        return custCallErnCny;
    }

    public void setCustCallErnCny(String custCallErnCny) {
        this.custCallErnCny = custCallErnCny;
    }

    public String getCustCallErnRate() {
        return custCallErnRate;
    }

    public void setCustCallErnRate(String custCallErnRate) {
        this.custCallErnRate = custCallErnRate;
    }
}
