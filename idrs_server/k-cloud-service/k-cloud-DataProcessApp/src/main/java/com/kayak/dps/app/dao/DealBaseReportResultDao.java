package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class DealBaseReportResultDao extends ComnDao {

	@Resource(name = "comnDao")
	private ComnDao comnDao;

	public List<SqlRow> getBaseReportData(Map<String, Object> params) throws Exception {
		return comnDao.findRows("select report_table,report_catgory,sys_ref_table,table_name from base_report_info where is_report ='1' order by report_catgory  ", DataSourceProperty.PUB,params);
	}


	public void addReportResrltByDealDate(Map<String, Object> params) throws Exception {
		String sysDataStatus ="";
		// 版本问题
		String containSql = "select  * from information_schema.`COLUMNS` c where TABLE_NAME =$S{report_table} and COLUMN_NAME ='sys_data_status' ";
		List<SqlRow> rows_excel = comnDao.findRows(containSql,params);
		if(rows_excel.size()>0){
			sysDataStatus = " and sys_data_status ='1' ";
		}
		String sql = "";
		if(!StringUtils.isEmpty(params.get("specialTB").toString())){
			sql = "insert into base_report_result(report_type,report_table,report_table_name,theory_report_start_date,theory_report_end_date,register_date" +
					",total,report_success_number,status,create_date,create_time) " +
					"select " +
					" $S{report_type} " +
					",$S{report_table} " +
					",$S{table_name} " +
					",$S{theoryReportStartDate} " +
					",$S{theory_report_end_date}" +
					",$S{theoryReportStartDate} "+
					",$S{data_num} " +
					",'0' "+
					",'2' " +
					",date_format(now(),'%Y%m%d') as create_date " +
					",date_format(now(),'%H%i%s') as create_time " +
					" from dual" ;
		}else{
			sql = "insert into base_report_result(report_type,report_table,report_table_name,theory_report_start_date,theory_report_end_date,register_date" +
					",total,report_success_number,status,create_date,create_time) " +
					"select " +
					" $S{report_type} " +
					",$S{report_table} " +
					",$S{table_name} " +
					",$S{theoryReportStartDate} " +
					",(select max(theory_report_end_date) as theory_report_end_date from $U{report_table} where theory_report_start_date=$S{theoryReportStartDate} "+sysDataStatus+") " +
					",(select max(register_date) as register_date from $U{report_table} where theory_report_start_date=$S{theoryReportStartDate} "+sysDataStatus+") " +
					",$S{data_num} " +
					",(select count(1) data_num from $U{report_table} where theory_report_start_date=$S{theoryReportStartDate} and register_status='3')"+
					",(case when (select count(1) data_num from $U{report_table} where theory_report_start_date=$S{theoryReportStartDate} and register_status='3')>0 then '1' else '2' end) " +
					",date_format(now(),'%Y%m%d') as create_date " +
					",date_format(now(),'%H%i%s') as create_time " +
					" from dual" ;
		}
		comnDao.update(sql, DataSourceProperty.PUB,params);
	}

	public void addReportResrltByRegist(Map<String, Object> params) throws Exception {
		String sql = "insert into base_report_result(report_type,report_table,report_table_name,theory_report_start_date,theory_report_end_date,register_date" +
				",prod_reg_enc,total,report_success_number,status,create_date,create_time) " +
				"select " +
				" $S{report_type} " +
				",$S{report_table} " +
				",$S{table_name} " +
				",$S{theoryReportStartDate} " +
				",theory_report_end_date " +
				",register_date " +
				",NULL" +
				",'1' " +
				",(case when register_status='3' then '1' else '0' end) "+
				",(case when register_status='3' then '1' else '2' end) " +
				",date_format(now(),'%Y%m%d') as create_date " +
				",date_format(now(),'%H%i%s') as create_time " +
				" from app_prod_regist_filing_info " +
				" where theory_report_start_date=$S{theoryReportStartDate} and sys_data_status ='1' " +
				"   and ident_code not in(select prod_reg_enc from base_report_result where report_table='app_prod_regist_filing_info' and theory_report_start_date=$S{theoryReportStartDate} and status='1')" ;
		comnDao.update(sql, DataSourceProperty.PUB,params);
	}

	public void deleteReportResrltByDealDate(Map<String, Object> params) throws Exception {
		String sql = "delete from base_report_result where theory_report_start_date = DATE_FORMAT($S{theoryReportStartDate},'%Y%m%d') and status='2' ";
		 comnDao.update(sql, DataSourceProperty.PUB,params);
	}

	public List<SqlRow> getDealDateList(Map<String, Object> params) throws Exception {

		return comnDao.findRows("select a.day as dealDate from (select curdate() - interval (a.a+10*b.a) + (100*c.a) + (1000*d.a) Day as day from (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as a cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as b cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as c cross join (select 0 as a union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) as d) a where a.day between '"+params.get("startDate")+"' and '"+params.get("endDate")+"'  order by a.dealDate", DataSourceProperty.PUB,params);
	}

	public List<SqlRow> getReportNum(Map<String, Object> params) throws Exception {
		return comnDao.findRows("select count(1) data_num from $U{report_table} where theory_report_start_date=$S{theoryReportStartDate}  ", DataSourceProperty.PUB,params);
	}
	public List<SqlRow> getSpecialReportNum(Map<String, Object> params) throws Exception {
		return comnDao.findRows("select total_num as data_num from base_port_file_log  where deal_date=$S{theoryReportStartDate} and  port_code =$S{specialTB} order by crt_time desc limit 1", DataSourceProperty.PUB,params);
	}
	public boolean isReportResult(Map<String, Object> params) throws Exception {
		List<SqlRow> list = comnDao.findRows("select report_table from base_report_result where theory_report_start_date=$S{theoryReportStartDate} and report_table=$S{report_table} ", DataSourceProperty.PUB,params);
		if (list.size()>0) {
			return false;
		}
		return true;
	}
}
