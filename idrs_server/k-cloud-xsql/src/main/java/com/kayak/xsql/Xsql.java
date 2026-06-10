package com.kayak.xsql;

import com.kayak.core.sql.UpdateResult;
import com.kayak.xsql.helper.XsqlHelper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * Xsql核心
 * 
 * <p>
 * 输入: SQL：将标准JDBC的SQL语句中的'?'更改为$s{name}这种形式，s表示类型，花括号中表示名称，要和参数中的名称保持一致
 * 参数：可以是Map<String, Object>，也可以是普通的JavaBean，其字段名需要和SQL中的参数名对应起来；还可以是直接数据
 * 
 * <p>
 * 输出: 列名和对象的属性名对应, 类型要匹配，可以输出Map，也可以输出JavaBean
 * 
 * <p>
 * 示例代码:
 * 
 * <p>
 * 查单个对象
 * 
 * 第一种形式：直接数据。这种形式要求至少要传2个数据，如果只有1个，可以多传一个null <code><pre>
 * String sql = "select id, name from t_book where id = $s{id}";
 * Book book = xsql.query(Book.class, sql, 1, null);
 * 
 * System.err.printf("id: %d, name: %s\n", book.getId(), book.getName());
 * </pre></code>
 * 
 * 第二种形式：传JavaBean。SQL中的参数名需要和JavaBean中的字段名对应起来，Xsql会自动做下划线到驼峰命名规则的转换
 * <code><pre>
 * String sql = "select id, name from t_book where author_name = $s{author_name}";
 * Book param = new Book();
 * param.setAuthorName("马克思");
 * Book book = xsql.query(Book.class, sql, param);
 * 
 * System.err.printf("id: %d, name: %s\n", book.getId(), book.getName());
 * </pre></code>
 * 
 * 第三种形式：传Map<String, Object>。SQL中的参数名需要和Map中的字段名对应起来，Xsql会自动做下划线到驼峰命名规则的转换
 * <code><pre>
 * String sql = "select id, name from t_book where id = $s{id}";
 * Map<String, Object> param = new HashMap<>();
 * param.put("id", 1);
 * Book book = xsql.query(Book.class, sql, param);
 * 
 * System.err.printf("id: %d, name: %s\n", book.getId(), book.getName());
 * </pre></code>
 * 
 * 
 * @author zuojie
 * 
 */
public interface Xsql {
	public static final int READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
	public static final int SERIALIZABLE = Connection.TRANSACTION_REPEATABLE_READ;
	// public static final int SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;

	/** 返回帮助器 */
	public XsqlHelper helper();

	/** 返回工具 */
//	public XsqlTool tool();

//	/** 设置默认数据源 */
//	public void setDefaultDataSource(DataSource source);

	/** 选择数据源 */
	public AutoCloseable selectDataSource(DataSource source, int index);

	/** 获取当前选中的数据源的索引 */
	public int getDataSourceIndex();

	/** 获取底层数据库连接 注意：不要自行调用Connection的close方法 */
	public Connection getConnection() throws Exception;

	/** 释放连接 */
	public void releaseConnection();

	// -----------------------------

	/**
	 * 获取当前连接的数据库名称。可以用于判断当前连接的数据库的类型，在需要判断时，可使用xsql提供的常量进行判断{@link DatabaseName}
	 */
	public String databaseName() throws SQLException;

	// -----------------------------

	/** 开始一个事务，采用指定的并发隔离度，从指定连接池取一个连接 */
	public void begin() throws Exception;

	/** 提交事务。如果真正提交了一个事务，返回真，否则返回假 */
	public boolean commit() throws Exception;

	/** 结束事务，尚未提交的事务被回滚。如果真正结束了一个事务，返回真，如果事务没有开始，或者是内部嵌套事务的结束，返回假 */
	public boolean end();

	// -----------------------------

	/**
	 * 执行返回一行结果的查询，将结果封装成指定类型的对象。
	 */
	public <T> T query(Class<T> clazz, String sql, Object... params) throws Exception;

	/** 执行返回多行结果的查询，将结果封装成指定类型的对象列表。 */
	public <T> List<T> list(Class<T> clazz, String sql, Object... params) throws Exception;
	
	public <T> List<T> listNoSqlLog(Class<T> clazz, String sql, Object... params) throws Exception;

	/** 执行返回多行结果的查询，将结果分页封装成指定类型的对象列表。 */
	public <T> List<T> page(Class<T> type, int offset, int limit, String sql, Object... params) throws Exception;

	/** 执行更新查询，返回更新行数。 */
	public UpdateResult update(String sql, Object... params) throws Exception;
	
	/** 执行更新查询，返回更新行数。 */
	public UpdateResult updateNoLog(String sql, Object... params) throws Exception;

	/**
	 * 开始一个批量更新操作。
	 * 1. 用完后，务必关闭BatchedXsql，请使用try-with-resouce的方式编写
	 * 2. 批量操作中的每次操作的参数形式必须完全一致
	 */
	public BatchedXsql batch(String sql) throws Exception;

	/**
	 * 通过游标迭代得到结果
	 * 1. 用完后，务必关闭CursoredXsql，请使用try-with-resource的方式编写
	 */
	public <T> CursoredXsql<T> iterate(Class<T> clazz, String sql, Object... params) throws Exception;

	/** 获取表格信息 */
	public String getTableInfoSql(String table) throws Exception;

	/** 获得刚才插入产生的自增字段的值 */
	public long getLastInsertId() throws Exception;

	ResultSetMetaData getMetaData(String sql, Object... params) throws Exception;
}
