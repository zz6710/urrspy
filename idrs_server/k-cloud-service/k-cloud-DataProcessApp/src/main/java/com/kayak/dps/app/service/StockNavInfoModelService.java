package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.dao.StockNavInfoModelDao;
import com.kayak.dps.app.model.StockNavInfoModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@APIDefine(desc = "股票净值信息服务", model = StockNavInfoModel.class)
@Slf4j
public class StockNavInfoModelService {

    @Autowired
    private StockNavInfoModelDao stockNavInfoModelDao;

    @API(desc = "查询股票信息代码与名称", operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<StockNavInfoModel> findStockNavInfoCdAndNm(SqlParam<StockNavInfoModel> params) throws Exception {
//        params.setMakeSql(true);
        return stockNavInfoModelDao.findStockNavInfoModels(params);
    }

    @API(desc = "查询股票代码", operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<StockNavInfoModel> findStockNavInfoCd(SqlParam<StockNavInfoModel> params) throws Exception{
        SqlResult<StockNavInfoModel> result = stockNavInfoModelDao.findStockNavInfoModelCd(params);
        List<StockNavInfoModel> rows = result.getRows();
        rows.forEach(System.out::println);
        return result;
    }


    @API(desc = "查询股票估值信息", auth = APIAuth.YES)
    public SqlResult<StockNavInfoModel> findStockNavInfoModelsByStockCd(SqlParam<StockNavInfoModel> params) throws Exception {
        params.setMakeSql(true);
        return stockNavInfoModelDao.findStockNavInfoModelsByStockCd(params);
    }

    @API(desc = "删除股票估值信息", auth = APIAuth.YES)
    public String deleteStockNavInfoModel(SqlParam<StockNavInfoModel> params) throws Exception {
        params.setMakeSql(true);
        try {
            stockNavInfoModelDao.deleteStockNavInfoModel(params);
            return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "删除失败", null).toString();
        }

    }

    @API(desc = "添加股票信息", auth = APIAuth.YES)
    public String addStockNavInfoModel(SqlParam<StockNavInfoModel> params) throws Exception {
        try {
            // 如果交易市场是上交所或者深交所
            SqlResult<StockNavInfoModel> stockNavCode = stockNavInfoModelDao.findStockNavCode(params);
            List<StockNavInfoModel> stockCodeRows = stockNavCode.getRows();
            for (StockNavInfoModel stockCodeRow : stockCodeRows) {
                if (params.getModel().getStockCode().equals(stockCodeRow.getStockCode())) {
                    return RequestSupport.updateReturnJson(false, "存在有相同的股票信息，新增失败", null).toString();
                }
            }
            // 添加股票信息
            stockNavInfoModelDao.addStockNavInfoModel(params);

            return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false, "新增失败", null).toString();
        }
    }


}
