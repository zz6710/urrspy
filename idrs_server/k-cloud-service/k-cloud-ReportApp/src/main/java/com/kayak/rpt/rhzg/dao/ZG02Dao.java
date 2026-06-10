package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzg.model.ZG02;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ZG02Dao extends ComnDao {




    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG02> findZG02s(SqlParam<ZG02> params) throws Exception {
        String sql = "SELECT t.id,t.prod_cd,t.report_date,t.theory_report_start_date,t.theory_report_end_date,t.clc_ccy,t.clc_source_zon_cd,t.clc_source_cust_typ,t.clc_amt_begin," +
                "            t.clc_amt_begin_cny,t.clc_lot_begin,t1.clc_source_zon_cd_text,t.unt_nav,t.unt_nav_cny " +
                "  FROM app_pbc_report_zg02 t " +
                "  left join (select itemkey,CONCAT(itemkey,':',itemval) as clc_source_zon_cd_text " +
                "               from sys_dict_item " +
                "              where dict  in ('pbc_prvc_area','pbc_country_code')) t1 on t.clc_source_zon_cd = t1.itemkey " +
                " where sys_data_status ='1' " ;
        if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
            sql = sql + " and  t.prod_cd = '" + params.getModel().getProdCd() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
            sql = sql + " and  t.report_date >= '" + params.getModel().getBeginDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
            sql = sql + " and  t.report_date <= '" + params.getModel().getQueryDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getId())) {
            sql = sql + " and  t.id = '" + params.getModel().getId() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getClcSourceZonCd())) {
            sql = sql + " and  clc_source_zon_cd like '%" + params.getModel().getClcSourceZonCd() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getClcSourceCustTyp())) {
            sql = sql + " and  clc_source_cust_typ = '" + params.getModel().getClcSourceCustTyp() + "'";
        }
        sql=sql+" order by t.clc_ccy desc,t.prod_cd, t.clc_source_zon_cd,t.clc_source_cust_typ";
        return super.findRows(sql, params);
    }

    public UpdateResult updateZG02(SqlParam<ZG02> params) throws Exception {
        return super.update("UPDATE app_pbc_report_zg02 SET prod_cd=$S{prodCd},theory_report_start_date=$S{theoryReportStartDate},clc_ccy=$S{clcCcy},clc_source_zon_cd=$S{clcSourceZonCdText},clc_source_cust_typ=$S{clcSourceCustTyp},clc_amt_begin=if($S{clcAmtBegin}='',null,$S{clcAmtBegin}),clc_amt_begin_cny=if($S{clcAmtBeginCny}='',null,$S{clcAmtBeginCny}),clc_lot_begin=if($S{clcLotBegin}='',null,$S{clcLotBegin}),unt_nav=if($S{untNav}='',null,$S{untNav}),unt_nav_cny=if($S{untNavCny}='',null,$S{untNavCny})  WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG02(SqlParam<ZG02> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg02 WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZg02ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg02 where report_date = $S{reportDate}", params);
    }


    public UpdateResult addZg02(Object params) throws Exception {
        return super.update("INSERT INTO app_pbc_report_zg02(prod_cd,theory_report_start_date,register_status,clc_ccy,clc_source_zon_cd,clc_source_cust_typ,clc_amt_begin,clc_amt_begin_cny,clc_lot_begin,unt_nav,unt_nav_cny ) VALUES($S{prodCd},$S{theoryReportStartDate},$S{registerStatus},$S{clcCcy},$S{clcSourceZonCd},$S{clcSourceCustTyp},if($S{clcAmtBegin}='',null,$S{clcAmtBegin}),if($S{clcAmtBeginCny}='',null,$S{clcAmtBeginCny}),if($S{clcLotBegin}='',null,$S{clcLotBegin}),if($S{untNav}='',null,$S{untNav}),if($S{untNavCny}='',null,$S{untNavCny}))", params);
    }

    public List<SqlRow> addclcSourceZonCdDict(Map<String, Object> params) throws Exception {
        String sql = "select VALUE, TEXT from ( ";
        sql = sql + "select '000000' as VALUE,'全部地区' as TEXT from dual union all SELECT itemkey VALUE, itemval TEXT  FROM sys_dict_item where 1 =1 ";
        if (params.containsKey("clcSourceCustTyp") && StringUtils.isNotBlank(params.get("clcSourceCustTyp").toString())) {
            if(params.get("clcSourceCustTyp").toString().equals("1") || params.get("clcSourceCustTyp").toString().equals("2") || params.get("clcSourceCustTyp").toString().equals("3") || params.get("clcSourceCustTyp").toString().equals("4") || params.get("clcSourceCustTyp").toString().equals("5") ){
                sql = sql + " and dict  = 'pbc_prvc_area' ";
            }
            if(params.containsKey("TEXT") &&  params.get("clcSourceCustTyp").toString().equals("6") ){
                sql = sql + " and dict  = 'pbc_country_code' ";
            }
        }else {
            sql = sql + " and dict  in ('pbc_prvc_area','pbc_country_code')  ";
        }
        sql = sql + " ) t where 1=1 ";
        if(params.containsKey("TEXT") && StringUtils.isNotBlank(params.get("TEXT").toString())) {
            sql = sql + " and (VALUE like '%$U{TEXT}%' or TEXT like '%$U{TEXT}%')  ";
        }
        List<SqlRow> s = super.findRows(sql, DataSourceProperty.PUB,params);
        return s;
    }

}
