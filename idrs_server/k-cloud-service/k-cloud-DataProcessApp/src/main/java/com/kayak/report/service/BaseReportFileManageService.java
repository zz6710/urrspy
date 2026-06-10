package com.kayak.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.report.dao.BaseReportFileManageDao;
import com.kayak.report.model.BaseReportFileManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "产品报告文件管理表服务", model = BaseReportFileManage.class)
public class BaseReportFileManageService {

	@Autowired
	private BaseReportFileManageDao baseReportFileManageDao;

	@API(desc = "查询产品报告文件管理表信息", auth = APIAuth.YES)
	public SqlResult<BaseReportFileManage> findBaseReportFileManages(SqlParam<BaseReportFileManage> params) throws Exception {
		return baseReportFileManageDao.findBaseReportFileManages(params);
	}

	@API(desc = "查询产品代码及名称", auth = APIAuth.YES)
	public SqlResult<BaseReportFileManage> findDwdPrdPrdBasInfs(SqlParam<BaseReportFileManage> params) throws Exception {
		return baseReportFileManageDao.findDwdPrdPrdBasInfs(params);
	}

	@API(desc = "查询产品代码及名称", auth = APIAuth.NO)
	public SqlResult<BaseReportFileManage> findOdsPrdPrdBasInfs(SqlParam<BaseReportFileManage> params) throws Exception {
		return baseReportFileManageDao.findOdsPrdPrdBasInfs(params);
	}


	@API(desc = "添加产品报告文件管理表", params = "id,file_name,prod_co,prod_nm_fu,zipfilename,remote_file,operaterno,operatername,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int addBaseReportFileManage(SqlParam<BaseReportFileManage> params) throws Exception {
		return baseReportFileManageDao.addBaseReportFileManage(params).getEffect();
	}
	
	@API(desc = "修改产品报告文件管理表", params = "id,file_name,prod_co,prod_nm_fu,zipfilename,remote_file,operaterno,operatername,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.YES)
	public int updateBaseReportFileManage(SqlParam<BaseReportFileManage> params) throws Exception {
		return baseReportFileManageDao.updateBaseReportFileManage(params).getEffect();
	}
	
	@API(desc = "删除产品报告文件管理表", params = "id,file_name,prod_co,prod_nm_fu,zipfilename,remote_file,operaterno,operatername,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.YES)
	public int deleteBaseReportFileManage(SqlParam<BaseReportFileManage> params) throws Exception {
		return baseReportFileManageDao.deleteBaseReportFileManage(params).getEffect();
	}

}
