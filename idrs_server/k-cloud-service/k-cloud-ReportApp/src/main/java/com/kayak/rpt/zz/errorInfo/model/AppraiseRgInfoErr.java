package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "appraiseRgInfoErrService",table = "app_appraise_regist_info_erdesc")
public class AppraiseRgInfoErr {
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "行内资产负债编码错误", sql = "asset_code_desc = $S{assetCodeDesc}" ,field = "asset_code_desc")
   private String assetCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "估值日期错误", sql = "valuation_date_desc = $S{valuationDateDesc}" ,field = "valuation_date_desc")
   private String valuationDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "单位估值", sql = "unit_debt_net_desc = $S{unitDebtNetDesc}" ,field = "unit_debt_net_desc")
   private String unitDebtNetDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "单位估值", sql = "unit_debt_full_desc = $S{unitDebtFullDesc}" ,field = "unit_debt_full_desc")
   private String unitDebtFullDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "备注错误", sql = "details_desc = $S{detailsDesc}" ,field = "details_desc")
   private String detailsDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
    @GraphQLField(label = "导入日期开始时间")
    private String impStartDate;
    @GraphQLField(label = "id")
    private String id;
    @GraphQLField(label = "导入日期截止时间")
    private String impEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;


    @GraphQLField(kkhtml = "KFieldText", label = "报告日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;

    public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }
  	public String getAssetCodeDesc() {
        return assetCodeDesc;
    }

    public void setAssetCodeDesc(String assetCodeDesc) {
        this.assetCodeDesc = assetCodeDesc;
    }
  	public String getValuationDateDesc() {
        return valuationDateDesc;
    }

    public void setValuationDateDesc(String valuationDateDesc) {
        this.valuationDateDesc = valuationDateDesc;
    }
  	public String getUnitDebtNetDesc() {
        return unitDebtNetDesc;
    }

    public void setUnitDebtNetDesc(String unitDebtNetDesc) {
        this.unitDebtNetDesc = unitDebtNetDesc;
    }
  	public String getUnitDebtFullDesc() {
        return unitDebtFullDesc;
    }

    public void setUnitDebtFullDesc(String unitDebtFullDesc) {
        this.unitDebtFullDesc = unitDebtFullDesc;
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

}