package com.kayak.rpt.email.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.email.model.AppProdBondPropertyInfoVo;
import com.kayak.rpt.email.model.EmailBizTask;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Repository
public class EmailBizTaskDao extends ComnDao {

	public SqlResult<EmailBizTask> findEmailBizTasks(SqlParam<EmailBizTask> params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,biz_type,biz_name,biz_date,remote_path,biz_table,biz_table_method,biz_status,task_flag,status,create_time,create_date FROM email_biz_task WHERE 1=1 ");
		if (StringUtils.isNotBlank(params.getModel().getId())) {
			sql.append(" and  id =" + params.getModel().getId());
		}
		if (StringUtils.isNotBlank(params.getModel().getStatus())) {
			sql.append(" and  status =" + params.getModel().getStatus());
		}
		if (StringUtils.isNotBlank(params.getModel().getBizDate())) {
			sql.append(" and  biz_date =" + params.getModel().getBizDate());
		}
		if (StringUtils.isNotBlank(params.getModel().getTaskFlag())) {
			sql.append(" and  task_flag =" + params.getModel().getTaskFlag());
		}
		if (StringUtils.isNotBlank(params.getModel().getBizStatus())) {
			sql.append(" and  biz_status =" + params.getModel().getBizStatus());
		}
		if (StringUtils.isNotBlank(params.getModel().getBizType())) {
			sql.append(" and  biz_type =" + params.getModel().getBizType());
		}
		if (StringUtils.isNotBlank(params.getModel().getBizName())) {
			sql.append(" and  biz_name like '%" + params.getModel().getBizName() + "%'");
		}
		if (StringUtils.isNotBlank(params.getModel().getBizTable())) {
			sql.append(" and  biz_table like '%" + params.getModel().getBizTable() + "%'");
		}
//		sql.append(" order by id desc");
		return super.findRows(sql.toString(), params);
	}

	public List<EmailBizTask> findEmailBizTaskList(EmailBizTask params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,biz_type,biz_name,biz_date,remote_path,biz_table,biz_table_method,biz_status,task_flag,status,create_time,create_date FROM email_biz_task WHERE 1=1 ");
		if (StringUtils.isNotBlank(params.getId())) {
			sql.append(" and  id =" + params.getId());
		}
		if (StringUtils.isNotBlank(params.getStatus())) {
			sql.append(" and  status =" + params.getStatus());
		}
		if (StringUtils.isNotBlank(params.getTaskFlag())) {
			sql.append(" and  task_flag =" + params.getTaskFlag());
		}
		if (StringUtils.isNotBlank(params.getBizStatus())) {
			sql.append(" and  biz_status =" + params.getBizStatus());
		}
		if (StringUtils.isNotBlank(params.getBizType())) {
			sql.append(" and  biz_type =" + params.getBizType());
		}
		if (StringUtils.isNotBlank(params.getBizName())) {
			sql.append(" and  biz_name like '%" + params.getBizName() + "%'");
		}
		if (StringUtils.isNotBlank(params.getBizTable())) {
			sql.append(" and  biz_table like '%" + params.getBizTable() + "%'");
		}
//		sql.append(" order by id desc");
		return super.findRows(EmailBizTask.class,sql.toString(), DataSourceProperty.PUB ,params);
	}
	public UpdateResult addEmailBizTask(SqlParam<EmailBizTask> params) throws Exception {

		return super.update("INSERT INTO email_biz_task(id,biz_type,biz_name,biz_date,remote_path,biz_table,biz_table_method,biz_status,task_flag,status,create_time,create_date) VALUES($AUTOIDS{id},$S{bizType},$S{bizName},$S{bizDate},$S{remotePath},$S{bizTable},$S{bizTableMethod},$S{bizStatus},$S{taskFlag},$S{status},$S{createTime},$S{createDate})",
				params.getModel());
	}
	
	public UpdateResult updateEmailBizTask(SqlParam<EmailBizTask> params) throws Exception {
		return super.update("UPDATE email_biz_task SET biz_type=$S{bizType} ,biz_name=$S{bizName} ,biz_date=$S{bizDate} ,remote_path=$S{remotePath} ,biz_table=$S{bizTable} ,biz_table_method=$S{bizTableMethod} ,biz_status=$S{bizStatus} ,task_flag=$S{taskFlag} ,status=$S{status} ,create_time=$S{createTime} ,create_date=$S{createDate}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteEmailBizTask(SqlParam<EmailBizTask> params) throws Exception {
		return super.update("DELETE FROM email_biz_task WHERE  id=$S{id} ",
				params.getModel());
	}
	public UpdateResult deleteEmailBizTaskById(String id) throws Exception {
		return super.update("DELETE FROM email_biz_task WHERE  id= '"+id+"'",null);
	}


	/**
	 * 验证从此处查询其他表，是否有可行
	 */
	public List<AppProdBondPropertyInfoVo>  getAppProdBondPropertyInfoVoList(AppProdBondPropertyInfoVo params) throws Exception {
			StringBuilder sql = new StringBuilder("SELECT id,report_date,bond_code,bond_qntt,bond_qntt_xp,issr_nm,quota_occ,moneyofproperty,prod_name,rate,bond_face FROM app_prod_bond_property_info WHERE 1=1 ");

		if (StringUtils.isNotBlank(params.getId())) {
			sql.append(" and  id =" + params.getId());
		}
		if (StringUtils.isNotBlank(params.getReportDate())) {
			sql.append(" and  report_date = '" + params.getReportDate()+"'");
		}
		if (StringUtils.isNotBlank(params.getBondCode())) {
			sql.append(" and  bond_code ='" + params.getBondCode()+"'");
		}
		if (StringUtils.isNotBlank(params.getIssrNm())) {
			sql.append(" and  issr_nm = '" + params.getIssrNm()+"'");
		}
		sql.append(" order by id asc");
		return super.findRows(AppProdBondPropertyInfoVo.class,sql.toString(), DataSourceProperty.PUB ,params);
	}

	public List<SqlRow>  getAppProdBondPropertyInfoVoSqlList(AppProdBondPropertyInfoVo params) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT id,report_date,bond_code,bond_qntt,bond_qntt_xp,issr_nm,quota_occ,moneyofproperty,prod_name,rate,bond_face FROM app_prod_bond_property_info WHERE 1=1 ");

		if (StringUtils.isNotBlank(params.getId())) {
			sql.append(" and  id =" + params.getId());
		}
		if (StringUtils.isNotBlank(params.getReportDate())) {
			sql.append(" and  report_date = '" + params.getReportDate()+"'");
		}
		if (StringUtils.isNotBlank(params.getBondCode())) {
			sql.append(" and  bond_code = '" + params.getBondCode()+"'");
		}
		if (StringUtils.isNotBlank(params.getIssrNm())) {
			sql.append(" and  issr_nm = '" + params.getIssrNm()+"'");
		}
		sql.append(" order by id asc");
		return super.findRows(sql.toString(), params);
	}

}
