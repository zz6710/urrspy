package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.model.GridCombineRiskAnalysis;
import org.springframework.stereotype.Repository;

@Repository
public class GridCombineRiskAnalysisDao extends ComnDao {


    public SqlResult<GridCombineRiskAnalysis> findGridCombineRiskAnalysis(SqlParam<GridCombineRiskAnalysis> params) throws Exception {
        return super.findRows("select  id, prod_cd, bred_cd, invest_way, invest_type, balance_amt, pos_dt, deal_dt, crt_dt  from  app_grid_combine_risk_analysis_base  order by deal_dt",DataSourceProperty.IDB, params);
    }

    public int updateGridCombineRiskAnalysis(SqlParam<GridCombineRiskAnalysis> params) throws Exception {
        String sql = "UPDATE app_grid_combine_risk_analysis_base \n" +
                "SET prod_cd = $S{prodCd},\n" +
                "bred_cd = $S{bredCd},\n" +
                "invest_way = $S{investWay},\n" +
                "invest_type = $S{investType},\n" +
                "balance_amt = $S{balanceAmt},\n" +
                "pos_dt = $S{posDt}\n" +
                "WHERE\n" +
                "\tid = $S{id}";
        return super.update(sql,DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public int deleteGridCombineRiskAnalysis(SqlParam<GridCombineRiskAnalysis> params) throws Exception {
        return super.update("delete  from  app_grid_combine_risk_analysis_base where  id = $S{id}",DataSourceProperty.IDB,params.getModel()).getEffect();
    }
}
