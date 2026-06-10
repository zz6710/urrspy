package com.kayak.rpt.zz.operate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "initialSubRegistService",table = "app_initial_sub_regist_remark")
public class InitialSubRegist {
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_code like '%$U{prodCode}%'" ,field = "prod_code")
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
   @GraphQLField(kkhtml = "KFieldText", label = "操作人员", sql = "summit_user like '%$U{summitUser}%'" ,field = "summit_user")
   private String summitUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "数据操作类型（D", sql = "op_type = $S{opType}" ,field = "op_type")
   private String opType;
   @GraphQLField(kkhtml = "KFieldText", label = "区域募集金额", sql = "zon_clc_amt = $S{zonClcAmt}" ,field = "zon_clc_amt")
   private String zonClcAmt;
    @GraphQLField(label = "开始时间")
    private String startDate;
    @GraphQLField(label = "id")
    private String id;
    @GraphQLField(label = "结束时间")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText")
    private String fndTrstActNbr;
    @GraphQLField(kkhtml = "KFieldText")
    private String fndTrstAct;

    @GraphQLField(kkhtml = "KFieldText")
    private String reportDate;

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
  	public String getSummitUser() {
        return summitUser;
    }

    public void setSummitUser(String summitUser) {
        this.summitUser = summitUser;
    }
  	public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
  	public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
  	public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

}