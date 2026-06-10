package com.kayak.server.action;

import javax.servlet.http.HttpServletRequest;


import com.kayak.cache.util.RepeatCacheUtil;
import com.kayak.core.util.MD5;
import com.kayak.core.util.Tools;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.graphql.service.GraphqlService;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@RestController
public class ServerAction extends BaseController {

    @Value("${basename:BaseServer}")
    private String basename;

    @Value("${workflow:true}")
    private Boolean workflow;

    @Value("${noWorkflowServer:DpsApp,RptApp}")
    private String noWorkflowServer;

    @Value("${skywalking.url:}")
    private String skyWalkingUrl;

    private static RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private GraphqlService graphqlService;

	@PostMapping(value = "/server/form/{serverName}/**")
	public Object postForm(HttpServletRequest httpRequest, @PathVariable String serverName) {
		try {
			String currentURL = httpRequest.getRequestURI();

			String prefix = "server/form/" + serverName;

			String url = currentURL.substring(currentURL.indexOf(prefix) + prefix.length());

			//校验重复
			if(null!=RequestSupport.getParameters().get("operate_type")&&checkC(RequestSupport.getParameters(),serverName,url)){
				return updateFailure("禁止重复提交!");
			}

			return graphqlService.requestPostForm(serverName, url, RequestSupport.getParameters());
		} catch (JSONException e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}

	}

	@PostMapping(value = "/server/json/{serverName}/**")
	public Object postJson(HttpServletRequest httpRequest, @PathVariable String serverName) {
		try {
			String currentURL = httpRequest.getRequestURI();

			String prefix = "server/json/" + serverName;

			String url = currentURL.substring(currentURL.indexOf(prefix) + prefix.length());

            // TODO 财富后台接口，目前均采用"/Txxx"模式，所以需要将".json"去掉，如果其他系统有用.json结尾，则去掉改行代码
            url = url.replace(".json", "");

            //校验重复
            if(checkC(RequestSupport.getParameters(),serverName,url)){
                return updateFailure("禁止重复提交!");
            }

            // 工作流开启校验
            Map<String, Object> params = RequestSupport.getParameters();
            params.put("serverName", serverName);
            params.put("url", url);
            params.put("transCode", Tools.isBlank(url) ? "" : url.substring(1));
            if (workflow && !noWorkflowServer.contains(serverName) ) {
                Map<String, Object> flowResult = (Map<String, Object>) graphqlService.requestPostJson(basename, "/flow/checkAndStart.json", params);
                if ((boolean) flowResult.get("success")) {             // 处理成功，判断是否走工作流
                    Map<String, Object> returndata = (Map<String, Object>) flowResult.get("returndata");
                    if ((boolean) returndata.get("isStartFlow")) {     // 流程开启成功，直接返回
                        return flowResult;
                    }
                } else {
                    return flowResult;                              // 处理失败直接返回
                }
            }
            return graphqlService.requestPostJson(serverName, url, RequestSupport.getParameters());
        } catch (Exception e) {
            log.warn("WarnMsg:[{}]", e.getMessage(), e);
            return updateFailure("系统异常:"+e.getMessage());
        }

    }

    @PostMapping(value = "/server/jsonbody/{serverName}/**")
    public Object postJsonBody(HttpServletRequest httpRequest, @PathVariable String serverName) {
        try {
            String currentURL = httpRequest.getRequestURI();

            String prefix = "server/jsonbody/" + serverName;
            String url    = currentURL.substring(currentURL.indexOf(prefix) + prefix.length());

            // TODO 财富后台接口，目前均采用"/Txxx"模式，所以需要将".json"去掉，如果其他系统有用.json结尾，则去掉改行代码
            url = url.replace(".json", "");

            // 工作流开启校验
            String bodyParams = RequestSupport.getBodyParameters(httpRequest);

            JSONObject bodyJson=new JSONObject(bodyParams);

            Map<String,Object> params = Tools.json2map(bodyJson);
            params.put("serverName", serverName);
            params.put("url", url);
            params.put("transCode", Tools.isBlank(url) ? "" : url.substring(1));
            if (workflow) {
                Map<String, Object> flowResult = (Map<String, Object>) graphqlService.requestPostJson(basename, "/flow/checkAndStart.json", params);
                if ((boolean) flowResult.get("success")) {             // 处理成功，判断是否走工作流
                    Map<String, Object> returndata = (Map<String, Object>) flowResult.get("returndata");
                    if ((boolean) returndata.get("isStartFlow")) {     // 流程开启成功，直接返回
                        return flowResult;
                    }
                } else {
                    return flowResult;                              // 处理失败直接返回
                }
            }
            return graphqlService.requestPostJson(serverName, url, bodyParams);
        } catch (JSONException e) {
            log.warn("WarnMsg:[{}]", e.getMessage(), e);
            return updateFailure(e.getMessage());
        }

    }

    /**
     * 转发到指定url
     * @param httpRequest
     * @param targetUrl 格式为_ip:端口
     * @return
     */
    @PostMapping(value = "/transmit/get/{targetUrl}/**")
    public Object getTransmit(HttpServletRequest httpRequest, @PathVariable String targetUrl) {
        try {
            String currentURL = httpRequest.getRequestURI();
            String prefix = "/transmit/get/" + targetUrl;
            String server=currentURL.substring(currentURL.indexOf(prefix)+prefix.length());
            String url=targetUrl.substring(1)+server;
            if(!url.startsWith("http")){
                url= "http://" + url;
            }
            return graphqlService.requestGetTransmit(url,server, RequestSupport.getParameters());
        } catch (JSONException e) {
            log.warn("WarnMsg:[{}]", e.getMessage(), e);
            return updateFailure(e.getMessage());
        }catch (Exception e){
            log.warn("WarnMsg:[{}]", e.getMessage(), e);
            return updateFailure(e.getMessage());
        }
    }

    /**
     * 转发到指定url
     * @param httpRequest
     * @param targetUrl 格式为_ip:端口
     * @return
     */
    @PostMapping(value = "/transmit/form/{targetUrl}/**")
    public Object postTransmitForm(HttpServletRequest httpRequest, @PathVariable String targetUrl) {
        try {
            String currentURL = httpRequest.getRequestURI();
            String prefix = "/transmit/form/" + targetUrl;
            String server=currentURL.substring(currentURL.indexOf(prefix)+prefix.length());
            String url=targetUrl.substring(1)+server;
            if(!url.startsWith("http")){
                url= "http://" + url;
            }
            return graphqlService.requestPostTransmitForm(url,server, RequestSupport.getParameters());
        } catch (JSONException e) {
            log.warn("WarnMsg:[{}]", e.getMessage(), e);
            return updateFailure(e.getMessage());
        }catch (Exception e){
            log.warn("WarnMsg:[{}]", e.getMessage(), e);
            return updateFailure(e.getMessage());
        }
    }

    /**
     * 转发到指定url
     * @param httpRequest
     * @param targetUrl 格式为_ip:端口
     * @return
     */
    @PostMapping(value = "/transmit/json/{targetUrl}/**")
    public Object postTransmitJson(HttpServletRequest httpRequest, @PathVariable String targetUrl) {
        try {
            String currentURL = httpRequest.getRequestURI();
            String prefix = "/transmit/json/" + targetUrl;
            String server=currentURL.substring(currentURL.indexOf(prefix)+prefix.length());
            String url=targetUrl.substring(1)+server;
            if(!url.startsWith("http")){
                url= "http://" + url;
            }
            return graphqlService.requestPostTransmitJson(url,server, RequestSupport.getBodyParameters(httpRequest));
        } catch (JSONException e) {
            log.warn("WarnMsg:[{}]", e.getMessage(), e);
            return updateFailure(e.getMessage());
        }catch (Exception e){
            log.warn("WarnMsg:[{}]", e.getMessage(), e);
            return updateFailure(e.getMessage());
        }
    }



    public boolean checkC(Map<String,Object> map,String serverName,String url){
        Map<String,Object> sortMap=sortByKey(map,true);
        String key="serverName="+serverName+"&"+"url="+url+"&";
        Set<String> keySet = sortMap.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key2 = iter.next();
            String value=null==sortMap.get(key2)?"":sortMap.get(key2).toString();
            key=key+key2+"="+value+"&";
        }
        key= MD5.MD5Encode(key);
        return 	RepeatCacheUtil.checkRequestRepeat(key);
    }

    public static <K extends Comparable<? super K>, V > Map<K, V> sortByKey(Map<K, V> map, boolean asc)
    {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> stream = map.entrySet().stream();
        if (asc)
        {
            stream.sorted(Map.Entry.<K, V>comparingByKey())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        else
        {
            stream.sorted(Map.Entry.<K, V>comparingByKey().reversed())
                    .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }

    @PostMapping(value = "/base/ServerModel/getAppNames.json")
    public Object getAppNames() {
        try {
            return graphqlService.requestPostForm(basename, "/base/ServerModel/getAppNames.json", null);
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            return updateFailure(e.getMessage());
        }
    }

    @PostMapping(value = "/base/ServerModel/getModelNames.json")
    public Object getModelNames() {
        try {
            return graphqlService.requestPostJson(basename, "/base/ServerModel/getModelNames.json", RequestSupport.getParameters());
        } catch (JSONException e) {
            log.error(e.getMessage(), e);
            return updateFailure(e.getMessage());
        }
    }

    /**
     * skywalking转发接口
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/skywalking/graphql.json")
    public Object postSkyWalking(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        try {
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, new HttpHeaders());
            return restTemplate.postForEntity(skyWalkingUrl, requestEntity, Object.class).getBody();
        } catch (Exception e) {
            log.error("skywalking转发接口 ErrorMsg:[{}]", e.getMessage(), e);
            return updateFailure(e.getMessage());
        }
    }

    public static void main(String[] args){
        String url = "/T104";
        System.out.println(url.substring(1));
    }

}
