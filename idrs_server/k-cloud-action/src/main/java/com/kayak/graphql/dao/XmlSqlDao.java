package com.kayak.graphql.dao;

import com.kayak.base.dao.BaseDao;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.XmlSqlOperate;
import com.kayak.base.dao.XmlSqlSupportDao;
import com.kayak.base.dao.sql.*;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.*;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.Tools;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.graphql.autoconfigure.GraphQLAnnotationImpl;
import com.kayak.graphql.model.FetcherData;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class XmlSqlDao extends BaseDao implements XmlSqlOperate {

    public <T> SqlResult<T> query(SqlParam<T> param, String action) throws Exception {
        String modelName = param.getModelClass().getSimpleName();

        if (!SqlXmlServer.sqlCache.containsKey(modelName)) {
            throw new PromptException("不存在实体XML配置");
        }

        if (!SqlXmlServer.sqlCache.get(modelName).containsKey(action)) {
            throw new PromptException("实体XML不存在action配置");
        }

        SqlConfig sqlConfig = SqlXmlServer.sqlCache.get(modelName).get(action);

        return query(param, sqlConfig);
    }

    public <T> SqlResult<T> query(SqlParam<T> param, SqlConfig sqlConfig) throws Exception {
        if ("true".equals(sqlConfig.getMakeSearchSql())) {
            param.setMakeSql(true);
        } else {
            param.setMakeSql(false);
        }

        SqlInterceptor sqlInterceptor = sqlConfig.getSqlInterceptor();

        Sql sql = new Sql();

        if (sqlInterceptor != null) {
            sqlInterceptor.beforeExecute(param);
        }

        sql.defaultSql(sqlConfig.getSql(null)).db2Sql(sqlConfig.getSql("db2")).mysqlSql(sqlConfig.getSql("mysql")).oracleSql(sqlConfig.getSql("oracle"));


        checkSql(param, sqlConfig);

        int sharding = sqlConfig.getDatasource();
        if (!Tools.strIsEmpty(sqlConfig.getDatasourceName())) {
            sharding = daoService.getSharding(sqlConfig.getDatasourceName());
        }

        SqlResult<T> sqlResult = super.findRows(sql, sharding, param);

        if (sqlInterceptor != null) {
            sqlInterceptor.afterExecute(param, sqlResult, null);
        }

        return sqlResult;
    }

    public SqlResult<Map> query(Map<String, Object> param, SqlConfig sqlConfig) throws Exception {
        SqlParam<Map> sqlParam = new FetcherData<>(param);
        return query(sqlParam, sqlConfig);
    }

    public <T> String update(SqlParam<T> param, String action) throws Exception {

        String modelName = param.getModelClass().getSimpleName();

        if (!SqlXmlServer.sqlCache.containsKey(modelName)) {
            throw new PromptException("不存在实体XML配置");
        }

        if (!SqlXmlServer.sqlCache.get(modelName).containsKey(action)) {
            throw new PromptException("实体XML不存在action配置");
        }

        SqlConfig sqlConfig = SqlXmlServer.sqlCache.get(modelName).get(action);
        return update(param, sqlConfig);
    }

    public String update(Map<String, Object> param, SqlConfig sqlConfig) throws Exception {
        SqlParam<Map> sqlParam = new FetcherData<>(param);
        return update(sqlParam, sqlConfig);
    }

    public <T> String update(SqlParam<T> param, SqlConfig sqlConfig) throws Exception {

        Sql sql = new Sql();

        sql.defaultSql(sqlConfig.getSql(null)).db2Sql(sqlConfig.getSql("db2")).mysqlSql(sqlConfig.getSql("mysql")).oracleSql(sqlConfig.getSql("oracle"));

        SqlInterceptor sqlInterceptor = sqlConfig.getSqlInterceptor();

        if (sqlInterceptor != null) {
            sqlInterceptor.beforeExecute(param);
        }

        int sharding = sqlConfig.getDatasource();
        if (!Tools.strIsEmpty(sqlConfig.getDatasourceName())) {
            sharding = daoService.getSharding(sqlConfig.getDatasourceName());
        }

        UpdateResult updateResult = null;
        if (checkSql(param, sqlConfig)) {

            String repeat = sqlConfig.getRepeat();
            updateResult = null;
            if (!Tools.strIsEmpty(repeat)) {
                String[] repeatKeys = repeat.split(",");

                Map<String, Object> params = param.getParamsDirect();

                //校验数组长度是否一至
                int len = 0;
                Map<String, String[]> repeatVaueMap = new HashMap<>();
                for (String repeatKey : repeatKeys) {
                    String[] repeatVaues = Tools.obj2Str(params.get(repeatKey)).split(",");

                    if (len == 0) {
                        len = repeatVaues.length;
                    } else {
                        if (len != repeatVaues.length) {
                            throw new PromptException("repeat属性长度不一致，长度：" + len + "，属性值:" + Tools.obj2Str(params.get(repeatKey)));
                        }
                    }

                    repeatVaueMap.put(repeatKey, repeatVaues);
                }

                for (int i = 0; i < len; i++) {
                    for (String repeatKey : repeatKeys) {
                        params.put(repeatKey, repeatVaueMap.get(repeatKey)[i]);
                    }
                    FetcherData fetcherData = new FetcherData(params, param.getModelClass());
                    updateResult = super.update(sql, sharding, fetcherData.getModel());
                }
            } else {
                updateResult = super.update(sql, sharding, param.getModel());
            }


            if (sqlInterceptor != null) {
                sqlInterceptor.afterExecute(param, null, updateResult);
            }
        }

        //执行引用的action
        List<ActionRef> actionRefs = sqlConfig.getActionRefs();

        if (actionRefs != null && actionRefs.size() > 0) {
            for (ActionRef actionRef : actionRefs) {
                String refModel = actionRef.getModel();
                String refAction = actionRef.getAction();
                Class<?> modelClass = GraphQLAnnotationImpl.modelClassMap.get(refModel);

                // 获取操作对象实例
                GraphQLModel graphQLModel = modelClass.getAnnotation(GraphQLModel.class);

                FetcherData fetcherData = new FetcherData(param.getParamsDirect(), modelClass);

                String fetcher = graphQLModel.fetcher();

                String server;
                if (!fetcher.equals("xml")) {
                    Object fetcherBean = SysBeans.getBean(fetcher);

                    if (fetcherBean == null) {
                        throw new PromptException("获取操作对象失败，无fetcher配置对应实例，fetcher值：" + fetcher);
                    }

                    Class<?> serviceClass = fetcherBean.getClass();

                    Method method = fetcherBean.getClass().getMethod(refAction, SqlParam.class);

                    method.invoke(fetcherBean, fetcherData);
                } else {
                    update(fetcherData, refAction);
                }
            }
        }

        return RequestSupport.updateReturnJson(true, sqlConfig.getDesc() + "成功", !ObjectUtils.isEmpty(updateResult) ? Tools.json2map(new JSONObject(updateResult)) : null).toString();

    }

    private <T> boolean checkSql(SqlParam<T> param, SqlConfig sqlConfig) throws Exception {
        List<SqlCheck> checks = sqlConfig.getChecks();
        if (checks == null) {
            return true;
        }

        boolean checkOK = true;// SQL校验是否成功，如果此变量值为false，则跳过这个SQL更新

        for (SqlCheck check : checks) {// 遍历单个SQL更新的校验配置信息
            String checkname = check.getCheckname();// 校验名称
            String checkString = check.getCheckstring();// 校验值
            String compareVal = check.getCompareval();// 比较值
            String compareSign = check.getComparesign();// 比较条件
            String errText = check.getErrtext();// 出错提示
            Boolean exitAll = check.getExitall();// 是否中止同步SQL执行
            String dbtype = daoService.getDbType(check.getDatasource());
            String checkSql = check.getChecksql(dbtype);// 查询SQL

            int sharding = sqlConfig.getDatasource();
            if (!Tools.strIsEmpty(check.getDatasourceName())) {
                sharding = daoService.getSharding(check.getDatasourceName());
            }

            Object checkVal = null;// 校验值

            if (!Tools.strIsEmpty(checkSql)) {
                // 执行校验检查SQL
                List<SqlRow> sqlRows = super.findRows(checkSql, sharding, param.getParams());

                if (sqlRows.size() > 0) {// SQL执行成功，进行校验检查
                    SqlRow sqlRow = sqlRows.get(0);
                    // 只取查询结果的第一行第一列的值
                    checkVal = sqlRow.get(sqlRow.keySet().iterator().next());
                }
            } else if (!Tools.strIsEmpty(checkString)) {
                checkVal = daoService.planParameterSql(checkString, param.getParamsDirect());
            } else {
                throw new PromptException("通用校验配置中有没有配置checksql或checkstring的配置checkname=" + checkname);
            }

            compareVal = daoService.planParameterSql(compareVal, param.getParamsDirect());

            if (compare(checkVal, compareVal, compareSign)) {// 校验检查失败条件成立
                errText = daoService.planParameterSql(errText, param.getParamsDirect());
                if (exitAll) {// 中止同步SQL执行，则抛出错误信息
                    throw new PromptException(errText);
                } else {// 不中止同步SQL执行，只跳过当前SQL更新的执行
                    log.info("通用SQL更新的SQL校验不通过，将跳过此更新的执行：checkname=" + checkname + ", errText:" + errText);
                    checkOK = false;
                }
            }

        }

        return checkOK;
    }

    /**
     * 比较从查询取到的对象sqlObj与compareVal对象，规则是：<br />
     * 1. 以condition指定的比较方式<br />
     * 2. 如果sqlObj是String类型，则两个对象都以字符串形式比较，否则把两者都转换成数字来比较
     *
     * @param sqlObj
     * @param compareVal
     * @param sign       比较方式： eql 等于, uneql 不等于, gt 大于, gteql 大于等于, lt 小于, lteql 小于等于
     * @return
     */
    private boolean compare(Object sqlObj, String compareVal, String sign) {
        if (sqlObj == null) return false;

        int res;
        if (sqlObj.getClass().equals(String.class)) {// 字符串比较
            res = ((String) sqlObj).compareTo(compareVal);
        } else {// 数值比较
            res = Tools.str2BigDecimal(String.valueOf(sqlObj)).compareTo(Tools.str2BigDecimal(compareVal));
        }
        sign = sign.trim();
        boolean ret;
        if ("uneql".equals(sign))// 判断不等于
            ret = res != 0;
        else
            ret = sign.indexOf("eql") > -1 && res == 0 || sign.indexOf("gt") > -1 && res > 0 || sign.indexOf("lt") > -1 && res < 0;
        log.info("##### compare : " + sqlObj + " " + sign + " " + compareVal + " " + ret);
        return ret;
    }


}
