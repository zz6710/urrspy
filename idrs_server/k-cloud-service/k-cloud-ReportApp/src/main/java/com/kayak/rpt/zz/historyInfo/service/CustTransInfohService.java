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
import com.kayak.rpt.zz.historyInfo.dao.CustTransInfohDao;
import com.kayak.rpt.zz.historyInfo.model.CustTransInfoh;
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
@APIDefine(desc = "投资者明细历史信息服务", model = CustTransInfoh.class)
public class CustTransInfohService {

	private static final Logger log = LoggerFactory.getLogger(CustTransInfohService.class);

	@Autowired
	private CustTransInfohDao custTransInfohDao;
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

	@API(desc = "查询投资者明细历史信息信息", auth = APIAuth.YES)
	public SqlResult<CustTransInfoh> findCustTransInfohs(SqlParam<CustTransInfoh> params) throws Exception {
		String port_address = SysUtil.getSystemParamsByParaid("inv_dd_port_2");
		Map<String, Object> pageParam = params.getParams();

		if(pageParam.get("reportDate")!= null ){
			pageParam.put("S_DEAL_DT",pageParam.get("reportDate"));
			pageParam.put("REPORT_DATE",pageParam.get("reportDate"));
		}

		if(pageParam.get("prodCode")!= null ){
			pageParam.put("PROD_CODE", pageParam.get("prodCode"));
		}

		if(pageParam.get("custNo")!= null ){
			pageParam.put("S_CUST_NO", pageParam.get("custNo"));
		}

		if(pageParam.get("hostCustNo")!= null ){
			pageParam.put("TA_ID", pageParam.get("hostCustNo"));
		}

		if(pageParam.get("agentBankCode")!= null ){
			pageParam.put("AGENT_BANK_CODE", pageParam.get("agentBankCode"));
		}

		if(pageParam.get("agentBankName")!= null ){
			pageParam.put("AGENT_BANK_NAME", pageParam.get("agentBankName"));
		}

		if(pageParam.get("busiCode")!= null ){
			pageParam.put("S_BUSI_CODE", StringUtils.replace((String) pageParam.get("busiCode"), ",", "|"));
			pageParam.remove("busiCode");// 防止中台误查
		} else {
			pageParam.put("S_BUSI_CODE", "^.*$");
		}

		//根据参数判断调用中台接口数据集
		if (pageParam.get("reportDate") == null) {
			port_address = SysUtil.getSystemParamsByParaid("inv_dd_port_1");//数据日期为空，调用客户查询数据集
		}

		if (pageParam.get("custNo") == null) {
			port_address = SysUtil.getSystemParamsByParaid("inv_dd_port_0");//客户为空，调用数据日期查询数据集
		}

		//清空表且仅入库中台接口当前查询页数据
		int total_count =
		ztHttpHelper.saveData(port_address,pageParam, obj -> {
			JSONArray contentArray=(JSONArray) obj;
			ztService.batchSave("api_app_cust_trans_info", pageParam, true, contentArray);
			return true;
		});

		params.setStart(0);
		SqlResult<CustTransInfoh> handledResult = custTransInfohDao.findCustTransInfohs(params);

		//判断是否进行文档下载数据脱敏操作
		if("true".equals(params.getModel().getIsDesensitization())){
			int maxCount = handledResult.getRows().size();
			for(int i=0; i<maxCount; i++){
				CustTransInfoh customer = handledResult.getRows().get(i);
				deSensitiveModel(customer);
			}
		}

		handledResult.setResults(total_count);
		return handledResult;
	}

	public SqlResult<CustTransInfoh> findCustTransRemark(SqlParam<CustTransInfoh> params) throws Exception {
		return custTransInfohDao.findCustTransRemark(params);
	}

	@API(desc = "添加投资者明细历史信息", params = "bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,part_register_date", auth = APIAuth.NO)
	public int addCustTransInfoh(SqlParam<CustTransInfoh> params) throws Exception {
		return custTransInfohDao.addCustTransInfoh(params).getEffect();
	}
	
	@API(desc = "修改投资者明细历史信息", params = "bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,part_register_date", operation = APIOperation.UPDATE, auth = APIAuth.NO)
	public int updateCustTransInfoh(SqlParam<CustTransInfoh> params) throws Exception {
		return custTransInfohDao.updateCustTransInfoh(params).getEffect();
	}
	
	@API(desc = "删除投资者明细历史信息", params = "bank_code,trans_serno,contract_no,fnc_trans_acct_no,host_cust_no,cust_no,cust_name,acct_no,acct_loc_code,is_agent,agent_bank_code,agent_bank_name,agent_regu_code,prod_code,busi_code,busi_regu_code,ack_date,ack_time,cur,ack_amt,convert_rmb,nav,ack_vol,fee_amt,channel_flag,inputuser,remark,son_share_code,spe_channel_flag,register_serno,imp_date,register_date,register_status,part_register_date", auth = APIAuth.NO)
	public int deleteCustTransInfoh(SqlParam<CustTransInfoh> params) throws Exception {
		return custTransInfohDao.deleteCustTransInfoh(params).getEffect();
	}

	@API(desc = "投资者明细信息登记历史下载", operation = APIOperation.UPDATE,auth = APIAuth.YES)
	public String historyDownload(SqlParam<CustTransInfoh> params) throws Exception {
		try {
			Map<String, Object> param = params.getParamsDirect();
			String processInstanceId = (String) param.get("processInstanceId");
			param.put("modelClassName", CustTransInfoh.class.getName());
			param.put("action", "findCustTransInfohs");
			String actionParamsStr = Tools.obj2Str(param.get("action_params"));
			param.put("action_params", Tools.str2Json(Tools.obj2Str(actionParamsStr.replace("}",",\"isDesensitization\":\"true\"}"))));//是否进行数据脱敏操作标识,请勿随意改动
			Runnable runnable = () -> {
				Map<String, String> resMap = excelWriter.downloadEx(param);
				BaseReportExportLog exportLog = new BaseReportExportLog();
				exportLog.setFilePath(resMap.get("local"));
				exportLog.setRemotePath(resMap.get("remote"));
				exportLog.setProcessInstanceId(processInstanceId);
				exportLog.setFileStatus("2");
				try {
					baseReportExportLogService.updateDataTimePath(exportLog);
				} catch (Exception e) {
					log.error("投资者明细信息登记历史下载，更新文件路径失败！", e);
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
	}

	/**
	 * 中债三期脱敏规则如括号(对于三个汉字以内（包含三个汉字）的名称保留最后一个字，其余长度的名称不变形)
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	private CustTransInfoh deSensitiveModel(CustTransInfoh customer) {
		String finalCustomerName = customer.getCustName();
		if(finalCustomerName.length() <= 3) {
			customer.setCustName(finalCustomerName.substring(finalCustomerName.length()-1));
		}
		return customer;
	}

}
