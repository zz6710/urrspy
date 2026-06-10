package com.kayak.config.utils;

import com.kayak.clear.utils.Tools;
import com.kayakwise.kcloud.db.*;
import com.kayakwise.kcloud.db.exception.SqlRuntimeException;
import com.kayakwise.kcloud.db.util.DbUtil;
import com.kayakwise.kcloud.db.util.ParamMap;
import com.kayakwise.kcloud.db.util.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.Tool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SimpleSqlChange {
    private static final Logger log = LoggerFactory.getLogger("kcloud.db.SimpleSql");
    protected final SqlParams sqlParams;
    private final PreparedStatement statement;

    SimpleSqlChange(String inSql, Connection conn) throws SQLException {
        this.sqlParams = SqlStatementParser.parse(inSql);
        this.statement = conn.prepareStatement(this.sqlParams.getSql());
    }

    SimpleSqlChange(String inSql, Connection conn, int type, int concurrency) throws SQLException {
        this.sqlParams = SqlStatementParser.parse(inSql);
        this.statement = conn.prepareStatement(this.sqlParams.getSql(), type, concurrency);
    }

    public boolean execute(ParamMap params) throws SQLException {
        if (this.statement.isClosed()) {
            throw new SqlRuntimeException("the sql statement is closed!");
        } else {
            boolean var2;
            try {
                this.fillParams(this.statement, params);
                var2 = this.statement.execute();
            } catch (Exception var6) {
                this.statementError(this.statement, this.sqlParams);
                throw var6;
            } finally {
                DbUtil.close(this.statement);
            }

            return var2;
        }
    }

    public int executeUpdate(ParamMap params) throws SQLException {
        if (this.statement.isClosed()) {
            throw new SqlRuntimeException("the sql statement is closed!");
        } else {
            int var2;
            try {
                this.fillParams(this.statement, params);
                var2 = this.statement.executeUpdate();
            } catch (Exception var6) {
                this.statementError(this.statement, this.sqlParams);
                throw var6;
            } finally {
                DbUtil.close(this.statement);
            }

            return var2;
        }
    }

    public SqlResult executeQuery(ParamMap params) throws SQLException {
        if (this.statement.isClosed()) {
            throw new SqlRuntimeException("the sql statement is closed!");
        } else {
            ResultSet rs = null;

            SqlResult var3;
            try {
                this.fillParams(this.statement, params);
                rs = this.statement.executeQuery();
                var3 = new SqlResult(rs);
            } catch (Exception var7) {
                this.statementError(this.statement, this.sqlParams);
                throw var7;
            } finally {
                DbUtil.close(rs);
                DbUtil.close(this.statement);
            }

            return var3;
        }
    }

    public SqlCursorResult executeQueryCursor(ParamMap params) throws SQLException {
        if (this.statement.isClosed()) {
            throw new SqlRuntimeException("the sql statement is closed!");
        } else {
            try {
                this.fillParams(this.statement, params);
            } catch (Exception var3) {
                this.statementError(this.statement, this.sqlParams);
                DbUtil.close(this.statement);
                throw var3;
            }

            return new SqlCursorResult(this.statement.executeQuery());
        }
    }

    public Object executeQueryOne(ParamMap params) throws SQLException {
        if (this.statement.isClosed()) {
            throw new SqlRuntimeException("the sql statement is closed!");
        } else {
            ResultSet rs = null;

            Object var3;
            try {
                this.fillParams(this.statement, params);
                rs = this.statement.executeQuery();
                if (!rs.next()) {
                    var3 = null;
                    return var3;
                }

                var3 = rs.getObject(1);
            } catch (Exception var7) {
                this.statementError(this.statement, this.sqlParams);
                throw var7;
            } finally {
                DbUtil.close(rs);
                DbUtil.close(this.statement);
            }

            return var3;
        }
    }

    public int[] batchUpdate(ParamMap[] params) throws SQLException {
        if (this.statement.isClosed()) {
            throw new SqlRuntimeException("the sql statement is closed!");
        } else {
            try {
                ParamMap[] var2 = params;
                int var3 = params.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    ParamMap pms = var2[var4];
                    this.fillParams(this.statement, pms);
                    this.statement.addBatch();
                }

                int[] var11 = this.statement.executeBatch();
                return var11;
            } catch (Exception var9) {
                this.statementError(this.statement, this.sqlParams);
                throw var9;
            } finally {
                DbUtil.close(this.statement);
            }
        }
    }

    public int[] batchUpdate(Collection<ParamMap> params) throws SQLException {
        if (this.statement.isClosed()) {
            throw new SqlRuntimeException("the sql statement is closed!");
        } else {
            try {
                Iterator var2 = params.iterator();

                while(var2.hasNext()) {
                    ParamMap pms = (ParamMap)var2.next();
                    this.fillParams(this.statement, pms);
                    this.statement.addBatch();
                }

                int[] var9 = this.statement.executeBatch();
                return var9;
            } catch (Exception var7) {
                this.statementError(this.statement, this.sqlParams);
                throw var7;
            } finally {
                DbUtil.close(this.statement);
            }
        }
    }

    public int[] batchUpdate(Object... params) throws SQLException {
        if (this.statement.isClosed()) {
            throw new SqlRuntimeException("the sql statement is closed!");
        } else {
            int[] var11;
            try {
                int paramNum = this.sqlParams.getParamsNames().size();
                int len = params.length;
                if (len < paramNum || len % paramNum > 0) {
                    throw new SqlRuntimeException("the parameters number to execute batch sql are not match to the sql paramters number!");
                }

                for(int i = 0; i < len; ++i) {
                    int mod = i % paramNum + 1;
                    SqlUtil.setPreparedStatement(this.statement, mod + 1, params[i]);
                    if (mod == paramNum) {
                        this.statement.addBatch();
                    }
                }

                var11 = this.statement.executeBatch();
            } catch (Exception var9) {
                this.statementError(this.statement, this.sqlParams);
                throw var9;
            } finally {
                DbUtil.close(this.statement);
            }

            return var11;
        }
    }

    private void fillParams(PreparedStatement preparedStatement, ParamMap params) throws SQLException {
        List<String> names = this.sqlParams.getParamsNames();
        int index = 1;

        for(Iterator var5 = names.iterator(); var5.hasNext(); ++index) {
            String name = (String)var5.next();
            if (!params.containsKey(name)) {
                throw new SQLException(String.format("未找到对应参数[%s]", name));
            }
            if(params.get(name)==null || params.get(name).toString().equals("")){
                SqlUtil.setPreparedStatement(preparedStatement, index, null);
            }else{
                SqlUtil.setPreparedStatement(preparedStatement, index, params.get(name));
            }

        }

    }

    private void statementError(PreparedStatement statement, SqlParams sqlParams) {
        log.error("statement error. SQLID:[{}] DBName:[{}] SQL:[{}]", new Object[]{Dbop.LAST_SQLID(), Dbop.getCurrDataSourceName(), sqlParams.getSql()});
    }

    public SqlParams getSqlParams() {
        return this.sqlParams;
    }

    public PreparedStatement getPreparedStatement() {
        return this.statement;
    }
}
