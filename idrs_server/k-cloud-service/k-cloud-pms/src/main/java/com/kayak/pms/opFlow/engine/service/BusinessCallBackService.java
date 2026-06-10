package com.kayak.pms.opFlow.engine.service;

import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.system.RequestSupport;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.busi.CommonWorkflowCallback;
import com.kayak.pms.opFlow.engine.entity.ProcessInstance;
import com.kayak.pms.opFlow.engine.handlers.RefuseHandler;
import com.kayak.pms.opFlow.engine.model.Execution;
import com.kayak.pms.opFlow.engine.utils.RemoteInvokeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 07/09/2017.
 */
public class BusinessCallBackService {
    private static final Logger logger = LoggerFactory.getLogger(BusinessCallBackService.class);
    private ProcessInstance processInstance;
    private String callbackUrl;

    public BusinessCallBackService(ProcessInstance processInstance, String callbackUrl) {
        this.processInstance = processInstance;
        this.callbackUrl = callbackUrl;
    }

    public void invoke(String processState) {
        invoke(processState, new HashMap<String, Object>());
    }

    public void invoke(String processState, Map<String, Object> submitParams) {
        Execution execution = new Execution(processInstance);
        execution.setBusinessCallbackFlag(processState);
        execution.setLatestSubmitParams(submitParams);
        Map<String, Object> otherParams = new HashMap<String, Object>();
        otherParams.put("execution", execution);
        RequestSupport.setUserParameters(otherParams);

        CommonWorkflowCallback commonWorkflowCallback = new CommonWorkflowCallback();
        commonWorkflowCallback.busiRefuseProcessCallback();

        if (this.callbackUrl != null) {
            RemoteInvokeUtil.restInvoke(this.callbackUrl, execution);
        }
    }

    private void doAllProcess(String controllerClass, String controllerMethod) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = Class.forName(controllerClass);
        Object controller = SpringContextHolder.getBean(clazz);
        Method method = clazz.getMethod(controllerMethod);
        method.invoke(controller);
    }

    private void doSingleProcess(String controllerClass, Execution execution) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (StringHelper.isNotEmpty(controllerClass)) {
            RemoteInvokeUtil.restInvoke(controllerClass, execution);
        } else {
            Class<?> clazz = Class.forName(controllerClass);
            RefuseHandler instance = (RefuseHandler) clazz.newInstance();
            instance.refuse(execution);
        }
    }

}
