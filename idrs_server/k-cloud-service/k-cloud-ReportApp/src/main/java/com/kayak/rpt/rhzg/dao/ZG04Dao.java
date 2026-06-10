package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzg.model.ZG04;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ZG04Dao extends ComnDao {



    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG04> findZG04s(SqlParam<ZG04> params) throws Exception {
        String sql = " SELECT t.id,t.prod_cd,t.report_date,t.theory_report_start_date,t.clc_source_zon_cd,t.clc_source_cust_typ,t.trans_ccy,t.cur_pch_amt,t.cur_pch_amt_cny,t.cur_pch_lot,t.cur_call_amt,t.cur_call_amt_cny,t.cur_call_lot,t.end_prod_amt,t.end_prod_amt_cny,t.end_prod_lot,FORMAT(t.netval_prod_end_nav,8) netval_prod_end_nav,FORMAT(t.netval_prod_end_nav_cny,8) netval_prod_end_nav_cny,FORMAT(t.netval_prod_end_acm_nav,8) netval_prod_end_acm_nav,FORMAT(t.netval_prod_end_acm_nav_cny,8) netval_prod_end_acm_nav_cny,t.prod_end_anl_yld,t.month_end_anl_yld FROM app_pbc_report_zg04 t  where sys_data_status ='1'  ";
        if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
            sql = sql + " and  t.prod_cd = '" + params.getModel().getProdCd() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql = sql + " and  t.report_date like '" + params.getModel().getReportDate() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getId())) {
            sql = sql + " and  t.id = '" + params.getModel().getId() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getClcSourceZonCd())) {
            sql = sql + " and  t.clc_source_zon_cd like '%" + params.getModel().getClcSourceZonCd() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getClcSourceCustTyp())) {
            sql = sql + " and  t.clc_source_cust_typ = '" + params.getModel().getClcSourceCustTyp() + "'";
        }
        sql=sql+" order by t.trans_ccy desc , t.prod_cd,t.clc_source_zon_cd,t.clc_source_cust_typ";
        return super.findRows(sql, params);
    }

    public UpdateResult updateZG04(SqlParam<ZG04> params) throws Exception {
        return super.update("UPDATE app_pbc_report_zg04 SET prod_cd=$S{prodCd},theory_report_start_date=$S{theoryReportStartDate},clc_source_zon_cd = $S{clcSourceZonCdText},clc_source_cust_typ = $S{clcSourceCustTyp},trans_ccy = $S{transCcy},cur_pch_amt=if($S{curPchAmt}='',null,$S{curPchAmt}),cur_pch_amt_cny=if($S{curPchAmtCny}='',null,$S{curPchAmtCny}),cur_pch_lot=if($S{curPchLot}='',null,$S{curPchLot}),cur_call_amt=if($S{curCallAmt}='',null,$S{curCallAmt}),cur_call_amt_cny=if($S{curCallAmtCny}='',null,$S{curCallAmtCny}),cur_call_lot=if($S{curCallLot}='',null,$S{curCallLot}),end_prod_amt=if($S{endProdAmt}='',null,$S{endProdAmt}),end_prod_amt_cny=if($S{endProdAmtCny}='',null,$S{endProdAmtCny}),end_prod_lot=if($S{endProdLot}='',null,$S{endProdLot}),netval_prod_end_nav=if($S{netvalProdEndNav}='',null,$S{netvalProdEndNav}),netval_prod_end_nav_cny=if($S{netvalProdEndNavCny}='',null,$S{netvalProdEndNavCny}),netval_prod_end_acm_nav=if($S{netvalProdEndAcmNav}='',null,$S{netvalProdEndAcmNav}),netval_prod_end_acm_nav_cny=if($S{netvalProdEndCcmNavCny}='',null,$S{netvalProdEndCcmNavCny}),netval_prod_end_acm_nav_cny=if($S{netvalProdEndAcmNavCny}='',null,$S{netvalProdEndAcmNavCny}),prod_end_anl_yld=if($S{prodEndAnlYld}='',null,$S{prodEndAnlYld}),theory_report_start_date=$S{theoryReportStartDate},month_end_anl_yld = if($S{monthEndAnlYld}='',null,$S{monthEndAnlYld}) WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG04(SqlParam<ZG04> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg04 WHERE id = $S{id}",
                params.getModel());
    }

    public  UpdateResult deleteZG04ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg04 where report_date = $S{reportDate} ", params);
    }

    public  UpdateResult addZG04(Object params) throws Exception {
        return super.update("INSERT INTO `app_pbc_report_zg04`(`PROD_CD`, `REPORT_DATE`, `CLC_SOURCE_ZON_CD`, `CLC_SOURCE_CUST_TYP`, `TRANS_CCY`, `CUR_PCH_AMT`, `CUR_PCH_AMT_CNY`, `CUR_PCH_LOT`, `CUR_CALL_AMT`, `CUR_CALL_AMT_CNY`, `CUR_CALL_LOT`, `END_PROD_AMT`, `END_PROD_AMT_CNY`, `END_PROD_LOT`, `NETVAL_PROD_END_NAV`, `NETVAL_PROD_END_NAV_CNY`, `NETVAL_PROD_END_ACM_NAV`, `NETVAL_PROD_END_ACM_NAV_CNY`, `PROD_END_ANL_YLD`, `REGISTER_STATUS`, `theory_report_start_date`, `theory_report_end_date`,month_end_anl_yld ) VALUES ($S{prodCd}, $S{reportDate}, $S{clcSourceZonCd}, $S{clcSourceCustTyp}, $S{transCcy}, if($S{curPchAmt}='',null,$S{curPchAmt}), if($S{curPchAmtCny}='',null,$S{curPchAmtCny}), if($S{curPchLot}='',null,$S{curPchLot}), if($S{curCallAmt}='',null,$S{curCallAmt}), if($S{curCallAmtCny}='',null,$S{curCallAmtCny}), if($S{curCallLot}='',null,$S{curCallLot}), if($S{endProdAmt}='',null,$S{endProdAmt}), if($S{endProdAmtCny}='',null,$S{endProdAmtCny}), if($S{endProdLot}='',null,$S{endProdLot}), if($S{netvalProdEndNav}='',null,$S{netvalProdEndNav}), if($S{netvalProdEndNavCny}='',null,$S{netvalProdEndNavCny}), if($S{netvalProdEndAcmNav}='',null,$S{netvalProdEndAcmNav}), if($S{netvalProdEndAcmNavCny}='',null,$S{netvalProdEndAcmNavCny}), if($S{prodEndAnlYld}='',null,$S{prodEndAnlYld}), $S{registerStatus}, $S{theoryReportStartDate}, $S{theoryReportEndDate},if($S{monthEndAnlYld}='',null,$S{monthEndAnlYld}))", params);
    }

    public List<SqlRow> addclcSourceZonCdDict(Map<String, Object> params) throws Exception {
        String sql = "SELECT itemkey VALUE, itemval TEXT  FROM sys_dict_item where 1 =1 ";
        if (StringUtils.isNotBlank(params.get("clcSourceCustTyp").toString())) {
            if(params.get("clcSourceCustTyp").toString().equals("1") || params.get("clcSourceCustTyp").toString().equals("2") || params.get("clcSourceCustTyp").toString().equals("3") || params.get("clcSourceCustTyp").toString().equals("4") || params.get("clcSourceCustTyp").toString().equals("5") ){
                sql = sql + " and dict  = 'pbc_prvc_area' ";
            }
            if(params.get("clcSourceCustTyp").toString().equals("6") ){
                sql = sql + " and dict  = 'pbc_country_code' ";
            }
        }else {
            sql = sql + " and dict  in ('pbc_prvc_area','pbc_country_code')  ";
        }
        if(params.containsKey("TEXT")){
            if(StringUtils.isNotBlank(params.get("TEXT").toString())) {
                sql = sql + " and (itemkey like '%$U{TEXT}%' or itemval like '%$U{TEXT}%')  ";
            }
        }
        return super.findRows(sql, DataSourceProperty.PUB,params);
    }
}
