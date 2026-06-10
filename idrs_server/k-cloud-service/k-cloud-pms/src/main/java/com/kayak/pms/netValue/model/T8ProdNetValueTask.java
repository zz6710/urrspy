package com.kayak.pms.netValue.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "t8ProdNetValueTaskService", table = "t8_prod_net_value_task")
public class T8ProdNetValueTask {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "任务id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "任务日期", sql = "task_date = $S{taskDate}", field = "task_date")
    private String taskDate;
    @GraphQLField(kkhtml = "KFieldText", label = "任务名称", sql = "task_name = $S{taskName}", field = "task_name")
    private String taskName;
    @GraphQLField(kkhtml = "KFieldText", label = "任务状态 0未确认 1已确认", sql = "task_status = $S{taskStatus}", field = "task_status")
    private String taskStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "任务描述", sql = "task_desc = $S{taskDesc}", field = "task_desc")
    private String taskDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user_name = $S{crtUserName}", field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "确认人id", sql = "confirm_user_id = $S{confirmUserId}", field = "confirm_user_id")
    private String confirmUserId;
    @GraphQLField(kkhtml = "KFieldText", label = "确认人姓名", sql = "confirm_user_name = $S{confirmUserName}", field = "confirm_user_name")
    private String confirmUserName;
    @GraphQLField(kkhtml = "KFieldText", label = "确认日期", sql = "confirm_date = $S{confirmDate}", field = "confirm_date")
    private String confirmDate;
    @GraphQLField(kkhtml = "KFieldText", label = "确认时间", sql = "confirm_time = $S{confirmTime}", field = "confirm_time")
    private String confirmTime;
    @GraphQLField(label = "开始日期", sql = " prod_base_date >=$S{startDate}", field = "start_date")
    private String startDate;
    @GraphQLField(label = "结束日期", sql = " prod_base_date <=$S{endDate}", field = "end_date")
    private String endDate;
    @GraphQLField(label = "基准日期/披露日期", sql = " prod_base_date =$S{prodBaseDate}", field = "prod_base_date")
    private String prodBaseDate;
    @GraphQLField(label = "产品数量", sql = " count =$S{count}", field = "count")
    private String count;
    @GraphQLField(label = "模板文件名称", sql = " mod_doc_name =$S{modDocName}", field = "mod_doc_name")
    private String modDocName;
    @GraphQLField(label = "模板版本号", sql = " mod_version_number =$S{modVersionNumber}", field = "mod_version_number")
    private String modVersionNumber;
    @GraphQLField(label = "状态", sql = "status =$S{status}", field = "status")
    private String status;
    @GraphQLField(label = "公告ID", sql = "notice_id =$S{noticeId}", field = "notice_id")
    private String noticeId;
    @GraphQLField(label = "信披类型", sql = "disclosure_type =$S{disclosureType}", field = "disclosure_type")
    private String disclosureType;
    @GraphQLField(label = "信披子类型", sql = "disclosure_son_type =$S{disclosureSonType}", field = "disclosure_son_type")
    private String disclosureSonType;
    @GraphQLField(label = "所有产品代码", sql = "prod_codes =$S{prodCodes}", field = "prod_codes")
    private String prodCodes;
    @GraphQLField(label = "所有产品名称", sql = "prod_names =$S{prodNames}", field = "prod_names")
    private String prodNames;
    @GraphQLField(label = "数据来源", sql = "data_source =$S{dataSource}", field = "data_source")
    private String dataSource;
    @GraphQLField(label = "最大公告版本id", sql = "ver_max_id =$S{verMaxId}", field = "ver_max_id")
    private String verMaxId;
    @GraphQLField(label = "模板版本id", sql = "disclosure_mod_version_id =$S{disclosureModVersionId}", field = "disclosure_mod_version_id")
    private String disclosureModVersionId;
    @GraphQLField(label = "公告文件名称", sql = "notice_doc_name =$S{noticeDocName}", field = "notice_doc_name")
    private String noticeDocName;
    @GraphQLField(label = "公告版本号", sql = "notice_version =$S{noticeVersion}", field = "notice_version")
    private String noticeVersion;
    @GraphQLField
    private List<T8ProdNetValueTask> netValueTask;
    @GraphQLField
    private List<T8ProdNetValueNotice> t8ProdNotice;


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


    @GraphQLField(kkhtml = "KFieldText", label = "产品信披规则id", sql = "t8_disclosure_prod_rule_id = $S{t8DisclosureProdRuleId}", field = "t8_disclosure_prod_rule_id")
    private String t8DisclosureProdRuleId;
    @GraphQLField(kkhtml = "KFieldText", label = "信披规则id", sql = "t8_disclosure_rule_id = $S{t8DisclosureRuleId}", field = "t8_disclosure_rule_id")
    private String t8DisclosureRuleId;
    @GraphQLField(kkhtml = "KFieldText", label = "公告标题", sql = "notice_title = $S{noticeTitle}", field = "notice_title")
    private String noticeTitle;
    @GraphQLField(kkhtml = "KFieldText", label = "任务创建时间", sql = "crt_task_date = $S{crtTaskDate}", field = "crt_task_date")
    private String crtTaskDate;
    @GraphQLField(kkhtml = "KFieldText", label = "预计系统生成日期", sql = "sys_crt_date = $S{sysCrtDate}", field = "sys_crt_date")
    private String sysCrtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "任务所属月份", sql = "task_month = $S{taskMonth}", field = "task_month")
    private String taskMonth;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField(kkhtml = "KFieldText", label = "信披状态", sql = "disclosure_status = $S{disclosureStatus}", field = "disclosure_status")
    private String disclosureStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "版本是否已发布", sql = "is_notice_pub = $S{isNoticePub}", field = "is_notice_pub")
    private String isNoticePub;
}