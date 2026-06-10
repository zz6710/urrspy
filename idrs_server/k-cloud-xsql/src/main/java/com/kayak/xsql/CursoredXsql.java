package com.kayak.xsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.kayak.xsql.parameter.ParameterHandler;
import com.kayak.xsql.result.ResultHandler;

/**
 * 游标化的Xsql接口
 * 
 * @author zuojie
 *
 */
public class CursoredXsql<T> implements AutoCloseable {
	private XsqlImpl xsql;

	private Class<T> clazz;
	private String sql;
	private Object param;

	private Connection db;
	private PreparedStatement ps;
	private ResultSet rs;
	private ResultHandler<T> rh;

	public CursoredXsql(XsqlImpl xsql, Class<T> type, String sql, Object param) {
		this.xsql = xsql;
		this.clazz = type;
		this.sql = sql;
		this.param = param;
	}

	public boolean next() throws Exception {
		if (rs == null) {
			db = xsql.getConnection();

			ParameterHandler ph = xsql.planParameterHandler(sql, param);
			ps = db.prepareStatement(ph.getSql(param));
			ph.setParameters(ps, param);

//			log.info("执行SQL: {}", ps.toString());
			rs = ps.executeQuery();
			rh = xsql.planResultHandler(clazz, sql, ps);
		}
		
		return rs.next();
	}

	public T get() throws Exception {
		return rh.build(rs);
	}

	@Override
	public void close() throws Exception {
		try {
			try {
				if (rs != null) {
					rs.close();
				}
			} finally {
				if (ps != null) {
					ps.close();
				}
			}
		} finally {
			if (db != null) {
				xsql.releaseConnection();
			}
		}
	}
}
