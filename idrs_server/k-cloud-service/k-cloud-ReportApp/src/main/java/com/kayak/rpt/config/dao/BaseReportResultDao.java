package com.kayak.rpt.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.config.model.BaseReportResultModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BaseReportResultDao extends ComnDao {

    /**
     * 报送任务管理查询
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<BaseReportResultModel> findBaseReportResultInfo(SqlParam<BaseReportResultModel> params) throws Exception {
        String sql="select id, report_type,report_table,report_table_name,prod_reg_enc,theory_report_start_date,theory_report_end_date,register_date,total," +
                   "report_success_number,status,create_date,create_time,update_date,update_time from base_report_result order by report_type asc ";
        return super.findRows(sql, params);
    }

    public UpdateResult updateBaseReportResultInfo(SqlParam<BaseReportResultModel> params) throws Exception {
        String sql="update base_report_result set register_date = $S{registerDate},report_success_number=$S{reportSuccessNumber},status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = $S{reportTable} and theory_report_start_date=$S{theoryReportStartDate} ";
        if(StringUtils.isNotEmpty(params.getModel().getProdRegEnc())){
            sql = sql+" and prod_reg_enc=$S{prodRegEnc}";
        }
        return super.update(sql,DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult updateAppDataInfo(SqlParam<BaseReportResultModel> params) throws Exception {
        String sql = "update $U{reportTable} set register_date = $S{registerDate},register_status= '3' where theory_report_start_date=$S{theoryReportStartDate} and theory_report_end_date=$S{theoryReportEndDate}";
        if(StringUtils.isNotEmpty(params.getModel().getProdRegEnc())){
            sql = sql+" and ident_code=$S{prodRegEnc}";
        }
        return super.update(sql,DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult cancelBaseReportResultInfo(SqlParam<BaseReportResultModel> params) throws Exception {
        String sql="update base_report_result set register_date='',report_success_number=0,status= '2',register_status= '0',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = $S{reportTable} and theory_report_start_date=$S{theoryReportStartDate} and theory_report_end_date=$S{theoryReportEndDate} ";
        if(StringUtils.isNotEmpty(params.getModel().getProdRegEnc())){
            sql = sql+" and prod_reg_enc=$S{prodRegEnc}";
        }
        return super.update(sql,DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult cancelAppDataInfo(SqlParam<BaseReportResultModel> params) throws Exception {
        String sql="update $U{reportTable} set register_date='',register_status='0' where theory_report_start_date=$S{theoryReportStartDate} and theory_report_end_date=$S{theoryReportEndDate} ";
        if(StringUtils.isNotEmpty(params.getModel().getProdRegEnc())){
            sql = sql+" and ident_code=$S{prodRegEnc}";
        }
        return super.update(sql,DataSourceProperty.PUB, params.getModel());
    }
}
