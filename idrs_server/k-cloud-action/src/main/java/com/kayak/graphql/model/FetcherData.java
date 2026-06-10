package com.kayak.graphql.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.util.Tools;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.convert.ConvertUtil;

public class FetcherData<T> implements SqlParam<T> {
	private static final Logger log = LoggerFactory.getLogger(FetcherData.class);

	private Map<String, Object> params;
	private Class<T> modelClass;
	private T model;
	private int start;
	private int limit;
	private boolean isPage;

	private String sort;
	private String dir;
	private boolean isOrder;

	private boolean makeSql;

	// 原表单数据
	private String nowData;
	private String oldData;
	private String oldSubmitData;

	private boolean sqlNoLog;

	public FetcherData(Map<String, Object> params) throws Exception {
		this.params = params;
		this.modelClass = (Class<T>) Map.class;

		if (this.params.containsKey("start") && this.params.containsKey("limit")) {
			this.start = Tools.str2Int(Tools.obj2Str(this.params.get("start")));
			this.limit = Tools.str2Int(Tools.obj2Str(this.params.get("limit")));
			this.isPage = true;
		}

        if(this.start==0&&this.limit==0){
             this.isPage=false;
		}

        if (this.params.containsKey("sort") && this.params.containsKey("dir")) {
            this.sort = Tools.obj2Str(this.params.get("sort"));
            this.dir = Tools.obj2Str(this.params.get("dir"));
            this.isOrder = true;
        }

		if (this.params.containsKey("oldData")) {
			oldData = Tools.obj2Str(this.params.get("oldData"));
			this.params.remove("oldData");
		}
		model = (T) params;
	}

	public FetcherData(Map<String, Object> params, Class<T> modelClass) throws Exception {
		this.params = params;
		this.modelClass = modelClass;

		if (this.params.containsKey("start") && this.params.containsKey("limit")) {
			this.start = Tools.str2Int(Tools.obj2Str(this.params.get("start")));
			this.limit = Tools.str2Int(Tools.obj2Str(this.params.get("limit")));
			this.isPage = true;
		}

		if(this.start==0&&this.limit==0){
			this.isPage=false;
		}

		if (this.params.containsKey("sort") && this.params.containsKey("dir")) {
			this.sort = Tools.obj2Str(this.params.get("sort"));
			this.dir = Tools.obj2Str(this.params.get("dir"));
			this.isOrder = true;
		}

		if (this.params.containsKey("oldData")) {
			oldData = Tools.obj2Str(this.params.get("oldData"));
			this.params.remove("oldData");
		}

		model = modelClass.newInstance();

		List<Field> fields = new ArrayList<>();
		Tools.getFields(fields, modelClass);
		for (Field field : fields) {
			if (field.isAnnotationPresent(GraphQLField.class)) {
				String fieldName = field.getName();

				if (!params.containsKey(fieldName) ||"".equals(params.get(fieldName))) {//当对象中不包含要素或者val为空字符串时不进行处理
					continue;
				}

				// 字段校验
				String checkMethodStr = "check" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

				Method checkMethod = null;
				try {
					checkMethod = modelClass.getMethod(checkMethodStr, field.getType());
				} catch (Exception e) {
//					log.error("获取字段校验方法异常",e);
				}
				Object value = params.get(fieldName);

				value = ConvertUtil.convert(field, Tools.obj2Str(value));

//				value = value.replaceAll("@#n#@", "\n");

				if (checkMethod != null) {
					String re = Tools.obj2Str(checkMethod.invoke(model, value));
					if (!Tools.strIsEmpty(re)) {
						throw new PromptException(re);
					}
				}

				String setMethod = "set" + getFieldName(fieldName);

				modelClass.getMethod(setMethod, field.getType()).invoke(model, value);
			}
		}

	}

	private String getFieldName(String fieldName) {
		if (fieldName.length() > 1 && Character.isUpperCase(fieldName.charAt(1))) {
			return fieldName;
		}

		return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}

	public Map<String, Object> getParams() throws Exception {

        if (model instanceof Map) {
            return (Map<String, Object>) model;
        }

		Map<String, Object> newParams = new HashMap<>();
		List<Field> fields = new ArrayList<>();
		Tools.getFields(fields, modelClass);
		for (Field field : fields) {
			if (field.isAnnotationPresent(GraphQLField.class)) {

				String fieldName = field.getName();
				// 字段校验
				String getMethodStr = "get" + getFieldName(fieldName);

				Method getMethod = null;
				try {
					getMethod = modelClass.getMethod(getMethodStr);
				} catch (Exception e) {
					throw new Exception(fieldName + "没有get方法");
				}

				if (getMethod != null) {
					Object re = getMethod.invoke(model);
					if (re != null) {
						newParams.put(fieldName, re);
					}
				}
			}
		}

		if (isPage) {
			newParams.put("start", this.start);
			newParams.put("limit", this.limit);
		}

		if (isOrder) {
			newParams.put("sort", this.sort);
			newParams.put("dir", this.dir);
		}

		return newParams;
	}

	public Map<String, Object> getParamsDirect(){
		return params;
	}

	@Override
	public Class<T> getModelClass() {
		return modelClass;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public int getLimit() {
		return limit;
	}

	@Override
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean isPage() {
		return isPage;
	}

	@Override
	public T getModel() {
		return model;
	}

	@Override
	public boolean isMakeSql() {
		return makeSql;
	}

	@Override
	public void setMakeSql(boolean makeSql) {
		this.makeSql = makeSql;
	}

	public String getOldData() {
		return oldData;
	}

	public void setOldData(String oldData) {
		this.oldData = oldData;
	}

	@Override
	public void setSqlNoLog(boolean sqlNoLog) {
		this.sqlNoLog = sqlNoLog;
	}

	@Override
	public boolean isSqlNoLog() {
		return this.sqlNoLog;
	}

}
