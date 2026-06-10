package com.kayak.rpt.dataMerge.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.dataMerge.dao.CustomerDataMergeDao;
import com.kayak.rpt.dataMerge.model.CustomerDataMergeModel;
import com.kayak.rpt.zz.historyInfo.model.CustTransInfoh;
import com.kayak.rpt.zz.historyInfo.model.CustVolRegisterInfoh;
import com.kayak.rpt.zz.historyInfo.service.comm.ZtHttpHelper;
import com.kayak.rpt.zz.historyInfo.service.comm.ZtService;
import com.kayak.rpt.zz.manage.model.BaseReportExportLog;
import com.kayak.rpt.zz.manage.model.InvestorSubHoldInfo;
import com.kayak.rpt.zz.manage.model.TrCustRegisterInfo;
import com.kayak.rpt.zz.manage.service.BaseReportExportLogService;
import com.kayak.utils.ExcelWriter;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@APIDefine(desc = "客户识别标识合并", model = CustomerDataMergeModel.class)
public class CustomerDataMergeService {

    protected static final Logger log = LoggerFactory.getLogger(CustomerDataMergeService.class);

    @Autowired
    private CustomerDataMergeDao customerDataMergeDao;
    @Autowired
    private ZtHttpHelper ztHttpHelper;
    @Autowired
    private ZtService ztService;
    @Autowired
    ExcelWriter excelWriter;
    @Autowired
    BaseReportExportLogService baseReportExportLogService;

    @API(desc = "查询客户识别标识合并指令", auth = APIAuth.YES)
    public SqlResult<CustomerDataMergeModel> findCustomerAccountMergeOrderInfo(SqlParam<CustomerDataMergeModel> params) throws Exception {
        return customerDataMergeDao.getCustomerAccountMergeInfo(params);
    }

    @API(desc = "新增客户识别标识合并指令", auth = APIAuth.YES)
    public String putCustomerAccountMergeOrderInfo(SqlParam<CustomerDataMergeModel> params) throws Exception {
        /**先校验合并前后的识别标识是否存在,该功能仅做数据合并，不做数据修改，其一客户识别标识不存在时返回提示信息*/
        boolean is_exists = customerDataMergeDao.judgeCustNoExists(params);
        if(!is_exists){
            return RequestSupport.updateReturnJson(false, "输入客户识别标识不存在!", null).toString();
        }
        UpdateResult updateResult = customerDataMergeDao.putCustomerAccountMergeInfo(params);
        return RequestSupport.updateReturnJson(true, "新增指令成功!", null).toString();
    }

    @API(desc = "删除客户识别标识合并指令", auth = APIAuth.YES)
    public String removeMergeOrderInfo(SqlParam<CustomerDataMergeModel> params) throws Exception {
        /**校验合并指令状态:已经合并完成的数据不允许删除*/
        if("03".equals(params.getModel().getMrgSts())){
            return RequestSupport.updateReturnJson(false, "不允许删除合并完成的指令!", null).toString();
        }
        customerDataMergeDao.deleteCustomerAccountMergeInfo(params);
        return RequestSupport.updateReturnJson(true, "删除指令成功!", null).toString();
    }

    @API(desc = "执行客户识别标识合并指令",operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String doAccountMergeOperation(SqlParam<CustomerDataMergeModel> params) throws Exception {
        int times = 0;
        /**校验合并指令状态:合并完成的数据不允许再次合并,需要重复合并需要新增合并指令*/
        if("03".equals(params.getModel().getMrgSts())){
            return RequestSupport.updateReturnJson(false, "请勿重复执行合并操作!", null).toString();
        }

        //合并前调用中台接口接入数据
        for (String inv : Arrays.asList(params.getParams().get("cstmAccF").toString(), params.getParams().get("cstmAccT").toString())) {
            Map<String, Object> loopParams = new HashMap<>();
            loopParams.put("custNo", inv);
            loopParams.put("start", 0);
            loopParams.put("limit", 10);
            loopParams.put("order_id", params.getModel().getId());
            if (!downloadLoopZtInvestorData(loopParams, ++times == 1?true:false)) {
                return RequestSupport.updateReturnJson(false, "查询投资者历史数据失败!", null).toString();
            }
        }
        //客户合并处理
        boolean mergeResult = customerDataMergeDao.doCustomerAccountMerge(params);

        if(mergeResult) {
            return RequestSupport.updateReturnJson(true, "合并执行执行完成!", null).toString();
        } else {
            return RequestSupport.updateReturnJson(false, "合并执行执行失败!", null).toString();
        }
    }

    /**
     * 从中台循环接数
     * @param params
     * @return
     * @throws Exception
     */
    private boolean downloadLoopZtInvestorData (Map<String, Object> params, boolean is_del) throws Exception {
        List<String> code_arr = Arrays.asList("api_app_cust_vol_register_sub_info","api_app_cust_vol_register_info","api_app_cust_trans_info");
        String err_code = "";
        String port_address = "";
        try {
            //将中台对应的母子产品持有及交易数据入库
            for (String code : code_arr) {
                switch (code) {
                    case "api_app_cust_vol_register_sub_info" :
                        port_address = SysUtil.getSystemParamsByParaid("inv_sh_port_1");
                        break;
                    case "api_app_cust_vol_register_info" :
                        port_address = SysUtil.getSystemParamsByParaid("inv_hd_port_1");
                        break;
                    case "api_app_cust_trans_info" :
                        port_address = SysUtil.getSystemParamsByParaid("inv_dd_port_1");
                        break;
                    default: port_address = "";
                }
                err_code = code;
                params.put("S_CUST_NO",params.get("custNo"));
                ztHttpHelper.saveLoopData (port_address, params, obj -> {
                    JSONArray contentArray=(JSONArray) obj;
                    ztService.batchSave(code.replace("api_", "download_"), params, is_del, contentArray);
                    return true;
                });
            }
        } catch (Exception e) {
            log.error("中台接口同步投资者历史数据异常:接口代码为" + err_code);
            return false;
        }
        return true;
    }


    @API(desc = "投资者合并数据下载", operation = APIOperation.UPDATE,auth = APIAuth.YES)
    public String customerHisDataDownload(SqlParam<CustomerDataMergeModel> params) throws Exception {
        try {
            Map<String, Object> param = params.getParamsDirect();
            String processInstanceId = (String) param.get("processInstanceId");
            param.put("dataExportDict", "1");
            param.put("type", "0");
            param.put("unToDict", "");
            param.put("action_params", "{order_id:"+param.get("id")+",mrg_typ:"+param.get("mrg_typ")+"}");
            List<String> model_arr = Arrays.asList(InvestorSubHoldInfo.class.getName(), CustVolRegisterInfoh.class.getName(), CustTransInfoh.class.getName());/**子产品持有、母产品持有、交易明细*/
            List<String> action_arr = Arrays.asList("queryInvestorSubHoldRemark", "findCustVolRegisterRemark", "findCustTransRemark");
            List<String> name_arr = Arrays.asList("投资者持有信息(子产品)", "投资者持有信息", "投资者明细信息");
            List<String> header_arr = customerDataMergeDao.getHeaderSqlInfo();//获取账户合并前/后投资者历史数据表头配置

            Runnable runnable = () -> {
                List<Map<String, String>> resMapList = new ArrayList<>();
                for (int i=0; i<model_arr.size(); i++) {
                    param.put("modelClassName", model_arr.get(i));
                    param.put("action", action_arr.get(i));
                    param.put("headers", header_arr.get(i));
                    param.put("dataExportName", name_arr.get(i) + ("1".equals(param.get("mrg_typ"))?"合并后":"合并前"));
                    param.put("isSingleFile", "false");
                    resMapList.add(excelWriter.downloadLoopEx(param));
                }

                Map<String, String> s3Result = null;
                try {
                    s3Result = excelWriter.doZipUploadS3(resMapList);
                } catch (Exception e) {
                    log.error("合并投资者历史数据文件并上传S3文件到指定路径异常！");
                    e.printStackTrace();
                }

                BaseReportExportLog exportLog = new BaseReportExportLog();
                exportLog.setFilePath(s3Result.get("local"));
                exportLog.setRemotePath(s3Result.get("remote"));
                exportLog.setReportName(s3Result.get("reportName"));
                exportLog.setDataTime(DateUtil.getCurrentDate());
                exportLog.setProcessInstanceId(processInstanceId);
                exportLog.setFileStatus("2");
                try {
                    baseReportExportLogService.updateFileNamePath(exportLog);
                } catch (Exception e) {
                    log.error("投资者身份信息下载，更新文件路径失败！", e);
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
