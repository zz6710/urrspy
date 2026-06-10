package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assetRegistInfoService", table = "app_asset_regist_info")
public class AssetRegistInfo {
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}", field = "bank_code")
    private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc like '%$U{prodRegEnc}%'", field = "prod_reg_enc")
    private String prodRegEnc;
    @GraphQLField(kkhtml = "KFieldText", label = "持仓类别", sql = "holding_type = $S{holdingType}", field = "holding_type")
    private String holdingType;
    @GraphQLField(kkhtml = "KFieldText", label = "行内资产/负债编码", sql = "asset_code like concat('%',$S{assetCode},'%')", field = "asset_code")
    private String assetCode;
    @GraphQLField(kkhtml = "KFieldText", label = "资产穿透情况", sql = "invested_asset = $S{investedAsset}", field = "invested_asset")
    private String investedAsset;
    @GraphQLField(kkhtml = "KFieldText", label = "中间层数", sql = "mezzanine_number = '%$U{mezzanineNumber}%'", field = "mezzanine_number")
    private String mezzanineNumber;
    @GraphQLField(kkhtml = "KFieldText", label = "中间层行内资产/负债编码", sql = "mezzanine_asset_code = $S{mezzanineAssetCode}", field = "mezzanine_asset_code")
    private String mezzanineAssetCode;
    @GraphQLField(kkhtml = "KFieldText", label = "会计科目名称", sql = "account_code = $S{accountCode}", field = "account_code")
    private String accountCode;
    @GraphQLField(kkhtml = "KFieldText", label = "金额", sql = "invested_amount = $S{investedAmount}", field = "invested_amount")
    private String investedAmount;
    @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额", sql = "invested_amount_cny = $S{investedAmountCny}", field = "invested_amount_cny")
    private String investedAmountCny;
    @GraphQLField(kkhtml = "KFieldText", label = "公允价值", sql = "fair_value = $S{fairValue}", field = "fair_value")
    private String fairValue;
    @GraphQLField(kkhtml = "KFieldText", label = "折算人民币公允价值", sql = "fair_value_cny = $S{fairValueCny}", field = "fair_value_cny")
    private String fairValueCny;
    @GraphQLField(kkhtml = "KFieldText", label = "单位估值(净价)", sql = "net_valuation = $S{netValuation}", field = "net_valuation")
    private String netValuation;
    @GraphQLField(kkhtml = "KFieldText", label = "单位估值(全价)", sql = "fl_valuation = $S{flValuation}", field = "fl_valuation")
    private String flValuation;
    @GraphQLField(kkhtml = "KFieldText", label = "数量", sql = "quantity = $S{quantity}", field = "quantity")
    private String quantity;
    @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cny = $S{cny}", field = "cny")
    private String cny;
    @GraphQLField(kkhtml = "KFieldText", label = "持仓日期", field = "holding_date")
    private String holdingDate;
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
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;
    /*发行机构名称*/
    private  String bankName;
    /*产品名称*/
    private  String prodName;

    /*数据状态*/
    private  String sysDataStatus;
    /*数据日期*/
    private  String sysDataDate;
    /*数据版本*/
    private  String sysDataVersion;
    /*数据源*/
    private  String sysDataSource;
}