package com.kayak.clear.dataSync.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.ExeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class TtrdInstitutionDao extends ComnDao {

    protected static final Logger log = LoggerFactory.getLogger(TtrdInstitutionDao.class);

    /**
     * 根据接口编码查询需要同步的接口字段
     * @param port_code
     * @return
     * @throws Exception
     */
    public List<String> getPortManageColumn (String port_code) throws Exception {
        List<String> columnList = new ArrayList<>();
        String sql = "select b.field_code, b.field_seq " +
                "       from base_port_field_manage b " +
                "      where port_code = '" + port_code + "' " +
                "      order by b.field_seq ";
        List<SqlRow> columnRows = super.findRows(sql, DataSourceProperty.PUB);
        for (SqlRow row: columnRows) {
            columnList.add(String.valueOf(row.get("field_code")));
        }
        return columnList;
    }

    /**
     * 清除接口表数据
     * @param portTable
     * @throws Exception
     */
    public void clearTableData(String portTable) {
        try {
            super.getConnection().setAutoCommit(true);
            super.daoService.update("truncate table " + portTable);
        } catch (Exception e) {
            log.error("清除资管接口表数据异常,接口表名:" + portTable);
        }
    }

    /**
     * 将接口数据入库
     * @throws Exception
     */
    public void putPortManageColumn (String port_table, ResultSet resultSet, List<String> fieldList, int max_length) throws Exception {
        long startTime = System.currentTimeMillis();
        AtomicInteger index = new AtomicInteger();//数据量
        String[] str = generatePjSql(fieldList);

        try {
            daoService.doTrans(() -> {
                while(resultSet.next()) {
                    String preSql = "replace into " + port_table + "(" + str[0] + ") values (";
                    index.getAndIncrement();

                    for (int i=0; i < fieldList.size(); i++) {
                        if (resultSet.getString(fieldList.get(i)) == null) {
                            preSql += resultSet.getString(fieldList.get(i));
                        } else {
                            preSql += ("'" + resultSet.getString(fieldList.get(i)) + "'");
                        }

                        if (i < fieldList.size()-1){
                            preSql += ",";
                        }

                        if (i == fieldList.size()-1){
                            preSql += ")";
                        }
                    }
                    super.update(preSql);
                }
            });
            log.info(" ##### 批量入库{}耗时: {} ms", index, System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("同步资管接口数据 " + port_table + "异常!", e);
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 拼接接口查询字段
     * @return
     * @throws Exception
     */
    private String[] generatePjSql (List<String> fieldList) {
        String[] str = new String[2];
        String pjSql = "";
        String valSql = "";

        for (String column_name : fieldList) {
            pjSql += (column_name + ",");
            valSql += "?,";
        }

        str[0] = pjSql.substring(0, pjSql.length() - 1);
        str[1] = valSql.substring(0, valSql.length() - 1);

        return str;
    }


    /**
     * 执行交易信息登记更新数据
     * @param sqlIdList
     */
    public void executeUpdSql(List<String> sqlIdList, Map<String, Object> params) throws Exception {
        for (String exeId : sqlIdList) {
            super.update(ExeQuery.queryExeId(exeId), DataSourceProperty.PUB, params);
        }
    }

    /**
     * 根据exeid查询结果集组装成条件
     * @param exeId
     * @return
     * @throws Exception
     */
    public String getQueryCondition(String exeId, Map<String, Object> params) throws Exception {
        String cond = "";
        List<SqlRow> sqlRows = super.findRows(ExeQuery.queryExeId(exeId), DataSourceProperty.PUB, params);
        for (SqlRow row : sqlRows) {
            cond += ("'" + row.getString("party_id") + "',");
        }

        return cond.substring(0, cond.length()>0?(cond.length()-1):0);
    }

    /**
     * 直连资管库查询机构信息数据
     * @param statement
     * @param querySql
     * @return
     * @throws Exception
     */
    public ResultSet getZgOrgInfo(Statement statement, String querySql, Map<String, Object> params) throws Exception {
        querySql += (" (" + params.get("condition") + ") ");
        log.info("执行查询资管数据库:" + querySql);
        return statement.executeQuery(querySql);
    }

    /**
     * 执行交易信息登记更新数据
     * @param sqlIdList
     */
    public void executeUpdSqls(List<String> sqlIdList, Map<String, Object> params) throws Exception {
        for (String exeId : sqlIdList) {
            super.update(ExeQuery.queryExeId(exeId), DataSourceProperty.PUB, params);
        }
    }


}
