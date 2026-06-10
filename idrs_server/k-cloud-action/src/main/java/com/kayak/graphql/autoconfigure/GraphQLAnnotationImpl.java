package com.kayak.graphql.autoconfigure;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Service;

import com.kayak.auth.service.ServerService;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.graphql.model.FieldMetaData;

@Service
@DependsOn("springContextHolder")
public class GraphQLAnnotationImpl implements ResourceLoaderAware {

	private static Logger log = LoggerFactory.getLogger(GraphQLAnnotationImpl.class);

	private ResourceLoader resourceLoader;

	@Autowired
	private ServerService serverService;

	@Value("${spring.application.name}")
	private String appName;

	@Value("${graphql.model.scan:true}")
	private String scan;

	@Value("${graphql.package:com.kayak}")
	private String pack;

	boolean isInit = false;

	private Map<String, List<Map<String, Object>>> searchMap = new ConcurrentHashMap<>();
	private Map<String, List<Map<String, Object>>> gridMap = new ConcurrentHashMap<>();
	private Map<String, List<Map<String, Object>>> defaultSearchMap = new ConcurrentHashMap<>();
	private Map<String, List<Map<String, Object>>> defaultGridMap = new ConcurrentHashMap<>();
	private Map<String, List<Map<String, Object>>> allSearchFieldMap = new ConcurrentHashMap<>();

	private Map<String, String> modelTableMap = new ConcurrentHashMap<>();
	public static Map<String, Class> modelClassMap = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		synchronized (GraphQLAnnotationImpl.class) {
			if (!isInit) {
				try {
					loadAnnotation();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			isInit = true;
		}
	}

	private void loadAnnotation() throws Exception {

		List<Map<String, Object>> datas = new ArrayList<>();

		String[] packs = pack.split(",");

		for (String pack : packs) {
			String _pack = pack.replaceAll("[.]", "/");
			scan(datas, _pack);
			log.info("扫描model包完成：" + _pack);
		}

		if ("true".equals(scan)) {
			serverService.addGraphQLModel(appName, datas);
			CacheUtil.freshenGateway();
		}

	}

	private void scan(List<Map<String, Object>> datas, String pack) throws Exception {
		ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
		Resource[] resources = resolver.getResources("classpath*:" + pack + "/**/*.class");

		for (Resource r : resources) {
			MetadataReader reader = metaReader.getMetadataReader(r);

			Class<?> modelClass = Class.forName(reader.getClassMetadata().getClassName());

			if (modelClass.isAnnotationPresent(GraphQLModel.class)) {
				// 获取类名
				String modelName = modelClass.getName().replace(modelClass.getPackage().getName() + ".", "");

				log.info("扫描model类：" + modelName);

				//缓存model-class
				modelClassMap.put(modelName, modelClass);

				GraphQLModel graphQLModel = modelClass.getAnnotation(GraphQLModel.class);
				String fetcher = graphQLModel.fetcher();
				String serverName = fetcher.substring(0, 1).toUpperCase() + fetcher.substring(1);

				// 缓存model-table映射
				modelTableMap.put(modelName, graphQLModel.table());

				String modelFullName = modelClass.getName();

				Map<String, Object> data = new HashMap<>();

				data.put("model_name", modelName);
				data.put("model_label", graphQLModel.label());
				data.put("model_full_name", modelFullName);
				data.put("app_name", appName);
				data.put("server_name", serverName);
				data.put("model_table", graphQLModel.table());

				StringBuilder feildBuilder = new StringBuilder();
				StringBuilder feildEncryptBuilder = new StringBuilder();
				StringBuilder feildKeyBuilder = new StringBuilder();


				List<Field> fields = new ArrayList<>();
				Tools.getFields(fields, modelClass);

				String isEncrypt = "0";

				Map<String, FieldMetaData> metaDataMap = new HashMap<String, FieldMetaData>();

				for (Field field : fields) {
					if (field.isAnnotationPresent(GraphQLField.class)) {

						String fieldName = field.getName();
						String fieldtype = field.getType().getName();

						if (List.class.getName().equals(fieldtype) || Date.class.getName().equals(fieldtype) || Timestamp.class.getName().equals(fieldtype)) {
							fieldtype = String.class.getName();
						}

						GraphQLField graphQLField = field.getAnnotation(GraphQLField.class);

						int encrypt = graphQLField.encryptType();

						String label = graphQLField.label();

						if (!Tools.strIsEmpty(label) && label.contains(",")) {
							throw new Exception("扫描类失败，属性label值不能包含[,]，请检查model配置，model：" + modelClass.getName() + "，属性label：" + label);
						}

						if (encrypt != GraphQLField.ENCRYPT_NONE) {
							isEncrypt = "1";
							feildEncryptBuilder.append(fieldName);
							feildEncryptBuilder.append(":");
							feildEncryptBuilder.append(graphQLField.field());
							feildEncryptBuilder.append(":");
							feildEncryptBuilder.append(graphQLField.label());
							feildEncryptBuilder.append(",");
						}

						if (graphQLField.key()) {
							feildKeyBuilder.append(graphQLField.field());
							feildKeyBuilder.append(",");
						}

						boolean isArgument = graphQLField.argument();

						if (isArgument) {
							feildBuilder.append(fieldName);
							feildBuilder.append(":");
							feildBuilder.append(fieldtype);
							feildBuilder.append(":");
							feildBuilder.append(graphQLField.label());
							feildBuilder.append(",");
						}

						// 缓存所有数据库映射字段
						if (!allSearchFieldMap.containsKey(modelName)) {
							List<Map<String, Object>> list = new ArrayList<>();
							allSearchFieldMap.put(modelName, list);
						}
						if (!Tools.strIsEmpty(graphQLField.field())) {
							allSearchFieldMap.get(modelName).add(getModelFeildMap(graphQLField, fieldName));
						}

						String kkhtml = graphQLField.kkhtml();

						if (!Tools.strIsEmpty(kkhtml)) {
							if (!searchMap.containsKey(modelName)) {
								List<Map<String, Object>> list = new ArrayList<>();
								list.add(this.getParamsMap(graphQLField, fieldName));
								searchMap.put(modelName, list);
							} else {
								searchMap.get(modelName).add(this.getParamsMap(graphQLField, fieldName));
							}

							if (graphQLField.kkhtmlDefault()) {// 默认的查询框
								if (!defaultSearchMap.containsKey(modelName)) {
									List<Map<String, Object>> list = new ArrayList<>();
									list.add(this.getParamsMap(graphQLField, fieldName));
									defaultSearchMap.put(modelName, list);
								} else {
									defaultSearchMap.get(modelName).add(this.getParamsMap(graphQLField, fieldName));
								}
							}
						}
						// 表格动态列-可设置项
						if (graphQLField.gridShow()) {
							// 表格参数
							Map<String, Object> gridParamsMap = this.getGridParamsMap(graphQLField, fieldName);
							if (!gridMap.containsKey(modelName)) {
								List<Map<String, Object>> list = new ArrayList<>();
								list.add(gridParamsMap);
								gridMap.put(modelName, list);
							} else {
								gridMap.get(modelName).add(gridParamsMap);
							}
							// 默认显示列
							if (graphQLField.gridDefault()) {
								if (!defaultGridMap.containsKey(modelName)) {
									List<Map<String, Object>> list = new ArrayList<>();
									list.add(gridParamsMap);
									defaultGridMap.put(modelName, list);
								} else {
									defaultGridMap.get(modelName).add(gridParamsMap);
								}
							}
						}

						// 缓存字段信息
						FieldMetaData metaData = new FieldMetaData();

						metaData.setFeild(fieldName);
						metaData.setLabel(label);

						String kkhtmlExt = graphQLField.kkhtmlExt();
						if (!Tools.strIsEmpty(kkhtmlExt)) {
							try {
								JSONObject json = new JSONObject(kkhtmlExt);
								if (json.has("data-dict")) {
									metaData.setDict(json.getString("data-dict"));
								} else if (json.has("data-action")) {
									metaData.setAction(json.getString("data-action"));
								}
							} catch (Exception e) {
								log.error("model[" + modelFullName + "]-字段[" + fieldName + "]，kkhtmlExt配置错误，请配置json格式数据，kkhtmlExt内容：" + kkhtmlExt, e);
								throw e;
							}
						}

						metaDataMap.put(fieldName, metaData);
					}
				}

				GraphQLAuthImpl.modelMetaDataMap.put(modelFullName, metaDataMap);

				String model_field = feildBuilder.toString();
				if (!Tools.strIsEmpty(model_field)) {
					model_field = model_field.substring(0, model_field.length() - 1);
				}

				String encrypt_field = feildEncryptBuilder.toString();
				if (!Tools.strIsEmpty(encrypt_field)) {
					encrypt_field = encrypt_field.substring(0, encrypt_field.length() - 1);
				}

				String model_keys = feildKeyBuilder.toString();
				if (!Tools.strIsEmpty(model_keys)) {
					model_keys = model_keys.substring(0, model_keys.length() - 1);
				}

				data.put("model_field", model_field);
				data.put("is_encrypt", isEncrypt);
				data.put("encrypt_field", encrypt_field);
				data.put("model_keys", model_keys);
				datas.add(data);

			}
		}
	}

	private Map<String, Object> getParamsMap(GraphQLField graphQLField, String fieldName) {
		Map<String, Object> map = new HashMap<>();
		map.put("label", graphQLField.label());

		String kkhtml = graphQLField.kkhtml();
		String inputConfig = graphQLField.kkhtmlExt();
		boolean kkhtmlDefault = graphQLField.kkhtmlDefault();
		map.put("isDefault", kkhtmlDefault);
		map.put("inputHtml", kkhtml);
		map.put("inputConfig", inputConfig);
		map.put("field", fieldName);

		return map;
	}

	private Map<String, Object> getModelFeildMap(GraphQLField graphQLField, String fieldName) {
		String label = graphQLField.label();
		Map<String, Object> map = new HashMap<>();
		map.put("label", label);
		map.put("dbfield", graphQLField.field());
		map.put("isDefault", graphQLField.kkhtmlDefault());
		map.put("inputHtml", graphQLField.kkhtml());
		map.put("inputConfig", graphQLField.kkhtmlExt());
		map.put("field", fieldName);
		return map;
	}

	private Map<String, Object> getGridParamsMap(GraphQLField graphQLField, String fieldName) {
		Map<String, Object> map = new HashMap<>(7);
		map.put("label", graphQLField.label());
		map.put("isDefault", graphQLField.gridDefault());
		map.put("dataShow", Boolean.TRUE);
		map.put("dataExt", graphQLField.gridExt());
		map.put("field", fieldName);
		return map;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public Map<String, List<Map<String, Object>>> getSearchMap() {
		return searchMap;
	}

	public Map<String, List<Map<String, Object>>> getDefaultSearchMap() {
		return defaultSearchMap;
	}

	public Map<String, List<Map<String, Object>>> getAllSearchFieldMap() {
		return allSearchFieldMap;
	}

	public Map<String, String> getModelTableMap() {
		return modelTableMap;
	}

	public Map<String, List<Map<String, Object>>> getGridMap() {
		return gridMap;
	}

	public Map<String, List<Map<String, Object>>> getDefaultGridMap() {
		return defaultGridMap;
	}

}
