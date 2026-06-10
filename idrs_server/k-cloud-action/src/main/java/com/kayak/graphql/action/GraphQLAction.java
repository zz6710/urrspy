package com.kayak.graphql.action;

import com.alibaba.fastjson.JSON;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.sql.SqlConfig;
import com.kayak.base.dao.sql.SqlXmlServer;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.dao.DaoService;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.graphql.autoconfigure.GraphQLAnnotationImpl;
import com.kayak.graphql.autoconfigure.GraphQLAuthImpl;
import com.kayak.graphql.dao.GridFieldDao;
import com.kayak.graphql.dao.SearchFieldDao;
import com.kayak.graphql.dao.XmlSqlDao;
import com.kayak.graphql.model.FetcherData;
import com.kayak.graphql.service.SearchFieldService;
import com.kayak.index.service.IndexService;
import com.kayak.log.service.LogService;
import com.kayak.workflow.WorkFlowableService;
import com.kayak.workflow.constants.WfFieldConstants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GraphQLAction extends BaseController {

	@Autowired
	private GraphQLAnnotationImpl graphQLAnnotationImpl;
	@Autowired
	private LogService logService;
	@Autowired
	private SearchFieldDao searchFieldDao;
	@Autowired
	private GridFieldDao gridFieldDao;
	@Autowired
	private IndexService indexService;
	@Autowired
	private SearchFieldService searchFieldService;
	@Autowired
	private XmlSqlDao xmlSqlDao;

	@Autowired
	private WorkFlowableService workFlowableService;


	@PostMapping(value = "/graphql/commQuery.json",produces = { "application/json;charset=UTF-8"})
	public Object query() {
		try {
			Map<String, Object> params = RequestSupport.getParameters();

			String modelClassName = Tools.obj2Str(params.get("modelClassName"));

			Class<?> modelClass = Class.forName(modelClassName);

			// 获取操作对象实例
			GraphQLModel graphQLModel = modelClass.getAnnotation(GraphQLModel.class);

			String fetcher = graphQLModel.fetcher();
			String action = Tools.obj2Str(params.get("action"));

			RequestSupport.setModelAction("类名：" + modelClassName + "，action：" + action + "，");

			Object data = null;
			if (!fetcher.equals("xml")) {

				Object fetcherBean = SysBeans.getBean(fetcher);

				if (fetcherBean == null) {
					return updateFailure("获取操作对象失败，无fetcher配置对应实例，fetcher值：" + fetcher);
				}

				Class<?> serviceClass = fetcherBean.getClass().getSuperclass();
				Method method = fetcherBean.getClass().getMethod(action, SqlParam.class);
				String server = serviceClass.getSimpleName() + "-" + action;

				data = method.invoke(fetcherBean, new FetcherData(params, modelClass));
			} else {
				data = xmlSqlDao.query(new FetcherData(params, modelClass), action);
			}

			// 脱敏操作
			if (data instanceof SqlResult) {
				SqlResult<?> sqlResult = (SqlResult<?>) data;
				if (sqlResult.isDesensitized()) {
					sqlResult.doDesensitized();
				}
			}

			return data;
		} catch (PromptException e) {
			return updateFailure(e.getMessage());
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof PromptException) {
				return updateFailure(e.getCause().getMessage());
			}
			log.error(e.getMessage(), e);
			return updateFailure("系统异常:"+e.getTargetException().getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("系统异常:"+e.getMessage());
		} finally {
			RequestSupport.removeModelAction();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value = "/graphql/commUpdate.json",produces = { "application/json;charset=UTF-8"})
	public Object update(@RequestBody String body) {
		try {
			Map<String, Object> params = RequestSupport.getParameters();

			String modelClassName = Tools.obj2Str(params.get("modelClassName"));

			Class<?> modelClass = Class.forName(modelClassName);

			// 获取操作对象实例
			GraphQLModel graphQLModel = modelClass.getAnnotation(GraphQLModel.class);

			String fetcher = graphQLModel.fetcher();
			String action = Tools.obj2Str(params.get("action"));

			RequestSupport.setModelAction("类名：" + modelClassName + "，action：" + action + "，");

			String server = null;

			Object[] res = new Object[1];

			FetcherData fetcherData = new FetcherData(params, modelClass);

			if (!fetcher.equals("xml")) {
				Object fetcherBean = SysBeans.getBean(fetcher);

				if (fetcherBean == null) {
					return updateFailure("获取操作对象失败，无fetcher配置对应实例，fetcher值：" + fetcher);
				}

				Class<?> serviceClass = fetcherBean.getClass();

				server = ClassUtils.getShortName(serviceClass) + "-" + action;
			} else {
				server = ClassUtils.getShortName(modelClass) + "-" + action;
			}

//			// 判断是否走工作流，从缓存中
			SqlRow wfBusiConfig = (SqlRow) CacheUtil.getFlowConfig(server);
			if (wfBusiConfig != null
					&& params.get(WfFieldConstants.BUSI_CALL_BACK) == null
					&& 1==wfBusiConfig.getInteger("status")
			) {
				params.put(WfFieldConstants.LABEL_INFO,
						JSON.toJSONString(GraphQLAuthImpl.modelMetaDataMap.get(modelClassName)));
				params.put("url", "/graphql/commUpdate.json");		// 工作流回调用
				params.put(WfFieldConstants.CONTENT_TYPE,RequestSupport.getLocalRequest().getContentType());
				params.put("oldData", fetcherData.getOldData());
				com.alibaba.fastjson.JSONObject start = workFlowableService.start(wfBusiConfig, params);
				return updateSuccess((String)start.get("returnmsg"));
			}

			if (!fetcher.equals("xml")) {
				Object fetcherBean = SysBeans.getBean(fetcher);
				// 记录操作日志
				APIDefine apiDefine = fetcherBean.getClass().getAnnotation(APIDefine.class);
				if (apiDefine == null) {
					apiDefine = fetcherBean.getClass().getSuperclass().getAnnotation(APIDefine.class);
				}
				Method method = fetcherBean.getClass().getMethod(action, SqlParam.class);
				try {
					res[0] = method.invoke(fetcherBean, fetcherData);

					if (apiDefine.log()) {
						// 记录日志
						logService.addLog(server, fetcherData, true, "");
					}
				} catch (Exception e) {
					if (apiDefine.log()) {
						// 记录日志
						logService.addLog(server, fetcherData, false, e.getMessage());
					}

					throw e;
				}
			} else {
				String modelName = modelClass.getSimpleName();
				if (!SqlXmlServer.sqlCache.containsKey(modelName)) {
					throw new PromptException("不存在实体XML配置");
				}

				if (!SqlXmlServer.sqlCache.get(modelName).containsKey(action)) {
					throw new PromptException("实体XML不存在action配置");
				}

				SqlConfig sqlConfig = SqlXmlServer.sqlCache.get(modelName).get(action);

				try {
					DaoUtil.doTrans(()->{
						res[0] = xmlSqlDao.update(fetcherData, action);
					});

					if (sqlConfig.isLog()) {
						// 记录日志
						logService.addLog(server, fetcherData, true, "");
					}
				} catch (Exception e) {
					log.error(e.getMessage(),e);
					if (sqlConfig.isLog()) {
						// 记录日志
						logService.addLog(server, fetcherData, false, e.getMessage());
					}
					if (e instanceof PromptException) {
						throw e;
					}
				}
			}

			if (res[0] instanceof Integer) {
				JSONObject data = new JSONObject();
				data.put("update", res[0]);
				data.put("success", true);
				return data.toString();
			}

			return res[0];
		} catch (PromptException e) {
			return updateFailure(e.getMessage());
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof PromptException) {
				return updateFailure(e.getCause().getMessage());
			}
			log.error(e.getMessage(), e);
			return updateFailure("服务繁忙，请稍后尝试");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure("系统异常:"+e.getMessage());
		} finally {
			RequestSupport.removeModelAction();
		}

	}


	@PostMapping(value = "/graphql/search.json",produces = { "application/json;charset=UTF-8"})
	public String search(@RequestBody String body) {
		try {
			Map<String, Object> params = Tools.json2map(new JSONObject(body));
			String modelName = Tools.obj2Str(params.get("modelName"));
			// 判断是否有自定义配置
			List<Map<String, Object>> list = searchFieldService.getCustomSearchField(modelName);

			if (list.size() == 0) {
				list = graphQLAnnotationImpl.getSearchMap().get(modelName);
			}

			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("rows", list);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/graphql/searchDefault.json",produces = { "application/json;charset=UTF-8"})
	public String searchDefault(@RequestBody String body) {
		try {
			Map<String, Object> params = Tools.json2map(new JSONObject(body));
			String modelName = Tools.obj2Str(params.get("modelName"));
			if (graphQLAnnotationImpl.getSearchMap().containsKey(modelName)) {

				String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));

				// 获取个人保存的查询框
				SqlRow searchField = searchFieldDao.findSearchField(userid, modelName);
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				if (searchField == null || Tools.strIsEmpty(searchField.getString("search_fields"))) {
					List<Map<String, Object>> list2 = searchFieldService.getDefaultCustomSearchField(modelName);
					if (list2 != null && list2.size() > 0) {
						list = searchFieldService.getDefaultCustomSearchField(modelName);
					} else {
						list = graphQLAnnotationImpl.getDefaultSearchMap().get(modelName);
					}
				} else {
					String search_fields = searchField.getString("search_fields");
					String[] searchFields = search_fields.split(",");
					list = new ArrayList<Map<String, Object>>();

					List<Map<String, Object>> searchFieldMaps = null;
					if (searchFieldService.getCustomSearchField(modelName).size() > 0) {
						searchFieldMaps = searchFieldService.getCustomSearchField(modelName);
					} else {
						searchFieldMaps = graphQLAnnotationImpl.getSearchMap().get(modelName);
					}

					for (String _searchField : searchFields) {
						for (Map<String, Object> searchFieldMap : searchFieldMaps) {
							if (_searchField.equals(searchFieldMap.get("label"))) {
								list.add(searchFieldMap);
							}
						}
					}
				}

				JSONObject json = new JSONObject();
				json.put("success", true);
				json.put("rows", list);
				return json.toString();
			}
			return updateFailure("无此对象配置");
		} catch (Exception e) {
			log.error("查询对象默认搜索框配置失败");
			return updateFailure(e.getMessage());
		}

	}

	@PostMapping(value = "/graphql/updateSearchDefault.json",produces = { "application/json;charset=UTF-8"})
	public String updateSearchDefault(@RequestBody String body) {
		try {
			String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));

			Map<String, Object> params = Tools.json2map(new JSONObject(body));
			String modelName = Tools.obj2Str(params.get("modelName"));
			String searchFields = Tools.obj2Str(params.get("searchFields"));
			searchFieldDao.updateSearchField(userid, modelName, searchFields);

			return updateSuccess("保存成功");
		} catch (Exception e) {
			log.error("更新默认搜索框失败");
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/graphql/getAllSearchField.json")
	public String getAllSearchField(@RequestBody String body) {
		try {
			Map<String, Object> params = Tools.json2map(new JSONObject(body));
			String modelName = Tools.obj2Str(params.get("modelName"));

			Map<String, String> indexMap = indexService
					.getTableIndex(graphQLAnnotationImpl.getModelTableMap().get(modelName));

			List<Map<String, Object>> list = graphQLAnnotationImpl.getAllSearchFieldMap().get(modelName);
			for (Map<String, Object> map : list) {
				String dbfield = Tools.obj2Str(map.get("dbfield"));
				String isDefault = Tools.obj2Str(map.get("isDefault"));
				map.put("isDefault", isDefault);
				if (indexMap.containsKey(dbfield)) {
					map.put("isIndex", "1");
					map.put("indexName", indexMap.get(dbfield));
				} else {
					map.put("isIndex", "0");
				}
				if ("true".equals(isDefault)) {
					map.put("isDefault", "1");
				} else {
					map.put("isDefault", "0");
				}
			}

			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("rows", list);
			return json.toString();
		} catch (Exception e) {
			log.error(e.getMessage());
			return updateFailure(e.getMessage());
		}
	}

	// 临时测试使用，后期必须删除
	@RequestMapping(value = "/db/connectionDetail.json",produces = { "application/json;charset=UTF-8"})
	public String connectionDetail() {
		try {
			DaoService daoService = SysBeans.getBean("daoService");
			return daoService.getConnectionKeepDetail();
		} catch (Exception e) {
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/graphql/grid.json")
	public String grid(@RequestBody String body) {
		try {
			Map<String, Object> params = Tools.json2map(new JSONObject(body));
			String modelName = Tools.obj2Str(params.get("modelName"));
			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("rows", graphQLAnnotationImpl.getGridMap().get(modelName));
			return json.toString();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/graphql/gridDefault.json")
	public String gridDefault(@RequestBody String body) {
		try {
			Map<String, Object> params = Tools.json2map(new JSONObject(body));
			String modelName = Tools.obj2Str(params.get("modelName"));
			if (graphQLAnnotationImpl.getGridMap().containsKey(modelName)) {
				String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
				// 获取个人保存的表格列
				SqlRow gridField = gridFieldDao.get(userid, modelName);
				List<Map<String, Object>> list;

				if (gridField == null || Tools.strIsEmpty(gridField.getString("grid_fields"))) {
					list = graphQLAnnotationImpl.getDefaultGridMap().get(modelName);
				} else {
					String[] gridFields = gridField.getString("grid_fields").split(",");
					list = new ArrayList<>();

					List<Map<String, Object>> gridMap = graphQLAnnotationImpl.getGridMap().get(modelName);

					for (String field : gridFields) {
						for (Map<String, Object> map : gridMap) {
							if (field.equals(map.get("field"))) {
								list.add(map);
							}
						}
					}
				}

				JSONObject json = new JSONObject();
				json.put("success", true);
				json.put("rows", list);
				return json.toString();
			}
			return updateFailure("无此对象配置");
		} catch (Exception e) {
			log.error("查询对象显示表格列失败");
			return updateFailure(e.getMessage());
		}
	}

	@PostMapping(value = "/graphql/updateGridDefault.json")
	public String updateGridDefault(@RequestBody String body) {
		try {
			String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));

			Map<String, Object> params = Tools.json2map(new JSONObject(body));
			String modelName = Tools.obj2Str(params.get("modelName"));
			String gridFields = Tools.obj2Str(params.get("gridFields"));
			gridFieldDao.update(userid, modelName, gridFields);

			return updateSuccess("保存成功");
		} catch (Exception e) {
			log.error("更新动态表格列失败");
			return updateFailure(e.getMessage());
		}
	}
}
