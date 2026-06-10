package com.kayak.rpt.email.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.email.model.EmailTickRule;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class EmailTickRuleDao extends ComnDao {

	public SqlResult<EmailTickRule> findEmailTickRules(SqlParam<EmailTickRule> params) throws Exception {
//		return super.findRows("SELECT id,rule_name,biz_table,biz_name,t2.template_id,template_name,rule_unit,rule_seprate,rule_tick,rule_status,create_time,create_date,update_time,update_date FROM email_tick_rule left join email_template t2 on t2.id = template_id order by id asc", params);
		return super.findRows("SELECT t.id,t.rule_name,t.biz_table,t.biz_name,t2.id as template_id,t.template_name,t.rule_unit,t.rule_seprate,t.rule_tick,t.rule_status,t.create_time,t.create_date,t.update_time,t.update_date FROM email_tick_rule t left join email_template t2 on t2.id = t.template_id order by t.id asc", params);
	}

	/**
	 * 只能获取到一条数据，若有业务调整，需改写此方法
	 * @param params
	 * @return
	 */
	public EmailTickRule findEmailTickRuleSingle(EmailTickRule params) {
		StringBuilder sql = new StringBuilder("SELECT id,rule_name,biz_table,biz_name,template_id,template_name,rule_unit,rule_seprate,rule_tick,rule_status,create_time,create_date,update_time,update_date FROM email_tick_rule  where 1=1 ");
		if(StringUtils.isNotEmpty(params.getId())){
			sql.append(" and id = "+params.getId());
		}
		if(StringUtils.isNotEmpty(params.getBizTable())){
			sql.append(" and biz_table = '"+params.getBizTable()+"'");
		}
		if(StringUtils.isNotEmpty(params.getBizName())){
			sql.append(" and biz_name = '"+params.getBizName()+"'");
		}
		if(StringUtils.isNotEmpty(params.getTemplateId())){
			sql.append(" and template_id = '"+params.getTemplateId()+"'");
		}
		if(StringUtils.isNotEmpty(params.getRuleStatus())){
			sql.append(" and rule_status = '"+params.getRuleStatus()+"'");
		}
		sql.append(" order by id desc limit 1");
		try {
			return super.findRow(EmailTickRule.class,sql.toString(), DataSourceProperty.PUB, params);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("EmailTickRuleDao.findEmailTickRuleSingle error params:"+params+" Exception:{}",e.getMessage() +";sql:"+sql.toString());
		}
		return null;
	}

	/**
	 * 规则表存有模板名称的情况下，从此处取模板信息 会因为修改而滞后
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<EmailTickRule> findEmailTemplateInfo(SqlParam<EmailTickRule> params) throws Exception {
		return super.findRows("SELECT distinct template_id,template_name FROM email_tick_rule order by template_id asc", params);
	}

	public UpdateResult addEmailTickRule(SqlParam<EmailTickRule> params) throws Exception {
		params.getModel().setCreateDate(DateUtil.getNowDate());
		params.getModel().setCreateTime(DateUtil.getNowTime());

		return super.update("INSERT INTO email_tick_rule(id,rule_name,biz_table,biz_name,template_id,template_name,rule_unit,rule_seprate,rule_tick,rule_status,create_time,create_date,update_time,update_date) VALUES($AUTOIDS{id},$S{ruleName},$S{bizTable},$S{bizName},$S{templateId},$S{templateName},$S{ruleUnit},$S{ruleSeprate},$S{ruleTick},$S{ruleStatus},$S{createTime},$S{createDate},$S{updateTime},$S{updateDate})",
				params.getModel());
	}
	
	public UpdateResult updateEmailTickRule(SqlParam<EmailTickRule> params) throws Exception {
		params.getModel().setUpdateTime(DateUtil.getNowTime());
		params.getModel().setUpdateDate(DateUtil.getNowDate());
		return super.update("UPDATE email_tick_rule SET rule_name=$S{ruleName} ,biz_table=$S{bizTable} ,biz_name=$S{bizName} ,template_id=$S{templateId} ,template_name=$S{templateName} ,rule_unit=$S{ruleUnit} ,rule_seprate=$S{ruleSeprate} ,rule_tick=$S{ruleTick} ,rule_status=$S{ruleStatus} ,create_time=$S{createTime} ,create_date=$S{createDate} ,update_time=$S{updateTime} ,update_date=$S{updateDate}  WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteEmailTickRule(SqlParam<EmailTickRule> params) throws Exception {
		return super.update("DELETE FROM email_tick_rule WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult updateEmailTickRuleTemplateInfo(EmailTickRule params) throws Exception {
		params.setUpdateTime(DateUtil.getNowTime());
		params.setUpdateDate(DateUtil.getNowDate());
		StringBuilder sql = new StringBuilder("UPDATE email_tick_rule SET biz_table=$S{bizTable} ,biz_name=$S{bizName} ,template_name=$S{templateName} ,update_time=$S{updateTime} ,update_date=$S{updateDate}  WHERE  template_id = '"+params.getTemplateId()+"'");
		return super.update(sql.toString(),params);
	}
}
