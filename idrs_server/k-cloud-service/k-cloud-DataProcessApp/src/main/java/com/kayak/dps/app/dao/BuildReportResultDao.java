package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class BuildReportResultDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;

    /**
     * 获取报表配置计划数据
     * @param dealDate 处理日期（工作日）
     * @return
     * @throws Exception
     */
    public List<SqlRow> getReportConfigPlanData(String dealDate) throws Exception {
        return comnDao.findRows("select t1.report_type, t1.report_table, t1.report_table_name, t1.work_date, t1.base_line_date ,t1.start_date ,t1.end_date , t1.exec_status,t2.base_type,t2.data_type from report_statistical_exec_plan_info t1 left join base_submission_time_config t2 on t1.report_table = t2.report_table  where t1.work_date = $S{dealDate}", DataSourceProperty.PUB,dealDate);
    }

    /**
     * 根据工作日删除统计报表结果
     * @param dealDate
     * @throws Exception
     */
    public void deleteReportResultByDealDate(String dealDate) throws Exception {
        String sql = "delete from base_report_result_tmp where register_date = $S{dealDate} and status='2'";
        comnDao.update(sql, DataSourceProperty.PUB,dealDate);
    }

    public boolean isReportResult(Map<String, Object> params) throws Exception {
        List<SqlRow> list = comnDao.findRows("select report_table from base_report_result_tmp where register_date=$S{work_date} and report_table=$S{report_table} ", DataSourceProperty.PUB,params);
        if (list.size()>0) {
            return false;
        }
        return true;
    }

    public List<SqlRow> getReportNum(Map<String, Object> params) throws Exception {
        String sql ="";
        String baseType = params.get("base_type").toString();
        if("06".equals(baseType) || "07".equals(baseType) || "08".equals(baseType) || "09".equals(baseType)){
            sql = "select count(1) data_num from $U{report_table} where report_date=$S{base_line_date} and sys_data_status = '1'";
        }else{
            sql = "select count(1) data_num from $U{report_table} where theory_report_start_date=$S{start_date} and sys_data_status = '1'";
        }
        return comnDao.findRows(sql, DataSourceProperty.PUB,params);
    }

    public void addReportResultByDealDate(Map<String, Object> params) throws Exception {
        String sql = "insert into base_report_result_tmp(report_type,report_table,report_table_name,theory_report_start_date,theory_report_end_date,register_date" +
                ",total,report_success_number,status,create_date,create_time) " +
                "select " +
                " $S{report_type} " +
                ",$S{report_table} " +
                ",$S{report_table_name} " +
                ",$S{start_date} " +
                ",$S{end_date}" +
                ",$S{work_date} "+
                ",$S{data_num} " +
                ",'0' "+
                ",'2' " +
                ",date_format(now(),'%Y%m%d') as create_date " +
                ",date_format(now(),'%H%i%s') as create_time " +
                " from dual" ;
        comnDao.update(sql, DataSourceProperty.PUB,params);
    }
}
