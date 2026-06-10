package com.kayak.xsql;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.kayak.xsql.parameter.ParameterHandler;

/**
 * 批量处理接口
 * 
 * 
 * @author zuojie
 *
 */
public class BatchedXsql implements AutoCloseable {
	private XsqlImpl xsql;
	private String sql;
	private Connection db;
	private PreparedStatement ps;
	private ParameterHandler ph;

	public BatchedXsql(XsqlImpl xsql, String sql) {
		this.xsql = xsql;
		this.sql = sql;
	}

	public void addBatch(Object... params) throws Exception {
		Object param = xsql.normalize(params);

		if (ps == null) {
			db = xsql.getConnection();
			ph = xsql.planParameterHandler(sql, param);
			ps = db.prepareStatement(ph.getSql(param));
		}

		ph.setParameters(ps, param);
		ps.addBatch();
	}

	public int executeBatch() throws Exception {
		int[] rs = ps.executeBatch();
		int count = 0;
		for (int i = 0; i < rs.length; i++) {
			count += rs[i];
		}
		return count;
	}

	@Override
	public void close() throws Exception {
		try {
			if (ps != null) {
				ps.close();
			}
		} finally {
			xsql.releaseConnection();
		}
	}
}
