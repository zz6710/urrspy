package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeVersionValueDao;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeProcess;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersionValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "信披公告版本数据表服务", model = DisclosureNoticeVersionValue.class)
public class DisclosureNoticeVersionValueService {

	@Autowired
	private DisclosureNoticeVersionValueDao disclosureNoticeVersionValueDao;

	@API(desc = "查询信披公告版本数据表信息", auth = APIAuth.YES)
	public SqlResult<DisclosureNoticeVersionValue> findDisclosureNoticeVersionValues(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		params.setMakeSql(true);
		return disclosureNoticeVersionValueDao.findDisclosureNoticeVersionValues(params);
	}

	@API(desc = "添加信披公告版本数据表", params = "id,t8_disclosure_notice_id,t8_disclosure_version_id,t8_disclosure_notice_version_id,prod_code,data_date,column_key,column_value,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark", auth = APIAuth.NO)
	public int addDisclosureNoticeVersionValue(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		return disclosureNoticeVersionValueDao.addDisclosureNoticeVersionValue(params).getEffect();
	}
	
	@API(desc = "修改信披公告版本数据表", params = "id,t8_disclosure_notice_id,t8_disclosure_version_id,t8_disclosure_notice_version_id,prod_code,data_date,column_key,column_value,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark", auth = APIAuth.NO)
	public int updateDisclosureNoticeVersionValue(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		return disclosureNoticeVersionValueDao.updateDisclosureNoticeVersionValue(params).getEffect();
	}
	
	@API(desc = "删除信披公告版本数据表", params = "id,t8_disclosure_notice_id,t8_disclosure_version_id,t8_disclosure_notice_version_id,prod_code,data_date,column_key,column_value,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark", auth = APIAuth.NO)
	public int deleteDisclosureNoticeVersionValue(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		return disclosureNoticeVersionValueDao.deleteDisclosureNoticeVersionValue(params).getEffect();
	}

	@API(desc = "查询未补录完成数据", auth = APIAuth.NO)
	public SqlResult<DisclosureNoticeVersionValue> findNotFinishProcessList(SqlParam<DisclosureNoticeVersionValue> params) throws Exception {
		return disclosureNoticeVersionValueDao.findNotFinishProcessList(params);
	}

}
