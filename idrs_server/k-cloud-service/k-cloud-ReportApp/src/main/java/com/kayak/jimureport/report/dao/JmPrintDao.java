package com.kayak.jimureport.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository(value = "jmPrintDao")
public class JmPrintDao extends ComnDao {

    /**
     * 查询积木报表SQL
     */
    public List<SqlRow> findJmReportSql(Map<String, Object> params) throws Exception {
        return super.findRows("select a.DEFAULTNAME,a.DEFAULTVALUE " +
                " from T8_DEFAULT_PRINTPARAM a " +
                " left join T8_JMPRINT_SQL b on a.id=b.id " +
                " where b.menuid=$S{menuid} ", params);
    }


    /**
     * 删除积木报表SQL
     */
    public void deleteJmPrintSql(Map<String, Object> params) throws Exception {
        super.update("delete from T8_JMPRINT_SQL where menuid = $S{menuid}", params);
    }

    /**
     * 更新积木报表SQL
     */
    public void updateJmPrintSql(Map<String, Object> params) throws Exception {
        super.update("update T8_JMPRINT_SQL set "
                + " sql = $S{sql}, upttime = TO_CHAR(SYSDATE,'yyyyMMdd HH:MI:ss') " +
                " where menuid = $S{menuid}", params);
    }

    /**
     * 插入积木报表SQL
     */
    public void insertJmPrintSql(Map<String, Object> params) throws Exception {
        super.update("insert into T8_JMPRINT_SQL (id,sql,menuid,sqltype,upttime,count) values"
                + "($S{id},$S{sql},$S{menuid},'1',TO_CHAR(SYSDATE,'yyyyMMdd HH:Mi:ss'),$S{count})", params);
    }


    public List<SqlRow> getJmPrintSql(Map<String, Object> params) throws Exception {
        return super.findRows("select SQL from T8_JMPRINT_SQL where menuid=$S{menuid}", params);
    }

    public SqlRow getJmPrintCount(Map<String, Object> params) throws Exception {
        return super.findRow("SELECT DISTINCT COUNT FROM T8_JMPRINT_SQL where id=$S{id}", params);
    }

    public List<SqlRow> sqlQuery(String sql, Map<String, Object> params) throws Exception {
        return super.findRows(sql, params);
    }


}
