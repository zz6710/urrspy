/*
package com.kayak.jimureport.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.cloud.base.exception.KPromptException;
import com.kayak.cloud.base.exception.KSqlException;
import com.kayak.cloud.base.exception.KSystemException;
import com.kayak.core.sql.SqlResult;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository(value = "SuperviseReportDao")
public class SuperviseReportDao {
    */
/**
     * 用于访问数据库，调用SQL配置的数据连接对象
     *//*

    @Resource
    private ComnDao comnDao;

    */
/**
     * 基础数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_GENERAL_DATA(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("GeneralDataEU001", params);
    }

    */
/**
     * 资产负债统计模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_ASSET_LIABILITY(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU003", params);
    }

    */
/**
     * 资管产品募集余额统计模板数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date 日期
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_STATISTICS(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED005", params);
    }

    */
/**
     * 资管产品募集余额统计模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_STATISTICS(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU005", params);
    }

    */
/**
     * 资管产品募集及兑付发生额统计模板数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_STATISTICS2(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED006", params);
    }

    */
/**
     * 资管产品募集及兑付发生额统计模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_STATISTICS2(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU006", params);
    }

    */
/**
     * 资管产品只数情况统计模板数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_STATISTICS3(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED007", params);
    }

    */
/**
     * 资管产品只数情况统计模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_STATISTICS3(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU007", params);
    }

    */
/**
     * 资管产品提前及延期兑付统计模板数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_STATISTICS4(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED008", params);
    }

    */
/**
     * 资管产品提前及延期兑付统计模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_STATISTICS4(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU008", params);
    }

    */
/**
     * 资管产品到期未兑付统计模板数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_STATISTICS5(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED009", params);
    }

    */
/**
     * 资管产品到期未兑付统计模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_STATISTICS5(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU009", params);
    }

    */
/**
     * 资产收益权按基础资产投向分类统计模板删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_STATISTICS6(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED010", params);
    }

    */
/**
     * 资产收益权按基础资产投向分类统计模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_STATISTICS6(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU010", params);
    }

    */
/**
     * 存续产品分合同期限募集余额统计模板删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_LIFE(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED011", params);
    }

    */
/**
     * 存续产品分合同期限募集余额统计模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_LIFE(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU011", params);
    }

    */
/**
     * 新发产品分合同期限募集金额统计模板删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_LIFE2(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED012", params);
    }

    */
/**
     * 新发产品分合同期限募集金额统计模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_LIFE2(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU012", params);
    }

    */
/**
     * 资管产品资产负债剩余期限统计模板删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_LIFE3(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED013", params);
    }

    */
/**
     * 资管产品资产负债剩余期限统计模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_LIFE3(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU013", params);
    }

    */
/**
     * 资管产品境内募集余额分地区统计模板删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_AREA_STATISTICS(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED014", params);
    }

    */
/**
     * 资管产品境内募集余额分地区统计模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_AREA_STATISTICS(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU014", params);
    }

    */
/**
     * 除回购和拆借外贷款分地区统计模板删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_AREA_STATISTICS2(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED015", params);
    }

    */
/**
     * 除回购和拆借外贷款分地区统计模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_AREA_STATISTICS2(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU015", params);
    }

    */
/**
     * 除回购和拆借外贷款分行业、企业规模统计模板删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_TRADE_STATISTICS(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED016", params);
    }

    */
/**
     * 除回购和拆借外贷款分行业、企业规模统计模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_TRADE_STATISTICS(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU016", params);
    }

    */
/**
     * 企业债券分行业、企业规模统计模板删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     * @data_date
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_TRADE_STATISTICS2(String data_date)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("data_date", data_date);
        comnDao.exeUpdate("SuperviseReportED017", params);
    }

    */
/**
     * 企业债券分行业、企业规模统计模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_TRADE_STATISTICS2(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU017", params);
    }

    */
/**
     * 银行月度统计表(结构性存款)导入模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_BANK_MONTH_STRUCTURE(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU018", params);
    }

    */
/**
     * 银行月度统计表(非保本)导入模板（产品情况）新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_BANK_MONTH_PROD(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU019", params);
    }

    */
/**
     * 银行月度统计表(非保本)导入模板（产品情况）新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_BANK_MONTH_ASSET(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU020", params);
    }

    */
/**
     * 历史数据登记-持仓登记导入模版数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_HISTORY_POSITION_INFO(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU021", params);
    }

    */
/**
     * 历史数据登记-个人产品历史数据批量导入模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_HISTORY_INFO(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU022", params);
    }

    */
/**
     * 底层资产信息登记导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_UNDERLYING_ASSET(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED023", params);
    }

    */
/**
     * 底层资产信息登记模版数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_UNDERLYING_ASSET(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU023", params);
    }

    */
/**
     * 从业人员登记导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_EMPLOYEE(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED024", params);
    }

    */
/**
     * 从业人员登记模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_EMPLOYEE(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU024", params);
    }

    */
/**
     * 产品终止登记导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_END(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED025", params);
    }

    */
/**
     * 产品终止登记导入模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_END(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU025", params);
    }

    */
/**
     * 交易登记登记导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_DEAL(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED026", params);
    }

    */
/**
     * 交易登记导入模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_DEAL(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU026", params);
    }

    */
/**
     * 产品存续期登记导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_DURATION(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED027", params);
    }

    */
/**
     * 产品存续期登记导入模版数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_DURATION(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU027", params);
    }

    */
/**
     * 募集期总量登记导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_COLLECT_TOTAL(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED028", params);
    }

    */
/**
     * 募集期总量登记导入模板数据新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_COLLECT_TOTAL(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU028", params);
    }

    */
/**
     * 资产估值表导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_ASSET_VAL(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED033", params);
    }

    */
/**
     * 资产估值表导入模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_ASSET_VAL(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU033", params);
    }

    */
/**
     * 公私募产品申报登记导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_DECLARE(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED032", params);
    }

    */
/**
     * 公私募产品申报登记导入模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_DECLARE(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU032", params);
    }

    */
/**
     * 公私募产品发行登记导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_PROD_PUBLISH(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED031", params);
    }

    */
/**
     * 公私募产品发行登记导入模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_PROD_PUBLISH(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU031", params);
    }

    */
/**
     * 资产要素登记导入模板无效数据删除
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteT8_DATA_ASSET_ELEMENT(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportED030", params);
    }

    */
/**
     * 查询资产要素登记导入模板重复行内资产/负债编码数据
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public SqlResult selectT8_DATA_ASSET_ELEMENT(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        return comnDao.exeQuery("SuperviseReportES030", params);
    }

    */
/**
     * 修改资产要素登记导入模板重复行内资产/负债编码数据
     *
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateT8_DATA_ASSET_ELEMENT(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU030_1", params);
    }

    */
/**
     * 资产要素登记导入模板新增或更新
     *
     * @param params
     * @throws KSqlException
     * @throws KSystemException
     * @throws SQLException
     *//*

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrInsertT8_DATA_ASSET_ELEMENT(Map<String, Object> params)
            throws KSqlException, KSystemException, SQLException, KPromptException {
        comnDao.exeUpdate("SuperviseReportEU030", params);
    }

}
*/
