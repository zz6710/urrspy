package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodStateRegistInfoService", table = "app_prod_state_regist_info")
public class ProdStateRegistInfo {
    @GraphQLField(kkhtml = "KFieldText", label = "主键", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}", field = "bank_code")
    private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_code like '%$U{prodCode}%'", field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc like '%$U{prodRegEnc}%'", field = "prod_reg_enc")
    private String prodRegEnc;
    @GraphQLField(kkhtml = "KFieldText", label = "理财产品总资产金额(元)", sql = "tot_assets = $S{totAssets}", field = "tot_assets")
    private String totAssets;
    @GraphQLField(kkhtml = "KFieldText", label = "理财产品杠杆率(%)", sql = "rate = $S{rate}", field = "rate")
    private String rate;
    @GraphQLField(kkhtml = "KFieldText", label = "产品状态统计日", sql = "valdate = $S{valdate}", field = "valdate")
    private String valdate;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}", field = "details")
    private String details;
    @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}", field = "register_serno")
    private String registerSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}", field = "imp_date")
    private String impDate;
    @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}", field = "register_date")
    private String registerDate;
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}", field = "register_status")
    private String registerStatus;
    @GraphQLField(label = "开始时间")
    private String startDate;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}", field = "create_date")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}", field = "theory_report_start_date")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}", field = "theory_report_end_date")
    private String theoryReportEndDate;
    @GraphQLField(label = "结束时间")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", sql = "audit_status = $S{auditStatus}" ,field = "audit_status")
    private String auditStatus;

    @GraphQLField(kkhtml = "KFieldText", sql = "sys_data_source = $S{sysDataSource}" ,field = "sys_data_source")
    private String sysDataSource;
    @GraphQLField(kkhtml = "KFieldText", sql = "sys_data_status = $S{sysDataStatus}" ,field = "sys_data_status")
    private String sysDataStatus;
    @GraphQLField(kkhtml = "KFieldText", sql = "sys_data_version = $S{sysDataVersion}" ,field = "sys_data_version")
    private String sysDataVersion;

}