package com.kayak.rpt.zz.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.audit.model.ReportAuditResult;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ty
 * @since 2023-05-18 11:24:54
 */
@Repository
public class ReportAuditResultDao extends ComnDao {

    public UpdateResult updateAuditResult(SqlParam<ReportAuditResult> params) throws Exception {
        Map<String, Object> param = params.getParams();
        String tableId = (String) param.get("tableId");
        String startDate = (String) param.get("startDate");
        String endDate = (String) param.get("endDate");
        log.info("---------------------------------------------");
        log.info("表名：{}，起始时间：{}，截止时间：{}", tableId, startDate, endDate);

        return null;
    }

    public UpdateResult updateAuditResult(ReportAuditResult reportAuditResult) throws Exception {
        String tableId = reportAuditResult.getTableId();
        String startDate = reportAuditResult.getStartDate();
        String endDate = reportAuditResult.getEndDate();
        String auditStatus = reportAuditResult.getAuditStatus();
        String table_name=getReportName(tableId);
        String date_type=getReportType(tableId);
        String audit_date = DateUtil.getSysWordDay();//系统工作日
        String delSql = "DELETE FROM base_report_data_audit_results WHERE table_id = $S{tableId} AND report_date= $S{reportDate}";
        String Sql = "INSERT base_report_data_audit(table_id,table_name,begin_date,end_date,audit_date,date_type,audit_status) VALUES($S{tableId},$S{tableName},$S{startDate},$S{endDate},$S{auditDate},$S{dateType},$S{auditStatus})";
        String insSql = "INSERT base_report_data_audit_results(table_id,report_date,audit_date,audit_status) VALUES($S{tableId},$S{reportDate},$S{auditDate},$S{auditStatus})";
        log.info("---------------------------------------------");
        log.info("表名：{}，起始时间：{}，截止时间：{}，复核状态：{}", tableId, startDate, endDate, auditStatus);
        List<String> dates = getDataDates(tableId,date_type,startDate, endDate);
        reportAuditResult.setAuditDate(audit_date);
        reportAuditResult.setTableName(table_name);
        reportAuditResult.setDateType(date_type);
        AtomicReference<UpdateResult> updateResult = new AtomicReference<>(new UpdateResult());
        super.update(Sql, DataSourceProperty.PUB, reportAuditResult);
        dates.forEach(date -> {
            reportAuditResult.setReportDate(date);
            try {
                updateResult.set(super.update(delSql, DataSourceProperty.PUB, reportAuditResult));
                updateResult.set(super.update(insSql, DataSourceProperty.PUB, reportAuditResult));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return updateResult.get();
    }

    public int getIndexStatus(ReportAuditResult reportAuditResult) throws Exception {
        String tableId = reportAuditResult.getTableId();
        String startDate = reportAuditResult.getStartDate();
        String endDate = reportAuditResult.getEndDate();
        String date_type=getReportType(reportAuditResult.getTableId());
        List<String> dates = getDataDates(tableId,date_type,startDate, endDate);
        if(dates.size()>0){
            reportAuditResult.setStartDate(dates.get(0));
            reportAuditResult.setEndDate(dates.get(dates.size()-1));
        }
        String Sql = "select count(1) cnt from base_data_validation where deal_date>=$S{startDate} and deal_date<=$S{endDate} and validate_table = $S{tableId} and validate_result=-1 ";
        SqlRow sqlRow = super.findRow(Sql, DataSourceProperty.PUB, reportAuditResult);
        if(sqlRow.getInteger("cnt")>0){
            return sqlRow.getInteger("cnt");
        }
        String tql = "";
        List<SqlRow> sqlRows = new ArrayList<>();
        if("app_cust_trans_info".equals(tableId) || "app_cust_vol_register_info".equals(tableId)){
            String sys_date =  SysUtil.getSystemParamsByParaid("10004");
            for(String dataDate : dates){
                if(dataDate.equals(sys_date)){
                    tql = "select * from "+tableId+" where theory_report_start_date='"+dataDate+"' and sys_data_status='1' and register_status='0' limit 1 ";
                    sqlRows = super.findRows(tql, DataSourceProperty.PUB, reportAuditResult);
                    if(sqlRows.size()>0)
                        return sqlRows.size();
                }else{
                    tql="select * from "+tableId+"_"+dataDate+" where theory_report_start_date='"+dataDate+"' and sys_data_status='1' and register_status='0' limit 1 ";
                    sqlRows = super.findRows(tql, DataSourceProperty.PUB, reportAuditResult);
                    if(sqlRows.size()>0)
                        return sqlRows.size();
                }
            }
        }else{
            tql = "select * from "+tableId+" where theory_report_start_date>=$S{startDate} and theory_report_start_date<=$S{endDate} and sys_data_status='1' and register_status='0' limit 1 ";
            sqlRows = super.findRows(tql, DataSourceProperty.PUB, reportAuditResult);
            return sqlRows.size();
        }
        return sqlRows.size();
    }

    public int getCheckTable(ReportAuditResult reportAuditResult) throws Exception {
        String tableId = reportAuditResult.getTableId();
        String sql="select distinct ident_code from "+tableId+" where sys_data_status='1' and theory_report_start_date>=$S{startDate} and theory_report_start_date<=$S{endDate}";
       List<SqlRow> list = super.findRows(sql, DataSourceProperty.PUB, reportAuditResult);
        for(SqlRow sqlRow : list){
            String tql="select count(1) cnt from base_report_file_manage where prod_cd='"+sqlRow.getString("ident_code")+"' and (prod_nm_fu is null or prod_nm_fu!='') and file_type='2' ";
            if(super.findRow(tql,DataSourceProperty.PUB).getInteger("cnt")==0)
                return 1;
        }
        return 0;
    }


    /**
     * 获取两个八位数字符串日期之间的所有日期
     *
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    private List<String> getDataDates(String table_id,String date_type,String startDateStr, String endDateStr) throws Exception {
        String sql="";
        List<String> dates = new ArrayList<>();
        if("1".equals(date_type)){
            sql="select distinct theory_report_start_date from "+table_id+" where sys_data_status='1' and report_date>='"+startDateStr+"'and report_date<='"+endDateStr+"' order by theory_report_start_date";
            List<SqlRow> list = super.findRows(sql,DataSourceProperty.PUB);
            for(SqlRow sqlRow : list){
                dates.add(sqlRow.getString("theory_report_start_date"));
            }
        }else{
            dates.addAll(DateUtil.getDateListDay(startDateStr,endDateStr,"1"));
            dates.add(endDateStr);
        }


        return dates;
    }

    /**
     * 获取表名
     * @return
     */
    private String getReportName(String table_id) throws Exception {
       String table_name="";
       String sqlk="select itemval from sys_dict_item where dict='report_data_table' and itemkey='"+table_id+"'";
       List<SqlRow> list = super.findRows(sqlk,DataSourceProperty.PUB);
       if(list.size()>0){
           table_name=list.get(0).get("itemval").toString();
       }
       return table_name;
    }

    /**
     * 获取表类型
     * @return
     */
    private String getReportType(String table_id) throws Exception {
        String date_type="";
        String sqlk="select itemorder from sys_dict_item where dict='report_data_table' and itemkey='"+table_id+"'";
        List<SqlRow> list = super.findRows(sqlk,DataSourceProperty.PUB);
        if(list.size()>0){
            date_type=list.get(0).get("itemorder").toString();
        }
        return date_type;
    }
}