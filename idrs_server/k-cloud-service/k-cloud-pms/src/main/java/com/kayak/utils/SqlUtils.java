package com.kayak.utils;

import com.google.common.base.CaseFormat;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.dao.DaoService;
import com.kayak.core.exception.SqlException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.graphql.annotation.GraphQLField;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/1/7 14:10
 */
public class SqlUtils {
    private static Logger log = LoggerFactory.getLogger(SqlUtils.class);
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

    /**
     *对sql进行分页,可以自动拼接参数
     * @param sql  原始sql
     * @param params  sql参数
     * @param comnDao  数据库操作基础抽象类
     * @return  返回map
     * @throws Exception
     */
    public static SqlResult<Map<String, Object>> sqlPackage(String sql, int sharding, SqlParam params, ComnDao comnDao) throws Exception {
        // 判断是否自动追加参数
        if (params.isMakeSql()) {
            sql = makeSql(sql, params.getParams(), params.getModelClass());
        }
        if(params.getLimit() == 0) params.setLimit(5);
        //获取总记录数量
        String countSql = " SELECT COUNT(1) count FROM ( " + sql + " ) t ";
        List<SqlRow> count = comnDao.findRows(countSql, sharding, params.getModel());
        //获取页面展示的内容
        String paged = "select * from (" + sql + ") xsql_t limit " + params.getStart() + ", " + params.getLimit();
        log.info("分页执行SQL: {}", "select * from (" + sql + ") xsql_t limit " + params.getStart() + ", " + params.getLimit());
        List<SqlRow> rows = comnDao.findRows(paged, sharding, params.getModel());
        return CamelCaseMapUtils.CamelCaseSqlRow(rows, count.get(0).getInteger("count"));
    }

    /**
     *对sql进行分页,不会对自动拼接参数到sql
     * @param sql  原始sql
     * @param params  sql参数,类型为map,必须要有分页参数
     * @param comnDao  数据库操作基础抽象类
     * @return  返回map
     * @throws Exception
     */
    public static SqlResult<Map<String, Object>> sqlPackage(String sql, int sharding, Map<String, Object> params, ComnDao comnDao) throws Exception {
        //获取总记录数量
        String countSql = " SELECT COUNT(1) count FROM ( " + sql + " ) t ";
        List<SqlRow> count = comnDao.findRows(countSql, sharding, params);
        //获取分页参数
        int start ;
        int limit ;
        try {
            start = Integer.parseInt(params.get("start").toString());
            limit = Integer.parseInt(params.get("limit").toString());
        }catch(Exception ex) {
            start = 0;
            limit = 5;
        }


        //获取页面展示的内容
        String paged = "select * from (" + sql + ") xsql_t limit " + start + ", " + limit;
        log.info("分页执行SQL: {}", "select * from (" + sql + ") xsql_t limit " + start + ", " + limit);
        List<SqlRow> rows = comnDao.findRows(paged, sharding, params);
        return CamelCaseMapUtils.CamelCaseSqlRow(rows,count.get(0).getInteger("count"));
    }

    private static String makeSql(String sql, Map<String, Object> params, Class<?> modelClass) throws SqlException {
        // 组装查询SQL
        StringBuilder sqlBuilder = new StringBuilder();

        int index = sql.length();

        index = getSortIndex("[Oo][Rr][Dd][Ee][Rr]\\s+[Bb][Yy]", sql, index);
        index = getSortIndex("[Gg][Rr][Oo][Uu][Pp]\\s+[Bb][Yy]", sql, index);

        String startSql = sql.substring(0, index);
        StringBuilder tailSql = new StringBuilder(sql.substring(index));

        sqlBuilder.append(startSql);

        Set<String> keys = params.keySet();
        if (keys != null && keys.size() > 0) {

            Field[] fields = modelClass.getDeclaredFields();

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

    private static int getSortIndex(String patternStr, String sql, int index) {
        Pattern pattern = Pattern.compile(patternStr);

        Matcher m = pattern.matcher(sql);
        if (m.find()) {
            int _index = m.start();
            if (index != 0 && index > _index) {
                index = _index;
            }
        }
        return index;
    }

    private static StringBuilder assembleSortSql(Map<String, Object> params) {
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
}
