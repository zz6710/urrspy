package com.kayak.base.dao;

import com.kayak.base.dao.sql.SqlConfig;
import com.kayak.base.dao.sql.SqlXmlServer;
import com.kayak.core.dao.DaoService;
import com.kayak.core.dao.Trans;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class BaseDao {

    protected static final Logger log = LoggerFactory.getLogger(BaseDao.class);

    @Autowired
    protected DaoService daoService;

    public <T> SqlResult<T> findRows(String sql, SqlParam<T> sqlParam) throws Exception {
        Sql _sql = Sql.build().defaultSql(sql);
        return findRows(_sql, 0, sqlParam);
    }

    public <T> SqlResult<T> findRows(String sql, String dbName, SqlParam<T> sqlParam) throws Exception {
        return findRows(sql, daoService.getSharding(dbName), sqlParam);
    }

    public <T> SqlResult<T> findRows(String sql, int sharding, SqlParam<T> sqlParam) throws Exception {
        Sql _sql = Sql.build().defaultSql(sql);
        return findRows(_sql, sharding, sqlParam);
    }

    public <T> SqlResult<T> findRows(Sql sql, SqlParam<T> sqlParam) throws Exception {
        return findRows(sql, 0, sqlParam);
    }

    public <T> SqlResult<T> findRows(Sql sql, String dbName, SqlParam<T> sqlParam) throws Exception {
        return findRows(sql, daoService.getSharding(dbName), sqlParam);
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
            AtomicReference<SqlResult<T>> t = new AtomicReference();
            daoService.doTrans(() -> {
                if (sqlParam.isPage()) {
                    t.set(daoService.page(sql.getSql(daoService.getDbType(sharding)), sqlParam));
                } else {
                    List<T> rows = daoService.list(sql.getSql(daoService.getDbType(sharding)), sqlParam);
                    t.set(SqlResult.build(rows));
                }
            }, sharding);

            return  t.get();
        }

    }

    public <T> List<T> findRows(Class<T> modelClass, String sql, String dbName, Object params) throws Exception {
        return findRows(modelClass, sql, daoService.getSharding(dbName), params);
    }

    public <T> List<T> findRows(Class<T> modelClass, String sql, int sharding, Object params) throws Exception {
        if (daoService.isTrans(sharding)) {
            return daoService.list(modelClass, sql, params);
        }else{
            AtomicReference<List<T>> t = new AtomicReference();
            daoService.doTrans(() -> {
                t.set( daoService.list(modelClass, sql, params));
            }, sharding);
            return t.get();
        }

    }

    public <T> List<T> findRows(Class<T> modelClass, Sql sql, String dbName, Object params) throws Exception {
        return findRows(modelClass, sql, daoService.getSharding(dbName), params);
    }

    public <T> List<T> findRows(Class<T> modelClass, Sql sql, int sharding, Object params) throws Exception {
        return findRows(modelClass, sql.getSql(daoService.getDbType(sharding)), sharding, params);
    }

    public List<SqlRow> findRows(String sql, String dbName, Object params) throws Exception {
        return findRows(sql, daoService.getSharding(dbName), params);
    }

    public List<SqlRow> findRows(String sql, int sharding, Object params) throws Exception {
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

    public <T> T findRow(Class<T> modelClass, String sql, String dbName, Object params) throws Exception {
        return findRow(modelClass, sql, daoService.getSharding(dbName), params);
    }

    public <T> T findRow(Class<T> modelClass, String sql, int sharding, Object params) throws Exception {
        if (daoService.isTrans(sharding)) {
            return daoService.query(modelClass, sql, params);
        }else{
            AtomicReference<T> t = new AtomicReference();
            daoService.doTrans(() -> {
                t.set(daoService.query(modelClass, sql, params));
            }, sharding);
            return t.get();
        }

    }

    public <T> T findRow(Class<T> modelClass, Sql sql, String dbName, Object params) throws Exception {
        return findRow(modelClass, sql, daoService.getSharding(dbName), params);
    }

    public <T> T findRow(Class<T> modelClass, Sql sql, int sharding, Object params) throws Exception {
        return findRow(modelClass, sql.getSql(daoService.getDbType(sharding)), sharding, params);
    }

    public SqlRow findRow(String sql, String dbName, Object params) throws Exception {
        return findRow(sql, daoService.getSharding(dbName), params);
    }

    public SqlRow findRow(String sql, int sharding, Object params) throws Exception {
        return findRow(SqlRow.class, sql, sharding, params);
    }

    public SqlRow findRow(String sql, Object params) throws Exception {
        return findRow(SqlRow.class, sql, 0, params);
    }

    public UpdateResult update(String sql, String dbName, Object params) throws Exception {
        return update(sql, daoService.getSharding(dbName), params);
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

    public UpdateResult update(String sql, Object params) throws Exception {
        return update(sql, 0, params);
    }

    public UpdateResult update(String sql) throws Exception {
        return update(sql, 0, null);
    }

    public UpdateResult update(Sql sql, String dbName, Object params) throws Exception {
        return update(sql, daoService.getSharding(dbName), params);
    }

    public UpdateResult update(Sql sql, int sharding, Object params) throws Exception {
        return update(sql.getSql(daoService.getDbType(sharding)), sharding, params);
    }

    public UpdateResult update(Sql sql, Object params) throws Exception {
        return update(sql.getSql(daoService.getDbType(0)), 0, params);
    }

    /**
     * 从XML获取SQL语句
     *
     * @param modelClass
     * @param action
     * @return
     * @throws PromptException
     */
    protected String getSqlFromXml(Class<?> modelClass, String action) throws PromptException {
        return getSqlFromXml(modelClass, action, null);
    }

    /**
     * 从XML获取SQL语句
     *
     * @param modelClass
     * @param action
     * @param dialect
     * @return
     * @throws PromptException
     */
    protected String getSqlFromXml(Class<?> modelClass, String action, String dialect) throws PromptException {
        String modelName = modelClass.getSimpleName();

        if (!SqlXmlServer.sqlCache.containsKey(modelName)) {
            throw new PromptException("不存在实体XML配置");
        }

        if (!SqlXmlServer.sqlCache.get(modelName).containsKey(action)) {
            throw new PromptException("实体XML不存在action配置");
        }

        SqlConfig sqlConfig = SqlXmlServer.sqlCache.get(modelName).get(action);

        return sqlConfig.getSql(dialect);
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
}
