package com.kayak.schedule.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.schedule.model.QuartzInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QuartzInfoDao extends ComnDao {
	StringBuffer sb = new StringBuffer();
	static int i = 0;

	public SqlResult<QuartzInfo> findQuartzInfos(SqlParam<QuartzInfo> params) throws Exception {
		return super.findRows("SELECT id,JOB_NAME,JOB_CLASSPATH,JOB_GROUP,status,QUARTZ_RULE,CRON_EXPRESSION,description,CREATE_TIME,MODIFY_TIME,PRE_TASK_ID,exec_ip FROM base_disclosure_quartz_info order by id+0", params);
	}

	public UpdateResult addQuartzInfo(QuartzInfo params) throws Exception {
		return super.update("INSERT INTO base_disclosure_quartz_info(id,JOB_NAME,JOB_CLASSPATH,JOB_GROUP,status,QUARTZ_RULE,CRON_EXPRESSION,description,CREATE_TIME,MODIFY_TIME,PRE_TASK_ID,exec_ip) VALUES($AUTOIDS{base_disclosure_quartz_info},$S{jobName},$S{jobClasspath},$S{jobGroup},$S{status},$S{quartzRule},$S{cronExpression},$S{description},$S{createTime},$S{modifyTime},$S{preTaskId},$S{execIp})",
				params);
	}

	public UpdateResult updateQuartzInfo(SqlParam<QuartzInfo> params) throws Exception {
		return super.update("UPDATE base_disclosure_quartz_info SET JOB_NAME=$S{jobName} ,JOB_CLASSPATH=$S{jobClasspath} ,JOB_GROUP=$S{jobGroup} ,status=$S{status} ,QUARTZ_RULE=$S{quartzRule} ,CRON_EXPRESSION=$S{cronExpression} ,description=$S{description} ,CREATE_TIME=$S{createTime} ,MODIFY_TIME=$S{modifyTime} ,PRE_TASK_ID=$S{preTaskId},exec_ip=$S{execIp}  WHERE id=$S{id}",
				params.getModel());
	}

	public UpdateResult deleteQuartzInfo(String id) throws Exception {
		return super.update("DELETE FROM base_disclosure_quartz_info WHERE id=$S{id}",
				id);
	}

	/**
	 * 功能：更新任务状态
	 * 作者：rennannan
	 * 日期：
	 *
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int updateStatus(QuartzInfo params) throws Exception {
		String sql = "update base_disclosure_quartz_info set status=$S{status} where id = $S{id}";
		return super.update(sql, params).getEffect();
	}

	/**
	 * 功能：根据id查询前置条件
	 * 作者：rennannan
	 * 日期：20210531
	 *
	 * @param preTaskId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getPreInfoById(String preTaskId) throws Exception {
		Map<String, String> mapV = new HashMap<String, String>();
		String sql = " select id,JOB_NAME,JOB_CLASSPATH,JOB_GROUP,status,QUARTZ_RULE,CRON_EXPRESSION,description,CREATE_TIME,MODIFY_TIME,PRE_TASK_ID FROM base_disclosure_quartz_info where id=$S{preTaskId}";
		SqlRow row = super.findRow(sql, preTaskId);
		if (row != null) {
			mapV.put("jobClassPath", row.getString("JOB_CLASSPATH"));
			mapV.put("jobName", row.getString("JOB_NAME"));
			mapV.put("jobGroup", row.getString("JOB_GROUP"));
		}
		return mapV;
	}

	/**
	 * 递归获取前置任务信息
	 * 描述 : <描述函数实现的功能>. <br>
	 * <p>
	 *
	 * @param preTaskIdV --传入的前置任务id
	 * @return
	 * @author lixiao
	 */
	public String[] getAllPathBypreId(String preTaskIdV) throws Exception {
		Map<String, String> mapV = new HashMap<String, String>();
		String sql = "select JOB_CLASSPATH,PRE_TASK_ID FROM base_disclosure_quartz_info where id=$S{preTaskId}";
		SqlRow row = super.findRow(sql, preTaskIdV);
		if (row != null) {
			String preJobclasspath = row.getString("jobClasspath");
			sb.append(preJobclasspath).append(",");
			String preTaskId = row.getString("preTaskId");
			getAllPathBypreId(preTaskId);
		}
		return sb.toString().split(",");


	}

	/**
	 * 查询出所有启动的任务
	 * 描述 : <描述函数实现的功能>. <br>
	 * <p>
	 *
	 * @return
	 * @author rennannan
	 * 日期：20210531
	 */
	public List<Map<String, String>> getStartTask() throws Exception {
		List<Map<String, String>> listMaps = new ArrayList<Map<String, String>>();
		Map<String, String> mapV = new HashMap<String, String>();
		String sql = "SELECT  JOB_NAME,JOB_GROUP,JOB_CLASSPATH,CRON_EXPRESSION ,PRE_TASK_ID  FROM base_disclosure_quartz_info WHERE status='1'";
		List<SqlRow> list = super.findRows(sql);
		for (SqlRow row : list) {
			mapV = new HashMap<String, String>();
			mapV.put("jobName", row.getString("JOB_NAME"));
			mapV.put("jobGroup", row.getString("JOB_GROUP"));
			mapV.put("jobClassPath", row.getString("JOB_CLASSPATH"));
			mapV.put("cronexpression", row.getString("CRON_EXPRESSION"));
			mapV.put("pretaskid", row.getString("PRE_TASK_ID"));
			listMaps.add(mapV);
		}
		return listMaps;
	}

	public String[] getpreAllPathByPreId(String preTaskIdV) throws Exception {
		if (i > 0) {
			sb = new StringBuffer();
			i = 0;
		}
		i++;
		String[] pathArgs = getAllPathBypreId(preTaskIdV);
		return pathArgs;
	}

	/**
	 * 功能：修改任务信息
	 * 作者：rennannan
	 * 日期：20210531
	 *
	 * @param quartzInfo
	 * @throws Exception
	 */
	public void modTask(QuartzInfo quartzInfo) throws Exception {
		String sql = "UPDATE t8_quartzInfo q SET q.jobclasspath=$S{jobClassPath},q.quartzrule=$S{quartzRule},\n" +
				"        q.cronexpression=$S{cronExpression},q.description=$S{description},q.modifytime=$S{modifyTime},\n" +
				"        q.pretaskid=$S{preTaskId}\n" +
				"         WHERE q.id=$S{id} ";
		super.update(sql, quartzInfo);
	}
	
	public SqlRow getTaskByPath(String preTaskId) throws Exception {
		String sql = " select id,exec_ip,JOB_NAME,JOB_CLASSPATH,JOB_GROUP,status,QUARTZ_RULE,CRON_EXPRESSION,description,CREATE_TIME,MODIFY_TIME,PRE_TASK_ID FROM base_disclosure_quartz_info where JOB_CLASSPATH = $S{path}";
		SqlRow row = super.findRow(sql, preTaskId);	
		return row;
	}

}
