package com.kayak.pms.T85.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.T85.model.T85002FlowInfo;
import com.kayak.pms.global.constants.GlobalConstants;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kayak.pms.global.constants.BatchTaskStatus.*;

/**
 * 文件名: Ta5002Dao.java
 * 描述: 
 * 创建人: zengzt
 * 创建时间:2020年5月23日下午4:18:51
 */
@Repository
public class T85002Dao extends ComnDao{

	public SqlResult<T85002FlowInfo> queryClearFlowInfo(SqlParam<T85002FlowInfo> params) throws Exception {
		
		String sql = "SELECT dis.simple_flow,$S{taskDate} AS task_date, "
					+"       SUM(CASE WHEN exe.exec_status IN ('"+SUCCESS+"', '"+SKIP+"', '"+TERMINATION+"') THEN 1 ELSE 0 END) AS success_num, "
					+"       SUM(CASE WHEN exe.exec_status IN ('"+FAILED+"','"+SLICE_FAILED+"') then 1 else 0 end) AS fail_num, "
					+"       SUM(CASE WHEN exe.exec_status IN ('"+LOOT+"', '"+EXECUTION+"', '"+SLICE_EXECUTION+"', '"+TO_EXEC+"') THEN 1 ELSE 0 END) AS executing_num, "
					+"       SUM(CASE WHEN exe.exec_status IN ('"+NON_EXECUTION+"') then 1 else 0 end) AS no_execute_num, "
					+"       count(1) clear_count "
					+"    FROM t8_clear_task_exec_display dis "
					+"                LEFT JOIN t8_clear_task_exec exe ON dis.task_execid = exe.task_execid "
					+"    WHERE  dis.task_date = $S{taskDate} "
					+" 			 AND dis.moduleid = '"+GlobalConstants.MODULEID+"' "
					+"           AND dis.IS_EXEC = '1' "
					+"         GROUP BY dis.simple_flow ";
		
		return super.findRows(sql, params);
	}
	
	/**
	 * 
	 * 方法描述:校验工作日当日的清算是否全部执行完成
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> checkClearExec(String params) throws Exception {

		String sql = "select count(1) count from "
				+"	t8_clear_task_exec_display dis LEFT JOIN t8_clear_task_exec exe ON dis.task_execid=exe.task_execid  "
				+"	 WHERE dis.task_date= $S{taskDate} "
				+"	  AND dis.is_exec = '1'  "
				+"	  AND dis.moduleid = '"+GlobalConstants.MODULEID+"' "
				+"	  AND NOT EXISTS (SELECT 1 FROM t8_clear_task_exec exe "
				+ "					WHERE dis.task_execid=exe.task_execid AND exe.exec_status NOT IN ('"+SUCCESS+"', '"+SKIP+"', '"+TERMINATION+"')) ";
		
		return super.findRows(sql, params);
	}

	/**
	 * 
	 * 方法描述:查询当日流程对应
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> queryClearExecProd(T85002FlowInfo params) throws Exception {

		String sql = "select distinct prod_code from t8_clear_task_exec where task_date=$S{taskDate} AND simple_flow=$S{simpleFlow} and prod_code is not null ";

		return super.findRows(sql, params);
	}

	/**
	 * 
	 * 方法描述:查询任务执行数据
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> queryClearExecTarget(T85002FlowInfo params) throws Exception {

		String sql = "select distinct target_code from t8_clear_task_exec where task_date=$S{taskDate} AND simple_flow=$S{simpleFlow} and target_code is not null ";

		return super.findRows(sql, params);
	}
	
}
