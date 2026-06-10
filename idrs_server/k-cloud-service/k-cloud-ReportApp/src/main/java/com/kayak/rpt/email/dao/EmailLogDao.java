package com.kayak.rpt.email.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.email.model.EmailLog;
import com.kayak.rpt.email.util.EmailDict;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class EmailLogDao extends ComnDao {

	public SqlResult<EmailLog> findEmailLogs(SqlParam<EmailLog> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,email_log_no,business_no,business_type,report_date,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time FROM email_log  WHERE 1=1 ");
		if (StringUtils.isNotBlank(params.getModel().getSendDate())) {
			sql.append(" and  send_date= "+ params.getModel().getSendDate());
		}
		if (StringUtils.isNotBlank(params.getModel().getSendStatus())) {
			sql.append(" and  send_status = "+ params.getModel().getSendStatus());
		}
		if (StringUtils.isNotBlank(params.getModel().getEmailSubject())) {
			sql.append(" and  email_subject like '%" + params.getModel().getEmailSubject() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getReceiver())) {
			sql.append(" and  receiver like '%" + params.getModel().getReceiver() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getCc())) {
			sql.append(" and  cc like '%" + params.getModel().getCc() + "%'");
		}

		sql.append(" order by id  desc ");
		return super.findRows(sql.toString(), params);
	}

	public EmailLog findEmailLogById(String id) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,email_log_no,business_no,business_type,report_date,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time FROM email_log where 1=1");
		if (StringUtils.isNotBlank(id)) {
			sql.append(" and  id ='" + id + "'");
		}
		return super.findRow(EmailLog .class, sql.toString(), DataSourceProperty.PUB, id);
	}

	/**
	 *
	 * @param ids 多个id用”,“分割
	 * @return
	 * @throws Exception
	 */
	public List<EmailLog> findEmailLogByIds(String ids) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,email_log_no,business_no,business_type,report_date,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time FROM email_log where 1=1");
		if (StringUtils.isNotBlank(ids)) {
			String[] idArr = ids.split(EmailDict.SymbolType.T_COMMA);
			if(idArr.length==1){
				sql.append(" and  id = " + ids);
			}else{
				sql.append(" and  id in(" + ids + ")");
			}
		}
		return super.findRows(EmailLog .class, sql.toString(), DataSourceProperty.PUB, null);
	}

	public UpdateResult addEmailLog(SqlParam<EmailLog> params) throws Exception {
		return super.update("INSERT INTO email_log(id,email_log_no,business_no,business_type,report_date,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time) VALUES($AUTOIDS{id},$S{emailLogNo},$S{businessNo},$S{businessType},$S{reportDate},$S{sender},$S{receiver},$S{cc},$S{emailSubject},$S{emailBody},$S{attachName},$S{filePath},$S{failReason},$S{sendStatus},$S{sendDate},$S{sendTime})",
				params.getModel());
	}

	public UpdateResult addEmailLogByMapParam(Map<String,Object> params) throws Exception {
		return super.update("INSERT INTO email_log(id,email_log_no,business_no,business_type,report_date,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time) VALUES($AUTOIDS{id},$S{emailLogNo},$S{businessNo},$S{businessType},$S{reportDate},$S{sender},$S{receiver},$S{cc},$S{emailSubject},$S{emailBody},$S{attachName},$S{filePath},$S{failReason},$S{sendStatus},$S{sendDate},$S{sendTime})",
				params);
	}

	public UpdateResult addEmailLogByModel(EmailLog emailLog) throws Exception {
		emailLog.setSendTime(DateUtil.getNowTime());
		emailLog.setSendDate(DateUtil.getNowDate());
		return super.update("INSERT INTO email_log(id,email_log_no,business_no,business_type,report_date,sender,receiver,cc,email_subject,email_body,attach_name,file_path,fail_reason,send_status,send_date,send_time) VALUES($AUTOIDS{id},$S{emailLogNo},$S{businessNo},$S{businessType},$S{reportDate},$S{sender},$S{receiver},$S{cc},$S{emailSubject},$S{emailBody},$S{attachName},$S{filePath},$S{failReason},$S{sendStatus},$S{sendDate},$S{sendTime})",
				emailLog);
	}


	public UpdateResult updateEmailLog(SqlParam<EmailLog> params) throws Exception {
		return super.update("UPDATE email_log SET email_log_no=$S{emailLogNo} ,business_no=$S{businessNo} ,business_type=$S{businessType} ,report_date=$S{reportDate} ,sender=$S{sender} ,receiver=$S{receiver} ,cc=$S{cc} ,email_subject=$S{emailSubject} ,email_body=$S{emailBody} ,attach_name=$S{attachName} ,file_path=$S{filePath} ,fail_reason=$S{failReason} ,send_status=$S{sendStatus} ,send_date=$S{sendDate} ,send_time=$S{sendTime}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteEmailLog(SqlParam<EmailLog> params) throws Exception {
		return super.update("DELETE FROM email_log WHERE  id=$S{id} ",
				params.getModel());
	}

}
