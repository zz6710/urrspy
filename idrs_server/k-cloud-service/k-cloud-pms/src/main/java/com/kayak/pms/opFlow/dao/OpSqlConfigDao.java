package com.kayak.pms.opFlow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.Sql;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.opFlow.constant.DbType;
import com.kayak.pms.opFlow.model.OpSqlCheckConfig;
import com.kayak.pms.opFlow.model.OpSqlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OpSqlConfigDao extends ComnDao {
    @Autowired
    OpSqlCheckConfigDao opSqlCheckConfigDao;

    public void save(SqlParam<OpSqlConfig> params) throws Exception {
        super.doTrans(() -> {
            // 先删除，sql配置表、sql校验表
            super.update("DELETE FROM op_sql_check_config WHERE sql_id in (SELECT sql_id FROM op_sql_config WHERE form_id=$S{formId})", params.getModel());
            super.update("DELETE FROM op_sql_config WHERE form_id=$S{formId}", params.getModel());

            // 再插入
            String sql = "INSERT INTO op_sql_config(form_id, action_type, sql_id, sql_name, sql_statement, datasource, db_type, log_print, order_no)" +
                    " VALUES ($S{formId}, $S{actionType}, $S{sqlId}, $S{sqlName}, $S{sqlStatement}, $S{datasource}, $S{dbType}, $S{logPrint}, $I{orderNo})";
            for (OpSqlConfig opSqlConfig : params.getModel().getList()) {
                // 插入sql配置表
                super.update(sql, opSqlConfig);
                if (opSqlConfig.getCheckData() != null) {
                    for (OpSqlCheckConfig checkData : opSqlConfig.getCheckData()) {
                        super.update("INSERT INTO op_sql_check_config(sql_id, check_id, check_type,check_sign, check_field, check_sql, check_target, check_msg, order_no)" +
                                " VALUES ($S{sqlId}, $S{checkId}, $S{checkType}, $S{checkSign}, $S{checkField}, $S{checkSql}, $S{checkTarget}, $S{checkMsg}, $I{orderNo})", checkData);
                    }
                }
            }
        });
    }

    public SqlResult<OpSqlConfig> find(SqlParam<OpSqlConfig> params) throws Exception {
        // 查询sql配置
        String sql = "SELECT form_id, action_type, sql_id, sql_name, sql_statement, datasource, db_type, log_print, order_no FROM op_sql_config WHERE form_id=$S{formId} order by order_no";
        SqlResult<OpSqlConfig> result = super.findRows(sql, params);
        // 取出 sql id 拼接
        List<String> sqlIdList = result.getRows().stream().map(OpSqlConfig::getSqlId).collect(Collectors.toList());
        String sqlIdListStr = String.join("','", sqlIdList);
        // 查询sql校验，where sql_id in 条件
        sql = "SELECT sql_id, check_id, check_type, check_sign, check_field, check_sql, check_target, check_msg FROM op_sql_check_config WHERE sql_id in('" + sqlIdListStr + "') order by sql_id,order_no";
        SqlResult<OpSqlCheckConfig> rows = super.findRows(sql, new FetcherData<>(new HashMap<>(), OpSqlCheckConfig.class));
        // 按sql id 分组，塞入result
        Map<String, List<OpSqlCheckConfig>> group = rows.getRows().stream().collect(Collectors.groupingBy(OpSqlCheckConfig::getSqlId));
        result.getRows().forEach(item -> {
            item.setCheckData(group.get(item.getSqlId()));
        });
        return result;
    }

    public List<OpSqlConfig> findByBusiId(String busiId, String actionType) throws Exception {
        // 查询sql配置
        String sql = "SELECT form_id, action_type, sql_id, sql_name, sql_statement, datasource, db_type, log_print, order_no FROM op_sql_config" +
                " WHERE action_type='" + actionType + "'" +
                " AND form_id in (SELECT form_id from op_busi_form WHERE busi_id='" + busiId + "')" +
                " ORDER BY order_no";
        SqlResult<OpSqlConfig> result = super.findRows(sql, 0,new FetcherData<>(new HashMap<>(), OpSqlConfig.class));
        // 取出 sql id 拼接
        List<String> sqlIdList = result.getRows().stream().map(OpSqlConfig::getSqlId).collect(Collectors.toList());
        String sqlIdListStr = String.join("','", sqlIdList);
        // 查询sql校验，where sql_id in 条件
        sql = "SELECT sql_id, check_id, check_type, check_sign, check_field, check_sql, check_target, check_msg FROM op_sql_check_config WHERE sql_id in('" + sqlIdListStr + "') order by sql_id,order_no";
        SqlResult<OpSqlCheckConfig> rows = super.findRows(sql, new FetcherData<>(new HashMap<>(), OpSqlCheckConfig.class));
        // 按sql id 分组，塞入result
        Map<String, List<OpSqlCheckConfig>> group = rows.getRows().stream().collect(Collectors.groupingBy(OpSqlCheckConfig::getSqlId));
        List<OpSqlConfig> opSqlConfigList = result.getRows();
        opSqlConfigList.forEach(item -> {
            item.setCheckData(group.get(item.getSqlId()));
        });
        return opSqlConfigList;
    }

    public void execSql(OpSqlConfig opSqlConfig, Map<String, Object> map) throws Exception {
        // 校验失败，以异常的形式抛出去
        opSqlCheckConfigDao.checkSql(opSqlConfig, map);
        Sql sql = Sql.build();
        switch (opSqlConfig.getDbType()) {
            case DbType.ORACLE:
                sql.oracleSql(opSqlConfig.getSqlStatement());
                break;
            case DbType.DB2:
                sql.db2Sql(opSqlConfig.getSqlStatement());
                break;
            default:
                // 默认mysql
                sql.mysqlSql(opSqlConfig.getSqlStatement());
                break;
        }
        super.update(sql, Integer.parseInt(opSqlConfig.getDatasource()), map);
    }
}
