package com.kayak.base.dao;

import com.kayak.core.dao.DaoService;
import com.kayak.core.dao.Trans;
import com.kayak.core.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * 数据库操作基础抽象类
 *
 * @author liuyg
 *
 */
@Repository
public class ComnDao {

	protected static final Logger log = LoggerFactory.getLogger(ComnDao.class);

	@Autowired
	protected DaoService daoService;

	public <T> SqlResult<T> findRows(String sql, SqlParam<T> sqlParam) throws Exception {
		Sql _sql = Sql.build().mysqlSql(sql).oracleSql(sql).db2Sql(sql);
		return findRows(_sql, 0, sqlParam);
	}

	public <T> SqlResult<T> findRows(String sql, int sharding, SqlParam<T> sqlParam) throws Exception {
		Sql _sql = Sql.build().mysqlSql(sql).oracleSql(sql).db2Sql(sql);
		return findRows(_sql, sharding, sqlParam);
	}

	public <T> SqlResult<T> findRows(Sql sql, SqlParam<T> sqlParam) throws Exception {
		return findRows(sql, 0, sqlParam);
	}

	public <T> SqlResult<T> findRows(Sql sql, int sharding, SqlParam<T> sqlParam) throws Exception {
		if (daoService.isTrans(sharding)) {
			if (sqlParam.isPage()) {
				return daoService.page(sql.getSql(daoService.getDbType(sharding)), sqlParam);
			} else {
				List<T> rows = daoService.list(sql.getSql(daoService.getDbType(sharding)), sqlParam);
				return SqlResult.build(rows);
			}
		} else {
			try (AutoCloseable ca = daoService.selectDataSource(sharding)) {
				if (sqlParam.isPage()) {
					return daoService.page(sql.getSql(daoService.getDbType(sharding)), sqlParam);
				} else {
					List<T> rows = daoService.list(sql.getSql(daoService.getDbType(sharding)), sqlParam);
					return SqlResult.build(rows);
				}
			}
		}
	}

	public <T> List<T> findRows(Class<T> modelClass, String sql, int sharding, Object params) throws Exception {
		try (AutoCloseable ca = daoService.selectDataSource(sharding)) {
			return daoService.list(modelClass, sql, params);
		}
	}

	public <T> List<T> findRows(Class<T> modelClass, Sql sql, int sharding, Object params) throws Exception {
		return findRows(modelClass, sql.getSql(daoService.getDbType(sharding)), sharding, params);
	}

	public List<SqlRow>  findRows(String sql, int sharding, Object params) throws Exception {
		return findRows(SqlRow.class, sql, sharding, params);
	}

	public List<SqlRow> findRows(String sql, Object params) throws Exception {
		return findRows(SqlRow.class, sql, 0, params);
	}

	public List<SqlRow> findRows(String sql, int sharding) throws Exception {
		return findRows(SqlRow.class, sql, sharding, null);
	}

	public List<SqlRow> findRows(String sql) throws Exception {
		return findRows(SqlRow.class, sql, 0, null);
	}

	public <T> T findRow(Class<T> modelClass, String sql, int sharding, Object params) throws Exception {
		try (AutoCloseable ca = daoService.selectDataSource(sharding)) {
			return daoService.query(modelClass, sql, params);
		}
	}

	public <T> T findRow(Class<T> modelClass, Sql sql, int sharding, Object params) throws Exception {
		return findRow(modelClass, sql.getSql(daoService.getDbType(sharding)), sharding, params);
	}

	public SqlRow findRow(String sql, int sharding, Object params) throws Exception {
		return findRow(SqlRow.class, sql, sharding, params);
	}

	public SqlRow findRow(String sql, Object params) throws Exception {
		return findRow(SqlRow.class, sql, 0, params);
	}

	public UpdateResult update(String sql, int sharding, Object params) throws Exception {
		if (daoService.isTrans(sharding)) {
			return daoService.update(sql, params);
		} else {
			final UpdateResult updateResult = new UpdateResult();
			daoService.doTrans(() -> {
				UpdateResult _updateResult = daoService.update(sql, params);

				updateResult.setAutoId(_updateResult.getAutoId());
				updateResult.setEffect(_updateResult.getEffect());
			}, sharding);

			return updateResult;

//			try (AutoCloseable ca = daoService.selectDataSource(sharding)) {
//				return daoService.update(sql, params);
//			}
		}
	}
	public UpdateResult updateNoLog(String sql, int sharding, Object params) throws Exception {
		if (daoService.isTrans(sharding)) {
			return daoService.updateNoLog(sql, params);
		} else {
			final UpdateResult updateResult = new UpdateResult();
			daoService.doTrans(() -> {
				UpdateResult _updateResult = daoService.updateNoLog(sql, params);

				updateResult.setAutoId(_updateResult.getAutoId());
				updateResult.setEffect(_updateResult.getEffect());
			}, sharding);

			return updateResult;


		}
	}

	public UpdateResult update(String sql, Object params) throws Exception {
		return update(sql, 0, params);
	}

	public UpdateResult update(String sql) throws Exception {
		return update(sql, 0, null);
	}

	public UpdateResult update(Sql sql, int sharding, Object params) throws Exception {
		return update(sql.getSql(daoService.getDbType(sharding)), sharding, params);
	}

	public UpdateResult update(Sql sql, Object params) throws Exception {
		return update(sql.getSql(daoService.getDbType(0)), 0, params);
	}

	/**
	 * 事务操作
	 *
	 * @param trans
	 * @throws Exception
	 */
	public void doTrans(Trans trans) throws Exception {
		daoService.doTrans(trans);
	}

	public Connection getConnection() throws SQLException {
		return daoService.getConnection();
	}

	public ResultSetMetaData getMetaData(String sql, int sharding, Object... params) throws Exception {
		try (AutoCloseable ca = daoService.selectDataSource(sharding)) {
			return daoService.getMetaData(sql, params);
		}
	}

}
