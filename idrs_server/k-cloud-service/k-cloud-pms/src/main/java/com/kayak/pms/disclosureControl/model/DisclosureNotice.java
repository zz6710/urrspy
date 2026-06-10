package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;


@Data
@GraphQLModel(fetcher = "disclosureNoticeService",table = "idb_disclosure_notice")
public class DisclosureNotice {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "公告id", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "公告版本id", sql = "notice_version_id = $S{noticeVersionId}" ,field = "notice_version_id")
   private String noticeVersionId;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
   private String prodName;
   @GraphQLField(kkhtml = "KFieldText", label = "产品信披规则id", sql = "t8_disclosure_rule_id = $S{t8DisclosureRuleId}" ,field = "t8_disclosure_rule_id")
   private String t8DisclosureRuleId;
   @GraphQLField(kkhtml = "KFieldText", label = "信披模板id", sql = "t8_disclosure_mod_id = $S{t8DisclosureModId}" ,field = "t8_disclosure_mod_id")
   private String t8DisclosureModId;
   @GraphQLField(kkhtml = "KFieldText", label = "信披模板版本id", sql = "t8_disclosure_mod_version_id = $S{t8DisclosureModVersionId}" ,field = "t8_disclosure_mod_version_id")
   private String t8DisclosureModVersionId;
   @GraphQLField(kkhtml = "KFieldText", label = "报告类型", sql = "report_type = $S{reportType}", field = "report_type")
   private String reportType;
   @GraphQLField(kkhtml = "KFieldText", label = "公告标题", sql = "notice_title = $S{noticeTitle}" ,field = "notice_title")
   private String noticeTitle;
   @GraphQLField(kkhtml = "KFieldText", label = "taskId", sql = "task_id = $S{taskId}", field = "task_id")
   private String taskId;
   @GraphQLField(kkhtml = "KFieldText", label = "计划发布日期", sql = "plan_fb_date = $S{planFbDate}" ,field = "plan_fb_date")
   private String planFbDate;
   @GraphQLField(kkhtml = "KFieldText", label = "计划审批日期", sql = "plan_sp_date = $S{planSpDate}" ,field = "plan_sp_date")
   private String planSpDate;
   @GraphQLField(kkhtml = "KFieldText", label = "计划补录日期", sql = "plan_bl_date = $S{planBlDate}" ,field = "plan_bl_date")
   private String planBlDate;
   @GraphQLField(kkhtml = "KFieldText", label = "实际补录日期", sql = "real_bl_date = $S{realBlDate}" ,field = "real_bl_date")
   private String realBlDate;
   @GraphQLField(kkhtml = "KFieldText", label = "实际审批日期", sql = "real_sp_date = $S{realSpDate}" ,field = "real_sp_date")
   private String realSpDate;
   @GraphQLField(kkhtml = "KFieldText", label = "实际发布日期", sql = "real_fb_date = $S{realFbDate}" ,field = "real_fb_date")
   private String realFbDate;
   @GraphQLField(kkhtml = "KFieldText", label = "基准日期", sql = "prod_base_date = $S{prodBaseDate}" ,field = "prod_base_date")
   private String prodBaseDate;
   @GraphQLField(kkhtml = "KFieldText", label = "信披状态", sql = "disclosure_status = $S{disclosureStatus}" ,field = "disclosure_status")
   private String disclosureStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "复核状态", sql = "review_status = $S{reviewStatus}" ,field = "review_status")
   private String reviewStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "报告开始日期", sql = "report_start_date = $S{reportStartDate}" ,field = "report_start_date")
   private String reportStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "报告结束日期", sql = "report_end_date = $S{reportEndDate}" ,field = "report_end_date")
   private String reportEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "信披类型", sql = "disclosure_type = $S{disclosureType}", field = "disclosure_type")
   private String disclosureType;
   @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", sql = "disclosure_son_type = $S{disclosureSonType}", field = "disclosure_son_type")
   private String disclosureSonType;
   @GraphQLField(kkhtml = "KFieldText", label = "已发送邮件", sql = "report_end_date = $S{isSendEmail}", field = "is_send_email")
   private String isSendEmail;
   @GraphQLField(kkhtml = "KFieldText", label = "是否生效", sql = "effect_status = $S{effectStatus}", field = "effect_status")
   private String effectStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "文件名", sql = "file_name = $S{fileName}", field = "file_name")
   private String fileName;
   @GraphQLField(kkhtml = "KFieldText", label = "文件路径", sql = "filePath = $S{filePath}", field = "file_path")
   private String filePath;
   @GraphQLField(kkhtml = "KFieldText", label = "文件发送路径", sql = "crt_path = $S{crtPath}", field = "crt_path")
   private String crtPath;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}" ,field = "crt_user_id")
   private String crtUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}" ,field = "crt_user_name")
   private String crtUserName;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user_id = $S{updUserId}" ,field = "upd_user_id")
   private String updUserId;
   @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}" ,field = "upd_user_name")
   private String updUserName;
   @GraphQLField(kkhtml = "KFieldText", label = "模板名称", sql = "mod_name = $S{modName}", field = "mod_name")
   private String modName;
   @GraphQLField(kkhtml = "KFieldText", label = "模板版本号", sql = "mod_version = $S{modVersion}", field = "mod_version")
   private String modVersion;
   @GraphQLField(kkhtml = "KFieldText", label = "信披公告版本号", sql = "disclosure_version = $S{disclosureVersion}", field = "disclosure_version")
   private String disclosureVersion;
   @GraphQLField(kkhtml = "KFieldText", label = "模板版本文件格式", sql = "suffix = $S{suffix}", field = "suffix")
   private String suffix;


   @GraphQLField(kkhtml = "KFieldText", label = "产品形态", sql = "PROD_FORM = $S{prodForm}", field = "PROD_FORM")
   private String prodForm;
   @GraphQLField(kkhtml = "KFieldText", label = "投资周期维度", sql = "INV_PRD_DIME = $S{invPrdDime}", field = "INV_PRD_DIME")
   private String invPrdDime;
   @GraphQLField(kkhtml = "KFieldText", label = "投资周期长度", sql = "INV_PRD_LEN = $S{invPrdLen}", field = "INV_PRD_LEN")
   private String invPrdLen;
   @GraphQLField(kkhtml = "KFieldText", label = "产品投资性质", sql = "prod_inv_typ = $S{prodInvTyp}", field = "prod_inv_typ")
   private String prodInvTyp;
   @GraphQLField(kkhtml = "KFieldText", label = "销售对象", sql = "PROD_OBJ = $S{prodObj}", field = "PROD_OBJ")
   private String prodObj;
   @GraphQLField(kkhtml = "KFieldText", label = "产品系列代码", sql = "PROD_SER_CD = $S{prodSerCd}", field = "PROD_SER_CD")
   private String prodSerCd;
   @GraphQLField(kkhtml = "KFieldText", label = "产品系列名称", sql = "PROD_SER_NM = $S{prodSerNm}", field = "PROD_SER_NM")
   private String prodSerNm;
   @GraphQLField(kkhtml = "KFieldText", label = "募集方式", sql = "PROD_CLC_MTH = $S{prodClcMth}", field = "PROD_CLC_MTH")
   private String prodClcMth;
   @GraphQLField(kkhtml = "KFieldText", label = "托管行名称", sql = "trustee_name like  '%$S{trusteeName}%' ", field = "trustee_name")
   private String trusteeName;

   @GraphQLField(label = "自动发布标识",field = "autoFlag")
   private String autoFlag;
   @GraphQLField(label = "发布批次号",field = "batch_no")
   private String batchNo;

   @GraphQLField(label = "首页披露情况标识",field = "disclosure_flag")
   private String disclosureFlag;

   @GraphQLField(label = "首页披露情况统计",field = "disclosure_count")
   private String disclosureCount;


}