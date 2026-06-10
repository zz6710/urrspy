package com.kayak.pms.netValue.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ProdNetvalDaysService", table = "t8_prod_netval_days")
public class T8ProdNetvalDays {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品id ", sql = "t8_prod_info_id = $S{t8ProdInfoId}", field = "t8_prod_info_id")
    private String t8ProdInfoId;
    @GraphQLField(kkhtml = "KFieldText", label = "净值日期", sql = "netval_date = $S{netvalDate}", field = "netval_date")
    private String netvalDate;
    @GraphQLField(kkhtml = "KFieldText", label = "披露日期", sql = "disclosure_date = $S{disclosureDate}", field = "disclosure_date")
    private String disclosureDate;
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
    @GraphQLField(kkhtml = "KFieldText", label = "状态", sql = "status = $S{status}", field = "status")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "remark = $S{remark}", field = "remark")
    private String remark;
    @GraphQLField(label = "所属月份", field = "month", sql = "month=$S{month}")
    private String month;
    @GraphQLField(label = "类型", field = "type", sql = "type=$S{type}")
    private String type;
    @GraphQLField(label = "净值日期类型", field = "netval_date_type", sql = "netval_date_type=$S{netvalDateType}")
    private String netvalDateType;
    @GraphQLField(label = "工作日", field = "workday")
    private String workDay;
    @GraphQLField(label = "开始日期")
    private String startDate;
    @GraphQLField(label = "结束日期")
    private String endDate;

}