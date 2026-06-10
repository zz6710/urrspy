package com.kayak.dps.direct.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.dps.direct.model.DataClearManage;
import org.springframework.stereotype.Repository;

@Repository
public class DataClearManageDao extends ComnDao {

	public SqlResult<DataClearManage> findDataClearManages(SqlParam<DataClearManage> params) throws Exception {
		StringBuilder sql = new StringBuilder("select krti.TASK_ID,kti.TASK_NAME ,kte.TASK_DATE ,kte.EXEC_DATE ,min(ktse.START_TIME) START_TIME ,kte.END_TIME ,kte.EXEC_STATUS ,kte.RTN_DESC \n" +
				"from kbatch_report_task_info krti\n" +
				"left join kbatch_task_exec kte on krti.TASK_ID = kte.TASK_ID \n" +
				"left join kbatch_task_info kti on kte.TASK_ID  = kti.TASK_ID \n" +
				"left join kbatch_task_step_exec ktse on kte.TASK_EXECID = ktse.TASK_EXECID \n" +
				"where krti.IS_ENABLE ='1'");
		if (Tools.isNotEmpty(params.getModel().getTaskDate())) {
			sql.append(" and kte.TASK_DATE = '").append(params.getModel().getTaskDate()).append("'");
		}
		sql.append(" group by krti.LOADORDER,krti.TASK_ID,kti.TASK_NAME ,kte.TASK_DATE ,kte.EXEC_DATE ,kte.END_TIME ,kte.EXEC_STATUS ,kte.RTN_DESC\n" +
				" order by krti.LOADORDER");
		return super.findRows(sql.toString(), params);
	}

	public void reBatchClearManage(SqlParam<DataClearManage> params) throws Exception {
		StringBuilder sql = new StringBuilder("update kbatch_task_exec kte set EXEC_STATUS='0' where \n" +
				"exists (select 1 from kbatch_report_task_info krti where krti.TASK_ID = kte.TASK_ID and krti.IS_ENABLE ='1')");
		if (Tools.isNotEmpty(params.getModel().getTaskDate())) {
			sql.append(" and kte.TASK_DATE = '").append(params.getModel().getTaskDate()).append("'");
		}
		if (Tools.isNotEmpty(params.getModel().getTaskId())) {
			sql.append(" and kte.TASK_ID = '").append(params.getModel().getTaskId()).append("'");
		}
		super.update(sql.toString(), DataSourceProperty.PUB,params.getModel());

		StringBuilder sql2 = new StringBuilder("update kbatch_task_step_exec kte set EXEC_STATUS='0' where \n" +
				"exists (select 1 from kbatch_report_task_info krti where krti.TASK_ID = kte.TASK_ID and krti.IS_ENABLE ='1')");
		if (Tools.isNotEmpty(params.getModel().getTaskDate())) {
			sql2.append(" and kte.TASK_DATE = '").append(params.getModel().getTaskDate()).append("'");
		}
		if (Tools.isNotEmpty(params.getModel().getTaskId())) {
			sql2.append(" and kte.TASK_ID = '").append(params.getModel().getTaskId()).append("'");
		}
		super.update(sql2.toString(), DataSourceProperty.PUB,params.getModel());
	}

}
