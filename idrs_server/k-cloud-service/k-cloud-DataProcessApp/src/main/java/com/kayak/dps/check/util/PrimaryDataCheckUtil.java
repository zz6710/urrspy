package com.kayak.dps.check.util;

import com.kayak.core.system.SysBeans;
import com.kayak.dps.check.constants.DataValidateTypeConstants;
import com.kayak.dps.check.dao.DataValidateDao;
import com.kayak.dps.check.exception.DataValidateExecuteException;
import com.kayak.dps.check.exception.SourceDataValidateQueryException;
import com.kayak.dps.expresssion.util.FormulaUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PrimaryDataCheckUtil {

    private static DataValidateDao dataValidateDao = SysBeans.getBean("dataValidateDao");

    /**
     * 通过表明及行列值获取该字段的值对象(二维报表)
     * @param table_name 报送表名
     * @param row_id
     * @param column_id
     * @param report_date 报送数据生成日期
     * @return
     */
    public static Object valueC2(String table_name, String row_id, String column_id, String report_date) throws SourceDataValidateQueryException {
        Map<String, Object> params = new HashMap<>();
        params.put("row_id", row_id);
        params.put("column_id", column_id);
        params.put("report_date", report_date);
        return dataValidateDao.getObjectValueByRowColumnId(table_name, params);//dataValidateDao.getObjectValueByRowColumnId(table_name, params);
    }

    /**
     * 通过表明及行列值获取该字段的值对象(一维报表)
     * @param table_name
     * @param column_id 一维报表列id
     * @param report_date 报送数据生成日期
     * @return
     */
    public static Object valueC1(String table_name, String column_id, String report_date) throws SourceDataValidateQueryException {
        Map<String, Object> params = new HashMap<>();
        params.put("table_name", table_name);
        params.put("column_id", column_id);
        params.put("report_date", report_date);
        return dataValidateDao.getObjectValueByRowColumnId(table_name, params);
    }

    /**
     * 通用数据校验工具方法：源数据校验/报送数据加工校验
     * @param index_type 校验类型 DataValidateTypeConstants常量
     * @param check_val 校验对象 查询结果值
     * PS:无论是源数据校验还是报表集市层校验，check_val字段的输入值都为express公式左端的结果对象
     * 报表集市层check_val位置传入PrimaryDataCheckUtil(table_name,row_id,column_id) ,而源数据贴源层校验传入查询语句单行单列查询结果作为check_val
     * @param express 计算公式 查询结果值
     * @param allow_deviation 允许误差值
     * @param report_date 报送数据校验日期
     * @param coordinate_type 报表维度:1-一维报表 2-二维报表
     * @param report_table 报送表名
     * @param reflect_column 校验列字段代码
     * @return
     */
    public static boolean validateDataByIndexTypeC2(String index_type, Object check_val, String report_table, String express, String coordinate_type, String allow_deviation,
                                                    String report_date, String reflect_column, Map<String,Object> params2) throws Exception{
        String[] expressStr;

        try{
            if (DataValidateTypeConstants.NULLABLE_CHECK.equals(index_type)){//非空校验
                return FormulaUtil.isnull(check_val);
            } else if (DataValidateTypeConstants.RANGING_CHECK.equals(index_type)){//值域校验
                return FormulaUtil.dictRange(check_val, express.trim());
            } else if (DataValidateTypeConstants.COLUMN_FORMAT_CHECK.equals(index_type)){//字段格式及长度校验
                if("C(".equalsIgnoreCase(express.substring(0,2))){//字符长度校验
                    return FormulaUtil.charLengthCheck(check_val, express);
                }else if("N(".equalsIgnoreCase(express.substring(0,2))){//数字长度校验
                    return FormulaUtil.numberLengthCheck(check_val, express);
                }else if("DATE(".equalsIgnoreCase(express.substring(0,5))){
                    return FormulaUtil.dateFormat(check_val, express);
                }else if("valueFormat".equalsIgnoreCase(express.trim().substring(0, express.trim().indexOf("(")))){
                    return FormulaUtil.valueFormat(String.valueOf(check_val), express.trim());
                }else{
                    return false;
                }
            } else if (DataValidateTypeConstants.FILE_FORMAT_CHECK.equals(index_type)){//文件格式及大小校验,暂无该校验
                return true;
            } else if (DataValidateTypeConstants.LINKED_CHECK.equals(index_type)){//字段联动校验
                /**暂未实现,具体实现逻辑需要设计*/
                return false;
            } else if (DataValidateTypeConstants.NUMERICAL_CHECK.equals(index_type)){//数字校验
                return FormulaUtil.numberRangeCheck(check_val, String.valueOf(check_val).trim());
            } else if (DataValidateTypeConstants.REPEATED_CHECK.equals(index_type)){//重复性校验
                /**重复校验场景仅存在于一维报表数据校验*/
                return false;
            } else if (DataValidateTypeConstants.IDENTITY_CHECK.equals(index_type)){//身份证校验
                return FormulaUtil.identityRegexValidate(String.valueOf(check_val));
            } else if ((DataValidateTypeConstants.CALCULATE_CHECK.equals(index_type) || DataValidateTypeConstants.TABLE_CHECK.equals(index_type))) {//计算/表间校验
                String express_symbol = getUsualSymbolByExpress(express);
                //先处理计算公式
                if (!"".equals(express_symbol)) {
                    expressStr = express.split(express_symbol);
                } else {
                    throw new DataValidateExecuteException("报送数据校验表达式不符合规范");
                }
                /**仅报表集市层数据 表间校验 校验*/
                return FormulaUtil.dataFormulaValidateCheck(express, allow_deviation, express_symbol, report_date, report_table, coordinate_type, params2);
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage() + ":" + e);
        }
    }

    /**
     * 通用数据校验工具方法：源数据校验/报送数据加工校验
     * @param index_type 校验类型 DataValidateTypeConstants常量
     * @param check_val 校验对象 查询结果值
     * PS:无论是源数据校验还是报表集市层校验，check_val字段的输入值都为express公式左端的结果对象
     * 报表集市层check_val位置传入PrimaryDataCheckUtil(table_name,row_id,column_id) ,而源数据贴源层校验传入查询语句单行单列查询结果作为check_val
     * @param express 计算公式 查询结果值
     * @param allow_deviation 允许误差值
     * @param report_date 报送数据校验日期
     * @param coordinate_type 报表维度:1-一维报表 2-二维报表
     * @param report_table 报送表名
     * @param reflect_column 校验列字段代码
     * @param params1 一维报表报送数据集合Map<表名, List<Map<字段名, 报送数据值>>>
     * @return
     */
    public static boolean validateDataByIndexTypeC1(String index_type, Object check_val, String report_table, String express, String coordinate_type, String allow_deviation,
                                                    String report_date, String reflect_column, Map<String, Object> params1) throws Exception{
        String[] expressStr;//校验表达式

        try{
            if (DataValidateTypeConstants.NULLABLE_CHECK.equals(index_type)) {//非空校验
                return FormulaUtil.isnull(check_val);
            } else if (DataValidateTypeConstants.RANGING_CHECK.equals(index_type)) {//值域校验
                return FormulaUtil.dictRange(report_table, express.trim());
            } else if (DataValidateTypeConstants.COLUMN_FORMAT_CHECK.equals(index_type)) {//字段格式及长度校验
                if ("CN".equalsIgnoreCase(express)) {//不能含有中文
                    return FormulaUtil.ChineseCheck(check_val);
                } else if ("C(".equalsIgnoreCase(express.substring(0,2))) {//字符长度校验
                    return FormulaUtil.charLengthCheck(check_val, express);
                } else if ("N(".equalsIgnoreCase(express.substring(0,2))) {//数字长度校验
                    return FormulaUtil.numberLengthCheck(check_val, express);
                }else if ("CD(".equalsIgnoreCase(express.substring(0,3)) || "ND(".equalsIgnoreCase(express.substring(0,3))) {//字符|数字长度定长校验
                    return FormulaUtil.fixedLengthCheck(check_val, express);
                } else if ("DATE(".equalsIgnoreCase(express.substring(0,5))) {//日期格式校验
                    return FormulaUtil.dateFormat(check_val, express);
                } else if ("TYPE(".equalsIgnoreCase(express.substring(0,5))) {//数据类型校验
                    return FormulaUtil.dataTypeCheck(String.valueOf(check_val), express);
                } else if ("EMAIL".equalsIgnoreCase(express)) {//邮箱格式校验
                    return FormulaUtil.emailFormat(String.valueOf(check_val));
                } else {
                    /** 其他类型的校验类型暂时不考虑,通过校验 */
                    return true;
                }
            } else if (DataValidateTypeConstants.FILE_FORMAT_CHECK.equals(index_type)) {//文件格式及大小校验,暂无该校验
                return true;
            } else if (DataValidateTypeConstants.LINKED_CHECK.equals(index_type)) {//字段联动校验
                /**暂未实现,具体实现逻辑需要设计*/
                if ((express.contains("LK_NULL(") || express.contains("LK_NESS(") || express.contains("LK_COND(")) && express.contains("?")) {//满足条件必须为空
                    return FormulaUtil.linkedNullCheck(express, report_table, params1);
                }
                return false;
            } else if (DataValidateTypeConstants.NUMERICAL_CHECK.equals(index_type)) {//数字校验
                return FormulaUtil.numberRangeCheck(check_val, express);
            } else if (DataValidateTypeConstants.REPEATED_CHECK.equals(index_type)) {//重复性校验
                /**重复校验场景仅存在于一维报表数据校验*/
                if (express.contains("RPC(")) {
                    return FormulaUtil.repeatCheck(express, params1, report_date) ;
                } else {
                    return false;
                }
            } else if (DataValidateTypeConstants.IDENTITY_CHECK.equals(index_type)) {//身份证校验
                return FormulaUtil.identityRegexValidate(String.valueOf(check_val));
            } else if ((DataValidateTypeConstants.CALCULATE_CHECK.equals(index_type) || DataValidateTypeConstants.TABLE_CHECK.equals(index_type))){//计算/表间校验
                /** 一维报表计算校验，获取值的方式为 */
                String express_symbol = getUsualSymbolByExpress(express);
                //先处理计算公式
                if (!"".equals(express_symbol)) {
                    expressStr = express.split(express_symbol);
                } else {
                    throw new DataValidateExecuteException("报送数据校验表达式不符合规范");
                }
                /**仅报表集市层数据 表间校验 校验*/
                return FormulaUtil.dataFormulaValidateCheck(express, allow_deviation, express_symbol, report_date, report_table, coordinate_type, params1);
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage() + ":" + e);
        }
    }

    /**
     * 各类异常信息记录处理方法
     * @param error_code 异常类型
     * @param error_info 异常信息
     */
    public static void ErrorInfoRecordHandle(String error_code, String error_info) {
        Map<String, Object> params = new HashMap<>();
        params.put("error_code", error_code);
        params.put("error_info", error_info);
        try{
            dataValidateDao.insertErrorMessage(params);
        }catch (Exception e){
            log.info("报错信息插入语句执行异常!");
        }
    }

    /**
     * 数据校验记录信息处理方法
     * @param params
     */
    public static void unsatisfiedValidateDataRecord(Map<String, Object> params){
        try{
            dataValidateDao.insertUnsatisfiedValidateData(params);
        }catch (Exception e){
            log.info("数据校验结果信息插入语句执行异常!"+params.toString(), e.getMessage());
        }
    }

    /**
     * 批量数据校验记录信息处理方法
     * @param paramsList
     */
    public static void unsatisfiedValidateDataRecord(
            Map<String, String> params_ep, List<Map<String, Object>> paramsList, String indexCode, String dealDate) throws Exception{
        try{
            //更新数据状态
            dataValidateDao.updateUnsatisfiedValidateData(params_ep, paramsList, dealDate);
            //插入错误数据
            dataValidateDao.insertUnsatisfiedValidateData(paramsList, indexCode, dealDate);
        }catch (Exception e){
            log.info("数据校验结果信息插入语句执行异常!"+paramsList.toString(), e.getMessage());
            throw new Exception(e.getMessage() + ":" + e);
        }
    }

    /**
     * 记录长耗时指标信息
     * @param index_code
     * @param cost_time
     * @param sys_date
     * @param report_table
     * @param num_count
     */
    public static void recordLongCostIndexInfo (String index_code, long cost_time, String sys_date, String report_table, int num_count) {
        try {
            dataValidateDao.putLongCostIndexRecord(index_code, cost_time, sys_date, report_table, num_count);
        } catch (Exception e) {
            log.error("插入长耗时指标信息异常" + e.getMessage());
        }
    }

    /**
     * 判断公式中常用比较符号
     * @param express
     * @return
     */
    public static String getUsualSymbolByExpress(String express){
        if (express.contains("<>") && express.split("<>").length > 1){
            return "<>";
        } else if ((express.contains("<=") && express.split("<=").length > 1) || (express.contains("≤") && express.split("≤").length > 1)) {
            return "<=";
        } else if ((express.contains(">=") && express.split(">=").length > 1) || (express.contains("≥") && express.split("≥").length > 1)) {
            return ">=";
        } else if (express.contains("<") && express.split("<").length > 1) {
            return "<";
        } else if (express.contains(">") && express.split(">").length > 1) {
            return ">";
        } else if (express.contains("=") && express.split("=").length > 1) {
            return "=";
        } else {
            return "";
        }
    }

    /**
     * 判断字符公式比较中表达式符号
     * @param express
     * @return
     */
    public static String getCharSymbolByExpress(String express){
        if (express.contains("=") && express.split("=").length > 1){
            return "=";
        } else if (express.contains("<>") && express.split("<>").length > 1) {
            return "<>";
        } else {
            return "";
        }
    }

}
