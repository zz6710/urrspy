package com.kayak.rpt.zz.historyInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "practyRegistInfohService",table = "app_practy_regist_info_h")
public class PractyRegistInfoh {
   @GraphQLField(kkhtml = "KFieldText", label = "从业人员类型", sql = "profession = $S{profession}" ,field = "profession")
   private String profession;
   @GraphQLField(kkhtml = "KFieldText", label = "姓名", sql = "name = $S{name}" ,field = "name")
   private String name;
   @GraphQLField(kkhtml = "KFieldText", label = "性别", sql = "sex = $S{sex}" ,field = "sex")
   private String sex;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "身份证号", sql = "id_code like '%$U{idCode}%'" ,field = "id_code")
   private String idCode;
   @GraphQLField(kkhtml = "KFieldText", label = "所属总行或分行", sql = "iss_branch_type = $S{issBranchType}" ,field = "iss_branch_type")
   private String issBranchType;
   @GraphQLField(kkhtml = "KFieldText", label = "所属区域", sql = "region = $S{region}" ,field = "region")
   private String region;
   @GraphQLField(kkhtml = "KFieldText", label = "具体单位名称", sql = "firm_name = $S{firmName}" ,field = "firm_name")
   private String firmName;
   @GraphQLField(kkhtml = "KFieldText", label = "所属部门", sql = "department = $S{department}" ,field = "department")
   private String department;
   @GraphQLField(kkhtml = "KFieldText", label = "职位", sql = "post = $S{post}" ,field = "post")
   private String post;
   @GraphQLField(kkhtml = "KFieldText", label = "学历", sql = "education = $S{education}" ,field = "education")
   private String education;
   @GraphQLField(kkhtml = "KFieldText", label = "学位", sql = "degree = $S{degree}" ,field = "degree")
   private String degree;
   @GraphQLField(kkhtml = "KFieldText", label = "首次参加工作时间", sql = "career_start_date = $S{careerStartDate}" ,field = "career_start_date")
   private String careerStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "首次从事理财业务时间", sql = "wealth_start_date = $S{wealthStartDate}" ,field = "wealth_start_date")
   private String wealthStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "专业技术职称", sql = "profess_qualy_level = $S{professQualyLevel}" ,field = "profess_qualy_level")
   private String professQualyLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "理财专业证书", sql = "wealth_cer = $S{wealthCer}" ,field = "wealth_cer")
   private String wealthCer;
   @GraphQLField(kkhtml = "KFieldText", label = "理财登记培训证书编号", sql = "regist_cer_no = $S{registCerNo}" ,field = "regist_cer_no")
   private String registCerNo;
   @GraphQLField(kkhtml = "KFieldText", label = "所获奖励", sql = "reward = $S{reward}" ,field = "reward")
   private String reward;
   @GraphQLField(kkhtml = "KFieldText", label = "办公电话", sql = "telphone = $S{telphone}" ,field = "telphone")
   private String telphone;
   @GraphQLField(kkhtml = "KFieldText", label = "移动电话", sql = "mobile = $S{mobile}" ,field = "mobile")
   private String mobile;
   @GraphQLField(kkhtml = "KFieldText", label = "电子邮箱", sql = "email = $S{email}" ,field = "email")
   private String email;
   @GraphQLField(kkhtml = "KFieldText", label = "登记业务分类", sql = "register_classify = $S{registerClassify}" ,field = "register_classify")
   private String registerClassify;
   @GraphQLField(kkhtml = "KFieldText", label = "登记人员类别", sql = "regist_type = $S{registType}" ,field = "regist_type")
   private String registType;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
    @GraphQLField(label = "导入日期开始时间")
    private String startDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(label = "导入日期截止时间")
    private String endDate;
  	public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
  	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
  	public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
  	public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
  	public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }
  	public String getIssBranchType() {
        return issBranchType;
    }

    public void setIssBranchType(String issBranchType) {
        this.issBranchType = issBranchType;
    }
  	public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
  	public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
  	public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
  	public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
  	public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
  	public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
  	public String getCareerStartDate() {
        return careerStartDate;
    }

    public void setCareerStartDate(String careerStartDate) {
        this.careerStartDate = careerStartDate;
    }
  	public String getWealthStartDate() {
        return wealthStartDate;
    }

    public void setWealthStartDate(String wealthStartDate) {
        this.wealthStartDate = wealthStartDate;
    }
  	public String getProfessQualyLevel() {
        return professQualyLevel;
    }

    public void setProfessQualyLevel(String professQualyLevel) {
        this.professQualyLevel = professQualyLevel;
    }
  	public String getWealthCer() {
        return wealthCer;
    }

    public void setWealthCer(String wealthCer) {
        this.wealthCer = wealthCer;
    }
  	public String getRegistCerNo() {
        return registCerNo;
    }

    public void setRegistCerNo(String registCerNo) {
        this.registCerNo = registCerNo;
    }
  	public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
  	public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
  	public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
  	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
  	public String getRegisterClassify() {
        return registerClassify;
    }

    public void setRegisterClassify(String registerClassify) {
        this.registerClassify = registerClassify;
    }
  	public String getRegistType() {
        return registType;
    }

    public void setRegistType(String registType) {
        this.registType = registType;
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

    public String getStartDate() {
        return startDate;
    }
}