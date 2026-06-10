package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.dao.ImportTemplateManageField02Dao;
import com.kayak.dps.app.dao.ImportTemplateManageField02Dao;
import com.kayak.dps.app.model.ImportTemplateManageField02;
import com.kayak.dps.app.model.ImportTemplateManageField02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "导入模板管理表", model = ImportTemplateManageField02.class)
public class ImportTemplateManageField02Service {
	@Autowired
	private ImportTemplateManageField02Dao importTemplateManageField02Dao;



	@API(desc = "查询二维模板字段列表", auth = APIAuth.NO)
	public SqlResult<ImportTemplateManageField02> findTemplateFieldList(SqlParam<ImportTemplateManageField02> params) throws Exception {
		params.setMakeSql(false);
		return importTemplateManageField02Dao.findTemplateFieldList(params);
	}


	@API(desc = "添加二维模板字段",  auth = APIAuth.YES)
	public String addTemplateFieldInfo(SqlParam<ImportTemplateManageField02> params) throws Exception {
		try {
			params.getModel().setUptDate(DateUtil.getNowDate());
			params.getModel().setUptTime(DateUtil.getNowTime());
			String userName = (String) SysUtil.getSysUserParamValue("sys_user_loginname");
			params.getModel().setUptUsr(userName);
			importTemplateManageField02Dao.addTemplateFieldInfo(params);
			return RequestSupport.updateReturnJson(true,  "添加成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "添加失败！", null).toString();
		}
	}

	@API(desc = "更新二维模板字段",  auth = APIAuth.YES)
	public String uptTemplateFieldInfo(SqlParam<ImportTemplateManageField02> params) throws Exception {
		try {
			params.getModel().setUptDate(DateUtil.getNowDate());
			params.getModel().setUptTime(DateUtil.getNowTime());
			String userName = (String) SysUtil.getSysUserParamValue("sys_user_loginname");
			params.getModel().setUptUsr(userName);
			importTemplateManageField02Dao.uptTemplateFieldInfo(params.getModel());
			return RequestSupport.updateReturnJson(true,  "成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "失败！", null).toString();
		}
	}

	@API(desc = "删除二维模板字段",  auth = APIAuth.YES)
	public String delTemplateFieldInfo(SqlParam<ImportTemplateManageField02> params) throws Exception {
		try {
			importTemplateManageField02Dao.delTemplateFieldInfo(params.getModel());
			return RequestSupport.updateReturnJson(true,  "成功！", null).toString();
		} catch (Exception e){
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "失败！", null).toString();
		}
	}

}
