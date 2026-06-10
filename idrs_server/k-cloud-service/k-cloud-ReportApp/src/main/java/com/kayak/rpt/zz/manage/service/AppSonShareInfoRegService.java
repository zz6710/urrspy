package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.InitialSubRegistInfo;
import com.kayak.rpt.zz.manage.model.ProdRegistFilingInfo;
import com.kayak.server.ServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.AppSonShareInfoRegDao;
import com.kayak.rpt.zz.manage.model.AppSonShareInfoReg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "子份额信息登记服务", model = AppSonShareInfoReg.class)
public class AppSonShareInfoRegService {

	@Autowired
	private AppSonShareInfoRegDao appSonShareInfoRegDao;
	@Autowired
	protected DaoService daoService;

	private final Logger log = LoggerFactory.getLogger(ProdRegistFilingInfoService.class);

	@API(desc = "查询子份额信息登记信息", auth = APIAuth.YES)
	public SqlResult<AppSonShareInfoReg> findAppSonShareInfoRegs(SqlParam<AppSonShareInfoReg> params) throws Exception {
		return appSonShareInfoRegDao.findAppSonShareInfoRegs(params);
	}

	@API(desc = "添加子份额信息登记", params = "bank_code,prod_reg_enc,son_share_code,son_share_name,son_share_task_type,son_share_reg_enc,son_share_sale_obj,task_date,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int addAppSonShareInfoReg(SqlParam<AppSonShareInfoReg> params) throws Exception {
		return appSonShareInfoRegDao.addAppSonShareInfoReg(params).getEffect();
	}

	@API(desc = "修改子份额信息登记", params = "bank_code,prod_reg_enc,son_share_code,son_share_name,son_share_task_type,son_share_reg_enc,son_share_sale_obj,task_date,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int updateAppSonShareInfoReg(SqlParam<AppSonShareInfoReg> params) throws Exception {
		params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
		params.getModel().setUpdDt(DateUtil.getNowDate());
		params.getModel().setUpdTime(DateUtil.getNowTime());
		return appSonShareInfoRegDao.updateAppSonShareInfoReg(params).getEffect();
	}

	@API(desc = "删除子份额信息登记", params = "bank_code,prod_reg_enc,son_share_code,son_share_name,son_share_task_type,son_share_reg_enc,son_share_sale_obj,task_date,details,register_date,register_serno,register_status,create_date,theory_report_start_date,theory_report_end_date,sys_data_status,sys_data_source,sys_data_version,report_date,id,crt_user,upd_user,crt_dt,upd_dt,crt_time,upd_time", auth = APIAuth.YES)
	public int deleteAppSonShareInfoReg(SqlParam<AppSonShareInfoReg> params) throws Exception {
		return appSonShareInfoRegDao.deleteAppSonShareInfoReg(params).getEffect();
	}

	@API(desc = "查询报送状态为0,1的数据", auth = APIAuth.NO)
	public String getAbnormalData(SqlParam<AppSonShareInfoReg> params) throws Exception {
		try {
			int  recordCnt = appSonShareInfoRegDao.findAppSonShareInfoRegsCount(params);
			if (recordCnt == 0) {
				return RequestSupport.updateReturnJson(false,  "没有需要确认并导出的数据，请检查！", null).toString();
			}
			int unreadyCnt= appSonShareInfoRegDao.findAppSonShareInfoRegStatus(params);
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
	public String updateAppSonShareInfoRegStatus(SqlParam<AppSonShareInfoReg> params) throws Exception {
		try {
			daoService.doTrans(() -> {
				appSonShareInfoRegDao.updateAppSonShareInfoRegStatus(params);
				appSonShareInfoRegDao.updateBaseReportResultInfo(params);
			});
			return RequestSupport.updateReturnJson(true,  "操作成功！", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false,  "操作失败，请检查!", null).toString();
		}
	}

	@API(desc = "导入子份额登记信息", auth = APIAuth.YES, operation = APIOperation.INSTER)
	public void importAppSonShareInfo(List<AppSonShareInfoReg> appSonShareInfoRegs, Map<String, Object> params) throws Exception {
//		Map<String, Object> params1 = new HashMap<>();
//		List<SqlRow> sonShareInfoList = appSonShareInfoRegDao.findSonShareProdInfo(params1);
		try {
			for (AppSonShareInfoReg appSonShareInfoReg : appSonShareInfoRegs) {
				Map<String, Object> map = BeanUtil.beanToMap(appSonShareInfoReg);
				map.put("updUser", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
				map.put("updDt", DateUtil.getNowDate());
				map.put("updTime", DateUtil.getNowTime());
//				for (int i = 0; i < sonShareInfoList.size(); i++) {
//					SqlRow sqlRow = sonShareInfoList.get(i);
//					if (sqlRow.get("PROD_CODE").equals(appSonShareInfoReg.getProdRegEnc())) {
//						if ("01".equals(appSonShareInfoReg.getSonShareTaskType())) {
//							map.put("theoryReportStartDate", DateUtil.getSysWordDayByNum(sqlRow.get("ESTABLISH_DATE").toString(),-1));
//							map.put("theoryReportEndDate", DateUtil.getSysWordDayByNum(sqlRow.get("ESTABLISH_DATE").toString(),0));
//						} else if ("02".equals(appSonShareInfoReg.getSonShareTaskType())) {
//							map.put("theoryReportStartDate", DateUtil.getSysWordDayByNum(sqlRow.get("ESTABLISH_DATE").toString(),0));
//							map.put("theoryReportEndDate", DateUtil.getSysWordDayByNum(sqlRow.get("ESTABLISH_DATE").toString(),1));
//						}
//					}
//				}
				appSonShareInfoRegDao.updateAppSonShareInfoReg(map);
			}
		} catch (Exception e) {
			try {
				log.info("导入失败");
			} catch (Exception e1) {
				log.error(e.getMessage(), e);
			}

		}
	}



}
