package com.kayak.core.util;

import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class ExeQuery {





    /***
     * 根据taskId获取sql语句
     * axin
     * @return
     * @throws Exception
     */
    public static List<SqlRow> queryPortSqlByTaskId(String taskId) throws Exception {
        DaoService daoService = SysBeans.getBean("daoService");
        try (AutoCloseable ca = daoService.selectDataSource(0)) {
            String str = "select t.exeid,t.sqlid,t.sqlstr from base_port_sql_info t where 1=1 and exe_order not like 'Z%' \t"+ "and t.task_id = '"+taskId+"' order by exe_order";
            List<SqlRow> sqlRows = daoService.list(SqlRow.class, str, null);
            return sqlRows;
        }


    }

    /***
     * 根据taskId获取合计项计算sql语句
     * axin
     * @return
     * @throws Exception
     */
    public static List<SqlRow> querySumPortSqlByTaskId(String taskId) throws Exception {
        DaoService daoService = SysBeans.getBean("daoService");
        try (AutoCloseable ca = daoService.selectDataSource(0)) {
            String str = "select t.exeid,t.sqlid,t.sqlstr from base_port_sql_info t where 1=1 and exe_order like 'Z%' \t"+ "and t.task_id = '"+taskId+"' order by exe_order";
            List<SqlRow> sqlRows = daoService.list(SqlRow.class, str, null);
            return sqlRows;
        }
    }


    /***
     * 根据exeId获取sql语句
     *
     * @return
     * @throws Exception
     */
    public static String queryExeId(String exeId) throws Exception {
        DaoService daoService = SysBeans.getBean("daoService");
        try (AutoCloseable ca = daoService.selectDataSource(0)) {
            String str = "select t.exeid,t.sqlid,t.sqlstr from base_port_sql_info t where 1=1 and t.exeid = '"+exeId+"'";
            List<SqlRow> sqlRows = daoService.list(SqlRow.class, str, null);
            String querySql ="";
            for (SqlRow sqlRow : sqlRows) {
                querySql = sqlRow.getString("sqlstr");
            }
            if(StringUtils.isEmpty(querySql)) throw new Exception("库中未找到["+exeId+"]对应的语句");
            return querySql;
        }

    }
    /***
     * 根据sqlId获取sql语句
     *
     * @return
     * @throws Exception
     */
    public static String querySqlId(String sqlId) throws Exception {
        DaoService daoService = SysBeans.getBean("daoService");
        try (AutoCloseable ca = daoService.selectDataSource(0)) {
            String str = "select t.exeid,t.sqlid,t.sqlstr from base_port_sql_info t where 1=1\t"+ "and t.sqlid = '"+sqlId+"'";
            List<SqlRow> sqlRows = daoService.list(SqlRow.class, str, null);
            String querySql ="";
            for (SqlRow sqlRow : sqlRows) {
                querySql = sqlRow.getString("sqlstr");
            }
            return querySql;
        }

    }

    /***
     * 根据exeId获取SqlRow对象
     *
     * @return
     * @throws Exception
     */
    public static SqlRow querySqlRowId(String exeId) throws Exception {
        DaoService daoService = SysBeans.getBean("daoService");
        try (AutoCloseable ca = daoService.selectDataSource(0)) {
            String str = "select t.exeid,t.sqlid,t.sqlstr from base_port_sql_info t where 1=1 and t.exeid = '"+exeId+"'";
            List<SqlRow> sqlRows = daoService.list(SqlRow.class, str, null);
            if(CollectionUtils.isEmpty(sqlRows)) throw new Exception("库中未找到["+exeId+"]对应的语句");
            return sqlRows.get(0);
        }

    }

    public static List<SqlRow> query(String sql) throws Exception {
        DaoService daoService = SysBeans.getBean("daoService");
        try (AutoCloseable ca = daoService.selectDataSource(0)) {
            return daoService.list(SqlRow.class, sql, null);
        }


    }

    /***
     * 根据report_table获取未校验的数据日期
     * axin
     * @return
     * @throws Exception
     */
    public static List<SqlRow> queryThdateByReportTable(String reportTable) throws Exception {
        DaoService daoService = SysBeans.getBean("daoService");
        try (AutoCloseable ca = daoService.selectDataSource(0)) {
            String str = "select distinct theory_report_start_date from  "+reportTable+" t where 1=1 and sys_data_status ='1' and register_status <>'3' ";
            return daoService.list(SqlRow.class, str, null);
        }
    }

}
