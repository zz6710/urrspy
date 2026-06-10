package com.kayak.pms.disclosureControl.service;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.disclosureControl.dao.DisclosureOperationDao;
import com.kayak.pms.disclosureControl.model.DisclosureOperation;
import com.kayak.utils.CamelCaseMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

import java.util.List;
import java.util.Map;


@Service
@APIDefine(desc = "信披待办任务服务", model = DisclosureOperation.class)
public class DisclosureOperationService {

	@Autowired
	private DisclosureOperationDao disclosureOperationDao;


	//修改公告状态
	public void updateStatusByDealId(String deal_table,String id,String disclosure_type) throws Exception {
		 disclosureOperationDao.updateStatusByDealId(deal_table,id,disclosure_type);
	}

	@API(desc = "查询信披待办任务信息", auth = APIAuth.NO)
	public SqlResult<DisclosureOperation> findDisclosureOperations(SqlParam<DisclosureOperation> params) throws Exception {
		params.setMakeSql(true);
		return disclosureOperationDao.findDisclosureOperations(params);
	}

	@API(desc = "查询首页信披待办任务信息", auth = APIAuth.NO)
	public SqlResult<Map<String,Object>> findDesktopDisclosureOperations(SqlParam<DisclosureOperation> params) throws Exception {
		List<SqlRow> desktopDisclosureOperations = disclosureOperationDao.findDesktopDisclosureOperations();
		return CamelCaseMapUtils.CamelCaseSqlRow(desktopDisclosureOperations);
	}

	@API(desc = "查询首页信披待办任务详情信息", auth = APIAuth.NO)
	public SqlResult<Map<String, Object>> findDesktopDisclosureOperationsDetail(SqlParam<DisclosureOperation> params) throws Exception {
		Map<String, Object> paramsDirect = params.getParamsDirect();
		/*params.getModel().setStatus("0");
		params.getModel().setUserid((String) SysUtil.getSysUserParamValue("sys_user_userid"));*/
		paramsDirect.put("status", "0");
		paramsDirect.put("userid", (String)SysUtil.getSysUserParamValue("sys_user_userid"));
		return disclosureOperationDao.findDesktopDisclosureOperationsDetail(paramsDirect);
	}

	@API(desc = "添加信披待办任务", params = "id,operation_type,disclosure_type,roleid,userid,status,deal_table,deal_id,crt_date,crt_time,crt_user_id,crt_user_name,end_date,end_time,remark", auth = APIAuth.NO)
	public int addDisclosureOperation(SqlParam<DisclosureOperation> params) throws Exception {
		return disclosureOperationDao.addDisclosureOperation(params).getEffect();
	}
	
	@API(desc = "修改信披待办任务", params = "id,operation_type,disclosure_type,roleid,userid,status,deal_table,deal_id,crt_date,crt_time,crt_user_id,crt_user_name,end_date,end_time,remark", auth = APIAuth.NO)
	public int updateDisclosureOperation(SqlParam<DisclosureOperation> params) throws Exception {
		return disclosureOperationDao.updateDisclosureOperation(params).getEffect();
	}

	@API(desc = "修改净值待办任务", params = "id,operation_type,disclosure_type,roleid,userid,status,deal_table,deal_id,crt_date,crt_time,crt_user_id,crt_user_name,end_date,end_time,remark", auth = APIAuth.NO)
	public int updateNetValOperation(DisclosureOperation params) throws Exception {
		return disclosureOperationDao.updateDisclosureOperation(params).getEffect();
	}
	public UpdateResult updateOperation(DisclosureOperation params) throws Exception {
		return disclosureOperationDao.updateOperation(params);
	}
}
