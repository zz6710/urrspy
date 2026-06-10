package com.kayak.dps.valtabimp.repository;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.valtabimp.model.OdsReadAssetsReport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class OdsReadAssetsReportDao extends ComnDao {

    public SqlResult<OdsReadAssetsReport> findOdsReadAssetsReports(SqlParam<OdsReadAssetsReport> params) throws Exception {
        OdsReadAssetsReport o = params.getModel();
        String sql = "select t1.id ,t1.asset_code ,t1.t8_val_reporttab_id ,t1.note,t1.inputuser,t1.crt_date ,t1.crt_time ,t1.isprodorasset,t2.reporttab_name  " +
                "from ods_fa_readassets_prodreportrel t1\n" +
                "left join base_fa_reporttab t2 on t1.t8_val_reporttab_id = t2.id " +
                "where 1 = 1 ";
        if (StringUtils.isNotBlank(o.getT8ValReporttabId())){
            sql += " and t8_val_reporttab_id = $S{t8ValReporttabId}";
        }
        if (StringUtils.isNotBlank(o.getAssetCode())){
            sql += " and asset_code = $S{assetCode}";
        }
        if (StringUtils.isNotBlank(o.getIsprodorasset())){
            sql += " and isprodorasset = $S{isprodorasset}";
        }
        return super.findRows(sql, params);
    }

    public UpdateResult addOdsReadAssetsReport(SqlParam<OdsReadAssetsReport> params) throws Exception {
        String sql = "insert into ods_fa_readassets_prodreportrel (asset_code , t8_val_reporttab_id ,note, inputuser,crt_date , crt_time , isprodorasset)" +
                "values ($S{assetCode} , $S{t8ValReporttabId} , $S{note} , $S{inputuser} , date_format(CURDATE(),'%Y%m%d') , date_format(CURTIME(),'%H%i%s') , $S{isprodorasset})";
        return super.update(sql, params.getModel());
    }

    public UpdateResult updateOdsReadAssetsReport(SqlParam<OdsReadAssetsReport> params) throws Exception {
        String sql = "update ods_fa_readassets_prodreportrel set " +
                "asset_code = $S{assetCode} , " +
                "t8_val_reporttab_id = $S{t8ValReporttabId} , " +
                "note = $S{note} , " +
                "inputuser = $S{inputuser} , " +
                "crt_date = date_format(CURDATE(),'%Y%m%d') , " +
                "crt_time = date_format(CURTIME(),'%H%i%s') , " +
                "isprodorasset = $S{isprodorasset} " +
                "where id = $S{id}" ;
        return super.update(sql, params.getModel());
    }

    public UpdateResult deleteOdsReadAssetsReport(SqlParam<OdsReadAssetsReport> params) throws Exception {
        String sql = "delete from ods_fa_readassets_prodreportrel where id = $S{id}";
        return super.update(sql, params.getModel());
    }


    public UpdateResult deleteOdsReadAssetsReport(String t8ValReporttabId) throws Exception {
        String sql = "delete from ods_fa_readassets_prodreportrel where t8_val_reporttab_id = $S{t8ValReporttabId} ";
        return super.update(sql, t8ValReporttabId);
    }

    public SqlResult<OdsReadAssetsReport> findOnlyOdsReadAssetsReports(SqlParam<OdsReadAssetsReport> params) throws Exception {
        OdsReadAssetsReport o = params.getModel();
        String sql = "select * from  base_fa_reporttab  where 1 = 1 and reporttab_name = $S{reporttabName} ";
        if (StringUtils.isNotBlank(o.getId())){
            sql += " and id != $S{id}";
        }
        return super.findRows(sql, params);
    }
}
