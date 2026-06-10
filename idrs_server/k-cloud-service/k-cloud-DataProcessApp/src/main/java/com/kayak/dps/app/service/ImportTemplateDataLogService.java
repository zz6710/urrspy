package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.dao.ImportTemplateDataLogDao;
import com.kayak.dps.app.model.ImportTemplateDataLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "报送数据导入日志表服务", model = ImportTemplateDataLog.class)
public class ImportTemplateDataLogService {

	@Autowired
	private ImportTemplateDataLogDao importTemplateDataLogDao;

	@API(desc = "查询报送数据导入日志表信息", auth = APIAuth.YES)
	public SqlResult<ImportTemplateDataLog> findImportTemplateDataLogs(SqlParam<ImportTemplateDataLog> params) throws Exception {
		return importTemplateDataLogDao.findImportTemplateDataLogs(params);
	}

	@API(desc = "添加报送数据导入日志表", params = "id,import_template_manage_id,report_date,sys_data_version,imp_date", auth = APIAuth.NO)
	public int addImportTemplateDataLog(SqlParam<ImportTemplateDataLog> params) throws Exception {
		return importTemplateDataLogDao.addImportTemplateDataLog(params).getEffect();
	}

	@API(desc = "修改报送数据导入日志表", params = "id,import_template_manage_id,report_date,sys_data_version,imp_date", auth = APIAuth.NO)
	public int updateImportTemplateDataLog(SqlParam<ImportTemplateDataLog> params) throws Exception {
		return importTemplateDataLogDao.updateImportTemplateDataLog(params).getEffect();
	}

	@API(desc = "删除报送数据导入日志表", params = "id,import_template_manage_id,report_date,sys_data_version,imp_date", auth = APIAuth.NO)
	public int deleteImportTemplateDataLog(SqlParam<ImportTemplateDataLog> params) throws Exception {
		return importTemplateDataLogDao.deleteImportTemplateDataLog(params).getEffect();
	}

}
