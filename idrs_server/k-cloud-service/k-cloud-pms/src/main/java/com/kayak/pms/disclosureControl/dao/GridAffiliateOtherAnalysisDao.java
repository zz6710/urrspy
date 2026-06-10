package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.model.GridAffiliateOtherAnalysis;
import com.kayak.pms.disclosureControl.model.GridCsmBndInvRltPty;
import org.springframework.stereotype.Repository;

@Repository
public class GridAffiliateOtherAnalysisDao extends ComnDao {


    public SqlResult<GridAffiliateOtherAnalysis> findGridAffiliateOtherAnalysis(SqlParam<GridAffiliateOtherAnalysis> params) throws Exception {
        return super.findRows("select  id, prod_cd, bred_cd, scr_id, affiliate_name, scr_nm, deal_type, deal_amount, pos_dt, deal_dt, crt_dt from  app_grid_affiliate_other_analysis_base  order by deal_dt",DataSourceProperty.IDB, params);
    }

    public int updateGridAffiliateOtherAnalysis(SqlParam<GridAffiliateOtherAnalysis> params) throws Exception {
        String sql = "UPDATE app_grid_affiliate_other_analysis_base \n" +
                "SET prod_cd = $S{prodCd},\n" +
                "bred_cd = $S{bredCd},\n" +
                "scr_id = $S{scrId},\n" +
                "affiliate_name = $S{affiliateName},\n" +
                "scr_nm = $S{scrNm},\n" +
                "deal_type = $S{dealType},\n" +
                "deal_amount = $S{dealAmount},\n" +
                "pos_dt = $S{posDt}\n" +
                "WHERE\n" +
                "\tid = $S{id}";
        return super.update(sql,DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public int deleteGridAffiliateOtherAnalysis(SqlParam<GridAffiliateOtherAnalysis> params) throws Exception {
        return super.update("delete  from  app_grid_affiliate_other_analysis_base where  id = $S{id}",DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public SqlResult<GridAffiliateOtherAnalysis> findScrIdAndscrNm(SqlParam<GridAffiliateOtherAnalysis> params) throws Exception {
        String sql = "SELECT DISTINCT scr_id,scr_nm FROM app_grid_affiliate_other_analysis_base where 1=1 ";
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
