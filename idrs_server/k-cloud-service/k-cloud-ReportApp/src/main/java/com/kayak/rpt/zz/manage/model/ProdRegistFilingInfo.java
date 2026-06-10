package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodRegistFilingInfoService",table = "app_prod_regist_filing_info")
public class ProdRegistFilingInfo {
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name like '%$U{prodName}%'" ,field = "prod_name")
   private String prodName;
   @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "行内标识码", sql = "ident_code  like '%$U{identCode}%'" ,field = "ident_code")
   private String identCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品品牌", sql = "prod_brand = $S{prodBrand}" ,field = "prod_brand")
   private String prodBrand;
   @GraphQLField(kkhtml = "KFieldText", label = "产品期次", sql = "prod_term_no = $S{prodTermNo}" ,field = "prod_term_no")
   private String prodTermNo;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
   private String bankCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品审批人姓名", sql = "PROD_APRV_NM = $S{prodAprvNm}" ,field = "PROD_APRV_NM")
   private String prodAprvNm;
   @GraphQLField(kkhtml = "KFieldText", label = "产品审批人身份证号", sql = "approver_id_code = $S{approverIdCode}" ,field = "approver_id_code")
   private String approverIdCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品设计人姓名", sql = "PROD_DSN_NM = $S{prodDsnNm}" ,field = "PROD_DSN_NM")
   private String prodDsnNm;
   @GraphQLField(kkhtml = "KFieldText", label = "产品设计人身份证号", sql = "designer_id_code = $S{designerIdCode}" ,field = "designer_id_code")
   private String designerIdCode;
   @GraphQLField(kkhtml = "KFieldText", label = "投资经理姓名", sql = "INV_MNG_NM = $S{invMngNm}" ,field = "INV_MNG_NM")
   private String invMngNm;
   @GraphQLField(kkhtml = "KFieldText", label = "投资经理身份证号", sql = "manager_id_code = $S{managerIdCode}" ,field = "manager_id_code")
   private String managerIdCode;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人姓名", sql = "contact_name = $S{contactName}" ,field = "contact_name")
   private String contactName;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人座机", sql = "contact_telphone = $S{contactTelphone}" ,field = "contact_telphone")
   private String contactTelphone;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人手机", sql = "contact_mobile = $S{contactMobile}" ,field = "contact_mobile")
   private String contactMobile;
   @GraphQLField(kkhtml = "KFieldText", label = "业务联络人邮箱", sql = "contact_email = $S{contactEmail}" ,field = "contact_email")
   private String contactEmail;
   @GraphQLField(kkhtml = "KFieldText", label = "产品募集方式", sql = "type_collect = $S{typeCollect}" ,field = "type_collect")
   private String typeCollect;
   @GraphQLField(kkhtml = "KFieldText", label = "产品收益类型", sql = "prod_retrun_type = $S{prodRetrunType}" ,field = "prod_retrun_type")
   private String prodRetrunType;
   @GraphQLField(kkhtml = "KFieldText", label = "产品期限", sql = "prod_term = $S{prodTerm}" ,field = "prod_term")
   private String prodTerm;
   @GraphQLField(kkhtml = "KFieldText", label = "是否金融同业专属", sql = "fiancial_exclusive = $S{fiancialExclusive}" ,field = "fiancial_exclusive")
   private String fiancialExclusive;
   @GraphQLField(kkhtml = "KFieldText", label = "资金投向地区", sql = "invert_region = $S{invertRegion}" ,field = "invert_region")
   private String invertRegion;
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资国家或地区（境外）", sql = "invert_country = $S{invertCountry}" ,field = "invert_country")
   private String invertCountry;
   @GraphQLField(kkhtml = "KFieldText", label = "理财业务服务模式", sql = "service_mode = $S{serviceMode}" ,field = "service_mode")
   private String serviceMode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品运作模式", sql = "operation_mode = $S{operationMode}" ,field = "operation_mode")
   private String operationMode;
   @GraphQLField(kkhtml = "KFieldText", label = "是否设置最短持有期限", sql = "min_hold_period = $S{minHoldPeriod}" ,field = "min_hold_period")
   private String minHoldPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "最短持有期限（天）", sql = "min_hold_day = $S{minHoldDay}" ,field = "min_hold_day")
   private String minHoldDay;
   @GraphQLField(kkhtml = "KFieldText", label = "最短持有期后是否自由赎回", sql = "option_redempt_period = $S{optionRedemptPeriod}" ,field = "option_redempt_period")
   private String optionRedemptPeriod;
   @GraphQLField(kkhtml = "KFieldText", label = "是否现金管理类", sql = "cash_manager = $S{cashManager}" ,field = "cash_manager")
   private String cashManager;
   @GraphQLField(kkhtml = "KFieldText", label = "产品资产配置方式", sql = "asset_ac_method = $S{assetAcMethod}" ,field = "asset_ac_method")
   private String assetAcMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "产品管理模式", sql = "prod_mana_mode = $S{prodManaMode}" ,field = "prod_mana_mode")
   private String prodManaMode;
   @GraphQLField(kkhtml = "KFieldText", label = "实际管理人名称", sql = "ac_mana_name = $S{acManaName}" ,field = "ac_mana_name")
   private String acManaName;
   @GraphQLField(kkhtml = "KFieldText", label = "产品定价方式", sql = "price_method = $S{priceMethod}" ,field = "price_method")
   private String priceMethod;
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资性质", sql = "invest_type = $S{investType}" ,field = "invest_type")
   private String investType;
   @GraphQLField(kkhtml = "KFieldText", label = "合作模式", sql = "cooperate_mode = $S{cooperateMode}" ,field = "cooperate_mode")
   private String cooperateMode;
   @GraphQLField(kkhtml = "KFieldText", label = "合作机构名称", sql = "cooperator = $S{cooperator}" ,field = "cooperator")
   private String cooperator;
   @GraphQLField(kkhtml = "KFieldText", label = "投资资产种类及比例", sql = "invest_type_ratio = $S{investTypeRatio}" ,field = "invest_type_ratio")
   private String investTypeRatio;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准%", sql = "prod_benchmark_str = $S{prodBenchmarkStr}" ,field = "prod_benchmark_str")
   private String prodBenchmarkStr;
   @GraphQLField(kkhtml = "KFieldText", sql = "prod_benchmark = $S{prodBenchmark}" ,field = "prod_benchmark")
   private String prodBenchmark;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者风险偏好", sql = "risk_level = $S{riskLevel}" ,field = "risk_level")
   private String riskLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "产品销售区域", sql = "prod_sales_region = $S{prodSalesRegion}" ,field = "prod_sales_region")
   private String prodSalesRegion;
   @GraphQLField(kkhtml = "KFieldText", label = "募集币种", sql = "fund_cur = $S{fundCur}" ,field = "fund_cur")
   private String fundCur;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付本金币种", sql = "principal_cur = $S{principalCur}" ,field = "principal_cur")
   private String principalCur;
   @GraphQLField(kkhtml = "KFieldText", label = "兑付收益币种", sql = "income_cur = $S{incomeCur}" ,field = "income_cur")
   private String incomeCur;
   @GraphQLField(kkhtml = "KFieldText", label = "起点销售金额", sql = "invest_threshold_str = $S{investThresholdStr}" ,field = "invest_threshold_str")
   private String investThresholdStr;
   @GraphQLField(kkhtml = "KFieldText",  sql = "invest_threshold = $S{investThreshold}" ,field = "invest_threshold")
   private String investThreshold;
   @GraphQLField(kkhtml = "KFieldText", label = "计划募集金额（元）", sql = "plan_fund_amt = $S{planFundAmt}" ,field = "plan_fund_amt")
   private String planFundAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期（从)", sql = "start_date_earliest = $S{startDateEarliest}" ,field = "start_date_earliest")
   private String startDateEarliest;
   @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期（到)", sql = "start_date_latest = $S{startDateLatest}" ,field = "start_date_latest")
   private String startDateLatest;
   @GraphQLField(kkhtml = "KFieldText", label = "投资本金到账日", sql = "principal_due_date = $S{principalDueDate}" ,field = "principal_due_date")
   private String principalDueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资收益到账日", sql = "income_due_date = $S{incomeDueDate}" ,field = "income_due_date")
   private String incomeDueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "销售手续费率%", sql = "sales_commission_rate_str = $S{salesComsalesCommissionRateStrmissionRate}" ,field = "sales_commission_rate_str")
   private String salesCommissionRateStr;
   @GraphQLField(kkhtml = "KFieldText",  sql = "sales_commission_rate = $S{salesCommissionRate}" ,field = "sales_commission_rate")
   private String salesCommissionRate;
   @GraphQLField(kkhtml = "KFieldText", label = "投资管理费率%", sql = "manage_fee_rate_str = $S{manageFeeRateStr}" ,field = "manage_fee_rate_str")
   private String manageFeeRateStr;
   @GraphQLField(kkhtml = "KFieldText", sql = "manage_fee_rate = $S{manageFeeRate}" ,field = "manage_fee_rate")
   private String manageFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "境内托管机构名称", sql = "dc_cd_name = $S{dcCdName}" ,field = "dc_cd_name")
   private String dcCdName;
   @GraphQLField(kkhtml = "KFieldText", label = "境内托管机构代码", sql = "dc_cd_ident_code = $S{dcCdIdentCode}" ,field = "dc_cd_ident_code")
   private String dcCdIdentCode;
   @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构国别", sql = "seas_cd_nation = $S{seasCdNation}" ,field = "seas_cd_nation")
   private String seasCdNation;
   @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构名称", sql = "seas_cd_name = $S{seasCdName}" ,field = "seas_cd_name")
   private String seasCdName;
   @GraphQLField(kkhtml = "KFieldText", label = "托管费率%", sql = "cd_fee_rate_str = $S{cdFeeRateStr}" ,field = "cd_fee_rate_str")
   private String cdFeeRateStr;
   @GraphQLField(kkhtml = "KFieldText", sql = "cd_fee_rate = $S{cdFeeRate}" ,field = "cd_fee_rate")
   private String cdFeeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品风险等级", sql = "risk_rate = $S{riskRate}" ,field = "risk_rate")
   private String riskRate;
   @GraphQLField(kkhtml = "KFieldText", label = "发行机构提前终止权标识", sql = "early_tn_option = $S{earlyTnOption}" ,field = "early_tn_option")
   private String earlyTnOption;
   @GraphQLField(kkhtml = "KFieldText", label = "客户赎回权标识", sql = "invest_rdm_option = $S{investRdmOption}" ,field = "invest_rdm_option")
   private String investRdmOption;
   @GraphQLField(kkhtml = "KFieldText", label = "产品增信标识", sql = "prod_crt_enhance = $S{prodCrtEnhance}" ,field = "prod_crt_enhance")
   private String prodCrtEnhance;
   @GraphQLField(kkhtml = "KFieldText", label = "产品增信机构类型", sql = "crt_ins_type = $S{crtInsType}" ,field = "crt_ins_type")
   private String crtInsType;
   @GraphQLField(kkhtml = "KFieldText", label = "产品增信形式", sql = "prod_crt_method = $S{prodCrtMethod}" ,field = "prod_crt_method")
   private String prodCrtMethod;
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
   @GraphQLField(kkhtml = "KFieldText", label = "报告主文件", sql = "main_doc = $S{mainDoc}" ,field = "main_doc")
   private String mainDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品可信性评估报告", sql = "feasy_ass_report = $S{feasyAssReport}" ,field = "feasy_ass_report")
   private String feasyAssReport;
   @GraphQLField(kkhtml = "KFieldText", label = "内部审核文件", sql = "inter_audit_doc = $S{interAuditDoc}" ,field = "inter_audit_doc")
   private String interAuditDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "对理财产品投资管理人托管人投资顾问等相关方的尽职调查", sql = "due_diligencr_doc = $S{dueDiligencrDoc}" ,field = "due_diligencr_doc")
   private String dueDiligencrDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "对理财产品投资管理人托管人投资顾问等相关方签署的法律文件", sql = "legal_doc_sifned = $S{legalDocSifned}" ,field = "legal_doc_sifned")
   private String legalDocSifned;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品销售文件", sql = "prod_sale_doc = $S{prodSaleDoc}" ,field = "prod_sale_doc")
   private String prodSaleDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品说明书", sql = "prod_specifi = $S{prodSpecifi}" ,field = "prod_specifi")
   private String prodSpecifi;
   @GraphQLField(kkhtml = "KFieldText", label = "理财产品宣传材料", sql = "prod_mark_doc = $S{prodMarkDoc}" ,field = "prod_mark_doc")
   private String prodMarkDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "其他材料", sql = "other_doc = $S{otherDoc}" ,field = "other_doc")
   private String otherDoc;
   @GraphQLField(kkhtml = "KFieldText", label = "新老产品标记", sql = "new_prod = $S{newProd}" ,field = "new_prod")
   private String newProd;
   @GraphQLField(label = "开始时间")
   private String startDate;
   @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
   private String createDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
   private String theoryReportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
   private String theoryReportEndDate;
   @GraphQLField(kkhtml = "KFieldText")
   private String queryStartDate;
   @GraphQLField(kkhtml = "KFieldText")
   private String queryEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "结束时间")
   private String endDate;
   @GraphQLField(kkhtml = "KFieldText", label = "成立日期")
   private String foundDt;
   @GraphQLField(kkhtml = "KFieldText", label = "产品特殊属性")
   private String prodEspPrpt;
   @GraphQLField(kkhtml = "KFieldText", sql = "audit_status = $S{auditStatus}" ,field = "auditStatus")
   private String auditStatus;
   @GraphQLField(kkhtml = "KFieldText",  label = "数据日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
   private String reportDate;
//   @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始", sql = "report_date >= $S{beginDate}" ,field = "beginDate")
   @GraphQLField(kkhtml = "KFieldText", label = "查询范围起始")
   private String beginDate;
//   @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止", sql = "report_date <= $S{queryDate}" ,field = "queryDate")
   @GraphQLField(kkhtml = "KFieldText", label = "查询范围终止")
   private String queryDate;

   /*数据状态*/
   private  String sysDataStatus;
   /*数据日期*/
   private  String sysDataDate;
   /*数据版本*/
   private  String sysDataVersion;
   /*数据源*/
   private  String sysDataSource;

}