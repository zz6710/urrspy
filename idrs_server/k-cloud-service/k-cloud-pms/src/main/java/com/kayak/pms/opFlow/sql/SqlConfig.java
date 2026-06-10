package com.kayak.pms.opFlow.sql;

import com.kayak.core.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用SQL配置模型
 */
public class SqlConfig {

    public class Sql {
        String sql;
        String dialect;


        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            if (sql == null) {
                this.sql = "";
            } else {
                this.sql = sql.trim();
            }
        }

        public String getDialect() {
            return dialect;
        }

        public void setDialect(String dialect) {
            this.dialect = dialect;
        }

    }

    private List<Sql> sqls = new ArrayList<Sql>();

    private List<SqlCheck> checks;

    private String action;
    private String desc;
    private String auth;
    private String makeSearchSql;
    private int datasource;

    private String repeat;

    private SqlInterceptor sqlInterceptor;

    private List<ActionRef> actionRefs = new ArrayList<>();

    private boolean log = true;

    public SqlConfig(String action, String desc, String auth, String makeSearchSql, boolean log, int datasource, String interceptor,String repeat) throws Exception {
        this.action = action;
        this.desc = desc;
        this.auth = auth;
        this.makeSearchSql = makeSearchSql;
        this.log = log;
        this.datasource = datasource;
        if (!Tools.strIsEmpty(interceptor)) {
            this.sqlInterceptor = (SqlInterceptor) Class.forName(interceptor).newInstance();
        }
        this.repeat = repeat;
    }

    public List<Sql> getSqls() {
        return sqls;
    }

    public void setSqls(List<Sql> sqls) {
        this.sqls = sqls;
    }

    public String getSql(String dialect) {
        if (sqls == null && sqls.size() < 1) {
            return "";
        }
        if (dialect != null)
            for (Sql s : sqls) {
                if (dialect.equals(s.getDialect())) {
                    return s.getSql();
                }
            }
        return sqls.get(0).getSql();

    }

    public void addSql(String sql, String dialect) throws Exception {
        Sql s = new Sql();
        s.setSql(sql);
        s.setDialect(dialect);
        sqls.add(s);
    }

    public String getAction() {
        return action;
    }

    public String getDesc() {
        return desc;
    }

    public String getAuth() {
        return auth;
    }

    public String getMakeSearchSql() {
        return makeSearchSql;
    }

    public boolean isLog() {
        return log;
    }

    public int getDatasource() {
        return datasource;
    }


    public void addActionRef(ActionRef actionRef) {
        actionRefs.add(actionRef);
    }

    public List<ActionRef> getActionRefs() {
        return actionRefs;
    }

    public SqlInterceptor getSqlInterceptor() {
        return sqlInterceptor;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public List<SqlCheck> getChecks() {
        return checks;
    }

    public void setChecks(List<SqlCheck> checks) {
        this.checks = checks;
    }
}
