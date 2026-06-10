package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "scheduleProdRuleService", table = "idb_disclosure_prod_rule")
public class ScheduleProdRule {

    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品id", sql = "t8_prod_info_id = $S{t8ProdInfoId}", field = "t8_prod_info_id")
    private String t8ProdInfoId;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}", field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "信披规则id", sql = "t8_disclosure_rule_id = $S{t8DisclosureRuleId}", field = "t8_disclosure_rule_id")
    private String t8DisclosureRuleId;
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
    @GraphQLField(kkhtml = "KFieldText", label = "模板id", sql = "disclosure_mod_id = $S{disclosureModId}", field = "disclosure_mod_id")
    private String disclosureModId;
    @GraphQLField(kkhtml = "KFieldText", label = "模板版本id", sql = "disclosure_mod_version_id = $S{disclosureModVersionId}", field = "disclosure_mod_version_id")
    private String disclosureModVersionId;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人名称", sql = "upd_user_name = $S{updUserName}", field = "upd_user_name")
    private String updUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "数据来源 1信披规则启用导入 2手工新增 3复制产品规则", sql = "source = $S{source}", field = "source")
    private String source;
    @GraphQLField(kkhtml = "KFieldText", label = "信披规则状态", sql = "status = $S{status}", field = "status")
    private String status;
    @GraphQLField(label = "产品名称", field = "prod_name")
    private String prodName;
    @GraphQLField(label = "信披规则名称", field = "rule_name")
    private String ruleName;
    @GraphQLField(label = "信披类型", field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(label = "信披子类型", field = "disclosure_son_type")
    private String disclosureSonType;

    @GraphQLField(kkhtml = "KFieldText", label = "是否复核", sql = "if_condition = $S{ifCondition}" ,field = "ifCondition")
    private String ifCondition;
    @GraphQLField(kkhtml = "KFieldText", label = "是否需要补录", sql = "if_clearing = $S{ifClearing}" ,field = "ifClearing")
    private String ifClearing;
    @GraphQLField(kkhtml = "KFieldText", label = "是否需要补录", sql = "channel_ids = $S{channelIds}" ,field = "channel_ids")
    private String channelIds;
    @GraphQLField(label = "基准日期")
    private String baseDate;

    @GraphQLField(label = "预计生成规则")
    private String expCreateRule;

    @GraphQLField(label = "预计生成天数")
    private String expCreateDays;

    @GraphQLField(label = "产品发行成立日")
    private String establishDate;

    @GraphQLField(label = "净值基准日期")
    private String netDate;

    @GraphQLField(label = "产品到期日")
    private String endDate;

    @GraphQLField(label = "募集起始日期")
    private String applyStartDate;

    @GraphQLField(label = "募集起始日期-base")
    private String startRaise;

    @GraphQLField(label = "终止日期")
    private String prodRealCloseDate;

    @GraphQLField(label = "产品品牌")
    private String prodBrnd;

    @GraphQLField(label = "实际到期日")
    private String realEndDate;

    @GraphQLField( label = "预计补录规则")
    private String expSupplementRule;
    @GraphQLField(label = "预计补录完成天数")
    private String expSupplementDays;

    @GraphQLField(label = "预计审批规则")
    private String expApprovalRule;
    @GraphQLField(label = "预计审批完成天数")
    private String expApprovalDays;

    @GraphQLField(label = "预计发布规则")
    private String expPublishRule;
    @GraphQLField(label = "预计发布天数")
    private String expPublishDays;
    @GraphQLField(label = "公告标题", field = "notice_title")
    private String noticeTitle;
    @GraphQLField(label = "发起方式", field = "start_rule")
    private String startRule;
    @GraphQLField(label = "净值披露日期规则", field = "net_value_date_rule")
    private String netValueDateRule;
    @GraphQLField(label = "净值披露基准日期", field = "net_value_date")
    private String netValueDate;
    @GraphQLField(label = "公告负责角色", sql = "notice_roleid = $S{noticeRoleid}", field = "notice_roleid")
    private String noticeRoleid;

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
    @GraphQLField(label = "红利发放日", field = "BONUS_ISSUE_DATE")
    private String bonusIssueDate;
}