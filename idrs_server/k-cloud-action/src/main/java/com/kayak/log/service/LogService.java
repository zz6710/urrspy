package com.kayak.log.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kayak.cache.util.CacheUtil;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.autoconfigure.GraphQLAuthImpl;
import com.kayak.graphql.model.ApiMetaData;
import com.kayak.graphql.model.FetcherData;
import com.kayak.graphql.model.FieldMetaData;
import com.kayak.log.dao.LogDao;
import com.kayak.server.ServerUtil;

@Service
public class LogService {

	private static final Logger log = LoggerFactory.getLogger(LogService.class);

	@Autowired
	private LogDao logDao;

	@Value("${spring.application.name}")
	private String serviceName;

	public void addLog(String server, FetcherData<?> fetcherData, boolean isSuccess, String errorMsg) {

		try {
			ApiMetaData apiMetaData = GraphQLAuthImpl.apiMetaDataMap.get(server);

			Map<Object, Object> addParams = new HashMap<>();

			addParams.put("userid", SysUtil.getLoginUserid());
			addParams.put("server", apiMetaData.getServer());
			addParams.put("server_desc", apiMetaData.getServerName());
			addParams.put("method", server);
			addParams.put("method_desc", apiMetaData.getMethodName());


			addParams.put("operation_date", DateUtil.getNowDate());
			addParams.put("operation_time", DateUtil.getNowTime());
			addParams.put("result", isSuccess ? "成功" : "失败");
			addParams.put("error_msg", errorMsg);

			// 获取模型对象
			Map<String, FieldMetaData> metaDataMap = GraphQLAuthImpl.modelMetaDataMap
					.get(fetcherData.getModelClass().getName());

			if (metaDataMap == null) {
				log.warn("模型[" + fetcherData.getModelClass().getName() + "]，没有对应的FieldMetaData，日志记录失败");
				return;
			}

			// 操作后数据
			JSONArray nowDatas = makeFormData(server, fetcherData.getParams(), metaDataMap, fetcherData);
			addParams.put("submit_data", nowDatas.toString());
			// 操作前数据
			String oldData = fetcherData.getOldData();

			if (!Tools.strIsEmpty(oldData) && Tools.isJson(oldData)) {
				String replace = oldData.replace("\\", "\\\\");
				Map<String, Object> oldParams = Tools.json2map(new JSONObject(replace));

				// 判断是否配置获取原数据SQL
				String oldDataSql = GraphQLAuthImpl.oldDataSqlMap.get(server);
				if (!Tools.strIsEmpty(oldDataSql)) {
					oldParams = logDao.findRow(oldDataSql, oldParams);
				}

				JSONArray oldDatas = makeFormData(server, oldParams, metaDataMap, fetcherData);
				addParams.put("submit_old_data", oldDatas.toString());
			} else {
				addParams.put("submit_old_data", "");
			}

			logDao.addLog(addParams);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}
	
	public void addLog(Map param) throws Exception {
		logDao.addLog(param);
		
	}

	private JSONArray makeFormData(String server, Map<String, Object> params, Map<String, FieldMetaData> metaDataMap,
			FetcherData<?> fetcherData) {
		JSONArray nowDatas = new JSONArray();

		if (params != null && !params.isEmpty()) {
			Set<String> keys = params.keySet();
			for (String key : keys) {
				FieldMetaData fieldMetaData = metaDataMap.get(key);
				JSONObject feildJson = new JSONObject();
				String label = null;
				String value = Tools.obj2Str(params.get(key));
				if (fieldMetaData != null) {
					label = fieldMetaData.getLabel();
					value = convertDict(value, fieldMetaData);
					value = convertAction(server, value, fieldMetaData, fetcherData);
				}

				if (Tools.strIsEmpty(label)) {
					label = key;
				}

				feildJson.put("label", label);
				feildJson.put("value", value);

				nowDatas.put(feildJson);
			}
		}

		return nowDatas;
	}

	private String convertDict(String value, FieldMetaData metaData) {
		String dict = metaData.getDict();
		if (!Tools.strIsEmpty(dict)) {
			value = CacheUtil.getDictItem(dict, value);
		}

		return value;
	}

	private String convertAction(String server, String value, FieldMetaData metaData, FetcherData<?> fetcherData) {
		String action = metaData.getAction();

		String dataDisplayField = metaData.getDataDisplayField();
		String dataValueField = metaData.getDataValueField();

		if (Tools.strIsEmpty(dataDisplayField) || Tools.strIsEmpty(dataValueField)) {
			/*log.error("model[" + fetcherData.getModelClass().getName() + "]，属性[" + metaData.getFeild()
					+ "]未配置dataDisplayField或dataValueField，无法转换action数据源下拉框");*/

			return value;
		}
		if (!Tools.strIsEmpty(action)) {
			// 查询APP服务名
			try {
				String app_name = logDao
						.findRow("SELECT app_name FROM sys_server_method = WHERE server = $S{server}", server)
						.getString("app_name");

				JSONObject json = ServerUtil.commQuery(app_name, fetcherData.getModelClass().getName(),
						server.split("-")[0], null);

				JSONArray rows = json.getJSONArray("rows");

				if (rows.length() == 0) {
					log.warn("model[" + fetcherData.getModelClass().getName() + "]，属性[" + metaData.getFeild()
							+ "]获取下来数据为空，无法转换action数据源下拉框");
				}

				String[] values = value.split(",");

				StringBuilder valueBuilder = new StringBuilder();
				String[] dataDisplayFields = dataDisplayField.split(",");

				for (String _value : values) {
					for (int i = 0; i < rows.length(); i++) {
						JSONObject row = rows.getJSONObject(i);
						if (row.getString(dataValueField).equals(_value)) {

							String displayValue = null;
							for (String _dataDisplayField : dataDisplayFields) {
								if (displayValue == null) {
									displayValue = row.getString(_dataDisplayField);
								} else {
									displayValue += "," + row.getString(_dataDisplayField);
								}
							}

							valueBuilder.append(displayValue);
							valueBuilder.append(",");
						}
					}
				}

				value = valueBuilder.toString();

				if (value.length() > 0) {
					value = value.substring(0, value.length() - 1);
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		return value;
	}

}
