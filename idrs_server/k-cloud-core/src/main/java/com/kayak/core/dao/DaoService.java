package com.kayak.core.dao;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库操作实现接口
 * 
 * @author liuyg
 *
 */
public interface DaoService {

	public AutoCloseable selectDataSource(int sharding);

	public AutoCloseable selectDataSource(String dbName);

	public DataSource getMaster();

	public String getDbType(int sharding);

	public void doTrans(Trans trans) throws Exception;

	public boolean isTrans(int sharding);

	public boolean isTrans(String dbName);

	public void doTrans(Trans trans, int sharding) throws Exception;

	public void doTrans(Trans trans, String dbName) throws Exception;

	public <T> T query(Class<T> clazz, String sql, Object... params) throws Exception;

	public <T> List<T> list(Class<T> clazz, String sql, Object... params) throws Exception;

	public <T> List<T> list(String sql, SqlParam<T> sqlParam) throws Exception;

	public <T> SqlResult<T> page(String sql, SqlParam<T> sqlParam) throws Exception;

	public UpdateResult update(String sql, Object... params) throws Exception;
	public UpdateResult updateNoLog(String sql, Object... params) throws Exception;

	public <T> UpdateResult update(String sql, SqlParam<T> sqlParam) throws Exception;

	public Connection getConnection() throws SQLException;

	/** 开始一个事务，采用指定的并发隔离度，从指定连接池取一个连接 */
	public void begin() throws Exception;

	/** 提交事务。如果真正提交了一个事务，返回真，否则返回假 */
	public boolean commit() throws Exception;

	/** 结束事务，尚未提交的事务被回滚。如果真正结束了一个事务，返回真，如果事务没有开始，或者是内部嵌套事务的结束，返回假 */
	public boolean end();

	public String getConnectionKeepDetail();

	String planParameterSql(String sql, Object params) throws Exception;

	public Integer getSharding(String dbName);

	public ResultSetMetaData getMetaData(String sql, Object... params) throws Exception;

}
