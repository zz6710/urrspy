package com.kayak.rpt.rhzj.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "reportPPIService", table = "app_rpt_ppi")
public class ReportPPI {
    @GraphQLField(kkhtml = "KFieldText", label = "发起机构内部产品代码", sql = "prod_code = $S{prodCode}", field = "prod_code")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品ID", sql = "orgno = $S{orgno}", field = "orgno")
    private String orgno;
    @GraphQLField(kkhtml = "KFieldText", label = "报送人行产品代码", sql = "peoplebank_submitcode = $S{peoplebankSubmitcode}", field = "peoplebank_submitcode")
    private String peoplebankSubmitcode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品品种", sql = "prod_variety = $S{prodVariety}", field = "prod_variety")
    private String prodVariety;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_name = $S{prodName}", field = "prod_name")
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "产品品牌", sql = "prod_brand = $S{prodBrand}", field = "prod_brand")
    private String prodBrand;
    @GraphQLField(kkhtml = "KFieldText", label = "产品期次", sql = "prod_times = $S{prodTimes}", field = "prod_times")
    private String prodTimes;
    @GraphQLField(kkhtml = "KFieldText", label = "募集方式", sql = "coll_mod = $S{collMod}", field = "coll_mod")
    private String collMod;
    @GraphQLField(kkhtml = "KFieldText", label = "管理方式", sql = "oper_mod = $S{operMod}", field = "oper_mod")
    private String operMod;
    @GraphQLField(kkhtml = "KFieldText", label = "运行方式", sql = "run_mod = $S{runMod}", field = "run_mod")
    private String runMod;
    @GraphQLField(kkhtml = "KFieldText", label = "产品类型", sql = "prod_type = $S{prodType}", field = "prod_type")
    private String prodType;
    @GraphQLField(kkhtml = "KFieldText", label = "业务模式", sql = "busi_mod = $S{busiMod}", field = "busi_mod")
    private String busiMod;
    @GraphQLField(kkhtml = "KFieldText", label = "收益保障标识", sql = "safe_rate = $S{safeRate}", field = "safe_rate")
    private String safeRate;
    @GraphQLField(kkhtml = "KFieldText", label = "本金保障标识", sql = "safe_capit = $S{safeCapit}", field = "safe_capit")
    private String safeCapit;
    @GraphQLField(kkhtml = "KFieldText", label = "预计客户最高收益率", sql = "max_rate = $S{maxRate}", field = "max_rate")
    private String maxRate;
    @GraphQLField(kkhtml = "KFieldText", label = "预计客户最低收益率", sql = "min_rate = $S{minRate}", field = "min_rate")
    private String minRate;
    @GraphQLField(kkhtml = "KFieldText", label = "募集起始日期", sql = "subs_bdate = $S{subsBdate}", field = "subs_bdate")
    private String subsBdate;
    @GraphQLField(kkhtml = "KFieldText", label = "募集结束日期", sql = "subs_edate = $S{subsEdate}", field = "subs_edate")
    private String subsEdate;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构提前终止权标识", sql = "term_flag = $S{termFlag}", field = "term_flag")
    private String termFlag;
    @GraphQLField(kkhtml = "KFieldText", label = "客户赎回权标识", sql = "redeem_flag = $S{redeemFlag}", field = "redeem_flag")
    private String redeemFlag;
    @GraphQLField(kkhtml = "KFieldText", label = "产品增信标识", sql = "prod_credit_flag = $S{prodCreditFlag}", field = "prod_credit_flag")
    private String prodCreditFlag;
    @GraphQLField(kkhtml = "KFieldText", label = "境内托管机构代码", sql = "bord_trusti_code = $S{bordTrustiCode}", field = "bord_trusti_code")
    private String bordTrustiCode;
    @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构国别", sql = "overs_trusti_nation = $S{oversTrustiNation}", field = "overs_trusti_nation")
    private String oversTrustiNation;
    @GraphQLField(kkhtml = "KFieldText", label = "境外托管机构名称", sql = "overs_trusti_name = $S{oversTrustiName}", field = "overs_trusti_name")
    private String oversTrustiName;
    @GraphQLField(kkhtml = "KFieldText", label = "产品起始日期", sql = "establish_date = $S{establishDate}", field = "establish_date")
    private String establishDate;
    @GraphQLField(kkhtml = "KFieldText", label = "产品预计终止日期", sql = "end_date = $S{endDate}", field = "end_date")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", label = "产品对应资产池代码", sql = "pbc_assetscode = $S{pbcAssetscode}", field = "pbc_assetscode")
    private String pbcAssetscode;
    @GraphQLField(kkhtml = "KFieldText", label = "募集资金币种", sql = "issu_ccy = $S{issuCcy}", field = "issu_ccy")
    private String issuCcy;
    @GraphQLField(kkhtml = "KFieldText", label = "兑付本金币种", sql = "return_ccy = $S{returnCcy}", field = "return_ccy")
    private String returnCcy;
    @GraphQLField(kkhtml = "KFieldText", label = "兑付收益币种", sql = "income_ccy = $S{incomeCcy}", field = "income_ccy")
    private String incomeCcy;
    @GraphQLField(kkhtml = "KFieldText", label = "客户类型", sql = "invest_object = $S{investObject}", field = "invest_object")
    private String investObject;
    @GraphQLField(kkhtml = "KFieldText", label = "增信机构类型", sql = "prod_credit_org = $S{prodCreditOrg}", field = "prod_credit_org")
    private String prodCreditOrg;
    @GraphQLField(kkhtml = "KFieldText", label = "增信形式", sql = "prod_credit_mod = $S{prodCreditMod}", field = "prod_credit_mod")
    private String prodCreditMod;
    @GraphQLField(kkhtml = "KFieldText", label = "产品实际终止日期", sql = "end_date_real = $S{endDateReal}", field = "end_date_real")
    private String endDateReal;
    @GraphQLField(kkhtml = "KFieldText", label = "兑付客户收益率", sql = "rate_real = $S{rateReal}", field = "rate_real")
    private String rateReal;
    @GraphQLField(kkhtml = "KFieldText", label = "收益特点", sql = "income_type = $S{incomeType}", field = "income_type")
    private String incomeType;
    @GraphQLField(kkhtml = "KFieldText", label = "合作模式", sql = "cooperation_mode = $S{cooperationMode}", field = "cooperation_mode")
    private String cooperationMode;
    @GraphQLField(kkhtml = "KFieldText", label = "分级产品标识", sql = "grading_flag = $S{gradingFlag}", field = "grading_flag")
    private String gradingFlag;
    @GraphQLField(kkhtml = "KFieldText", label = "受托职责", sql = "entrested_obligation = $S{entrestedObligation}", field = "entrested_obligation")
    private String entrestedObligation;
    @GraphQLField(kkhtml = "KFieldText", label = "收益权转让产品标识", sql = "transfer_flag = $S{transferFlag}", field = "transfer_flag")
    private String transferFlag;
    @GraphQLField(kkhtml = "KFieldText", label = "理财产品发起机构标识", sql = "orgno_flag = $S{orgnoFlag}", field = "orgno_flag")
    private String orgnoFlag;
    @GraphQLField(kkhtml = "KFieldText", label = "理财产品是否是现金管理类产品", sql = "cash_type = $S{cashType}", field = "cash_type")
    private String cashType;
    @GraphQLField(kkhtml = "KFieldText", label = "理财产品是否是跨境理财通“北向通”产品", sql = "cross_border_finan = $S{crossBorderFinan}", field = "cross_border_finan")
    private String crossBorderFinan;

    @GraphQLField(label = "开始时间", sql = "establish_date >= $S{beginDate}", field = "establish_date")
    private String beginDate;
    @GraphQLField(label = "结束时间", sql = "establish_date <= $S{queryDate}", field = "establish_date")
    private String queryDate;

}