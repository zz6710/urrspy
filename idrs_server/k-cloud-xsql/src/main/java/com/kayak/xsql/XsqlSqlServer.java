package com.kayak.xsql;

import java.util.List;

import com.kayak.xsql.helper.XsqlHelper;
import com.kayak.xsql.helper.XsqlHelperSqlserver;

/**
 * Esql的MySQL实现
 * 
 * @author zuojie
 * 
 */
public class XsqlSqlServer extends XsqlImpl {
	// private static final Logger log =
	// LoggerFactory.getLogger(XsqlSqlServer.class);

	/**
	 * MySQL下的分页查询
	 * 
	 * 因为MySQL的JDBC驱动程序居然不支持limit子句的参数化，所以，只有用硬拼接的方式来产生最终的SQL语句。
	 * 但是又不能让参数处理器缓存无限制扩大，所以，使用原始的SQL作为key
	 */
	@Override
	public <T> List<T> page(Class<T> clazz, int offset, int limit, String sql, Object... params) throws Exception {
		/*
		 * select * from table order by id offset @offset rows fetch next @size rows
		 * only
		 */
		throw new RuntimeException("XSQL暂不支持SQL Server数据库的分页查询");
	}

	@Override
	public String getTableInfoSql(String table) throws Exception {
		return "select top 1 * from " + table;
	}

	@Override
	public long getLastInsertId() throws Exception {
		// TODO 并不确定该函数是否与last_insert_id()等价
		String sql = "select @@identity";
		return query(Long.class, sql);
	}

	private XsqlHelper helper = new XsqlHelperSqlserver();

	public XsqlSqlServer() {
		helper.setEsql(this);
	}

	@Override
	public XsqlHelper helper() {
		return helper;
	}

}
