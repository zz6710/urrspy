package com.kayak.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;

public class ServerUtil {

	static Logger logger = LoggerFactory.getLogger(ServerUtil.class);

	private static RestTemplate restTemplate;

	public static Object requestPostJson(String appName, String url, Object params) {
		if (restTemplate == null) {
			synchronized (ServerUtil.class) {
				if (restTemplate == null) {
					restTemplate = SysBeans.getBean("restTemplate");
				}
			}
		}

		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
		requestHeaders.add("Authorization", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
		// HttpEntity
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, requestHeaders);

		String body = restTemplate.postForEntity("http://" + appName + url, requestEntity, String.class).getBody();

		return body;
	}

	public static Object requestPostForm(String appName, String url, Map<String, Object> params) {
		return requestPostForm(appName, url, params, Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
	}

	public static String requestPostForm(String appName, String url, Map<String, Object> params, String loginUserid) {
		if (restTemplate == null) {
			synchronized (ServerUtil.class) {
				if (restTemplate == null) {
					restTemplate = SysBeans.getBean("restTemplate");
				}
			}
		}

		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.add("Authorization", loginUserid);

		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

		//设置接收返回值的格式为json
		List<MediaType> mediaTypeList = new ArrayList<>();
		mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
		requestHeaders.setAccept(mediaTypeList);

		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				postParameters.add(key, params.get(key));
			}
		}

		// HttpEntity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);

		String body = restTemplate.postForEntity("http://" + appName + url, requestEntity, String.class).getBody();
		logger.info(" 发起工作流返回信息: {} ", body);
		return body;
	}

	public static JSONObject commQuery(String appName, String modelFullName, String action, Map<String, Object> params)
			throws Exception {
		// 获取操作对象实例
		params.put("modelClassName", modelFullName);
		params.put("action", action);

		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.add("Authorization", SysUtil.getLoginUserid());
		// HttpEntity
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				postParameters.add(key, params.get(key));
			}
		}

		// HttpEntity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);

		String body = restTemplate
				.postForEntity("http://" + appName + "/graphql/commQuery.json", requestEntity, String.class).getBody();

		return new JSONObject(body);
	}
	
	
	public static JSONObject query(String appName, String modelFullName, String action, Map<String, Object> params)
			throws Exception {
		if (restTemplate == null) {
			synchronized (ServerUtil.class) {
				if (restTemplate == null) {
					restTemplate = SysBeans.getBean("restTemplate");
				}
			}
		}
		// 获取操作对象实例
		params.put("modelClassName", modelFullName);
		params.put("action", action);

		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.add("Authorization", "admin");
		// HttpEntity
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				postParameters.add(key, params.get(key));
			}
		}

		// HttpEntity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);

		ResponseEntity<String> response = restTemplate
				.postForEntity("http://" + appName + "/graphql/commQuery.json", requestEntity, String.class);
		 String body = response.getBody();
		return new JSONObject(body);
	}
	
	public static Object requestPost(String appName,String modelFullName, String action, Map<String, Object> params) {
		if (restTemplate == null) {
			synchronized (ServerUtil.class) {
				if (restTemplate == null) {
					restTemplate = SysBeans.getBean("restTemplate");
				}
			}
		}

		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.add("Authorization", "admin");
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		// 获取操作对象实例
		params.put("modelClassName", modelFullName);
		params.put("action", action);
		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				postParameters.add(key, params.get(key));
			}
		}

		// HttpEntity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);
		Object body = restTemplate.postForEntity("http://" + appName + "/graphql/commUpdate.json", requestEntity, Object.class).getBody();

		return body;
	}
	

}
