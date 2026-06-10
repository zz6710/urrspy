package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "custRegisterInfoErrService",table = "app_cust_register_info_erdesc")
public class CustRegisterInfoErr {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "登记银行代码错误描述", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "该权益人是否属于本行错误描述", sql = "is_belong_desc = $S{isBelongDesc}" ,field = "is_belong_desc")
   private String isBelongDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "权益人所属银行名称错误描述", sql = "iss_bank_name_desc = $S{issBankNameDesc}" ,field = "iss_bank_name_desc")
   private String issBankNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "权益人所属银行代码错误描述", sql = "iss_bank_code_desc = $S{issBankCodeDesc}" ,field = "iss_bank_code_desc")
   private String issBankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "权益人境内外标识错误描述", sql = "in_out_sign_desc = $S{inOutSignDesc}" ,field = "in_out_sign_desc")
   private String inOutSignDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "权益人所属国家或地区错误描述", sql = "iss_country_desc = $S{issCountryDesc}" ,field = "iss_country_desc")
   private String issCountryDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "数据类型错误描述", sql = "data_type_desc = $S{dataTypeDesc}" ,field = "data_type_desc")
   private String dataTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "原识别标识错误描述", sql = "ori_cust_no_desc = $S{oriCustNoDesc}" ,field = "ori_cust_no_desc")
   private String oriCustNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "识别标识错误描述", sql = "cust_no_desc = $S{custNoDesc}" ,field = "cust_no_desc")
   private String custNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "权益人类别错误描述", sql = "cust_type_desc = $S{custTypeDesc}" ,field = "cust_type_desc")
   private String custTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "个人证件类别错误描述", sql = "personal_id_type_desc = $S{personalIdTypeDesc}" ,field = "personal_id_type_desc")
   private String personalIdTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "机构证件类别错误描述", sql = "organization_id_type_desc = $S{organizationIdTypeDesc}" ,field = "organization_id_type_desc")
   private String organizationIdTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他证件名称错误描述", sql = "other_id_name_desc = $S{otherIdNameDesc}" ,field = "other_id_name_desc")
   private String otherIdNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "证件号码错误描述", sql = "id_code_desc = $S{idCodeDesc}" ,field = "id_code_desc")
   private String idCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "SPV资金托管账户开户行错误描述", sql = "spv_open_bank_desc = $S{spvOpenBankDesc}" ,field = "spv_open_bank_desc")
   private String spvOpenBankDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他资金托管账户开户行错误描述", sql = "other_open_bank_desc = $S{otherOpenBankDesc}" ,field = "other_open_bank_desc")
   private String otherOpenBankDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "权益人名称错误描述", sql = "cust_name_desc = $S{custNameDesc}" ,field = "cust_name_desc")
   private String custNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "性别错误描述", sql = "sex_desc = $S{sexDesc}" ,field = "sex_desc")
   private String sexDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "风险偏好错误描述", sql = "risk_level_desc = $S{riskLevelDesc}" ,field = "risk_level_desc")
   private String riskLevelDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "手机号码错误描述", sql = "moble_desc = $S{mobleDesc}" ,field = "moble_desc")
   private String mobleDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "固定电话错误描述", sql = "tel_phone_desc = $S{telPhoneDesc}" ,field = "tel_phone_desc")
   private String telPhoneDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "电子邮箱错误描述", sql = "email_desc = $S{emailDesc}" ,field = "email_desc")
   private String emailDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "降序备注", sql = "remark_desc = $S{remarkDesc}" ,field = "remark_desc")
   private String remarkDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(label = "导入日期开始时间")
    private String impStartDate;

    @GraphQLField(label = "导入日期截止时间")
    private String impEndDate;


    @GraphQLField(kkhtml = "KFieldText", label = "报告日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;

  	public String getRegisterSerno() {
        return registerSerno;
    }

    public void setRegisterSerno(String registerSerno) {
        this.registerSerno = registerSerno;
    }
  	public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }
  	public String getIsBelongDesc() {
        return isBelongDesc;
    }

    public void setIsBelongDesc(String isBelongDesc) {
        this.isBelongDesc = isBelongDesc;
    }
  	public String getIssBankNameDesc() {
        return issBankNameDesc;
    }

    public void setIssBankNameDesc(String issBankNameDesc) {
        this.issBankNameDesc = issBankNameDesc;
    }
  	public String getIssBankCodeDesc() {
        return issBankCodeDesc;
    }

    public void setIssBankCodeDesc(String issBankCodeDesc) {
        this.issBankCodeDesc = issBankCodeDesc;
    }
  	public String getInOutSignDesc() {
        return inOutSignDesc;
    }

    public void setInOutSignDesc(String inOutSignDesc) {
        this.inOutSignDesc = inOutSignDesc;
    }
  	public String getIssCountryDesc() {
        return issCountryDesc;
    }

    public void setIssCountryDesc(String issCountryDesc) {
        this.issCountryDesc = issCountryDesc;
    }
  	public String getDataTypeDesc() {
        return dataTypeDesc;
    }

    public void setDataTypeDesc(String dataTypeDesc) {
        this.dataTypeDesc = dataTypeDesc;
    }
  	public String getOriCustNoDesc() {
        return oriCustNoDesc;
    }

    public void setOriCustNoDesc(String oriCustNoDesc) {
        this.oriCustNoDesc = oriCustNoDesc;
    }
  	public String getCustNoDesc() {
        return custNoDesc;
    }

    public void setCustNoDesc(String custNoDesc) {
        this.custNoDesc = custNoDesc;
    }
  	public String getCustTypeDesc() {
        return custTypeDesc;
    }

    public void setCustTypeDesc(String custTypeDesc) {
        this.custTypeDesc = custTypeDesc;
    }
  	public String getPersonalIdTypeDesc() {
        return personalIdTypeDesc;
    }

    public void setPersonalIdTypeDesc(String personalIdTypeDesc) {
        this.personalIdTypeDesc = personalIdTypeDesc;
    }
  	public String getOrganizationIdTypeDesc() {
        return organizationIdTypeDesc;
    }

    public void setOrganizationIdTypeDesc(String organizationIdTypeDesc) {
        this.organizationIdTypeDesc = organizationIdTypeDesc;
    }
  	public String getOtherIdNameDesc() {
        return otherIdNameDesc;
    }

    public void setOtherIdNameDesc(String otherIdNameDesc) {
        this.otherIdNameDesc = otherIdNameDesc;
    }
  	public String getIdCodeDesc() {
        return idCodeDesc;
    }

    public void setIdCodeDesc(String idCodeDesc) {
        this.idCodeDesc = idCodeDesc;
    }
  	public String getSpvOpenBankDesc() {
        return spvOpenBankDesc;
    }

    public void setSpvOpenBankDesc(String spvOpenBankDesc) {
        this.spvOpenBankDesc = spvOpenBankDesc;
    }
  	public String getOtherOpenBankDesc() {
        return otherOpenBankDesc;
    }

    public void setOtherOpenBankDesc(String otherOpenBankDesc) {
        this.otherOpenBankDesc = otherOpenBankDesc;
    }
  	public String getCustNameDesc() {
        return custNameDesc;
    }

    public void setCustNameDesc(String custNameDesc) {
        this.custNameDesc = custNameDesc;
    }
  	public String getSexDesc() {
        return sexDesc;
    }

    public void setSexDesc(String sexDesc) {
        this.sexDesc = sexDesc;
    }
  	public String getRiskLevelDesc() {
        return riskLevelDesc;
    }

    public void setRiskLevelDesc(String riskLevelDesc) {
        this.riskLevelDesc = riskLevelDesc;
    }
  	public String getMobleDesc() {
        return mobleDesc;
    }

    public void setMobleDesc(String mobleDesc) {
        this.mobleDesc = mobleDesc;
    }
  	public String getTelPhoneDesc() {
        return telPhoneDesc;
    }

    public void setTelPhoneDesc(String telPhoneDesc) {
        this.telPhoneDesc = telPhoneDesc;
    }
  	public String getEmailDesc() {
        return emailDesc;
    }

    public void setEmailDesc(String emailDesc) {
        this.emailDesc = emailDesc;
    }
  	public String getImpDate() {
        return impDate;
    }

    public void setImpDate(String impDate) {
        this.impDate = impDate;
    }
  	public String getRemarkDesc() {
        return remarkDesc;
    }

    public void setRemarkDesc(String remarkDesc) {
        this.remarkDesc = remarkDesc;
    }


}