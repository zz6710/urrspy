package com.kayak.rpt.rhzj.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzj.model.ReportPIB;
import org.springframework.stereotype.Repository;

@Repository
public class ReportPIBDao extends ComnDao {

    public SqlResult<ReportPIB> findReportPIBs(SqlParam<ReportPIB> params) throws Exception {
        return super.findRows("SELECT id,peoplebank_submitcode,report_date,prod_code,area_code,cust_type,cny,init_amount,init_amount_rmb,init_vol FROM app_rpt_pib", params);
    }

    public UpdateResult addReportPIB(SqlParam<ReportPIB> params) throws Exception {
        return super.update("INSERT INTO app_rpt_pib(id,peoplebank_submitcode,report_date,prod_code,area_code,cust_type,cny,init_amount,init_amount_rmb,init_vol) VALUES($AUTOIDI{id},$S{peoplebankSubmitcode},$S{reportDate},$S{prodCode},$S{areaCode},$S{custType},$S{cny},if($S{initAmount}='',null,$S{initAmount}),if($S{initAmountRmb}='',null,$S{initAmountRmb}),if($S{initVol}='',null,$S{initVol}))",
                params.getModel());
    }
    public UpdateResult addReportPIB(Object params) throws Exception {
        return super.update("INSERT INTO app_rpt_pib(id,peoplebank_submitcode,report_date,prod_code,area_code,cust_type,cny,init_amount,init_amount_rmb,init_vol) VALUES($AUTOIDI{id},$S{peoplebankSubmitcode},$S{reportDate},$S{prodCode},$S{areaCode},$S{custType},$S{cny},if($S{initAmount}='',null,$S{initAmount}),if($S{initAmountRmb}='',null,$S{initAmountRmb}),if($S{initVol}='',null,$S{initVol}))",
                params);
    }

    public UpdateResult updateReportPIB(SqlParam<ReportPIB> params) throws Exception {
        return super.update("UPDATE app_rpt_pib SET peoplebank_submitcode=$S{peoplebankSubmitcode} ,report_date=$S{reportDate} ,prod_code=$S{prodCode} ,area_code=$S{areaCode} ,cust_type=$S{custType} ,cny=$S{cny} ,init_amount=if($S{initAmount}='',null,$S{initAmount}) ,init_amount_rmb=if($S{initAmountRmb}='',null,$S{initAmountRmb}) ,init_vol=if($S{initVol}='',null,$S{initVol})  WHERE  id=$S{id} ",
                params.getModel());
    }

    public UpdateResult deleteReportPIB(SqlParam<ReportPIB> params) throws Exception {
        return super.update("DELETE FROM app_rpt_pib WHERE  id=$S{id} ",
                params.getModel());
    }

    public UpdateResult deleteReportPIBByReportDate(Object params) throws Exception {
        return super.update("DELETE FROM app_rpt_pib where report_date between $S{beginDate} and $S{queryDate}",
                params);
    }

    public SqlResult<ReportPIB> findReportPIBsByReportDate(SqlParam<ReportPIB> params) throws Exception {
        return super.findRows("select id,report_date,peoplebank_submitcode,prod_code,area_code,cust_type,cny,init_amount,init_amount_rmb,init_vol from app_rpt_pib where  report_date = $S{queryDate}", params);
    }
}
