package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * com.kayak.pms.prod.model
 * user:rennannan
 * date:2021/3/30 11:17
 * function:
 */
@Data
@GraphQLModel(fetcher = "t8ProdStandBookService")
public class T8ProdStandBook {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "id", sql = "t.id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品模型", sql = "prod_mode = $S{prodMode}", field = "prod_mode")
    private String prodMode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品模型模板id", sql = "prod_mode_id = $S{prodModeId}", field = "prod_mode_id")
    private String prodModeId;
    @GraphQLField(kkhtml = "KFieldText", label = "产品系列", sql = "prod_series = $S{prodSeries}", field = "prod_series")
    private String prodSeries;
    @GraphQLField(kkhtml = "KFieldSelect", label = "产品代码", sql = "t.prod_code IN ($S{prodCode})", field = "prod_code", kkhtmlExt = "{\"data-action\":\"T8ProdInfo.findT8ProdInfos\",\"data-display-field\":\"prodCode,prodName\",\"data-value-field\":\"prodCode\"}")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name like '%$U{prodName}%'", field = "prod_name", kkhtmlDefault = true)
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "产品品牌", sql = "prod_brand = $S{prodBrand}", field = "prod_brand")
    private String prodBrand;
    @GraphQLField(kkhtml = "KFieldText", label = "中债登记编码", sql = "regist_code = $S{registCode}", field = "regist_code")
    private String registCode;
    @GraphQLField(kkhtml = "KFieldText", label = "是否关联创意", sql = "is_originality = $S{isOriginality}", field = "is_originality")
    private String isOriginality;
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


    //系列字段
    @GraphQLField(label = "系列名称", sql = "series_name = $S{seriesName}", field = "series_name")
    private String seriesName;
    @GraphQLField(label = "系列代码", sql = "series_code = $S{seriesCode}", field = "series_code")
    private String seriesCode;
    @GraphQLField(label = "系列id", sql = "t8_prod_series_id = $S{t8ProdSeriesId}", field = "id")
    private String t8ProdSeriesId;

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
    @GraphQLField(label = "成日期（从）")
    private String establishStartDate;
    @GraphQLField(label = "成日期（到）")
    private String establishEndDate;
    @GraphQLField(label = "到日期（从）")
    private String expireStartDate;
    @GraphQLField(label = "到日期（到）")
    private String expireEndDate;

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
    @GraphQLField(label = "投资经理姓名")
    private String investManageName;
    @GraphQLField
    private String bonusFrequency;
    @GraphQLField(label = "分红方式")
    private String bonusType;
    @GraphQLField
    private String liquidate;
    @GraphQLField(label = "开放频率类型")
    private String cycleOpenType;
    @GraphQLField
    private String minSubsInterbank;
    @GraphQLField
    private String liquidateType;
    @GraphQLField(label = "产品经理姓名")
    private String prodManageName;
    @GraphQLField(label = "投资经理id")
    private String investManageId;
    @GraphQLField(label = "产品经理id")
    private String prodManageId;
    @GraphQLField(label = "开放开始日期")
    private String openStartDate;
    @GraphQLField(label = "开放结束日期")
    private String openEndDate;
    @GraphQLField(label = "估值方法")
    private String valuationMethod;
    @GraphQLField(label = "是否代码回收")
    private String isRecycleCode;
}
