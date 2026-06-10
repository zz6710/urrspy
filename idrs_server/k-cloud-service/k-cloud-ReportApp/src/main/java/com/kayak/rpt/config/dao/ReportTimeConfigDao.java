package com.kayak.rpt.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.config.model.ReportTimeConfig;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportTimeConfigDao extends ComnDao {

	public SqlResult<ReportTimeConfig> findReportTimeConfigs(SqlParam<ReportTimeConfig> params) throws Exception {
		String sql = "SELECT id,report_type,report_table,base_type,data_type," +
				"            (case when SUBSTR(inner_submission_time_require,1,1)='-' then '1' else '2' end) as inner_submission_time," +
				"            replace(inner_submission_time_require,'-','') as inner_submission_time_require," +
				"            (case when SUBSTR(supervise_submission_time_require,1,1)='-' then '1' else '2' end) as supervise_submission_time," +
				"            replace(supervise_submission_time_require,'-','') as supervise_submission_time_require," +
				"            (case when SUBSTR(data_gener_time_require,1,1)='-' then '1' else '2' end) as data_gener_time," +
				"            replace(data_gener_time_require,'-','') as data_gener_time_require,create_date,update_date,time_type " +
				"       FROM base_submission_time_config " +
				"      where 1=1";
		if (Strings.isNotBlank(params.getModel().getTableName())) {
			sql += " and report_table in  (select report_table from  base_report_info where report_table like '%$U{tableName}%' )";
		}

		sql += " order by update_date desc";
		return super.findRows(sql, params);
	}

	/**
	 * 获取非规则配置的截止日期列表
	 * @param  tableName 表名称
	 * @return
	 * @throws Exception
	 */
	public String getEndDateList(String tableName) throws Exception{
		StringBuilder result = new StringBuilder();
		String strSql = "select end_date from report_time_type_info where report_table = $S{tableName}";
		List<SqlRow> rows = super.findRows(strSql, tableName);
		if(rows != null && rows.size()>0){
			for(int i=0; i<rows.size(); i++){
				String endDate = rows.get(i).getString("end_date");
				result.append(endDate);
				if(i + 1 != rows.size()){
					result.append(",");
				}
			}
		}

		return result.toString();
	}

	public UpdateResult addReportTimeConfig(SqlParam<ReportTimeConfig> params) throws Exception {
		this.flashEndDataList(params.getModel()); //刷新非规则配置的截止日期
		return super.update("INSERT INTO base_submission_time_config(report_type,report_table,base_type,data_type,inner_submission_time_require,supervise_submission_time_require,data_gener_time_require,create_date,update_date,time_type) VALUES($S{reportType},$S{reportTable},$S{baseType},$S{dataType},$S{innerSubmissionTimeRequire},$S{superviseSubmissionTimeRequire},$S{dataGenerTimeRequire},date_format(CURDATE(),'%Y%m%d'),date_format(CURDATE(),'%Y%m%d'),$S{timeType})",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateReportTimeConfig(SqlParam<ReportTimeConfig> params) throws Exception {
		this.flashEndDataList(params.getModel()); //刷新非规则配置的截止日期
		return super.update("UPDATE base_submission_time_config SET report_type=$S{reportType} ,report_table=$S{reportTable} ,base_type=$S{baseType} ,data_type=$S{dataType} ,inner_submission_time_require=$S{innerSubmissionTimeRequire} ,supervise_submission_time_require= $S{superviseSubmissionTimeRequire},data_gener_time_require=$S{dataGenerTimeRequire},update_date=date_format(CURDATE(),'%Y%m%d'),time_type = $S{timeType}  WHERE  id=$S{id} ",
				DataSourceProperty.PUB,params.getModel());
	}

	/**
	 * 刷新非规则配置截止日期
	 * @param config
	 * @return
	 */
	public int flashEndDataList(ReportTimeConfig config) throws Exception{
		String timeType = config.getTimeType();
		int result = 0;
		if("1".equals(timeType)){ //当为非规则配置时，进行数据处理
			String strEndList = config.getEndDateString();
			if(strEndList != null && strEndList.length() > 0){
				String[] endList = strEndList.split(","); //进行数据分割
				if(endList.length > 0){
					//删除这一年配置的数据 /*substr(end_date,1,4) = substr('"+endList[0]+"',1,4) and*/
					String strDel = "delete from report_time_type_info where substr(end_date,1,4) = substr('"+endList[0]+"',1,4) and report_table = '"+config.getReportTable()+"'";
					super.update(strDel, null);

					//插入配置的数据
					for(int i =0;i <endList.length; i++){
						String strInsert = "insert into report_time_type_info(report_type,report_table,end_date,create_time) values('"+config.getReportType()+"','"+config.getReportTable()+"','"+endList[i]+"',now())";
						super.update(strInsert, null); //批量插入
						++result;
					}
				}
			}
		}

		return result;
	}

	public UpdateResult deleteReportTimeConfig(SqlParam<ReportTimeConfig> params) throws Exception {
		return super.update("DELETE FROM base_submission_time_config WHERE  id=$S{id} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public SqlResult<ReportTimeConfig> getReportTable(SqlParam<ReportTimeConfig> params) throws Exception {
		String sql = " select report_table ,table_name  from base_report_info where 1=1";
		if (Strings.isNotBlank(params.getModel().getReportType())) {
			sql += " and report_catgory = $S{reportType}";
		}
		if (Strings.isNotBlank(params.getModel().getReportTable())) {
			sql += " and report_table = $S{reportTable}";
		}
		return super.findRows(sql, DataSourceProperty.PUB, params);

	}

	/**配置了指标校验的报表**/
	public SqlResult<ReportTimeConfig> getValidReportTable(SqlParam<ReportTimeConfig> params) throws Exception {
		String sql = " select report_table ,table_name  from base_report_info where 1=1";
		if (Strings.isNotBlank(params.getModel().getReportType())) {
			sql += " and report_catgory = $S{reportType} and report_table in (select distinct report_table  from base_reportdata_index_config) ";
		}else{
			sql += " and report_table in (select distinct report_table  from base_reportdata_index_config) ";
		}
		if (Strings.isNotBlank(params.getModel().getReportTable())) {
			sql += " and report_table = $S{reportTable}";
		}
		return super.findRows(sql, DataSourceProperty.PUB, params);

	}

	public SqlRow findChineseReportName(ReportTimeConfig reportTimeConfig) throws Exception {
		String sql = "select table_name from base_report_info where report_table = $S{reportTable}";
		return super.findRow(sql, DataSourceProperty.PUB, reportTimeConfig);
	}

	public SqlResult<ReportTimeConfig> getReportTimeConfig(SqlParam<ReportTimeConfig> params) throws Exception {
		String sql = " select report_table from base_submission_time_config where 1=1";
		if (Strings.isNotBlank(params.getModel().getReportTable())) {
			sql += " and report_table = $S{reportTable}";
		}
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}
}
