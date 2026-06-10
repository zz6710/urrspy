package com.kayak.xsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayak.xsql.helper.XsqlHelper;
import com.kayak.xsql.helper.XsqlHelperMysql;
import com.kayak.xsql.parameter.ParameterHandler;
import com.kayak.xsql.result.ResultHandler;

/**
 * Esql的MySQL实现
 * 
 * @author zuojie
 * 
 */
public class XsqlMysql extends XsqlImpl {
	private static final Logger log = LoggerFactory.getLogger(XsqlMysql.class);

	/**
	 * MySQL下的分页查询
	 * 
	 * 因为MySQL的JDBC驱动程序居然不支持limit子句的参数化，所以，只有用硬拼接的方式来产生最终的SQL语句。
	 * 但是又不能让参数处理器缓存无限制扩大，所以，使用原始的SQL作为key
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
			String paged = "select * from (" + ph.getSql(param) + ") xsql_t limit " + offset + ", " + limit;
			try (PreparedStatement ps = db.prepareStatement(paged)) {
				StringBuilder sb = ph.setParameters(ps, param, true);

				log.info("分页执行SQL: {}", "select * from (" + sb.toString() + ") xsql_t limit " + offset + ", " + limit);
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
		return "select * from " + table + " limit 1";
	}

	@Override
	public long getLastInsertId() throws Exception {
		String sql = "select last_insert_id()";
		return query(Long.class, sql);
	}

	private XsqlHelper helper = new XsqlHelperMysql();

	public XsqlMysql() {
		helper.setEsql(this);
	}

	@Override
	public XsqlHelper helper() {
		return helper;
	}

}
