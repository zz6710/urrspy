package com.kayak.pms.basePublish.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureRuleService", table = "idb_disclosure_rule")
public class DisclosureRule {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "dr.id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "规则名称", sql = "rule_name like '%$U{ruleName}%'", field = "rule_name")
    private String ruleName;
    @GraphQLField(kkhtml = "KFieldText", label = "信披类型", sql = "disclosure_type = $S{disclosureType}", field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(kkhtml = "KFieldText", label = "信披子类型", sql = "disclosure_son_type = $S{disclosureSonType}", field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "disclosure_mod_version_id = $S{disclosureModVersionId}", field = "disclosure_mod_version_id")
    private String disclosureModVersionId;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本名称", sql = "t8_disclosure_version_name = $S{t8DisclosureVersionName}", field = "t8_disclosure_version_name")
    private String t8DisclosureVersionName;
    @GraphQLField(kkhtml = "KFieldText", label = "模板名称", sql = "t8_disclosure_mod_name = $S{t8DisclosureModName}", field = "t8_disclosure_mod_name")
    private String t8DisclosureModName;
    @GraphQLField(kkhtml = "KFieldText", label = "模板id", sql = "disclosure_mod_id = $S{disclosureModId}", field = "disclosure_mod_id")
    private String disclosureModId;
    @GraphQLField(kkhtml = "KFieldText", label = "渠道规则id", sql = "channel_rule_id = $S{channelRuleId}", field = "channel_rule_id")
    private String channelRuleId;
    @GraphQLField(kkhtml = "KFieldText", label = "渠道ids", sql = "channel_ids = $S{channelIds}", field = "channel_ids")
    private String channelIds;
    @GraphQLField
    private String docName;
    @GraphQLField(kkhtml = "KFieldText", label = "公告标题", sql = "notice_title = $S{noticeTitle}", field = "notice_title")
    private String noticeTitle;
    @GraphQLField(kkhtml = "KFieldText", label = "信披规则状态", sql = "dr.status = $S{status}", field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "信披模板版本状态", sql = "mod_V_Status = $S{modVStatus}", field = "status")
    private String modVStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "公告负责角色", sql = "notice_roleid = $S{noticeRoleid}", field = "notice_roleid")
    private String noticeRoleid;
    @GraphQLField(kkhtml = "KFieldText", label = "任务发起规则", sql = "start_rule = $S{startRule}", field = "start_rule")
    private String startRule;
    @GraphQLField(kkhtml = "KFieldText", label = "基准日期", sql = "base_date = $S{baseDate}", field = "base_date")
    private String baseDate;
    @GraphQLField(kkhtml = "KFieldText", label = "预计生成规则", sql = "exp_create_rule = $S{expCreateRule}", field = "exp_create_rule")
    private String expCreateRule;
    @GraphQLField(kkhtml = "KFieldText", label = "预计生成天数", sql = "exp_create_days = $S{expCreateDays}", field = "exp_create_days")
    private String expCreateDays;
    @GraphQLField(kkhtml = "KFieldText", label = "预计生成日期属性", sql = "exp_create_attribute = $S{expCreateAttribute}", field = "exp_create_attribute")
    private String expCreateAttribute;
    @GraphQLField(kkhtml = "KFieldText", label = "预计补录规则", sql = "exp_supplement_rule = $S{expSupplementRule}", field = "exp_supplement_rule")
    private String expSupplementRule;
    @GraphQLField(kkhtml = "KFieldText", label = "预计补录完成天数", sql = "exp_supplement_days = $S{expSupplementDays}", field = "exp_supplement_days")
    private String expSupplementDays;
    @GraphQLField(kkhtml = "KFieldText", label = "预计补录完成日期属性", sql = "exp_supplement_attribute = $S{expSupplementAttribute}", field = "exp_supplement_attribute")
    private String expSupplementAttribute;
    @GraphQLField(kkhtml = "KFieldText", label = "预计审批规则", sql = "exp_approval_rule = $S{expApprovalRule}", field = "exp_approval_rule")
    private String expApprovalRule;
    @GraphQLField(kkhtml = "KFieldText", label = "预计审批完成天数", sql = "exp_approval_days = $S{expApprovalDays}", field = "exp_approval_days")
    private String expApprovalDays;
    @GraphQLField(kkhtml = "KFieldText", label = "预计审批完成日期属性", sql = "exp_approval_attribute = $S{expApprovalAttribute}", field = "exp_approval_attribute")
    private String expApprovalAttribute;
    @GraphQLField(kkhtml = "KFieldText", label = "预计发布规则", sql = "exp_publish_rule = $S{expPublishRule}", field = "exp_publish_rule")
    private String expPublishRule;
    @GraphQLField(kkhtml = "KFieldText", label = "预计发布天数", sql = "exp_publish_days = $S{expPublishDays}", field = "exp_publish_days")
    private String expPublishDays;
    @GraphQLField(kkhtml = "KFieldText", label = "预计发布日期日期属性", sql = "exp_publish_attribute = $S{expPublishAttribute}", field = "exp_publish_attribute")
    private String expPublishAttribute;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_id = $S{crtUserId}", field = "crt_user_id")
    private String crtUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人名称", sql = "crt_user_name = $S{crtUserName}", field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user_id = $S{updUserId}", field = "upd_user_id")
    private String updUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}", field = "upd_user_name")
    private String updUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "版本号", sql = "version_number = $S{versionNumber}", field = "version_number")
    private String versionNumber;
    @GraphQLField(kkhtml = "KFieldText", label = "是否需要补录", sql = "if_clearing = $S{ifClearing}", field = "if_clearing")
    private String ifClearing;
    @GraphQLField(kkhtml = "KFieldText", label = "是否复核", sql = "if_condition = $S{ifCondition}", field = "if_condition")
    private String ifCondition;
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
    @GraphQLField(label = "净值披露日期规则", field = "net_value_date_rule")
    private String netValueDateRule;
    @GraphQLField(label = "净值披露基准日期", field = "net_value_date")
    private String netValueDate;
    @GraphQLField(label = "运作模式", field = "prod_mode")
    private String prodMode;
    @GraphQLField(label = "募集方式", field = "PROD_CLC_MTH")
    private String prodClcMth;
    @GraphQLField(label = "产品id", field = "prod_id")
    private String prodId;
    @GraphQLField(label = "产品代码", field = "prod_code")
    private String prodCode;
    @GraphQLField(label = "产品全称",field = "prod_full_name")
    private String prodFullName;
    @GraphQLField(label = "判断结果" ,field = "check_num")
    private String checkNum;
    @GraphQLField(label = "计算结果",field = "sum_num")
    private String sumNum;
    //只用来回显前端公告标题真实value
    @GraphQLField()
    private String noticeTitle1;
    @GraphQLField(label = "分级产品标志", field = "mother_fund_flag")
    private String motherFundFlag;
}