package com.kayak.pms.T81.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "t8ProdInfoService", table = "T8_PROD_INFO")
public class T8ProdInfo {

    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "t.id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品模型", sql = "prod_mode = $S{prodMode}", field = "prod_mode")
    private String prodMode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品模型模板id", sql = "prod_mode_id = $S{prodModeId}", field = "prod_mode_id")
    private String prodModeId;
    @GraphQLField(kkhtml = "KFieldText", label = "产品系列", sql = "prod_series = $S{prodSeries}", field = "prod_series")
    private String prodSeries;
    @GraphQLField
    private String prodSonSeries;
    @GraphQLField(kkhtml = "KFieldSelect", label = "产品代码", sql = "t.prod_code IN ($S{prodCode})", field = "prod_code", kkhtmlExt = "{\"data-action\":\"T8ProdInfo.findT8ProdInfos\",\"data-display-field\":\"prodCode,prodName\",\"data-value-field\":\"prodCode\"}")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldSelect", label = "产品id-代码", sql = "t.id_and_code IN ($S{idAndCode})", field = "id_and_code")
    private String idAndCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name like '%$U{prodName}%'", field = "prod_name", kkhtmlDefault = true)
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "产品品牌", sql = "prod_brand = $S{prodBrand}", field = "prod_brand")
    private String prodBrand;
    @GraphQLField(kkhtml = "KFieldText", label = "中债登记编码", sql = "regist_code = $S{registCode}", field = "regist_code")
    private String registCode;
    @GraphQLField(kkhtml = "KFieldText", label = "是否关联创意", sql = "is_originality = $S{isOriginality}", field = "is_originality")
    private String isOriginality;
    @GraphQLField(kkhtml = "KFieldText", label = "是否份额分类", sql = "is_share_sort = $S{isShareSort}", field = "is_share_sort")
    private String isShareSort;
    @GraphQLField(kkhtml = "KFieldText", label = "是否系列过会", sql = "is_series_meeting = $S{isSeriesMeeting}", field = "is_series_meeting")
    private String isSeriesMeeting;
    @GraphQLField(kkhtml = "KFieldText", label = "创意id", sql = "originality_id = $S{originalityId}", field = "originality_id")
    private String originalityId;
    @GraphQLField(kkhtml = "KFieldText", label = "风险等级", sql = "prod_risk_level = $S{prodRiskLevel}", field = "prod_risk_level")
    private String prodRiskLevel;
    @GraphQLField(kkhtml = "KFieldText", label = "*产品币种", sql = "prod_cur = $S{prodCur}", field = "prod_cur")
    private String prodCur;
    @GraphQLField(kkhtml = "KFieldText", label = "*发行价格 ", sql = "netprice = $S{netprice}", field = "netprice")
    private String netprice;
    @GraphQLField(kkhtml = "KFieldText", label = "募集方式", sql = "raise_type = $S{raiseType}", field = "raise_type")
    private String raiseType;
    @GraphQLField(kkhtml = "KFieldText", label = "收益特点", sql = "income_type = $S{incomeType}", field = "income_type")
    private String incomeType;
    @GraphQLField(kkhtml = "KFieldText", label = "产品分类", sql = "prod_classify = $S{prodClassify}", field = "prod_classify")
    private String prodClassify;
    @GraphQLField(kkhtml = "KFieldText", label = "产品管理人", sql = "manager_code = $S{managerCode}", field = "manager_code")
    private String managerCode;
    @GraphQLField(kkhtml = "KFieldText", label = "净值披露说明", sql = "publish_explain = $S{publishExplain}", field = "publish_explain")
    private String publishExplain;
    @GraphQLField(kkhtml = "KFieldText", label = "报备状态", sql = "filing_status = $S{filingStatus}", field = "filing_status")
    private String filingStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "报备资料状态", sql = "filing_materials_status = $S{filingMaterialsStatus}", field = "filing_materials_status")
    private String filingMaterialsStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "其它报备资料状态", sql = "other_filing_status = $S{otherFilingStatus}", field = "other_filing_status")
    private String otherFilingStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "其它报备资料确认", sql = "other_filing_materials_status = $S{otherFilingMaterialsStatus}", field = "other_filing_materials_status")
    private String otherFilingMaterialsStatus;
    @GraphQLField(label = "钞汇标识")
    private String prodTrait;
    @GraphQLField(label = "产品特点")
    private String investDirection;
    @GraphQLField(label = "投资方向")
    private String bnoteRemitFlag;
    @GraphQLField(label = "其他风险2")
    private String otherRisk;
    @GraphQLField(kkhtml = "KFieldText", label = "生命周期状态", sql = "prod_status = $S{prodStatus}", field = "prod_status")
    private String prodStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "子状态", sql = "prod_son_status = $S{prodSonStatus}", field = "prod_son_status")
    private String prodSonStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "审批状态", sql = "approval_status = $S{approvalStatus}", field = "approval_status")
    private String approvalStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}", field = "crt_user")
    private String crtUser;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_date = $S{updDate}", field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}", field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user = $S{updUser}", field = "upd_user")
    private String updUser;
    @GraphQLField(label = "产品文档模板id", field = "prod_doc_mods")
    private String prodDocMods;
    @GraphQLField(label = "风险评分", field = "risk_score")
    private String riskScore;
    @GraphQLField(label = "产品风险评分状态", field = "risk_score_status")
    private String riskScoreStatus;
    @GraphQLField(label = "...", field = "risk_score_status")
    private String distributorCode;
    @GraphQLField(label = "...", field = "risk_score_status")
    private String t8ProdAccountInfoId;
    @GraphQLField
    private String prodDesc;

    @GraphQLField(label = "备用字段一", field = "t8_spare_column_one")
    private String t8SpareColumnOne;
    @GraphQLField(label = "备用字段二", field = "t8_spare_column_two")
    private String t8SpareColumnTwo;
    @GraphQLField(label = "备用字段三", field = "t8_spare_column_three")
    private String t8SpareColumnThree;
    @GraphQLField(label = "备用字段四", field = "t8_spare_column_four")
    private String t8SpareColumnFour;
    @GraphQLField(label = "备用字段五", field = "t8_spare_column_five")
    private String t8SpareColumnFive;
    @GraphQLField(label = "货币单位（文档使用）", field = "prod_company")
    private String prodCompany;


    //系列字段
    @GraphQLField(label = "系列名称", sql = "series_name = $S{seriesName}", field = "series_name")
    private String seriesName;
    @GraphQLField(label = "系列代码", sql = "series_code = $S{seriesCode}", field = "series_code")
    private String seriesCode;
    @GraphQLField(label = "系列id", sql = "t8_prod_series_id = $S{t8ProdSeriesId}", field = "id")
    private String t8ProdSeriesId;
    @GraphQLField(label = "父系列代码", sql = "parent_code = $S{parentCode}", field = "parent_code")
    private String parentCode;
    @GraphQLField(label = "系列说明", sql = "series_explain = $S{seriesExplain}", field = "series_explain")
    private String seriesExplain;

    //创意
    @GraphQLField(label = "创意名称", sql = "originality_name = $S{originalityName}", field = "originality_name")
    private String originalityName;


    //产品调整表
    @GraphQLField(label = "调整记录表id", sql = "t8_prod_adjust_id = $S{t8ProdAdjustId}", field = "t8_prod_adjust_id")
    private String t8ProdAdjustId;
    @GraphQLField(label = "调整日期", sql = "adjust_date = $S{adjustDate}", field = "adjust_date")
    private String adjustDate;
    @GraphQLField(label = "修改人", sql = "adjust_user = $S{adjustUser}", field = "adjust_user")
    private String adjustUser;
    @GraphQLField(label = "修改类型", sql = "adjust_type = $S{adjustType}", field = "adjust_type")
    private String adjustType;
    @GraphQLField(label = "原因", sql = "adjust_cause = $S{adjustCause}", field = "adjust_cause")
    private String adjustCause;

    //协议类型(托管协议,代销协议)
    @GraphQLField(label = "协议类型", sql = "doc_type = $S{docType}", field = "doc_type")
    private String docType;
    //额度决策会
    @GraphQLField(label = "成立日期", sql = "establish_date = $S{establishDate}", field = "establish_date")
    private String establishDate;

    @GraphQLField(label = "到期日期", sql = "end_date = $S{endDate}", field = "end_date")
    private String endDate;

    @GraphQLField(label = "查询开始日期")
    private String queryStartDate;
    @GraphQLField(label = "查询结束日期")
    private String queryEndDate;

    @GraphQLField(label = "文档类型")
    private String documentType;

    @GraphQLField
    private String declaraCrtDate;
    @GraphQLField
    private String declaraCrtUser;
    @GraphQLField
    private String issueCrtDate;
    @GraphQLField
    private String issueCrtUser;

    @GraphQLField(label = "申报登记日期", sql = "apply_regist_date = $S{applyRegistDate}", field = "apply_regist_date")
    private String applyRegistDate;
    @GraphQLField(label = "发行登记日期", sql = "issue_regist_date = $S{issueRegistDate}", field = "issue_regist_date")
    private String issueRegistDate;

    @GraphQLField(field = "market_value")
    private String marketValue;
    @GraphQLField
    private String limitDate;


    //产品台账
    @GraphQLField
    private String productTerm;
    @GraphQLField
    private String cycleOpenTerm;
    @GraphQLField
    private String openDate;
    @GraphQLField
    private String subEndDate;
    @GraphQLField
    private String subStartDate;
    @GraphQLField
    private String minSubsPerson;
    @GraphQLField
    private String prodSaleCustom;
    @GraphQLField
    private String baseRate;
    @GraphQLField
    private String minSubsMechanism;
    @GraphQLField
    private String investManageName;
    @GraphQLField
    private String bonusFrequency;
    @GraphQLField
    private String bonusType;
    @GraphQLField
    private String liquidate;
    @GraphQLField
    private String cycleOpenType;
    @GraphQLField
    private String minSubsInterbank;
    @GraphQLField
    private String liquidateType;

    @GraphQLField(field = "has_template")
    private String hasTemplate;
    @GraphQLField
    private String isShow;

    //删除附近
    @GraphQLField(field = "parentId")
    private String parentId;
    @GraphQLField(field = "prod_document_id")
    private String prodDocumentId;
    @GraphQLField(field = "path")
    private String path;
    @GraphQLField(field = "file_name")
    private String fileName;
    @GraphQLField(field = "attachment_type")
    private String attachmentType;
    @GraphQLField(field = "inputuser")
    private String inputuser;
    @GraphQLField(field = "t8_trutee_info_id")
    private String t8TruteeInfoId;

    //老版本的t8prodInfo实体

    @GraphQLField(label = "产品状态", sql = "t1.prod_lifecycle = $S{prodLifecycle}", field = "prod_lifecycle", kkhtmlExt = "{\"data-dict\": \"prod_lifecycle\"}")
    private String prodLifecycle;
    @GraphQLField(label = "发行规模", sql = "vol = $S{vol}", field = "vol")
    private String vol;
    @GraphQLField(label = "最新净值", sql = "nav = $S{nav}", field = "nav")
    private String nav;
    @GraphQLField(label = "净值日期", sql = "nav_date = $S{navDate}", field = "nav_date")
    private String navDate;
    @GraphQLField(label = "销售状态", sql = "sale_status = $S{saleStatus}", field = "sale_status")
    private String saleStatus;
    @GraphQLField(label = "产品模型名称", sql = "prod_mode_name = $S{prodModeName}", field = "prod_mode_name")
    private String prodModeName;
    @GraphQLField(label = "产品期限", sql = "prod_days = $S{prodDays}", field = "prod_days")
    private String prodDays;

    @GraphQLField(label = "产品简称", sql = "prod_name_short = $S{prodNameShort}", field = "prod_name_short")
    private String prodNameShort;
    @GraphQLField(label = "产品说明", sql = "prod_explain = $S{prodExplain}", field = "prod_explain")
    private String prodExplain;
    @GraphQLField(label = "父产品代码", sql = "parent_prod_code = $S{parentProdCode}", field = "parent_prod_code")
    private String parentProdCode;
    @GraphQLField(label = "产品主办部门", sql = "prod_spon_dep = $S{prodSponDep}", field = "prod_spon_dep")
    private String prodSponDep;
    @GraphQLField(label = "产品主办机构", sql = "prod_spon_org = $S{prodSponOrg}", field = "prod_spon_org")
    private String prodSponOrg;
    @GraphQLField(label = "产品研发部门", sql = "prod_rese_dep = $S{prodReseDep}", field = "prod_rese_dep")
    private String prodReseDep;
    @GraphQLField(label = "产品销售部门", sql = "prod_sale_dep = $S{prodSaleDep}", field = "prod_sale_dep")
    private String prodSaleDep;
    @GraphQLField(label = "产品模式", sql = "period_type = $S{periodType}", field = "period_type")
    private String periodType;
    @GraphQLField(label = "产品类型", sql = "profit_type = $S{profitType}", field = "profit_type")
    private String profitType;
    @GraphQLField(label = "销售类型", sql = "sale_type = $S{saleType}", field = "sale_type")
    private String saleType;
    @GraphQLField(label = "产品投资经理", sql = "product_inv_manager = $S{productInvManager}", field = "product_inv_manager")
    private String productInvManager;
    @GraphQLField(label = "是否本行托管", sql = "is_trutee = $S{isTrutee}", field = "is_trutee")
    private String isTrutee;
    @GraphQLField(label = "产品托管行", sql = "trutee_code = $S{truteeCode}", field = "trutee_code")
    private String truteeCode;
    @GraphQLField(label = "法人代码 多法人模式下的发行机构的法人代码", sql = "legal_code = $S{legalCode}", field = "legal_code")
    private String legalCode;
    @GraphQLField(label = "发行价格", sql = "price = $S{price}", field = "price")
    private String price;
    @GraphQLField(label = "TA代码", sql = "ta_no = $S{taNo}", field = "ta_no")
    private String taNo;
    @GraphQLField(label = "收益类型", sql = "income_characteristic = $S{incomeCharacteristic}", field = "income_characteristic")
    private String incomeCharacteristic;
    @GraphQLField(label = "认购款到账方式", sql = "subs_capital_type = $S{subsCapitalType}", field = "subs_capital_type")
    private String subsCapitalType;
    @GraphQLField(label = "最低募集金额", sql = "min_raise_amt = $S{minRaiseAmt}", field = "min_raise_amt")
    private String minRaiseAmt;
    @GraphQLField(label = "最高募集金额", sql = "max_raise_amt = $S{maxRaiseAmt}", field = "max_raise_amt")
    private String maxRaiseAmt;
    @GraphQLField(label = "实际募集金额", sql = "actual_subs_size = $S{actualSubsSize}", field = "actual_subs_size")
    private String actualSubsSize;
    @GraphQLField(label = "产品成立确认比例", sql = "confirm_rate = $S{confirmRate}", field = "confirm_rate")
    private String confirmRate;
    @GraphQLField(label = "产品募集失败日期", sql = "prod_failure_date = $S{prodFailureDate}", field = "prod_failure_date")
    private String prodFailureDate;
    @GraphQLField(label = "发行规模控制方式", sql = "subs_quota_type = $S{subsQuotaType}", field = "subs_quota_type")
    private String subsQuotaType;
    @GraphQLField(label = "规模上限产品总额度", sql = "max_size = $S{maxSize}", field = "max_size")
    private String maxSize;
    @GraphQLField(label = "规模下限最低规模条件如果赎回导致底于下限则拒绝赎回", sql = "min_size = $S{minSize}", field = "min_size")
    private String minSize;
    @GraphQLField(label = "节假日方案", sql = "pgmno = $S{pgmno}", field = "pgmno")
    private String pgmno;
    @GraphQLField(label = "是否分级产品", sql = "can_grading = $S{canGrading}", field = "can_grading")
    private String canGrading;
    @GraphQLField(label = "分级方案代码", sql = "grad_prj = $S{gradPrj}", field = "grad_prj")
    private String gradPrj;
    @GraphQLField(label = "认购扣款模式（1-扣款 2-冻结）", sql = "subs_mode = $S{subsMode}", field = "subs_mode")
    private String subsMode;
    @GraphQLField(label = "申购扣款模式（1-扣款 2-冻结）", sql = "apply_mode = $S{applyMode}", field = "apply_mode")
    private String applyMode;
    @GraphQLField(label = "认购是否计息（0-否 1-是）", sql = "subs_is_interest = $S{subsIsInterest}", field = "subs_is_interest")
    private String subsIsInterest;
    @GraphQLField(label = "认购计息年天数", sql = "subs_interest_yeardays = $S{subsInterestYeardays}", field = "subs_interest_yeardays")
    private String subsInterestYeardays;
    @GraphQLField(label = "认购利息处理方式", sql = "subs_interest_deal_mode = $S{subsInterestDealMode}", field = "subs_interest_deal_mode")
    private String subsInterestDealMode;
    @GraphQLField(label = "认购计息起始日", sql = "subs_interest_start_date = $S{subsInterestStartDate}", field = "subs_interest_start_date")
    private String subsInterestStartDate;
    @GraphQLField(label = "认购计息截止日", sql = "subs_interest_end_date = $S{subsInterestEndDate}", field = "subs_interest_end_date")
    private String subsInterestEndDate;
    @GraphQLField(label = "是否可部分返还（0-否 1-是）（封闭净值使用）", sql = "is_part_cash = $S{isPartCash}", field = "is_part_cash")
    private String isPartCash;
    @GraphQLField(label = "费率合并方式（0-单笔计算 1-当天合并计算）", sql = "rate_merge_method = $S{rateMergeMethod}", field = "rate_merge_method")
    private String rateMergeMethod;
    @GraphQLField(label = "后收费用计算方式（1-按原净值2-按现价3-两者最小）", sql = "backfee_calculate_method = $S{backfeeCalculateMethod}", field = "backfee_calculate_method")
    private String backfeeCalculateMethod;
    @GraphQLField(label = "默认分红方式：0-红利再投 1-现金分红", sql = "def_div_method = $S{defDivMethod}", field = "def_div_method")
    private String defDivMethod;
    @GraphQLField(label = "分红方式是否可修改：0-不可 1-可 默认0不可修改", sql = "div_chg_flag = $S{divChgFlag}", field = "div_chg_flag")
    private String divChgFlag;
    @GraphQLField(label = "最小现金分红", sql = "min_div_amt = $S{minDivAmt}", field = "min_div_amt")
    private String minDivAmt;
    @GraphQLField(label = "清盘方式（0-净值清盘 1-总金额清盘）", sql = "windup_type = $S{windupType}", field = "windup_type")
    private String windupType;
    @GraphQLField(label = "是否通过TA清算（0-否 1-是）", sql = "is_ta_clear = $S{isTaClear}", field = "is_ta_clear")
    private String isTaClear;
    @GraphQLField(label = "是否锁定期产品（0-否 1-是）", sql = "is_lock_prod = $S{isLockProd}", field = "is_lock_prod")
    private String isLockProd;
    @GraphQLField(label = "锁定期期限", sql = "lock_days = $S{lockDays}", field = "lock_days")
    private String lockDays;
    @GraphQLField(label = "是否支持自动续投（0-否 1-是）", sql = "is_volunt_buy = $S{isVoluntBuy}", field = "is_volunt_buy")
    private String isVoluntBuy;
    @GraphQLField(label = "投资周期（天）", sql = "invest_days = $S{investDays}", field = "invest_days")
    private String investDays;
    @GraphQLField(label = "申购截止日", sql = "apply_end_date = $S{applyEndDate}", field = "apply_end_date")
    private String applyEndDate;
    @GraphQLField(label = "自动续申购确认天数", sql = "apply_cfm_days = $S{applyCfmDays}", field = "apply_cfm_days")
    private String applyCfmDays;
    @GraphQLField(label = "自动续投投资次数限制天数", sql = "invest_num_days = $S{investNumDays}", field = "invest_num_days")
    private String investNumDays;
    @GraphQLField(label = "担险标志（1-非担险类 2-担险类）", sql = "risk_flag = $S{riskFlag}", field = "risk_flag")
    private String riskFlag;
    @GraphQLField(label = "是否可质押（0-否 1-是）", sql = "is_pledge = $S{isPledge}", field = "is_pledge")
    private String isPledge;
    @GraphQLField(label = "质押率上限", sql = "max_pledge_rate = $S{maxPledgeRate}", field = "max_pledge_rate")
    private String maxPledgeRate;
    @GraphQLField(label = "持有理财证明开立权限（0-可开立时期证明1-可开立时点证明 2-不可开立3-可开立时点及时期证明）", sql = "is_hole_prove = $S{isHoleProve}", field = "is_hole_prove")
    private String isHoleProve;
    @GraphQLField(label = "认购隔日撤单标志（0-允许 1-不允许）", sql = "is_subs_cancel = $S{isSubsCancel}", field = "is_subs_cancel")
    private String isSubsCancel;
    @GraphQLField(label = "赎回份额明细处理（0-后进先出 1-先进先出）", sql = "tn_redeem_share = $S{tnRedeemShare}", field = "tn_redeem_share")
    private String tnRedeemShare;
    @GraphQLField(label = "转换份额明细处理（0-后进先出 1-先进先出）", sql = "tn_convert_share = $S{tnConvertShare}", field = "tn_convert_share")
    private String tnConvertShare;
    @GraphQLField(label = "部分赎回结息方式（1-按比例结息 2-不结息）", sql = "interest_convert_type = $S{interestConvertType}", field = "interest_convert_type")
    private String interestConvertType;
    @GraphQLField(label = "赎回方式（0-按明细 1-按汇总）", sql = "redeem_type = $S{redeemType}", field = "redeem_type")
    private String redeemType;
    @GraphQLField(label = "末日日期", sql = "last_date = $S{lastDate}", field = "last_date")
    private String lastDate;
    @GraphQLField(label = "产品分类(0-传统TA产品1-互联网TA产品)", sql = "prod_class = $S{prodClass}", field = "prod_class")
    private String prodClass;
    @GraphQLField(label = "净值推送批次(0-一批次推送1-二批次推送)", sql = "nav_batch = $S{navBatch}", field = "nav_batch")
    private String navBatch;
    @GraphQLField(label = "数据状态", sql = "data_status = $S{dataStatus}", field = "data_status")
    private String infoDataStatus;
    @GraphQLField(label = "下一开放日产品状态", sql = "next_prod_status = $S{nextProdStatus}", field = "next_prod_status")
    private String nextProdStatus;
    @GraphQLField(label = "任务分组", sql = "task_group = $S{taskGroup}", field = "task_group")
    private String taskGroup;
    @GraphQLField(label = "是否支持快速过户", sql = "is_quicktransfer = $S{isQuicktransfer}", field = "is_quicktransfer")
    private String isQuicktransfer;

    @GraphQLField(label = "预留开始日", sql = "booking_begin_date = $S{bookingBeginDate}", field = "booking_begin_date")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private String bookingBeginDate;
    @GraphQLField(label = "预留失效日", sql = "booking_invalid_date = $S{bookingInvalidDate}", field = "booking_invalid_date")
    private String bookingInvalidDate;
    @GraphQLField(label = "预约开始日", sql = "order_begin_date = $S{orderBeginDate}", field = "order_begin_date")
    private String orderBeginDate;
    @GraphQLField(label = "认购开始日", sql = "subs_begin_date = $S{subsBeginDate}", field = "subs_begin_date")
    private String subsBeginDate;
    @GraphQLField(label = "认购结束日", sql = "subs_end_date = $S{subsEndDate}", field = "subs_end_date")
    private String subsEndDate;
    @GraphQLField(label = "认购开始时间", sql = "subs_start_time = $S{subsStartTime}", field = "subs_start_time")
    private String subsStartTime;
    @GraphQLField(label = "认购结束时间", sql = "subs_end_time = $S{subsEndTime}", field = "subs_end_time")
    private String subsEndTime;
    @GraphQLField(label = "允许购买开始时间", sql = "start_buy_time = $S{startBuyTime}", field = "start_buy_time")
    private String startBuyTime;
    @GraphQLField(label = "允许购买结束时间", sql = "end_buy_time = $S{endBuyTime}", field = "end_buy_time")
    private String endBuyTime;
    @GraphQLField(label = "允许赎回开始时间", sql = "start_redeem_time = $S{startRedeemTime}", field = "start_redeem_time")
    private String startRedeemTime;
    @GraphQLField(label = "允许赎回结束时间", sql = "end_redeem_time = $S{endRedeemTime}", field = "end_redeem_time")
    private String endRedeemTime;
    @GraphQLField(label = "收益起始日", sql = "value_date = $S{valueDate}", field = "value_date")
    private String valueDate;
    @GraphQLField(label = "首次成立日", sql = "first_establish_date = $S{firstEstablishDate}", field = "first_establish_date")
    private String firstEstablishDate;
    @GraphQLField(label = "开放起始日", sql = "open_begin_date = $S{openBeginDate}", field = "open_begin_date")
    private String openBeginDate;
    @GraphQLField(label = "开放结束日", sql = "open_end_date = $S{openEndDate}", field = "open_end_date")
    private String openEndDate;
    @GraphQLField(label = "下一个到期日", sql = "next_winding_date = $S{nextWindingDate}", field = "next_winding_date")
    private String nextWindingDate;
    @GraphQLField(label = "提前到期日", sql = "advance_winding_date = $S{advanceWindingDate}", field = "advance_winding_date")
    private String advanceWindingDate;
    @GraphQLField(label = "还款日期", sql = "pay_date = $S{payDate}", field = "pay_date")
    private String payDate;
    @GraphQLField(label = "清盘日", sql = "winding_date = $S{windingDate}", field = "winding_date")
    private String windingDate;
    @GraphQLField(label = "数据状态", sql = "data_status = $S{dataStatus}", field = "data_status")
    private String periodDataStatus;
    @GraphQLField(label = "申购确认日")
    private String applyCfmM;
    @GraphQLField(label = "赎回确认日")
    private String redeemCfmM;
    @GraphQLField(label = "认购确认日")
    private String subsCfmM;
    @GraphQLField(label = "配置进度", sql = "config_status = $S{configStatus}", field = "config_status")
    private String configStatus;

    @GraphQLField(label = "开放总天数")
    private String openSumDate;
    @GraphQLField(label = "认购总天数")
    private String subsSumDate;

    //产品创设状态
    @GraphQLField
    private String prodinfo;
    @GraphQLField
    private String truteeinfo;
    @GraphQLField
    private String prodcalendar;
    @GraphQLField
    private String limitinfo;
    @GraphQLField
    private String prodbonus;
    @GraphQLField
    private String prodinvest;
    @GraphQLField
    private String prodvaluation;
    @GraphQLField
    private String prodfee;
    @GraphQLField
    private String feedeal;
    @GraphQLField
    private String performanceinfo;
    @GraphQLField
    private String proddocinfo;
    @GraphQLField
    private String declarationinfo;
    @GraphQLField
    private String modelId;
    @GraphQLField(label = "产品经理姓名", field = "prod_manage_name")
    private String prodManageName;
    @GraphQLField(label = "产品经理id")
    private String prodManageId;
    @GraphQLField(label = "基准类型", field = "base_type")
    private String baseType;
    @GraphQLField
    private String prodsharesort;
    @GraphQLField
    private String isRecycleCode;
    @GraphQLField
    private String flowTemplate;


}
