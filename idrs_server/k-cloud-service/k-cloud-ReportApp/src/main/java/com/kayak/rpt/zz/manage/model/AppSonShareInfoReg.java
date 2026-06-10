package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "appSonShareInfoRegService",table = "app_son_share_info_reg")
@Data
public class AppSonShareInfoReg {

   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc = $S{prodRegEnc}" ,field = "prod_reg_enc")
   private String prodRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "产品子份额代码", sql = "son_share_code = $S{sonShareCode}" ,field = "son_share_code")
   private String sonShareCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品子份额名称", sql = "son_share_name = $S{sonShareName}" ,field = "son_share_name")
   private String sonShareName;
   @GraphQLField(kkhtml = "KFieldText", label = "产品子份额业务类型", sql = "son_share_task_type = $S{sonShareTaskType}" ,field = "son_share_task_type")
   private String sonShareTaskType;
   @GraphQLField(kkhtml = "KFieldText", label = "子份额登记编码", sql = "son_share_reg_enc = $S{sonShareRegEnc}" ,field = "son_share_reg_enc")
   private String sonShareRegEnc;
   @GraphQLField(kkhtml = "KFieldText", label = "子份额销售对象", sql = "son_share_sale_obj = $S{sonShareSaleObj}" ,field = "son_share_sale_obj")
   private String sonShareSaleObj;
   @GraphQLField(kkhtml = "KFieldText", label = "业务日期", sql = "task_date = $S{taskDate}" ,field = "task_date")
   private String taskDate;
   @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
   private String details;
   @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
   private String registerDate;
   @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
   private String registerSerno;
   @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
   private String registerStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
   private String theoryReportEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "数据状态", sql = "sys_data_status = $S{sysDataStatus}" ,field = "sys_data_status")
   private String sysDataStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "数据源(1-系统生成，2-人工导入)", sql = "sys_data_source = $S{sysDataSource}" ,field = "sys_data_source")
   private String sysDataSource;
   @GraphQLField(kkhtml = "KFieldText", label = "版本(从1.0开始自增)", sql = "sys_data_version = $S{sysDataVersion}" ,field = "sys_data_version")
   private String sysDataVersion;
   @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user = $S{updUser}" ,field = "upd_user")
   private String updUser;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
   private String updDt;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;

   @GraphQLField(kkhtml = "KFieldText", sql = "audit_status = $S{auditStatus}" ,field = "auditStatus")
   private String auditStatus;

   @GraphQLField(kkhtml = "KFieldText", label = "新增日期起始", sql = "crt_dt >= $S{beginCrtDate}" ,field = "begin_crt_date")
   private String beginCrtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "新增日期终止", sql = "crt_dt <= $S{endCrtDate}" ,field = "end_crt_date")
   private String endCrtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "报送日期起始", sql = "report_date >= $S{reportBeginDate}" ,field = "report_begin_date")
   private String reportBeginDate;
   @GraphQLField(kkhtml = "KFieldText", label = "报送日期终止", sql = "report_date <= $S{reportEndDate}" ,field = "report_end_date")
   private String reportEndDate;


}