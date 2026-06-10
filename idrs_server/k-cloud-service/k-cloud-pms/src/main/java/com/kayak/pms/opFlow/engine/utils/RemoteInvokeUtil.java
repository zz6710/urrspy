package com.kayak.pms.opFlow.engine.utils;

import com.kayak.pms.opFlow.engine.context.ApplicationContextHolder;
import com.kayak.pms.opFlow.engine.entity.result.ResponseResult;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import com.kayak.pms.opFlow.engine.model.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class RemoteInvokeUtil {
    private static Logger logger = LoggerFactory.getLogger(RemoteInvokeUtil.class);

    public static Object restInvoke(String url, Execution execution) {
        RestTemplate restTemplate = ApplicationContextHolder.getRestTemplate();
        ResponseResult responseResult;
        try {
            responseResult = restTemplate.postForObject("http://" + url, execution, ResponseResult.class);
            if (responseResult == null) {
                logger.error("远程调用接口{}返回为null", url);
                throw new WorkflowException("服务器异常");
            }
            if (ResponseResult.SUCCESS.equals(responseResult.getStatus())) {
                return  responseResult.getData();
            } else {
                throw new WorkflowException(responseResult.getMessage());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new WorkflowException("服务器异常");
        }
    }

}
