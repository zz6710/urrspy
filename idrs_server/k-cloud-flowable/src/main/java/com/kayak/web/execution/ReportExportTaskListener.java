package com.kayak.web.execution;

import com.alibaba.fastjson2.JSONObject;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.DateUtil;
import com.kayak.utils.SysUtil;
import com.kayak.utils.Tools;
import com.kayak.web.business.domain.BaseReportExportLog;
import com.kayak.web.business.mapper.BaseReportExportLogMapper;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.variable.api.persistence.entity.VariableInstance;

import java.util.HashMap;
import java.util.Map;

/**
 * 报表导出审批：发起
 * @author lizhongsi
 */
public class ReportExportTaskListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        Map<String, VariableInstance> variableInstances = delegateExecution.getParent().getVariableInstances();
        String processInstanceId = delegateExecution.getProcessInstanceId();
        // 数据日期
        VariableInstance actionParams = variableInstances.get("action_params");
        if (actionParams != null) {
            String actionParamsJson = actionParams.getTextValue();
            HashMap actionParamsMap = JSONObject.parseObject(actionParamsJson, HashMap.class);
            String reportDate = (String) actionParamsMap.get("reportDate");
            if (Tools.isNotEmpty(reportDate)) {
                // 报表名称
                VariableInstance dataExportName = variableInstances.get("dataExportName");
                BaseReportExportLogMapper mapper = SysBeans.getBean("baseReportExportLogMapper");
                // 如果是多天，需要分别插入
                String[] reportDates = reportDate.split(",");
                for (String date : reportDates) {
                    BaseReportExportLog exportLog = new BaseReportExportLog();
                    exportLog.setApplyTime(DateUtil.getTimestamp19());
                    if (dataExportName != null) {
                        exportLog.setReportName(dataExportName.getTextValue());
                    }
                    exportLog.setFileStatus("1"); //未生成
                    exportLog.setUserid(SysUtil.getCurrentUserId());
                    exportLog.setProcessInstanceId(processInstanceId);
                    exportLog.setDataTime(date);
                    mapper.insert(exportLog);
                }
            }
        }
    }
}
