package com.kayak.dps.expresssion.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class CalculationFormulaDao {

    @Resource
    protected ComnDao comnDao;

    /**
     * 通过表名获取报送业务表中报送单元格信息
     * @param params
     * @param tableName
     * @return
     */
    public List<SqlRow> getDimensionalValue(Map<String,Object> params, String tableName) throws Exception {
        String sqlStr = "select row_id, column_id, data_value, report_date from " + tableName + " where report_date = $S{report_date} order by row_id,column_id ";
        return comnDao.findRows(sqlStr, DataSourceProperty.SRB, params);
    }

    /**
     * 报送表坐标数据入库
     * @param params
     * @param tableName
     * @throws Exception
     */
    public void fieldDataStoreIntoDatabase(Map<String, Object> params, String tableName) throws Exception {
        String sqlStr = "insert into " + tableName + " (row_id, column_id, data_value, report_date) values ($S{row_id}, $S{column_id}, $S{data_value}, $S{report_date})";
        comnDao.update(sqlStr, DataSourceProperty.SRB, params);
    }

}
