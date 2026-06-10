package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class DealReportTimingDao extends ComnDao {

	@Resource(name = "comnDao")
	private ComnDao comnDao;

	public List<SqlRow> getReportTableInfo(Map<String, Object> params) throws Exception {
		return comnDao.findRows("SELECT distinct report_table,base_type,data_type,(case when SUBSTR(supervise_submission_time_require,1,1)='-' then '1' else '2' end) as supervise_submission_time,replace(supervise_submission_time_require,'-','') as supervise_submission_time_require FROM base_submission_time_config order by report_type ", DataSourceProperty.PUB,params);
	}

	public String getWeekdayByIntervalDays(Map<String, Object> params,int intervalDays) throws Exception {
		String sqlStr2 = "select max(workday) theory_date from (SELECT workday  FROM sys_workday_set WHERE workday > $S{theoryReportStartDate} limit  "+intervalDays+") wd ";
		SqlRow resultRow2 = comnDao.findRow(sqlStr2, DataSourceProperty.PUB, params);
		String theoryReportEndDate ="";
		if(resultRow2!= null)
			theoryReportEndDate = resultRow2.getString("theory_date");
		return  theoryReportEndDate;
	}


	public void updatetheoryReportEndDate(Map<String, Object> params)  throws Exception {
        String sql = "update "+params.get("reportTable")+" set theory_report_end_date = $S{theoryReportEndDate} where THEORY_REPORT_START_DATE = $S{theoryReportStartDate}";
		comnDao.update(sql, DataSourceProperty.PUB,params);
	}

	public void updatetheoryReportEndDate01(Map<String, Object> params)  throws Exception {
		String sql = "update "+params.get("reportTable")+" set theory_report_end_date = $S{theoryReportEndDate} where report_date = $S{theoryReportStartDate}";
		comnDao.update(sql, DataSourceProperty.PUB,params);
	}

	public List<SqlRow> getDealDateList(Map<String, Object> params) throws Exception {
		return comnDao.findRows("select a.day as dealDate from (select curdate() - interval (a.a+10*b.a) + (100*c.a) + (1000*d.a) Day as day from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as d) a where a.day between '"+params.get("startDate")+"' and '"+params.get("endDate")+"'  order by a.dealDate", DataSourceProperty.PUB,params);
	}

}
