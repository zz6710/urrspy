package com.kayak.rpt.zz.operate.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assetRegistService",table = "app_asset_regist_remark")
public class AssetRegist {
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}", field = "bank_code")
    private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc like '%$U{prodRegEnc}%'", field = "prod_reg_enc")
    private String prodRegEnc;
    @GraphQLField(kkhtml = "KFieldText", label = "持仓类别", sql = "holding_type = $S{holdingType}", field = "holding_type")
    private String holdingType;
    @GraphQLField(kkhtml = "KFieldText", label = "行内资产/负债编码", sql = "asset_code = $S{assetCode}", field = "asset_code")
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
    @GraphQLField(kkhtml = "KFieldText", label = "折算人民币公允价", sql = "fair_value_cny = $S{fairValueCny}", field = "fair_value_cny")
    private String fairValueCny;
    @GraphQLField(kkhtml = "KFieldText", label = "单位估值(净价)", sql = "net_valuation = $S{netValuation}", field = "netValuation")
    private String netValuation;
    @GraphQLField(kkhtml = "KFieldText", label = "单位估值(全价)", sql = "fl_valuation = $S{flValuation}", field = "fl_valuation")
    private String flValuation;
    @GraphQLField(kkhtml = "KFieldText", label = "数量", sql = "quantity = $S{quantity}", field = "quantity")
    private String quantity;
    @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "cny = $S{cny}", field = "cny")
    private String cny;
    @GraphQLField(kkhtml = "KFieldText", label = "持仓日期", sql = "holding_date = $S{holdingDate}", field = "holding_date")
    private String holdingDate;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}", field = "details")
    private String details;
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

    @GraphQLField(label = "报送日期")
    private String reportDate;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getProdRegEnc() {
        return prodRegEnc;
    }

    public void setProdRegEnc(String prodRegEnc) {
        this.prodRegEnc = prodRegEnc;
    }

    public String getHoldingType() {
        return holdingType;
    }

    public void setHoldingType(String holdingType) {
        this.holdingType = holdingType;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getInvestedAsset() {
        return investedAsset;
    }

    public void setInvestedAsset(String investedAsset) {
        this.investedAsset = investedAsset;
    }

    public String getMezzanineNumber() {
        return mezzanineNumber;
    }

    public void setMezzanineNumber(String mezzanineNumber) {
        this.mezzanineNumber = mezzanineNumber;
    }

    public String getMezzanineAssetCode() {
        return mezzanineAssetCode;
    }

    public void setMezzanineAssetCode(String mezzanineAssetCode) {
        this.mezzanineAssetCode = mezzanineAssetCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getInvestedAmount() {
        return investedAmount;
    }

    public void setInvestedAmount(String investedAmount) {
        this.investedAmount = investedAmount;
    }

    public String getInvestedAmountCny() {
        return investedAmountCny;
    }

    public void setInvestedAmountCny(String investedAmountCny) {
        this.investedAmountCny = investedAmountCny;
    }

    public String getFairValue() {
        return fairValue;
    }

    public void setFairValue(String fairValue) {
        this.fairValue = fairValue;
    }

    public String getFairValueCny() {
        return fairValueCny;
    }

    public void setFairValueCny(String fairValueCny) {
        this.fairValueCny = fairValueCny;
    }

    public String getNetValuation() {
        return netValuation;
    }

    public void setNetValuation(String netValuation) {
        this.netValuation = netValuation;
    }

    public String getFlValuation() {
        return flValuation;
    }

    public void setFlValuation(String flValuation) {
        this.flValuation = flValuation;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCny() {
        return cny;
    }

    public void setCny(String cny) {
        this.cny = cny;
    }

    public String getHoldingDate() {
        return holdingDate;
    }

    public void setHoldingDate(String holdingDate) {
        this.holdingDate = holdingDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getReportStartDate() {
        return reportStartDate;
    }

    public void setReportStartDate(String reportStartDate) {
        this.reportStartDate = reportStartDate;
    }

    public String getReportEndDate() {
        return reportEndDate;
    }

    public void setReportEndDate(String reportEndDate) {
        this.reportEndDate = reportEndDate;
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