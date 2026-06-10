package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.disclosureControl.dao.DisclosureChangeNoticeStatusDao;
import com.kayak.pms.disclosureControl.model.DisclosureChangeNoticeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "信披公告版本信息表服务", model = DisclosureChangeNoticeStatus.class)
public class DisclosureChangeNoticeStatusService {

	@Autowired
	private DisclosureChangeNoticeStatusDao disclosureChangeNoticeStatusDao;


	/**
	 * 查询信披状态变更记录
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询信披状态变更记录", auth = APIAuth.YES)
	public SqlResult<DisclosureChangeNoticeStatus> findDisclosureChangeNoticeStatus(SqlParam<DisclosureChangeNoticeStatus> params) throws Exception {
		return disclosureChangeNoticeStatusDao.findDisclosureChangeNoticeStatus(params);
	}

	@API(desc = "导出",auth = APIAuth.YES)
	public String exportChangeNoticeStatusControl() {
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}
}
