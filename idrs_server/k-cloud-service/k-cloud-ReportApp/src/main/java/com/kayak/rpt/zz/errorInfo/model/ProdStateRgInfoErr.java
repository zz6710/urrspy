package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodStateRgInfoErrService",table = "app_prod_state_regist_info_erdesc")
public class ProdStateRgInfoErr {
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
    private String bankCodeDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码错误", sql = "prod_reg_enc_desc = $S{prodRegEncDesc}" ,field = "prod_reg_enc_desc")
    private String prodRegEncDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "理财产品总资产金额错误", sql = "tot_assets_desc = $S{totAssetsDesc}" ,field = "tot_assets_desc")
    private String totAssetsDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "理财产品杠杆率错误", sql = "rate_desc = $S{rateDesc}" ,field = "rate_desc")
    private String rateDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "产品状态统计日错误", sql = "valdate_desc = $S{valdateDesc}" ,field = "valdate_desc")
    private String valdateDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details_desc = $S{detailsDesc}" ,field = "detailsDesc")
    private String detailsDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
    private String registerSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
    private String impDate;
    @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
    private String registerDate;
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(label = "开始时间")
    private String startDate;

    @GraphQLField(label = "结束时间")
    private String endDate;

    public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdRegEncDesc() {
        return prodRegEncDesc;
    }

    public void setProdRegEncDesc(String prodRegEncDesc) {
        this.prodRegEncDesc = prodRegEncDesc;
    }

    public String getTotAssetsDesc() {
        return totAssetsDesc;
    }

    public void setTotAssetsDesc(String totAssetsDesc) {
        this.totAssetsDesc = totAssetsDesc;
    }

    public String getRateDesc() {
        return rateDesc;
    }

    public void setRateDesc(String rateDesc) {
        this.rateDesc = rateDesc;
    }

    public String getValdateDesc() {
        return valdateDesc;
    }

    public void setValdateDesc(String valdateDesc) {
        this.valdateDesc = valdateDesc;
    }

    public String getDetailsDesc() {
        return detailsDesc;
    }

    public void setDetailsDesc(String detailsDesc) {
        this.detailsDesc = detailsDesc;
    }

    public String getRegisterSerno() {
        return registerSerno;
    }

    public void setRegisterSerno(String registerSerno) {
        this.registerSerno = registerSerno;
    }

    public String getImpDate() {
        return impDate;
    }

    public void setImpDate(String impDate) {
        this.impDate = impDate;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}