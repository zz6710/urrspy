package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * com.kayak.pms.disclosureControl.model
 * user:rennannan
 * date:2021/5/20 16:33
 * function:定期报告产品披露确认实体
 */
@Data
@GraphQLModel(fetcher = "regularDisProdConfirmService")
public class RegularDisProdConfirm {
    //任务id
    @GraphQLField(label = "任务id", field = "dis_prod_task_id")
    private String disProdTaskId;
    //产品代码
    @GraphQLField(label = "产品代码", field = "prod_code")
    private String prodCode;
    //产品id
    @GraphQLField(label = "产品id", field = "t8_prod_info_id")
    private String t8ProdInfoId;
    //产品名称
    @GraphQLField(label = "产品名称", field = "prod_name")
    private String prodName;
    //募集方式
    @GraphQLField(label = "募集方式", field = "raise_type")
    private String raiseType;
    //产品分类
    @GraphQLField(label = "产品分类", field = "prod_classify")
    private String prodClassify;
    //投资经理
    @GraphQLField(label = "投资经理", field = "invest_man")
    private String investMan;
    //联系方式
    @GraphQLField(label = "联系方式", field = "contact")
    private String contact;
    //产品成立日
    @GraphQLField(label = "产品成立日", field = "establish_date")
    private String establishDate;
    //产品到期日
    @GraphQLField(label = "产品到期日", field = "end_date")
    private String endDate;
    //报告日期
    @GraphQLField(label = "报告日期", field = "report_date")
    private String reportDate;
    //信披规则id
    @GraphQLField(label = "信披规则id", field = "t8_disclosure_rule_id")
    private String t8DisclosureRuleId;
    //信披子类型
    @GraphQLField(label = "信披类型", field = "disclosure_type")
    private String disclosureType;
    //信披子类型
    @GraphQLField(label = "信披子类型", field = "disclosure_son_type")
    private String disclosureSonType;
    //报告日距成立日天数（自然日）
    @GraphQLField(label = "报告日距成立日天数", field = "to_establish_date_days")
    private String toEstablishDateDays;
    //报告日距到期日天数（自然日）
    @GraphQLField(label = "报告日距到期日天数", field = "to_end_date_days")
    private String toEndDateDays;
    //基准日期
    @GraphQLField(label = "基准日期", field = "prod_base_date")
    private String prodBaseDate;
    //计划生成日期
    @GraphQLField(label = "计划生成日期", field = "plan_create_date")
    private String planCreateDate;
    //投资意见
    @GraphQLField(label = "投资意见", field = "invest_approval_result")
    private String investApprovalResult;
    //投资审核日期
    @GraphQLField(label = "投资审核日期", field = "invest_approval_date")
    private String investApprovalDate;
    //投资审核时间
    @GraphQLField(label = "投资审核时间", field = "invest_approval_time")
    private String investApprovalTime;
    //审核投资经理
    @GraphQLField(label = "审核投资经理", field = "invest_approval_user_id")
    private String investApprovalUserId;
    //审核投资经理姓名
    @GraphQLField(label = "审核投资经理姓名", field = "invest_approval_user_name")
    private String investApprovalUserName;
    //信披经理意见
    @GraphQLField(label = "信披经理意见", field = "disclosure_approval_result")
    private String disclosureApprovalResult;
    //信披审核日期
    @GraphQLField(label = "信披审核日期", field = "disclosure_approval_date")
    private String disclosureApprovalDate;
    //信披审核时间
    @GraphQLField(label = "信披审核时间", field = "disclosure_approval_time")
    private String disclosureApprovalTime;
    //信披经理id
    @GraphQLField(label = "信披经理id", field = "disclosure_user_id")
    private String disclosureUserId;
    //信披经理姓名
    @GraphQLField(label = "信披经理姓名", field = "disclosure_user_name")
    private String disclosureUserName;
    @GraphQLField(label = "开始月份")
    private String startMonth;
    @GraphQLField(label = "结束月份")
    private String endMonth;
    @GraphQLField(label = "状态")
    private String status;
    @GraphQLField(label = "公告标题")
    private String noticeTitle;
    @GraphQLField(label = "是否已披露", field = "is_already_disclosure")
    private String isAlreadyDisclosure;
    @GraphQLField
    private String crtDate;
    @GraphQLField
    private String crtTime;
    @GraphQLField(label = "是否补录完成")
    private String isCompleted;
    @GraphQLField(label = "是否审批完成")
    private String isApproved;

    @GraphQLField(label = "估值核算", field = "valuation_accounting_id")
    private String valuationAccountingId;

    @GraphQLField(label = "当前阶段状态")
    private String currentStageStatus;
    
    
    @GraphQLField(label = "估值核算联系方式", field = "valuation_accounting_id_tel")
    private String valuationAccountingIdTel;

    
    @GraphQLField(label = "投资经理联系方式", field = "invest_man_tel")
    private String investManTel;
    @GraphQLField(label = "公告id", field = "notice_id")
    private String noticeId;
    @GraphQLField(label = "模板版本id", field = "disclosure_mod_version_id")
    private String disclosureModVersionId;
    @GraphQLField(label = "公告版本号", field = "notice_version")
    private String noticeVersion;
    @GraphQLField(label = "模板名称", field = "mod_doc_name")
    private String modDocName;
    @GraphQLField(label = "模板版本号", field = "mod_version_number")
    private String modVersionNumber;
    @GraphQLField(label = "报告月份", field = "task_month")
    private String taskMonth;


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
}
