package com.kayak.rpt.zz.historyInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "initialSubRegistInfohService",table = "app_initial_sub_regist_info_h")
public class InitialSubRegistInfoh {
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "个人投资者总数", sql = "number_indiv_invest = $S{numberIndivInvest}" ,field = "number_indiv_invest")
   private String numberIndivInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "法人投资者总数", sql = "number_corpor_invest = $S{numberCorporInvest}" ,field = "number_corpor_invest")
   private String numberCorporInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "非法人投资者总数", sql = "number_ucor_invest = $S{numberUcorInvest}" ,field = "number_ucor_invest")
   private String numberUcorInvest;
   @GraphQLField(kkhtml = "KFieldText", label = "是否有其他机构代销", sql = "other_distribut_agents = $S{otherDistributAgents}" ,field = "other_distribut_agents")
   private String otherDistributAgents;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "实际募集金额", sql = "actual_subscribed_amt = $S{actualSubscribedAmt}" ,field = "actual_subscribed_amt")
   private String actualSubscribedAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "募集总份额", sql = "subscribed_vol = $S{subscribedVol}" ,field = "subscribed_vol")
   private String subscribedVol;
   @GraphQLField(kkhtml = "KFieldText", label = "代销总金额", sql = "amt_other_db_agents = $S{amtOtherDbAgents}" ,field = "amt_other_db_agents")
   private String amtOtherDbAgents;
    @GraphQLField(kkhtml = "KFieldText")
    private String fndTrstActNbr;
    @GraphQLField(kkhtml = "KFieldText")
    private String navDt;
    @GraphQLField(kkhtml = "KFieldText", label = "区域募集金额")
    private String zonClcAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "产品币种")
    private String prodCcy;
    @GraphQLField(kkhtml = "KFieldText", label = "成立日期")
    private String foundDt;
    @GraphQLField(kkhtml = "KFieldText")
    private String fndTrstAct;
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(label = "导入日期开始时间")
    private String startDate;

    @GraphQLField(label = "导入日期截止时间")
    private String endDate;
  	public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
  	public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }
  	public String getNumberIndivInvest() {
        return numberIndivInvest;
    }

    public void setNumberIndivInvest(String numberIndivInvest) {
        this.numberIndivInvest = numberIndivInvest;
    }
  	public String getNumberCorporInvest() {
        return numberCorporInvest;
    }

    public void setNumberCorporInvest(String numberCorporInvest) {
        this.numberCorporInvest = numberCorporInvest;
    }
  	public String getNumberUcorInvest() {
        return numberUcorInvest;
    }

    public void setNumberUcorInvest(String numberUcorInvest) {
        this.numberUcorInvest = numberUcorInvest;
    }
  	public String getOtherDistributAgents() {
        return otherDistributAgents;
    }

    public void setOtherDistributAgents(String otherDistributAgents) {
        this.otherDistributAgents = otherDistributAgents;
    }
  	public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
  	public String getActualSubscribedAmt() {
        return actualSubscribedAmt;
    }

    public void setActualSubscribedAmt(String actualSubscribedAmt) {
        this.actualSubscribedAmt = actualSubscribedAmt;
    }
  	public String getSubscribedVol() {
        return subscribedVol;
    }

    public void setSubscribedVol(String subscribedVol) {
        this.subscribedVol = subscribedVol;
    }
  	public String getAmtOtherDbAgents() {
        return amtOtherDbAgents;
    }

    public void setAmtOtherDbAgents(String amtOtherDbAgents) {
        this.amtOtherDbAgents = amtOtherDbAgents;
    }

    public String getStartDate() {
        return startDate;
    }
}