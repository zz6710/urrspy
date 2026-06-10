package com.kayak.rpt.zz.manage.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.historyInfo.service.comm.ZtHttpHelper;
import com.kayak.rpt.zz.historyInfo.service.comm.ZtService;
import com.kayak.rpt.zz.manage.dao.InvestorSubHoldDao;
import com.kayak.rpt.zz.manage.enums.OperatorEnum;
import com.kayak.rpt.zz.manage.model.BaseReportExportLog;
import com.kayak.rpt.zz.manage.model.InvestorSubHoldInfo;
import com.kayak.rpt.zz.operate.service.InvestorSubHoldMarkService;
import com.kayak.utils.ExcelWriter;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "投资者持有信息(子产品)", model = InvestorSubHoldInfo.class)
public class investorSubHoldService {

    private static final Logger log = LoggerFactory.getLogger(investorSubHoldService.class);

    @Autowired
    private InvestorSubHoldDao investorSubHoldDao;

    @Autowired
    private InvestorSubHoldMarkService subHoldMarkService;

    @Autowired
    private ZtHttpHelper ztHttpHelper;
    @Autowired
    private ZtService ztService;
    @Autowired
    private BaseReportExportLogService baseReportExportLogService;
    @Autowired
    private ExcelWriter excelWriter;

    @API(desc = "查询投资者持有(子产品)信息", auth = APIAuth.YES)
    public SqlResult<InvestorSubHoldInfo> queryInvestorSubHoldInfo(SqlParam<InvestorSubHoldInfo> params) throws Exception {
        String port_address = SysUtil.getSystemParamsByParaid("inv_sh_port_2");
        Map<String, Object> pageParam = params.getParams();

        if(pageParam.get("holdDate")!= null ){
            pageParam.put("S_HOLD_DATE",pageParam.get("holdDate"));
        }

        if(pageParam.get("custNo")!= null ){
            pageParam.put("S_CUST_NO", pageParam.get("custNo"));
        }

        if(pageParam.get("prodCode")!= null ){
            pageParam.put("PROD_CODE", pageParam.get("prodCode"));
        }

        if(pageParam.get("prodCodeS")!= null ){
            pageParam.put("PROD_CODE_S", pageParam.get("prodCodeS"));
        }

        if(pageParam.get("taId")!= null ){
            pageParam.put("TA_ID", pageParam.get("taId"));
        }

        if(pageParam.get("cur")!= null ){
            pageParam.put("S_CUR", StringUtils.replace((String) pageParam.get("cur"), ",", "|"));
            pageParam.remove("cur");// 防止中台误查
        } else {
            pageParam.put("S_CUR", "^.*$");
        }

        if(pageParam.get("channelCode")!= null ){
            pageParam.put("SLLR_CD", pageParam.get("channelCode"));
        }

        if(pageParam.get("custType")!= null ){
            pageParam.put("S_CUST_TYPE", StringUtils.replace((String) pageParam.get("custType"), ",", "|"));
            pageParam.remove("custType");// 防止中台误查
        } else {
            pageParam.put("S_CUST_TYPE", "^.*$");
        }

        //若传入日期等于当前系统工作日,仅查询当日非历史表数据
        if (DateUtil.getSysWordDay().equals(pageParam.get("holdDate"))){
            return investorSubHoldDao.queryInvestorSubHoldInfoByCond(params, "app_cust_vol_register_sub_info");
        }

        //根据参数判断调用中台接口数据集
        if (pageParam.get("holdDate") == null) {
            port_address = SysUtil.getSystemParamsByParaid("inv_sh_port_1");//数据日期为空，调用客户查询数据集
        }

        if (pageParam.get("custNo") == null) {
            port_address = SysUtil.getSystemParamsByParaid("inv_sh_port_0");//客户为空，调用数据日期查询数据集
        }

        //调用中台接口接入数据
        int total_count =
        ztHttpHelper.saveData(port_address,pageParam, obj -> {
            JSONArray contentArray=(JSONArray) obj;
            ztService.batchSave("api_app_cust_vol_register_sub_info", pageParam, true, contentArray);
            return true;
        });
        params.setStart(0);
        SqlResult<InvestorSubHoldInfo> sqlResult= investorSubHoldDao.queryInvestorSubHoldInfoByCond(params, "app_cust_vol_register_sub_info_h");

        sqlResult.setResults(total_count);
        return sqlResult;
    }

    /**
     * 查询合并操作合并数据
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<InvestorSubHoldInfo> queryInvestorSubHoldRemark(SqlParam<InvestorSubHoldInfo> params) throws Exception {
        return investorSubHoldDao.queryInvestorSubHoldRemark(params);
    }

    @API(desc = "投资者持有信息登记(子产品)历史下载", operation = APIOperation.UPDATE,auth = APIAuth.YES)
    public String historyDownload(SqlParam<InvestorSubHoldInfo> params) throws Exception {
        try {
            Map<String, Object> param = params.getParamsDirect();
            String processInstanceId = (String) param.get("processInstanceId");
            param.put("modelClassName", InvestorSubHoldInfo.class.getName());
            param.put("action", "queryInvestorSubHoldInfo");
            String actionParamsStr = Tools.obj2Str(param.get("action_params"));
            String holdDate = JSONObject.parseObject(actionParamsStr).getString("holdDate");
            Runnable runnable = () -> {
                Map<String, String> resMap = excelWriter.downloadEx(param);
                BaseReportExportLog exportLog = new BaseReportExportLog();
                exportLog.setFilePath(resMap.get("local"));
                exportLog.setRemotePath(resMap.get("remote"));
                exportLog.setDataTime(holdDate);
                exportLog.setProcessInstanceId(processInstanceId);
                exportLog.setFileStatus("2");
                try {
                    baseReportExportLogService.updateDataTimePath(exportLog);
                } catch (Exception e) {
                    log.error("投资者持有信息登记(子产品)历史下载，更新文件路径失败！", e);
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    @API(desc = "新增投资者持有(子产品)信息", auth = APIAuth.YES)
    public String putInvestorSubHoldInfo(SqlParam<InvestorSubHoldInfo> params) throws Exception {
        /**先校验客户识别标识是否存在,不存在则返回提示信息*/
        boolean is_exists = investorSubHoldDao.judgeInverstorExists(params);
        if(!is_exists){
            return RequestSupport.updateReturnJson(false, "不存在输入识别标识:"+params.getModel().getCustNo()+" 对应的投资者信息!", null).toString();
        }
        subHoldMarkService.addInvestorSubHoldMark(params.getModel(), "0");
        investorSubHoldDao.putInvestorSubHoldInfo(params);
        return RequestSupport.updateReturnJson(true, "新增投资者持有(子产品)信息成功!", null).toString();
    }

    @API(desc = "更新投资者持有(子产品)信息", auth = APIAuth.YES)
    public String updateInvestorSubHoldInfo(SqlParam<InvestorSubHoldInfo> params) throws Exception {
        subHoldMarkService.addInvestorSubHoldMark(params.getModel(), "1");
        investorSubHoldDao.updateInvestorSubHoldInfo(params);
        return RequestSupport.updateReturnJson(true, "更新投资者持有(子产品)信息成功!", null).toString();
    }

    @API(desc = "删除投资者持有(子产品)信息", auth = APIAuth.YES)
    public String removeInvestorSubHoldInfo(SqlParam<InvestorSubHoldInfo> params) throws Exception {
        subHoldMarkService.addInvestorSubHoldMark(params.getModel(), "2");
        investorSubHoldDao.deleteInvestorSubHoldInfo(params);
        return RequestSupport.updateReturnJson(true, "已删除投资者持有(子产品)信息!", null).toString();
    }

    public void importModifyInvSubHoldInfo(List<InvestorSubHoldInfo> investorSubHoldInfos) throws Exception {
        // 删除当天的导入变更记录
        Map<String, Object> params = new HashMap<>();
        params.put("createDate", DateUtil.getNowDate());
        investorSubHoldDao.deleteModifyInvestorSubHoldInfo(params);
        for (InvestorSubHoldInfo investorSubHoldInfo : investorSubHoldInfos) {
            Map<String, Object> map = BeanUtil.beanToMap(investorSubHoldInfo);
//            subHoldMarkService.addInvestorSubHoldMark(investorSubHoldInfo, OperatorEnum.MODIFY.getVal());
            investorSubHoldDao.putModifyInvestorSubHoldInfo(map);
        }
    }

}
