package com.kayak.pms.schedule.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@GraphQLModel(fetcher = "logBackupService",table = "t8_prod_nav")
public class LogBackup {
    @GraphQLField(label = "id", kkhtml = "KFieldText", sql = "id = $S{id}" ,field = "id")
    private String id;

    @GraphQLField(label = "number", kkhtml = "KFieldText", sql = "number = $S{number}" ,field = "number")
    private String number;

    @GraphQLField(label = "净值日期", kkhtml = "KFieldText", sql = "nav_date = $S{navDate}" ,field = "nav_date")
    private String navDate;

    @GraphQLField(label = "基金名称", kkhtml = "KFieldText", sql = "prod_name = $S{prodName}" ,field = "prod_name")
    private String prodName;

    @GraphQLField(label = "基金代码", kkhtml = "KFieldText", sql = "prod_code = $S{prodCode}" ,field = "prod_code")
    private String prodCode;

    @GraphQLField(label = "基金总净值", kkhtml = "KFieldText", sql = "total_net = $S{totalNet}" ,field = "total_net")
    private String totalNet;

    @GraphQLField(label = "基金总份额", kkhtml = "KFieldText", sql = "total_vol = $S{totalVol}" ,field = "total_vol")
    private String totalVol;

    @GraphQLField(label = "基金单位净值", kkhtml = "KFieldText", sql = "nav = $S{nav}" ,field = "nav")
    private String nav;

    @GraphQLField(label = "基金收益", kkhtml = "KFieldText", sql = "nav_profit = $S{navProfit}" ,field = "nav_profit")
    private String navProfit;

    @GraphQLField(label = "万份基金收益", kkhtml = "KFieldText", sql = "ten_thousand_income_amt = $S{tenThousandIncomeAmt}" ,field = "ten_thousand_income_amt")
    private String tenThousandIncomeAmt;

    @GraphQLField(label = "近七日年化收益率", kkhtml = "KFieldText", sql = "seven_days_income_rate = $S{sevenDaysIncomeRate}" ,field = "seven_days_income_rate")
    private String sevenDaysIncomeRate;

    @GraphQLField(label = "销售服务费", kkhtml = "KFieldText", sql = "sale_service_fee = $S{saleServiceFee}" ,field = "sale_service_fee")
    private String saleServiceFee;

    @GraphQLField(label = "基金累计净值", kkhtml = "KFieldText", sql = "total_nav = $S{totalNav}" ,field = "total_nav")
    private String totalNav;

    @GraphQLField(label = "净值增长率", kkhtml = "KFieldText", sql = "nav_growth_rate = $S{navGrowthRate}" ,field = "nav_growth_rate")
    private String navGrowthRate;

    @GraphQLField(label = "管理费", kkhtml = "KFieldText", sql = "management_fees = $S{managementFees}" ,field = "management_fees")
    private String managementFees;

    @GraphQLField(label = "季度年化收益率", kkhtml = "KFieldText", sql = "quarter_income_rate = $S{quarterIncomeRate}" ,field = "quarter_income_rate")
    private String quarterIncomeRate;

    @GraphQLField(label = "二次分配收益", kkhtml = "KFieldText", sql = "sec_ass_profit = $S{id}" ,field = "sec_ass_profit")
    private String secAssProfit;

    @GraphQLField(label = "非存款类当日年化收益率", kkhtml = "KFieldText", sql = "non_deposit_day = $S{nonDepositDay}" ,field = "non_deposit_day")
    private String nonDepositDay;

    @GraphQLField(label = "集合计划总资产净值", kkhtml = "KFieldText", sql = "total_asset_nav = $S{totalAssetNav}" ,field = "total_asset_nav")
    private String totalAssetNav;

    @GraphQLField(label = "导入日期", kkhtml = "KFieldText", sql = "import_date = $S{importDate}" ,field = "import_date")
    private String importDate;

    @GraphQLField(label = "创建日期", kkhtml = "KFieldText", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
    private String crtDate;

    @GraphQLField(label = "创建时间", kkhtml = "KFieldText", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;

    @GraphQLField(label = "创建人", kkhtml = "KFieldText", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
    private String crtUser;

    @GraphQLField(label = "更新日期", kkhtml = "KFieldText", sql = "upd_date = $S{updDate}" ,field = "upd_date")
    private String updDate;

    @GraphQLField(label = "更新时间", kkhtml = "KFieldText", sql = "upd_time = $S{updTime}" ,field = "upd_time")
    private String updTime;

    @GraphQLField(label = "更新人", kkhtml = "KFieldText", sql = "upd_user = $S{updUser}" ,field = "upd_user")
    private String updUser;

    @GraphQLField(label = "备注", kkhtml = "KFieldText", sql = "remark = $S{remark}" ,field = "remark")
    private String remark;

}
