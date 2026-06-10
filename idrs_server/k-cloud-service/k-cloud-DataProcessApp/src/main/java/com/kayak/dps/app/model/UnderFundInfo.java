package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "underFundInfoService" )
public class UnderFundInfo {
    @GraphQLField
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
    private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "对应资管及委外资产行内资产/负债编码", sql = "asset_manager_code LIKE '%$U{assetManagerCode}%'"  ,field = "asset_manager_code")
    private String assetManagerCode;
    @GraphQLField(kkhtml = "KFieldText", label = "资管及委外资产当前总折算人民币金额", sql = "convert_sum_amt = $S{convertSumAmt}" ,field = "convert_sum_amt")
    private String convertSumAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "资管及委外资产当前总数量", sql = "asset_sum_number = $S{assetSumNumber}" ,field = "asset_sum_number")
    private String assetSumNumber;
    @GraphQLField(kkhtml = "KFieldText", label = "资管及委外资产未投资头寸", sql = "non_invested_amt = $S{nonInvestedAmt}" ,field = "non_invested_amt")
    private String nonInvestedAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "底层资产行内资产/负债编码", sql = "under_asset_code LIKE '%$U{underAssetCode}%'",field = "under_asset_code")
    private String underAssetCode;
    @GraphQLField(kkhtml = "KFieldText", label = "底层资产持仓数量", sql = "under_asset_sum = $S{underAssetSum}" ,field = "under_asset_sum")
    private String underAssetSum;
    @GraphQLField(kkhtml = "KFieldText", label = "底层资产折算人民币市值", sql = "under_convert_sum_amt = $S{underConvertSumAmt}" ,field = "under_convert_sum_amt")
    private String underConvertSumAmt;
    @GraphQLField(kkhtml = "KFieldText", label = "持仓日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
    private String crtUser;
    @GraphQLField(kkhtml = "KFieldText", label = "修改日期", sql = "upd_date = $S{updDate}" ,field = "upd_date")
    private String updDate;
    @GraphQLField(kkhtml = "KFieldText", label = "修改时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "修改人", sql = "upd_user = $S{updUser}" ,field = "upd_user")
    private String updUser;
}
