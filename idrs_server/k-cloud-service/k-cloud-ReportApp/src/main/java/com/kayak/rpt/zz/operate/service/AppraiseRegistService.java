package com.kayak.rpt.zz.operate.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.AppraiseRegistInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.operate.dao.AppraiseRegistDao;
import com.kayak.rpt.zz.operate.model.AppraiseRegist;

import java.util.Map;

@Service
@APIDefine(desc = "估值信息登记要素操作记录服务", model = AppraiseRegist.class)
public class AppraiseRegistService {

	@Autowired
	private AppraiseRegistDao appraiseRegistDao;

	@API(desc = "查询估值信息登记要素操作记录信息", auth = APIAuth.YES)
	public SqlResult<AppraiseRegist> findAppraiseRegists(SqlParam<AppraiseRegist> params) throws Exception {
		params.setMakeSql(true);
		return appraiseRegistDao.findAppraiseRegists(params);
	}

	@API(desc = "添加估值信息登记要素操作记录", params = "bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addAppraiseRegist(SqlParam<AppraiseRegistInfo> params,String opType) throws Exception {

		AppraiseRegist appraiseRegist = BeanUtil.copyProperties(params.getModel(), AppraiseRegist.class);
		appraiseRegist.setOpType(opType);
		appraiseRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return appraiseRegistDao.addAppraiseRegist(appraiseRegist).getEffect();
	}
	
	@API(desc = "修改估值信息登记要素操作记录", params = "bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int updateAppraiseRegist(SqlParam<AppraiseRegist> params) throws Exception {
		return appraiseRegistDao.updateAppraiseRegist(params).getEffect();
	}
	
	@API(desc = "删除估值信息登记要素操作记录", params = "bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int deleteAppraiseRegist(SqlParam<AppraiseRegist> params) throws Exception {
		return appraiseRegistDao.deleteAppraiseRegist(params).getEffect();
	}
	@API(desc = "添加估值信息登记要素导入操作记录", params = "bank_code,asset_code,valuation_date,unit_debt_net,unit_debt_full,details,register_serno,imp_date,register_date,register_status,summit_user,create_date,create_time,op_type", auth = APIAuth.NO)
	public int addImportAppraiseRegist(AppraiseRegistInfo appraiseRegistInfo, String opType) throws Exception {
		AppraiseRegist appraiseRegist = BeanUtil.copyProperties(appraiseRegistInfo, AppraiseRegist.class);
		appraiseRegist.setOpType(opType);
		appraiseRegist.setSummitUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username")));
		return appraiseRegistDao.addAppraiseRegist(appraiseRegist).getEffect();
	}
}
