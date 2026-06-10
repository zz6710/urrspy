package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzg.model.ZG03;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ZG03Dao extends ComnDao {






    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG03> findZG03s(SqlParam<ZG03> params) throws Exception {
        String sql = "SELECT ID,PROD_CD,report_date,theory_report_start_date,PROD_REL_END_DT,TRANS_CCY,ISU_ORG_ERN,ISU_ORG_ERN_CNY,CUST_CALL_ERN,CUST_CALL_ERN_CNY,CUST_CALL_ERN_RATE " +
                "       FROM app_pbc_report_zg03 where sys_data_status ='1' " ;
        if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
            sql = sql + " and prod_cd = '" + params.getModel().getProdCd() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
            sql = sql + " and report_date >= '" + params.getModel().getBeginDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
            sql = sql + " and report_date <= '" + params.getModel().getQueryDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getId())) {
            sql = sql + " and id = '" + params.getModel().getId() + "'";
        }
        return super.findRows(sql, params);
    }





    public UpdateResult updateZG03(SqlParam<ZG03> params) throws Exception {
        return super.update("UPDATE app_pbc_report_zg03 SET PROD_CD=$S{prodCd},theory_report_start_date=$S{theoryReportStartDate},PROD_REL_END_DT=$S{prodRelEndDt},TRANS_CCY=$S{transCcy},ISU_ORG_ERN=if($S{isuOrgErn}='',null,$S{isuOrgErn}),ISU_ORG_ERN_CNY=if($S{isuOrgErnCny}='',null,$S{isuOrgErnCny}),CUST_CALL_ERN=if($S{custCallErn}='',null,$S{custCallErn}),CUST_CALL_ERN_CNY=if($S{custCallErnCny}='',null,$S{custCallErnCny}),CUST_CALL_ERN_RATE=if($S{custCallErnRate}='',null,$S{custCallErnRate})  WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG03(SqlParam<ZG03> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg03 WHERE id = $S{id}",
                params.getModel());
    }


    public UpdateResult deleteZg03ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg03 where report_date = $S{reportDate}", params);
    }

    public UpdateResult addZg03(Object params) throws Exception {
        return super.update("INSERT INTO app_pbc_report_zg03(PROD_CD,theory_report_start_date,register_status,PROD_REL_END_DT,TRANS_CCY,ISU_ORG_ERN,ISU_ORG_ERN_CNY,CUST_CALL_ERN,CUST_CALL_ERN_CNY,CUST_CALL_ERN_RATE) VALUES($S{prodCd},$S{theoryReportStartDate},$S{registerStatus},$S{prodRelEndDt},$S{transCcy},if($S{isuOrgErn}='',null,$S{isuOrgErn}),if($S{isuOrgErnCny}='',null,$S{isuOrgErnCny}),if($S{custCallErn}='',null,$S{custCallErn}),if($S{custCallErnCny}='',null,$S{custCallErnCny}),if($S{custCallErnRate}='',null,$S{custCallErnRate}))", params);
    }

}
