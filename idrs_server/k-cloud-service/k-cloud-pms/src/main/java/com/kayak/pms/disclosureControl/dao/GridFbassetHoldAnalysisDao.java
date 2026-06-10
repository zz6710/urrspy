package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.model.GridFbassetHoldAnalysis;
import org.springframework.stereotype.Repository;

@Repository
public class GridFbassetHoldAnalysisDao extends ComnDao {


    public SqlResult<GridFbassetHoldAnalysis> findGridFbassetHoldAnalysis(SqlParam<GridFbassetHoldAnalysis> params) throws Exception {
        return super.findRows("select  id, prod_cd, bred_cd, scr_id, scr_nm, finance_customer, project_name, left_days, income_allocate, deal_structure, risk_conditions, pos_dt, deal_dt, crt_dt from  app_grid_fbasset_holding_analysis_base order by deal_dt",DataSourceProperty.IDB, params);
    }

    public int updateGridFbassetHoldAnalysis(SqlParam<GridFbassetHoldAnalysis> params) throws Exception {
        String sql = "UPDATE app_grid_fbasset_holding_analysis_base \n" +
                "SET prod_cd = $S{prodCd},\n" +
                "bred_cd = $S{bredCd},\n" +
                "scr_id = $S{scrId},\n" +
                "scr_nm = $S{scrNm},\n" +
                "finance_customer = $S{financeCustomer},\n" +
                "project_name = $S{projectName},\n" +
                "left_days = $S{leftDays},\n" +
                "income_allocate = $S{incomeAllocate},\n" +
                "deal_structure = $S{dealStructure},\n" +
                "risk_conditions = $S{riskConditions},\n" +
                "pos_dt = $S{posDt},\n" +
                "deal_dt = $S{dealDt}\n" +
                "WHERE\n" +
                "\tid = $S{id}";
        return super.update(sql,DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public int deleteGridFbassetHoldAnalysis(SqlParam<GridFbassetHoldAnalysis> params) throws Exception {
        return super.update("delete from app_grid_fbasset_holding_analysis_base where id=$S{id}",DataSourceProperty.IDB,params.getModel()).getEffect();
    }

    public SqlResult<GridFbassetHoldAnalysis> findScrIdAndscrNm(SqlParam<GridFbassetHoldAnalysis> params) throws Exception {
        String sql = "SELECT DISTINCT scr_id,scr_nm FROM app_grid_fbasset_holding_analysis_base where 1=1 ";
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
