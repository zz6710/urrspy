package com.kayak.pms.opFlow.engine.handlers.impl;

import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysBeans;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.engine.busi.CommonWorkflowCallback;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceConstant;
import com.kayak.pms.opFlow.engine.dao.ProcessInstanceDao;
import com.kayak.pms.opFlow.engine.entity.ProcessInstance;
import com.kayak.pms.opFlow.engine.handlers.IHandler;
import com.kayak.pms.opFlow.engine.helper.ClassHelper;
import com.kayak.pms.opFlow.engine.model.Execution;
import com.kayak.pms.opFlow.engine.model.TransitionModel;
import com.kayak.pms.opFlow.engine.service.WorkflowEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 19/03/2017.
 */
public class EndProcessHandler implements IHandler {
    private static Logger log = LoggerFactory.getLogger(EndProcessHandler.class);
    private String busiUrl;

    /**
     * 1、完成当前流程实例
     * 2、处理父工作流
     * 3、处理表单数据
     *
     * @param execution
     */
    @Override
    public void handle(Execution execution) {
        WorkflowEngine engine = execution.getEngine();
        ProcessInstanceDao processInstanceDao = SysBeans.getBean("processInstanceDao");
        ProcessInstance processInstance = execution.getProcessInstance();
        //完成当前流程实例
        processInstance.setFinishDate(DateHelper.getCurrentDate());
        processInstance.setFinishTime(DateHelper.getCurrentTime());
        processInstance.setCurrentStatus(ProcessInstanceConstant.FINISH);
        processInstanceDao.complete(processInstance);
        // 不走公共的rest
//        Object success;
//        if ("true".equalsIgnoreCase(Global.getGlobalConf("IS_SERVICE"))) {
//            try {
//                RestTemplate restTemplate = ApplicationContextHolder.getRestTemplate();
//                String result = restTemplate.postForObject(busiUrl, JsonHelper.toJson(execution.getLatestSubmitParams()), String.class);
//                Map map = JsonHelper.fromJson(result, Map.class);
//                success = map.get("success");
//
//            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
//                throw new WorkflowException("调用接口失败");
//            }
//
//            if (!(Boolean) success) {
//                throw new WorkflowException("业务处理失败");
//            }
//        } else {
            //增加回调参数
            Map<String, Object> otherParams = new HashMap<String, Object>();
            otherParams.putAll(execution.getLatestSubmitParams());
            execution.setBusinessCallbackFlag(ProcessInstanceConstant.FINISH);
            //TODO 注释 防止出现 org.json 的栈溢出
            otherParams.put("execution", execution);
            // 用于判断流程属于结束状态, 不执行业务的状态修改
            otherParams.put("process_status", ProcessInstanceConstant.FINISH);
            RequestSupport.setUserParameters(otherParams);

        try {
            CommonWorkflowCallback commonWorkflowCallback = (CommonWorkflowCallback) ClassHelper.newInstance("com.kayak.opFlow.busi.CommonWorkflowCallback");
            commonWorkflowCallback.busiEndProcessCallback();
        } catch (Exception e) {
            log.error("流程回调失败", e);
        }
//        }
    }

    /**
     * 结束节点无输出变迁
     */
    public List<TransitionModel> getOutputs() {
        return Collections.emptyList();
    }

    public String getBusiUrl() {
        return busiUrl;
    }

    public void setBusiUrl(String busiUrl) {
        this.busiUrl = busiUrl;
    }
}
