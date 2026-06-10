package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

@GraphQLModel(fetcher = "pprouctService",table = "a_fp_p_product")
public class Pprouct {
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "product_code = $S{productCode}" ,field = "product_code")
   private String productCode;
   @GraphQLField(kkhtml = "KFieldText", label = "托管人代码", sql = "custodian_code = $S{custodianCode}" ,field = "custodian_code")
   private String custodianCode;
   @GraphQLField(kkhtml = "KFieldText", label = "资管系统代码", sql = "asset_code = $S{assetCode}" ,field = "asset_code")
   private String assetCode;
   @GraphQLField(kkhtml = "KFieldText", label = "基金经理代码", sql = "fund_manager_code = $S{fundManagerCode}" ,field = "fund_manager_code")
   private String fundManagerCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品分类代码", sql = "product_type = $S{productType}" ,field = "product_type")
   private String productType;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "product_name = $S{productName}" ,field = "product_name")
   private String productName;
   @GraphQLField(kkhtml = "KFieldText", label = "产品别名", sql = "product_alias_name = $S{productAliasName}" ,field = "product_alias_name")
   private String productAliasName;
   @GraphQLField(kkhtml = "KFieldText", label = "产品英文名称", sql = "product_english_name = $S{productEnglishName}" ,field = "product_english_name")
   private String productEnglishName;
   @GraphQLField(kkhtml = "KFieldText", label = "币种", sql = "currency_type = $S{currencyType}" ,field = "currency_type")
   private String currencyType;
   @GraphQLField(kkhtml = "KFieldText", label = "钞汇标志", sql = "cash_flag = $S{cashFlag}" ,field = "cash_flag")
   private String cashFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "面值", sql = "face_value = $S{faceValue}" ,field = "face_value")
   private String faceValue;
   @GraphQLField(kkhtml = "KFieldText", label = "发行价格", sql = "issue_price = $S{issuePrice}" ,field = "issue_price")
   private String issuePrice;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准", sql = "perfor_compare_norm = $S{perforCompareNorm}" ,field = "perfor_compare_norm")
   private String perforCompareNorm;
   @GraphQLField(kkhtml = "KFieldText", label = "同类产品收益率，参考值，无业务逻辑", sql = "similar_product_income_rate = $S{similarProductIncomeRate}" ,field = "similar_product_income_rate")
   private String similarProductIncomeRate;
   @GraphQLField(kkhtml = "KFieldText", label = "募集类型", sql = "ipo_type = $S{ipoType}" ,field = "ipo_type")
   private String ipoType;
   @GraphQLField(kkhtml = "KFieldText", label = "募集规模", sql = "product_size = $S{productSize}" ,field = "product_size")
   private String productSize;
   @GraphQLField(kkhtml = "KFieldText", label = "最低募集金额", sql = "min_raise = $S{minRaise}" ,field = "min_raise")
   private String minRaise;
   @GraphQLField(kkhtml = "KFieldText", label = "最高募集金额", sql = "max_raise = $S{maxRaise}" ,field = "max_raise")
   private String maxRaise;
   @GraphQLField(kkhtml = "KFieldText", label = "募集开始日期", sql = "ipo_start_date = $S{ipoStartDate}" ,field = "ipo_start_date")
   private String ipoStartDate;
   @GraphQLField(kkhtml = "KFieldText", label = "募集结束日期", sql = "ipo_end_date = $S{ipoEndDate}" ,field = "ipo_end_date")
   private String ipoEndDate;
   @GraphQLField(kkhtml = "KFieldText", label = "募集结束日交易截止时间", sql = "ipo_end_time = $S{ipoEndTime}" ,field = "ipo_end_time")
   private String ipoEndTime;
   @GraphQLField(kkhtml = "KFieldText", label = "募集期闭市时间", sql = "ipo_close_time = $S{ipoCloseTime}" ,field = "ipo_close_time")
   private String ipoCloseTime;
   @GraphQLField(kkhtml = "KFieldText", label = "认购计息截至日期", sql = "subscribe_interest_end = $S{subscribeInterestEnd}" ,field = "subscribe_interest_end")
   private String subscribeInterestEnd;
   @GraphQLField(kkhtml = "KFieldText", label = "认购是否计息", sql = "subscribe_interest_flag = $S{subscribeInterestFlag}" ,field = "subscribe_interest_flag")
   private String subscribeInterestFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "认购利息处理方式", sql = "subscribe_interest_deal_type = $S{subscribeInterestDealType}" ,field = "subscribe_interest_deal_type")
   private String subscribeInterestDealType;
   @GraphQLField(kkhtml = "KFieldText", label = "计息基数", sql = "rate_base = $S{rateBase}" ,field = "rate_base")
   private String rateBase;
   @GraphQLField(kkhtml = "KFieldText", label = "实际募集金额", sql = "collection_amount = $S{collectionAmount}" ,field = "collection_amount")
   private String collectionAmount;
   @GraphQLField(kkhtml = "KFieldText", label = "成立确认比例", sql = "establish_confirm_ratio = $S{establishConfirmRatio}" ,field = "establish_confirm_ratio")
   private String establishConfirmRatio;
   @GraphQLField(kkhtml = "KFieldText", label = "募集失败日期", sql = "fail_date = $S{failDate}" ,field = "fail_date")
   private String failDate;
   @GraphQLField(kkhtml = "KFieldText", label = "份额确认天数", sql = "confirm_days = $S{confirmDays}" ,field = "confirm_days")
   private String confirmDays;
   @GraphQLField(kkhtml = "KFieldText", label = "认购到账天数", sql = "subscribe_days = $S{subscribeDays}" ,field = "subscribe_days")
   private String subscribeDays;
   @GraphQLField(kkhtml = "KFieldText", label = "申购到账天数", sql = "purchase_days = $S{purchaseDays}" ,field = "purchase_days")
   private String purchaseDays;
   @GraphQLField(kkhtml = "KFieldText", label = "赎回到账天数", sql = "redeem_days = $S{redeemDays}" ,field = "redeem_days")
   private String redeemDays;
   @GraphQLField(kkhtml = "KFieldText", label = "分红到账天数", sql = "bonus_days = $S{bonusDays}" ,field = "bonus_days")
   private String bonusDays;
   @GraphQLField(kkhtml = "KFieldText", label = "认申购退款天数", sql = "refund_days = $S{refundDays}" ,field = "refund_days")
   private String refundDays;
   @GraphQLField(kkhtml = "KFieldText", label = "发行失败到账天数", sql = "fail_days = $S{failDays}" ,field = "fail_days")
   private String failDays;
   @GraphQLField(kkhtml = "KFieldText", label = "产品转换清算天数", sql = "convert_days = $S{convertDays}" ,field = "convert_days")
   private String convertDays;
   @GraphQLField(kkhtml = "KFieldText", label = "个人最低持有份额", sql = "min_hold_indi = $S{minHoldIndi}" ,field = "min_hold_indi")
   private String minHoldIndi;
   @GraphQLField(kkhtml = "KFieldText", label = "法人最低持有份额", sql = "min_hold_inst = $S{minHoldInst}" ,field = "min_hold_inst")
   private String minHoldInst;
   @GraphQLField(kkhtml = "KFieldText", label = "同业最低持有份额", sql = "min_hold_inte = $S{minHoldInte}" ,field = "min_hold_inte")
   private String minHoldInte;
   @GraphQLField(kkhtml = "KFieldText", label = "最高持有天数", sql = "max_hold_days = $S{maxHoldDays}" ,field = "max_hold_days")
   private String maxHoldDays;
   @GraphQLField(kkhtml = "KFieldText", label = "最低持有天数", sql = "min_hold_days = $S{minHoldDays}" ,field = "min_hold_days")
   private String minHoldDays;
   @GraphQLField(kkhtml = "KFieldText", label = "认申购撤单标志", sql = "subscribe_cancel_flag = $S{subscribeCancelFlag}" ,field = "subscribe_cancel_flag")
   private String subscribeCancelFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "允许修改分红方式", sql = "alter_dividend_flag = $S{alterDividendFlag}" ,field = "alter_dividend_flag")
   private String alterDividendFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "默认分红方式", sql = "default_dividend_mode = $S{defaultDividendMode}" ,field = "default_dividend_mode")
   private String defaultDividendMode;
   @GraphQLField(kkhtml = "KFieldText", label = "赎回明细处理方式", sql = "sharedetail_mode = $S{sharedetailMode}" ,field = "sharedetail_mode")
   private String sharedetailMode;
   @GraphQLField(kkhtml = "KFieldText", label = "允许购买客户类型", sql = "allot_client_type = $S{allotClientType}" ,field = "allot_client_type")
   private String allotClientType;
   @GraphQLField(kkhtml = "KFieldText", label = "是否允许实时赎回", sql = "fast_redeem_flag = $S{fastRedeemFlag}" ,field = "fast_redeem_flag")
   private String fastRedeemFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "实时赎回质押比例", sql = "fast_redeem_pledge_ratio = $S{fastRedeemPledgeRatio}" ,field = "fast_redeem_pledge_ratio")
   private String fastRedeemPledgeRatio;
   @GraphQLField(kkhtml = "KFieldText", label = "实时赎回资金占用费率", sql = "fast_redeem_occupy_fee = $S{fastRedeemOccupyFee}" ,field = "fast_redeem_occupy_fee")
   private String fastRedeemOccupyFee;
   @GraphQLField(kkhtml = "KFieldText", label = "转换费归基金资产比例", sql = "change_fare_ratio = $S{changeFareRatio}" ,field = "change_fare_ratio")
   private String changeFareRatio;
   @GraphQLField(kkhtml = "KFieldText", label = "巨额赎回比例", sql = "large_redeem_radio = $S{largeRedeemRadio}" ,field = "large_redeem_radio")
   private String largeRedeemRadio;
   @GraphQLField(kkhtml = "KFieldText", label = "超限认购处理方式", sql = "over_subscribe_confirm = $S{overSubscribeConfirm}" ,field = "over_subscribe_confirm")
   private String overSubscribeConfirm;
   @GraphQLField(kkhtml = "KFieldText", label = "申购超额处理方式", sql = "over_purchase_confirm = $S{overPurchaseConfirm}" ,field = "over_purchase_confirm")
   private String overPurchaseConfirm;
   @GraphQLField(kkhtml = "KFieldText", label = "超额申购比例", sql = "excess_purchase_radio = $S{excessPurchaseRadio}" ,field = "excess_purchase_radio")
   private String excessPurchaseRadio;
   @GraphQLField(kkhtml = "KFieldText", label = "个人认购期利率", sql = "interest_individual = $S{interestIndividual}" ,field = "interest_individual")
   private String interestIndividual;
   @GraphQLField(kkhtml = "KFieldText", label = "同业认购期利率", sql = "interest_interbank = $S{interestInterbank}" ,field = "interest_interbank")
   private String interestInterbank;
   @GraphQLField(kkhtml = "KFieldText", label = "法人认购期利率", sql = "interest_institution = $S{interestInstitution}" ,field = "interest_institution")
   private String interestInstitution;
   @GraphQLField(kkhtml = "KFieldText", label = "生命周期状态", sql = "product_lifecycle_status = $S{productLifecycleStatus}" ,field = "product_lifecycle_status")
   private String productLifecycleStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "产品交易状态", sql = "transaction_status = $S{transactionStatus}" ,field = "transaction_status")
   private String transactionStatus;
   @GraphQLField(kkhtml = "KFieldText", label = "风险等级", sql = "risk_level = $S{riskLevel}" ,field = "risk_level")
   private String riskLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "是否允许未成年人购买", sql = "juveniles_enable = $S{juvenilesEnable}" ,field = "juveniles_enable")
   private String juvenilesEnable;
   @GraphQLField(kkhtml = "KFieldText", label = "最小现金分红", sql = "min_bonus = $S{minBonus}" ,field = "min_bonus")
   private String minBonus;
   @GraphQLField(kkhtml = "KFieldText", label = "工作日方案代码", sql = "workday_suite_code = $S{workdaySuiteCode}" ,field = "workday_suite_code")
   private String workdaySuiteCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品成立日", sql = "establish_date = $S{establishDate}" ,field = "establish_date")
   private String establishDate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品到期日", sql = "due_date = $S{dueDate}" ,field = "due_date")
   private String dueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "到期是否自动清盘", sql = "auto_due = $S{autoDue}" ,field = "auto_due")
   private String autoDue;
   @GraphQLField(kkhtml = "KFieldText", label = "产品清盘日，手工清盘日", sql = "clean_date = $S{cleanDate}" ,field = "clean_date")
   private String cleanDate;
   @GraphQLField(kkhtml = "KFieldText", label = "是否跟随系统开市", sql = "open_with_system = $S{openWithSystem}" ,field = "open_with_system")
   private String openWithSystem;
   @GraphQLField(kkhtml = "KFieldText", label = "开市时间，当跟随系统开市时为空，不跟随系统开市时必填，并且开市时间必须小于当天最后一个预约转受理批处理时间", sql = "open_time = $S{openTime}" ,field = "open_time")
   private String openTime;
   @GraphQLField(kkhtml = "KFieldText", label = "闭市时间", sql = "close_time = $S{closeTime}" ,field = "close_time")
   private String closeTime;
   @GraphQLField(kkhtml = "KFieldText", label = "开放类型", sql = "open_type = $S{openType}" ,field = "open_type")
   private String openType;
   @GraphQLField(kkhtml = "KFieldText", label = "最大预约认购天数", sql = "max_subscribe_days = $S{maxSubscribeDays}" ,field = "max_subscribe_days")
   private String maxSubscribeDays;
   @GraphQLField(kkhtml = "KFieldText", label = "最大预约申购天数", sql = "max_purchase_days = $S{maxPurchaseDays}" ,field = "max_purchase_days")
   private String maxPurchaseDays;
   @GraphQLField(kkhtml = "KFieldText", label = "最大预约赎回天数", sql = "max_redeem_days = $S{maxRedeemDays}" ,field = "max_redeem_days")
   private String maxRedeemDays;
   @GraphQLField(kkhtml = "KFieldText", label = "当前工作日", sql = "current_workdate = $S{currentWorkdate}" ,field = "current_workdate")
   private String currentWorkdate;
   @GraphQLField(kkhtml = "KFieldText", label = "产品当前净值", sql = "netvalue = $S{netvalue}" ,field = "netvalue")
   private String netvalue;
   @GraphQLField(kkhtml = "KFieldText", label = "产品当前净值日", sql = "netvalue_date = $S{netvalueDate}" ,field = "netvalue_date")
   private String netvalueDate;
   @GraphQLField(kkhtml = "KFieldText", label = "昨日总份额", sql = "yesterday_total_share = $S{yesterdayTotalShare}" ,field = "yesterday_total_share")
   private String yesterdayTotalShare;
   @GraphQLField(kkhtml = "KFieldText", label = "额度控制标志", sql = "quota_flag = $S{quotaFlag}" ,field = "quota_flag")
   private String quotaFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "按目标客户划分产品类型（只维护，没有业务逻辑）", sql = "target_product_type = $S{targetProductType}" ,field = "target_product_type")
   private String targetProductType;
   @GraphQLField(kkhtml = "KFieldText", label = "费用折扣方案ID", sql = "agio_id = $S{agioId}" ,field = "agio_id")
   private String agioId;
   @GraphQLField(kkhtml = "KFieldText", label = "费用方案ID", sql = "fee_id = $S{feeId}" ,field = "fee_id")
   private String feeId;
   @GraphQLField(kkhtml = "KFieldText", label = "产品说明书文档编号", sql = "instruction_document_no = $S{instructionDocumentNo}" ,field = "instruction_document_no")
   private String instructionDocumentNo;
   @GraphQLField(kkhtml = "KFieldText", label = "风险揭示书文档编号", sql = "risk_document_no = $S{riskDocumentNo}" ,field = "risk_document_no")
   private String riskDocumentNo;
   @GraphQLField(kkhtml = "KFieldText", label = "购买协议书文档编号", sql = "agreement_document_no = $S{agreementDocumentNo}" ,field = "agreement_document_no")
   private String agreementDocumentNo;
   @GraphQLField(kkhtml = "KFieldText", label = "客户权益须知文档编号", sql = "right_document_no = $S{rightDocumentNo}" ,field = "right_document_no")
   private String rightDocumentNo;
   @GraphQLField(kkhtml = "KFieldText", label = "最近一个购买日，在产品状态预设和系统切日时根据产品状态预设情况更新为最近一个产品可购买日", sql = "recent_purchase_date = $S{recentPurchaseDate}" ,field = "recent_purchase_date")
   private String recentPurchaseDate;
   @GraphQLField(kkhtml = "KFieldText", label = "最近一个赎回日，在产品状态预设和系统切日时根据产品状态预设情况更新为最近一个产品可赎回日", sql = "recent_redeem_date = $S{recentRedeemDate}" ,field = "recent_redeem_date")
   private String recentRedeemDate;
   @GraphQLField(kkhtml = "KFieldText", label = "最近一个T+N跑批生效日期", sql = "recent_tn_effect = $S{recentTnEffect}" ,field = "recent_tn_effect")
   private String recentTnEffect;
   @GraphQLField(kkhtml = "KFieldText", label = "工作时段开始时间", sql = "start_working_time = $S{startWorkingTime}" ,field = "start_working_time")
   private String startWorkingTime;
   @GraphQLField(kkhtml = "KFieldText", label = "工作时段结束时间", sql = "end_working_time = $S{endWorkingTime}" ,field = "end_working_time")
   private String endWorkingTime;
   @GraphQLField(kkhtml = "KFieldText", label = "最后更新日期", sql = "update_date = $S{updateDate}" ,field = "update_date")
   private String updateDate;
   @GraphQLField(kkhtml = "KFieldText", label = "操作员", sql = "operator_no = $S{operatorNo}" ,field = "operator_no")
   private String operatorNo;
   @GraphQLField(kkhtml = "KFieldText", label = "复核员", sql = "checker_no = $S{checkerNo}" ,field = "checker_no")
   private String checkerNo;
   @GraphQLField(kkhtml = "KFieldText", label = "T+N跑批生效日期，T+N生效时更新", sql = "effect_date = $S{effectDate}" ,field = "effect_date")
   private String effectDate;
   @GraphQLField(kkhtml = "KFieldText", label = "是否限制投资者名额，0-否；1-是", sql = "investor_quota_flag = $S{investorQuotaFlag}" ,field = "investor_quota_flag")
   private String investorQuotaFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "投资者名额上限", sql = "max_investor_quota = $S{maxInvestorQuota}" ,field = "max_investor_quota")
   private String maxInvestorQuota;
   @GraphQLField(kkhtml = "KFieldText", label = "收益计算天数", sql = "profit_start_days = $S{profitStartDays}" ,field = "profit_start_days")
   private String profitStartDays;
   @GraphQLField(kkhtml = "KFieldText", label = "收益结转频率", sql = "profit_forward_frequency = $S{profitForwardFrequency}" ,field = "profit_forward_frequency")
   private String profitForwardFrequency;
   @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "product_regist_code = $S{productRegistCode}" ,field = "product_regist_code")
   private String productRegistCode;
   @GraphQLField(kkhtml = "KFieldText", label = "产品天数", sql = "product_days = $S{productDays}" ,field = "product_days")
   private String productDays;
   @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准", sql = "perfor_compare_norm_text = $S{perforCompareNormText}" ,field = "perfor_compare_norm_text")
   private String perforCompareNormText;
   @GraphQLField(kkhtml = "KFieldText", label = "自营或代理标志，0-代理；1-自营", sql = "self_support_flag = $S{selfSupportFlag}" ,field = "self_support_flag")
   private String selfSupportFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "是否需要播讲，0-否；1-是", sql = "play_explain_flag = $S{playExplainFlag}" ,field = "play_explain_flag")
   private String playExplainFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "是否强制播讲，0-否；2-是", sql = "force_play_explain_flag = $S{forcePlayExplainFlag}" ,field = "force_play_explain_flag")
   private String forcePlayExplainFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "上一投资周期涨跌幅", sql = "last_investment_rise_and_fall = $S{lastInvestmentRiseAndFall}" ,field = "last_investment_rise_and_fall")
   private String lastInvestmentRiseAndFall;
   @GraphQLField(kkhtml = "KFieldText", label = "上一投资周期年化收益率", sql = "last_investment_annual_rate = $S{lastInvestmentAnnualRate}" ,field = "last_investment_annual_rate")
   private String lastInvestmentAnnualRate;
   @GraphQLField(kkhtml = "KFieldText", label = "是否蚂蚁专供", sql = "exclusive_ant_flag = $S{exclusiveAntFlag}" ,field = "exclusive_ant_flag")
   private String exclusiveAntFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "产品属性标志", sql = "product_attr_flag = $S{productAttrFlag}" ,field = "product_attr_flag")
   private String productAttrFlag;
   @GraphQLField(kkhtml = "KFieldText", label = "收益率显示类型", sql = "yield_show_type = $S{yieldShowType}" ,field = "yield_show_type")
   private String yieldShowType;
   
  	public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
  	public String getCustodianCode() {
        return custodianCode;
    }

    public void setCustodianCode(String custodianCode) {
        this.custodianCode = custodianCode;
    }
  	public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
  	public String getFundManagerCode() {
        return fundManagerCode;
    }

    public void setFundManagerCode(String fundManagerCode) {
        this.fundManagerCode = fundManagerCode;
    }
  	public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
  	public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
  	public String getProductAliasName() {
        return productAliasName;
    }

    public void setProductAliasName(String productAliasName) {
        this.productAliasName = productAliasName;
    }
  	public String getProductEnglishName() {
        return productEnglishName;
    }

    public void setProductEnglishName(String productEnglishName) {
        this.productEnglishName = productEnglishName;
    }
  	public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
  	public String getCashFlag() {
        return cashFlag;
    }

    public void setCashFlag(String cashFlag) {
        this.cashFlag = cashFlag;
    }
  	public String getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(String faceValue) {
        this.faceValue = faceValue;
    }
  	public String getIssuePrice() {
        return issuePrice;
    }

    public void setIssuePrice(String issuePrice) {
        this.issuePrice = issuePrice;
    }
  	public String getPerforCompareNorm() {
        return perforCompareNorm;
    }

    public void setPerforCompareNorm(String perforCompareNorm) {
        this.perforCompareNorm = perforCompareNorm;
    }
  	public String getSimilarProductIncomeRate() {
        return similarProductIncomeRate;
    }

    public void setSimilarProductIncomeRate(String similarProductIncomeRate) {
        this.similarProductIncomeRate = similarProductIncomeRate;
    }
  	public String getIpoType() {
        return ipoType;
    }

    public void setIpoType(String ipoType) {
        this.ipoType = ipoType;
    }
  	public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }
  	public String getMinRaise() {
        return minRaise;
    }

    public void setMinRaise(String minRaise) {
        this.minRaise = minRaise;
    }
  	public String getMaxRaise() {
        return maxRaise;
    }

    public void setMaxRaise(String maxRaise) {
        this.maxRaise = maxRaise;
    }
  	public String getIpoStartDate() {
        return ipoStartDate;
    }

    public void setIpoStartDate(String ipoStartDate) {
        this.ipoStartDate = ipoStartDate;
    }
  	public String getIpoEndDate() {
        return ipoEndDate;
    }

    public void setIpoEndDate(String ipoEndDate) {
        this.ipoEndDate = ipoEndDate;
    }
  	public String getIpoEndTime() {
        return ipoEndTime;
    }

    public void setIpoEndTime(String ipoEndTime) {
        this.ipoEndTime = ipoEndTime;
    }
  	public String getIpoCloseTime() {
        return ipoCloseTime;
    }

    public void setIpoCloseTime(String ipoCloseTime) {
        this.ipoCloseTime = ipoCloseTime;
    }
  	public String getSubscribeInterestEnd() {
        return subscribeInterestEnd;
    }

    public void setSubscribeInterestEnd(String subscribeInterestEnd) {
        this.subscribeInterestEnd = subscribeInterestEnd;
    }
  	public String getSubscribeInterestFlag() {
        return subscribeInterestFlag;
    }

    public void setSubscribeInterestFlag(String subscribeInterestFlag) {
        this.subscribeInterestFlag = subscribeInterestFlag;
    }
  	public String getSubscribeInterestDealType() {
        return subscribeInterestDealType;
    }

    public void setSubscribeInterestDealType(String subscribeInterestDealType) {
        this.subscribeInterestDealType = subscribeInterestDealType;
    }
  	public String getRateBase() {
        return rateBase;
    }

    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
  	public String getCollectionAmount() {
        return collectionAmount;
    }

    public void setCollectionAmount(String collectionAmount) {
        this.collectionAmount = collectionAmount;
    }
  	public String getEstablishConfirmRatio() {
        return establishConfirmRatio;
    }

    public void setEstablishConfirmRatio(String establishConfirmRatio) {
        this.establishConfirmRatio = establishConfirmRatio;
    }
  	public String getFailDate() {
        return failDate;
    }

    public void setFailDate(String failDate) {
        this.failDate = failDate;
    }
  	public String getConfirmDays() {
        return confirmDays;
    }

    public void setConfirmDays(String confirmDays) {
        this.confirmDays = confirmDays;
    }
  	public String getSubscribeDays() {
        return subscribeDays;
    }

    public void setSubscribeDays(String subscribeDays) {
        this.subscribeDays = subscribeDays;
    }
  	public String getPurchaseDays() {
        return purchaseDays;
    }

    public void setPurchaseDays(String purchaseDays) {
        this.purchaseDays = purchaseDays;
    }
  	public String getRedeemDays() {
        return redeemDays;
    }

    public void setRedeemDays(String redeemDays) {
        this.redeemDays = redeemDays;
    }
  	public String getBonusDays() {
        return bonusDays;
    }

    public void setBonusDays(String bonusDays) {
        this.bonusDays = bonusDays;
    }
  	public String getRefundDays() {
        return refundDays;
    }

    public void setRefundDays(String refundDays) {
        this.refundDays = refundDays;
    }
  	public String getFailDays() {
        return failDays;
    }

    public void setFailDays(String failDays) {
        this.failDays = failDays;
    }
  	public String getConvertDays() {
        return convertDays;
    }

    public void setConvertDays(String convertDays) {
        this.convertDays = convertDays;
    }
  	public String getMinHoldIndi() {
        return minHoldIndi;
    }

    public void setMinHoldIndi(String minHoldIndi) {
        this.minHoldIndi = minHoldIndi;
    }
  	public String getMinHoldInst() {
        return minHoldInst;
    }

    public void setMinHoldInst(String minHoldInst) {
        this.minHoldInst = minHoldInst;
    }
  	public String getMinHoldInte() {
        return minHoldInte;
    }

    public void setMinHoldInte(String minHoldInte) {
        this.minHoldInte = minHoldInte;
    }
  	public String getMaxHoldDays() {
        return maxHoldDays;
    }

    public void setMaxHoldDays(String maxHoldDays) {
        this.maxHoldDays = maxHoldDays;
    }
  	public String getMinHoldDays() {
        return minHoldDays;
    }

    public void setMinHoldDays(String minHoldDays) {
        this.minHoldDays = minHoldDays;
    }
  	public String getSubscribeCancelFlag() {
        return subscribeCancelFlag;
    }

    public void setSubscribeCancelFlag(String subscribeCancelFlag) {
        this.subscribeCancelFlag = subscribeCancelFlag;
    }
  	public String getAlterDividendFlag() {
        return alterDividendFlag;
    }

    public void setAlterDividendFlag(String alterDividendFlag) {
        this.alterDividendFlag = alterDividendFlag;
    }
  	public String getDefaultDividendMode() {
        return defaultDividendMode;
    }

    public void setDefaultDividendMode(String defaultDividendMode) {
        this.defaultDividendMode = defaultDividendMode;
    }
  	public String getSharedetailMode() {
        return sharedetailMode;
    }

    public void setSharedetailMode(String sharedetailMode) {
        this.sharedetailMode = sharedetailMode;
    }
  	public String getAllotClientType() {
        return allotClientType;
    }

    public void setAllotClientType(String allotClientType) {
        this.allotClientType = allotClientType;
    }
  	public String getFastRedeemFlag() {
        return fastRedeemFlag;
    }

    public void setFastRedeemFlag(String fastRedeemFlag) {
        this.fastRedeemFlag = fastRedeemFlag;
    }
  	public String getFastRedeemPledgeRatio() {
        return fastRedeemPledgeRatio;
    }

    public void setFastRedeemPledgeRatio(String fastRedeemPledgeRatio) {
        this.fastRedeemPledgeRatio = fastRedeemPledgeRatio;
    }
  	public String getFastRedeemOccupyFee() {
        return fastRedeemOccupyFee;
    }

    public void setFastRedeemOccupyFee(String fastRedeemOccupyFee) {
        this.fastRedeemOccupyFee = fastRedeemOccupyFee;
    }
  	public String getChangeFareRatio() {
        return changeFareRatio;
    }

    public void setChangeFareRatio(String changeFareRatio) {
        this.changeFareRatio = changeFareRatio;
    }
  	public String getLargeRedeemRadio() {
        return largeRedeemRadio;
    }

    public void setLargeRedeemRadio(String largeRedeemRadio) {
        this.largeRedeemRadio = largeRedeemRadio;
    }
  	public String getOverSubscribeConfirm() {
        return overSubscribeConfirm;
    }

    public void setOverSubscribeConfirm(String overSubscribeConfirm) {
        this.overSubscribeConfirm = overSubscribeConfirm;
    }
  	public String getOverPurchaseConfirm() {
        return overPurchaseConfirm;
    }

    public void setOverPurchaseConfirm(String overPurchaseConfirm) {
        this.overPurchaseConfirm = overPurchaseConfirm;
    }
  	public String getExcessPurchaseRadio() {
        return excessPurchaseRadio;
    }

    public void setExcessPurchaseRadio(String excessPurchaseRadio) {
        this.excessPurchaseRadio = excessPurchaseRadio;
    }
  	public String getInterestIndividual() {
        return interestIndividual;
    }

    public void setInterestIndividual(String interestIndividual) {
        this.interestIndividual = interestIndividual;
    }
  	public String getInterestInterbank() {
        return interestInterbank;
    }

    public void setInterestInterbank(String interestInterbank) {
        this.interestInterbank = interestInterbank;
    }
  	public String getInterestInstitution() {
        return interestInstitution;
    }

    public void setInterestInstitution(String interestInstitution) {
        this.interestInstitution = interestInstitution;
    }
  	public String getProductLifecycleStatus() {
        return productLifecycleStatus;
    }

    public void setProductLifecycleStatus(String productLifecycleStatus) {
        this.productLifecycleStatus = productLifecycleStatus;
    }
  	public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
  	public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
  	public String getJuvenilesEnable() {
        return juvenilesEnable;
    }

    public void setJuvenilesEnable(String juvenilesEnable) {
        this.juvenilesEnable = juvenilesEnable;
    }
  	public String getMinBonus() {
        return minBonus;
    }

    public void setMinBonus(String minBonus) {
        this.minBonus = minBonus;
    }
  	public String getWorkdaySuiteCode() {
        return workdaySuiteCode;
    }

    public void setWorkdaySuiteCode(String workdaySuiteCode) {
        this.workdaySuiteCode = workdaySuiteCode;
    }
  	public String getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(String establishDate) {
        this.establishDate = establishDate;
    }
  	public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
  	public String getAutoDue() {
        return autoDue;
    }

    public void setAutoDue(String autoDue) {
        this.autoDue = autoDue;
    }
  	public String getCleanDate() {
        return cleanDate;
    }

    public void setCleanDate(String cleanDate) {
        this.cleanDate = cleanDate;
    }
  	public String getOpenWithSystem() {
        return openWithSystem;
    }

    public void setOpenWithSystem(String openWithSystem) {
        this.openWithSystem = openWithSystem;
    }
  	public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
  	public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
  	public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }
  	public String getMaxSubscribeDays() {
        return maxSubscribeDays;
    }

    public void setMaxSubscribeDays(String maxSubscribeDays) {
        this.maxSubscribeDays = maxSubscribeDays;
    }
  	public String getMaxPurchaseDays() {
        return maxPurchaseDays;
    }

    public void setMaxPurchaseDays(String maxPurchaseDays) {
        this.maxPurchaseDays = maxPurchaseDays;
    }
  	public String getMaxRedeemDays() {
        return maxRedeemDays;
    }

    public void setMaxRedeemDays(String maxRedeemDays) {
        this.maxRedeemDays = maxRedeemDays;
    }
  	public String getCurrentWorkdate() {
        return currentWorkdate;
    }

    public void setCurrentWorkdate(String currentWorkdate) {
        this.currentWorkdate = currentWorkdate;
    }
  	public String getNetvalue() {
        return netvalue;
    }

    public void setNetvalue(String netvalue) {
        this.netvalue = netvalue;
    }
  	public String getNetvalueDate() {
        return netvalueDate;
    }

    public void setNetvalueDate(String netvalueDate) {
        this.netvalueDate = netvalueDate;
    }
  	public String getYesterdayTotalShare() {
        return yesterdayTotalShare;
    }

    public void setYesterdayTotalShare(String yesterdayTotalShare) {
        this.yesterdayTotalShare = yesterdayTotalShare;
    }
  	public String getQuotaFlag() {
        return quotaFlag;
    }

    public void setQuotaFlag(String quotaFlag) {
        this.quotaFlag = quotaFlag;
    }
  	public String getTargetProductType() {
        return targetProductType;
    }

    public void setTargetProductType(String targetProductType) {
        this.targetProductType = targetProductType;
    }
  	public String getAgioId() {
        return agioId;
    }

    public void setAgioId(String agioId) {
        this.agioId = agioId;
    }
  	public String getFeeId() {
        return feeId;
    }

    public void setFeeId(String feeId) {
        this.feeId = feeId;
    }
  	public String getInstructionDocumentNo() {
        return instructionDocumentNo;
    }

    public void setInstructionDocumentNo(String instructionDocumentNo) {
        this.instructionDocumentNo = instructionDocumentNo;
    }
  	public String getRiskDocumentNo() {
        return riskDocumentNo;
    }

    public void setRiskDocumentNo(String riskDocumentNo) {
        this.riskDocumentNo = riskDocumentNo;
    }
  	public String getAgreementDocumentNo() {
        return agreementDocumentNo;
    }

    public void setAgreementDocumentNo(String agreementDocumentNo) {
        this.agreementDocumentNo = agreementDocumentNo;
    }
  	public String getRightDocumentNo() {
        return rightDocumentNo;
    }

    public void setRightDocumentNo(String rightDocumentNo) {
        this.rightDocumentNo = rightDocumentNo;
    }
  	public String getRecentPurchaseDate() {
        return recentPurchaseDate;
    }

    public void setRecentPurchaseDate(String recentPurchaseDate) {
        this.recentPurchaseDate = recentPurchaseDate;
    }
  	public String getRecentRedeemDate() {
        return recentRedeemDate;
    }

    public void setRecentRedeemDate(String recentRedeemDate) {
        this.recentRedeemDate = recentRedeemDate;
    }
  	public String getRecentTnEffect() {
        return recentTnEffect;
    }

    public void setRecentTnEffect(String recentTnEffect) {
        this.recentTnEffect = recentTnEffect;
    }
  	public String getStartWorkingTime() {
        return startWorkingTime;
    }

    public void setStartWorkingTime(String startWorkingTime) {
        this.startWorkingTime = startWorkingTime;
    }
  	public String getEndWorkingTime() {
        return endWorkingTime;
    }

    public void setEndWorkingTime(String endWorkingTime) {
        this.endWorkingTime = endWorkingTime;
    }
  	public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
  	public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }
  	public String getCheckerNo() {
        return checkerNo;
    }

    public void setCheckerNo(String checkerNo) {
        this.checkerNo = checkerNo;
    }
  	public String getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(String effectDate) {
        this.effectDate = effectDate;
    }
  	public String getInvestorQuotaFlag() {
        return investorQuotaFlag;
    }

    public void setInvestorQuotaFlag(String investorQuotaFlag) {
        this.investorQuotaFlag = investorQuotaFlag;
    }
  	public String getMaxInvestorQuota() {
        return maxInvestorQuota;
    }

    public void setMaxInvestorQuota(String maxInvestorQuota) {
        this.maxInvestorQuota = maxInvestorQuota;
    }
  	public String getProfitStartDays() {
        return profitStartDays;
    }

    public void setProfitStartDays(String profitStartDays) {
        this.profitStartDays = profitStartDays;
    }
  	public String getProfitForwardFrequency() {
        return profitForwardFrequency;
    }

    public void setProfitForwardFrequency(String profitForwardFrequency) {
        this.profitForwardFrequency = profitForwardFrequency;
    }
  	public String getProductRegistCode() {
        return productRegistCode;
    }

    public void setProductRegistCode(String productRegistCode) {
        this.productRegistCode = productRegistCode;
    }
  	public String getProductDays() {
        return productDays;
    }

    public void setProductDays(String productDays) {
        this.productDays = productDays;
    }
  	public String getPerforCompareNormText() {
        return perforCompareNormText;
    }

    public void setPerforCompareNormText(String perforCompareNormText) {
        this.perforCompareNormText = perforCompareNormText;
    }
  	public String getSelfSupportFlag() {
        return selfSupportFlag;
    }

    public void setSelfSupportFlag(String selfSupportFlag) {
        this.selfSupportFlag = selfSupportFlag;
    }
  	public String getPlayExplainFlag() {
        return playExplainFlag;
    }

    public void setPlayExplainFlag(String playExplainFlag) {
        this.playExplainFlag = playExplainFlag;
    }
  	public String getForcePlayExplainFlag() {
        return forcePlayExplainFlag;
    }

    public void setForcePlayExplainFlag(String forcePlayExplainFlag) {
        this.forcePlayExplainFlag = forcePlayExplainFlag;
    }
  	public String getLastInvestmentRiseAndFall() {
        return lastInvestmentRiseAndFall;
    }

    public void setLastInvestmentRiseAndFall(String lastInvestmentRiseAndFall) {
        this.lastInvestmentRiseAndFall = lastInvestmentRiseAndFall;
    }
  	public String getLastInvestmentAnnualRate() {
        return lastInvestmentAnnualRate;
    }

    public void setLastInvestmentAnnualRate(String lastInvestmentAnnualRate) {
        this.lastInvestmentAnnualRate = lastInvestmentAnnualRate;
    }
  	public String getExclusiveAntFlag() {
        return exclusiveAntFlag;
    }

    public void setExclusiveAntFlag(String exclusiveAntFlag) {
        this.exclusiveAntFlag = exclusiveAntFlag;
    }
  	public String getProductAttrFlag() {
        return productAttrFlag;
    }

    public void setProductAttrFlag(String productAttrFlag) {
        this.productAttrFlag = productAttrFlag;
    }
  	public String getYieldShowType() {
        return yieldShowType;
    }

    public void setYieldShowType(String yieldShowType) {
        this.yieldShowType = yieldShowType;
    }

}