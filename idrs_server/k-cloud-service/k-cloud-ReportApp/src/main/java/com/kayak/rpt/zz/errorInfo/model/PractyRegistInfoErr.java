package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "practyRegistInfoErrService",table = "app_practy_regist_info_erdesc")
public class PractyRegistInfoErr {
    @GraphQLField(label = "id")
    private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "从业人员类型错误", sql = "profession_desc = $S{professionDesc}" ,field = "profession_desc")
   private String professionDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "姓名错误", sql = "name_desc = $S{nameDesc}" ,field = "name_desc")
   private String nameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "性别错误", sql = "sex_desc = $S{sexDesc}" ,field = "sex_desc")
   private String sexDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误", sql = "bank_code_desc = $S{bankCodeDesc}" ,field = "bank_code_desc")
   private String bankCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "身份证号错误", sql = "id_code_desc = $S{idCodeDesc}" ,field = "id_code_desc")
   private String idCodeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "所属总行或分行错误", sql = "iss_branch_type_desc = $S{issBranchTypeDesc}" ,field = "iss_branch_type_desc")
   private String issBranchTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "所属区域错误", sql = "region_desc = $S{regionDesc}" ,field = "region_desc")
   private String regionDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "具体单位名称错误", sql = "firm_name_desc = $S{firmNameDesc}" ,field = "firm_name_desc")
   private String firmNameDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "所属部门错误", sql = "department_desc = $S{departmentDesc}" ,field = "department_desc")
   private String departmentDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "职位错误", sql = "post_desc = $S{postDesc}" ,field = "post_desc")
   private String postDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "学历错误", sql = "education_desc = $S{educationDesc}" ,field = "education_desc")
   private String educationDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "学位错误", sql = "degree_desc = $S{degreeDesc}" ,field = "degree_desc")
   private String degreeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "首次参加工作时间错误", sql = "career_start_date_desc = $S{careerStartDateDesc}" ,field = "career_start_date_desc")
   private String careerStartDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "首次从事理财业务时间错误", sql = "wealth_start_date_desc = $S{wealthStartDateDesc}" ,field = "wealth_start_date_desc")
   private String wealthStartDateDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "专业技术职称错误", sql = "profess_qualy_level_desc = $S{professQualyLevelDesc}" ,field = "profess_qualy_level_desc")
   private String professQualyLevelDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财专业证书错误", sql = "wealth_cer_desc = $S{wealthCerDesc}" ,field = "wealth_cer_desc")
   private String wealthCerDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财登记培训证书编号错误", sql = "regist_cer_no_desc = $S{registCerNoDesc}" ,field = "regist_cer_no_desc")
   private String registCerNoDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "所获奖励错误", sql = "reward_desc = $S{rewardDesc}" ,field = "reward_desc")
   private String rewardDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "办公电话错误", sql = "telphone_desc = $S{telphoneDesc}" ,field = "telphone_desc")
   private String telphoneDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "移动电话错误", sql = "mobile_desc = $S{mobileDesc}" ,field = "mobile_desc")
   private String mobileDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "电子邮箱错误", sql = "email_desc = $S{emailDesc}" ,field = "email_desc")
   private String emailDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "登记业务分类错误", sql = "register_classify_desc = $S{registerClassifyDesc}" ,field = "register_classify_desc")
   private String registerClassifyDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "登记人员类别错误", sql = "regist_type_desc = $S{registTypeDesc}" ,field = "regist_type_desc")
   private String registTypeDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "备注错误", sql = "details_desc = $S{detailsDesc}" ,field = "details_desc")
   private String detailsDesc;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
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

  	public String getProfessionDesc() {
        return professionDesc;
    }

    public void setProfessionDesc(String professionDesc) {
        this.professionDesc = professionDesc;
    }
  	public String getNameDesc() {
        return nameDesc;
    }

    public void setNameDesc(String nameDesc) {
        this.nameDesc = nameDesc;
    }
  	public String getSexDesc() {
        return sexDesc;
    }

    public void setSexDesc(String sexDesc) {
        this.sexDesc = sexDesc;
    }
  	public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }
  	public String getIdCodeDesc() {
        return idCodeDesc;
    }

    public void setIdCodeDesc(String idCodeDesc) {
        this.idCodeDesc = idCodeDesc;
    }
  	public String getIssBranchTypeDesc() {
        return issBranchTypeDesc;
    }

    public void setIssBranchTypeDesc(String issBranchTypeDesc) {
        this.issBranchTypeDesc = issBranchTypeDesc;
    }
  	public String getRegionDesc() {
        return regionDesc;
    }

    public void setRegionDesc(String regionDesc) {
        this.regionDesc = regionDesc;
    }
  	public String getFirmNameDesc() {
        return firmNameDesc;
    }

    public void setFirmNameDesc(String firmNameDesc) {
        this.firmNameDesc = firmNameDesc;
    }
  	public String getDepartmentDesc() {
        return departmentDesc;
    }

    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }
  	public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }
  	public String getEducationDesc() {
        return educationDesc;
    }

    public void setEducationDesc(String educationDesc) {
        this.educationDesc = educationDesc;
    }
  	public String getDegreeDesc() {
        return degreeDesc;
    }

    public void setDegreeDesc(String degreeDesc) {
        this.degreeDesc = degreeDesc;
    }
  	public String getCareerStartDateDesc() {
        return careerStartDateDesc;
    }

    public void setCareerStartDateDesc(String careerStartDateDesc) {
        this.careerStartDateDesc = careerStartDateDesc;
    }
  	public String getWealthStartDateDesc() {
        return wealthStartDateDesc;
    }

    public void setWealthStartDateDesc(String wealthStartDateDesc) {
        this.wealthStartDateDesc = wealthStartDateDesc;
    }
  	public String getProfessQualyLevelDesc() {
        return professQualyLevelDesc;
    }

    public void setProfessQualyLevelDesc(String professQualyLevelDesc) {
        this.professQualyLevelDesc = professQualyLevelDesc;
    }
  	public String getWealthCerDesc() {
        return wealthCerDesc;
    }

    public void setWealthCerDesc(String wealthCerDesc) {
        this.wealthCerDesc = wealthCerDesc;
    }
  	public String getRegistCerNoDesc() {
        return registCerNoDesc;
    }

    public void setRegistCerNoDesc(String registCerNoDesc) {
        this.registCerNoDesc = registCerNoDesc;
    }
  	public String getRewardDesc() {
        return rewardDesc;
    }

    public void setRewardDesc(String rewardDesc) {
        this.rewardDesc = rewardDesc;
    }
  	public String getTelphoneDesc() {
        return telphoneDesc;
    }

    public void setTelphoneDesc(String telphoneDesc) {
        this.telphoneDesc = telphoneDesc;
    }
  	public String getMobileDesc() {
        return mobileDesc;
    }

    public void setMobileDesc(String mobileDesc) {
        this.mobileDesc = mobileDesc;
    }
  	public String getEmailDesc() {
        return emailDesc;
    }

    public void setEmailDesc(String emailDesc) {
        this.emailDesc = emailDesc;
    }
  	public String getRegisterClassifyDesc() {
        return registerClassifyDesc;
    }

    public void setRegisterClassifyDesc(String registerClassifyDesc) {
        this.registerClassifyDesc = registerClassifyDesc;
    }
  	public String getRegistTypeDesc() {
        return registTypeDesc;
    }

    public void setRegistTypeDesc(String registTypeDesc) {
        this.registTypeDesc = registTypeDesc;
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


}