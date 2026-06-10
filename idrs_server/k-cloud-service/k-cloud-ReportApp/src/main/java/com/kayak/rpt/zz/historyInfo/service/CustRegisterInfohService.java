package com.kayak.rpt.zz.historyInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.zz.historyInfo.dao.CustRegisterInfohDao;
import com.kayak.rpt.zz.historyInfo.model.CustRegisterInfoh;
import com.kayak.rpt.zz.historyInfo.service.comm.ZtHttpHelper;
import com.kayak.rpt.zz.historyInfo.service.comm.ZtService;
import com.kayak.rpt.zz.manage.model.BaseReportExportLog;
import com.kayak.rpt.zz.manage.service.BaseReportExportLogService;
import com.kayak.utils.ExcelWriter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@APIDefine(desc = "投资者登记历史信息服务", model = CustRegisterInfoh.class)
public class CustRegisterInfohService {

	private static final Logger log = LoggerFactory.getLogger(CustRegisterInfohService.class);

	@Autowired
	private CustRegisterInfohDao custRgisterInfoDao;
	@Autowired
	private ZtHttpHelper ztHttpHelper;
	@Autowired
	private ZtService ztService;
	@Autowired
	BaseReportExportLogService baseReportExportLogService;
	@Autowired
	ExcelWriter excelWriter;

	@API(desc = "查询投资者登记历史信息信息", auth = APIAuth.YES)
	public SqlResult<CustRegisterInfoh> findCustRegisterInfos(SqlParam<CustRegisterInfoh> params) throws Exception {
		String port_address = SysUtil.getSystemParamsByParaid("inv_idn_port");
		Map<String, Object> pageParam = params.getParams();

		if(pageParam.get("reportDate")!= null ){
			pageParam.put("S_DEAL_DT",pageParam.get("reportDate"));
		}
		if(pageParam.get("dataType")!= null ){
			pageParam.put("S_DATA_TYPE",StringUtils.replace((String) pageParam.get("dataType"), ",", "|"));
			pageParam.remove("dataType");// 防止中台误查
		} else {
			pageParam.put("S_DATA_TYPE", "^.*$");
		}
		if(pageParam.get("custType")!= null ){
			pageParam.put("S_CUST_TYPE",StringUtils.replace((String) pageParam.get("custType"), ",", "|"));
			pageParam.remove("custType");// 防止中台误查
		} else {
			pageParam.put("S_CUST_TYPE", "^.*$");
		}
		if(pageParam.get("idCode")!= null ){
			pageParam.put("ID_CODE",pageParam.get("idCode"));
		}
		if(pageParam.get("personalIdType")!= null ){
			pageParam.put("S_PERSONAL_ID_TYPE",StringUtils.replace((String) pageParam.get("personalIdType"), ",", "|"));
			pageParam.remove("personalIdType");// 防止中台误查
		} else {
			pageParam.put("S_PERSONAL_ID_TYPE", "^.*$");
		}
		if(pageParam.get("organizationIdType")!= null ){
			pageParam.put("S_ORGANIZATION_ID_TYPE",StringUtils.replace((String) pageParam.get("organizationIdType"), ",", "|"));
			pageParam.remove("organizationIdType");// 防止中台误查
		} else {
			pageParam.put("S_ORGANIZATION_ID_TYPE", "^.*$");
		}

		//清空表且仅入库中台接口当前查询页数据
		int total_count =
		ztHttpHelper.saveData(port_address,pageParam, obj -> {
			JSONArray contentArray=(JSONArray) obj;
			ztService.batchSave("api_app_cust_register_info", pageParam, true, contentArray);
			return true;
		});
		params.setStart(0);
		SqlResult<CustRegisterInfoh> sqlResult=custRgisterInfoDao.findCustRegisterInfohs(params);

		sqlResult.setResults(total_count);
		return sqlResult;
	}


	@API(desc = "添加投资者登记历史信息", params = "bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark,register_serno,imp_date,register_date,register_status,register_acct,register_cust_no", auth = APIAuth.NO)
	public int addCustRegisterInfo(SqlParam<CustRegisterInfoh> params) throws Exception {
		return custRgisterInfoDao.addCustRegisterInfoh(params).getEffect();
	}
	
	@API(desc = "修改投资者登记历史信息", params = "bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark,register_serno,imp_date,register_date,register_status,register_acct,register_cust_no", auth = APIAuth.NO)
	public int updateCustRegisterInfo(SqlParam<CustRegisterInfoh> params) throws Exception {
		return custRgisterInfoDao.updateCustRegisterInfoh(params).getEffect();
	}
	
	@API(desc = "删除投资者登记历史信息", params = "bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark,register_serno,imp_date,register_date,register_status,register_acct,register_cust_no", auth = APIAuth.NO)
	public int deleteCustRegisterInfo(SqlParam<CustRegisterInfoh> params) throws Exception {
		return custRgisterInfoDao.deleteCustRegisterInfoh(params).getEffect();
	}

	@API(desc = "投资者身份登记信息历史下载", operation = APIOperation.UPDATE,auth = APIAuth.YES)
	public String historyDownload(SqlParam<CustRegisterInfoh> params) throws Exception {
		try {
			Map<String, Object> param = params.getParamsDirect();
			String processInstanceId = (String) param.get("processInstanceId");
			param.put("modelClassName", CustRegisterInfoh.class.getName());
			param.put("action", "findCustRegisterInfos");
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
					log.error("投资者身份信息登记历史下载，更新文件路径失败！", e);
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
