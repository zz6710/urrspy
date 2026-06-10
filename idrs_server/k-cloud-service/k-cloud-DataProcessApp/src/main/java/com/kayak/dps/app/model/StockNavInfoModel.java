package com.kayak.dps.app.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

/**
 * @author Ty
 * @since 2023-03-16 15:12:32
 */
@Data
@GraphQLModel(fetcher = "stockNavInfoModelService", table = "dwd_stock_nav_info")
public class StockNavInfoModel {

    @GraphQLField(key = true, kkhtml = "KFieldText", label = "股票代码", sql = "STOCK_CODE like '%$U{stockCode}%'", field = "stock_code")
    private String stockCode;
    @GraphQLField(kkhtml = "KFieldSelect", label = "股票名称", sql = "STOCK_NAME like '%$U{stockName}%'", field = "stock_name")
    private String stockName;
    @GraphQLField(kkhtml = "KFieldText", label = "股票类型", sql = "stock_type = $S{stockType}", field = "stock_type")
    private String stockType;
    @GraphQLField(kkhtml = "KFieldText", label = "昨收盘价（元）", field = "last_clo_price")
    private Double lastCloPrice;
    @GraphQLField(kkhtml = "KFieldText", label = "最高价（元）", field = "high_price")
    private Double highPrice;
    @GraphQLField(kkhtml = "KFieldText", label = "最低价（元）", field = "low_price")
    private Double lowPrice;

    @GraphQLField(kkhtml = "KFieldText", label = "起始日期", sql = "start_date >= $S{startDate}",field = "start_date")
    private String startDate;
    @GraphQLField(kkhtml = "KFieldText", label = "结束日期", sql = "end_date <= $S{endDate}",field = "end_date")
    private String endDate;

    @GraphQLField(kkhtml = "KFieldText", label = "涨跌（元）", field = "profit_and_loss")
    private Double profitAndLoss;
    @GraphQLField(kkhtml = "KFieldText", label = "涨跌（元）", field = "profit_and_loss_range")
    private Double profitAndLossRange;
    @GraphQLField(kkhtml = "KFieldText", label = "成交量（手）", field = "trading_volume")
    private Integer tradingVolume;
    @GraphQLField(kkhtml = "KFieldText", label = "成交金额（亿元）", field = "trading_money")
    private Double tradingMoney;
    @GraphQLField(kkhtml = "KFieldText", label = "开盘价（元）", field = "start_price")
    private Double startPrice;
    @GraphQLField(kkhtml = "KFieldText", label = "收盘价（元）", field = "clo_price")
    private Double cloPrice;
}
