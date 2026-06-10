package com.kayak.rpt.email.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.email.model.EmailBizCheckTableInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class EmailBizCheckTableInfoDao extends ComnDao {

	public SqlResult<EmailBizCheckTableInfo> findEmailBizCheckTableInfos(SqlParam<EmailBizCheckTableInfo> params) throws Exception {
		return super.findRows("SELECT id,biz_report_table,biz_check_table_info,biz_report_sub_table,report_date,create_time,create_date FROM email_biz_check_table_info", params);
	}

	public UpdateResult addEmailBizCheckTableInfo(SqlParam<EmailBizCheckTableInfo> params) throws Exception {
		return super.update("INSERT INTO email_biz_check_table_info(id,biz_report_table,biz_check_table_info,biz_report_sub_table,report_date,create_time,create_date) VALUES($AUTOIDI{id},$S{bizReportTable},$S{bizCheckTableInfo},$S{bizReportSubTable},$S{reportDate},$S{createTime},$S{createDate})",
				params.getModel());
	}
	
	public UpdateResult updateEmailBizCheckTableInfo(SqlParam<EmailBizCheckTableInfo> params) throws Exception {
		return super.update("UPDATE email_biz_check_table_info SET biz_report_table=$S{bizReportTable} ,biz_check_table_info=$S{bizCheckTableInfo} ,biz_report_sub_table=$S{bizReportSubTable} ,report_date=$S{reportDate} ,create_time=$S{createTime} ,create_date=$S{createDate}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteEmailBizCheckTableInfo(SqlParam<EmailBizCheckTableInfo> params) throws Exception {
		return super.update("DELETE FROM email_biz_check_table_info WHERE  id=$I{id} ",
				params.getModel());
	}

}
