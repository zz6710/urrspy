package com.kayak.rpt.zz.manage.service;

import com.kayak.core.dao.DaoService;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.zz.manage.model.AppSonShareInfoReg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.AppSonShareDelRegDao;
import com.kayak.rpt.zz.manage.model.AppSonShareDelReg;

@Service
@APIDefine(desc = "子份额登记删除服务", model = AppSonShareDelReg.class)
public class AppSonShareDelRegService {

	@Autowired
	private AppSonShareDelRegDao appSonShareDelRegDao;

	@Autowired
	protected DaoService daoService;

	private final Logger log = LoggerFactory.getLogger(ProdRegistFilingInfoService.class);

	@API(desc = "查询子份额登记删除信息", auth = APIAuth.YES)
	public SqlResult<AppSonShareDelReg> findAppSonShareDelRegs(SqlParam<AppSonShareDelReg> params) throws Exception {
		return appSonShareDelRegDao.findAppSonShareDelRegs(params);
	}

	@API(desc = "添加子份额登记删除", params = "bank_code,prod_reg_enc,son_share_code,son_share_task_type,task_date,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int addAppSonShareDelReg(SqlParam<AppSonShareDelReg> params) throws Exception {
		return appSonShareDelRegDao.addAppSonShareDelReg(params).getEffect();
	}
	
	@API(desc = "修改子份额登记删除", params = "bank_code,prod_reg_enc,son_share_code,son_share_task_type,task_date,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int updateAppSonShareDelReg(SqlParam<AppSonShareDelReg> params) throws Exception {
		return appSonShareDelRegDao.updateAppSonShareDelReg(params).getEffect();
	}
	
	@API(desc = "删除子份额登记删除", params = "bank_code,prod_reg_enc,son_share_code,son_share_task_type,task_date,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int deleteAppSonShareDelReg(SqlParam<AppSonShareDelReg> params) throws Exception {
		return appSonShareDelRegDao.deleteAppSonShareDelReg(params).getEffect();
	}

	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<AppSonShareDelReg> params) throws Exception {
		try {
			int  recordCnt = appSonShareDelRegDao.findAppSonShareDelRegsCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= appSonShareDelRegDao.findAppSonShareDelRegStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出募集",auth = APIAuth.YES)
	public String updateAppSonShareDelRegStatus(SqlParam<AppSonShareDelReg> params) throws Exception {
		try {
			daoService.doTrans(() -> {
				appSonShareDelRegDao.updateAppSonShareDelRegStatus(params);
				appSonShareDelRegDao.updateBaseReportResultInfo(params);
			});
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}

}
