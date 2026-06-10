package com.kayak.xsql.parameter;

import java.util.Map;

import com.kayak.core.exception.SqlException;
import com.kayak.core.sql.UpdateResult;
import com.kayak.xsql.autoid.AutoId;

public class FieldGetterAutoId extends FieldGetter {

	private String pname;
	private String table;

	private UpdateResult updateResult;

	private Class<?> clazz;

	private AutoId autoId;

	public FieldGetterAutoId(String sql, String pname, Class<?> clazz, AutoId autoId, UpdateResult updateResult)
			throws SqlException {
		this.pname = pname;
		this.clazz = clazz;
		this.autoId = autoId;
		this.updateResult = updateResult;

		sql = sql.replaceAll("\r", " ").replaceAll("\n", " ").replaceAll("\t", " ").replaceAll("=", " = ")
				.replaceAll("[(]", " (").replaceAll("[)]", ") ").trim();
		String[] sqllower = sql.toLowerCase().split("[ ]");
		if (!("insert".equals(sqllower[0]) && "into".equals(sqllower[1]))) {
			throw new SqlException("参数类型 $AUTOID 只能使用在INSERT语句中");
		}

		this.table = sqllower[2];
	}

	@Override
	public Object get(Object params) {
		try {
			String newid = autoId.getAutoId(table, pname);

			if (Map.class.isAssignableFrom(clazz)) { // 通过Map给出参数
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) params;
				map.put(pname, newid);
			}

			updateResult.setAutoId(newid);

			return newid;

		} catch (Exception e) {
			return null;
		}
	}

	public void prepare() throws Exception {
		autoId.prepare(table, pname);
	}

}
