package com.kayak.rpt.zz.historyInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "custRegisterInfohService",table = "app_cust_register_info_h")
public class CustRegisterInfoh {
   @GraphQLField(kkhtml = "KFieldText", label = "登记银行代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者是否属于本行", sql = "is_belong = $S{isBelong}" ,field = "is_belong")
   private String isBelong;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者所属银行名称", sql = "iss_bank_name = $S{issBankName}" ,field = "iss_bank_name")
   private String issBankName;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者所属银行代码", sql = "iss_bank_code = $S{issBankCode}" ,field = "iss_bank_code")
   private String issBankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者境内外标识", sql = "in_out_sign = $S{inOutSign}" ,field = "in_out_sign")
   private String inOutSign;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者所属国家或地区", sql = "iss_country = $S{issCountry}" ,field = "iss_country")
   private String issCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "数据类型", sql = "data_type = $S{dataType}" ,field = "data_type")
   private String dataType;
   @GraphQLField(kkhtml = "KFieldText", label = "原识别标识", sql = "ori_cust_no = $S{oriCustNo}" ,field = "ori_cust_no")
   private String oriCustNo;
   @GraphQLField(kkhtml = "KFieldText", label = "识别标识", sql = "cust_no = $S{custNo}" ,field = "cust_no")
   private String custNo;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者类别", sql = "cust_type = $S{custType}" ,field = "cust_type")
   private String custType;
   @GraphQLField(kkhtml = "KFieldText", label = "个人证件类别", sql = "personal_id_type = $S{personalIdType}" ,field = "personal_id_type")
   private String personalIdType;
   @GraphQLField(kkhtml = "KFieldText", label = "机构证件类别", sql = "organization_id_type = $S{organizationIdType}" ,field = "organization_id_type")
   private String organizationIdType;
   @GraphQLField(kkhtml = "KFieldText", label = "其他证件名称", sql = "other_id_name = $S{otherIdName}" ,field = "other_id_name")
   private String otherIdName;
   @GraphQLField(kkhtml = "KFieldText", label = "证件号码", sql = "id_code = $S{idCode}" ,field = "id_code")
   private String idCode;
   @GraphQLField(kkhtml = "KFieldText", label = "SPV资金托管账户开户行", sql = "spv_open_bank = $S{spvOpenBank}" ,field = "spv_open_bank")
   private String spvOpenBank;
   @GraphQLField(kkhtml = "KFieldText", label = "其他资金托管账户开户行", sql = "other_open_bank = $S{otherOpenBank}" ,field = "other_open_bank")
   private String otherOpenBank;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者名称", sql = "cust_name LIKE '%$U{custName}%'" ,field = "cust_name")
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
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
   private String remark;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
   private String impDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者登记账号", sql = "register_acct = $S{registerAcct}" ,field = "register_acct")
   private String registerAcct;
   @GraphQLField(kkhtml = "KFieldText", label = "登记识别标识", sql = "register_cust_no = $S{registerCustNo}" ,field = "register_cust_no")
   private String registerCustNo;
    @GraphQLField(label = "导入日期开始时间")
    private String startDate;
    @GraphQLField(kkhtml = "KFieldText", label = "是否审核通过", sql = "is_error = $S{isError}" ,field = "isError")
    private String isError;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;
    @GraphQLField(label = "导入日期截止时间")
    private String endDate;

    @GraphQLField(kkhtml = "KFieldText", label = "数据日期" ,field = "report_date")
    private String reportDate;

   @GraphQLField(kkhtml = "KFieldText", label = "ID" ,field = "id")
   private String id;

}