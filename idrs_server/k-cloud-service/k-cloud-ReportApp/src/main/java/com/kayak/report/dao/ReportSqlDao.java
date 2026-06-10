package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.report.model.ReportSql;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ReportSqlDao extends ComnDao {

    public SqlResult<ReportSql> findReportSqls(SqlParam<ReportSql> params) throws Exception {
        return super.findRows("SELECT a.exeid,a.sqlid,a.report_sql FROM sys_report_sql a LEFT JOIN sys_report_xml_sql b ON ( a.exeid = b.exeid ) WHERE b.for_table = $S{forTable} ORDER BY a.exeid ASC", params);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addReportSql(SqlParam<ReportSql> params) throws Exception {
        String forTable = params.getModel().getForTable();
        super.update("DELETE FROM sys_report_sql s WHERE s.exeid IN (SELECT temp.exeid FROM (SELECT xs.exeid FROM sys_report_xml_sql xs WHERE xs.for_table=$S{for_table}) AS temp)", forTable);
        super.update("DELETE FROM sys_report_xml_sql WHERE for_table = $S{for_table}", forTable);
        if(StringUtils.isEmpty(params.getModel().getExeid())){
            return;
        }
        if (params.getModel().getExeid().indexOf("&&&") > 0) {
            String[] exeids = params.getModel().getExeid().split("&&&");
            String[] sqlids = params.getModel().getSqlid().split("&&&");
            String[] reportqls = params.getModel().getReportSql().split("&&&");
            for (int i = 0; i < exeids.length; i++) {
                this.setSqlValue(exeids[i], sqlids[i], reportqls[i], forTable, i + 1);
            }
        } else {
            String exeid = params.getModel().getExeid();
            String sqlid = params.getModel().getSqlid();
            String reportSql = params.getModel().getReportSql();
            this.setSqlValue(exeid, sqlid, reportSql, forTable, 1);
        }
    }

    public void setSqlValue(String exeid, String sqlid, String reportSql, String forTable, int i) throws Exception {
        String loginname = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_loginname"));
        Map<String, Object> params = new HashMap<>();
        params.put("exeid", exeid);
        params.put("sqlid", sqlid);
        params.put("reportSql", reportSql);
        params.put("forTable", forTable);
        params.put("ds", "ds" + i);
        params.put("inputuser", loginname);
        params.put("crtDate", Tools.dt2Date1(new Date()));
        params.put("crtTime", Tools.dt2Time1(new Date()));
        doTrans(() -> {
            super.update("INSERT INTO sys_report_xml_sql(id,for_table,ds,exeid,inputuser,crt_date,crt_time," +
                    "upd_date,upd_time) VALUES((SELECT temp.maxid FROM (SELECT ifnull( max( ifnull( id, 0 )), 0 )+ 1 " +
                    "AS maxid FROM sys_report_xml_sql ) AS temp),$S{forTable},$S{ds},$S{exeid},$S{inputuser}," +
                    "$S{crtDate},$S{crtTime},null,null)", params);

            super.update("INSERT INTO sys_report_sql(id,exeid,sqlid,report_sql,inputuser,crt_date,crt_time," +
                    "upd_date,upd_time) VALUES($AUTOIDI{id},$S{exeid},$S{sqlid},$S{reportSql},$S{inputuser},$S{crtDate}," +
                    "$S{crtTime},null,null)", params);
        });
    }

}
