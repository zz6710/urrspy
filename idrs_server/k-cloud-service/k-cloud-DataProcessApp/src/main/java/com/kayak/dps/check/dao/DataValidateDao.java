package com.kayak.dps.check.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.clear.utils.DateUtils;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.app.model.CheckIndexModel;
import com.kayak.dps.check.constants.DataValidateTypeConstants;
import com.kayak.dps.check.constants.ErrorCollectionConstants;
import com.kayak.dps.check.enums.CoorDataEnum;
import com.kayak.dps.check.exception.DataValidateExecuteException;
import com.kayak.dps.check.exception.SourceDataValidateQueryException;
import com.kayak.dps.check.model.ExpressDictDTO;
import com.kayak.dps.check.util.ExpressDictUtil;
import com.kayak.dps.check.util.PrimaryDataCheckUtil;
import com.kayak.dps.direct.model.dto.IndexCodeDTO;
import com.kayak.dps.direct.model.dto.IndexCodeResultDTO;
import com.kayak.dps.expresssion.model.dto.ExpressDTO;
import com.kayak.dps.expresssion.util.ExpressionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Repository
@Slf4j
public class DataValidateDao {

    @Autowired
    protected DaoService daoService;
    @Autowired
    protected ComnDao comnDao;

    /**
     * 获取需要校验的所有指标信息
     * 该方法需处理由验证指标表 index_info 转化成坐标公式的value(table_name, row_id, column_id)格式的计算公式
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, List<CheckIndexModel>> getIndexCheckInfoByTopic(Map<String, String> params) throws Exception {
        Map<String, List<CheckIndexModel>> indexListMap = new HashMap<>();
        try {
            String sqlStr = "select distinct t.report_table, r.coordinate_type,r.report_freq,r.is_revalue" +
                    "  from base_reportdata_index_config t " +
                    "  join base_report_info r on r.report_table = t.report_table " +
                    " where t.is_effect = '01' and  r.check_flag ='1' ";
            if (StringUtils.isNotBlank(params.get("reportType")) && !"null".equals(params.get("reportType"))) {
                sqlStr = sqlStr + " and r.report_catgory = '" + params.get("reportType") + "'";
            }
            if (StringUtils.isNotBlank(params.get("indexType")) && !"null".equals(params.get("indexType"))) {
                sqlStr = sqlStr + " and t.index_type = '" + params.get("indexType") + "'";
            }
            if (StringUtils.isNotBlank(params.get("reportTable")) && !"null".equals(params.get("reportTable"))) {
                sqlStr = sqlStr + " and r.report_table = '" + params.get("reportTable") + "'";
            }
            //sqlStr += " order by t.index_code asc ";
            List<SqlRow> reportTableResList = comnDao.findRows(sqlStr, DataSourceProperty.PUB, params);//获取所有的报表集合

            List<CheckIndexModel> indexList = new ArrayList<>();
            for (SqlRow sqlRow : reportTableResList) {
                String indexSql = "select t.index_code, t.report_table, t.row_num, t.column_num, t.row_name, t.list_name, t.relation_tables, t.tables_relationships, " +
                        "t.index_type, t.express, t.allow_deviation, t.index_detail, t.correct_prompt, t.error_prompt, t.remark, t.index_name, t.index_rule ,t.split_flag" +
                        " from base_reportdata_index_config t " +
                        " join base_report_info r on r.report_table = t.report_table " +
                        "where t.is_effect = '01' and t.report_table = '" + sqlRow.getString("report_table") + "'";
                if (StringUtils.isNotBlank(params.get("indexType")) && !"null".equals(params.get("indexType"))) {
                    indexSql = indexSql + " and t.index_type = '" + params.get("indexType") + "'";
                }
                if (StringUtils.isNotBlank(params.get("indexCode")) && !"null".equals(params.get("indexCode"))) {
                    indexSql = indexSql + " and t.index_code = '" + params.get("indexCode") + "'";
                }
                indexSql += " order by t.index_code asc ";
                indexList = comnDao.findRows(CheckIndexModel.class, indexSql, DataSourceProperty.PUB, params);//获取报表需要校验的指标集合
                indexListMap.put(sqlRow.getString("report_table") + "-" + sqlRow.getString("coordinate_type")+ "-" + sqlRow.getString("report_freq")+ "-" + sqlRow.getString("is_revalue"), indexList);
            }

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new SourceDataValidateQueryException("报送数据查询汇总校验指标信息异常");
        }
        return indexListMap;
    }

    /**
     * 报送业务表数据校验方法(二维报表)
     * 校验逻辑:根据校验日期查询
     *
     * @param params 查询参数组装:校验日期、表名、字段名
     */
    public Map<String, Object> checkC2ReportDataByColumnCode(Map<String, String> params, Map<String, String> params_row, List<Map<String, String>> paramList, Map<String, Object> c2RowDataMap) throws Exception {
        Map<String, Object> res_param = new HashMap<>();
        try {
            res_param.put("topic", String.valueOf(params.get("topic")));
            res_param.put("validate_type", String.valueOf(params.get("indexType")));
            res_param.put("validate_table", params.get("reportTable"));//校验记录:校验表表名
            res_param.put("validate_row", params.get("rowNum") + "-" + params.get("rowName"));//一维度报表不记录行信息
            res_param.put("column_code", params.get("reflect_column"));//校验字段代码
            res_param.put("validate_column", params.get("columnNum") + "-" + params.get("listName"));//校验记录:校验问题表列字段对应数
            res_param.put("index_code", params.get("indexCode"));//校验指标代码
            res_param.put("index_name", params.get("indexName"));//校验指标名称
            res_param.put("deal_date", params.get("deal_date"));//数据日期
            res_param.put("data_id", params.get(params.get("reportTable") + ".ID") == null ? params.get(params.get("reportTable") + ".REGISTER_SERNO") : params.get(params.get("reportTable") + ".ID"));//校验指标名称

            if (params_row == null && CollectionUtil.isNotEmpty(paramList)) {
                params_row = paramList.get(0);
            }

            ExpressDTO expressDTO = ExpressDTO.dto().initDTO(params.get("indexCode"), params.get("express"), params.get("allowDeviation"));
            IndexCodeResultDTO indexCodeResultDTO = ExpressionUtil.checkExpress(expressDTO, params_row, paramList, c2RowDataMap);
            Boolean checkRes = indexCodeResultDTO.getRetVal();
            //校验通过不记录校验结果
            if(checkRes) {
                return null;
            }
            //录入校验是否通过的相关信息
            res_param.put("validate_result", checkRes ? 1 : -1);//-1-校验不通过/1-校验通过
            String passInfo = checkRes?params.get("correctPrompt"):params.get("errorPrompt");
            for (String c2Row : indexCodeResultDTO.getC2RowLogList()) {
                passInfo = passInfo.replaceFirst(CoorDataEnum.COOR_DATA_TAG.getVal(), c2Row);
            }
            if (ObjectUtil.isNotEmpty(params.get("remark_column"))) {
                String[] tag_columns = params.get("remark_column").split(",");
                for (String column : tag_columns) {
                    passInfo = passInfo.replace("[" + column + "]", String.valueOf(params_row.get(params.get("reportTable") + "." + column.toUpperCase())));//将字符替换入错误提示
                }
            }
            res_param.put("reason", passInfo);

        } catch (Exception e) {
            String error_mess = "报送数据: 校验指标" + params.get("indexCode") + " 数据校验异常: " + e.getMessage();
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_REPORT, error_mess);//base_error_message记录异常报错报错信息
            res_param.put("validate_result", -1);//-1-校验不通过
            res_param.put("reason", error_mess);//校验不通过
            PrimaryDataCheckUtil.unsatisfiedValidateDataRecord(res_param);//base_data_validation记录数据校验不通过信息
        }
        return res_param;
    }

    /**
     * 报送业务表数据校验方法(一维报表)
     * 校验逻辑:根据校验日期查询
     *
     * @param params  查询参数组装:校验日期、表名、字段名
     * @param params1 一维报表报送数据行Map<字段名, 值>
     */
    public void checkC1ReportDataByColumnCode(Map<String, String> params, Map<String, Object> params1) throws Exception {
        boolean check_result = true;//字段校验结果
        Map<String, Object> res_param = new HashMap<>();

        try {
            res_param.put("topic", String.valueOf(params.get("topic")));
            res_param.put("validate_type", String.valueOf(params.get("indexType")));
            res_param.put("validate_table", params.get("reportTable"));//校验记录:校验表表名
            res_param.put("validate_row", null);//一维度报表不记录行信息
            res_param.put("column_code", params.get("reflect_column"));//校验字段代码
            res_param.put("validate_column", params.get("columnNum") + "-" + params.get("listName"));//校验记录:校验问题表列字段对应数
            res_param.put("index_code", params.get("indexCode"));//校验指标代码

            boolean checkRes = PrimaryDataCheckUtil.validateDataByIndexTypeC1(params.get("indexType").toString(), params1.get(res_param.get("column_code")), String.valueOf(params.get("reportTable")),
                    String.valueOf(params.get("express")), String.valueOf(params.get("coordinate_type")),
                    String.valueOf(params.get("allowDeviation")), String.valueOf(params.get("deal_date")), String.valueOf(params.get("reflect_column")), params1);

            if (!checkRes) {
                check_result = false;
            }

            if ((DataValidateTypeConstants.CALCULATE_CHECK.equals(params.get("indexType").toString()) || DataValidateTypeConstants.TABLE_CHECK.equals(params.get("indexType").toString())) ||
                    DataValidateTypeConstants.REPEATED_CHECK.equals(params.get("indexType").toString())) {
                //break;//一维报表计算校验及表间校验只需要校验一条数据，跳出循环
            }

            res_param.put("validate_result", check_result ? 1 : -1);//-1-校验不通过/1-校验通过
            if ((!check_result)) {
                res_param.put("reason", params.get("errorPrompt"));//校验不通过
            } else {
                res_param.put("reason", params.get("correctPrompt"));//校验通过
            }
            PrimaryDataCheckUtil.unsatisfiedValidateDataRecord(res_param);//base_data_validation记录数据校验不通过信息
        } catch (Exception e) {
            String error_mess = "报送数据: 校验指标" + params.get("indexCode") + " 数据校验异常: " + e.getMessage();
            res_param.put("validate_result", check_result ? 1 : -1);//-1-校验不通过/1-校验通过
            res_param.put("reason", error_mess);//校验不通过
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_REPORT, error_mess);//base_error_message记录异常报错报错信息
        }
    }

    /**
     * 人行dat文件数据报送业务表数据校验方法
     * 校验逻辑:根据校验日期查询
     *
     * @param params     查询参数组装:校验日期、表名、字段名
     * @param params_row 人行dat报表报送数据行Map<表名.字段名, 值>
     */
    public Map<String, Object> checkPBCReportDataByColumnCode(Map<String, String> params, Map<String, String> params_row) throws Exception {
        boolean check_result = true;//字段校验结果
        Map<String, Object> res_param = new HashMap<>();
        String index_rule = params.get("indexRule");

        try {
            res_param.put("topic", String.valueOf(params.get("topic")));
            res_param.put("validate_type", String.valueOf(params.get("indexType")));
            res_param.put("validate_table", params.get("reportTable"));//校验记录:校验表表名
            res_param.put("validate_row", null);//一维度报表不记录行信息
            res_param.put("column_code", params.get("reflect_column"));//校验字段代码
            res_param.put("validate_column", params.get("columnNum") + "-" + params.get("listName"));//校验记录:校验问题表列字段对应数
            res_param.put("index_code", params.get("indexCode"));//校验指标代码
            res_param.put("index_name", params.get("indexName"));//校验指标名称
            res_param.put("deal_date", params_row.get(params.get("reportTable") + ".THEORY_REPORT_START_DATE"));//校验指标名称
            res_param.put("data_id", params_row.get(params.get("reportTable") + ".ID") == null ? params_row.get(params.get("reportTable") + ".REGISTER_SERNO") : params_row.get(params.get("reportTable") + ".ID"));//校验指标名称
            res_param.put("data_id_col", params_row.get(params.get("reportTable") + ".ID") == null ? "REGISTER_SERNO" : "ID");

            ExpressDTO expressDTO = ExpressDTO.dto().initDTO(params.get("indexCode"), params.get("express"), params.get("allowDeviation"));
            IndexCodeResultDTO indexCodeResultDTO = ExpressionUtil.checkExpressCorOne(expressDTO, params_row);
            Boolean checkRes = indexCodeResultDTO.getRetVal();

            // 添加校验表达式
            res_param.put("express_val", indexCodeResultDTO.getExpressVal());
            if ((DataValidateTypeConstants.CALCULATE_CHECK.equals(params.get("indexType").toString()) || DataValidateTypeConstants.TABLE_CHECK.equals(params.get("indexType").toString())) ||
                    DataValidateTypeConstants.REPEATED_CHECK.equals(params.get("indexType").toString())) {
                //break;//一维报表计算校验及表间校验只需要校验一条数据，跳出循环
            }
            //校验通过不记录校验结果
            if(checkRes) {
                return null;
            }

            if ("3".equals(index_rule)) {
                res_param.put("validate_result", checkRes ? 1 : 2);//2-校验预警/1-校验通过
            } else {
                res_param.put("validate_result", checkRes ? 1 : -1);//-1-校验不通过/1-校验通过
            }

            //录入校验是否通过的相关信息
            String passInfo = checkRes?params.get("correctPrompt"):params.get("errorPrompt");
            if (ObjectUtil.isNotEmpty(params.get("remark_column"))) {
                String[] tag_columns = params.get("remark_column").split(",");
                for (String column : tag_columns) {
                    passInfo = passInfo.replace("[" + column + "]", String.valueOf(params_row.get(params.get("reportTable") + "." + column.toUpperCase())));//将字符替换入错误提示
                }
            }
            res_param.put("reason", passInfo);

        } catch (Exception e) {
            String error_mess = "报送数据: 校验指标" + params.get("indexCode") + " 数据校验异常: " + e.getMessage();
            if ("3".equals(index_rule)) {
                res_param.put("validate_result", check_result ? 1 : 2);//2-校验预警/1-校验通过
            } else {
                res_param.put("validate_result", check_result ? 1 : -1);//-1-校验不通过/1-校验通过
            }
            res_param.put("validate_result", check_result ? 1 : -1);//-1-校验不通过/1-校验通过
            res_param.put("reason", error_mess);//校验不通过
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_REPORT, error_mess);//base_error_message记录异常报错报错信息
        }
        return res_param;
    }

    /**
     * 根据表名和坐标信息获取业务报送表中数据对象
     *
     * @param table_name
     * @param params
     * @return
     * @throws SourceDataValidateQueryException
     */
    public Object getObjectValueByRowColumnId(String table_name, Map<String, Object> params) throws SourceDataValidateQueryException {
        String queryStr = "select data_value from " + table_name + " where row_id = $S{row_id} and column_id = $S{column_id} and report_date = $S{report_date}";

        try {
            SqlRow row = comnDao.findRow(queryStr, DataSourceProperty.PUB, params);
            if (row != null) {
                return row.get("data_value");
            } else {
                throw new SourceDataValidateQueryException("查询报送业务表" + table_name + "报送数据查询信息异常:row_id---->" +
                        params.get("row_id") + ",column_id---->" + params.get("column_id") + ",报送日期:" + params.get("report_date") + ";");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SourceDataValidateQueryException(e.getMessage());
        }
    }

    /**
     * 查询校验值域字典是否满足对应关系
     *
     * @param check_val
     * @param range_dict
     * @return
     */
    public boolean dataDictRangeCheck(String check_val, String range_dict) throws SourceDataValidateQueryException {
        //判断校验是源数据校验还是报送数据校验
        String dict = range_dict.substring(1, range_dict.indexOf(")") - 1);

        String sqlStr = "select itemval from sys_dict_item where dict = '" + dict + "' and itemkey = '" + check_val + "'";
        try {
            SqlRow row = comnDao.findRow(sqlStr, DataSourceProperty.PUB, null);
            //查询结果为空时认为不符合校验结果，非空时通过
            if (row != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new SourceDataValidateQueryException("报送数据值域校验查询语句异常:" + e.getMessage());
        }
    }

    /**
     * 插入处理报错信息
     *
     * @param params
     */
    public void insertErrorMessage(Map<String, Object> params) throws DataValidateExecuteException {
        String sqlStr = "insert into base_error_message (error_code, error_info, error_group, create_date, create_time) " +
                "values ($S{error_code}, $S{error_info}, $S{error_group}, DATE_FORMAT(NOW(), '%Y%m%d'), DATE_FORMAT(NOW(), '%H%i%s'))";
        try {
            comnDao.update(sqlStr, DataSourceProperty.PUB, params);
        } catch (Exception e) {
            throw new DataValidateExecuteException("源数据校验插入报错信息语句执行异常: " + e.getMessage());
        }
    }

    /**
     * 插入源数据及报送数据校验结果记录
     *
     * @param params
     */
    public void insertUnsatisfiedValidateData(Map<String, Object> params) throws DataValidateExecuteException {
        String deleteSql = "delete from base_data_validation where index_code = '"+params.get("index_code")+"' " +
                "and create_date = DATE_FORMAT(NOW(), '%Y%m%d') and deal_date = '"+params.get("deal_date")+"'";

        String executeStr = "insert into base_data_validation (topic, validate_type, validate_result, reason," +
                "validate_table, column_code, validate_row, validate_column, create_date, create_time, index_code, index_name,data_id, deal_date) " +
                "values ($S{topic}, $S{validate_type}, $S{validate_result}, $S{reason}, " +
                "$S{validate_table}, $S{column_code}, $S{validate_row}, $S{validate_column}, " +
                "DATE_FORMAT(NOW(), '%Y%m%d'), DATE_FORMAT(NOW(), '%H%i%s'), $S{index_code}, $S{index_name}, $S{data_id}, $S{deal_date})";
        try {
            comnDao.update(deleteSql, DataSourceProperty.PUB);
            comnDao.update(executeStr, DataSourceProperty.PUB, params);
        } catch (Exception e) {
            throw new DataValidateExecuteException("数据校验插入数据校验结果信息语句执行异常: " + e.getMessage());
        }
    }

    /**
     * 更新全部数据
     */
    public void updateSuccess(String table, String dealDate) throws Exception {
        String sql = "update " + table + " set register_status = 2 where theory_report_start_date = " + dealDate;
        comnDao.update(sql);
    }


    /**
     * 更新错误数据记录
     * @param paramsList
     */
    public void updateUnsatisfiedValidateData(Map<String, String> paramsEp, List<Map<String, Object>> paramsList, String dealDate) throws Exception {
        //更新校验数据状态
        try{
            if (ObjectUtil.isEmpty(paramsList)) {
                return;
            }
            StringBuffer keyFalse = new StringBuffer();
            for (Map<String, Object> param : paramsList) {
                if(ObjectUtil.isEmpty(param) || ObjectUtil.isEmpty(param.get("data_id"))) {
                    continue;
                }
                keyFalse.append("'" + param.get("data_id") + "',");
            }
            String paramId = String.valueOf(paramsList.get(0).get("data_id_col"));
            if (ObjectUtil.isNotEmpty(keyFalse)) {
                keyFalse.deleteCharAt(keyFalse.length() - 1);
                String sqlFalse = "update " + paramsEp.get("reportTable") + " set register_status = 1 " +
                        "where `" + paramId + "` in ( " + keyFalse + " ) and theory_report_start_date = " + dealDate;
                comnDao.update(sqlFalse);
            }
        } catch (Exception e) {
            throw new Exception("校验数据更新异常" + e.getMessage());
        }

    }

    /**
     * 插入源数据及报送数据校验结果记录
     *
     * @param paramsList
     */
    public  void insertUnsatisfiedValidateData(List<Map<String, Object>> paramsList, String indexCode, String dealDate) throws Exception {

        long startTime = System.currentTimeMillis();
        // 删除数据日期、校验日期对应指标（目前批量校验都是相同校验日期与数据日期，默认取第一个）
//        String deleteSql = "delete from base_data_validation where index_code = '"+indexCode+"' " +
//                "and create_date = DATE_FORMAT(NOW(), '%Y%m%d') and deal_date = '"+dealDate+"'";

        String executeStr = "insert into base_data_validation (topic, validate_type, validate_result, reason," +
                "validate_table, column_code, validate_row, validate_column, create_date, create_time, index_code, " +
                "index_name,data_id, deal_date) values (?, ?, ?, ?, ?, ?, ?, ?, DATE_FORMAT(NOW(), '%Y%m%d'), " +
                "DATE_FORMAT(NOW(), '%H%i%s'), ?, ?, ?, ?)";

        comnDao.doTrans(() -> {
            //comnDao.update(deleteSql,paramsList);

            Connection connection = comnDao.getConnection();
            PreparedStatement ps = connection.prepareStatement(executeStr);

            if(ObjectUtil.isEmpty(paramsList)) {
                return;
            }
            try  {
                for (Map<String, Object> param : paramsList) {
                    if(ObjectUtil.isEmpty(param)) {
                        continue;
                    }
                    ps.setString(1, String.valueOf(param.get("topic")));
                    ps.setString(2, String.valueOf(param.get("validate_type")));
                    ps.setString(3, String.valueOf(param.get("validate_result")));
                    ps.setString(4, String.valueOf(param.get("reason")));
                    ps.setString(5, String.valueOf(param.get("validate_table")));
                    ps.setString(6, String.valueOf(param.get("column_code")));
                    ps.setString(7, String.valueOf(param.get("validate_row")));
                    ps.setString(8, String.valueOf(param.get("validate_column")));
                    ps.setString(9, String.valueOf(param.get("index_code")));
                    ps.setString(10, String.valueOf(param.get("index_name")));
                    ps.setString(11, String.valueOf(param.get("data_id")));
                    ps.setString(12, String.valueOf(param.get("deal_date")));
                    ps.addBatch();
                }
                ps.executeBatch();
                log.info(" ##### 批量入库{}耗时: {} ms", paramsList.size(), System.currentTimeMillis() - startTime);
            } catch (Exception e) {
                throw new DataValidateExecuteException("数据校验插入数据校验结果信息语句执行异常: " + e.getMessage());
            }finally {
                ps.close();
            }
        });

    }


    public List<String> getValueListByReportColumn(String report_table, String column_code) throws DataValidateExecuteException {
        List<String> valueList = new ArrayList<>();
        String sql = "select " + column_code + " from " + report_table;
        //List<SqlRow> valueRes = comnDao.findRows(sql, DataSourceProperty.PUB, null);
        return valueList;
    }

    /**
     * 根据报表名称查询报表查询维度
     *
     * @param report_table
     * @return
     * @throws Exception
     */
    public String getReportTableCoordinateType(String report_table) throws Exception {
        String sql = "select coordinate_type from base_report_info where report_table = '" + report_table + "'  and  check_flag ='1' ";
        SqlRow sqlRes = comnDao.findRow(sql, DataSourceProperty.PUB, null);
        if (sqlRes != null) {
            return sqlRes.getString("coordinate_type");
        }
        return "";
    }

    /**
     * 根据报送表列顺序指针获取字段代码
     *
     * @param report_table
     * @param field_index
     * @return
     * @throws Exception
     */
    public String getReflectColumnByColumnNum(String report_table, String field_index) throws Exception {
        String sql = "SELECT reflect_column FROM base_report_column_info WHERE field_type = 'C' AND  report_table = '" + report_table + "' AND field_index = '" + field_index + "'";
        SqlRow sqlRes = comnDao.findRow(sql, DataSourceProperty.PUB, null);
        if (sqlRes != null) {
            return sqlRes.getString("reflect_column");
        }
        return "";
    }

    /**
     * 查询重复性校验
     *
     * @param report_table   校验表
     * @param reflect_column 校验列字段代码
     * @param report_date    报送日期
     * @return
     * @throws Exception
     */
    public boolean getCheckColumnTotalNum(String report_table, String reflect_column, String report_date) throws Exception {
        String totalSql = "select count(1) as total_num from " + report_table + " where register_status = '0' ";
        String disSql = "select count(c.val) as distinct_num from (select distinct CONCAT(" + reflect_column + ") as val from " + report_table + " where register_status = '0') c ";
        SqlRow totalRes = comnDao.findRow(totalSql, DataSourceProperty.PUB, null);
        SqlRow disRes = comnDao.findRow(disSql, DataSourceProperty.PUB, null);
        if (totalRes != null && disRes != null) {
            if (Integer.parseInt(disRes.getString("distinct_num")) < Integer.parseInt(totalRes.getString("total_num"))) {//当数值不同的查询总数小于查询总数,则代表数值有重复
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 根据报表大诶查询报表信息
     *
     * @param coordinate_type 报表维度:1-一维报表 2-二维报表
     * @return
     * @throws Exception
     */
    public List<String> getReportInfoList(String coordinate_type, String report_category) throws Exception {
        List<String> reportList = new ArrayList<>();
        String sqlStr = "select report_table from base_report_info where coordinate_type = '" + coordinate_type + "' and check_flag ='1' ";
        List<SqlRow> resList = comnDao.findRows(sqlStr, DataSourceProperty.PUB);
        if (resList.size() > 0) {
            for (SqlRow res : resList) {
                reportList.add(res.getString("report_table"));
            }
        }
        return reportList;
    }

    /**
     * 根据报表名称获取一维报表的map集合
     *
     * @param report_table
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getCoOneReportDataConfig(String report_table, String report_date) throws Exception {
        List<Map<String, Object>> mapList = new ArrayList<>();
        String queryStr = "select * from " + report_table + " where register_status <> '3' ";
        try {
            List<SqlRow> sqlResList = comnDao.findRows(queryStr, DataSourceProperty.PUB);
            if (sqlResList.size() <= 0) {
                return mapList;
            }

            for (SqlRow sqlRes : sqlResList) {
                Set<String> keySet = sqlRes.keySet();
                Map<String, Object> map = new HashMap<>();
                for (String key : keySet) {
                    map.put(key, sqlRes.getString(key));
                }
                mapList.add(map);
            }
        } catch (Exception e) {
            //部分报表无表结构或无数据，跳过并记录异常报表
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_ZZ, "组装一维报表数据异常：" + report_table + "数据异常：" + e.getMessage());//base_error_message记录异常报错报错信息
        }

        return mapList;
    }

    /**
     * 根据报表名称获取二维报表的map集合
     *
     * @param report_table
     * @return
     * @throws Exception
     */
    public Map<String, Object> getCoTwoReportDataConfig(String report_table, String report_date) throws Exception {
        Map<String, Object> paramsMap = new HashMap<>();
        String queryStr = "select report_date, row_id, column_id, data_value from " + report_table + " where report_date = '" + report_date + "' " +
                "and sys_data_status = '1'";
        try {
            List<SqlRow> sqlResList = comnDao.findRows(queryStr, DataSourceProperty.PUB);
            if (sqlResList.size() <= 0) {
                return paramsMap;
            }

            for (SqlRow sqlRes : sqlResList) {
                paramsMap.put(sqlRes.getString("row_id") + "_" + sqlRes.getString("column_id"), sqlRes.get("data_value"));
            }
        } catch (Exception e) {
            //部分报表无表结构或无数据，跳过并记录异常报表
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_ZZ, "组装二维报表：" + report_table + "数据异常：" + e.getMessage());//base_error_message记录异常报错报错信息
        }

        return paramsMap;
    }

    /**
     * 处理指标中关联表信息语句
     *
     * @param indexCodeSql
     * @param indexCodeDTO
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> indexTablesSqlHandler(String indexCodeSql, IndexCodeDTO indexCodeDTO) {
        List<Map<String, String>> params_index_list = new ArrayList<>();
        try {
            /**
             * 组装参数
             */
            try {
                indexCodeDTO.setLastDealDateRe(getNextTermDate(indexCodeDTO.getDealDate(), "2"));
                Instant startTime = Instant.now();
                List<SqlRow> sqlRowList = comnDao.findRows(indexCodeSql, DataSourceProperty.PUB, indexCodeDTO);
                Instant endTime = Instant.now();
                // 计算时间间隔
                Duration duration = Duration.between(startTime, endTime);
                long seconds = duration.getSeconds();
                log.info("本次查询用时{}秒",seconds);
                for (SqlRow row : sqlRowList) {
                    Set<String> keySet = row.keySet();
                    Map<String, String> params_index = new HashMap<>();
                    for (String key : keySet) {
                        params_index.put(indexCodeDTO.getReportTable() + "." + key.toUpperCase(), row.getString(key));//字段名统一大写
                    }
                    params_index_list.add(params_index);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_REPORT, "组装人行dat报表数据异常：指标代码:" + indexCodeDTO.getIndexCode() + "," + indexCodeDTO.getReportTable() + " 表数据异常：" + e1.getMessage());//base_error_message记录异常报错报错信息
            }
            return params_index_list;
        } catch (Exception e) {
            String error_mess = "报送数据: 校验指标" + indexCodeDTO.getIndexCode() + " 数据校验异常: " + e.getMessage();
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_REPORT, error_mess);//base_error_message记录异常报错报错信息
            return params_index_list;
        }

    }


    public String getNextTermDate(String cur_date, String freq_type) throws Exception {
        String sqlStr = "";
        if ("2".equals(freq_type)) {
            sqlStr = "select date_format(last_day('" + cur_date + "' - interval 1 month), '%Y%m%d') as next_date ";
        }
        return comnDao.findRow(sqlStr, DataSourceProperty.PUB, null).getString("next_date");
    }

    /**
     * 查询双条件是否唯一
     *
     * @param valueArr 0、1分别为表名、字段名
     * @param val      为校验值
     * @return
     * @throws Exception
     */
    public Boolean getUniqueIndexTable(String[] valueArr, String val) throws Exception {

        String sqlStr = "select " + valueArr[1] + " from " + valueArr[0] + " where 1 = 1 and " + valueArr[1]
                + " = '" + val + "'";

        List<SqlRow> sqlRowList = comnDao.findRows(sqlStr, DataSourceProperty.PUB);

        // 魔法值2指数据小于两条，不重复
        return sqlRowList != null && sqlRowList.size() < 2;
    }

    /**
     * 工作日校验查询
     *
     * @param startDay 开始工作日
     * @param endDay   结束工作日
     * @return
     * @throws Exception
     */
    public Integer getWorkDays(String startDay, String endDay) throws Exception {
        String sqlStr = "select * from sys_workday_set where workday > " + startDay + "and workday < " + endDay;
        List<SqlRow> sqlRowList = comnDao.findRows(sqlStr, DataSourceProperty.PUB);
        // 魔法值2指数据小于两条，不重复
        return sqlRowList.size();
    }

    /**
     * 字典值查询
     *
     * @return
     * @throws Exception
     */
    public Map<String, String> getReportTableTagColumns(String report_type) throws Exception {
        String sqlStr = "select report_table, remark_column from base_report_info where report_catgory = '" + report_type + "' ";
        List<SqlRow> sqlRows = comnDao.findRows(sqlStr, DataSourceProperty.PUB);
        if (CollectionUtil.isEmpty(sqlRows)) {
            return null;
        } else {
            Map<String, String> retObj = new HashMap<>();
            sqlRows.stream().forEach(
                    o -> retObj.put(o.getString("report_table"), o.getString("remark_column"))

            );
            return retObj;
        }
    }

    /**
     * 字典值查询
     *
     * @param dictKey   字典英文值
     * @param dictValue 字典中文值
     * @return
     * @throws Exception
     */
    public String getDictKey(String dictKey, String dictValue) throws Exception {
        String sqlStr = "select * from sys_dict_item where dict = '" + dictKey + "' and itemval = '" + dictValue + "' limit 1";
        SqlRow sqlRow = comnDao.findRow(sqlStr, DataSourceProperty.PUB);
        // 返回查询的字典值
        return ObjectUtil.isNotNull(sqlRow) ? (sqlRow.getString("itemkey")) : "";
    }

    /**
     * 查询发行机构代码
     *
     * @return
     * @throws Exception
     */
    public String getParaValueForFXJGDM() throws Exception {
        String sqlStr = "select * from sys_param where paraid = '80000047' limit 1";
        SqlRow sqlRow = comnDao.findRow(sqlStr, DataSourceProperty.PUB);
        return sqlRow.getString("paravalue");
    }

    /**
     * 字典值存在查询
     *
     * @param dictKey   字典大类名称
     * @param dictValue 字典key
     * @return
     * @throws Exception
     */
    public Boolean exitDictKey(String dictKey, String dictValue) throws Exception {
        List<ExpressDictDTO> dictList = ExpressDictUtil.dictMap.get(dictKey);
        if (ObjectUtil.isEmpty(dictList)) {
            return false;
        }
        //k、v、k+空格+v 三种形式值域均可通过
        for (ExpressDictDTO expressDictDTO : dictList) {
            if (ObjectUtil.equals(dictValue, expressDictDTO.getDictKey()) ||
                    ObjectUtil.equals(dictValue, expressDictDTO.getDictVal()) ||
                    ObjectUtil.equals(dictValue, expressDictDTO.getDictKv())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 多选字典值存在查询
     * @param dictKey    字典大类名称
     * @param dictValues 多选字典key
     * @return
     * @throws Exception
     */
    public Boolean exitDictKeys(String dictKey, String[] dictValues) throws Exception {
        List<ExpressDictDTO> dictList = ExpressDictUtil.dictMap.get(dictKey);
        if (ObjectUtil.isEmpty(dictList)) {
            return false;
        }

        for (int i = 0; i < dictValues.length; i++) {
            //k、v、k+空格+v 三种形式值域均可通过
            if(!exitDictKey(dictKey, dictValues[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查询指标是否存在
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Boolean exitIndexCode(Map<String, String> params) throws Exception {
        String str = "select * from base_reportdata_index_config where index_code = $S{indexCode}";
        SqlRow sqlRow = comnDao.findRow(str, DataSourceProperty.PUB, params);
        return CollectionUtil.isNotEmpty(sqlRow);
    }

    /**
     * 查询整体指标信息
     * @return List<SqlRow>
     * @throws Exception
     */
    public List<SqlRow> loadDict() throws Exception {
        String sql = "select dict,itemkey k,itemval v from sys_dict_item  order by dict";
        return comnDao.findRows(sql);
    }

    /**对于产品募集总量表，查询对应prod_code的产品销售区域选项数据**/
    public String getProdArea(String prodCode, String startDate) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("prodCode", prodCode);
        params.put("startDate", startDate);
        String sql = "select t1.PROD_SALES_REGION from app_initial_sub_regist_info t3 \n" +
                "left join app_prod_issuance_regist_info t2 on t2.PROD_CODE = t3.PROD_CODE\n" +
                "left join app_prod_regist_filing_info t1 on t1.IDENT_CODE = t2.PROD_IDENT_CODE " +
                "where t3.prod_code = $S{prodCode} and t3.theory_report_start_date = $S{startDate} limit 1";
        SqlRow sqlRow = comnDao.findRow(sql, DataSourceProperty.PUB, params);
        // 返回查询的字典值
        return ObjectUtil.isNotNull(sqlRow) ? (sqlRow.getString("PROD_SALES_REGION")) : "";
    }

    /**
     * 查询待验证的中间层负债编码
     * @return
     * @throws Exception
     */
    public List<String> getAssetCode(String dict) throws Exception {
        String sql = "select ASSET_CODE from app_asset_debt_register_info where ASSET_CODE is not null and ASSET_CODE <> ''";
        if ("451031".equals(dict)) {
            sql = "select ASSET_CODE from app_asset_debt_register_info where ass_debt_type in ('2601','2602','2604','2605','2606','2607','2610','2611','2612','2617','2613','2614','2616','2618','2619','2620','2699','1701','1703','1704','1706','1702','1705','2801','2802','2803','2804','2805','2806','2899') and  ASSET_CODE is not null and ASSET_CODE <> ''";
        }
        List<SqlRow> sqlRows = comnDao.findRows(sql);
        if (ObjectUtil.isEmpty(sqlRows)) {
            return null;
        }
        return sqlRows.stream().map(o->o.getString("ASSET_CODE")).collect(Collectors.toList());
    }

    /**
     *  1：（中债三期）根据校验报表与校验日期删除校验日志
     * 2：更改报送数据状态为 2：校验成功。清空备注字段
     * @param reportTable    报表名称
     * @param dealDate 数据日期
     */
    public void updateValidateData(String reportTable, String dealDate, int id, String taId, String custNo) throws Exception {
        try {
            // 特殊：投资者身份页面修改，字需要单条数据校验（为了给前端调用指标使用的）
            String sql = "";
            String dSql = "";
            if ("app_cust_register_info".equals(reportTable) && id > 0) {
                sql = " id = " + id + " and ";
                dSql = " and reason regexp '" + custNo + "'";
            }
            // 特殊：投资者持有、子产品、明细需要单条数据校验（为了给前端调用指标使用的）
            if (("app_cust_vol_register_info".equals(reportTable) ||
                    "app_cust_vol_register_sub_info".equals(reportTable) ||
                    "app_cust_trans_info".equals(reportTable)) && StringUtils.isNotEmpty(taId)) {
                sql = " ta_id = '" + taId + "' and ";
                dSql = " and reason regexp '" + custNo + "'";
            }

            long startTime = System.currentTimeMillis();
            String limit = SysUtil.getSystemParamsByParaid("90000051211"); // 查询分页数
            //1：根据校验报表与校验日期删除校验日志
            String sqlDelete = "delete from  base_data_validation where validate_table = '" + reportTable + "' and deal_date = '" + dealDate + "'" + dSql;
            comnDao.update(sqlDelete);
            //2：更改报送数据状态为 2：校验成功
            String sqlPrimaryKey = "select distinct lower(k.column_name) as column_name from information_schema.key_column_usage k where table_name = '"+reportTable+"' ";
            List<SqlRow> sqlKeyRows = comnDao.findRows(sqlPrimaryKey);
            // 没有ID主键直接更新
            boolean hasId = false;
            for (SqlRow sqlKey: sqlKeyRows) {
                if("id".equals(sqlKey.getString("column_name"))){
                    hasId = true;
                    break;
                }
            }
            if(sqlKeyRows.size()==0||!hasId){
                String updateSql = "update " + reportTable + " set register_status = 2  where " + sql + " register_status not in ('3','9') and theory_report_start_date = '" + dealDate + "'  ";
                comnDao.update(updateSql);
            } else {
                //数据是否存在
                String sql2 = "select 1 from  " + reportTable + " where " + sql + " theory_report_start_date = '" + dealDate + "'  order by id desc limit 1 ";
                List<SqlRow> sqlRows2 = comnDao.findRows(sql2);
                if(sqlRows2.size()==0){
                    return;
                }
                int dataID = 0;
                while (true) {
                    // 获取分页后最大的数据ID
                    String sql1 = "select max(id) as maxId from (select id from " + reportTable + " where " + sql + " id >" + dataID + " and theory_report_start_date = '" + dealDate + "'   limit " + limit + " ) a";
                    // 批量更新
                    String sql3 = "update " + reportTable + " set register_status = 2 where " + sql + " register_status not in ('3','9') and id >" + dataID + " and theory_report_start_date = '" + dealDate + "'   limit " + limit + " ";
                    List<SqlRow> sqlRows1 = comnDao.findRows(sql1);
                    comnDao.update(sql3);
                    // 下次更新数据的起始ID
                    dataID = Integer.parseInt(sqlRows1.get(0).getString("maxId"));
                    // dataID之后是否有数
                    String sql4 = "select 1  from  " + reportTable + " where " + sql + " id >" + dataID + " and theory_report_start_date = '" + dealDate + "'   limit 1";
                    List<SqlRow> sqlRows4 = comnDao.findRows(sql4);
                    if (sqlRows4.size()==0)
                        break;
                }
            }
            log.info(" ##### 批量更新总耗时-------: {} ms", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            throw new Exception("校验数据操作异常" + e.getMessage());
        }
    }

    /**
     * 根据报送日期查询数据日期
     * @param dParams
     * @return
     * @throws Exception
     */
    public String checkDataDate (Map<String, String> dParams) throws Exception {
        String checkSql = ExeQuery.queryExeId("VALIDATEEU001");
        return comnDao.findRow(checkSql, dParams).getString("theory_start_date");
    }

    /**
     * 根据基准日期和顺延工作日计算报表数据生成日期
     * @param base_date
     * @param delay_days
     * @return
     * @throws Exception
     */
    public String calReportGenerDate (String base_date, int delay_days, String delay_type) throws Exception {
        String querySql = "select max(workday) as workday from (select workday from sys_workday_set where workday >= '" + base_date + "' limit " + delay_days + ") b";
        if ("0".equals(delay_type)) {
            return DateUtils.getDateAddDays(base_date, delay_days);
        } else {//非自然日类型都归入工作日计算
            return comnDao.findRow(querySql, null).getString("workday");//工作日
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
    public void putLongCostIndexRecord (String index_code, long cost_time, String sys_date, String report_table, int num_count) throws Exception {
        comnDao.doTrans(() -> {
            comnDao.update("insert sys_index_analysis (index_code, cost_time, sys_date, report_table, num_count, create_date) " +
                            "values ('"+index_code+"',"+cost_time+",'"+sys_date+"','"+report_table+"',"+num_count+", sysdate())",
                    DataSourceProperty.PUB, null);
        });
    }

    /**
     * 查询报表大类
     * @param params
     */
    public List<SqlRow> getReportTable(Map<String, Object> params) throws Exception {
        String sql = " select report_catgory, report_table, table_name from base_report_info where report_table = $S{reportTable}";
        return comnDao.findRows(sql, DataSourceProperty.PUB, params);

    }

    /**
     * 查询报表大类
     * @param params
     */
    public List<SqlRow> getReportTableList(Map<String, String> params) throws Exception {
        String sql = " select table_name from base_report_info where report_table in(" +
                "select distinct report_table  from base_reportdata_index_config where report_table in(select report_table from base_report_info k where k.report_catgory=$S{reportType}))";
        return comnDao.findRows(sql, DataSourceProperty.PUB, params);

    }
}
