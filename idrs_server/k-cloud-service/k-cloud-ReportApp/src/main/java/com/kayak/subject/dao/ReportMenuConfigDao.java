package com.kayak.subject.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.subject.model.ReportMenuConfig;
import com.kayak.utils.ReportDataGenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReportMenuConfigDao extends ComnDao {

	public SqlResult<ReportMenuConfig> findReportMenuConfigs(SqlParam<ReportMenuConfig> params) throws Exception {
		return super.findRows("SELECT id,menu_id,menu_name,button_name,task_id,task_name,task_type,report_type,report_code,report_value,is_use,is_show,sort,create_date FROM base_report_menu_config", params);
	}

	public SqlResult<ReportMenuConfig> findReportMenuConfigs1(SqlParam<ReportMenuConfig> params) throws Exception {
		return super.findRows("SELECT id,menu_id,menu_name,button_name,task_id,task_name,task_name as name,task_type,report_type,report_code,report_value,is_use,is_show,sort,create_date FROM base_report_menu_config order by sort", params);
	}

	public List<SqlRow> findReportMenuConfigs(Map<String, Object> params) throws Exception {
		String sql = "SELECT id,menu_id,menu_name,button_name,task_id,task_name,task_type,report_type,report_code,report_value,is_use,is_show,sort,create_date FROM base_report_menu_config " +
				"WHERE is_use='1' and menu_id = $S{menuId} and button_name = $S{buttonName} ";
				if (!ObjectUtils.isEmpty(params.get("id"))) {
					sql += "and id in ($U{id}) ";
				}
				sql += "order by sort ";
		return super.findRows(sql, params);
	}

	/**
	 * 查询报送数据锁表配置表
	 * @param settle_date
	 * @param task_id
	 * @return
	 */
	public boolean checkTaskIsLocked(String settle_date, String task_id) {
		try {
			String checkSql = "select distinct dd.lock_status, dd.report_table" +
					"            from base_report_data_lock_record dd" +
					"            left join base_report_data_lock_config dl on dl.report_table = dd.report_table " +
					"           where dd.report_date = '" + settle_date + "' " +
					"             and dl.task_id = '" + task_id + "' ";
			SqlRow row = super.findRow(checkSql, null);
			if (row == null || row.size() == 0) {
				return false;
			}
			return ("01".equals(row.getString("lock_status")));//需同时满足配置记录表锁表+表中存在数据时才返回锁表
		} catch (Exception e) {
			log.error("查询报送数据锁定配置表信息异常:" + e.getMessage());
			return false;
		}
	}

	public UpdateResult addReportMenuConfig(SqlParam<ReportMenuConfig> params) throws Exception {
		return super.update("INSERT INTO base_report_menu_config(id,menu_id,menu_name,button_name,task_id,task_name,task_type,report_type,report_code,report_value,is_use,is_show,sort,create_date) VALUES($AUTOIDI{id},$S{menuId},$S{menuName},$S{buttonName},$S{taskId},$S{taskType},$S{reportType},$S{reportCode},$S{reportValue},$S{isUse},$S{sort},$S{createDate})",
				params.getModel());
	}
	
	public UpdateResult updateReportMenuConfig(SqlParam<ReportMenuConfig> params) throws Exception {
		return super.update("UPDATE base_report_menu_config SET menu_id=$S{menuId} ,menu_name=$S{menuName} ,button_name=$S{buttonName} ,task_id=$S{taskId} ,task_name=$S{taskName} ,task_type=$S{taskType} ,report_type=$S{reportType} ,report_code=$S{reportCode} ,report_value=$S{reportValue} ,is_use=$S{isUse} ,is_show=$S{isShow} ,sort=$S{sort} ,create_date=$S{createDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteReportMenuConfig(SqlParam<ReportMenuConfig> params) throws Exception {
		return super.update("DELETE FROM base_report_menu_config WHERE  id=$I{id} ",
				params.getModel());
	}

	public String reportMenuConfigHandler (Map<String, Object> params) throws Exception {
		String reportDate = (String) params.get("reportDate"); // 前端提供的报表日期
		StringBuilder stringBuilder = new StringBuilder();     // 前端展示的报表名称
		String nextMonthLastDay = DateUtil.getNextMonthLastDay(reportDate,1);// 下月末日期

		doTrans(() -> {
			List<SqlRow> sqlRows = findReportMenuConfigs(params);

			if (CollectionUtil.isEmpty(sqlRows)) {
				throw new Exception("清算任务未配置，请联系管理员!");
			}

			int number = 0;
			for (SqlRow sqlRow : sqlRows) {
				String task_id = sqlRow.getString("task_id");
				String task_name = sqlRow.getString("task_name");
				String task_type = sqlRow.getString("task_type");
				String report_type = sqlRow.getString("report_type");
				String report_code = sqlRow.getString("report_code");
				String report_value = sqlRow.getString("report_value");
				String is_show = sqlRow.getString("is_show");
				if(checkTaskIsLocked(reportDate,task_id)){
					if ("1".equals(is_show)){
						stringBuilder.append(++number + "、" + task_name + "已锁定<br>");
					}
					continue;
				}
				if ("1".equals(is_show)) {
					stringBuilder.append(++number + "、" + task_name + "<br>");
				}

				switch (report_type) {
					case "0":
						// 中间表
						Map<String, Object> paramModel = new HashMap<>();
						paramModel.put("settle_date", reportDate);
						paramModel.put("deal_date", reportDate);

						if ("2".equals(task_type)) {
							// 每月
							paramModel.put("settle_date", nextMonthLastDay);
							paramModel.put("deal_date", nextMonthLastDay);
						}

						log.info("---------- 中间表任务重跑: " + task_id +" 数据加工开始 Start -----------");
						List<SqlRow> sqlStrs = ExeQuery.queryPortSqlByTaskId(task_id);
						for (SqlRow sqlStr : sqlStrs) {
							// 报表参数report_code替换成report_value执行
							String sqlStrString = sqlStr.getString("sqlstr");
							if (StringUtils.isNotEmpty(report_code)) {
								sqlStrString = sqlStrString.replaceAll(report_code, String.valueOf(params.get(report_value)));
							}
							super.update(sqlStrString, paramModel);
						}
						log.info("---------- 中间表任务重跑: " + task_id +" 数据加工结束 End-----------");
						break;
					case "1":
						// 一维报表
						Map<String, Object> paramOne = new HashMap<>();
						paramOne.put("settle_date", reportDate);
						paramOne.put("deal_date", reportDate);

						if ("2".equals(task_type)) {
							// 每月
							paramOne.put("settle_date", nextMonthLastDay);
							paramOne.put("deal_date", nextMonthLastDay);
						}

						log.info("---------- 一维报表任务重跑: " + task_id +" 数据加工开始 Start -----------");
						List<SqlRow> sqlStrs1 = ExeQuery.queryPortSqlByTaskId(task_id);
						for (SqlRow sqlStr : sqlStrs1) {
							// 报表参数report_code替换成report_value执行
							String sqlStrString = sqlStr.getString("sqlstr");
							if (StringUtils.isNotEmpty(report_code)) {
								sqlStrString = sqlStrString.replaceAll(report_code, String.valueOf(params.get(report_value)));
							}
							super.update(sqlStrString, paramOne);
						}
						log.info("---------- 一维报表任务重跑: " + task_id +" 数据加工结束 End-----------");
						break;
					case "2":
						// 二维报表
						log.info("---------- 二维报表任务重跑: " + task_id +" 数据加工开始 Start -----------");
						ReportDataGenUtils.reportDataHandlerProcess(task_id, reportDate);
						log.info("---------- 二维报表任务重跑: " + task_id +" 数据加工结束 End-----------");
						break;
					default:
				}
			}
		});

		return String.valueOf(stringBuilder);
	}

}
