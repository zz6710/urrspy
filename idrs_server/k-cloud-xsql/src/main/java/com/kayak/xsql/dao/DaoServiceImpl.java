package com.kayak.xsql.dao;

import com.google.common.base.CaseFormat;
import com.kayak.core.dao.DaoService;
import com.kayak.core.dao.Trans;
import com.kayak.core.exception.SqlException;
import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.xsql.XsqlImpl;
import com.kayak.xsql.XsqlMysql;
import com.kayak.xsql.XsqlOracle;
import com.kayak.xsql.XsqlSqlServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("daoService")
@DependsOn("springContextHolder")
public class DaoServiceImpl implements DaoService {

	private static Logger log = LoggerFactory.getLogger(DaoService.class);

	private ThreadLocal<Map<Integer, Integer>> localTrans = new ThreadLocal<>(); // 存储开启事务的数据源

	private Map<String, Integer> sourceMap = new ConcurrentHashMap<>();
	private XsqlImpl xsql;
	private DataSource[] sources;
	private DataSource master;

	private String dbType;

	boolean isInit = false;

	public final static Map<String, String> typeMap = new HashMap<>();

	static {
		typeMap.put("java.lang.String", "S");
		typeMap.put("java.lang.Integer", "I");
		typeMap.put("java.lang.Long", "D");
		typeMap.put("java.lang.Double", "F");
		typeMap.put("java.sql.Date", "T");
		typeMap.put("java.math.BigDecimal", "B");
		typeMap.put("java.sql.Timestamp", "P");
	}

	@PostConstruct
	public void init() {
		synchronized (DaoServiceImpl.class) {
			if (!isInit) {
				try {
					initDataSources();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			isInit = true;
		}
	}

	// 初始化操作
	private void initDataSources() throws Exception {
		String master = SpringContextHolder.getApplicationContext().getEnvironment().getProperty("jdbc.master");
		String nodes = SpringContextHolder.getApplicationContext().getEnvironment().getProperty("jdbc.nodes");
		String driver = SpringContextHolder.getApplicationContext().getEnvironment().getProperty("jdbc.driver");

		prepareXsql(driver);

		String[] names = nodes.split("[ ]*,[ ]*");
		sources = new DataSource[names.length];

		DataSourceLoader loader = new DataSourceLoaderDbcp();

		for (int i = 0; i < 100; i++) {
			String prefix = "jdbc." + i;
			String name = SpringContextHolder.getApplicationContext().getEnvironment().getProperty(prefix + ".name");

			if (Tools.strIsEmpty(name)) {
				continue;
			}

			// 是否是数据节点
			Integer found = null;
			for (int j = 0; j < names.length; j++) {
				if (name.equals(names[j])) {
					found = j;
					break;
				}
			}

			if (name.equals(master) || found != null) {
				DataSource source = loader.load(name, driver, prefix);
				if (name.equals(master)) {
					this.master = source;

				}
				if (found != null) {
					sources[found] = source;
				}
			}
		}

		xsql.setDefaultDataSource(this.master);

	}

	private void prepareXsql(String driver) throws Exception {
		if (driver.indexOf("mysql") >= 0) {
			xsql = new XsqlMysql();
			dbType = "mysql";
		} else if (driver.indexOf("oracle") >= 0) {
			xsql = new XsqlOracle();
			dbType = "oracle";
		} else if (driver.indexOf("sqlserver") >= 0) {
			xsql = new XsqlSqlServer();
			dbType = "sqlserver";
		} else {
			throw new Exception("Xsql未针对该数据库实现: " + driver);
		}
	}

	@Override
	public AutoCloseable selectDataSource(int sharding) {
		DataSource source = sharding < 0 ? master : sources[sharding];
		return xsql.selectDataSource(source, sharding);
	}

    @Override
    public AutoCloseable selectDataSource(String dbName) {
        return selectDataSource(sourceMap.get(dbName));
    }

	@Override
	public DataSource getMaster() {
		return master;
	}

    @Override
    public String getDbType(int sharding) {
        // TODO 暂时只支持同一种数据类型，后面考虑是否增加支持
        return dbType;
    }

	@Override
	public void doTrans(Trans trans, int sharding) throws Exception {
		// TODO 后期改成使用注解方式
		Map<Integer, Integer> transMap = localTrans.get();
		if (transMap == null) {
			transMap = new ConcurrentHashMap<Integer, Integer>();
			localTrans.set(transMap);
		}
		AutoCloseable autoCloseable = null;
		try {
			transMap.put(sharding, sharding);
			autoCloseable = selectDataSource(sharding);
			xsql.getConnection();
			xsql.begin();
			trans.run();
			xsql.commit();
		} catch (Throwable e) {
				xsql.end();
			throw e;
		} finally {
			if (autoCloseable != null) {
				autoCloseable.close();
			}
			transMap.remove(sharding);
		}
	}

    @Override
    public void doTrans(Trans trans, String dbName) throws Exception {
        doTrans(trans, sourceMap.get(dbName));
    }

    @Override
    public void doTrans(Trans trans) throws Exception {
        doTrans(trans, 0);
    }

	/**
	 * 判断该数据源是否已经开启事务
	 * 
	 * @param sharding
	 * @return
	 */
	public boolean isTrans(int sharding) {
		Map<Integer, Integer> transMap = localTrans.get();

        if (transMap != null && transMap.containsKey(sharding)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isTrans(String dbName) {
        return isTrans(sourceMap.get(dbName));
    }

	@Override
	public <T> T query(Class<T> clazz, String sql, Object... params) throws Exception {
		return xsql.query(clazz, sql, params);
	}

	@Override
	public <T> List<T> list(Class<T> clazz, String sql, Object... params) throws Exception {
		return xsql.list(clazz, sql, params);
	}

	@Override
	public <T> List<T> list(String sql, SqlParam<T> sqlParam) throws Exception {
		// 判断是否自动追加参数
		if (sqlParam.isMakeSql()) {
			sql = makeSql(sql, sqlParam.getParams(), sqlParam.getModelClass());
		}

		if (sqlParam.isSqlNoLog()) {
			return xsql.listNoSqlLog(sqlParam.getModelClass(), sql, sqlParam.getModel());
		} else {
			return xsql.list(sqlParam.getModelClass(), sql, sqlParam.getModel());
		}

	}

	@Override
	public <T> SqlResult<T> page(String sql, SqlParam<T> sqlParam) throws Exception {

		// 判断是否自动追加参数
		if (sqlParam.isMakeSql()) {
			sql = makeSql(sql, sqlParam.getParams(), sqlParam.getModelClass());
		}

		List<T> rows = xsql.page(sqlParam.getModelClass(), sqlParam.getStart(), sqlParam.getLimit(), sql,
				sqlParam.getModel());
		// TODO 分页待实现
		if (sqlParam.isPage()) {
			int count = queryCount(sql, sqlParam.getModel());
			return SqlResult.build(rows, count);
		} else {
			return SqlResult.build(rows);
		}
	}

	@Override
	public UpdateResult update(String sql, Object... params) throws Exception {
		return xsql.update(sql, params);
	}
	@Override
	public UpdateResult updateNoLog(String sql, Object... params) throws Exception {
		return xsql.updateNoLog(sql, params);
	}

	@Override
	public <T> UpdateResult update(String sql, SqlParam<T> sqlParam) throws Exception {
		// 判断是否自动追加参数
		if (sqlParam.isMakeSql()) {
			sql = makeSql(sql, sqlParam.getParams(), sqlParam.getModelClass());
		}
		return xsql.update(sql, sqlParam.getModel());
	}

	public int queryCount(String sql, Object param) throws Exception {
		String countSql = " SELECT COUNT(1) FROM ( " + sql + " ) t ";
		return xsql.query(Integer.class, countSql, param);
	}

	private String makeSql(String sql, Map<String, Object> params, Class<?> modelClass) throws SqlException {
		// 组装查询SQL
		StringBuilder sqlBuilder = new StringBuilder();

		int index = sql.length();

        // 获取最后一个右括号下标
        int rightBracketsIndex = getLastPatternIndex("[)]", sql);

        String preSql = sql.substring(0, rightBracketsIndex);
        String suffixSql = sql.substring(rightBracketsIndex, index);

        index = suffixSql.length();
        index = getPatternIndex("[Oo][Rr][Dd][Ee][Rr]\\s+[Bb][Yy]", suffixSql, index);
        index = getPatternIndex("[Gg][Rr][Oo][Uu][Pp]\\s+[Bb][Yy]", suffixSql, index);

        String startSql = suffixSql.substring(0, index);
        StringBuilder tailSql = new StringBuilder(suffixSql.substring(index));

        sqlBuilder.append(preSql);
        sqlBuilder.append(startSql);

		Set<String> keys = params.keySet();
		if (keys != null && keys.size() > 0) {

            List<Field> fields = new ArrayList<>();
            Tools.getFields(fields, modelClass);

			Map<String, Field> fieldTypeMap = new HashMap<>();

			for (Field field : fields) {
				if (field.isAnnotationPresent(GraphQLField.class)) {
					fieldTypeMap.put(field.getName(), field);
				}
			}

			boolean hasCondition = false;

			if (sql.contains("WHERE") || sql.contains("where")) {
				hasCondition = true;
			}

			for (String key : keys) {
				Field field = fieldTypeMap.get(key);
				if (field == null) {
					continue;
				}

				String fieldType = field.getType().getName();

				GraphQLField graphQLField = field.getAnnotation(GraphQLField.class);

				String operate = graphQLField.operate();
				if (Tools.strIsEmpty(operate)) {
					throw new SqlException("字段：" + key + " operate值不能为空");
				}

				String searchSql = graphQLField.sql();

				String dbfield = graphQLField.field();

				boolean isUpdate = graphQLField.update();

				if (Tools.strIsEmpty(dbfield) || !isUpdate) {// 如果未指定表字段映射，则去属性名作为映射
					continue;
				}

				if (Tools.strIsEmpty(searchSql)) {
					searchSql = dbfield + "=$" + typeMap.get(fieldType) + "{" + key + "} ";
				}

				if (hasCondition) {
					sqlBuilder.append(" ");
					sqlBuilder.append(operate);
					sqlBuilder.append(" ");
					hasCondition = true;
				} else {
					sqlBuilder.append(" WHERE ");
					hasCondition = true;
				}

				sqlBuilder.append(searchSql);
			}
		}

		sqlBuilder.append(" ");
		sqlBuilder.append(tailSql);

		// 拼接排序sql
		StringBuilder sortSql = assembleSortSql(params);
		if (sortSql.length() > 0) {
			sqlBuilder.insert(0, "SELECT * FROM (");
			sqlBuilder.append(") sortTable ");
			sqlBuilder.append(sortSql);
		}
		return sqlBuilder.toString();
	}

	private StringBuilder assembleSortSql(Map<String, Object> params) {
		StringBuilder sortSql = new StringBuilder();
		if (!params.containsKey("sort") || !params.containsKey("dir")) {
			return sortSql;
		}

		String sorts = (String) params.get("sort");
		String dirs = (String) params.get("dir");

		if (Tools.isBlank(sorts) || Tools.isBlank(dirs)) {
			return sortSql;
		}

		String[] sortArr = sorts.split(",");
		String[] dirArr = dirs.split(",");

		if (sortArr.length == 0 || dirArr.length == 0) {
			return sortSql;
		}

		if (sortArr.length != dirArr.length) {
			log.error("排序参数参数错误,sort[{}],dir[{}].", sorts, dirs);
			throw new RuntimeException("参数错误");
		}

		sortSql.append(" ORDER BY ");
		for (int i = 0; i < sortArr.length; i++) {
			String field = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortArr[i]);
			sortSql.append(field + " " + dirArr[i] + ",");
		}
		sortSql.deleteCharAt(sortSql.length() - 1);
		return sortSql;
	}

    private int getPatternIndex(String patternStr, String sql, int index) {
        Pattern pattern = Pattern.compile(patternStr);

        Matcher m = pattern.matcher(sql);
        if (m.find()) {
            int _index = m.start();
            if (index > _index) {
                index = _index;
            }
        }
        return index;
    }

    private int getLastPatternIndex(String patternStr, String sql) {
        Matcher matcher = Pattern.compile(patternStr).matcher(sql);
        int index = 0;
        while (matcher.find()) {
            index = matcher.start();
        }
        return index;
    }

	@Override
	public Connection getConnection() throws SQLException {
		return xsql.getConnection();
	}

	@Override
	public void begin() throws Exception {
		xsql.begin();

	}

	@Override
	public boolean commit() throws Exception {
		return xsql.commit();
	}

	@Override
	public boolean end() {
		return xsql.end();
	}

	@Override
	public String getConnectionKeepDetail() {
		if (XsqlImpl.dbKeepMap.isEmpty()) {
			return "当前无使用中的连接";
		}

		Set<Connection> keys = XsqlImpl.dbKeepMap.keySet();
		StringBuilder stackTraceStringBuilder = new StringBuilder();
		for (Connection key : keys) {
			String detail = XsqlImpl.dbKeepMap.get(key);

			if (!Tools.strIsEmpty(detail)) {
				stackTraceStringBuilder.append("---------------------------占用连接栈信息---------------------------");
				stackTraceStringBuilder.append(detail);
				stackTraceStringBuilder.append("\n");
			}

		}
		return stackTraceStringBuilder.toString();
	}
	@Override
	public String planParameterSql(String sql, Object params) throws Exception {
		return xsql.planParameterHandler(sql,params).setParameters(params).toString();
	}

    @Override
    public Integer getSharding(String dbName) {
        return sourceMap.get(dbName);
    }

	@Override
	public ResultSetMetaData getMetaData(String sql, Object... params) throws Exception {
		return xsql.getMetaData(sql, params);
	}

}
