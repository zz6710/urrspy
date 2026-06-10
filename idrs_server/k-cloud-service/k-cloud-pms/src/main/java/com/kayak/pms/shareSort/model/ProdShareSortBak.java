package com.kayak.pms.shareSort.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;

import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "prodShareSortBakService", table = "t8_prod_share_sort_bak")
public class ProdShareSortBak {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品id", sql = "t8_prod_info_id = $S{t8ProdInfoId}", field = "t8_prod_info_id")
    private String t8ProdInfoId;
    @GraphQLField(kkhtml = "KFieldText", label = "份额名称", sql = "share_name = $S{shareName}", field = "share_name")
    private String shareName;
    @GraphQLField(kkhtml = "KFieldText", label = "销售名称", sql = "sales_name = $S{salesName}", field = "sales_name")
    private String salesName;
    @GraphQLField(kkhtml = "KFieldText", label = "销售代码", sql = "sales_code = $S{salesCode}", field = "sales_code")
    private String salesCode;
    @GraphQLField(kkhtml = "KFieldText", label = "销售客群", sql = "sales_group = $S{salesGroup}", field = "sales_group")
    private String salesGroup;
    @GraphQLField(kkhtml = "KFieldText", label = "销售份额状态", sql = "sales_share_status = $S{salesShareStatus}", field = "sales_share_status")
    private String salesShareStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "起点金额", sql = "min_amount = $S{minAmount}", field = "min_amount")
    private String minAmount;
    @GraphQLField(kkhtml = "KFieldText", label = "递增金额", sql = "step_amount = $S{stepAmount}", field = "step_amount")
    private String stepAmount;
    @GraphQLField(kkhtml = "KFieldText", label = "认购追加金额", sql = "sub_append_amount = $S{subAppendAmount}", field = "sub_append_amount")
    private String subAppendAmount;
    @GraphQLField(kkhtml = "KFieldText", label = "赎回追加金额", sql = "redeem_append_amount = $S{redeemAppendAmount}", field = "redeem_append_amount")
    private String redeemAppendAmount;
    @GraphQLField(kkhtml = "KFieldText", label = "单笔最小赎回份额", sql = "min_redeem_vol = $S{minRedeemVol}", field = "min_redeem_vol")
    private String minRedeemVol;
    @GraphQLField(kkhtml = "KFieldText", label = "最小持有份额", sql = "min_hole_vol = $S{minHoleVol}", field = "min_hole_vol")
    private String minHoleVol;
    @GraphQLField(kkhtml = "KFieldText", label = "最大持有份额", sql = "max_hole_vol = $S{maxHoleVol}", field = "max_hole_vol")
    private String maxHoleVol;
    @GraphQLField(kkhtml = "KFieldText", label = "销售服务费率%", sql = "sales_fee_rate = $S{salesFeeRate}", field = "sales_fee_rate")
    private String salesFeeRate;
    @GraphQLField(kkhtml = "KFieldText", label = "付费规则", sql = "fee_rules = $S{feeRules}", field = "fee_rules")
    private String feeRules;
    @GraphQLField(kkhtml = "KFieldText", label = "计提基数", sql = "raise_base = $S{raiseBase}", field = "raise_base")
    private String raiseBase;
    @GraphQLField(kkhtml = "KFieldText", label = "计提规则", sql = "raise_rules = $S{raiseRules}", field = "raise_rules")
    private String raiseRules;
    @GraphQLField(kkhtml = "KFieldText", label = "基准类型", sql = "base_type = $S{baseType}", field = "base_type")
    private String baseType;
    @GraphQLField(kkhtml = "KFieldText", label = "业绩报酬提取比例%", sql = "performance_out = $S{performanceOut}", field = "performance_out")
    private String performanceOut;
    @GraphQLField(kkhtml = "KFieldText", label = "基准利率", sql = "base_rate = $S{baseRate}", field = "base_rate")
    private String baseRate;
    @GraphQLField(kkhtml = "KFieldText", label = "基准利率下限", sql = "base_min_rate = $S{baseMinRate}", field = "base_min_rate")
    private String baseMinRate;
    @GraphQLField(kkhtml = "KFieldText", label = "基准利率上限", sql = "base_max_rate = $S{baseMaxRate}", field = "base_max_rate")
    private String baseMaxRate;
    @GraphQLField(kkhtml = "KFieldText", label = "市场利率", sql = "market_rate = $S{marketRate}", field = "market_rate")
    private String marketRate;
    @GraphQLField(kkhtml = "KFieldText", label = "自定义利率", sql = "custom = $S{custom}", field = "custom")
    private String custom;
    @GraphQLField(kkhtml = "KFieldText", label = "指数名称", sql = "ratio_index = $S{ratioIndex}", field = "ratio_index")
    private String ratioIndex;
    @GraphQLField(kkhtml = "KFieldText", label = "系数", sql = "coefficient = $S{coefficient}", field = "coefficient")
    private String coefficient;
    @GraphQLField(kkhtml = "KFieldText", label = "提交人员", sql = "inputuser = $S{inputuser}", field = "inputuser")
    private String inputuser;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}", field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}", field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upt_date = $S{uptDate}", field = "upt_date")
    private String uptDate;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upt_time = $S{uptTime}", field = "upt_time")
    private String uptTime;
    @GraphQLField(kkhtml = "KFieldText", label = "业绩比较基准说明", sql = "perf_method_explain = $S{perfMethodExplain}", field = "perf_method_explain")
    private String perfMethodExplain;
    @GraphQLField
    private List<ProdShareRationBak> prodShareRatio;
    @GraphQLField
    private List<ProdShareSectionBak> prodShareSection;
    @GraphQLField(label = "产品代码", field = "prod_code")
    private String prodCode;
    @GraphQLField(label = "产品名称", field = "prod_name")
    private String prodName;
    @GraphQLField(label = "确认状态", field = "confirm_status")
    private String confirmStatus;
}