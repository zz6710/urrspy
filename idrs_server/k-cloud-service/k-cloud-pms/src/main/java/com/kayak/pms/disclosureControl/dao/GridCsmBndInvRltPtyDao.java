package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.model.GridCsmBndInvRltPty;
import org.springframework.stereotype.Repository;

@Repository
public class GridCsmBndInvRltPtyDao extends ComnDao {


    public SqlResult<GridCsmBndInvRltPty> findGridCsmBndInvRltPty(SqlParam<GridCsmBndInvRltPty> params) throws Exception {
        return super.findRows("select  id, prod_cd, bred_cd, affiliate_name, securities_code, securities_name, deal_amount, party_relation, pos_dt, deal_dt, crt_dt from  app_grid_bnd_inv_rlt_pty_base  order by deal_dt",DataSourceProperty.IDB, params);
    }

    public int updateGridCsmBndInvRltPty(SqlParam<GridCsmBndInvRltPty> params) throws Exception {
        String sql = "UPDATE app_grid_bnd_inv_rlt_pty_base \n" +
                "SET prod_cd = $S{prodCd},\n" +
                "bred_cd = $S{bredCd},\n" +
                "affiliate_name = $S{affiliateName},\n" +
                "securities_code = $S{securitiesCode},\n" +
                "securities_name = $S{securitiesName},\n" +
                "deal_amount = $S{dealAmount},\n" +
                "party_relation = $S{partyRelation},\n" +
                "pos_dt = $S{posDt},\n" +
                "deal_dt = $S{dealDt}\n" +
                "WHERE\n" +
                "\tid = $S{id}";
        return super.update(sql,DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public int deleteGridCsmBndInvRltPty(SqlParam<GridCsmBndInvRltPty> params) throws Exception {
        return super.update("delete  from  app_grid_bnd_inv_rlt_pty_base where  id = $S{id}",DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public SqlResult<GridCsmBndInvRltPty> findSecuritiesCodeAndSecuritiesName(SqlParam<GridCsmBndInvRltPty> params) throws Exception {
        String sql = "SELECT DISTINCT securities_code,securities_name FROM app_grid_bnd_inv_rlt_pty_base where 1=1 ";
        if(Tools.isNotBlank(params.getModel().getSecuritiesCode()) && Tools.isNotBlank(params.getModel().getSecuritiesName())){
            sql = sql + " and (securities_code like '%$U{securitiesCode}%' or securities_name like '%$U{securitiesName}%')";
        }else if(Tools.isNotBlank(params.getModel().getSecuritiesName())){
            sql = sql + " and securities_name like '%$U{securitiesName}%' ";
        }else if(Tools.isNotBlank(params.getModel().getSecuritiesCode())){
            sql = sql + " and securities_code like '%$U{securitiesCode}%' ";
        }
        return super.findRows(sql,DataSourceProperty.IDB, params);
    }
}
