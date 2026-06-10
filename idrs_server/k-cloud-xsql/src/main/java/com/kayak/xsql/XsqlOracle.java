package com.kayak.xsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayak.xsql.helper.XsqlHelper;
import com.kayak.xsql.helper.XsqlHelperOracle;
import com.kayak.xsql.parameter.ParameterHandler;
import com.kayak.xsql.result.ResultHandler;

/**
 * Esql的Oracle实现
 * 
 * @author zuojie
 * 
 */
public class XsqlOracle extends XsqlImpl {
	private static final Logger log = LoggerFactory.getLogger(XsqlOracle.class);

	/**
	 * Oracle下的分页查询
	 */
	@Override
	public <T> List<T> page(Class<T> clazz, int offset, int limit, String sql, Object... params) throws Exception {
		Object param = normalize(params);

		if (log.isDebugEnabled()) {
			log.debug("分页查询: " + sql);
			logParams(params);
		}

		try {
			Connection db = getConnection();
			ParameterHandler ph = planParameterHandler(sql, param);
			String paged = "select /*+ FIRST_ROWS */ * from (select xsql_t1.*, rownum xsql_rownum from (" + ph.getSql(param)
					+ ") xsql_t1 where rownum <= " + (offset + limit) + ") xsql_t2 where xsql_rownum > " + offset;
			try (PreparedStatement ps = db.prepareStatement(paged)) {
				ph.setParameters(ps, param);
				log.info("执行SQL: {}", ps.toString());
				try (ResultSet rs = ps.executeQuery()) {
					ResultHandler<T> handler = planResultHandler(clazz, sql, ps);

					List<T> list = new ArrayList<>();
					while (rs.next()) {
						list.add(handler.build(rs));
					}
					return list;
				}
			}
		} finally {
			releaseConnection();
		}
	}

	@Override
	public String getTableInfoSql(String table) throws Exception {
		return "select * from " + table + " where rownum <= 1";
	}

	@Override
	public long getLastInsertId() throws Exception {
		String sql = "select last_insert_id()";
		return query(Long.class, sql);
	}

	private XsqlHelper helper = new XsqlHelperOracle();

	public XsqlOracle() {
		helper.setEsql(this);
	}

	@Override
	public XsqlHelper helper() {
		return helper;
	}

}
