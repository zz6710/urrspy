package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.report.model.ReportCss;
import com.kayak.report.model.ReportXmlCss;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class ReportCssDao extends ComnDao {

    public SqlResult<ReportCss> findHtmlInfo(SqlParam<ReportCss> params) throws Exception {
        return super.findRows("SELECT b.id,b.css_id,b.css_name,b.css_type,b.css_class,b.label_discrible," +
                "b.crt_date,b.upd_time,b.upd_date,b.crt_time,b.inputuser,b.data_min_length,b.data_max_length," +
                "b.data_data,b.data_exeid,b.data_dict,b.data_value_field,b.data_display_field,b.data_workday," +
                "b.data_date_format,b.data_value,b.data_allowblank,b.data_min_value,b.data_max_value,b.ligroupid," +
                "b.validatetype,b.data_sqlinfo,data_on_change FROM sys_report_css b LEFT JOIN sys_report_xml_css a " +
                "ON b.id = a.sys_report_css_id WHERE a.for_table = $S{forTable} ORDER BY b.id,b.ligroupid", params);
    }

    public SqlResult<ReportCss> findReportCss(SqlParam<ReportCss> params) throws Exception {
        return super.findRows("SELECT id,css_id,css_name,css_type,css_class,datalist,data_on_change," +
                "label_discrible,data_min_length,data_max_length,data_data,data_exeid,data_dict,data_value_field," +
                "data_display_field,data_workday,data_date_format,data_value,data_allowblank,data_min_value," +
                "data_max_value,ligroupid,validatetype,data_sqlinfo,inputuser,crt_date,crt_time,upd_date,upd_time," +
                "data_define,remark,data_select_branch FROM sys_report_css WHERE id in (select a.sys_report_css_id " +
                "from sys_report_xml_css a WHERE a.for_table = $S{forTable})", params);
    }

    public SqlResult<ReportCss> findById(SqlParam<ReportCss> params) throws Exception {
        return super.findRows("SELECT a.for_table,b.css_name,b.label_discrible FROM sys_report_css b LEFT JOIN " +
                "sys_report_xml_css a ON b.id = a.sys_report_css_id WHERE a.for_table = $S{forTable}", params);
    }

    public void addReportCss(SqlParam<ReportCss> params) throws Exception {
        String forTable = params.getModel().getForTable();
        List<ReportCss> reportCssList = params.getModel().getReportCssList();
        String loginname = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_loginname"));
        String crtDate = Tools.dt2Date1(new Date());
        String crtTime = Tools.dt2Time1(new Date());
        List<SqlRow> sqlRow = super.findRows("SELECT sys_report_css_id FROM sys_report_xml_css WHERE for_table=$S{forTable}",forTable);
        String ids = Arrays.toString(sqlRow.stream().map(row -> "'" + row.get("sys_report_css_id") + "'").toArray());
        doTrans(() -> {
            super.update("DELETE FROM sys_report_xml_css WHERE for_table=$S{forTable}", forTable);
            if(!CollectionUtils.isEmpty(sqlRow)){
                super.update("DELETE FROM sys_report_css WHERE id IN (" + ids.substring(1, ids.length() - 1) + ")");
            }
            if (CollectionUtils.isEmpty(reportCssList)) {
                return;
            }
            for (ReportCss reportCss : reportCssList) {
                UpdateResult updateResult = super.update("INSERT INTO sys_report_css(id,css_id,css_name,css_type,css_class,datalist," +
                        "data_on_change,label_discrible,data_min_length,data_max_length,data_data," +
                        "data_exeid,data_dict,data_value_field,data_display_field,data_workday," +
                        "data_date_format,data_value,data_allowblank,data_min_value,data_max_value," +
                        "ligroupid,validatetype,data_sqlinfo,inputuser,crt_date,crt_time,upd_date,upd_time," +
                        "data_define,remark,data_select_branch) VALUES($AUTOIDI{id},$S{cssId},$S{cssName}," +
                        "$S{cssType},$S{cssClass},$S{datalist},$S{dataOnChange},$S{labelDiscrible}," +
                        "$S{dataMinLength},$S{dataMaxLength},$S{dataData},$S{dataExeid},$S{dataDict}," +
                        "$S{dataValueField},$S{dataDisplayField},$S{dataWorkday},$S{dataDateFormat}," +
                        "$S{dataValue},$S{dataAllowblank},$S{dataMinValue},$S{dataMaxValue},$S{ligroupid}," +
                        "$S{validatetype},$S{dataSqlinfo},'" + loginname + "','" + crtDate +
                        "','" + crtTime + "',null,null,$S{dataDefine},$S{remark}," +
                        "$S{dataSelectBranch})", reportCss);
                ReportXmlCss reportXmlCss = new ReportXmlCss();
                reportXmlCss.setSysReportCssId(updateResult.getAutoId());
                reportXmlCss.setForTable(forTable);
                reportXmlCss.setInputuser(loginname);
                reportXmlCss.setCrtDate(crtDate);
                reportXmlCss.setCrtTime(crtTime);
                super.update("INSERT INTO sys_report_xml_css(id,sys_report_css_id,for_table,inputuser,crt_date," +
                                "crt_time,upd_date,upd_time) VALUES($AUTOIDI{id},$I{sysReportCssId},$S{forTable}," +
                                "$S{inputuser},$S{crtDate},$S{crtTime},null,null)",
                        reportXmlCss);
            }
        });
    }
}
