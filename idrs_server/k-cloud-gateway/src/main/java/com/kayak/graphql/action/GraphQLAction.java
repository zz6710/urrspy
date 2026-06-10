package com.kayak.graphql.action;

import com.kayak.cache.util.RepeatCacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlQueryTree;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.MD5;
import com.kayak.core.util.Tools;
import com.kayak.graphql.autoconfigure.GraphQLAnnotationImpl;
import com.kayak.graphql.service.GraphqlService;
import graphql.ExceptionWhileDataFetching;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class GraphQLAction extends BaseController {

	@Autowired
	private GraphQLAnnotationImpl graphQLAnnotationImpl;

	@Autowired
	private GraphqlService graphqlService;

	@PostMapping(value = "/graphql.json")
	public String query(@RequestBody String query) {
		try {
			//add by zhangchangsi 20211023 移动审批传的参数不一样，在这里添加判断处理
			/*boolean b = checkJson(query);
			if (b) {
				JSONObject queryParams = new JSONObject(query);
				String bankAppParams = (String)queryParams.get("bankAppParams");
				if (StringUtils.isNotBlank(bankAppParams)) {
					query = bankAppParams;
				}
			}*/
			JSONObject json = new JSONObject();

			json.put("success", true);
			if(query.indexOf("mutation")>-1){
				if(checkC(query)){
					return updateFailure("禁止重复提交!");
				}
			}

			// 判断是否有树形配置
			JSONObject treeConfigs = null;
			if (query.contains("treeConfig")) {
				String treeConfigStr = query.substring(query.indexOf("treeConfig") + 10);
				treeConfigs = new JSONObject(treeConfigStr);
				query = query.substring(0, query.indexOf("treeConfig"));
			}

			ExecutionResult executionResult = graphQLAnnotationImpl.getGraphQL().execute(query);
			List<GraphQLError> graphQLErrors = executionResult.getErrors();
			if (graphQLErrors != null && graphQLErrors.size() > 0) {
				ExceptionWhileDataFetching exceptionWhileDataFetching = (graphql.ExceptionWhileDataFetching) graphQLErrors
						.get(0);
				return updateFailure(exceptionWhileDataFetching.getException().getMessage() == null
						? exceptionWhileDataFetching.getException().getCause().getMessage()
						: exceptionWhileDataFetching.getException().getMessage());

			}
			Map<String, Map<String, Object>> result = executionResult.getData();
			if (result != null && result.size() > 0) {
				Set<String> keys = result.keySet();

				for (String key : keys) {
					Object value = result.get(key);
					if (value == null) {
						continue;
					}

					Map<String, Object> map = (Map<String, Object>) value;
					if (key.startsWith("mutation")) {
						json.put(key, map);
					} else {
						JSONObject jsonMap = new JSONObject();
						if (map.containsKey("rows")) {// 树形控件
							List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rows");
							if (treeConfigs != null && treeConfigs.has(key)) {

								JSONObject treeConfig = treeConfigs.getJSONObject(key);

								String diffcondition = Tools.obj2Str(treeConfig.get("diffcondition"));
								SqlQueryTree sqlQueryTree = new SqlQueryTree(diffcondition, list);
								JSONArray treeJson = sqlQueryTree.getTreeJson();

								jsonMap.put("rows", treeJson);
							} else {

								JSONArray rows = new JSONArray();
								if (list != null) {
									list.forEach(item -> {
										rows.put(new JSONObject(item));
									});
								}
								jsonMap.put("rows", rows);
							}

						}

						if (map.containsKey("results")) {
							jsonMap.put("results", map.get("results"));
						}

						if (map.containsKey("treeData")) {
							jsonMap.put("treeData", map.get("treeData"));
						}

						json.put(key, jsonMap);
					}

				}
			}

			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}

	}

	@PostMapping(value = "/graphql/search.json")
	public Object search() {
		try {
			String modelName = Tools.obj2Str(RequestSupport.getParameters().get("modelName"));
			if (graphQLAnnotationImpl.getServerMap().containsKey(modelName)) {
				// npm
				String appName = graphQLAnnotationImpl.getServerMap().get(modelName);
				return graphqlService.requestPostJson(appName, "/graphql/search.json", RequestSupport.getParameters());
			}
			return updateFailure("无此对象配置");
		} catch (JSONException e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}

	}

	@PostMapping(value = "/graphql/searchDefault.json")
	public Object searchDefault() {
		try {
			String modelName = Tools.obj2Str(RequestSupport.getParameters().get("modelName"));
			if (graphQLAnnotationImpl.getServerMap().containsKey(modelName)) {
				// 获取对应的应用名
				String appName = graphQLAnnotationImpl.getServerMap().get(modelName);
				return graphqlService.requestPostJson(appName, "/graphql/searchDefault.json",
						RequestSupport.getParameters());
			}
			return updateFailure("无此对象配置");
		} catch (JSONException e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}

	}

	@PostMapping(value = "/graphql/updateSearchDefault.json")
	public Object updateSearchDefault() {
		try {
			String modelName = Tools.obj2Str(RequestSupport.getParameters().get("modelName"));
			if (graphQLAnnotationImpl.getServerMap().containsKey(modelName)) {
				// 获取对应的应用名
				String appName = graphQLAnnotationImpl.getServerMap().get(modelName);
				return graphqlService.requestPostJson(appName, "/graphql/updateSearchDefault.json",
						RequestSupport.getParameters());
			}
			return updateFailure("无此对象配置");
		} catch (JSONException e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}

	}

	@RequestMapping(value = "/graphql/reloadConfig.json")
	public Object reloadConfig() {
		try {
			graphQLAnnotationImpl.loadConfig();
			return updateSuccess("graphql配置即权限加载成功");
		} catch (Exception e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}

	}

	@PostMapping(value = "/graphql/grid.json")
	public Object grid() {
		try {
			String modelName = Tools.obj2Str(RequestSupport.getParameters().get("modelName"));
			if (graphQLAnnotationImpl.getServerMap().containsKey(modelName)) {
				// 获取对应的应用名
				String appName = graphQLAnnotationImpl.getServerMap().get(modelName);
				return graphqlService.requestPostJson(appName, "/graphql/grid.json", RequestSupport.getParameters());
			}
			return updateFailure("无此对象配置");
		} catch (JSONException e) {
			log.error(e.getMessage(), e);
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/graphql/gridDefault.json")
	public Object gridDefault() {
		try {
			String modelName = Tools.obj2Str(RequestSupport.getParameters().get("modelName"));
			if (graphQLAnnotationImpl.getServerMap().containsKey(modelName)) {
				// 获取对应的应用名
				String appName = graphQLAnnotationImpl.getServerMap().get(modelName);
				return graphqlService.requestPostJson(appName, "/graphql/gridDefault.json",
						RequestSupport.getParameters());
			}
			return updateFailure("无此对象配置");
		} catch (JSONException e) {
			log.error(e.getMessage(), e);
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/graphql/updateGridDefault.json")
	public Object updateGridDefault() {
		try {
			String modelName = Tools.obj2Str(RequestSupport.getParameters().get("modelName"));
			if (graphQLAnnotationImpl.getServerMap().containsKey(modelName)) {
				// 获取对应的应用名
				String appName = graphQLAnnotationImpl.getServerMap().get(modelName);
				return graphqlService.requestPostJson(appName, "/graphql/updateGridDefault.json",
						RequestSupport.getParameters());
			}
			return updateFailure("无此对象配置");
		} catch (JSONException e) {
			log.error(e.getMessage(), e);
			return updateFailure(e.getMessage());
		}
	}

	public boolean checkC(String query){
		String key=MD5.MD5Encode(query);
		return RepeatCacheUtil.checkRequestRepeat(key);
	}
}
