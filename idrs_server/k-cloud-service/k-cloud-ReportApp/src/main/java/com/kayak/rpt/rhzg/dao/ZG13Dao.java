package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzg.model.ZG13;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ZG13Dao extends ComnDao {

    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG13> findZG13s(SqlParam<ZG13> params) throws Exception {
        String sql = "SELECT id,prod_cd,report_date,asset_debt_project,scr_cd,scr_org_nm,scr_org_cd,org_blg_zon,org_blg_industry,org_typ_ecn,org_typ_scale,right_invest_way,right_org_cd,right_org_nm,ccy_cd,amount,amount_cny,right_ccy_cd,right_amount,right_amount_cny,pos_rat,invest_ext_way,bgn_dt,mtu_dt,defer_mtu_dt,theory_report_start_date,theory_report_end_date from app_pbc_report_zg13 where sys_data_status ='1' ";
        if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
            sql = sql + " and  prod_cd = '" + params.getModel().getProdCd() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql = sql + " and  report_date like '" + params.getModel().getReportDate() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getId())) {
            sql = sql + " and  id = '" + params.getModel().getId() + "'";
        }
        return super.findRows(sql, params);
    }

    public UpdateResult updateZG13(SqlParam<ZG13> params) throws Exception {
        return super.update("update app_pbc_report_zg13 set prod_cd=$S{prodCd} ," +
                        "asset_debt_project=$S{assetDebtProject}," +
                        "scr_cd = $S{scrCd}," +
                        "scr_org_nm = $S{scrOrgNm}," +
                        "scr_org_cd = $S{scrOrgCd}," +
                        "org_blg_zon = $S{orgBlgZon}," +
                        "org_blg_industry = $S{orgBlgIndustry}," +
                        "org_typ_ecn = $S{orgTypEcn}," +
                        "org_typ_scale = $S{orgTypScale}," +
                        "right_invest_way = $S{rightInvestWay}," +
                        "right_org_cd = $S{rightOrgCd}," +
                        "right_org_nm = $S{rightOrgNm}," +
                        "ccy_cd = $S{ccyCd}," +
                        "amount=if($S{amount}='',null,$S{amount})," +
                        "amount_cny=if($S{amountCny}='',null,$S{amountCny})," +
                        "right_ccy_cd = $S{rightCcyCd}," +
                        "right_amount=if($S{rightAmount}='',null,$S{rightAmount})," +
                        "right_amount_cny=if($S{rightAmountCny}='',null,$S{rightAmountCny})," +
                        "pos_rat=if($S{posRat}='',null,$S{posRat})," +
                        "invest_ext_way = $S{investExtWay}," +
                        "bgn_dt = $S{bgnDt},mtu_dt = $S{mtuDt},defer_mtu_dt = $S{deferMtuDt}  WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG13(SqlParam<ZG13> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg13 WHERE id = $S{id}",
                params.getModel());
    }


    public UpdateResult deleteZG13ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg13 where report_date = $S{beginDate} ", params);
    }



}
