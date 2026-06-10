package com.kayak.core.sql;

import com.kayak.core.desensitized.DefaultDesensitized;
import com.kayak.core.desensitized.Desensitized;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class SqlResult<T> {
	protected static final Logger log = LoggerFactory.getLogger(SqlResult.class);

	/**
	 * 执行查询后构造出来的结果集
	 */
	private List<T> rows;

	private List<T> rowsList1;

	public List<T> getRowsList1() {
		return rowsList1;
	}

	public void setRowsList1(List<T> rowsList1) {
		this.rowsList1 = rowsList1;
	}

	/**
	 * 总记录数
	 */
	private long results;

	private boolean desensitized;

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public long getResults() {
		return results;
	}

	public void setResults(long results) {
		this.results = results;
	}

	public boolean isDesensitized() {
		return desensitized;
	}

	public void setDesensitized(boolean desensitized) {
		this.desensitized = desensitized;
	}

	/**
	 * 脱敏处理
	 *
	 * @throws Exception
	 */
	public void doDesensitized() throws Exception {
		if (rows == null || rows.size() == 0) {
			return;
		}

		// 获取对象需要脱敏的字段
		Class<?> modelClass = rows.get(0).getClass();

		GraphQLModel graphQLModel = modelClass.getAnnotation(GraphQLModel.class);

		Field[] fields = modelClass.getDeclaredFields();

		List<DesensitizedFeild> desensitizedFeilds = new ArrayList<DesensitizedFeild>();

		for (Field field : fields) {
			GraphQLField graphQLField = field.getAnnotation(GraphQLField.class);

			if (graphQLField != null && graphQLField.desensitized() != DefaultDesensitized.class) {
				// 需要脱敏处理
				desensitizedFeilds.add(new DesensitizedFeild(modelClass, field,
						(Desensitized) graphQLField.desensitized().newInstance()));
			}
		}

		if (desensitizedFeilds.size() == 0) {
			return;
		}

		// 脱敏处理
		for (int i = 0; i < rows.size(); i++) {
			if (!graphQLModel.firstLineNotDesensitized()) {
				for (DesensitizedFeild desensitizedFeild : desensitizedFeilds) {
					desensitizedFeild.doDesensitized(rows.get(i));
				}
			} else {
				if (i > 0) {
					for (DesensitizedFeild desensitizedFeild : desensitizedFeilds) {
						desensitizedFeild.doDesensitized(rows.get(i));
					}
				}
			}
		}
	}

	public static <T> SqlResult<T> build(List<T> rows) {
		SqlResult<T> sqlResult = new SqlResult<T>();
		sqlResult.setRows(rows);
		return sqlResult;
	}

	public static <T> SqlResult<T> build(List<T> rows, long results) {
		SqlResult<T> sqlResult = new SqlResult<T>();
		sqlResult.setRows(rows);
		sqlResult.setResults(results);
		return sqlResult;
	}

	public static <T> SqlResult<T> build(List<T> rows, long results, boolean desensitized) {
		SqlResult<T> sqlResult = new SqlResult<T>();
		sqlResult.setRows(rows);
		sqlResult.setResults(results);
		sqlResult.setDesensitized(desensitized);
		return sqlResult;
	}

	//业务层进行分页返回分页后的list集合
	public static <T> LinkedList<T> page(SqlParam<T> params,List<T> rows) throws Exception {
		LinkedList<T> finalResult = new LinkedList<>();
		Map<String, Object> params1 = params.getParams();
		Integer start = params1.get("start")==null?0:(Integer) params1.get("start");
		Integer end = start + 10 ;
		for (int i = start;i<end;i++){
			if(i<rows.size()){
				finalResult.add(rows.get(i));
			}
		}
		return finalResult;
	}

	/**
	 * 内部类，处理脱敏操作
	 *
	 * @author liuyg
	 *
	 */
	static class DesensitizedFeild {

		private Method getMethod;
		private Method setMethod;
		Desensitized desensitized;

		public DesensitizedFeild(Class<?> modelClass, Field field, Desensitized desensitized) throws Exception {
			String fieldName = field.getName();
			// 字段校验
			String baseMethodStr = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

			this.getMethod = modelClass.getMethod("get" + baseMethodStr);
			this.setMethod = modelClass.getMethod("set" + baseMethodStr,field.getType());
			this.desensitized = desensitized;
		}

		public void doDesensitized(Object model) throws Exception {
			Object value = getMethod.invoke(model);

			if (value == null) {
				return;
			}

			setMethod.invoke(model, desensitized.desensitized(value));
		}
	}

}
