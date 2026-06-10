package com.kayak.dps.valtabimp.service;

import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.valtabimp.repository.OdsReadAssetsReportDao;
import com.kayak.dps.valtabimp.repository.ValReportTabDao;
import com.kayak.dps.valtabimp.repository.ValReportTabParsetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.valtabimp.model.ValReportTab;

@Service
@APIDefine(desc = "估值表主体服务", model = ValReportTab.class)
public class ValReportTabService {

	@Autowired
	private ValReportTabDao valReportTabDao;

	@Autowired
	private OdsReadAssetsReportDao odsReadAssetsReportDao;
	@Autowired
	private ValReportTabParsetDao valReportTabParsetDao;

	@API(desc = "查询估值表主体信息", auth = APIAuth.YES)
	public SqlResult<ValReportTab> findValReportTabs(SqlParam<ValReportTab> params) throws Exception {
		params.setMakeSql(true);
		return valReportTabDao.findValReportTabs(params);
	}

	@API(desc = "添加估值表主体", auth = APIAuth.YES)
	public int addValReportTab(SqlParam<ValReportTab> params) throws Exception {
		return valReportTabDao.addValReportTab(params).getEffect();
	}
	
	@API(desc = "修改估值表主体",  auth = APIAuth.YES)
	public int updateValReportTab(SqlParam<ValReportTab> params) throws Exception {
		return valReportTabDao.updateValReportTab(params).getEffect();
	}
	
	@API(desc = "删除估值表主体", auth = APIAuth.YES)
	public String deleteValReportTab(SqlParam<ValReportTab> params) throws Exception {

		try {
			DaoUtil.doTrans(() -> {
				valReportTabDao.deleteValReportTab(params.getModel().getId());
				valReportTabParsetDao.deleteValReportTabParset(params.getModel().getId());
				odsReadAssetsReportDao.deleteOdsReadAssetsReport(params.getModel().getId());
			});
			return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
		}catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "保存失败", null).toString();
		}

	}

}
