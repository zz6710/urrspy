package com.kayak.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.constants.SystemParamConstants;
import com.kayak.core.util.Tools;
import com.kayak.system.dao.SystemParamDao;
import com.kayak.system.model.SystemParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@APIDefine(desc = "系统参数服务", model = SystemParam.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class SystemParamService {

	private final SystemParamDao systemParamDao;

	@API(desc = "保存系统参数列表")
	public String update(SqlParam<SystemParam> param) throws Exception {
		SystemParam model = param.getModel();
		String paraid = model.getParaid();
		String paravalue = model.getParavalue();

		if (Tools.isBlank(paraid)) {
			return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
		}
		// 等号分隔转换为list
		List<SystemParam> params = new ArrayList<>();
		String[] paraidArr = paraid.split("=");
		String[] paravalueArr = paravalue.split("=");
		for (int i = 0; i < paraidArr.length; i++) {
			SystemParam p = new SystemParam();
			p.setParaid(paraidArr[i]);
			p.setParavalue(paravalueArr[i]);
			params.add(p);
		}

		systemParamDao.update(params);
		refreshCache();
		return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
	}

	@API(desc = "查询系统参数列表",auth = APIAuth.NO)
	public SqlResult<SystemParam> find(SqlParam<SystemParam> params) throws Exception {
		SystemParam model = params.getModel();
		model.setIsdisplay(SystemParamConstants.SHOW);
		params.setMakeSql(true);
		return systemParamDao.find(params);
	}

	@API(desc = "查询系统参数列表驾驶舱",auth = APIAuth.NO)
	public SqlResult<SystemParam> findImpDate(SqlParam<SystemParam> params) throws Exception {
		SystemParam model = params.getModel();
		model.setIsdisplay(SystemParamConstants.SHOW);
		params.setMakeSql(false);
		return systemParamDao.findImpinfo(params);
	}

	@API(desc = "查询系统参数表信息", auth = APIAuth.YES)
	public SqlResult<SystemParam> findSysParams1(SqlParam<SystemParam> params) throws Exception {
		return findSysParams(params);
	}

	@API(desc = "查询系统参数表信息", auth = APIAuth.NO)
	public SqlResult<SystemParam> findSysParams(SqlParam<SystemParam> params) throws Exception {
 		return systemParamDao.findSysParams(params);
	}
	
	@API(desc = "查询业务参数表信息", auth = APIAuth.NO)
	public SqlResult<SystemParam> findBusinessParams(SqlParam<SystemParam> params) throws Exception {
 		return systemParamDao.findBusinessParams(params);
	}

	@API(desc = "查询接口参数表信息", auth = APIAuth.NO)
	public SqlResult<SystemParam> findPortParams(SqlParam<SystemParam> params) throws Exception {
 		return systemParamDao.findPortParams(params);
	}

	@API(desc = "查询直连参数表信息", auth = APIAuth.NO)
	public SqlResult<SystemParam> findRegisterParams(SqlParam<SystemParam> params) throws Exception {
		return systemParamDao.findRegisterParams(params);
	}

	@API(desc = "添加系统参数表", params = "moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay", auth = APIAuth.NO)
	public int addSysParam(SqlParam<SystemParam> params) throws Exception {
		int effect = systemParamDao.addSysParam(params).getEffect();
		refreshCache();
		return effect;
	}

	@API(desc = "修改系统参数表",  auth = APIAuth.NO, operation = APIOperation.UPDATE)
	public int updateSysParam(SqlParam<SystemParam> params) throws Exception {
		int effect = systemParamDao.updateSysParam(params).getEffect();
		refreshCache();
		return effect;
	}

	@API(desc = "删除系统参数表", params = "moduleid,paraid,paravalue,paraname,groupparaid,dict,functype,confoption,isdisplay", auth = APIAuth.NO)
	public int deleteSysParam(SqlParam<SystemParam> params) throws Exception {
		int effect = systemParamDao.deleteSysParam(params).getEffect();
		refreshCache();
		return effect;
	}

	private void refreshCache() throws Exception {
		CacheUtil.freshenSystemParam();
	}

	@API(desc = "获取是否启用系统工作日", auth = APIAuth.NO)
	public SqlResult<SystemParam> findSystemTime(SqlParam<SystemParam> params) throws Exception {
		return systemParamDao.findSystemTime(params);
	}
}
