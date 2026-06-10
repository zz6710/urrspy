package com.kayak.clear.dataSync.dao;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.ExeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ZgSonProductInfoDao extends ComnDao {

    protected static final Logger log = LoggerFactory.getLogger(ZgSonProductInfoDao.class);

    /**
     * Dps服务数据库分布式锁更新方法
     * @param params
     * @return
     * @throws Exception
     */
    public int upTaskStatus(Map<String, Object> params) throws Exception {
        daoService.selectDataSource(0);
        String sqlstr = "update sys_param set paravalue = $S{paravalue} where paraid = $S{paraid} and paravalue = $S{oldParavalue} ";
        return daoService.update(sqlstr, params).getEffect();
    }

    /**
     * 直连资管库查询机构信息数据
     * @param statement
     * @param querySql
     * @return ResultSet
     * @throws Exception
     */
    public ResultSet getZgSonProductInfo(Statement statement, String querySql) throws Exception {
        log.info("执行查询资管数据库:" + querySql);
        return statement.executeQuery(querySql);
    }

    /**
     * 子产品信息到期日更新
     * @param resultSet
     * @return List<SqlRow>
     * @throws Exception
     */
    public List<SqlRow> insTempOdsProdBaseInfo(ResultSet resultSet) throws Exception {
        PreparedStatement ps = null;
        List<SqlRow> sqlRows = new ArrayList<>();

        // 选择主数据源
        try (AutoCloseable ac = daoService.selectDataSource(0)) {
            // 1、先删除该表数据
            daoService.update("truncate temp_ods_prod_base_info");

            // 2、批量插入产品临时表
            Connection connection = daoService.getConnection();
            ps = connection.prepareStatement("insert into temp_ods_prod_base_info (prod_code, run_stat, real_end_date) values (?, ?, ?)");

            while(resultSet.next()) {
                ps.setString(1, resultSet.getString("prod_code"));
                ps.setString(2, resultSet.getString("run_stat"));
                ps.setString(3, resultSet.getString("real_end_date"));
                ps.addBatch();
            }

            ps.executeBatch();

            // 3、找到那些变更的产品列表
            sqlRows = super.findRows(ExeQuery.queryExeId("ZGSONEQ002"));

            // 4、更新子产品产品状态和到期日
            if (CollectionUtil.isNotEmpty(sqlRows)) {
                daoService.update(ExeQuery.queryExeId("ZGSONEQ003"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

        return sqlRows;
    }

    /**
     * 子产品信息登记和删除重跑
     * @param params
     * @throws Exception
     */
    public void runSonShareDelInfo(Map<String, Object> params) throws Exception {
        try (AutoCloseable ac = daoService.selectDataSource(0)) {
            daoService.doTrans(() -> {
                daoService.update(ExeQuery.queryExeId("ZGSONEQ004"), params);
                daoService.update(ExeQuery.queryExeId("ZGSONEQ005"), params);
            });
        } catch (Exception e) {
            log.error("子产品信息登记和删除重跑失败!", e);
            throw e;
        }
    }

}
