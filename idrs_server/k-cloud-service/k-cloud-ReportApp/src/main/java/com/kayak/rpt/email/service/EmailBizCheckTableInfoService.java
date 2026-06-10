package com.kayak.rpt.email.service;

import com.kayak.rpt.email.model.EmailBizCheckTableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.email.dao.EmailBizCheckTableInfoDao;
import com.kayak.rpt.email.model.EmailBizCheckTableInfo;

@Service
@APIDefine(desc = "邮件业务主子表关联校验服务", model = EmailBizCheckTableInfo.class)
public class EmailBizCheckTableInfoService {

	@Autowired
	private EmailBizCheckTableInfoDao emailBizCheckTableInfoDao;

	@API(desc = "查询邮件业务主子表关联校验信息", auth = APIAuth.YES)
	public SqlResult<EmailBizCheckTableInfo> findEmailBizCheckTableInfos(SqlParam<EmailBizCheckTableInfo> params) throws Exception {
		params.setMakeSql(true);
		return emailBizCheckTableInfoDao.findEmailBizCheckTableInfos(params);
	}

	@API(desc = "添加邮件业务主子表关联校验", params = "id,biz_report_table,biz_check_table_info,biz_report_sub_table,report_date,create_time,create_date", auth = APIAuth.NO)
	public int addEmailBizCheckTableInfo(SqlParam<EmailBizCheckTableInfo> params) throws Exception {
		return emailBizCheckTableInfoDao.addEmailBizCheckTableInfo(params).getEffect();
	}
	
	@API(desc = "修改邮件业务主子表关联校验", params = "id,biz_report_table,biz_check_table_info,biz_report_sub_table,report_date,create_time,create_date", auth = APIAuth.NO)
	public int updateEmailBizCheckTableInfo(SqlParam<EmailBizCheckTableInfo> params) throws Exception {
		return emailBizCheckTableInfoDao.updateEmailBizCheckTableInfo(params).getEffect();
	}
	
	@API(desc = "删除邮件业务主子表关联校验", params = "id,biz_report_table,biz_check_table_info,biz_report_sub_table,report_date,create_time,create_date", auth = APIAuth.NO)
	public int deleteEmailBizCheckTableInfo(SqlParam<EmailBizCheckTableInfo> params) throws Exception {
		return emailBizCheckTableInfoDao.deleteEmailBizCheckTableInfo(params).getEffect();
	}

}
