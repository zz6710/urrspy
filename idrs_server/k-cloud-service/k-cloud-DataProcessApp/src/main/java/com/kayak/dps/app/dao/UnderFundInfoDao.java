package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.UnderFundInfo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class UnderFundInfoDao extends ComnDao {

    public SqlResult<UnderFundInfo> findFundInfos(SqlParam<UnderFundInfo> params) throws Exception {
        String sql = "SELECT id, bank_code, asset_manager_code, convert_sum_amt, asset_sum_number, non_invested_amt, under_asset_code," +
                " under_asset_sum, under_convert_sum_amt, report_date, crt_date,crt_time,crt_user,upd_date,upd_time,upd_user " +
                "FROM app_under_fund_info where 1=1 ";
        if (Strings.isNotBlank(params.getModel().getAssetManagerCode())) {
            sql += " and asset_manager_code = $S{assetManagerCode}";
        }
        if (Strings.isNotBlank(params.getModel().getUnderAssetCode())) {
            sql += " and under_asset_code = $S{underAssetCode}";
        }
        if (Strings.isNotBlank(params.getModel().getReportDate())) {
            sql += " and report_date = $S{reportDate}";
        }
        if (Strings.isNotBlank(params.getModel().getBankCode())) {
            sql += " and bank_code = $S{bankCode}";
        }
        return super.findRows(sql, params);
    }

    public UpdateResult addFundInfo(UnderFundInfo params) throws Exception {
        return super.update("INSERT INTO app_under_fund_info(id ,bank_code,asset_manager_code,convert_sum_amt,asset_sum_number,non_invested_amt,under_asset_code,under_asset_sum,under_convert_sum_amt,report_date,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user) " +
                        "VALUES($AUTOIDS{id},$S{bankCode},$S{assetManagerCode},$S{convertSumAmt},$S{assetSumNumber},$S{nonInvestedAmt},$S{underAssetCode},$S{underAssetSum},$S{underConvertSumAmt},$S{reportDate},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})",
                params);
    }

    public UpdateResult updateFundInfo(UnderFundInfo params) throws Exception {
        return super.update("UPDATE app_under_fund_info SET " +
                        "bank_code=$S{bankCode} ," +
                        "asset_manager_code=$S{assetManagerCode} ," +
                        "convert_sum_amt=$S{convertSumAmt} ," +
                        "asset_sum_number=$S{assetSumNumber} ," +
                        "non_invested_amt=$S{nonInvestedAmt} ," +
                        "under_asset_code=$S{underAssetCode} ," +
                        "under_asset_sum=$S{underAssetSum} ," +
                        "under_convert_sum_amt=$S{underConvertSumAmt} ," +
                        "report_date=$S{reportDate} ," +
                        "upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser}" +
                        " WHERE id = $S{id}",
                params);
    }

    public UpdateResult deleteFundInfo(UnderFundInfo params) throws Exception {
        return super.update("DELETE FROM app_under_fund_info WHERE id = $S{id}",
                params);
    }
    public SqlResult<UnderFundInfo> findassetManagerCode(SqlParam<UnderFundInfo> params) throws Exception {
        String sql = "SELECT  asset_manager_code" +
                " FROM app_under_fund_info where asset_manager_code like '%$U{assetManagerCode}%' ";
        return super.findRows(sql, params);
    }

}
