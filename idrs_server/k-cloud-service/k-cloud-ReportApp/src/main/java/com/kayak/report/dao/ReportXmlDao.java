package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.report.model.ReportXml;
import org.springframework.stereotype.Repository;

@Repository
public class ReportXmlDao extends ComnDao {

    public SqlResult<ReportXml> findXmlInfo(SqlParam<ReportXml> params) throws Exception {
        return super.findRows("SELECT x.id,x.for_table,x.xml,s.exeid FROM sys_report_xml x LEFT JOIN " +
                "sys_report_xml_sql s ON x.for_table=s.for_table WHERE x.for_table = $S{forTable}", params);
    }

    public SqlResult<ReportXml> findTreeListTableSort(SqlParam<ReportXml> params) throws Exception {
        return super.findRows("SELECT for_table,table_sort FROM sys_report_xml WHERE for_table = $S{forTable}", params);
    }

    public SqlRow findReportXmls(SqlParam<ReportXml> params) throws Exception {
        return super.findRow("SELECT id,for_table,xml,table_sort FROM sys_report_xml WHERE for_table = $S{forTable}", params.getModel().getForTable());
    }


    public UpdateResult insertXml(SqlParam<ReportXml> params) throws Exception {
        return super.update("INSERT INTO sys_report_xml(id,for_table,xml,table_sort,inputuser,crt_date,crt_time," +
                        "upd_date,upd_time) VALUES($AUTOIDI{id},$S{forTable},$S{xml},$S{tableSort},$S{inputuser}," +
                        "$S{crtDate},$S{crtTime},null,null)",
                params.getModel());
    }

    public UpdateResult deleteReportXml(SqlParam<ReportXml> params) throws Exception {
        return super.update("DELETE FROM sys_report_xml WHERE  for_table = $S{forTable}",
                params.getModel());
    }

}
