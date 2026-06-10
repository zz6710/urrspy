package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureProdRuleService", table = "idb_disclosure_prod_rule")
public class DisclosureProdRule {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField
    private String prodCodeItem;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_codes = $S{prodCodes}" ,field = "prod_codes")
    private String prodCodes;
    @GraphQLField(kkhtml = "KFieldText", label = "信披规则id", sql = "t8_disclosure_rule_id = $S{t8DisclosureRuleId}" ,field = "t8_disclosure_rule_id")
    private String t8DisclosureRuleId;
    @GraphQLField(kkhtml = "KFieldText", label = "规则名称", sql = "rule_name = $S{ruleName}" ,field = "rule_name")
    private String ruleName;
    @GraphQLField(kkhtml = "KFieldText", label = "信披类型", sql = "disclosure_type = $S{disclosureType}" ,field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", sql = "disclosure_son_type = $S{disclosureSonType}" ,field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "disclosure_mod_version_id = $S{disclosureModVersionId}" ,field = "disclosure_mod_version_id")
    private String disclosureModVersionId;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本(文档)名称", sql = "t8_disclosure_version_name = $S{t8DisclosureVersionName}", field = "t8_disclosure_version_name")
    private String t8DisclosureVersionName;
    @GraphQLField(kkhtml = "KFieldText", label = "模板名称", sql = "t8_disclosure_mod_name = $S{t8DisclosureModName}", field = "t8_disclosure_mod_name")
    private String t8DisclosureModName;
    @GraphQLField(kkhtml = "KFieldText", label = "模板id", sql = "disclosure_mod_id = $S{disclosureModId}", field = "disclosure_mod_id")
    private String disclosureModId;
    @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "version_number = $S{versionNumber}", field = "version_number")
    private String versionNumber;
    @GraphQLField(kkhtml = "KFieldText", label = "公告标题", sql = "notice_title = $S{noticeTitle}" ,field = "notice_title")
    private String noticeTitle;
    @GraphQLField(kkhtml = "KFieldText", label = "信披规则状态", sql = "status = $S{status}" ,field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "公告负责角色", sql = "notice_roleid = $S{noticeRoleid}" ,field = "notice_roleid")
    private String noticeRoleid;
    @GraphQLField(kkhtml = "KFieldText", label = "任务发起方式", sql = "start_rule = $S{startRule}" ,field = "start_rule")
    private String startRule;
    @GraphQLField(kkhtml = "KFieldText", label = "基准日期", sql = "base_date = $S{baseDate}" ,field = "base_date")
    private String baseDate;
    @GraphQLField(kkhtml = "KFieldText", label = "预计生成规则", sql = "exp_create_rule = $S{expCreateRule}" ,field = "exp_create_rule")
    private String expCreateRule;
    @GraphQLField(kkhtml = "KFieldText", label = "预计生成天数", sql = "exp_create_days = $S{expCreateDays}" ,field = "exp_create_days")
    private String expCreateDays;
    @GraphQLField(kkhtml = "KFieldText", label = "预计生成日期属性", sql = "exp_create_attribute = $S{expCreateAttribute}" ,field = "exp_create_attribute")
    private String expCreateAttribute;
    @GraphQLField(kkhtml = "KFieldText", label = "预计补录规则", sql = "exp_supplement_rule = $S{expSupplementRule}" ,field = "exp_supplement_rule")
    private String expSupplementRule;
    @GraphQLField(kkhtml = "KFieldText", label = "预计补录完成天数", sql = "exp_supplement_days = $S{expSupplementDays}" ,field = "exp_supplement_days")
    private String expSupplementDays;
    @GraphQLField(kkhtml = "KFieldText", label = "预计补录完成日期属性", sql = "exp_supplement_attribute = $S{expSupplementAttribute}" ,field = "exp_supplement_attribute")
    private String expSupplementAttribute;
    @GraphQLField(kkhtml = "KFieldText", label = "预计审批规则", sql = "exp_approval_rule = $S{expApprovalRule}" ,field = "exp_approval_rule")
    private String expApprovalRule;
    @GraphQLField(kkhtml = "KFieldText", label = "预计审批完成天数", sql = "exp_approval_days = $S{expApprovalDays}" ,field = "exp_approval_days")
    private String expApprovalDays;
    @GraphQLField(kkhtml = "KFieldText", label = "预计审批完成日期属性", sql = "exp_approval_attribute = $S{expApprovalAttribute}" ,field = "exp_approval_attribute")
    private String expApprovalAttribute;
    @GraphQLField(kkhtml = "KFieldText", label = "预计发布规则", sql = "exp_publish_rule = $S{expPublishRule}" ,field = "exp_publish_rule")
    private String expPublishRule;
    @GraphQLField(kkhtml = "KFieldText", label = "预计发布天数", sql = "exp_publish_days = $S{expPublishDays}" ,field = "exp_publish_days")
    private String expPublishDays;
    @GraphQLField(kkhtml = "KFieldText", label = "预计发布日期日期属性", sql = "exp_publish_attribute = $S{expPublishAttribute}" ,field = "exp_publish_attribute")
    private String expPublishAttribute;
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
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}" ,field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "数据来源 1信披规则启用导入 2手工新增 3复制产品规则", sql = "source = $S{source}" ,field = "source")
    private String source;
    @GraphQLField(kkhtml = "KFieldText", label = "是否复核", sql = "if_condition = $S{ifCondition}" ,field = "ifCondition")
    private String ifCondition;
    @GraphQLField(kkhtml = "KFieldText", label = "是否需要补录", sql = "if_clearing = $S{ifClearing}" ,field = "ifClearing")
    private String ifClearing;
    @GraphQLField(kkhtml = "KFieldText", label = "渠道ids", sql = "channel_ids = $S{channelIds}" ,field = "channelIds")
    private String channelIds;
    @GraphQLField(label = "产品名称", field = "prod_name")
    private String prodName;
    @GraphQLField(label = "产品全称", field = "prod_full_name")
    private String prodFullName;
    @GraphQLField(label = "产品成立日")
    private String establishDate;

    @GraphQLField(label = "产品到期日")
    private String endDate;

    @GraphQLField(label = "募集起始日期")
    private String applyStartDate;

    @GraphQLField(label = "终止日期")
    private String prodRealCloseDate;


    @GraphQLField(label = "净值披露日期规则", field = "net_value_date_rule")
    private String netValueDateRule;
    @GraphQLField(label = "净值披露基准日期", field = "net_value_date")
    private String netValueDate;
    @GraphQLField(label = "规则非份额分类模板", field = "rule_version_id")
    private String ruleVersionId;
}