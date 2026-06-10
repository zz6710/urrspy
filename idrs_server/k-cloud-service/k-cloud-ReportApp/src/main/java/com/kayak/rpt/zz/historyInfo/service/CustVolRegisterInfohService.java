package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.historyInfo.dao.CustVolRegisterInfohDao;
import com.kayak.rpt.zz.historyInfo.model.CustVolRegisterInfoh;
import com.kayak.rpt.zz.historyInfo.service.comm.ZtHttpHelper;
import com.kayak.rpt.zz.historyInfo.service.comm.ZtService;
import com.kayak.rpt.zz.manage.model.BaseReportExportLog;
import com.kayak.rpt.zz.manage.service.BaseReportExportLogService;
import com.kayak.utils.ExcelWriter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@APIDefine(desc = "投资者持有历史信息服务", model = CustVolRegisterInfoh.class)
public class CustVolRegisterInfohService {

	private static final Logger log = LoggerFactory.getLogger(CustVolRegisterInfohService.class);

	@Autowired
	private CustVolRegisterInfohDao custVolRegisterInfohDao;
	@Autowired
	PractyRegistInfohService practyRegistInfohService;
	@Autowired
	private ZtHttpHelper ztHttpHelper;
	@Autowired
	private ZtService ztService;
	@Autowired
	ExcelWriter excelWriter;
	@Autowired
	BaseReportExportLogService baseReportExportLogService;

	@API(desc = "查询投资者持有历史信息信息", auth = APIAuth.YES)
	public SqlResult<CustVolRegisterInfoh> findCustVolRegisterInfohs(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		Map<String, Object> pageParam = params.getParams();
		String port_address = SysUtil.getSystemParamsByParaid("inv_hd_port_2");

		if(pageParam.get("reportDate")!= null ){
			pageParam.put("S_HOLD_DATE", pageParam.get("reportDate"));
			pageParam.put("REPORT_DATE", pageParam.get("reportDate"));
		}
		if(pageParam.get("custNo")!= null ){
			pageParam.put("S_CUST_NO", pageParam.get("custNo"));
		}
		if(pageParam.get("prodCode")!= null ){
			pageParam.put("PROD_CODE", pageParam.get("prodCode"));
		}
		if(pageParam.get("cur")!= null ){
			pageParam.put("S_CUR", StringUtils.replace((String) pageParam.get("cur"), ",", "|"));
			pageParam.remove("cur");// 防止中台误查
		} else {
			pageParam.put("S_CUR", "^.*$");
		}

		//根据参数判断调用中台接口数据集
		if (pageParam.get("reportDate") == null) {
			port_address = SysUtil.getSystemParamsByParaid("inv_hd_port_1");//数据日期为空，调用客户查询数据集
		}

		if (pageParam.get("custNo") == null) {
			port_address = SysUtil.getSystemParamsByParaid("inv_hd_port_0");//客户为空，调用数据日期查询数据集
		}

		//清空表且仅入库中台接口当前查询页数据
		int total_count =
		ztHttpHelper.saveData(port_address, pageParam, obj -> {
			JSONArray contentArray=(JSONArray) obj;
			ztService.batchSave("api_app_cust_vol_register_info", pageParam, true, contentArray);
			return true;
		});

		params.setStart(0);
		SqlResult<CustVolRegisterInfoh> sqlResult = custVolRegisterInfohDao.findCustVolRegisterInfohs(params);

		sqlResult.setResults(total_count);
		return sqlResult;
	}

	/**
	 * 查询投资者持有(母产品)合并前后数据
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<CustVolRegisterInfoh> findCustVolRegisterRemark (SqlParam<CustVolRegisterInfoh> params) throws Exception {
		return custVolRegisterInfohDao.findCustVolRegisterRemark(params);
	}

	@API(desc = "添加投资者持有历史信息", params = "bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id", auth = APIAuth.NO)
	public int addCustVolRegisterInfoh(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		return custVolRegisterInfohDao.addCustVolRegisterInfoh(params).getEffect();
	}
	
	@API(desc = "修改投资者持有历史信息", params = "bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id", operation = APIOperation.UPDATE, auth = APIAuth.NO)
	public int updateCustVolRegisterInfoh(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		return custVolRegisterInfohDao.updateCustVolRegisterInfoh(params).getEffect();
	}
	
	@API(desc = "删除投资者持有历史信息", params = "bank_code,prod_code,cust_no,hold_date,cur,hold_vol,hold_amt,convert_rmb,imp_date,register_date,register_status,register_serno,id", auth = APIAuth.NO)
	public int deleteCustVolRegisterInfoh(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		return custVolRegisterInfohDao.deleteCustVolRegisterInfoh(params).getEffect();
	}

	@API(desc = "投资者持有信息登记历史下载", operation = APIOperation.UPDATE,auth = APIAuth.YES)
	public String historyDownload(SqlParam<CustVolRegisterInfoh> params) throws Exception {
		try {
			Map<String, Object> param = params.getParamsDirect();
			String processInstanceId = (String) param.get("processInstanceId");
			param.put("modelClassName", CustVolRegisterInfoh.class.getName());
			param.put("action", "findCustVolRegisterInfohs");
			Runnable runnable = () -> {
				Map<String, String> resMap = excelWriter.downloadEx(param);
				BaseReportExportLog exportLog = new BaseReportExportLog();
				exportLog.setFilePath(resMap.get("local"));
				exportLog.setRemotePath(resMap.get("remote"));
				exportLog.setProcessInstanceId(processInstanceId);
				exportLog.setFileStatus("2");
				try {
					baseReportExportLogService.updateFilePath(exportLog);
				} catch (Exception e) {
					log.error("投资者持有信息登记历史下载，更新文件路径失败！", e);
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

}
