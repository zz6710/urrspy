package com.kayak.rpt.zz.operate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "underAssetRegistService",table = "app_under_asset_regist_remark")
public class UnderAssetRegist {
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
    private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "对应资管及委外资产行内资产/负债编码", sql = "asset_manager_code like '%$U{assetManagerCode}%'" ,field = "asset_manager_code")
    private String assetManagerCode;
    @GraphQLField(kkhtml = "KFieldText", label = "资管及委外资产当前总折算人民币金额", sql = "convert_sum_amt = $S{convertSumAmt}" ,field = "convert_sum_amt")
    private String convertSumAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "资管及委外资产当前总数量", sql = "asset_sum_number = $S{assetSumNumber}" ,field = "asset_sum_number")
    private String assetSumNumber;
    @GraphQLField(kkhtml = "KFieldText", label = "资管及委外资产未投资头寸", sql = "non_invested_amt = $S{nonInvestedAmt}" ,field = "non_invested_amt")
    private String nonInvestedAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "底层资产行内资产/负债编码", sql = "under_asset_code like '%$U{underAssetCode}%'" ,field = "under_asset_code")
    private String underAssetCode;
    @GraphQLField(kkhtml = "KFieldText", label = "底层资产持仓数量", sql = "under_asset_sum = $S{underAssetSum}" ,field = "under_asset_sum")
    private String underAssetSum;
    @GraphQLField(kkhtml = "KFieldText", label = "底层资产折算人民币市值", sql = "under_convert_sum_amt = $S{underConvertSumAmt}" ,field = "under_convert_sum_amt")
    private String underConvertSumAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "持仓日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;
    @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
    private String registerSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
    private String impDate;
    @GraphQLField(kkhtml = "KFieldText", label = "投资者登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
    private String registerDate;
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态（0", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "操作人员", sql = "summit_user like '%$U{summitUser}%'" ,field = "summit_user")
    private String summitUser;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "create_date = $S{createDate}" ,field = "create_date")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "create_time = $S{createTime}" ,field = "create_time")
    private String createTime;
    @GraphQLField(kkhtml = "KFieldText", label = "数据操作类型（D", sql = "op_type = $S{opType}" ,field = "op_type")
    private String opType;
    @GraphQLField(label = "开始时间")
    private String startDate;

    @GraphQLField(label = "结束时间")
    private String endDate;
    @GraphQLField(label = "持仓开始时间")
    private String reportStartDate;

    @GraphQLField(label = "持仓结束时间")
    private String reportEndDate;
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getAssetManagerCode() {
        return assetManagerCode;
    }

    public void setAssetManagerCode(String assetManagerCode) {
        this.assetManagerCode = assetManagerCode;
    }
    public String getConvertSumAmt() {
        return convertSumAmt;
    }

    public void setConvertSumAmt(String convertSumAmt) {
        this.convertSumAmt = convertSumAmt;
    }
    public String getAssetSumNumber() {
        return assetSumNumber;
    }

    public void setAssetSumNumber(String assetSumNumber) {
        this.assetSumNumber = assetSumNumber;
    }
    public String getNonInvestedAmt() {
        return nonInvestedAmt;
    }

    public void setNonInvestedAmt(String nonInvestedAmt) {
        this.nonInvestedAmt = nonInvestedAmt;
    }
    public String getUnderAssetCode() {
        return underAssetCode;
    }

    public void setUnderAssetCode(String underAssetCode) {
        this.underAssetCode = underAssetCode;
    }
    public String getUnderAssetSum() {
        return underAssetSum;
    }

    public void setUnderAssetSum(String underAssetSum) {
        this.underAssetSum = underAssetSum;
    }
    public String getUnderConvertSumAmt() {
        return underConvertSumAmt;
    }

    public void setUnderConvertSumAmt(String underConvertSumAmt) {
        this.underConvertSumAmt = underConvertSumAmt;
    }
    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }
    public String getRegisterSerno() {
        return registerSerno;
    }

    public void setRegisterSerno(String registerSerno) {
        this.registerSerno = registerSerno;
    }
    public String getImpDate() {
        return impDate;
    }

    public void setImpDate(String impDate) {
        this.impDate = impDate;
    }
    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }
    public String getSummitUser() {
        return summitUser;
    }

    public void setSummitUser(String summitUser) {
        this.summitUser = summitUser;
    }
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

}