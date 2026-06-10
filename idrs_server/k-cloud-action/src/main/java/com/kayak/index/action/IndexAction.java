package com.kayak.index.action;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.core.action.BaseController;
import com.kayak.core.util.Tools;
import com.kayak.graphql.autoconfigure.GraphQLAnnotationImpl;
import com.kayak.index.service.IndexService;

@RestController
public class IndexAction extends BaseController {

	@Autowired
	private GraphQLAnnotationImpl graphQLAnnotationImpl;

	@Autowired
	private IndexService indexService;

	@PostMapping(value = "/index/addIndex.json")
	public String addIndex(@RequestBody String body) {
		try {
			Map<String, Object> params = Tools.json2map(new JSONObject(body));
			String modelName = Tools.obj2Str(params.get("modelName"));
			String dbField = Tools.obj2Str(params.get("dbfield"));
			String indexName = Tools.obj2Str(params.get("indexName"));
			indexService.addIndex(graphQLAnnotationImpl.getModelTableMap().get(modelName), dbField,indexName);

			JSONObject json = new JSONObject();
			json.put("success", true);
			return json.toString();
		} catch (Exception e) {
			log.error(e.getMessage());
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/index/deleteIndex.json")
	public String deleteIndex(@RequestBody String body) {
		try {
			Map<String, Object> params = Tools.json2map(new JSONObject(body));
			String modelName = Tools.obj2Str(params.get("modelName"));
			String indexName = Tools.obj2Str(params.get("indexName"));
			indexService.deleteIndex(graphQLAnnotationImpl.getModelTableMap().get(modelName), indexName);

			JSONObject json = new JSONObject();
			json.put("success", true);
			return json.toString();
		} catch (Exception e) {
			log.error(e.getMessage());
			return updateFailure(e.getMessage());
		}
	}

}
