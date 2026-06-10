package com.kayak.rpt.zz.operate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "custRegistMarkService",table = "app_cust_register_remark")
public class CustRegistMark {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "操作用户", sql = "summit_user  like '%$U{summitUser}%'" ,field = "summit_user")
   private String summitUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
   private String createTime;
   @GraphQLField(kkhtml = "KFieldText", label = "登记银行代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "数据类型", sql = "data_type = $S{dataType}" ,field = "data_type")
   private String dataType;
   @GraphQLField(kkhtml = "KFieldText", label = "识别标识", sql = "cust_no like '%$U{custNo}%'" ,field = "cust_no")
   private String custNo;
   @GraphQLField(kkhtml = "KFieldText", label = "原识别标识", sql = "ori_cust_no = $S{oriCustNo}" ,field = "ori_cust_no")
   private String oriCustNo;
   @GraphQLField(kkhtml = "KFieldText", label = "该投资者是否属于本机构", sql = "is_belong = $S{isBelong}" ,field = "is_belong")
   private String isBelong;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者所属机构名称", sql = "iss_bank_name = $S{issBankName}" ,field = "iss_bank_name")
   private String issBankName;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者所属机构代码", sql = "iss_bank_code = $S{issBankCode}" ,field = "iss_bank_code")
   private String issBankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者境内外标识", sql = "in_out_sign = $S{inOutSign}" ,field = "in_out_sign")
   private String inOutSign;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者所属国家或地区", sql = "iss_country = $S{issCountry}" ,field = "iss_country")
   private String issCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者类别", sql = "cust_type = $S{custType}" ,field = "cust_type")
   private String custType;
   @GraphQLField(kkhtml = "KFieldText", label = "个人证件类别", sql = "personal_id_type = $S{personalIdType}" ,field = "personal_id_type")
   private String personalIdType;
   @GraphQLField(kkhtml = "KFieldText", label = "机构证件类别", sql = "organization_id_type = $S{organizationIdType}" ,field = "organization_id_type")
   private String organizationIdType;
   @GraphQLField(kkhtml = "KFieldText", label = "其他证件名称", sql = "other_id_name = $S{otherIdName}" ,field = "other_id_name")
   private String otherIdName;
   @GraphQLField(kkhtml = "KFieldText", label = "证件号码", sql = "id_code like '%$U{idCode}%'" ,field = "id_code")
   private String idCode;
   @GraphQLField(kkhtml = "KFieldText", label = "SPV资金托管账户开户行", sql = "spv_open_bank = $S{spvOpenBank}" ,field = "spv_open_bank")
   private String spvOpenBank;
   @GraphQLField(kkhtml = "KFieldText", label = "其他资金托管账户开户行", sql = "other_open_bank = $S{otherOpenBank}" ,field = "other_open_bank")
   private String otherOpenBank;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者名称", sql = "cust_name like '%$U{custName}%'"  ,field = "cust_name")
   private String custName;
   @GraphQLField(kkhtml = "KFieldText", label = "性别", sql = "sex = $S{sex}" ,field = "sex")
   private String sex;
   @GraphQLField(kkhtml = "KFieldText", label = "风险偏好", sql = "risk_level = $S{riskLevel}" ,field = "risk_level")
   private String riskLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "手机号码", sql = "moble = $S{moble}" ,field = "moble")
   private String moble;
   @GraphQLField(kkhtml = "KFieldText", label = "固定电话", sql = "tel_phone = $S{telPhone}" ,field = "tel_phone")
   private String telPhone;
   @GraphQLField(kkhtml = "KFieldText", label = "电子邮箱", sql = "email = $S{email}" ,field = "email")
   private String email;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记账号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "数据操作类型（D", sql = "op_type = $S{opType}" ,field = "op_type")
   private String opType;
    @GraphQLField(label = "开始时间")
    private String startDate;

    @GraphQLField(label = "结束时间")
    private String endDate;

    @GraphQLField(kkhtml = "KFieldText",  label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;

    @GraphQLField(kkhtml = "KFieldText", label = "TA_ID", sql = "ta_id = $S{taId}" ,field = "taId")
    private String taId;

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
  	public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
  	public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
  	public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }
  	public String getOriCustNo() {
        return oriCustNo;
    }

    public void setOriCustNo(String oriCustNo) {
        this.oriCustNo = oriCustNo;
    }
  	public String getIsBelong() {
        return isBelong;
    }

    public void setIsBelong(String isBelong) {
        this.isBelong = isBelong;
    }
  	public String getIssBankName() {
        return issBankName;
    }

    public void setIssBankName(String issBankName) {
        this.issBankName = issBankName;
    }
  	public String getIssBankCode() {
        return issBankCode;
    }

    public void setIssBankCode(String issBankCode) {
        this.issBankCode = issBankCode;
    }
  	public String getInOutSign() {
        return inOutSign;
    }

    public void setInOutSign(String inOutSign) {
        this.inOutSign = inOutSign;
    }
  	public String getIssCountry() {
        return issCountry;
    }

    public void setIssCountry(String issCountry) {
        this.issCountry = issCountry;
    }
  	public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }
  	public String getPersonalIdType() {
        return personalIdType;
    }

    public void setPersonalIdType(String personalIdType) {
        this.personalIdType = personalIdType;
    }
  	public String getOrganizationIdType() {
        return organizationIdType;
    }

    public void setOrganizationIdType(String organizationIdType) {
        this.organizationIdType = organizationIdType;
    }
  	public String getOtherIdName() {
        return otherIdName;
    }

    public void setOtherIdName(String otherIdName) {
        this.otherIdName = otherIdName;
    }
  	public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }
  	public String getSpvOpenBank() {
        return spvOpenBank;
    }

    public void setSpvOpenBank(String spvOpenBank) {
        this.spvOpenBank = spvOpenBank;
    }
  	public String getOtherOpenBank() {
        return otherOpenBank;
    }

    public void setOtherOpenBank(String otherOpenBank) {
        this.otherOpenBank = otherOpenBank;
    }
  	public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
  	public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
  	public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
  	public String getMoble() {
        return moble;
    }

    public void setMoble(String moble) {
        this.moble = moble;
    }
  	public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }
  	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
  	public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
  	public String getRegisterSerno() {
        return registerSerno;
    }

    public void setRegisterSerno(String registerSerno) {
        this.registerSerno = registerSerno;
    }
  	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
  	public String getImpDate() {
        return impDate;
    }

    public void setImpDate(String impDate) {
        this.impDate = impDate;
    }
  	public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }
  	public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getTaId() {
        return taId;
    }

    public void setTaId(String taId) {
        this.taId = taId;
    }
}