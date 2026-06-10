package com.kayak.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.clear.constants.BatchTaskStatus;
import com.kayak.config.constants.DataStatus;
import com.kayak.config.constants.DisplayBatchTaskStatus;
import com.kayak.config.constants.GlobalConstants;
import com.kayak.config.model.Ta5014Detail;
import com.kayak.config.model.Ta5014DetailRootStatus;
import com.kayak.core.sql.Sql;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import org.springframework.stereotype.Repository;

/**
 * 文件名: Ta5014DetailDao.java
 * 描述: 清算列表
 * 创建人: 骆福周
 * 创建时间:2021年4月01日下午3:07:37
 */
@Repository
public class Ta5014DetailDao extends ComnDao {

    public SqlResult<Ta5014Detail> queryClearTaskExecInfo(SqlParam<Ta5014Detail> params, String execGridId) throws Exception {
        Ta5014Detail model     = params.getModel();
        String       taskGroup = model.getTaskGroup();
        String       type      = model.getExecTaskType(); //总共四大类: 系统批组(1) 产品批组(2) 销售商批组(3,4) 资管批组(5,6)
        Sql          sql = getSysAndZGImpGroupSql(model, type, taskGroup);
        return super.findRows(sql, params);
    }

    /**
     * 判断execGridId是否合法.
     */
    public boolean checkExecGridId(String execGridId) {
        if (execGridId == null) return false;
        execGridId = execGridId.replace(" ",""); //去掉空字符
        if ("()".equals(execGridId) || "".equals(execGridId)) return false;
        return true; //其他情况合法
    }

    /**
     * 方法描述:展示表和执行表都要查询时，拼接状态条件
     *
     * @param model
     * @return
     */
    private String getExecBothCondition(Ta5014Detail model) {

        //执行状态
        String execStatus = model.getExecStatus();
        String condition  = "";

        if (execStatus != null && !"".equals(execStatus)) {
            switch (execStatus) {
                case DisplayBatchTaskStatus.NON_EXECUTION:
                    condition = condition
                            + " AND (CASE WHEN exec.exec_status IS NULL THEN  a.exec_status ELSE exec.exec_status END) IN ('0','Z')";
                    break;
                case DisplayBatchTaskStatus.EXECUTION:
                    condition = condition
                            + " AND (CASE WHEN exec.exec_status IS NULL THEN  a.exec_status ELSE exec.exec_status END) IN ('1', '2', '3', '4') ";
                    break;
                case DisplayBatchTaskStatus.SUCCESS:
                    condition = condition
                            + " AND (CASE WHEN exec.exec_status IS NULL THEN  a.exec_status ELSE exec.exec_status END) IN ('5', '7', '8') ";
                    break;
                case DisplayBatchTaskStatus.ROLL_BACK:
                    condition = condition
                            + " AND (CASE WHEN exec.exec_status IS NULL THEN  a.exec_status ELSE exec.exec_status END) IN ('R') ";
                    break;
                default:
                    condition = condition
                            + " AND (CASE WHEN exec.exec_status IS NULL THEN  a.exec_status ELSE exec.exec_status END) in ('6','9') ";
                    break;
            }
        }
        return condition;
    }

    /**
     * 方法描述: 只查询展示表或执行表时，拼接状态条件
     *
     * @param model
     * @return
     */
    private String getExecCondition(Ta5014Detail model) {

        //执行状态
        String execStatus = model.getExecStatus();
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
     * 方法描述:根据入参生成查询条件
     *
     * @param model
     * @return
     */
    private String getSqlCondition(Ta5014Detail model) {

        //查询条件
        String condition = "";
        //查询清算执行日期
        String queryTaskDate = model.getQueryTaskDate();
        //分组ID
        String taskGroup = model.getTaskGroup();
        //产品代码
        String prodCode = model.getProdCode();
        //销售商代码
        String targetCode = model.getTargetCode();
        //任务名称
        String taskName = model.getTaskName();

        if (!Tools.strIsEmpty(queryTaskDate)) {
            //查询的清算开始日期不为空
            condition = condition + " AND a.task_date = $S{queryTaskDate} ";
        }
        if (!Tools.strIsEmpty(taskGroup)) {
            //清算批次不为空
            condition = condition + " AND a.task_group = $S{taskGroup} ";
        }
        if (!Tools.strIsEmpty(targetCode)) {
            //目标代码不为空
            condition = condition + " AND a.target_code =   $S{targetCode} ";
        }
        if (!Tools.strIsEmpty(taskName)) {
            //清算批次不为空
            condition = condition + " AND b.task_name like  '%$U{taskName}%' ";
        }
        if (!Tools.strIsEmpty(prodCode)) {
            //清算批次不为空
            condition = condition + " AND a.prod_code =  $S{prodCode} ";
        }
        return condition;
    }

    /**
     * 方法描述: 更新预展示表、执行表的执行状态
     *
     * @param model
     * @return
     * @throws Exception
     */
    public void updateExecStatus(Ta5014Detail model, String concatSql) throws Exception {
        doTrans(() -> {
            String sql = "UPDATE kbatch_task_exec a SET a.exec_status=$S{execStatus} WHERE a.task_execid= $S{taskExecid} AND a.exec_status IN ('"
                    + BatchTaskStatus.NON_EXECUTION + "', '"
                    + BatchTaskStatus.FAILED + "', '"
                    + BatchTaskStatus.SLICE_FAILED + "')";
            String sql2 = "UPDATE kbatch_task_exec a SET a.exec_status=$S{execStatus} WHERE a.task_execid= $S{taskExecid} ";

            if (!Tools.strIsEmpty(concatSql)) {
                sql = sql + concatSql;
            }
            //更新执行表
            super.update(sql, model);
            //更新执行展示表，展示表不需要加状态条件
            super.update(sql2, model);
        });
    }


    /**
     * @param model
     * @return
     * @throws Exception
     */
    public void updateExecStatusOfBlockTask(Ta5014Detail model, String concatSql) throws Exception {
        String sql = "UPDATE kbatch_task_exec a SET a.exec_status=$S{execStatus} WHERE a.task_execid= $S{taskExecid} AND a.exec_status = '" + BatchTaskStatus.BLOCK + "'";
        //更新执行表
        super.update(sql, model);
    }


    public void rollBackClearTask(SqlParam<Ta5014Detail> params) throws Exception {
        String sql = "UPDATE kbatch_task_exec a SET a.exec_status=$S{execStatus} WHERE a.task_execid= $S{taskExecid} ";
        String sql2 = "UPDATE kbatch_task_step_exec a SET a.exec_status=$S{execStatus} WHERE a.task_execid= $S{taskExecid} ";
        //更新执行表
        super.update(sql, params.getModel());
        super.update(sql2, params.getModel());
    }

    /**
     * oracle 子项分页的方法
     */
    public String oraclePageFn(String sql, int offset, int limit) {
        return "select * from (select xsql_t1.*, rownum xsql_rownum from (" + sql
                + ") xsql_t1 where rownum <= " + (offset + limit) + ") xsql_t2 where xsql_rownum > " + offset;
    }

    /**
     * db2 子项分页的方法
     */
    public String db2PageFn(String sql, int offset, int limit) {
        return "select * from (select xsql_t1.*,rownumber() over() as xsql_rownum from ("
                + sql + ") xsql_t1 ) xsql_t2 where xsql_rownum between " + (offset + 1) + " and " + (offset + limit);
    }

    /**
     * mysql 子项分页的方法
     */
    public String mysqlPageFn(String sql, int offset, int limit) {
        return "select * from (" + sql + ") xsql_t limit " + offset + "," + limit;
    }

    /**
     * 查询回滚任务 by target_code
     *
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<Ta5014Detail> queryRevocation(SqlParam<Ta5014Detail> params) throws Exception {
        String sql = "select TASK_EXECID,TASK_DATE, STEP_NO,EXEC_STATUS,TARGET_CODE, EXEC_DATE, server_ip, START_TIME," + "RTN_DESC from kbatch_task_step_exec t WHERE TARGET_CODE = $S{taskExecid}";
        return super.findRows(sql, params);
    }


    public SqlResult<Ta5014DetailRootStatus> queryRootStatus(SqlParam<Ta5014DetailRootStatus> params) throws Exception {

        String sql = "SELECT map_key,SUM(no_excute) no_excute,SUM(excuting) excuting,SUM(success) success,SUM(failure) failure," + " SUM(no_registry) no_registry FROM (\n" + "\t SELECT \n" + "\t CASE WHEN t.prod_code IS NULL THEN t.target_code ELSE  t.prod_code END AS map_key,\n" + "\t CASE WHEN t.exec_status IN ( '0','Z' ) THEN 1 ELSE 0 END no_excute,\n" + "\t CASE WHEN t.exec_status IN ( '1','2','3','4','R' ) THEN 1 ELSE 0 END excuting,\n" + "\t CASE WHEN t.exec_status IN ( '5','7','8' ) THEN 1 ELSE 0 END success,\n" + "\t CASE WHEN t.exec_status IN ( '6','9' ) THEN 1 ELSE 0 END failure,\n" + "\t 0 no_registry\n" + " FROM kbatch_task_exec t WHERE t.TASK_GROUP= $S{taskGroup} AND t.TASK_DATE= $S{queryTaskDate} " + " UNION ALL \n" + " SELECT \n" + "\t CASE WHEN tc.prod_code IS NULL then tc.target_code ELSE  tc.prod_code END AS map_key,\n" + "\t 0 no_excute,0 excuting," + " CASE WHEN tc.exec_status ='7' THEN 1 ELSE 0 END success, 0 failure," + " CASE WHEN tc.exec_status ='Z' THEN 1 ELSE 0 END  no_registry \n" + " FROM kbatch_task_exec_DISPLAY tc WHERE \n" + " NOT EXISTS (SELECT 1 FROM kbatch_task_exec t WHERE t.task_execid=tc.task_execid) AND tc.TASK_GROUP =$S{taskGroup} " + " AND tc.TASK_DATE = $S{queryTaskDate} AND tc.is_exec = '1'\n" + " ) t  GROUP BY map_key ";
        return super.findRows(sql, params);
    }

    //系统批组和资管导入批组 sql拼接
    public Sql getSysAndZGImpGroupSql(Ta5014Detail model, String type, String taskGroup) {
        //===============ORACLE===============
        String sql3;
        String sql4;

        //===============MYSQL===============
        String sql3Mysql;
        String sql4Mysql;

        //===============DB2===============
        String sql3Db2;
        String sql4Db2;

        //第二层-执行任务，实际的任务数据，如果已经注册到执行表的数据
        sql3 = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	a.target_code,"
                + " '' AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " st.display_order AS task_order"
                + " FROM "
                + "kbatch_task_exec a"
                + "	LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id	"
                + "	LEFT JOIN kbatch_task_set st ON a.task_id=st.task_id AND st.task_group=a.task_group	AND st.moduleid = a.moduleid	"
                + "	LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + " WHERE a.moduleid = 'a'	";

        sql3Mysql = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	a.target_code,"
                + " '' AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " st.display_order AS task_order"
                + " FROM"
                + "	kbatch_task_exec a "
                + "	LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id	"
                + "	LEFT JOIN kbatch_task_set st ON a.task_id=st.task_id AND st.task_group=a.task_group	AND st.moduleid = a.moduleid"
                + "	LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'	";

        sql3Db2 = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	a.target_code,"
                + " '' AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " st.display_order  AS task_order "
                + " FROM "
                + "		kbatch_task_exec a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id	"
                + "		LEFT JOIN kbatch_task_set st ON a.task_id=st.task_id	AND st.task_group=a.task_group	AND st.moduleid = a.moduleid"
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group"
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'";


        //第三层-执行任务，展示未达执行条件，未插入到执行表的数据
        sql4 = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	a.target_code,"
                + " '' AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " se.display_order AS task_order"
                + " FROM"
                + "	kbatch_task_exec_display a"
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id"
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group"
                + "    	LEFT JOIN kbatch_task_set se"
                + "			ON se.moduleid = a.moduleid AND se.task_group=a.task_group AND se.task_id=a.task_id"
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'	AND a.is_exec='1'";

        //mysql
        //第三层-执行任务，展示未达执行条件，未插入到执行表的数据
        sql4Mysql = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	a.target_code,"
                + " '' AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + "CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " se.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN kbatch_task_set se "
                + "			ON se.moduleid = a.moduleid AND se.task_group=a.task_group AND se.task_id=a.task_id "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' AND a.is_exec='1' ";
        //db2
        sql4Db2 = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	'' AS target_code,"
                + " '' AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " se.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN kbatch_task_set se "
                + "			ON se.moduleid = a.moduleid AND se.task_group=a.task_group AND se.task_id=a.task_id "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'	AND a.is_exec='1' ";

        //oracle
        sql3 = sql3 + getSqlCondition(model) + getExecCondition(model);
        sql4 = sql4 + getSqlCondition(model) + getExecCondition(model) + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";

        //mysql
        sql3Mysql = sql3Mysql + getSqlCondition(model) + getExecCondition(model);
        sql4Mysql = sql4Mysql + getSqlCondition(model) + getExecCondition(model) + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";


        //db2
        sql3Db2 = sql3Db2 + getSqlCondition(model) + getExecCondition(model);
        sql4Db2 = sql4Db2 + getSqlCondition(model) + getExecCondition(model) + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";

        String sqlAll   = String.format("SELECT t.* from ( %s  UNION ALL %s ) t ORDER BY t.task_order,t.exec_grid_id", sql3, sql4);
        String sqlDb2   = String.format("SELECT t.* from ( %s  UNION ALL %s ) t ORDER BY t.task_order,t.exec_grid_id", sql3Db2, sql4Db2);
        String sqlMysql = String.format("SELECT t.* from ( %s  UNION ALL %s ) t ORDER BY t.task_order,t.exec_grid_id", sql3Mysql, sql4Mysql);
        Sql    sql      = Sql.build().oracleSql(sqlAll).db2Sql(sqlDb2).mysqlSql(sqlMysql);
        return sql;
    }


    //产品批组sql拼接
    public Sql getProdGroupSql(Ta5014Detail model, String type, String taskGroup, String execGridId) {
        //===============ORACLE===============
        String sql3;
        String sql4;

        //===============MYSQL===============
        String sql3Mysql;
        String sql4Mysql;

        //===============DB2===============
        String sql3Db2;
        String sql4Db2;


        //第三层-执行任务，实际的任务数据，如果已经注册到执行表的数据
        sql3 = "SELECT "
                + "	a.prod_code  || '_' || a.task_id AS exec_grid_id,"
                + " a.prod_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	'' AS target_code,"
                + " '' AS distributor_name,"
                + "	a.prod_code,"
                + "	prod.prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " pt.display_order AS task_order "
                + " FROM "
                + "		kbatch_task_exec a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_prod_info prod ON prod.prod_code = a.prod_code	  AND  prod.DATA_STATUS='" + DataStatus.EFFECTED + "'	"
                + "		LEFT JOIN ta_prod_task_set pt ON pt.task_id=a.task_id	AND pt.batch_mode=prod.batch_mode	AND pt.moduleid = a.moduleid	"
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' ";

        sql3Mysql = "SELECT "
                + "	concat(a.prod_code , '_' , a.task_id) AS exec_grid_id,"
                + " a.prod_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	'' AS target_code,"
                + " '' AS distributor_name,"
                + "	a.prod_code,"
                + "	prod.prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " pt.display_order AS task_order "
                + " FROM "
                + "		kbatch_task_exec a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_prod_info prod ON prod.prod_code = a.prod_code	  AND  prod.DATA_STATUS='" + DataStatus.EFFECTED + "'	"
                + "		LEFT JOIN ta_prod_task_set pt ON pt.task_id=a.task_id	AND pt.batch_mode=prod.batch_mode	AND pt.moduleid = a.moduleid	"
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' ";

        sql3Db2 = "SELECT "
                + "	a.prod_code || '_' ||  a.task_id AS exec_grid_id,"
                + " a.prod_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	'' AS target_code,"
                + " '' AS distributor_name,"
                + "	a.prod_code,"
                + "	prod.prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " pt.display_order AS task_order "
                + " FROM "
                + "		kbatch_task_exec a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_prod_info prod ON prod.prod_code = a.prod_code	 AND  prod.DATA_STATUS='" + DataStatus.EFFECTED + "' "
                + "		LEFT JOIN ta_prod_task_set pt ON pt.task_id=a.task_id	AND pt.batch_mode=prod.batch_mode	AND pt.moduleid = a.moduleid	"
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' ";


        //第三层-执行任务，展示未达执行条件，未插入到执行表的数据
        sql4 = "SELECT "
                + "	a.prod_code  || '_' || a.task_id AS exec_grid_id,"
                + " a.prod_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	'' AS target_code,"
                + " '' AS distributor_name,"
                + "	a.prod_code,"
                + "	prod.prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " pt.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_prod_info prod ON prod.prod_code = a.prod_code	AND  prod.DATA_STATUS='" + DataStatus.EFFECTED + "'	"
                + "		LEFT JOIN ta_prod_task_set pt ON pt.task_id = a.task_id"
                + "			                           AND pt.batch_mode = prod.batch_mode"
                + "			                            AND pt.moduleid = a.moduleid"
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' AND a.is_exec='1' ";

        //mysql
        //第三层-执行任务，展示未达执行条件，未插入到执行表的数据
        sql4Mysql = "SELECT "
                + "	concat( a.prod_code, '_' , a.task_id) AS exec_grid_id,"
                + " a.prod_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	'' AS target_code,"
                + " '' AS distributor_name,"
                + "	a.prod_code,"
                + "	prod.prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " pt.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_prod_info prod ON prod.prod_code = a.prod_code		AND  prod.DATA_STATUS='" + DataStatus.EFFECTED + "'	"
                + "		LEFT JOIN ta_prod_task_set pt ON pt.task_id = a.task_id"
                + "			                           AND pt.batch_mode = prod.batch_mode "
                + "			                            AND pt.moduleid = a.moduleid "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' AND a.is_exec='1' ";
        //db2
        sql4Db2 = "SELECT "
                + "	a.prod_code || '_' ||  a.task_id AS exec_grid_id,"
                + " a.prod_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	'' AS target_code,"
                + " '' AS distributor_name,"
                + "	a.prod_code,"
                + "	prod.prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " pt.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_prod_info prod ON prod.prod_code = a.prod_code		AND  prod.DATA_STATUS='" + DataStatus.EFFECTED + "'	"
                + "		LEFT JOIN ta_prod_task_set pt ON pt.task_id = a.task_id "
                + "			                           AND pt.batch_mode = prod.batch_mode "
                + "			                            AND pt.moduleid = a.moduleid "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' AND a.is_exec='1' ";

        //oracle
        sql3 = sql3 + getSqlCondition(model) + getExecCondition(model);
        sql4 = sql4 + getSqlCondition(model) + getExecCondition(model) + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";

        //mysql
        sql3Mysql = sql3Mysql + getSqlCondition(model) + getExecCondition(model);
        sql4Mysql = sql4Mysql + getSqlCondition(model) + getExecCondition(model) + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";


        //db2
        sql3Db2 = sql3Db2 + getSqlCondition(model) + getExecCondition(model);
        sql4Db2 = sql4Db2 + getSqlCondition(model) + getExecCondition(model) + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";

        String sqlAll   = String.format("SELECT t.* FROM ( %s  UNION ALL %s ) t WHERE t.prod_code in " + execGridId + " ORDER BY t.task_order ", sql3, sql4);
        String sqlDb2   = String.format("SELECT t.* FROM ( %s  UNION ALL %s ) t WHERE t.prod_code in " + execGridId + " ORDER BY t.task_order ", sql3Db2, sql4Db2);
        String sqlMysql = String.format("SELECT t.* FROM ( %s  UNION ALL %s ) t WHERE t.prod_code in " + execGridId + " ORDER BY t.task_order ", sql3Mysql, sql4Mysql);
        Sql    sql      = Sql.build().oracleSql(sqlAll).db2Sql(sqlDb2).mysqlSql(sqlMysql);
        return sql;
    }


    //销售商批组sql拼接
    public Sql getDistributorGroupSql(Ta5014Detail model, String type, String taskGroup, String execGridId) {
        //===============ORACLE===============
        String sql3;
        String sql4;

        //===============MYSQL===============
        String sql3Mysql;
        String sql4Mysql;

        //===============DB2===============
        String sql3Db2;
        String sql4Db2;

        //销售商批组
        //第三层-执行任务，实际的任务数据，如果已经注册到执行表的数据
        sql3 = "SELECT "
                + "	a.target_code || '_' || a.task_id AS exec_grid_id,"
                + " a.target_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	a.target_code,"
                + " dist.distributor_name  AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " st.display_order AS task_order "
                + " FROM "
                + "	 kbatch_task_exec a "
                + "	 LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "	 LEFT JOIN kbatch_task_set st ON a.task_id=st.task_id	AND st.task_group=a.task_group	AND st.moduleid = a.moduleid	"
                + "	 LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "  LEFT JOIN ta_distributor_info dist ON dist.distributor_code = a.target_code	AND  dist.DATA_STATUS='"
                + DataStatus.EFFECTED + "' WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' ";

        sql3Mysql = "SELECT "
                + "	concat(a.target_code  , '_' , a.task_id) AS exec_grid_id,"
                + " a.target_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	a.target_code,"
                + " dist.distributor_name AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " st.display_order AS task_order "
                + " FROM "
                + "		kbatch_task_exec a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_task_set st ON a.task_id=st.task_id	AND st.task_group=a.task_group	AND st.moduleid = a.moduleid	"
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_distributor_info dist ON dist.distributor_code = a.target_code	AND  dist.DATA_STATUS='"
                + DataStatus.EFFECTED + "' WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' ";

        sql3Db2 = "SELECT "
                + "	a.target_code || '_' ||  a.task_id AS exec_grid_id,"
                + " a.target_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	a.target_code,"
                + " dist.distributor_name  AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " st.display_order AS task_order "
                + " FROM "
                + "	 kbatch_task_exec a "
                + "	 LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "	 LEFT JOIN kbatch_task_set st ON a.task_id=st.task_id	AND st.task_group=a.task_group	AND st.moduleid = a.moduleid	"
                + "	 LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "  LEFT JOIN ta_distributor_info dist ON dist.distributor_code = a.target_code	AND  dist.DATA_STATUS='"
                + DataStatus.EFFECTED + "' WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' ";


        //第三层-执行任务，展示未达执行条件，未插入到执行表的数据
        sql4 = "SELECT "
                + "	a.target_code  || '_' || a.task_id AS exec_grid_id,"
                + " a.target_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	a.target_code,"
                + " dist.distributor_name AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION
                + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " se.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_distributor_info dist ON dist.distributor_code = a.target_code		AND  dist.DATA_STATUS='"
                + DataStatus.EFFECTED + "' "
                + "    	LEFT JOIN kbatch_task_set se "
                + "			ON se.moduleid = a.moduleid AND se.task_group=a.task_group AND se.task_id=a.task_id "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'		AND a.is_exec='1' ";

        //mysql
        //第三层-执行任务，展示未达执行条件，未插入到执行表的数据
        sql4Mysql = "SELECT "
                + "	concat(a.target_code , '_' , a.task_id) AS exec_grid_id,"
                + " a.target_code AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	a.target_code,"
                + " dist.distributor_name AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION
                + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " se.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_distributor_info dist ON dist.distributor_code = a.target_code		AND  dist.DATA_STATUS='"
                + DataStatus.EFFECTED + "' "
                + "    	LEFT JOIN kbatch_task_set se "
                + "			ON se.moduleid = a.moduleid AND se.task_group=a.task_group AND se.task_id=a.task_id "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'		AND a.is_exec='1' ";
        //db2
        sql4Db2 = "SELECT "
                + "	a.target_code || '_' ||  a.task_id AS exec_grid_id,"
                + "  a.target_code  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	a.target_code,"
                + " dist.distributor_name AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION
                + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " se.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "    	LEFT JOIN ta_distributor_info dist ON dist.distributor_code = a.target_code		AND  dist.DATA_STATUS='"
                + DataStatus.EFFECTED + "' "
                + "    	LEFT JOIN kbatch_task_set se "
                + "			ON se.moduleid = a.moduleid AND se.task_group=a.task_group AND se.task_id=a.task_id "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'		AND a.is_exec='1' ";


        //oracle
        sql3 = sql3 + getSqlCondition(model) + getExecCondition(model);
        sql4 = sql4 + getSqlCondition(model) + getExecCondition(model)
                + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";

        //mysql
        sql3Mysql = sql3Mysql + getSqlCondition(model) + getExecCondition(model);
        sql4Mysql = sql4Mysql + getSqlCondition(model) + getExecCondition(model)
                + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";


        //db2
        sql3Db2 = sql3Db2 + getSqlCondition(model) + getExecCondition(model);
        sql4Db2 = sql4Db2 + getSqlCondition(model) + getExecCondition(model)
                + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";

        String sqlAll   = String.format("SELECT t.* FROM (%s  UNION ALL %s ) t WHERE t.target_code in " + execGridId + "  ORDER BY t.task_order", sql3, sql4);
        String sqlDb2   = String.format("SELECT t.* FROM (%s  UNION ALL %s ) t WHERE t.target_code in " + execGridId + "  ORDER BY t.task_order", sql3Db2, sql4Db2);
        String sqlMysql = String.format("SELECT t.* FROM (%s  UNION ALL %s ) t WHERE t.target_code in " + execGridId + "  ORDER BY t.task_order", sql3Mysql, sql4Mysql);
        Sql    sql      = Sql.build().oracleSql(sqlAll).db2Sql(sqlDb2).mysqlSql(sqlMysql);
        return sql;
    }


    //资管导出批组sql拼接
    public Sql getZGExpGroupSql(Ta5014Detail model, String type, String taskGroup) {
        //===============ORACLE===============
        String sql3;
        String sql4;

        //===============MYSQL===============
        String sql3Mysql;
        String sql4Mysql;

        //===============DB2===============
        String sql3Db2;
        String sql4Db2;

        //第二层-执行任务，实际的任务数据，如果已经注册到执行表的数据
        sql3 = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "' AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	a.target_code,"
                + " intf.file_desc AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " st.display_order AS task_order"
                + " FROM "
                + "		kbatch_task_exec a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_task_set st ON a.task_id=st.task_id	AND st.task_group=a.task_group	AND st.moduleid = a.moduleid	"
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "     LEFT JOIN ta_intf_file_manage intf ON intf.file_type = a.target_code "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'	";

        sql3Mysql = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	a.target_code,"
                + " intf.file_desc AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " st.display_order AS task_order "
                + " FROM "
                + "		kbatch_task_exec a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_task_set st ON a.task_id=st.task_id	AND st.task_group=a.task_group	AND st.moduleid = a.moduleid	"
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "     LEFT JOIN ta_intf_file_manage intf ON intf.file_type = a.target_code "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'	";

        sql3Db2 = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	a.exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	a.alarm_time,"
                + "	a.target_code,"
                + " intf.file_desc AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	a.start_time,"
                + "	a.end_time,"
                + " a.exec_status,"
                + "	a.thread_uuid,"
                + "	a.server_node,"
                + "	a.server_name,"
                + "	a.server_ip,"
                + "	a.rtn_code,"
                + "	a.rtn_desc,"
                + "	a.task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '1' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " st.display_order  AS task_order "
                + " FROM "
                + "		kbatch_task_exec a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_task_set st ON a.task_id=st.task_id	AND st.task_group=a.task_group	AND st.moduleid = a.moduleid	"
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "     LEFT JOIN ta_intf_file_manage intf ON intf.file_type = a.target_code "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' ";


        //第三层-执行任务，展示未达执行条件，未插入到执行表的数据
        sql4 = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	a.target_code,"
                + " intf.file_desc AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " se.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "     LEFT JOIN ta_intf_file_manage intf ON intf.file_type = a.target_code "
                + "    	LEFT JOIN kbatch_task_set se "
                + "			ON se.moduleid = a.moduleid AND se.task_group=a.task_group AND se.task_id=a.task_id "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'	AND a.is_exec='1' ";

        //mysql
        //第三层-执行任务，展示未达执行条件，未插入到执行表的数据
        sql4Mysql = "SELECT "
                + "'' exec_grid_id,"
                + "'" + taskGroup + "'   AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	a.target_code,"
                + " intf.file_desc AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + "CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " se.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "     LEFT JOIN ta_intf_file_manage intf ON intf.file_type = a.target_code "
                + "    	LEFT JOIN kbatch_task_set se "
                + "			ON se.moduleid = a.moduleid AND se.task_group=a.task_group AND se.task_id=a.task_id "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"' AND a.is_exec='1' ";
        //db2
        sql4Db2 = "SELECT "
                + "	a.task_id AS exec_grid_id,"
                + "'" + taskGroup + "'  AS parent_exec_grid_id,"
                + "	a.task_execid,"
                + "	a.moduleid,"
                + "	a.task_id,"
                + "	'' AS exec_date,"
                + "	a.should_exec_date,"
                + "	a.should_exec_time,"
                + "	'' AS alarm_time,"
                + "	a.target_code,"
                + " intf.file_desc AS distributor_name,"
                + "	'' AS prod_code,"
                + "	'' AS prod_name,"
                + "	'' AS start_time,"
                + "	'' AS end_time,"
                + " CASE WHEN a.exec_status='" + BatchTaskStatus.DISPLAY_INIT + "' THEN '" + BatchTaskStatus.NON_EXECUTION + "' ELSE a.exec_status  END AS  exec_status,"
                + "	'' AS thread_uuid,"
                + "	'' AS server_node,"
                + "	'' AS server_name,"
                + "	'' AS server_ip,"
                + "	'' AS rtn_code,"
                + "	'' AS rtn_desc,"
                + "	'' AS task_params,"
                + "'" + taskGroup + "' task_group,"
                + "	a.pre_task_id,"
                + "	a.task_date,"
                + "	b.task_name,"
                + "	b.can_again,"
                + "'" + type + "' task_type,"
                + " '0' AS button_is_display,"
                + " b.simple_flow,"
                + " a.distributor_batch,"
                + " '' AS group_order,"
                + " se.display_order AS task_order "
                + " FROM "
                + "	kbatch_task_exec_display a "
                + "		LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
                + "		LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
                + "     LEFT JOIN ta_intf_file_manage intf ON intf.file_type = a.target_code "
                + "    	LEFT JOIN kbatch_task_set se "
                + "			ON se.moduleid = a.moduleid AND se.task_group=a.task_group AND se.task_id=a.task_id "
                + " WHERE a.moduleid = '"+ GlobalConstants.MODULEID +"'	AND a.is_exec='1' ";

        //oracle
        sql3 = sql3 + getSqlCondition(model) + getExecCondition(model);
        sql4 = sql4 + getSqlCondition(model) + getExecCondition(model) + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";

        //mysql
        sql3Mysql = sql3Mysql + getSqlCondition(model) + getExecCondition(model);
        sql4Mysql = sql4Mysql + getSqlCondition(model) + getExecCondition(model) + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";


        //db2
        sql3Db2 = sql3Db2 + getSqlCondition(model) + getExecCondition(model);
        sql4Db2 = sql4Db2 + getSqlCondition(model) + getExecCondition(model) + " AND NOT EXISTS (SELECT 1 FROM kbatch_task_exec exec WHERE exec.task_execid=a.task_execid) ";

        String sqlAll   = String.format("SELECT t.* from ( %s  UNION ALL %s ) t ORDER BY t.task_order,t.exec_grid_id", sql3, sql4);
        String sqlDb2   = String.format("SELECT t.* from ( %s  UNION ALL %s ) t ORDER BY t.task_order,t.exec_grid_id", sql3Db2, sql4Db2);
        String sqlMysql = String.format("SELECT t.* from ( %s  UNION ALL %s ) t ORDER BY t.task_order,t.exec_grid_id", sql3Mysql, sql4Mysql);
        Sql    sql      = Sql.build().oracleSql(sqlAll).db2Sql(sqlDb2).mysqlSql(sqlMysql);
        return sql;
    }

    public SqlResult<Ta5014Detail> queryRootData(SqlParam<Ta5014Detail> params) throws Exception {
        Ta5014Detail model       = params.getModel();
        String       sql         = "";
        int          limit       = model.getLimit();
        int          currentPage = model.getCurrentPage();
        if (limit <= 0) {
            limit = 10;
        }
        if (currentPage < 0) {
            currentPage = 0;
        }
//        if (BatchTaskType.isProdGroup(model.getExecTaskType())) {
//            //产品批次
//            sql = "SELECT "
//                    + "  a.prod_code AS exec_grid_id,"
//                    + "  '' AS parent_exec_grid_id,"
//                    + "  '' AS task_execid,"
//                    + "  'a' AS moduleid,"
//                    + "  '' AS task_id,"
//                    + "  '' AS exec_date,"
//                    + "  MAX(exec.should_exec_date) AS should_exec_date,"
//                    + "  MAX(exec.should_exec_time) AS should_exec_time,"
//                    + "  '' AS alarm_time,"
//                    + "  '' AS target_code,"
//                    + "  '' AS distributor_name,"
//                    + "  a.prod_code,"
//                    + "  prod.prod_name,"
//                    + "  MIN(exec.start_time)  AS start_time,"
//                    + "  MAX(exec.end_time)    AS end_time,"
//                    + "  '' AS exec_status,"
//                    + "  '' AS thread_uuid,"
//                    + "  '' AS server_node,"
//                    + "  '' AS server_name,"
//                    + "  '' AS server_ip,"
//                    + "  '' AS rtn_code,"
//                    + "  '' AS rtn_desc,"
//                    + "  '' AS task_params,"
//                    + "'" + model.getTaskGroup() + "'  AS task_group,"
//                    + "  '' AS pre_task_id,"
//                    + "  MAX(exec.task_date) AS task_date,"
//                    + "  prod.prod_name AS task_name,"
//                    + "  '' AS can_again,"
//                    + "  '' AS task_type,"
//                    + "  '0' AS button_is_display,"
//                    + "  '' AS simple_flow,"
//                    + "  '' AS distributor_batch,"
//                    + "  '' AS group_order,"
//                    + "  0 AS task_order "
//                    + " FROM "
//                    + "  kbatch_task_exec_display a "
//                    + "    LEFT JOIN kbatch_task_exec exec on a.task_execid=exec.task_execid "
//                    + "    LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
//                    + "    LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
//                    + "    LEFT JOIN ta_prod_info prod ON prod.prod_code = a.prod_code	AND  prod.DATA_STATUS='"
//                    + DataStatus.EFFECTED + "' WHERE a.moduleid = 'a' AND a.is_exec = '1' " + getSqlCondition(model)
//                    + getExecBothCondition(model) + " GROUP BY a.prod_code,prod.prod_name ORDER BY a.prod_code ";
//        } else {
//            //销售商批组
//            sql = "SELECT "
//                    + "  a.target_code AS exec_grid_id,"
//                    + "  '' AS parent_exec_grid_id,"
//                    + "  '' AS task_execid,"
//                    + "  'a' AS moduleid,"
//                    + "  '' AS task_id,"
//                    + "  '' AS exec_date,"
//                    + "  MAX(exec.should_exec_date) AS should_exec_date,"
//                    + "  MAX(exec.should_exec_time) AS should_exec_time,"
//                    + "  '' AS alarm_time,"
//                    + "  a.target_code,"
//                    + "  dist.distributor_name,"
//                    + "  '' AS prod_code,"
//                    + "  '' AS prod_name,"
//                    + "  MIN(exec.start_time)  AS start_time,"
//                    + "  MAX(exec.end_time)    AS end_time,"
//                    + "  '' AS exec_status,"
//                    + "  '' AS thread_uuid,"
//                    + "  '' AS server_node,"
//                    + "  '' AS server_name,"
//                    + "  '' AS server_ip,"
//                    + "  '' AS rtn_code,"
//                    + "  '' AS rtn_desc,"
//                    + "  '' AS task_params,"
//                    + "'" + model.getTaskGroup() + "'  AS task_group,"
//                    + "  ''  AS pre_task_id,"
//                    + "  MAX(exec.task_date)   AS task_date,"
//                    + "  dist.distributor_name AS task_name,"
//                    + "  ''  AS can_again,"
//                    + "  ''  AS task_type,"
//                    + "  '0' AS button_is_display,"
//                    + "  '' AS simple_flow,"
//                    + "  '' AS distributor_batch,"
//                    + "  '' AS group_order,"
//                    + "  0 AS task_order "
//                    + " FROM "
//                    + "  kbatch_task_exec_display a "
//                    + "    LEFT JOIN kbatch_task_exec exec on a.task_execid=exec.task_execid "
//                    + "    LEFT JOIN kbatch_task_info b ON a.task_id=b.task_id "
//                    + "    LEFT JOIN kbatch_group_info gi ON a.task_group = gi.task_group "
//                    + "    LEFT JOIN ta_distributor_info dist ON dist.distributor_code = a.target_code	AND  dist.DATA_STATUS='"
//                    + DataStatus.EFFECTED + "'	" + " WHERE a.moduleid = 'a' AND   a.is_exec = '1' " + getSqlCondition(model)
//                    + getExecBothCondition(model) + " GROUP BY a.target_code,dist.distributor_name ORDER BY a.target_code ";
//        }

        Long                    total     = super.findRow(sql.replaceAll(".*(FROM.*)$","SELECT COUNT(1) total FROM (SELECT 1 $1) t"), params.getParams()).getLong("total");
        String                  mysqlSql  = this.mysqlPageFn(sql, currentPage * limit, limit);
        String                  oracleSql = this.oraclePageFn(sql, currentPage * limit, limit);
        String                  db2Sql    = this.db2PageFn(sql, currentPage * limit, limit);
        Sql                     ssql      = Sql.build().mysqlSql(mysqlSql).oracleSql(oracleSql).db2Sql(db2Sql);
        SqlResult<Ta5014Detail> rows      = super.findRows(ssql, params);
        rows.getRows().forEach(res -> {
            res.setTotal(String.valueOf(total));
        });
        return rows;
    }
}
