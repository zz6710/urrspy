package com.kayak.xsql;

import java.util.ArrayList;
import java.util.List;

import com.kayak.core.sql.UpdateResult;

/**
 * 查询构造器
 * 
 * @author zuojie
 * 
 */
public class QueryBuilder {
	private StringBuilder sql = new StringBuilder();
	private List<Object> params = new ArrayList<Object>();

	@Override
	public String toString() {
		return sql.toString();
	}

	public QueryBuilder(String segment, Object... values) {
		append(segment, values);
	}

	public QueryBuilder append(String segment, Object... values) {
		sql.append(segment);
		for (Object value : values) {
			params.add(value);
		}

		return this;
	}

	public QueryBuilder append(QueryBuilder qb) {
		this.sql.append(qb.sql);
		this.params.addAll(qb.params);

		return this;
	}

	public String getSql() {
		return sql.toString();
	}

	public <T> T query(Xsql xsql, Class<T> type) throws Exception {
		List<Object> params2 = new ArrayList<>(params);
		params2.add(null);
		params2.add(null);
		return xsql.query(type, sql.toString(), params2.toArray());
	}

	public <T> List<T> list(Xsql xsql, Class<T> type) throws Exception {
		List<Object> params2 = new ArrayList<>(params);
		params2.add(null);
		params2.add(null);
		return xsql.list(type, sql.toString(), params2.toArray());
	}

	public <T> List<T> page(Xsql xsql, Class<T> type, int offset, int limit) throws Exception {
		List<Object> params2 = new ArrayList<>(params);
		params2.add(null);
		params2.add(null);
		return xsql.page(type, offset, limit, sql.toString(), params2.toArray());
	}

	public UpdateResult update(Xsql xsql) throws Exception {
		List<Object> params2 = new ArrayList<>(params);
		params2.add(null);
		params2.add(null);
		return xsql.update(sql.toString(), params2.toArray());
	}
}
