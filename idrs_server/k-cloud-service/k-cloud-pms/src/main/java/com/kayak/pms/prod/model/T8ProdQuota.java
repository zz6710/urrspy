package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * 功能：额度需求管理-总额度model
 * 作者：rennannan
 * 日期：20210222
 */
@Data
@GraphQLModel(fetcher = "t8ProdQuotaService",table = "t8_prod_quota")
public class T8ProdQuota {
   @GraphQLField(label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(label = "t8ProdInfoId", sql = "t8_prod_info_id = $S{t8ProdInfoId}" ,field = "t8_prod_info_id")
   private String t8ProdInfoId;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "quota.prod_code = $S{prodCode}" ,field = "prod_code")
   private String prodCode;
   @GraphQLField(kkhtml = "KFieldText", label = "成立日/开放日", sql = "quota_date = $S{quotaDate}" ,field = "quota_date")
   private String quotaDate;
   @GraphQLField(label = "销售总额度", sql = "total_sale_quota = $D{totalSaleQuota}" ,field = "total_sale_quota")
   private String totalSaleQuota;
   @GraphQLField(label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser;
   @GraphQLField(label = "更新者", sql = "upd_user = $S{updUser}" ,field = "upd_user")
   private String updUser;
    @GraphQLField(label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(label = "修改日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(label = "修改时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(label = "确认状态", sql = "confirm_status = $S{confirmStatus}", field = "confirm_status")
    private String confirmStatus;
    @GraphQLField(label = "状态", sql = "confirm_status >= $S{status}", field = "confirm_status")
    private String status;
    @GraphQLField(label = "决策类型", sql = "decision_type = $S{decisionType}", field = "decision_type")
    private String decisionType;
    @GraphQLField(label = "会议id", sql = "meeting_id = $S{meetingId}", field = "meeting_id")
    private String meetingId;
    //产品名称  用于展示
    @GraphQLField(label = "产品名称", field = "prod_name")
    private String prodName;
    @GraphQLField(label = "查询起始日", sql = "quota_date>=$S{startEstablishDate}", field = "quota_date")
    private String startEstablishDate;
    @GraphQLField(label = "查询结束日", sql = "quota_date<=$S{endEstablishDate}", field = "quota_date")
    private String endEstablishDate;
    @GraphQLField(label = "剩余额度", field = "remain_quota")
    private String remainQuota;
    @GraphQLField(label = "创建人姓名", field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(label = "日期类型", field = "date_type")
    private String dateType;
    @GraphQLField(label = "确认状态", field = "confirm_status_name")
    private String confirmStatusName;
    @GraphQLField(label = "会议名称", field = "meeting_name")
    private String meetingName;

}