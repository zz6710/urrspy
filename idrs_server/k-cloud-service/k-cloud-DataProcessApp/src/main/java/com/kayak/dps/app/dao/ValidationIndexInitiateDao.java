package com.kayak.dps.app.dao;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.app.model.CheckIndexModel;
import com.kayak.dps.app.model.ValidationIndexTempModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ValidationIndexInitiateDao extends ComnDao {

    @Autowired
    protected ComnDao comnDao;

    /**
     * 根据校验项目类型获取校验指标对象返回
     * @return
     * @throws Exception
     */
    public List<ValidationIndexTempModel> getValidationIndexByIndexType(Map<String, Object> params) throws Exception {
        List<ValidationIndexTempModel> indexList = new ArrayList<>();
        String sql = "select * from temp_validate_index_config c " +
                " where c.report_table = $S{report_table} " +
                " order by id,report_table";
        indexList = comnDao.findRows(ValidationIndexTempModel.class, sql, DataSourceProperty.PUB, params);
        return indexList;
    }

    public List<String> getInvalidateTableList() throws Exception {
        List<String> tbList = new ArrayList<>();
        String sql = "SELECT report_table FROM temp_validate_index_config GROUP BY report_table";
        List<SqlRow> reportTableListRes = comnDao.findRows(sql, DataSourceProperty.PUB);
        if (reportTableListRes.size() > 0) {
            for(SqlRow tbNameRes : reportTableListRes){
                tbList.add(tbNameRes.getString("report_table"));
            }
        }
        return tbList;
    }

    /**
     * 根据行或列名获取
     * @param type 字段类型: R-行 C-列
     * @param project_type 校验项目类型:1-一维/2-二维
     * @param code 校验字段代码
     * @return String[2] 0-num 1-name
     * @throws Exception
     */
    public String[] getRowOrColumnNumByName (String type, String project_type, String code, String report_table) throws Exception {
        String[] str = new String[2];
        String sql = "";

        if ("r".equalsIgnoreCase(type)) {
            if ("1".equals(project_type)) {
                return null;
            } else if ("2".equals(project_type)) {
                sql = "SELECT field_index,field_name FROM base_report_column_info WHERE (field_type = 'R' or field_type = 'r') AND field_code = '" + code + "' AND report_table = '" + report_table + "' ";
            }
        } else if ("c".equalsIgnoreCase(type)) {
            sql = "SELECT field_index,field_name FROM base_report_column_info WHERE (field_type = 'C' or field_type = 'c') AND field_code = '" + code + "' AND report_table = '" + report_table + "' ";
        }

        SqlRow sqlRes = comnDao.findRow(sql, DataSourceProperty.PUB);
        if (sqlRes != null) {
            str[0] = sqlRes.getString("field_index");
            str[1] = sqlRes.getString("field_name");
        } else {
            return null;
        }

        return str;
    }

    /**
     * 根据报表名称查询报表表名
     * @param table_name
     * @return
     * @throws Exception
     */
    public String getReportTableByName (String table_name) throws Exception {
        String report_table = "";
        String sql = "select report_table from base_report_info r where table_name = '" + table_name + "'";
        SqlRow sqlRes = comnDao.findRow(sql, DataSourceProperty.PUB, null);
        if (sqlRes != null) {
            report_table = sqlRes.getString("report_table");
        }
        return report_table;
    }

    /**
     * 查询表字段所在列数
     * @param report_table
     * @param field_name
     * @return
     * @throws Exception
     */
    public String getColumnIndexByName (String report_table, String field_name) throws Exception {
        String index = "";
        String sql = "SELECT field_index FROM base_report_column_info WHERE report_table = '"+report_table+"' AND field_name = '"+field_name+"' AND field_type = 'C'";
        SqlRow sqlRes = comnDao.findRow(sql, DataSourceProperty.PUB, null);
        if (sqlRes != null) {
            index = sqlRes.getString("field_index");
        }
        return  index;
    }


    /**
     * 转换后的指标数据入库
     * @param checkIndexModel 校验指标对象
     * @throws Exception
     */
    public void putIndexIn (CheckIndexModel checkIndexModel) throws Exception {
        Map<String, Object> params = BeanUtil.beanToMap(checkIndexModel);

        String delSql = "delete from base_reportdata_index_config where index_code = $S{indexCode}";
        comnDao.update(delSql, DataSourceProperty.PUB, params);

        String sql = "insert into base_reportdata_index_config (index_code, report_table, row_num, column_num, row_name, list_name, index_type, " +
                     "       express, allow_deviation, index_detail, correct_prompt, error_prompt, remark) " +
                     "values ($S{indexCode}, $S{reportTable}, $S{rowNum}, $S{columnNum}, $S{rowName}, $S{listName}, $S{indexType}, " +
                     "       $S{express}, $S{allowDeviation}, $S{indexDetail}, $S{correctPrompt}, $S{errorPrompt}, $S{remark})";
        comnDao.update(sql, DataSourceProperty.PUB, params);
    }


    /**
     * 更新指标表达式
     * @param index_code 指标代码
     * @param expression 表达式
     * @throws Exception
     */
    public void updateIndexExpress (String index_code, String expression) throws Exception {

        String sql = "update base_reportdata_index_config " +
                     "   set express = '" + expression + "' " +
                     " where index_code = '" + index_code +"' ";
        comnDao.update(sql, DataSourceProperty.PUB, null);
    }


    public String getRowIndexByName (String report_table, String field_name, String type) throws Exception {
        String index = "";
        String sql = "SELECT field_index FROM base_report_column_info WHERE report_table = '"+report_table+"' AND field_name = '"+field_name+"' AND field_type = '"+type+"'";
        SqlRow sqlRes = comnDao.findRow(sql, DataSourceProperty.PUB, null);
        if (sqlRes != null) {
            index = sqlRes.getString("field_index");
        }
        return  index;
    }

    public String getColumnIndexByCode (String report_table, String field_code, String type) throws Exception {
        String index = "";
        String sql = "SELECT field_index FROM base_report_column_info WHERE report_table = '"+report_table+"' AND field_code = '"+field_code+"' AND field_type = '"+type+"'";
        SqlRow sqlRes = comnDao.findRow(sql, DataSourceProperty.PUB, null);
        if (sqlRes != null) {
            index = sqlRes.getString("field_index");
        }
        return  index;
    }

}
