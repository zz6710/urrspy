package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.dao.ImportTemplateManageField01Dao;
import com.kayak.dps.app.model.ImportTemplateManageField01;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "导入模板管理表", model = ImportTemplateManageField01.class)
public class ImportTemplateManageField01Service {
	@Autowired
	private ImportTemplateManageField01Dao importTemplateManageField01Dao;



	@API(desc = "查询一维模板字段列表", auth = APIAuth.NO)
	public SqlResult<ImportTemplateManageField01> findTemplateFieldList(SqlParam<ImportTemplateManageField01> params) throws Exception {
		params.setMakeSql(false);
		return importTemplateManageField01Dao.findTemplateFieldList(params);
	}


	@API(desc = "添加一维模板字段",  auth = APIAuth.YES)
	public String addTemplateFieldInfo(SqlParam<ImportTemplateManageField01> params) throws Exception {
		try {
			params.getModel().setUptDate(DateUtil.getNowDate());
			params.getModel().setUptTime(DateUtil.getNowTime());
			String userName = (String) SysUtil.getSysUserParamValue("sys_user_loginname");
			params.getModel().setUptUsr(userName);
			importTemplateManageField01Dao.addTemplateFieldInfo(params);
			return RequestSupport.updateReturnJson(true,  "添加成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "添加失败！", null).toString();
		}
	}

	@API(desc = "更新一维模板字段",  auth = APIAuth.YES)
	public String uptTemplateFieldInfo(SqlParam<ImportTemplateManageField01> params) throws Exception {
		try {
			params.getModel().setUptDate(DateUtil.getNowDate());
			params.getModel().setUptTime(DateUtil.getNowTime());
			String userName = (String) SysUtil.getSysUserParamValue("sys_user_loginname");
			params.getModel().setUptUsr(userName);
			importTemplateManageField01Dao.uptTemplateFieldInfo(params.getModel());
			return RequestSupport.updateReturnJson(true,  "成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "失败！", null).toString();
		}
	}

	@API(desc = "删除一维模板字段",  auth = APIAuth.YES)
	public String delTemplateFieldInfo(SqlParam<ImportTemplateManageField01> params) throws Exception {
		try {
			importTemplateManageField01Dao.delTemplateFieldInfo(params.getModel());
			return RequestSupport.updateReturnJson(true,  "成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "失败！", null).toString();
		}
	}
	@API(desc = "查询对应表名字段信息",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<ImportTemplateManageField01> findColumnName(SqlParam<ImportTemplateManageField01> params) throws Exception {
		return importTemplateManageField01Dao.findColumnName(params);
	}

	@API(desc = "查询字典信息",operation = APIOperation.SELECT, auth = APIAuth.NO)
	public SqlResult<ImportTemplateManageField01> findTableName(SqlParam<ImportTemplateManageField01> params) throws Exception {
		return importTemplateManageField01Dao.findTableName(params);
	}
}
