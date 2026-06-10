package com.kayak.rpt.zz.operate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "appraiseRegistService",table = "app_appraise_regist_remark")
public class AppraiseRegist {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "行内资产负债编码", sql = "asset_code like '%$U{assetCode}%'" ,field = "asset_code")
   private String assetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "估值日期", sql = "valuation_date = $S{valuationDate}" ,field = "valuation_date")
   private String valuationDate;
   @GraphQLField(kkhtml = "KFieldText", label = "单位估值", sql = "unit_debt_net = $S{unitDebtNet}" ,field = "unit_debt_net")
   private String unitDebtNet;
   @GraphQLField(kkhtml = "KFieldText", label = "单位估值", sql = "unit_debt_full = $S{unitDebtFull}" ,field = "unit_debt_full")
   private String unitDebtFull;
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
   @GraphQLField(kkhtml = "KFieldText", label = "操作人员", sql = "summit_user LIKE '%$U{summitUser}%'",field = "summit_user")
   private String summitUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "数据操作类型（D", sql = "op_type = $S{opType}" ,field = "op_type")
   private String opType;
    @GraphQLField(label = "开始时间")
    private String startDate;

    @GraphQLField(label = "结束时间")
    private String endDate;
    @GraphQLField(label = "开始时间")
    private String valuationStartDate;

    @GraphQLField(label = "结束时间")
    private String valuationEndDate;
  	public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
  	public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
  	public String getValuationDate() {
        return valuationDate;
    }

    public void setValuationDate(String valuationDate) {
        this.valuationDate = valuationDate;
    }
  	public String getUnitDebtNet() {
        return unitDebtNet;
    }

    public void setUnitDebtNet(String unitDebtNet) {
        this.unitDebtNet = unitDebtNet;
    }
  	public String getUnitDebtFull() {
        return unitDebtFull;
    }

    public void setUnitDebtFull(String unitDebtFull) {
        this.unitDebtFull = unitDebtFull;
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