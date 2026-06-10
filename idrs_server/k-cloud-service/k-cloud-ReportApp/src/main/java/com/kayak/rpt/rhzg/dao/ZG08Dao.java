package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzg.model.ZG08;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ZG08Dao extends ComnDao {



    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG08> findZG08s(SqlParam<ZG08> params) throws Exception {
        String sql = "SELECT ID,PROD_CD,report_date,theory_report_start_date,ASSET_DEBT_PROJECT,COUNTERPART_PROD_TYP,COUNTERPART_ORG_CD,COUNTERPART_PROD_CD,CCY,END_DT_AMT,END_DT_AMT_CNY FROM app_pbc_report_zg08 where sys_data_status ='1' ";
        if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
            sql = sql + " and  prod_cd like '%" + params.getModel().getProdCd() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql = sql + " and  report_date like '" + params.getModel().getReportDate() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getId())) {
            sql = sql + " and  id = '" + params.getModel().getId() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAssetDebtProject())) {
            sql = sql + " and  asset_debt_project = '" + params.getModel().getAssetDebtProject() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getCounterpartProdTyp())) {
            sql = sql + " and  counterpart_prod_typ = '" + params.getModel().getCounterpartProdTyp() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getCounterpartOrgCd())) {
            sql = sql + " and  counterpart_org_cd = '" + params.getModel().getCounterpartOrgCd() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getCounterpartProdCd())) {
            sql = sql + " and  counterpart_prod_cd = '" + params.getModel().getCounterpartProdCd() + "'";
        }
        return super.findRows(sql, params);
    }

    public UpdateResult updateZG08(SqlParam<ZG08> params) throws Exception {
        return super.update("UPDATE app_pbc_report_zg08 SET PROD_CD=$S{prodCd},theory_report_start_date=$S{theoryReportStartDate},ASSET_DEBT_PROJECT=$S{assetDebtProject},COUNTERPART_PROD_TYP=$S{counterpartProdTyp},COUNTERPART_ORG_CD=$S{counterpartOrgCd},COUNTERPART_PROD_CD=$S{counterpartProdCd},CCY=$S{ccy},END_DT_AMT=if($S{endDtAmt}='',null,$S{endDtAmt}),END_DT_AMT_CNY=if($S{endDtAmtCny}='',null,$S{endDtAmtCny})  WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG08(SqlParam<ZG08> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg08 WHERE id = $S{id}",
                params.getModel());
    }


    public UpdateResult deleteZg08ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg08 where report_date = $S{beginDate} ", params);
    }

    public UpdateResult addZg08(Object params) throws Exception {
        return super.update("INSERT INTO app_pbc_report_zg08(PROD_CD,theory_report_start_date,register_status,ASSET_DEBT_PROJECT,COUNTERPART_PROD_TYP,COUNTERPART_ORG_CD,COUNTERPART_PROD_CD,CCY,END_DT_AMT,END_DT_AMT_CNY) VALUES($S{prodCd},$S{theoryReportStartDate},$S{registerStatus},$S{assetDebtProject},$S{counterpartProdTyp},$S{counterpartOrgCd},$S{counterpartProdCd},$S{ccy},if($S{endDtAmt}='',null,$S{endDtAmt}),if($S{endDtAmtCny}='',null,$S{endDtAmtCny}))", params);
    }
}
