package com.kayak.rpt.email.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.email.model.EmailTemplate;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;
import java.util.Map;

@Repository
public class EmailTemplateDao extends ComnDao {

	public SqlResult<EmailTemplate> findEmailTemplates(SqlParam<EmailTemplate> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,name,type,type_name,subject,content,dynamic_params,receiver,cc,status," +
				"create_time,create_date FROM email_template WHERE 1=1 ");
		if (StringUtils.isNotBlank(params.getModel().getName())) {
			sql.append(" and  name like '%" + params.getModel().getName() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getSubject())) {
			sql.append(" and  subject like '%" + params.getModel().getSubject() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getContent())) {
			sql.append(" and  content like '%" + params.getModel().getContent() + "%'");
		}
		sql.append(" order by id asc");
		return super.findRows(sql.toString(), params);
	}

	public UpdateResult addEmailTemplate(SqlParam<EmailTemplate> params) throws Exception {
		return super.update("INSERT INTO email_template(id,name,type,type_name,subject,content,dynamic_params,receiver,cc,status,create_time,create_date) VALUES($AUTOIDS{id},$S{name},$S{type},$S{typeName},$S{subject},$S{content},$S{dynamicParams},$S{receiver},$S{cc},$S{status},$S{createTime},$S{createDate})",
				params.getModel());
	}
	
	public UpdateResult updateEmailTemplate(SqlParam<EmailTemplate> params) throws Exception {
		return super.update("UPDATE email_template SET name=$S{name} ,type=$S{type} ,type_name=$S{typeName} ,subject=$S{subject} ,content=$S{content} ,dynamic_params=$S{dynamicParams} ,receiver=$S{receiver} ,cc=$S{cc} ,status=$S{status} ,create_time=$S{createTime} ,create_date=$S{createDate}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteEmailTemplate(SqlParam<EmailTemplate> params) throws Exception {
		return super.update("DELETE FROM email_template WHERE  id=$S{id} ",
				params.getModel());
	}

	public SqlResult<EmailTemplate> getEmailTemplateTypes(SqlParam<EmailTemplate> params) throws Exception {
		return super.findRows("SELECT id,type,type_name FROM email_template order by id asc", params);

	}

	public SqlResult<EmailTemplate> getEmailStatus(SqlParam<EmailTemplate> params) throws Exception {
		return super.findRows("SELECT id,type,type_name,status FROM email_template", params);

	}


	public List< EmailTemplate > findEmailTemplateInfo(Map<String,String> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,name,type,type_name,subject,content,dynamic_params,receiver,cc,status,create_time,create_date FROM email_template WHERE 1 ='1'");
		if (StringUtils.isNotBlank(params.get("name"))) {
			sql.append(" and  name like '%" + params.get("name") + "%'");
		}
		if (StringUtils.isNotBlank(params.get("subject"))) {
			sql.append(" and  subject like '%" + params.get("subject") + "%'");
		}
		if (StringUtils.isNotBlank(params.get("content"))) {
			sql.append(" and  content like '%" + params.get("content") + "%'");
		}
		sql.append(" order by id asc");
		return super.findRows(EmailTemplate.class, sql.toString(), DataSourceProperty.PUB, params);
	}

	public EmailTemplate findEmailTemplateById(String id) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,name,type,type_name,subject,content,dynamic_params,receiver,cc,status,create_time,create_date FROM email_template WHERE 1 ='1'");
		if (StringUtils.isNotBlank(id)) {
			sql.append(" and  id ='" + id + "'");
		}
		return super.findRow(EmailTemplate.class, sql.toString(), DataSourceProperty.PUB, id);
	}

	public EmailTemplate findEmailTemplateByBizType(String bizType) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,name,type,type_name,subject,content,dynamic_params,receiver,cc,status,create_time,create_date FROM email_template WHERE 1 ='1'");
		if (StringUtils.isNotBlank(bizType)) {
			sql.append(" and  type ='" + bizType + "'");
		}
		return super.findRow(EmailTemplate.class, sql.toString(), DataSourceProperty.PUB, bizType);
	}
}
