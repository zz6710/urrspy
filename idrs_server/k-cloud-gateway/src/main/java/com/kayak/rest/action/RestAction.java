package com.kayak.rest.action;

import com.kayak.cache.util.RepeatCacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlQueryTree;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.MD5;
import com.kayak.core.util.NetworkUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.autoconfigure.GraphQLAnnotationImpl;
import com.kayak.graphql.service.GraphqlService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RestAction extends BaseController {

	@Autowired
	private GraphQLAnnotationImpl graphQLAnnotationImpl;
	@Autowired
	private GraphqlService graphqlService;

	@PostMapping(value = "/commQuery/{model}/{action}.json",produces = { "application/json;charset=UTF-8"})
	public Object commQuery(@PathVariable String model, @PathVariable String action) {
		try {

			log.info("查询接口：model：" + model + ", action：" + action);

			Map<String, Object> params = RequestSupport.getParameters();
			if (!RequestSupport.getCanCode(params)) {
				return updateSuccess();
			}

			if (!graphQLAnnotationImpl.modelMap.containsKey(model)) {
				return updateFailure("无该实体类配置"+model);
			}

			SqlRow serverModel = graphQLAnnotationImpl.modelMap.get(model);

			String appName = serverModel.getString("app_name");
			String modelFullName = serverModel.getString("model_full_name");
			String serverName = serverModel.getString("server_name");

			params.put("action", action);
            String ip= NetworkUtil.getIpAddress(RequestSupport.getLocalRequest());
			return graphqlService.commQuery(appName, serverName, modelFullName, params,ip);

		} catch (PromptException e1) {
			return updateFailure(e1.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("服务器异常，请稍后尝试");
		}
	}

	@SuppressWarnings("unchecked")
	@PostMapping(value = "/commTreeQuery/{model}/{action}.json",produces = { "application/json;charset=UTF-8"})
	public Object commTreeQuery(@PathVariable String model, @PathVariable String action) {
		try {
			Map<String, Object> params = RequestSupport.getParameters();

			// 是否懒加载
			boolean lazy = Boolean.parseBoolean(String.valueOf(params.getOrDefault("lazy", "false")));

			if (!graphQLAnnotationImpl.modelMap.containsKey(model)) {
				return updateFailure("无该实体类配置"+model);
			}

			String diffcondition = Tools.obj2Str(params.get("diffcondition"));

			if (Tools.strIsEmpty(diffcondition)) {
				return updateFailure("diffcondition参数不能为空");
			}

			SqlRow serverModel = graphQLAnnotationImpl.modelMap.get(model);

			String appName = serverModel.getString("app_name");
			String modelFullName = serverModel.getString("model_full_name");
			String serverName = serverModel.getString("server_name");

			params.put("action", action);
			String ip= NetworkUtil.getIpAddress(RequestSupport.getLocalRequest());
			Object body = graphqlService.commQuery(appName, serverName, modelFullName, params,ip);

			List<SqlRow> sqlRows;

			if (body instanceof Map) {

				Map<String, Object> bodyMap = (Map<String, Object>) body;
				List<Map<String, Object>> rows = (List<Map<String, Object>>) bodyMap.get("rows");
				if (rows==null){
					throw new PromptException((String)bodyMap.get("returnmsg"), (Map<String, Object>) bodyMap.get("returndata"));
				}

				sqlRows = new ArrayList<SqlRow>();

				for (Map<String, Object> row : rows) {
					SqlRow sqlRow = new SqlRow();
					sqlRow.putAll(row);
					sqlRows.add(sqlRow);
				}

			} else {
				JSONObject bodyJson = new JSONObject(body.toString());

				JSONArray rows = bodyJson.getJSONArray("rows");

				sqlRows = new ArrayList<SqlRow>();

				if (rows.length() > 0) {
					for (int i = 0; i < rows.length(); i++) {
						sqlRows.add(Tools.json2sqlrow(rows.getJSONObject(i)));
					}
				}

			}
			JSONObject json = new JSONObject();

			json.put("success", true);

			if (lazy && params.containsKey(diffcondition.split(",")[1])) {
				json.put("rows", sqlRows.stream().peek(sqlRow -> sqlRow.put("hasChildren", true)).collect(Collectors.toList()));
				return json.toString();
			}

			SqlQueryTree sqlQueryTree = new SqlQueryTree(diffcondition, sqlRows);
			JSONArray treeData = sqlQueryTree.getTreeJson();
			json.put("rows", treeData);

			if (lazy) {
				List<JSONObject> children = new ArrayList<>();
				for (Object item : treeData) {
					JSONObject row = new JSONObject(String.valueOf(item));
					row.remove("children");
					row.put("hasChildren", true);
					children.add(row);
				}
				json.put("rows", children);
			}

			return json.toString();

		} catch (PromptException e1) {
			return updateFailure(e1.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("服务器异常，请稍后尝试");
		}
	}

	@PostMapping(value = "/commUpdate/{model}/{action}.json",produces = { "application/json;charset=UTF-8"})
	public Object commUpdate(@PathVariable String model, @PathVariable String action) {
		try {
			Map<String, Object> params = RequestSupport.getParameters();

			if (!graphQLAnnotationImpl.modelMap.containsKey(model)) {
				return updateFailure("无该实体类配置"+model);
			}

			//校验重复
			if(checkC(params,model,action)){
				return updateFailure("禁止重复提交!");
			}

			SqlRow serverModel = graphQLAnnotationImpl.modelMap.get(model);

			String appName = serverModel.getString("app_name");
			String modelFullName = serverModel.getString("model_full_name");
			String serverName = serverModel.getString("server_name");

			params.put("action", action);
			String ip= NetworkUtil.getIpAddress(RequestSupport.getLocalRequest());
			return graphqlService.commUpdate(appName, serverName, modelFullName, params,ip);

		} catch (PromptException e1) {
			return updateFailure(e1.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("服务器异常，请稍后尝试");
		}
	}

	public boolean checkC(Map<String,Object> map,String model,String action){
		Map<String,Object> sortMap=sortByKey(map,true);
		String key="model="+model+"&"+"action="+action+"&";
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



}
