package com.kayak.graphql.autoconfigure;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOldDataSql;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.auth.service.ServerService;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.system.constants.ExcelDownloadConstants;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.ApiMetaData;
import com.kayak.graphql.model.FieldMetaData;

@Service
@DependsOn("springContextHolder")
public class GraphQLAuthImpl implements ResourceLoaderAware {

	private static final Logger log = LoggerFactory.getLogger(GraphQLAuthImpl.class);

	public static final Map<String, String> oldDataSqlMap = new ConcurrentHashMap<>();

	public static final Map<String, ApiMetaData> apiMetaDataMap = new ConcurrentHashMap<>();

	public static final Map<String, Map<String, FieldMetaData>> modelMetaDataMap = new ConcurrentHashMap<>();

	private ResourceLoader resourceLoader;

	boolean isInit = false;

	@Autowired
	private ServerService serverService;

	@Value("${spring.application.name}")
	private String appName;

	@Value("${graphql.auth.scan:true}")
	private String scan;

	@Value("${graphql.package:com.kayak}")
	private String pack;

	@PostConstruct
	public void init() {
		synchronized (GraphQLAuthImpl.class) {
			if (!isInit) {
				try {
					loadAuth();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			isInit = true;
		}
	}

	private void loadAuth() throws Exception {

		List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();

		String[] packs = pack.split(",");

		for (String pack : packs) {
			String _pack = pack.replaceAll("[.]", "/");
			scan(params, _pack);
			log.info("扫描Serivce包完成：" + pack);
		}

		if ("true".equals(scan)) {
			serverService.addServer(params, appName);
			CacheUtil.freshenGateway();
		}

		log.warn("权限加载成功");
	}

	private void scan(List<Map<String, Object>> params, String pack) throws Exception {
		ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
		Resource[] resources = resolver.getResources("classpath*:" + pack + "/**/*.class");

		for (Resource r : resources) {
			MetadataReader reader = metaReader.getMetadataReader(r);
			Class<?> modelClass = Class.forName(reader.getClassMetadata().getClassName());

			if (modelClass.isAnnotationPresent(APIDefine.class)) {
				APIDefine apiDefine = modelClass.getAnnotation(APIDefine.class);
				String className = modelClass.getSimpleName();

				Method[] methods = ReflectionUtils.getAllDeclaredMethods(modelClass);
				if (methods != null) {

					String apiDesc = apiDefine.desc();

					String modelName = "";
					Class<?> _modelClass = apiDefine.model();

					if (_modelClass != null) {
						modelName = _modelClass.getSimpleName();
					}

					log.info("扫描Service类：" + modelClass.getName());

					boolean flag = false;

					for (Method method : methods) {
						API api = AnnotationUtils.findAnnotation(method, API.class);
						String server = className + "-" + method.getName();

						if (api != null) {
							if (api.auth() == APIAuth.YES) {
								flag = true;
							}

							String desc = api.desc();

							params.add(Tools.getParams().put("server", server).put("uper_server", className)
									.put("name", desc).put("model_name", modelName).put("app_name", appName)
									.put("type", "2").put("page", api.page())
									.put("auth", api.auth() == APIAuth.YES ? 1 : 0).put("desc", api.desc())
									.put("operation", api.operation().getOperation())
									.put("params", api.params() == null ? "" : api.params()).build());

							if (api.excel() == true) {
								// 导出excel接口,默认验证权限
								params.add(Tools.getParams().put("server", server + ExcelDownloadConstants.SUFFIX)
										.put("uper_server", className).put("name", api.excelServerName())
										.put("model_name", modelName).put("app_name", appName).put("type", "2")
										.put("page", api.page()).put("auth", 1).put("desc", api.excelServerName())
										.put("operation", APIOperation.SELECT.getOperation()) // 导出赋值SELECT，防止展示在工作流接口配置中
										.put("params", api.params() == null ? "" : api.params()).build());
							}

							ApiMetaData apiMetaData = new ApiMetaData();

							apiMetaData.setServer(className);
							apiMetaData.setServerName(apiDesc);
							apiMetaData.setMethodName(desc);

							apiMetaDataMap.put(server, apiMetaData);
						}

						// 判断是否定义ApiOldDataSql
						APIOldDataSql oldDataSql = AnnotationUtils.findAnnotation(method, APIOldDataSql.class);
						if (oldDataSql != null) {
							String oldSql = oldDataSql.value();
							if (!Tools.strIsEmpty(oldSql)) {
								oldDataSqlMap.put(server, oldSql);
							}
						}
					}

					params.add(Tools.getParams().put("server", className).put("uper_server", "").put("name", apiDesc)
							.put("model_name", modelName).put("app_name", appName).put("type", "1")
							.put("auth", flag ? 1 : 0).put("desc", apiDefine.desc()).put("params", "").build());

				}
			}
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
