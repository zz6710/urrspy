package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.report.model.ReportXmlSql;
import org.springframework.stereotype.Repository;

@Repository
public class ReportXmlSqlDao extends ComnDao {

    public SqlResult<ReportXmlSql> findXmlSqlInfo(SqlParam<ReportXmlSql> params) throws Exception {
        return super.findRows("SELECT for_table,ds,exeid FROM sys_report_xml_sql WHERE for_table = $S{forTable}", params);
    }

    public SqlRow findReportXmlSqls(SqlParam<ReportXmlSql> params) throws Exception {
        return super.findRow("SELECT for_table,exeid FROM sys_report_xml_sql WHERE for_table = $S{forTable}", params.getModel().getForTable());
    }

    public void insertXmlSql(SqlParam<ReportXmlSql> params) throws Exception {
        super.update("INSERT INTO sys_report_xml_sql(id,for_table,ds,exeid,inputuser,crt_date,crt_time," +
                "upd_date,upd_time) VALUES($AUTOIDI{id},$S{forTable},$S{ds},$S{exeid},$S{inputuser}," +
                "$S{crtDate},$S{crtTime},null,null)", params.getParams());
    }

    public UpdateResult deleteReportXmlSql(SqlParam<ReportXmlSql> params) throws Exception {
        return super.update("DELETE FROM sys_report_xml_sql WHERE for_table = $S{forTable}", params.getParams());
    }

}
