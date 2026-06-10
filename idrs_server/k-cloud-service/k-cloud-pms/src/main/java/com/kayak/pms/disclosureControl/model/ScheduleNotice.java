package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "scheduleNoticeService", table = "idb_disclosure_notice")
public class ScheduleNotice {
    @GraphQLField(label = "信披状态", field = "disclosureStatus")
    private String disclosureStatus;
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(label = "所有产品代码", sql = "prod_codes =$S{prodCodes}", field = "prod_codes")
    private String prodCodes;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}" ,field = "prod_name")
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "产品信披规则id", sql = "disclosure_prod_rule_id = $S{disclosureProdRuleId}" ,field = "disclosure_prod_rule_id")
    private String disclosureProdRuleId;
    @GraphQLField(kkhtml = "KFieldText", label = "信披规则id", sql = "disclosure_rule_id = $S{disclosureRuleId}" ,field = "disclosure_rule_id")
    private String disclosureRuleId;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "disclosure_mod_version_id = $S{disclosureModVersionId}" ,field = "disclosure_mod_version_id")
    private String disclosureModVersionId;
    @GraphQLField(kkhtml = "KFieldText", label = "公告标题", sql = "notice_title = $S{noticeTitle}" ,field = "notice_title")
    private String noticeTitle;
    @GraphQLField(kkhtml = "KFieldText", label = "基准日期", sql = "prod_base_date = $S{prodBaseDate}" ,field = "prod_base_date")
    private String prodBaseDate;
    @GraphQLField(kkhtml = "KFieldText", label = "发布状态", sql = "publish_status = $S{publishStatus}" ,field = "publish_status")
    private String publishStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "审批状态", sql = "approval_status = $S{approvalStatus}" ,field = "approval_status")
    private String approvalStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "托管行审批", sql = "eba_status = $S{ebaStatus}" ,field = "eba_status")
    private String ebaStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "复核状态", sql = "review_status = $S{reviewStatus}" ,field = "review_status")
    private String reviewStatus;
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
    @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}", field = "upd_user_name")
    private String updUserName;

    @GraphQLField(label = "计划发布日期", field = "plan_fb_date")
    private String planFbDate;
    @GraphQLField(label = "计划审批日期", field = "plan_sp_date")
    private String planSpDate;
    @GraphQLField(label = "计划补录日期", field = "plan_bl_date")
    private String planBlDate;
    @GraphQLField(label = "实际发布日期", field = "real_fb_date")
    private String realFbDate;
    @GraphQLField(label = "实际审批日期", field = "real_sp_date")
    private String realSpDate;
    @GraphQLField(label = "实际补录日期", field = "real_bl_date")
    private String realBlDate;
    @GraphQLField(label = "公告数据生成日期", field = "sys_crt_date")
    private String sysCrtDate;
    @GraphQLField(label = "模板版本", field = "version")
    private String version;
    @GraphQLField(label = "模板版本", field = "channel_ids")
    private String channelIds;
    @GraphQLField(kkhtml = "KFieldText", label = "信披类型", field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(kkhtml = "KFieldText", label = "生效状态", field = "effect_status")
    private String effectStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "文件存放路径", field = "file_path")
    private String filePath;
    @GraphQLField(kkhtml = "KFieldText", label = "文件名", field = "file_name")
    private String fileName;
    @GraphQLField(kkhtml = "KFieldText", label = "生成文件路径", field = "crt_path")
    private String crtPath;
    @GraphQLField(label = "当前阶段", field = "stage")
    private String stage;
    @GraphQLField(label = "当前阶段状态", field = "current_stage_status")
    private String currentStageStatus;
    @GraphQLField(label = "模板角色", field = "role_ids")
    private String roleIds;
    @GraphQLField(label = "用户组角色", field = "role_id")
    private String roleId;
    @GraphQLField(label = "用户编号", field = "user_id")
    private String userId;
    @GraphQLField(label = "信披任务id", field = "task_id")
    private String taskId;
    @GraphQLField(label = "报告类型", sql = "report_type = $S{reportType}", field = "report_type")
    private String reportType;
    @GraphQLField(label = "不包括的公告id")
    private String notInNoticeIds;
    @GraphQLField(label = "报告开始日期", sql = "report_start_date = $S{reportStartDate}", field = "report_start_date")
    private String reportStartDate;
    @GraphQLField(label = "报告结束日期", sql = "report_end_date = $S{reportEndDate}", field = "report_end_date")
    private String reportEndDate;
    @GraphQLField(label = "是否份额分类", sql = "is_share_sort = $S{isShareSort}", field = "is_share_sort")
    private String isShareSort;
    @GraphQLField(label = "公告渠道ID")
    private String noticeChannelId;
    @GraphQLField(label = "公告版本ID")
    private String noticeVersionId;
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
    @GraphQLField(kkhtml = "KFieldText", label = "产品系列", sql = "PROD_SER_CD = $S{prodSerCd}", field = "PROD_SER_CD")
    private String prodSerCd;
    @GraphQLField(label = "募集方式", field = "PROD_CLC_MTH")
    private String prodClcMth;
    @GraphQLField(label = "渠道列表", field = "channel_list")
    private String channelList;
    @GraphQLField(label = "文件列表", field = "file_list")
    private String fileList;
    @GraphQLField(label = "是否手工公告", field = "is_manual_notice")
    private String isManualNotice;
}