package com.kayak.dps.check.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.clear.service.business.BusinessBaseTaskService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.dao.BaseReportReloadLogDao;
import com.kayak.dps.app.model.BaseReportReloadLog;
import com.kayak.dps.app.service.ModelDataToGenService;
import com.kayak.dps.check.service.ReportDataValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ReportDataValidateController {

    private static Logger log = LoggerFactory.getLogger(ReportDataValidateController.class);

    @Autowired
    private ReportDataValidateService reportDataValidateService;

    @Autowired
    private ModelDataToGenService modelDataToGenService;

    @Autowired
    public BusinessBaseTaskService businessBaseTaskService;

    @Autowired
    private BaseReportReloadLogDao baseReportReloadLoDao;

    /**
     * 应用层加工任务
     */
    @RequestMapping(value="/reportDataTask.action")
    public String reportDataTaskExecute() throws Exception {
        Map<String, Object> vueParams = RequestSupport.getParameters();//获取前台传递参数

        String deal_date = String.valueOf(vueParams.get("settleDate"));//处理日期
        String indexCode = String.valueOf(vueParams.get("indexCode"));//指标代码
        if (!RequestSupport.getCanCodeMore(vueParams)) {
            return RequestSupport.updateReturnJson(false,"检测到sql注入！！！",null).toString();
        }

        String maxId = "";
        String date = ObjectUtil.isNotEmpty(vueParams.get("settleDate")) ? deal_date : String.valueOf(vueParams.get("reportDate"));
        Map<String, Object> map = new HashMap<>();
        map.put("paravalue","1");
        map.put("oldParavalue","0");
        map.put("paraid","90000062001");

        BaseReportReloadLog baseReportReloadLog = new BaseReportReloadLog();
        baseReportReloadLog.setMenuId(String.valueOf(vueParams.get("menuId")));
        baseReportReloadLog.setReportDate(date);
        baseReportReloadLog.setStartDate(DateUtil.getNowDate());
        baseReportReloadLog.setStartTime(DateUtil.getNowTime());
        baseReportReloadLog.setResultStatus("正在"+vueParams.get("buttonName")+"中");
        baseReportReloadLog.setUserName(SysUtil.getLoginUserid());

        try{
            if (businessBaseTaskService.upTaskStatus(map) > 0) {
                baseReportReloadLoDao.addBaseReportReloadLog(baseReportReloadLog);
                List<SqlRow> baseReportReloadLogs = baseReportReloadLoDao.findIdBaseReportReloadLogs(baseReportReloadLog);
                if (CollectionUtil.isNotEmpty(baseReportReloadLogs)) {
                    maxId = baseReportReloadLogs.get(0).getString("id");
                }

                // 查询报表大类
                Map<String, Object> param= new HashMap<>();
                param.put("reportTable", String.valueOf(vueParams.get("reportTable")));
                List<SqlRow> sqlRows = reportDataValidateService.getReportTable(param);
                String reportName = "";
                String reportType = String.valueOf(vueParams.get("reportType"));
                if (CollectionUtil.isNotEmpty(sqlRows)) {
                    reportName = String.valueOf(sqlRows.get(0).get("table_name"));
                    reportType = String.valueOf(sqlRows.get(0).get("report_catgory"));
                }

                Map<String, String> params = new HashMap<>();
                params.put("reportType", reportType);//报表大类
                params.put("reportTable", String.valueOf(vueParams.get("reportTable")));//报表名称
                params.put("indexType", String.valueOf(vueParams.get("indexType")));//指标类型
                params.put("indexCode", indexCode);
                params.put("reportDate", String.valueOf(vueParams.get("reportDate")));//报送日期
                params.put("dealDate", deal_date);//数据日期

                // 查询指标配置是否存在
                if (ObjectUtil.isNotEmpty(indexCode)  && !"null".equals(indexCode) && !reportDataValidateService.exitIndexCode(params)) {
                    // 执行完成后更新数据分布式锁
                    map.put("paravalue","0");
                    map.put("oldParavalue","1");
                    businessBaseTaskService.upTaskStatus(map);
                    return RequestSupport.updateReturnJson(false,"未找到校验指标",null).toString();
                }
                //根据报送日期推算数据日期并校验指标日期是否符合要求
                if (!"".equals(reportName) && ("null").equals(reportDataValidateService.calDataDateByReportDate(params))) {
                    // 执行完成后更新数据分布式锁
                    map.put("paravalue","0");
                    map.put("oldParavalue","1");
                    businessBaseTaskService.upTaskStatus(map);
                    return RequestSupport.updateReturnJson(false,"请输入数据日期",null).toString();
                }
                //将计算的数据日期进行覆盖
                if(!"".equals(reportName)){
                    deal_date = reportDataValidateService.calDataDateByReportDate(params);
                    params.put("dealDate", deal_date);
                }

                reportDataValidateService.execute(deal_date, params);

                // 执行完成后更新数据分布式锁
                map.put("paravalue","0");
                map.put("oldParavalue","1");
                businessBaseTaskService.upTaskStatus(map);

                baseReportReloadLog.setId(maxId);
                baseReportReloadLog.setReportDate(deal_date);
                baseReportReloadLog.setEndDate(DateUtil.getNowDate());
                baseReportReloadLog.setEndTime(DateUtil.getNowTime());
                baseReportReloadLog.setResultStatus(vueParams.get("buttonName")+"成功");
                String str1 = ObjectUtil.isNotEmpty(vueParams.get("indexType")) ? String.valueOf(vueParams.get("indexType")) : "";
                String str2 = ObjectUtil.isNotEmpty(vueParams.get("indexCode")) ? String.valueOf(vueParams.get("indexCode")) : "";
                String resultInfo = "";
                if(!"".equals(reportName)){
                    resultInfo = "报表名称：" + reportName + "<br/>指标类型：" + str1  + "<br/>指标代码：" + str2;
                }else{
                    List<SqlRow> tableList = reportDataValidateService.getReportTableList(params);
                    int k = 1;
                    for(SqlRow tableTmp : tableList){
                        reportName =reportName+k+"、"+tableTmp.getString("table_name") + "<br/>";
                        k++;
                    }
                    resultInfo="报表名称：<br/>"+reportName+"指标类型：" + str1  + "<br/>指标代码：" + str2;
                }
                baseReportReloadLog.setResultInfo(resultInfo);
                baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);
            } else {
                return RequestSupport.updateReturnJson(false, "正在"+vueParams.get("buttonName")+"，请稍后重试！", null).toString();
            }
        } catch (Exception e) {
            // 执行失败后更新数据分布式锁
            map.put("paravalue","0");
            map.put("oldParavalue","1");
            businessBaseTaskService.upTaskStatus(map);

            baseReportReloadLog.setId(maxId);
            baseReportReloadLog.setReportDate(deal_date);
            baseReportReloadLog.setEndDate(DateUtil.getNowDate());
            baseReportReloadLog.setEndTime(DateUtil.getNowTime());
            baseReportReloadLog.setResultStatus(vueParams.get("buttonName")+"失败");
            baseReportReloadLog.setResultInfo(e.getMessage());
            baseReportReloadLoDao.updateBaseReportReloadLog(baseReportReloadLog);

            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }

        return RequestSupport.updateReturnJson(true,"报送指标校验完成！",null).toString();
    }

    /**
     * 其他服务调用
     */
    @RequestMapping(value="/handleReportDataTask.action")
    public String handleReportDataTaskExecute(@RequestBody Map<String, Object> variables){
        Map<String, Object> vueParams = RequestSupport.getParameters();//获取前台传递参数
        vueParams.putAll(variables);
        String deal_date = String.valueOf(vueParams.get("settleDate"));//处理日期
        String indexCode = String.valueOf(vueParams.get("indexCode"));//指标代码
        if (!RequestSupport.getCanCodeMore(vueParams)) {
            return RequestSupport.updateReturnJson(false,"检测到sql注入！！！",null).toString();
        }
        Map<String, String> params = new HashMap<>();
        params.put("reportType", String.valueOf(vueParams.get("reportType")));//报表大类
        params.put("reportTable", String.valueOf(vueParams.get("reportTable")));//报表名称
        params.put("indexType", String.valueOf(vueParams.get("indexType")));//指标类型
        params.put("indexCode", indexCode);
        params.put("reportDate", String.valueOf(vueParams.get("reportDate")));//报送日期
        params.put("dealDate", deal_date);//数据日期
        params.put("id", ObjectUtil.isNotEmpty(vueParams.get("id")) ? String.valueOf(vueParams.get("id")) : "0");//单条数据
        params.put("taId", ObjectUtil.isNotEmpty(vueParams.get("taId")) ? String.valueOf(vueParams.get("taId")) : "");
        params.put("custNo", ObjectUtil.isNotEmpty(vueParams.get("custNo")) ? String.valueOf(vueParams.get("custNo")) : "");
        try{

            // 查询指标配置是否存在
            if (ObjectUtil.isNotEmpty(indexCode)  && !"null".equals(indexCode) && !reportDataValidateService.exitIndexCode(params)) {
                return RequestSupport.updateReturnJson(false,"未找到校验指标",null).toString();
            }
            //根据报送日期推算数据日期并校验指标日期是否符合要求
            if (("null").equals(reportDataValidateService.calDataDateByReportDate(params))) {
                return RequestSupport.updateReturnJson(false,"请输入数据日期",null).toString();
            }
            //将计算的数据日期进行覆盖
            deal_date = reportDataValidateService.calDataDateByReportDate(params);
            params.put("dealDate", deal_date);

            reportDataValidateService.execute(deal_date, params);
            return RequestSupport.updateReturnJson(true,"报送指标校验完成！",null).toString();
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }

    }
}

