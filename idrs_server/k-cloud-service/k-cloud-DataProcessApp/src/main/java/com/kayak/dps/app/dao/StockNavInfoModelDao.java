package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.StockNavInfoModel;
import org.springframework.stereotype.Repository;

@Repository
public class StockNavInfoModelDao extends ComnDao {

    public SqlResult<StockNavInfoModel> findStockNavInfoModelCd(SqlParam<StockNavInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT aas.stock_code FROM dwd_stock_nav_info aas WHERE 1=1";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    public SqlResult<StockNavInfoModel> findStockNavInfoModels(SqlParam<StockNavInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT stock_code,stock_name FROM dwd_stock_nav_info where stock_code like '%$U{stockCode}%' or stock_name like '%$U{stockName}%'";

        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    public SqlResult<StockNavInfoModel> findStockNavCode(SqlParam<StockNavInfoModel> params) throws Exception {
        String sql = "SELECT DISTINCT stock_code from dwd_stock_nav_info where 1=1";

        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    public SqlResult<StockNavInfoModel> findStockNavInfoModelsByStockCd(SqlParam<StockNavInfoModel> params) throws Exception {
        String sql = "SELECT * FROM dwd_stock_nav_info where 1=1";
        return super.findRows(sql, DataSourceProperty.PUB, params);

    }

    // 插入股票信息
    public UpdateResult addStockNavInfoModel(SqlParam<StockNavInfoModel> params) throws Exception {
        return super.update("INSERT INTO dwd_stock_nav_info(stock_code,stock_name,stock_type,start_date,end_date,last_clo_price,high_price,low_price,profit_and_loss,profit_and_loss_range,trading_volume,trading_money,start_price,clo_price)\n" +
                        "VALUES($S{stockCode} ,$S{stockName} ,$S{stockType} ,$S{startDate} ,$S{endDate} ,$D{lastCloPrice} ,$D{highPrice} ,$D{lowPrice} ,$D{profitAndLoss} ,$D{profitAndLossRange} ,$D{tradingVolume} ,$D{tradingMoney} ,$D{startPrice} ,$D{cloPrice})",
                DataSourceProperty.PUB, params.getModel());
    }


    // 删除
    public UpdateResult deleteStockNavInfoModel(SqlParam<StockNavInfoModel> params) throws Exception {
        return super.update("DELETE FROM dwd_stock_nav_info WHERE stock_code=$S{stockCode}",
                DataSourceProperty.PUB, params.getModel());
    }

}
