package com.kayak.pms.T82.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;



@GraphQLModel(fetcher = "t8BranchBankService",table = "T8_branch_bank")
public class T8BranchBank {
  @GraphQLField(field = "id")
  private String id;
  @GraphQLField(field = "prod_code",label = "产品名称", kkhtmlDefault = true)
  private String prodCode;
  @GraphQLField(field = "branch_name",label = "分行名称", kkhtmlDefault = true)
  private String branchName;
  @GraphQLField(field = "branch_quota",label = "分行额度", kkhtmlDefault = true)
  private String branchQuota;
  @GraphQLField(field = "inputuser")
  private String inputuser;
  @GraphQLField(field = "updateuser")
  private String updateuser;
  @GraphQLField(field = "crt_date", kkhtmlDefault = true)
  private String crtDate;
  @GraphQLField(field = "crt_time", kkhtmlDefault = true)
  private String crtTime;
  @GraphQLField(field = "upt_date")
  private String uptDate;
  @GraphQLField(field = "upt_time")
  private String uptTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProdCode() {
    return prodCode;
  }

  public void setProdCode(String prodCode) {
    this.prodCode = prodCode;
  }

  public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  public String getBranchQuota() {
    return branchQuota;
  }

  public void setBranchQuota(String branchQuota) {
    this.branchQuota = branchQuota;
  }

  public String getInputuser() {
    return inputuser;
  }

  public void setInputuser(String inputuser) {
    this.inputuser = inputuser;
  }

  public String getUpdateuser() {
    return updateuser;
  }

  public void setUpdateuser(String updateuser) {
    this.updateuser = updateuser;
  }

  public String getCrtDate() {
    return crtDate;
  }

  public void setCrtDate(String crtDate) {
    this.crtDate = crtDate;
  }

  public String getCrtTime() {
    return crtTime;
  }

  public void setCrtTime(String crtTime) {
    this.crtTime = crtTime;
  }

  public String getUptDate() {
    return uptDate;
  }

  public void setUptDate(String uptDate) {
    this.uptDate = uptDate;
  }

  public String getUptTime() {
    return uptTime;
  }

  public void setUptTime(String uptTime) {
    this.uptTime = uptTime;
  }

}
