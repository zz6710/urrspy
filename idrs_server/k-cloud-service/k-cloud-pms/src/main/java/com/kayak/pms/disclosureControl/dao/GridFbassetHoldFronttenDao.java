package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.model.GridFbassetHoldAnalysis;
import com.kayak.pms.disclosureControl.model.GridFbassetHoldFrontten;
import com.kayak.pms.disclosureControl.model.RegularDisProdConfirm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class GridFbassetHoldFronttenDao extends ComnDao {


    public SqlResult<GridFbassetHoldFrontten> findGridFbassetHoldFrontten(SqlParam<GridFbassetHoldFrontten> params) throws Exception {
        return super.findRows("select  id, prod_cd, bred_cd, scr_id, scr_nm, bal_amt, pos_dt, deal_dt, crt_dt   from   app_grid_fbasset_holding_frontten_base order by deal_dt",DataSourceProperty.IDB, params);
    }

    public int updateGridFbassetHoldFrontten(SqlParam<GridFbassetHoldFrontten> params) throws Exception {
        String sql = "UPDATE \n" +
                "app_grid_fbasset_holding_frontten_base \n" +
                "SET prod_cd = $S{prodCd}, \n" +
                "\tbred_cd = $S{bredCd}, \n" +
                "\tscr_id = $S{scrId}, \n" +
                "\tscr_nm = $S{scrNm}, \n" +
                "\tbal_amt = $S{balAmt}, \n" +
                "\tpos_dt = $S{posDt}\n" +
                "WHERE id = $S{id}";
        return super.update(sql,DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public int deleteGridFbassetHoldFrontten(SqlParam<GridFbassetHoldFrontten> params) throws Exception {
        return super.update("delete  from  app_grid_fbasset_holding_frontten_base where  id=$S{id} ",DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public SqlResult<GridFbassetHoldFrontten> findScrIdAndscrNm(SqlParam<GridFbassetHoldFrontten> params) throws Exception {
        String sql = "SELECT DISTINCT scr_id,scr_nm FROM app_grid_fbasset_holding_frontten_base where 1=1 ";
        if(Tools.isNotBlank(params.getModel().getScrId()) && Tools.isNotBlank(params.getModel().getScrNm())){
            sql = sql + " and (scr_id like '%$U{scrId}%' or scr_nm like '%$U{scrNm}%')";
        }else if(Tools.isNotBlank(params.getModel().getScrNm())){
            sql = sql + " and scr_nm like '%$U{scrNm}%' ";
        }else if(Tools.isNotBlank(params.getModel().getScrId())){
            sql = sql + " and scr_id like '%$U{scrId}%' ";
        }
        return super.findRows(sql,DataSourceProperty.IDB, params);
    }
}

