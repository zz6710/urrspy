package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.AssetCodeManageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;


@Repository
public class AssetCodeManageModelDao extends ComnDao {

    public SqlResult<AssetCodeManageModel> findAssetCodeManage(SqlParam<AssetCodeManageModel> params) throws Exception {
        String sql = "select " +
                "t1.ID,"+
                "t1.SCR_CD,"+
                "t1.OLD_SCR_CD,"+
                "t1.SCR_NM,"+
                "t1.TRX_MKT,"+
                "t1.ASSET_TYPE,"+
                "t1.STATUS,"+
                "t1.DATA_SOURCE,"+
                "t1.EFFECTIVE_DATE,"+
                "t1.EFFECTIVE_TIME,"+
                "t1.EXPIRATION_DATE,"+
                "t1.EXPIRATION_TIME,"+
                "t1.UPD_DATE,"+
                "t1.UPD_TIME,"+
                "t1.UPD_USER "+
                "from base_asset_code_management t1 where 1=1 ";
        if (StringUtils.isNotBlank(params.getModel().getOldScrCd())){
            sql += "and t1.OLD_SCR_CD = $S{oldScrCd} ";
        }
        if (StringUtils.isNotBlank(params.getModel().getScrCd())){
            sql += "and t1.SCR_CD  = $S{scrCd}";
        }
        return super.findRows(sql,DataSourceProperty.PUB, params);
    }

    public void deleteAssetCodeManage(SqlParam<AssetCodeManageModel> params) throws Exception {
        String sql = " delete from base_asset_code_management where id = $S{id}";
        super.update(sql, DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult updateAssetCodeManage(SqlParam<AssetCodeManageModel> params) throws Exception {
        return super.update("update base_asset_code_management set " +
                "SCR_CD= $S{scrCd}, " +
                "OLD_SCR_CD= $S{oldScrCd}, " +
                "SCR_NM= $S{scrNm}, " +
                "TRX_MKT= $S{trxMkt}, " +
                "ASSET_TYPE= $S{trxMkt}, " +
                "STATUS= $S{status}, " +
                "DATA_SOURCE= $S{dataSource}, " +
                "EFFECTIVE_DATE= $S{effectiveDate}, " +
                "EFFECTIVE_TIME= $S{effectiveTime}, " +
                "EXPIRATION_DATE= $S{expirationDate}, " +
                "EXPIRATION_TIME= $S{expirationTime}, " +
                "UPD_DATE= $S{updDate}, " +
                "UPD_TIME= $S{updTime}, " +
                "UPD_USER= $S{updUser} " +
                "where id= $S{id}", DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult insertAssetCodeManage(SqlParam<AssetCodeManageModel> params) throws Exception {
        return super.update("insert into base_asset_code_management (" +
                "SCR_CD,"+
                "OLD_SCR_CD,"+
                "SCR_NM,"+
                "TRX_MKT,"+
                "ASSET_TYPE,"+
                "STATUS,"+
                "DATA_SOURCE,"+
                "EFFECTIVE_DATE,"+
                "EFFECTIVE_TIME,"+
                "EXPIRATION_DATE,"+
                "EXPIRATION_TIME)values("+
                "$S{scrCd}," +
                "$S{oldScrCd}," +
                "$S{scrNm}," +
                "$S{trxMkt}," +
                "$S{assetType}," +
                "$S{status}," +
                "$S{dataSource}," +
                "$S{effectiveDate}," +
                "$S{effectiveTime}," +
                "$S{expirationDate}," +
                "$S{expirationTime})", DataSourceProperty.PUB, params.getModel());
    }
}
