package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.report.model.ReportCondition;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ReportConditionDao extends ComnDao {

    public SqlResult<ReportCondition> findReportMenu(SqlParam<ReportCondition> params) throws Exception {
        return super.findRows(
                "SELECT a.menuid,a.menuname,a.upperid,a.url,a.loadorder,a.icon,a.`status`,a.pageid,a.reporturl," +
                        "b.REPORT_NAME,b.FOR_TABLE,b.OBJ_TYPE,b.IS_REPORT,b.BUTTON_IDS,b.INPUTUSER,b.CRT_DATE,b.CRT_TIME," +
                        "b.UPD_DATE,b.UPD_TIME,b.INIT_SQL, js.jimu_report_id FROM sys_menu AS a LEFT JOIN ( sys_report_condition AS b )" +
                        "ON a.menuid = b.FOR_TABLE LEFT JOIN JIMU_SQLDICT js on js.menuid = a.menuid WHERE b.IS_REPORT = 0",
                params);
    }

    public void addReportMenu(SqlParam<ReportCondition> params) throws Exception {
        doTrans(() -> {
            this.addReportCondition(params);
            this.addJimuReportInfo(params);
            super.update("INSERT INTO sys_menu(moduleid, menuid, menuname, shortname, `model`, upperid, url, " +
                    "iconcls, loadorder, icon, `status`, remark, auth_server, pageid, reporturl) VALUES ('8', " +
                    "$S{menuid}, $S{menuname}, '', NULL, $S{upperid}, $S{url}, NULL, $S{loadorder}, $S{icon}, " +
                    "$S{status}, NULL, NULL, $S{pageid}, $S{reporturl})", params.getModel());
        });
    }

    public void updateReportMenu(SqlParam<ReportCondition> params) throws Exception {
        doTrans(() -> {
            super.update("UPDATE sys_menu SET menuname=$S{menuname},upperid=$S{upperid},url=$S{url},icon=$S{icon}," +
                            "loadorder=$S{loadorder},pageid=$S{pageid},reporturl=$S{reporturl},`status`=$S{status} where menuid=$S{menuid}",
                    params.getModel());
            this.updateJimuReportInfo(params);
            this.deleteReportCondition(params);
            this.addReportCondition(params);
        });
    }

    public Boolean checkPrimary(SqlParam<ReportCondition> params) throws Exception {
        return super.findRows("SELECT menuid FROM sys_menu WHERE menuid = $S{menuid}", params).getRows().size() > 0;
    }

    public SqlResult<ReportCondition> findReportConditions(SqlParam<ReportCondition> params) throws Exception {
        return super.findRows("SELECT id,report_name,for_table,obj_type,is_report,button_ids,inputuser,crt_date," +
                "crt_time,upd_date,upd_time,init_sql FROM sys_report_condition", params);
    }

    public UpdateResult addReportCondition(SqlParam<ReportCondition> params) throws Exception {
        String loginname = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_loginname"));
        return super.update("INSERT INTO sys_report_condition(id,report_name,for_table,obj_type,inputuser," +
                        "crt_date,crt_time,upd_date,upd_time) VALUES($AUTOIDI{id},$S{menuname},$S{menuid}," +
                        "$S{objType},'" + loginname + "','" + Tools.dt2Date1(new Date()) + "','" +
                        Tools.dt2Time1(new Date()) + "',null,null)",
                params.getModel());
    }

    public UpdateResult deleteReportCondition(SqlParam<ReportCondition> params) throws Exception {
        return super.update("DELETE FROM sys_report_condition WHERE for_table=$S{menuid}",
                params.getModel());
    }

    public void addJimuReportInfo(SqlParam<ReportCondition> params) throws Exception {
        super.update("INSERT INTO jimu_sqldict (ID, REPORT_SQL, MENUID, UPTTIME, SQLTYPE, JIMU_REPORT_ID) " +
                " VALUES($S{menuid}, $S{menuid}, $S{menuid}, date_format(now(), '%Y%m%d'), 1, $S{jimuReportId})", params.getModel());
    }

    public void updateJimuReportInfo(SqlParam<ReportCondition> params) throws Exception {
        super.update("update jimu_sqldict set JIMU_REPORT_ID = $S{jimuReportId} where MENUID = $S{menuid}", params.getModel());
    }
}
