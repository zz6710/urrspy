package com.kayak.rpt.email.service;

import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.email.dao.EmailTemplateDao;
import com.kayak.rpt.email.dao.EmailTickRuleDao;
import com.kayak.rpt.email.model.EmailTemplate;
import com.kayak.rpt.email.model.EmailTickRule;
import com.kayak.rpt.email.util.EmailDict;
import com.spire.ms.System.Collections.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.List;

@Service
@APIDefine(desc = "邮件模板配置服务", model = EmailTemplate.class)
public class EmailTemplateService {
	private static Logger logger = LogManager.getLogger(EmailTemplateService.class);


	@Autowired
	private EmailTemplateDao emailTemplateDao;

	@Autowired
	private EmailTickRuleDao emailTickRuleDao;


	@API(desc = "查询邮件模板配置信息", auth = APIAuth.YES)
	public SqlResult<EmailTemplate> findEmailTemplates(SqlParam<EmailTemplate> params) throws Exception {
		params.setMakeSql(true);
		return emailTemplateDao.findEmailTemplates(params);
	}

	@API(desc = "查询已启用的邮件模板配置信息", auth = APIAuth.YES)
	public SqlResult<EmailTemplate> findEffectEmailTemplates(SqlParam<EmailTemplate> params) throws Exception {
		params.getModel().setStatus(EmailDict.EffectStatus.ON_1);
		params.setMakeSql(true);
		return emailTemplateDao.findEmailTemplates(params);
	}

	@API(desc = "添加邮件模板配置", params = "id,name,type,type_name,subject,content,dynamic_params,receiver,cc,status,create_time,create_date", auth = APIAuth.YES)
	public int addEmailTemplate(SqlParam<EmailTemplate> params) throws Exception {
		params.getModel().setCreateTime(DateUtil.getNowTime());
		params.getModel().setCreateDate(DateUtil.getNowDate());
		return emailTemplateDao.addEmailTemplate(params).getEffect();
	}
	
	@API(desc = "修改邮件模板配置", params = "id,name,type,type_name,subject,content,dynamic_params,receiver,cc,status,create_time,create_date", auth = APIAuth.YES)
	public int updateEmailTemplate(SqlParam<EmailTemplate> params) throws Exception {
		params.getModel().setCreateTime(DateUtil.getNowTime());
		params.getModel().setCreateDate(DateUtil.getNowDate());
		int count = emailTemplateDao.updateEmailTemplate(params).getEffect();
		if(count>0){
			EmailTickRule rule =new EmailTickRule();
			rule.setTemplateId(params.getModel().getId());
			rule.setTemplateName(params.getModel().getName());
			rule.setBizTable(params.getModel().getType());
			rule.setBizName(params.getModel().getTypeName());
			//更新关联的模板名称及业务类型
			emailTickRuleDao.updateEmailTickRuleTemplateInfo(rule);
		}
		return count;
	}
	@API(desc = "修改邮件模板状态为启用", params = "id,name,status", auth = APIAuth.YES)
	public int updateEmailTemplateStatusOn(SqlParam<EmailTemplate> params) throws Exception {
//		params.getModel().setCreateTime(DateUtil.getNowTime());
//		params.getModel().setCreateDate(DateUtil.getNowDate());
		params.getModel().setStatus(EmailDict.EffectStatus.ON_1);
		return emailTemplateDao.updateEmailTemplate(params).getEffect();
	}
	@API(desc = "修改邮件模板状态为停用", params = "id,name,status", auth = APIAuth.YES)
	public int updateEmailTemplateStatusOff(SqlParam<EmailTemplate> params) throws Exception {
//		params.getModel().setCreateTime(DateUtil.getNowTime());
//		params.getModel().setCreateDate(DateUtil.getNowDate());
		params.getModel().setStatus(EmailDict.EffectStatus.OFF_0);
		return emailTemplateDao.updateEmailTemplate(params).getEffect();
	}
	@API(desc = "删除邮件模板配置", params = "id,name,type,type_name,subject,content,dynamic_params,receiver,cc,status,create_time,create_date", auth = APIAuth.YES)
	public String deleteEmailTemplate(SqlParam<EmailTemplate> params) throws Exception {
		//删除邮件模板信息，先校验规则中是否有已启用的记录，有则无法删除
		EmailTickRule etr = new EmailTickRule();
		etr.setTemplateId(params.getModel().getId());
//		etr.setRuleStatus("1");
		EmailTickRule etrResult = emailTickRuleDao.findEmailTickRuleSingle(etr);
		if(etrResult!=null){
//            throw new Exception("该模板有启用状态规则绑定，不可删除。模板编号:"+params.getModel().getId());
			return RequestSupport.updateReturnJson(false, "删除失败，该模板有对应的规则绑定，不可删除。模板编号：" + params.getModel().getId(), null).toString();

		}else{
			emailTemplateDao.deleteEmailTemplate(params);
		}
		return RequestSupport.updateReturnJson(true, "删除成功" , null).toString();
	}

	@API(desc = "获取启用状态的邮件模板", params = "id,name,type,type_name,status", auth = APIAuth.YES)
	public SqlResult<EmailTemplate> getEmailStatus(SqlParam<EmailTemplate> params){
		SqlResult<EmailTemplate> sqlResult =new SqlResult<>();
		List<EmailTemplate> tmList = new ArrayList();
		EmailTemplate tmp = new EmailTemplate();
		tmp.setStatus(EmailDict.EffectStatus.ON_1);
		tmp.setStatusName(EmailDict.EffectStatusName.ON_1);
		tmList.add(tmp);
		tmp.setStatus(EmailDict.EffectStatus.OFF_0);
		tmp.setStatusName(EmailDict.EffectStatusName.OFF_0);
		tmList.add(tmp);
		sqlResult.setRows(tmList);
		return sqlResult;
	}

}
