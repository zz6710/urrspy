package com.kayak.pms.netValue.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ProdNetValueNoticeService", table = "t8_prod_net_value_notice")
public class T8ProdNetValueNotice {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "披露任务ID", sql = "t8_disclosure_task_id = $S{t8DisclosureTaskId}", field = "t8_disclosure_task_id")
    private String t8DisclosureTaskId;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码 ", sql = "prod_code = $S{prodCode}", field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "净值日期", sql = "netval_date = $S{netvalDate}", field = "netval_date")
    private String netvalDate;
    @GraphQLField(kkhtml = "KFieldText", label = "披露日期", sql = "disclosure_date = $S{disclosureDate}", field = "disclosure_date")
    private String disclosureDate;
    @GraphQLField(kkhtml = "KFieldText", label = "产品总净值", sql = "total_net = $S{totalNet}", field = "total_net")
    private String totalNet;
    @GraphQLField(kkhtml = "KFieldText", label = "产品总份额", sql = "total_vol = $S{totalVol}", field = "total_vol")
    private String totalVol;
    @GraphQLField(kkhtml = "KFieldText", label = "产品单位净值", sql = "nav = $S{nav}", field = "nav")
    private String nav;
    @GraphQLField(kkhtml = "KFieldText", label = "当日收益", sql = "nav_profit = $S{navProfit}", field = "nav_profit")
    private String navProfit;
    @GraphQLField(kkhtml = "KFieldText", label = "单位万份收益", sql = "ten_thousand_income_amt = $S{tenThousandIncomeAmt}", field = "ten_thousand_income_amt")
    private String tenThousandIncomeAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "近七日年化收益率", sql = "seven_days_income_rate = $S{sevenDaysIncomeRate}", field = "seven_days_income_rate")
    private String sevenDaysIncomeRate;
    @GraphQLField(kkhtml = "KFieldText", label = "产品累计净值", sql = "total_nav = $S{totalNav}", field = "total_nav")
    private String totalNav;
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
    @GraphQLField(kkhtml = "KFieldText", label = "状态", sql = "status = $S{status}", field = "status")
    private String status;
    @GraphQLField(label = "产品名称", field = "prod_name")
    private String prodName;
    @GraphQLField(label = "产品名称", field = "master_netval_date")
    private String masterNetvalDate;
    @GraphQLField(label = "是否份额分类", field = "is_share_sort")
    private String isShareSort;
    @GraphQLField(label = "是否为母产品", field = "is_parent_prod")
    private String isParentProd;
}