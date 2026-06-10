package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.dao.DaoService;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.AppNavInfoReg;
import com.kayak.rpt.zz.manage.util.CheckDataParams;
import com.kayak.rpt.zz.operate.service.AppNavRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.AppNavInfoRegDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "净值信息登记服务", model = AppNavInfoReg.class)
public class AppNavInfoRegService {

	@Autowired
	private AppNavInfoRegDao appNavInfoRegDao;

	@Autowired
	private AppNavRegService appNavRegService;

	@Autowired
	protected DaoService daoService;
	@Autowired
	CheckDataForVueService checkDataForVueService;

	CheckDataParams checkDataParams = new CheckDataParams();

	@API(desc = "查询净值信息登记信息", auth = APIAuth.YES)
	public SqlResult<AppNavInfoReg> findAppNavInfoRegs(SqlParam<AppNavInfoReg> params) throws Exception {
		return appNavInfoRegDao.findAppNavInfoRegs(params);
	}

	@API(desc = "添加净值信息登记", params = "bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int addAppNavInfoReg(SqlParam<AppNavInfoReg> params) throws Exception {
//		appNavRegService.addAppNavInfoReg(params, OperatorEnum.CREATE.getVal());
		return appNavInfoRegDao.addAppNavInfoReg(params).getEffect();
	}
	
	@API(desc = "修改净值信息登记", params = "bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public String updateAppNavInfoReg(SqlParam<AppNavInfoReg> params) throws Exception {
		try {
			checkDataParams.initDataNoDict();
			String whiteregex = CheckDataParams.whiteregex;
			String whitereForCode = CheckDataParams.whitereForCode;
			String checkErr = checkDataForVueService.appNavInfoRegCheckForVue(whiteregex,whitereForCode,params.getModel());
			if (org.apache.commons.lang3.StringUtils.isNotBlank(checkErr)) {
				return RequestSupport.updateReturnJson(false,  "修改失败！错误信息为：\n"+checkErr, null).toString();
			}
			Map paramMap = new HashMap<>();
			paramMap.put("registerSerno",params.getModel().getRegisterSerno());
			SqlParam<AppNavInfoReg> oldParams =  new FetcherData<>(paramMap,AppNavInfoReg.class);
			SqlResult<AppNavInfoReg> originParams =  appNavInfoRegDao.findAppNavInfoRegs(oldParams);
			if(originParams.getRows().size()>0){
				AppNavInfoReg param = 	originParams.getRows().get(0);
				paramMap = BeanUtil.beanToMap(param);
				oldParams =  new FetcherData<>(paramMap,AppNavInfoReg.class);
			}
			appNavRegService.addAppNavInfoReg(oldParams, OperatorEnum.UPDATE.getVal());
			appNavInfoRegDao.updateAppNavInfoReg(params);
			return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "修改失败，数据库错误信息为："+e.getMessage(), null).toString();
		}
	}
	
	@API(desc = "删除净值信息登记", params = "bank_code,prod_reg_enc,nav_reg_type,son_share_code,cny,nav,rmb_nav,dj_nav,total_nav,rmb_total_nav,fq_nav,rmb_fq_nav,nav_cal_type,share,nav_date,disclosure_date,remain_bal,rmb_remain_bal,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,data_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int deleteAppNavInfoReg(SqlParam<AppNavInfoReg> params) throws Exception {
		appNavRegService.addAppNavInfoReg(params, OperatorEnum.DELETE.getVal());
		return appNavInfoRegDao.deleteAppNavInfoReg(params).getEffect();
	}

	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<AppNavInfoReg> params) throws Exception {
		try {
			int  recordCnt = appNavInfoRegDao.findAppNavInfoRegsCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= appNavInfoRegDao.findAppNavInfoRegStatus(params);
			if (unreadyCnt > 0) {
				return RequestSupport.updateReturnJson(false,  "存在报送状态异常(0 初始化 或 1 校验失败)的数据，请处理后导出！", null).toString();
			}
			return RequestSupport.updateReturnJson(true,  "", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "查询失败，请检查", null).toString();
		}
	}

	@API(desc = "确认并导出净值信息登记",auth = APIAuth.YES)
	public String updateAppNavInfoRegStatus(SqlParam<AppNavInfoReg> params) throws Exception {
		try {
			daoService.doTrans(() -> {
				appNavInfoRegDao.updateAppNavInfoRegStatus(params);
				appNavInfoRegDao.updateBaseReportResultInfo(params);
			});
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}

	@API(desc = "导入净值信息登记",auth = APIAuth.YES)
	public void importAppNavInfoReg(List<AppNavInfoReg> appNavInfoRegs, Map<String, Object> params) throws Exception {
		appNavInfoRegDao.deleteImportAppNavInfoReg(params);
		for (AppNavInfoReg appNavInfoReg : appNavInfoRegs) {
			Map<String, Object> map = BeanUtil.beanToMap(appNavInfoReg);
			// 添加至操作记录
//			appNavRegService.addImportAppNavInfoReg(appNavInfoReg,OperatorEnum.IMPORT.getVal());
			appNavInfoRegDao.addImportAppNavInfoReg(map);
		}
	}

}
