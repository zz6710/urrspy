package com.kayak.dps.expresssion.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.dps.check.constants.ErrorCollectionConstants;
import com.kayak.dps.check.util.PrimaryDataCheckUtil;
import com.kayak.dps.expresssion.dao.CalculationFormulaDao;
import com.kayak.dps.expresssion.exception.CalculationFormulaQueryException;
import com.kayak.dps.expresssion.model.FieldData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalculationFormulaService {

    @Resource
    protected CalculationFormulaDao calculationFormulaDao;

    /**
     * 通过表名查询报表的最大列数和行数，组装成FieldData对象，再包装成LIST返回
     * @param params 日期参数report_date:报送日期
     * @param tableName
     * @return
     */
    public List<FieldData> TwoDimensionalToCoordinate(Map<String, Object> params, String tableName){
        List<FieldData> fieldList = null;
        try{
            List<SqlRow> sqlRowList = calculationFormulaDao.getDimensionalValue(params, tableName);
            for(SqlRow row : sqlRowList){
                fieldList.add(new FieldData(row.getString("row_id"), row.getString("column_id"), row.getString("data_value")));
            }
        }catch (Exception e){
            throw new CalculationFormulaQueryException("计算公式查询报表最大行列数语句执行异常!");
        }
        return fieldList;
    }

    /**
     * 坐标数据入库方法，将FiedData的值存放在tableName变量值的数据库表中
     * @param fieldList
     * @param tableName
     * @param report_date
     */
    public void CoordinateStore(List<FieldData> fieldList, String tableName, String report_date){
        Map<String, Object> params = new HashMap<>();
        params.put("report_date", report_date);
        for(FieldData fieldData : fieldList){
            params.put("row_id", fieldData.getRowId());
            params.put("column_id", fieldData.getColumnId());
            params.put("data_value", fieldData.getDataValue());
            try {
                calculationFormulaDao.fieldDataStoreIntoDatabase(params, tableName);
            }catch (Exception e){
                PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_EXPRESSION_CALC_EXECUTE, "计算公式表坐标数据入库语句执行异常:" + e.getMessage());
                //throw new CalculationFormulaExecuteException("计算公式表坐标数据入库语句执行异常!");
            }
        }
    }



}
