package com.kayak.jimureport.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.jimureport.report.entity.JmReportMenuInfo;
import com.kayak.jimureport.report.entity.JmSqlDictInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository(value = "jmReportDao")
public class JmReportDao extends ComnDao {

    /**
     * 查询数据库表信息
     */
    public List<SqlRow> selectTableInfo(Map<String, String> params) throws Exception {
        return super.findRows("select TABLE_NAME, COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH DATA_LENGTH " +
                " from information_schema.columns where table_name = $S{tableName} ", params);
    }

    public List<SqlRow> selectSqlInfos(String sql, Map<String, Object> params) throws Exception {
        return super.findRows(sql, params);
    }

    public List<SqlRow> selectSqlInfos(String sql) throws Exception {
        return super.findRows(sql);
    }

    public SqlRow selectSqlInfo(String sql, Map<String, Object> params) throws Exception {
        return super.findRow(sql, params);
    }

    public SqlRow selectMenuName(Map<String, Object> params) throws Exception {
        return super.findRow("select menuname from sys_menu where menuid = $S{menuid} ", params);
    }

    public SqlRow selectMenuInfo(Map<String, Object> params) throws Exception {
        return super.findRow("select * from sys_menu where menuid = $S{menuid} ", params);
    }

    public SqlRow selectRoleRight(Map<String, Object> params) throws Exception {
        return super.findRow("select * from sys_role_right m where m.menuid = $S{menuid} and roleid='*' ", params);
    }

    public void deleteTableInfo(String sql) throws Exception {
        super.update(sql);
    }

    public void update(String sql, Map<String, Object> params) throws Exception {
        super.update(sql, params);
    }

    public List<SqlRow> selectSQLDICTCOUNT(Map<String, Object> params) throws Exception {
        return super.findRows("SELECT DISTINCT jimu_report_id FROM JIMU_SQLDICT where id=$S{id}", params);
    }

    public List<SqlRow> findJmDefaultInfo(Map<String, Object> params) throws Exception {
        return super.findRows("select a.DEFAULTNAME,a.DEFAULTVALUE from T8_DEFAULT_PARAM a " +
                " left join JIMU_SQLDICT b on a.id = b.id where b.menuid = $S{id} ", params);
    }

    public SqlRow selectQuerySql(Map<String, Object> params) throws Exception {
        return super.findRow("select query_sql from T8_QUERY_SQL where MENUID=$S{menuid} ", params);
    }

    public SqlRow selectReportSql(Map<String, Object> params) throws Exception {
        return super.findRow("select report_sql from JIMU_SQLDICT where MENUID=$S{id} ", params);
    }

    public SqlRow selectmenu(Map<String, Object> params) throws Exception {
        return super.findRow(" select b.MENUNAME from SYS_MENU b where b.menuid=$S{menuid} ", params);
    }

    public SqlResult<JmReportMenuInfo> selectReportMenuInfo(SqlParam<JmReportMenuInfo> params) throws Exception {
        return super.findRows("SELECT DISTINCT a.MENUID,a.UPPERID,a.MENUNAME ,c.jimu_report_id " +
                "        FROM SYS_MENU a " +
                "        LEFT JOIN JIMU_SQLDICT c ON c.ID=a.MENUID " +
                "        WHERE a.URL='page/M8/jmreport/M860004.html' " +
                "        and a.MODULEID='8'", params);
    }

    public SqlResult<JmSqlDictInfo> selectJmSqlDictInfo(SqlParam<JmSqlDictInfo> params) throws Exception {
        return super.findRows("select sm.menuid, sm.menuname, js.jimu_report_id, js.upttime, js.report_sql from SYS_MENU sm " +
                "        left join JIMU_SQLDICT js on sm.MENUID = js.MENUID " +
                "        where sm.MENUID = $S{menuid} ", params);
    }

}
