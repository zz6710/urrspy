package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.model.GridRltPtyOsd;
import org.springframework.stereotype.Repository;

@Repository
public class GridRltPtyOsdDao extends ComnDao {


    public SqlResult<GridRltPtyOsd> findGridRltPtyOsd(SqlParam<GridRltPtyOsd> params) throws Exception {
        return super.findRows("select   id, prod_cd, bred_cd, scr_id, scr_nm, affiliate_name, deal_type, deal_subject, deal_direction, deal_amount, pos_dt, deal_dt, crt_dt  from  app_grid_rlt_pty_osd_base  order by deal_dt",DataSourceProperty.IDB, params);
    }

    public int updateGridRltPtyOsd(SqlParam<GridRltPtyOsd> params) throws Exception {
        String sql = "UPDATE app_grid_rlt_pty_osd_base \n" +
                "SET prod_cd = $S{prodCd},\n" +
                "bred_cd = $S{bredCd},\n" +
                "scr_id = $S{scrId},\n" +
                "scr_nm = $S{scrNm},\n" +
                "affiliate_name = $S{affiliateName},\n" +
                "deal_type = $S{dealType},\n" +
                "deal_subject =  $S{dealSubject},\n" +
                "deal_direction =  $S{dealDirection},\n" +
                "deal_amount =  $S{dealAmount},\n" +
                "pos_dt =  $S{posDt}\n" +
                "WHERE\n" +
                "\tid =  $S{id} ";
        return super.update(sql,DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public int deleteGridRltPtyOsd(SqlParam<GridRltPtyOsd> params)  throws Exception {
        return super.update("delete from  app_grid_rlt_pty_osd_base where id=$S{id}",DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public SqlResult<GridRltPtyOsd> findScrIdAndscrNm(SqlParam<GridRltPtyOsd> params) throws Exception {
        String sql = "SELECT DISTINCT scr_id,scr_nm FROM app_grid_rlt_pty_osd_base where 1=1 ";
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
