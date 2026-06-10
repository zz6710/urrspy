package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.config.constants.DisplayBatchTaskStatus;
import com.kayak.config.constants.GlobalConstants;
import com.kayak.config.model.Ta5014;
import com.kayak.config.model.Ta5015GroupList;
import com.kayak.core.sql.Sql;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * @author: lfzh
 * @date: 2020-12-24 15:39
 */
@Repository
public class Ta5014Dao extends ComnDao {

    public static String ORACLE = "oracle";
    public static String MYSQL = "mysql";

    /**
     * 查询清算任务组
     *
     * @param params
     * @return
     */
    public SqlResult<Ta5014> queryClearGroups(SqlParam<Ta5014> params) throws Exception {
        String sql1Oracle = "SELECT t.*, NVL( s1.NON_EXECUTION, 0 ) as NON_EXECUTION,NVL( s5.NON_REGISTRY, 0 ) as NON_REGISTRY, NVL( s2.EXECUTING, 0 ) as EXECUTING,NVL( s3.SUCCESS, 0 ) AS SUCCESS,NVL( s4.FAILED, 0 ) AS FAILED FROM ( ";
        String sql2Oracle = "SELECT DISTINCT" +
                " a.task_group," +
                "a.SHOULD_EXEC_TIME," +
                "gi.LAST_TASK_GROUP," +
                "gi.PRE_TASK_GROUP," +
                "gi.RUNNING_TYPE," +
                "gi.task_group_name," +
                "gi.exec_task_type," +
                "'0' AS button_is_display" +
                " FROM " +
                " kbatch_task_exec_display a " +
                " RIGHT JOIN kbatch_group_info gi ON a.task_group = gi.task_group " +
                " WHERE " +
                " a.moduleid = '"+ GlobalConstants.MODULEID+"' " +
                " AND a.is_exec = '1' " +
                "AND gi.should_exec_time != '999999'";
        String sql3Oracle = " UNION ALL" +
                " SELECT" +
                " gi.task_group," +
                " gi.SHOULD_EXEC_TIME," +
                " gi.LAST_TASK_GROUP," +
                " gi.PRE_TASK_GROUP," +
                " '' as RUNNING_TYPE," +
                " gi.task_group_name," +
                " gi.exec_task_type," +
                " '0' AS button_is_display" +
                " FROM" +
                " kbatch_group_info gi " +
                " WHERE" +
                " should_exec_time = '999999'";
        String sql1Mysql = "SELECT t.*, IFNULL( s1.NON_EXECUTION, 0 ) as NON_EXECUTION,IFNULL( s5.NON_REGISTRY, 0 ) as NON_REGISTRY, IFNULL( s2.EXECUTING, 0 ) as EXECUTING,IFNULL( s3.SUCCESS, 0 ) AS SUCCESS,IFNULL( s4.FAILED, 0 ) AS FAILED FROM ( ";
        String sql2Mysql = "SELECT DISTINCT" +
                " a.task_group," +
                "a.SHOULD_EXEC_TIME," +
                "gi.ROW_NUM,"+
                "gi.LAST_TASK_GROUP," +
                "gi.PRE_TASK_GROUP," +
                "gi.RUNNING_TYPE," +
                "gi.task_group_name," +
                "gi.exec_task_type," +
                "'0' AS button_is_display" +
                " FROM " +
                " kbatch_task_exec_display a " +
                " RIGHT JOIN (select t.*,row_number() over (order by SHOULD_EXEC_TIME asc ) as ROW_NUM from kbatch_group_info t ) gi ON a.task_group = gi.task_group " +
                " WHERE " +
                " a.moduleid = '"+ GlobalConstants.MODULEID+"' " +
                " AND a.is_exec = '1' " +
                "AND gi.should_exec_time != '999999'";
        String sql3Mysql = " UNION ALL" +
                " SELECT" +
                " gi.task_group," +
                " gi.SHOULD_EXEC_TIME," +
                " '' as ROW_NUM,"+
                " gi.LAST_TASK_GROUP," +
                " gi.PRE_TASK_GROUP," +
                " '' as RUNNING_TYPE," +
                " gi.task_group_name," +
                " gi.exec_task_type," +
                " '0' AS button_is_display" +
                " FROM" +
                " kbatch_group_info gi " +
                " WHERE" +
                " should_exec_time = '999999'";
        String sqlOracle = sql1Oracle + sql2Oracle + getSqlCondition(params.getModel(),ORACLE) + sql3Oracle + getExecStatus() + " ORDER BY t.SHOULD_EXEC_TIME ASC";
        String sqlMysql  = sql1Mysql + sql2Mysql + getSqlCondition(params.getModel(),MYSQL) + sql3Mysql + getExecStatus() + " ORDER BY t.SHOULD_EXEC_TIME,t.ROW_NUM ASC";
        Sql    sql       = Sql.build().oracleSql(sqlOracle).db2Sql(sqlOracle).mysqlSql(sqlMysql);
        return super.findRows(sql, params);
    }

    /**
     * 方法描述:根据入参生成查询条件
     *
     * @param model
     * @return
     */
    private String getSqlCondition(Ta5014 model,String dbType) {

        //查询条件
        String condition = "";
        //查询清算执行日期
        String queryTaskDate = model.getQueryTaskDate();
        //分组ID
        String taskGroup = model.getTaskGroup();

        //清算批次名称
        String taskGroupName = model.getTaskGroupName();

        // String simpleFlow = model.getSimpleFlow();

        if (!Tools.strIsEmpty(queryTaskDate)) {
            //查询的清算开始日期不为空
            condition = condition + " AND a.task_date = $S{queryTaskDate} ";
        }

        if (!Tools.strIsEmpty(taskGroup)) {
            //清算批次不为空
            condition = condition + " AND a.task_group = $S{taskGroup}  ";
        }

        if (!Tools.strIsEmpty(taskGroupName)) {
            if(ORACLE.equals(dbType)){
                //清算批次名称不为空
                condition = condition + " AND gi.task_group_name LIKE  '%' || $S{taskGroupName} || '%' ";
            }else{
                //清算批次名称不为空
                condition = condition + " AND gi.task_group_name LIKE  CONCAT('%',$S{taskGroupName},'%')";

            }
        }

//        if (!Tools.strIsEmpty(simpleFlow)) {
//            //
//            condition = condition + " AND b.simple_flow = '" + simpleFlow + "' ";
//        }

        return condition;
    }

    /**
     * 方法描述: 只查询展示表或执行表时，拼接状态条件
     *
     * @param model
     * @return
     */
    private String getExecCondition(Ta5014 model) {

        //执行状态
        String execStatus = "model.getExecStatus()";
        String condition  = "";

        if (execStatus != null && !"".equals(execStatus)) {
            switch (execStatus) {
                case DisplayBatchTaskStatus.NON_EXECUTION:
                    //未执行
                    condition = condition + " AND a.exec_status IN ('0','Z')";
                    break;
                case DisplayBatchTaskStatus.EXECUTION:
                    //执行中
                    condition = condition
                            + " AND a.exec_status IN ('1', '2', '3', '4') ";
                    break;
                case DisplayBatchTaskStatus.SUCCESS:
                    //执行成功
                    condition = condition
                            + " AND a.exec_status IN ('5', '7', '8') ";
                    break;
                case DisplayBatchTaskStatus.ROLL_BACK:
                    //回滚中
                    condition = condition + " AND a.exec_status IN ('R') ";
                    break;

                default:
                    //失败
                    condition = condition + " AND a.exec_status in ('6','9') ";
                    break;
            }
        }
        return condition;

    }

    /**
     * 方法描述:展示表和执行表都要查询时，拼接状态条件
     *
     *
     * @return
     */
    private String getExecStatus() {
        String sql = " ) t " +
                " LEFT JOIN ( SELECT count( 1 ) AS NON_EXECUTION, TASK_GROUP FROM kbatch_task_exec WHERE EXEC_STATUS IN ( '0', 'Z' ) AND TASK_DATE = $S{queryTaskDate} GROUP BY TASK_GROUP ) s1 ON t.TASK_GROUP = s1.TASK_GROUP " +
                " LEFT JOIN ( " +
                "  SELECT count( 1 ) AS EXECUTING , " +
                "  TASK_GROUP FROM kbatch_task_exec WHERE EXEC_STATUS IN ( '1', '2', '3', '4', 'R' )  " +
                "  AND TASK_DATE = $S{queryTaskDate} GROUP BY TASK_GROUP  " +
                " ) s2 ON t.TASK_GROUP = s2.TASK_GROUP " +
                " LEFT JOIN ( SELECT count(1) success,task_group FROM (SELECT  TASK_GROUP FROM kbatch_task_exec WHERE EXEC_STATUS IN ( '5', '7', '8' ) AND TASK_DATE = $S{queryTaskDate} "  +
                " UNION ALL SELECT  TASK_GROUP FROM kbatch_task_exec_display tc WHERE NOT EXISTS (SELECT 1 FROM kbatch_task_exec t WHERE t.task_execid=tc.task_execid) " +
                " AND tc.TASK_DATE = $S{queryTaskDate} AND tc.is_exec = '1' AND tc.EXEC_STATUS = '7') b GROUP BY TASK_GROUP ) s3 ON t.TASK_GROUP = s3.TASK_GROUP " +
                " LEFT JOIN ( SELECT count( 1 ) AS NON_REGISTRY, TASK_GROUP FROM kbatch_task_exec_display tc WHERE NOT EXISTS (SELECT 1 FROM kbatch_task_exec t WHERE t.task_execid=tc.task_execid) " +
                " AND tc.TASK_DATE = $S{queryTaskDate} AND tc.is_exec = '1' AND tc.EXEC_STATUS = 'Z' GROUP BY TASK_GROUP) s5 ON t.TASK_GROUP = s5.TASK_GROUP " +
                " LEFT JOIN ( SELECT count( 1 ) AS FAILED, TASK_GROUP FROM kbatch_task_exec WHERE EXEC_STATUS IN ( '6', '9' ) AND TASK_DATE = $S{queryTaskDate} GROUP BY TASK_GROUP ) s4 ON t.TASK_GROUP = s4.TASK_GROUP";
        return sql;
    }

    /**
     * 获取所有的清算组合清算组的上一个清算组
     *
     * @return
     */
    public SqlResult<Ta5015GroupList> getAllGroup() throws Exception {
        FetcherData<Ta5015GroupList> params = new FetcherData<>(new HashMap<>(), Ta5015GroupList.class);
        params.setMakeSql(false);
        params.setSqlNoLog(false);
        return super.findRows("select info.task_group,info.last_task_group from kbatch_group_info info", params);
    }

    public SqlResult<Ta5014> queryErrorTaskByTaskGroup(SqlParam<Ta5014> param) throws Exception {
        String sql = "SELECT task_id  FROM kbatch_task_exec WHERE task_date = $S{queryTaskDate} AND task_group = $S{taskGroup} "
                + " AND exec_status IN ('6','9')";
        SqlResult<Ta5014> result = super.findRows(sql, param);
        return result;
    }

    public SqlResult<Ta5014> queryTaskByTaskGroup(SqlParam<Ta5014> param) throws Exception {
        String sql = "SELECT task_execid  FROM kbatch_task_exec WHERE task_date = $S{queryTaskDate} AND task_group = $S{taskGroup} ";
        return super.findRows(sql, param);
    }
}
