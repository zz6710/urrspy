package com.kayak.config.utils;

import com.kayakwise.kcloud.db.*;
import com.kayakwise.kcloud.db.datasource.DataSourceBuilder;
import com.kayakwise.kcloud.db.dialect.IDialect;
import com.kayakwise.kcloud.db.exception.SqlRuntimeException;
import com.kayakwise.kcloud.db.sql.DynamicSql;
import com.kayakwise.kcloud.db.sql.DynamicSqlConfig;
import com.kayakwise.kcloud.db.sql.ISqlLoader;
import com.kayakwise.kcloud.db.util.DbUtil;
import com.kayakwise.kcloud.db.util.ParamMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

/**
 * @author zhanglinsong
 */
@Repository
@Scope("singleton")
public class DbopChange{
    private static final Logger log = LoggerFactory.getLogger("config.utils.dbopChange");
    @Autowired
    private ISqlLoader sqlLoader;
    @Autowired
    private DataSourceBuilder builder;
    private static final ThreadLocal<String> DATA_SOURCE_NAMES = new ThreadLocal();
    private static final ThreadLocal<Connection> localConnections = new ThreadLocal();
    private static final ThreadLocal<Dbtrans> localTransactions = new ThreadLocal();
    private static final ThreadLocal<String> executingSqlid = new ThreadLocal();
    private static final ParamMap[] EMPTY_PARAMMAP_ARRAY = new ParamMap[0];

    public DbopChange() {
    }

    public static void setDataSourceName(String dataSourceName) {
        Connection connection = (Connection)localConnections.get();

        try {
            if (connection != null && !connection.isClosed()) {
                throw new SqlRuntimeException("Current thread is already in transaction");
            } else {
                if (dataSourceName == null) {
                    dataSourceName = "default";
                }

                DATA_SOURCE_NAMES.set(dataSourceName);
                if (log.isDebugEnabled()) {
                    log.debug("Set Datasource:[{}]", dataSourceName);
                }

            }
        } catch (SQLException var3) {
            log.error(var3.getMessage(), var3);
            throw new SqlRuntimeException("SQLException: " + var3.getMessage());
        }
    }

    public static String getCurrDataSourceName() {
        return (String)DATA_SOURCE_NAMES.get();
    }

    public static String getDefaultDataSourceName() {
        return "default";
    }

    private String getDataSourceName() {
        String dataSourceName = (String)DATA_SOURCE_NAMES.get();
        if (dataSourceName == null) {
            dataSourceName = "default";
            DATA_SOURCE_NAMES.set(dataSourceName);
        }

        return dataSourceName;
    }

    public IDialect getDialect() {
        return this.builder.getDialect(this.getDataSourceName());
    }

    private Connection getConnection() throws SQLException {
        String dataSourceName = this.getDataSourceName();
        Connection conn = (Connection)localConnections.get();
        if (conn == null || conn.isClosed()) {
            DataSource dataSource = this.builder.dataSource(dataSourceName);
            conn = dataSource.getConnection();
            localConnections.set(conn);
        }

        return conn;
    }

    public Dbtrans starttrans() throws SQLException {
        Dbtrans transaction = (Dbtrans)localTransactions.get();
        if (transaction != null) {
            throw new SQLException("transaction already started!");
        } else {
            Connection conn = null;

            try {
                conn = this.getConnection();
                if (!conn.getAutoCommit()) {
                    throw new SQLException("transaction already started!");
                } else {
                    conn.setAutoCommit(false);
                    transaction = new Dbtrans();
                    localTransactions.set(transaction);
                    log.info("---------- StartTrans ----------");
                    return transaction;
                }
            } catch (Exception var4) {
                DbUtil.close(conn);
                localTransactions.remove();
                log.error("事务开启异常:[{}]", var4.getMessage(), var4);
                throw var4;
            }
        }
    }

    public void commit() throws SQLException {
        if (localTransactions.get() == null) {
            throw new SQLException("transaction not started!");
        } else {
            Connection conn = (Connection)localConnections.get();
            Throwable var2 = null;

            try {
                localTransactions.remove();
                if (conn != null && !conn.isClosed()) {
                    conn.commit();
                    if (!conn.getAutoCommit()) {
                        conn.setAutoCommit(true);
                    }

                    log.info("----------   Commit   ----------");
                }
            } catch (Throwable var11) {
                var2 = var11;
                throw var11;
            } finally {
                if (conn != null) {
                    if (var2 != null) {
                        try {
                            conn.close();
                        } catch (Throwable var10) {
                            var2.addSuppressed(var10);
                        }
                    } else {
                        conn.close();
                    }
                }

            }

        }
    }

    public void rollback() throws SQLException {
        ROLLBACK();
    }

    protected static void ROLLBACK() throws SQLException {
        Statement statement = null;
        if (localTransactions.get() != null) {
            statement = ((Dbtrans)localTransactions.get()).getStatement();
            localTransactions.remove();
        }

        Connection conn = (Connection)localConnections.get();
        Throwable var2 = null;

        try {
            if (statement != null && !statement.isClosed()) {
                ResultSet rs = statement.getResultSet();
                DbUtil.close(rs);
                DbUtil.close(statement);
            }

            if (conn != null && !conn.isClosed()) {
                conn.rollback();
                if (!conn.getAutoCommit()) {
                    conn.setAutoCommit(true);
                }
            }
        } catch (Throwable var11) {
            var2 = var11;
            throw var11;
        } finally {
            if (conn != null) {
                if (var2 != null) {
                    try {
                        conn.close();
                    } catch (Throwable var10) {
                        var2.addSuppressed(var10);
                    }
                } else {
                    conn.close();
                }
            }

        }

    }

    public Object selectOne(String sqlid, ParamMap params) throws SQLException {
        executingSqlid.set(sqlid);
        Object obj = this.selectOneSql(this.getSql(sqlid, params), params);
        executingSqlid.remove();
        return obj;
    }

    protected Object selectOneSql(String sql, ParamMap params) throws SQLException {
        if (localTransactions.get() == null) {
            Connection conn = this.getConnection();
            Throwable var4 = null;

            Object var6;
            try {
                SimpleSqlChange sim = new SimpleSqlChange(sql, conn);
                var6 = sim.executeQueryOne(params);
            } catch (Throwable var15) {
                var4 = var15;
                throw var15;
            } finally {
                if (conn != null) {
                    if (var4 != null) {
                        try {
                            conn.close();
                        } catch (Throwable var14) {
                            var4.addSuppressed(var14);
                        }
                    } else {
                        conn.close();
                    }
                }

            }

            return var6;
        } else {
            SimpleSqlChange sim = new SimpleSqlChange(sql, this.getConnection());
            return sim.executeQueryOne(params);
        }
    }

    public SqlResult select(String sqlid, ParamMap params) throws SQLException {
        return this.select(sqlid, params, (Integer)null, (Integer)null);
    }

    public SqlResult select(String sqlid, ParamMap params, Integer start, Integer limit) throws SQLException {
        executingSqlid.set(sqlid);
        String sql = this.getSql(sqlid, params);
        SqlResult sqlResult = this.selectSqlChange(sql, params, start, limit);
        executingSqlid.remove();
        return sqlResult;
    }

    public <T> T selectOne(Class<T> type, String sqlid, ParamMap params) throws SQLException {
        return null;
    }

    public <T> T select(Class<T> type, String sqlid, ParamMap params) {
        return null;
    }

    public <T> T select(Class<T> type, String sqlid, ParamMap params, Integer start, Integer limit) {
        return null;
    }

    public SqlResult selectSqlChange(String sql, ParamMap params, Integer start, Integer limit) throws SQLException {
        if (start != null && limit != null) {
            sql = this.getDialect().getLimitString(sql, start, limit);
        }

        if (localTransactions.get() == null) {
            Connection conn = this.getConnection();
            Throwable var6 = null;

            SqlResult var8;
            try {
                SimpleSqlChange sim = new SimpleSqlChange(sql, conn);
                var8 = sim.executeQuery(params);
            } catch (Throwable var17) {
                var6 = var17;
                throw var17;
            } finally {
                if (conn != null) {
                    if (var6 != null) {
                        try {
                            conn.close();
                        } catch (Throwable var16) {
                            var6.addSuppressed(var16);
                        }
                    } else {
                        conn.close();
                    }
                }

            }

            return var8;
        } else {
            SimpleSqlChange sim = new SimpleSqlChange(sql, this.getConnection());
            return sim.executeQuery(params);
        }
    }

    public SqlCursorResult selectCursor(String sqlid, ParamMap params) throws SQLException {
        return this.selectCursor(sqlid, params, (Integer)null, (Integer)null);
    }

    public SqlCursorResult selectCursor(String sqlid, ParamMap params, Integer start, Integer limit) throws SQLException {
        executingSqlid.set(sqlid);
        String sql = this.getSql(sqlid, params);
        SqlCursorResult sqlCursorResult = this.selectCursorSql(sql, params, start, limit);
        executingSqlid.remove();
        return sqlCursorResult;
    }

    protected SqlCursorResult selectCursorSql(String sql, ParamMap params, Integer start, Integer limit) throws SQLException {
        if (start != null && limit != null) {
            sql = this.getDialect().getLimitString(sql, start, limit);
        }

        if (localTransactions.get() == null) {
            throw new SQLException("未开启事物,游标查询需要手动开启事物");
        } else if (((Dbtrans)localTransactions.get()).getStatement() != null) {
            throw new SQLException("存在未关闭 Statement");
        } else {
            SimpleSqlChange sim = new SimpleSqlChange(sql, this.getConnection(), 1004, 1007);
            SqlCursorResult sqlCursorResult = sim.executeQueryCursor(params);
            ((Dbtrans)localTransactions.get()).setStatement(sim.getPreparedStatement());
            return sqlCursorResult;
        }
    }

    public int update(String sqlid, ParamMap params) throws SQLException {
        executingSqlid.set(sqlid);
        int iRet = this.updateSql(this.getSql(sqlid, params), params);
        executingSqlid.remove();
        return iRet;
    }

    protected int updateSql(String sql, ParamMap params) throws SQLException {
        if (localTransactions.get() == null) {
            Connection conn = this.getConnection();
            Throwable var4 = null;

            int var6;
            try {
                SimpleSqlChange sim = new SimpleSqlChange(sql, conn);
                var6 = sim.executeUpdate(params);
            } catch (Throwable var15) {
                var4 = var15;
                throw var15;
            } finally {
                if (conn != null) {
                    if (var4 != null) {
                        try {
                            conn.close();
                        } catch (Throwable var14) {
                            var4.addSuppressed(var14);
                        }
                    } else {
                        conn.close();
                    }
                }

            }

            return var6;
        } else {
            SimpleSqlChange sim = new SimpleSqlChange(sql, this.getConnection());
            return sim.executeUpdate(params);
        }
    }
    public int[] updateBatch(String sqlid, Collection<ParamMap> params) throws SQLException {
        return this.updateBatch(sqlid, (ParamMap[])params.toArray(EMPTY_PARAMMAP_ARRAY));
    }

    public int[] updateBatch(String sqlid, ParamMap[] params) throws SQLException {
        executingSqlid.set(sqlid);
        int[] iRet = this.updateBatchSqlChange(this.getSql(sqlid, params[0]), params);
        executingSqlid.remove();
        return iRet;
    }
    public int[] updateBatchSqlChange(String sql, ParamMap[] params) throws SQLException {
        if (localTransactions.get() == null) {
            Dbtrans dbtrans = this.starttrans();
            Throwable var5 = null;
            int[] iRetArry;
            try {
                SimpleSqlChange sim = new SimpleSqlChange(sql, this.getConnection());
                iRetArry = sim.batchUpdate(params);
                this.commit();
            } catch (Throwable var14) {
                var5 = var14;
                throw var14;
            } finally {
                if (dbtrans != null) {
                    if (var5 != null) {
                        try {
                            ROLLBACK();
//                           dbtrans.close();
                        } catch (Throwable var13) {
                            var5.addSuppressed(var13);
                        }
                    } else {
                        ROLLBACK();
//                        dbtrans.close();
                    }
                }

            }

            return iRetArry;
        } else {
            SimpleSqlChange sim = new SimpleSqlChange(sql, this.getConnection());
            return sim.batchUpdate(params);
        }
    }



    public String getSql(String sqlid, ParamMap params) throws SQLException {
        DynamicSqlConfig dynamicSqlConfig = (DynamicSqlConfig)this.sqlLoader.getSqls().get(sqlid);
        if (dynamicSqlConfig == null) {
            throw new SQLException(String.format("未查询到 SqlID:[%s] 信息", sqlid));
        } else {
            String dbType = this.builder.getDbtype(this.getDataSourceName());
            DynamicSql dynamicSql = dynamicSqlConfig.get(dbType);
            if (dynamicSql == null) {
                throw new SQLException(String.format("SqlID:[%s] 不存在 DbType:[%s] 类型节点", sqlid, dbType));
            } else {
                return dynamicSql.toString(params);
            }
        }
    }

    public static String LAST_SQLID() {
        String sqlid = (String)executingSqlid.get();
        return sqlid == null ? "" : sqlid;
    }

}
