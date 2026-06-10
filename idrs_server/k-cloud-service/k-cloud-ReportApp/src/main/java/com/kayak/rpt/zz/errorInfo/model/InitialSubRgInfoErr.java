package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "initialSubRgInfoErrService",table = "app_initial_sub_regist_info_erdesc")
public class InitialSubRgInfoErr {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码错误", sql = "prod_code_desc = $S{prodCodeDesc}" ,field = "prod_code_desc")
   private String prodCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "个人投资者总数错误", sql = "number_indiv_invest_desc = $S{numberIndivInvestDesc}" ,field = "number_indiv_invest_desc")
   private String numberIndivInvestDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "法人投资者总数错误", sql = "number_corpor_invest_desc = $S{numberCorporInvestDesc}" ,field = "number_corpor_invest_desc")
   private String numberCorporInvestDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "非法人投资者总数错误", sql = "number_ucor_invest_desc = $S{numberUcorInvestDesc}" ,field = "number_ucor_invest_desc")
   private String numberUcorInvestDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "认购币种错误", sql = "subscript_cur_desc = $S{subscriptCurDesc}" ,field = "subscript_cur_desc")
   private String subscriptCurDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "折算人民币错误", sql = "convert_rmb_desc = $S{convertRmbDesc}" ,field = "convert_rmb_desc")
   private String convertRmbDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品销售区域错误", sql = "prod_sales_region_desc = $S{prodSalesRegionDesc}" ,field = "prod_sales_region_desc")
   private String prodSalesRegionDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有其他机构代销错误", sql = "other_distribut_agents_desc = $S{otherDistributAgentsDesc}" ,field = "other_distribut_agents_desc")
   private String otherDistributAgentsDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "认购金额错误", sql = "subscript_amt_desc = $S{subscriptAmtDesc}" ,field = "subscript_amt_desc")
   private String subscriptAmtDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "区域募集金额错误", sql = "subscript_amt_region_desc = $S{subscriptAmtRegionDesc}" ,field = "subscript_amt_region_desc")
   private String subscriptAmtRegionDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "实际募集金额错误", sql = "actual_subscribed_amt_desc = $S{actualSubscribedAmtDesc}" ,field = "actual_subscribed_amt_desc")
   private String actualSubscribedAmtDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "募集总份额错误", sql = "subscribed_vol_desc = $S{subscribedVolDesc}" ,field = "subscribed_vol_desc")
   private String subscribedVolDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "代销总金额错误", sql = "amt_other_db_agents_desc = $S{amtOtherDbAgentsDesc}" ,field = "amt_other_db_agents_desc")
   private String amtOtherDbAgentsDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "备注错误", sql = "details_desc = $S{detailsDesc}" ,field = "details_desc")
   private String detailsDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
    @GraphQLField(label = "导入日期开始时间")
    private String impStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(label = "导入日期截止时间")
    private String impEndDate;

    @GraphQLField(kkhtml = "KFieldText", label = "报告日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;

  	public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }
  	public String getProdCodeDesc() {
        return prodCodeDesc;
    }

    public void setProdCodeDesc(String prodCodeDesc) {
        this.prodCodeDesc = prodCodeDesc;
    }
  	public String getNumberIndivInvestDesc() {
        return numberIndivInvestDesc;
    }

    public void setNumberIndivInvestDesc(String numberIndivInvestDesc) {
        this.numberIndivInvestDesc = numberIndivInvestDesc;
    }
  	public String getNumberCorporInvestDesc() {
        return numberCorporInvestDesc;
    }

    public void setNumberCorporInvestDesc(String numberCorporInvestDesc) {
        this.numberCorporInvestDesc = numberCorporInvestDesc;
    }
  	public String getNumberUcorInvestDesc() {
        return numberUcorInvestDesc;
    }

    public void setNumberUcorInvestDesc(String numberUcorInvestDesc) {
        this.numberUcorInvestDesc = numberUcorInvestDesc;
    }
  	public String getSubscriptCurDesc() {
        return subscriptCurDesc;
    }

    public void setSubscriptCurDesc(String subscriptCurDesc) {
        this.subscriptCurDesc = subscriptCurDesc;
    }
  	public String getConvertRmbDesc() {
        return convertRmbDesc;
    }

    public void setConvertRmbDesc(String convertRmbDesc) {
        this.convertRmbDesc = convertRmbDesc;
    }
  	public String getProdSalesRegionDesc() {
        return prodSalesRegionDesc;
    }

    public void setProdSalesRegionDesc(String prodSalesRegionDesc) {
        this.prodSalesRegionDesc = prodSalesRegionDesc;
    }
  	public String getOtherDistributAgentsDesc() {
        return otherDistributAgentsDesc;
    }

    public void setOtherDistributAgentsDesc(String otherDistributAgentsDesc) {
        this.otherDistributAgentsDesc = otherDistributAgentsDesc;
    }
  	public String getSubscriptAmtDesc() {
        return subscriptAmtDesc;
    }

    public void setSubscriptAmtDesc(String subscriptAmtDesc) {
        this.subscriptAmtDesc = subscriptAmtDesc;
    }
  	public String getSubscriptAmtRegionDesc() {
        return subscriptAmtRegionDesc;
    }

    public void setSubscriptAmtRegionDesc(String subscriptAmtRegionDesc) {
        this.subscriptAmtRegionDesc = subscriptAmtRegionDesc;
    }
  	public String getActualSubscribedAmtDesc() {
        return actualSubscribedAmtDesc;
    }

    public void setActualSubscribedAmtDesc(String actualSubscribedAmtDesc) {
        this.actualSubscribedAmtDesc = actualSubscribedAmtDesc;
    }
  	public String getSubscribedVolDesc() {
        return subscribedVolDesc;
    }

    public void setSubscribedVolDesc(String subscribedVolDesc) {
        this.subscribedVolDesc = subscribedVolDesc;
    }
  	public String getAmtOtherDbAgentsDesc() {
        return amtOtherDbAgentsDesc;
    }

    public void setAmtOtherDbAgentsDesc(String amtOtherDbAgentsDesc) {
        this.amtOtherDbAgentsDesc = amtOtherDbAgentsDesc;
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