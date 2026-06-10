package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * 功能：额度需求管理-销售商额度需求model
 * 作者：rennannan
 * 日期：20210222
 */
@Data
@GraphQLModel(fetcher = "t8DistributorQuotaManageService",table = "t8_distributor_quota_manage")
public class T8DistributorQuotaManage {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
   private String id;
    @GraphQLField(label = "t8ProdInfoId", sql = "t8_prod_info_id = $S{t8ProdInfoId}" ,field = "t8_prod_info_id")
    private String t8ProdInfoId;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "总额度id", sql = "total_quota_id = $S{totalQuotaId}" ,field = "total_quota_id")
    private String totalQuotaId;
   @GraphQLField(kkhtml = "KFieldText", label = "销售商代码", sql = "distributor_code = $S{distributorCode}" ,field = "distributor_code")
   private String distributorCode;
    @GraphQLField(kkhtml = "KFieldText", label = "销售对象", sql = "prod_sale_custom = $S{prodSaleCustom}" ,field = "prod_sale_custom")
    private String prodSaleCustom;
   @GraphQLField(kkhtml = "KFieldText", label = "申请额度", sql = "quota = $D{quota}" ,field = "quota")
   private String quota;
   @GraphQLField(kkhtml = "KFieldText", label = "创建人员", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
   private String crtUser   ;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
   private String crtDate;
   @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
   private String crtTime;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
   private String updDate;
   @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
   private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人员", sql = "upd_user = $S{updUser}", field = "upd_user")
    private String updUser;
    @GraphQLField(kkhtml = "KFieldText", label = "确认人员", sql = "confirm_user = $S{confirmUser}", field = "confirm_user")
    private String confirmUser;
    @GraphQLField(kkhtml = "KFieldText", label = "确认日期", sql = "confirm_date = $S{confirmDate}", field = "confirm_date")
    private String confirmDate;
    @GraphQLField(kkhtml = "KFieldText", label = "确认时间", sql = "confirm_time = $S{confirmTime}", field = "confirm_time")
    private String confirmTime;
    @GraphQLField(kkhtml = "KFieldText", label = "确认状态", sql = "confirm_status = $S{confirmStatus}", field = "confirm_status")
    private String confirmStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "状态")
    private String status;
    @GraphQLField(kkhtml = "KFieldText", label = "所属部门", field = "MANAGER_DEPT")
    private String managerDept;
    //以下字段用于查询展示
    @GraphQLField(label = "部门名称", field = "MANAGER_DEPT_NAME")
    private String managerDeptName;
    @GraphQLField(kkhtml = "KFieldText", label = "部门总金额", field = "total_Dept_Quota")
    private String totalDeptQuota;
    @GraphQLField(kkhtml = "KFieldText", label = "销售商类型", field = "distributor_type")
    private String distributorType;
    @GraphQLField(kkhtml = "KFieldText", label = "销售商名称", field = "distributor_name")
    private String distributorName;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人姓名" ,field = "crt_user_name")
    private String crtUserName;
    @GraphQLField(label = "部门总额度",field = "dept_total_quota")
    private String deptTotalQuota;
}