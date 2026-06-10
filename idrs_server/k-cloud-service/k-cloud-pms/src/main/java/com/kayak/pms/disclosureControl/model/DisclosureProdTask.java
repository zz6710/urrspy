package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "disclosureProdTaskService", table = "idb_disclosure_prod_task")
public class DisclosureProdTask {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}", field = "prod_code")
    private String prodCode;
    @GraphQLField(label = "所有产品代码", sql = "prod_codes =$S{prodCodes}", field = "prod_codes")
    private String prodCodes;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}", field = "prod_name")
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "任务创建时间", sql = "crt_task_date = $S{crtTaskDate}", field = "crt_task_date")
    private String crtTaskDate;
    @GraphQLField(kkhtml = "KFieldText", label = "产品信披规则id", sql = "t8_disclosure_prod_rule_id = $S{t8DisclosureProdRuleId}", field = "t8_disclosure_prod_rule_id")
    private String t8DisclosureProdRuleId;
    @GraphQLField(kkhtml = "KFieldText", label = "信披规则id", sql = "t8_disclosure_rule_id = $S{t8DisclosureRuleId}", field = "t8_disclosure_rule_id")
    private String t8DisclosureRuleId;
    @GraphQLField(kkhtml = "KFieldText", label = "公告标题", sql = "notice_title = $S{noticeTitle}", field = "notice_title")
    private String noticeTitle;
    @GraphQLField(kkhtml = "KFieldText", label = "基准日期", sql = "prod_base_date = $S{prodBaseDate}", field = "prod_base_date")
    private String prodBaseDate;
    @GraphQLField(kkhtml = "KFieldText", label = "预计系统生成日期", sql = "sys_crt_date = $S{sysCrtDate}", field = "sys_crt_date")
    private String sysCrtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "任务所属月份", sql = "task_month = $S{taskMonth}", field = "task_month")
    private String taskMonth;
    @GraphQLField(kkhtml = "KFieldText", label = "状态 ", sql = "status = $S{status}", field = "status")
    private String status;
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
    @GraphQLField(label = "信披类型", field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(label = "信披子类型", field = "disclosure_son_type")
    private String disclosureSonType;


    @GraphQLField(label = "产品品牌")
    private String prodBrand;

    @GraphQLField(label = "报告日期", field = "report_date", sql = "report_date=$S{reportDate}")
    private String reportDate;

    @GraphQLField(label = "数据来源", field = "data_source", sql = "data_source=$S{dataSource}")
    private String dataSource;
    @GraphQLField(label = "开始月份", sql = "task.task_month>=$S{startMonth}", field = "task_month")
    private String startMonth;
    @GraphQLField(label = "结束月份", sql = "task.task_month<=$S{endMonth}", field = "task_month")
    private String endMonth;
    @GraphQLField(label = "信披规则名称")
    private String ruleName;
    @GraphQLField(label = "待办类型")
    private String operationType;


    @GraphQLField(label = "开始时间")
    private String startDate;

    @GraphQLField(label = "结束时间")
    private String endDate;

    @GraphQLField(label = "月份")
    private String month;

    @GraphQLField(label = "jsonStr")
    private String jsonStr;

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
    @GraphQLField(label = "公告发布状态", field = "is_notice_pub")
    private String isNoticePub;
    @GraphQLField(label = "信披状态", field = "disclosure_status")
    private String disclosureStatus;
    @GraphQLField(label = "公告生成状态", field = " create_status")
    private String createStatus;
}