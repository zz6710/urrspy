package com.kayak.rpt.zz.errorInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assetRgInfoErrService",table = "app_under_asset_regist_info_erdesc")
public class AssetRgInfoErr {
    @GraphQLField(label = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码错误", sql = "bank_code_desc = $S{bankCodeDesc}", field = "bank_code_desc")
    private String bankCodeDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码错误", sql = "prod_reg_enc_desc like '%$U{prodRegEncDesc}%'", field = "prod_reg_enc_desc")
    private String prodRegEncDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "持仓类别错误", sql = "holding_type_desc = $S{holdingTypeDesc}", field = "holding_type_desc")
    private String holdingTypeDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "行内资产/负债编码错误", sql = "asset_code_desc = $S{assetCodeDesc}", field = "asset_code_desc")
    private String assetCodeDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "资产穿透情况错误", sql = "invested_asset_desc = $S{investedAssetDesc}", field = "invested_asset_desc")
    private String investedAssetDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "中间层数错误", sql = "mezzanine_number_desc = '%$U{mezzanineNumberDesc}%'", field = "mezzanine_number_desc")
    private String mezzanineNumberDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "中间层行内资产/负债编码错误", sql = "mezzanine_asset_code_desc = $S{mezzanineAssetCodeDesc}", field = "mezzanine_asset_code_desc")
    private String mezzanineAssetCodeDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "会计科目名称错误", sql = "account_code_desc = $S{accountCodeDesc}", field = "account_code_desc")
    private String accountCodeDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "金额错误", sql = "invested_amount_desc = $S{investedAmountDesc}", field = "invested_amount_desc")
    private String investedAmountDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "折算人民币金额错误", sql = "invested_amount_cny_desc = $S{investedAmountCnyDesc}", field = "invested_amount_cny_desc")
    private String investedAmountCnyDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "公允价值错误", sql = "fair_value_desc = $S{fairValueDesc}", field = "fair_value_desc")
    private String fairValueDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "折算人民币公允价错误", sql = "fair_value_cny_desc = $S{fairValueCnyv}", field = "fair_value_cny_desc")
    private String fairValueCnyDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "单位估值(净价)错误", sql = "net_valuation_desc = $S{netValuationDesc}", field = "netValuation_desc")
    private String netValuationDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "单位估值(全价)错误", sql = "fl_valuation_desc = $S{flValuationDesc}", field = "fl_valuation_desc")
    private String flValuationDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "数量错误", sql = "quantity _desc= $S{quantityDesc}", field = "quantity_desc")
    private String quantityDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "币种错误", sql = "cny_desc = $S{cnyDesc}", field = "cny_desc")
    private String cnyDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "持仓日期错误", sql = "holding_date_desc = $S{holdingDateDesc}", field = "holding_date_desc")
    private String holdingDateDesc;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}", field = "details")
    private String details;
    @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
    private String registerSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "导入日期", sql = "imp_date = $S{impDate}" ,field = "imp_date")
    private String impDate;
    @GraphQLField(label = "导入日期开始时间")
    private String impStartDate;

    @GraphQLField(label = "导入日期截止时间")
    private String impEndDate;
    @GraphQLField()
    private String registerDate;
    @GraphQLField()
    private String registerStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "createDate")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theoryReportStartDate")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theoryReportEndDate")
    private String theoryReportEndDate;


    @GraphQLField(kkhtml = "KFieldText", label = "报告日期", sql = "report_date = $S{reportDate}" ,field = "reportDate")
    private String reportDate;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankCodeDesc() {
        return bankCodeDesc;
    }

    public void setBankCodeDesc(String bankCodeDesc) {
        this.bankCodeDesc = bankCodeDesc;
    }

    public String getProdRegEncDesc() {
        return prodRegEncDesc;
    }

    public void setProdRegEncDesc(String prodRegEncDesc) {
        this.prodRegEncDesc = prodRegEncDesc;
    }

    public String getHoldingTypeDesc() {
        return holdingTypeDesc;
    }

    public void setHoldingTypeDesc(String holdingTypeDesc) {
        this.holdingTypeDesc = holdingTypeDesc;
    }

    public String getAssetCodeDesc() {
        return assetCodeDesc;
    }

    public void setAssetCodeDesc(String assetCodeDesc) {
        this.assetCodeDesc = assetCodeDesc;
    }

    public String getInvestedAssetDesc() {
        return investedAssetDesc;
    }

    public void setInvestedAssetDesc(String investedAssetDesc) {
        this.investedAssetDesc = investedAssetDesc;
    }

    public String getMezzanineNumberDesc() {
        return mezzanineNumberDesc;
    }

    public void setMezzanineNumberDesc(String mezzanineNumberDesc) {
        this.mezzanineNumberDesc = mezzanineNumberDesc;
    }

    public String getMezzanineAssetCodeDesc() {
        return mezzanineAssetCodeDesc;
    }

    public void setMezzanineAssetCodeDesc(String mezzanineAssetCodeDesc) {
        this.mezzanineAssetCodeDesc = mezzanineAssetCodeDesc;
    }

    public String getAccountCodeDesc() {
        return accountCodeDesc;
    }

    public void setAccountCodeDesc(String accountCodeDesc) {
        this.accountCodeDesc = accountCodeDesc;
    }

    public String getInvestedAmountDesc() {
        return investedAmountDesc;
    }

    public void setInvestedAmountDesc(String investedAmountDesc) {
        this.investedAmountDesc = investedAmountDesc;
    }

    public String getInvestedAmountCnyDesc() {
        return investedAmountCnyDesc;
    }

    public void setInvestedAmountCnyDesc(String investedAmountCnyDesc) {
        this.investedAmountCnyDesc = investedAmountCnyDesc;
    }

    public String getFairValueDesc() {
        return fairValueDesc;
    }

    public void setFairValueDesc(String fairValueDesc) {
        this.fairValueDesc = fairValueDesc;
    }

    public String getFairValueCnyDesc() {
        return fairValueCnyDesc;
    }

    public void setFairValueCnyDesc(String fairValueCnyDesc) {
        this.fairValueCnyDesc = fairValueCnyDesc;
    }

    public String getNetValuationDesc() {
        return netValuationDesc;
    }

    public void setNetValuationDesc(String netValuationDesc) {
        this.netValuationDesc = netValuationDesc;
    }

    public String getFlValuationDesc() {
        return flValuationDesc;
    }

    public void setFlValuationDesc(String flValuationDesc) {
        this.flValuationDesc = flValuationDesc;
    }

    public String getQuantityDesc() {
        return quantityDesc;
    }

    public void setQuantityDesc(String quantityDesc) {
        this.quantityDesc = quantityDesc;
    }

    public String getCnyDesc() { return cnyDesc; }

    public void setCnyDesc(String cnyDesc) { this.cnyDesc = cnyDesc; }

    public String getHoldingDateDesc() {
        return holdingDateDesc;
    }

    public void setHoldingDateDesc(String holdingDateDesc) {
        this.holdingDateDesc = holdingDateDesc;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRegisterSerno() {
        return registerSerno;
    }

    public String getImpStartDate() {
        return impStartDate;
    }

    public void setImpStartDate(String impStartDate) {
        this.impStartDate = impStartDate;
    }

    public String getImpEndDate() {
        return impEndDate;
    }

    public void setImpEndDate(String impEndDate) {
        this.impEndDate = impEndDate;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTheoryReportStartDate() {
        return theoryReportStartDate;
    }

    public void setTheoryReportStartDate(String theoryReportStartDate) {
        this.theoryReportStartDate = theoryReportStartDate;
    }

    public String getTheoryReportEndDate() {
        return theoryReportEndDate;
    }

    public void setTheoryReportEndDate(String theoryReportEndDate) {
        this.theoryReportEndDate = theoryReportEndDate;
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


}