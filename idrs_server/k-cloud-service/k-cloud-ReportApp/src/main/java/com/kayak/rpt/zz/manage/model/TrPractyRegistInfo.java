package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "trPractyRegistInfoService",table = "app_practy_regist_info")
public class TrPractyRegistInfo {
   @GraphQLField(kkhtml = "KFieldText", label = "从业人员类型", sql = "profession = $S{profession}" ,field = "profession")
   private String profession;
   @GraphQLField(kkhtml = "KFieldText", label = "姓名", sql = "name like '%$U{name}%'" ,field = "name")
   private String name;
    @GraphQLField(kkhtml = "KFieldText", label = "工号", sql = "jobnumber = $S{jobnumber}" ,field = "jobnumber")
    private String jobnumber;
   @GraphQLField(kkhtml = "KFieldText", label = "性别", sql = "sex = $S{sex}" ,field = "sex")
   private String sex;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "身份证号", sql = "id_code like '%$U{idCode}%'" ,field = "id_code")
   private String idCode;
   @GraphQLField(kkhtml = "KFieldText", label = "所属总行或分支行", sql = "iss_branch_type = $S{issBranchType}" ,field = "iss_branch_type")
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
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "是否审核通过", sql = "is_error = $S{isError}" ,field = "isError")
    private String isError;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "数据操作类型（D", sql = "op_type = $S{opType}" ,field = "op_type")
    private String opType;
    @GraphQLField(kkhtml = "KFieldText", label = "操作人员", sql = "summit_user = $S{summitUser}" ,field = "summit_user")
    private String summitUser;
    @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date >= $S{queryStartDate}" ,field = "queryStartDate")
    private String queryStartDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "theory_report_start_date <= $S{queryEndDate}" ,field = "queryEndDate")
    private String queryEndDate;
    @GraphQLField
    private String initIdCode;

    @GraphQLField(kkhtml = "KFieldText", sql = "audit_status = $S{auditStatus}" ,field = "auditStatus")
    private String auditStatus;

}
