package com.kayak.utils;

import com.kayak.common.entity.result.ResponseResult;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.spring.ApplicationContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

/**
 * 远程接口调用工具类
 *
 * @author yuanjinqiao
 * @date 2021-2-3
 */
public class RemoteInvokeUtil {

    private static Logger logger = LoggerFactory.getLogger(RemoteInvokeUtil.class);

    /**
     * 工作流审批过程中接口、拦截器等回调
     *
     * @param url
     * @param execution 审批过程中上下文参数
     * @return
     */
    public static Object restPostInvoke(String url, Map<String, Object> execution) {
        RestTemplate restTemplate = ApplicationContextUtils.getRestTemplate();
        ResponseResult responseResult;
        try {
            responseResult = restTemplate.postForObject("http://" + url, execution, ResponseResult.class);
            if (responseResult == null) {
                logger.error("远程调用接口{}返回为null", url);
                throw new WorkflowException("服务器异常");
            }
            if (ResponseResult.SUCCESS.equals(responseResult.getStatus())) {
                return responseResult.getData();
            } else {
                throw new WorkflowException(responseResult.getMessage());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new WorkflowException("服务器异常");
        }
    }

    /**
     * 远程接口调用（审批完业务回调）
     *
     * @param appName
     * @param url
     * @param params
     * @return
     */
    public static Object requestPostJson(String appName, String url, String userid, Object params) {
        RestTemplate restTemplate = ApplicationContextUtils.getRestTemplate();

        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        requestHeaders.add("Authorization", userid);
        // HttpEntity
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, requestHeaders);

        Object body = restTemplate.postForEntity("http://" + appName + url, requestEntity, Object.class).getBody();

        return body;
    }

    /**
     * 远程接口调用（审批完业务回调）
     *
     * @param appName
     * @param url
     * @param params
     * @param userid
     * @return
     */
    public static Object requestPostForm(String appName, String url, String userid, Map<String, Object> params) {
        RestTemplate restTemplate = ApplicationContextUtils.getRestTemplate();

        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.add("Authorization", userid);

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                postParameters.add(key, params.get(key));
            }
        }

        // HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);
        Object body = restTemplate.postForEntity("http://" + appName + url, requestEntity, Object.class).getBody();

        return body;
    }

    public static Object restGetInvoke(String url) {
        RestTemplate restTemplate = ApplicationContextUtils.getRestTemplate();
        ResponseResult responseResult;
        try {
            responseResult = restTemplate.getForEntity("http://" + url, ResponseResult.class).getBody();
            if (responseResult == null) {
                logger.error("远程调用接口{}返回为null", url);
                throw new WorkflowException("服务器异常");
            }
            if (ResponseResult.SUCCESS.equals(responseResult.getStatus())) {
                return responseResult.getData();
            } else {
                throw new WorkflowException(responseResult.getMessage());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new WorkflowException("服务器异常:" + e.getMessage());
        }
    }
}
