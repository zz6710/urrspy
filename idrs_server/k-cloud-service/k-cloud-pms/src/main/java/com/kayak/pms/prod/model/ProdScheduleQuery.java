package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * com.kayak.pms.prod.model
 * user:rennannan
 * date:2021/3/16 16:25
 * function:产品进度表菜单对应的实体类   用于查询与导出产品生命周期进度表
 */
@Data
@GraphQLModel(fetcher = "prodScheduleQueryService")
public class ProdScheduleQuery {
    @GraphQLField(label = "产品经理",field = "prod_manage_name")
    private String prodManageName;
    @GraphQLField(label = "产品当前阶段",field = "prod_status")
    private String prodStatus;
    @GraphQLField(label = "产品代码",field = "prod_code")
    private String prodCode;
    @GraphQLField(label = "产品名称",field = "prod_name")
    private String prodName;
    @GraphQLField(label = "过创设会",field = "meet_date")
    private String meetDate;
    @GraphQLField(label = "会后参数确认",field = "meet_param_confirm_Date")
    private String meetParamConfirmDate;
    @GraphQLField(label = "申报参数确认",field = "apply_param_confirm_date")
    private String applyParamConfirmDate;
    @GraphQLField(label = "说明书法审",field = "manual_law_examine_date")
    private String manualLawExamineDate;
    @GraphQLField(label = "说明书定稿",field = "manual_finalize_date")
    private String manualFinalizeDate;
    @GraphQLField(label = "报备材料法审", field = "apply_regist_doc_law")
    private String applyRegistDocLaw;
    @GraphQLField(label = "完成一次报备", field = "apply_confirm")
    private String applyConfirm;
    @GraphQLField(label = "发行参数确认", field = "issue_param_confirm_date")
    private String issueParamConfirmDate;
    @GraphQLField(label = "完成二次报备", field = "issue_confirm")
    private String issueConfirm;
    @GraphQLField(label = "完成产品参数设置", field = "prod_manage_name")
    private String paramFinalize;
    @GraphQLField(label = "产品经理id", field = "prod_manage_id")
    private String prodManageId;
    @GraphQLField(label = "一次报备开始日期")
    private String firstStartDate;
    @GraphQLField(label = "一次报备结束日期")
    private String firstEndDate;
    @GraphQLField(label = "二次报备开始日期")
    private String secondStartDate;
    @GraphQLField(label = "二次报备结束日期")
    private String secondEndDate;
    @GraphQLField(label = "是否代码回收")
    private String isRecycleCode;
}
