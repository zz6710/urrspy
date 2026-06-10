package com.kayak.rpt.config.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.rpt.config.model.ReportValidationIndexModel;
import com.kayak.rpt.validate.model.ReportValidationModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReportDataValidationIndexDao extends ComnDao {

    /**
     * 查询校验指标信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<ReportValidationIndexModel> queryReportValidationIndexConfig(SqlParam<ReportValidationIndexModel> params) throws Exception {
        String sql = "select r.index_code, r.index_name, b.table_name, r.report_table, r.row_num, r.column_num, r.row_name, r.list_name, r.index_type, " +
                     "       r.express, r.allow_deviation, r.index_detail, r.correct_prompt, r.error_prompt, r.remark, r.is_effect,r.index_rule, r.relation_tables " +
                     "  from base_reportdata_index_config r " +
                     "  join base_report_info b on b.report_table = r.report_table " +
                     " where 1=1 ";
        if (StringUtils.isNotBlank(params.getModel().getReportTable())) {
            sql = sql + " and r.report_table like '%" + params.getModel().getReportTable() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getIndexCode())) {
            sql = sql + " and r.index_code like '%" + params.getModel().getIndexCode() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getIndexName())) {
            sql = sql + " and r.index_name like '%" + params.getModel().getIndexName() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getIndexType())) {
            sql = sql + " and r.index_type = '"+params.getModel().getIndexType()+"' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getRowName())) {
            sql = sql + " and r.row_name like '%"+params.getModel().getRowName()+"%' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getListName())) {
            sql = sql + " and r.list_name like '%"+params.getModel().getListName()+"%' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getIsEffect())) {
            sql = sql + " and r.is_effect = '"+params.getModel().getIsEffect()+"' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getIndexRule())) {
            sql = sql + " and r.index_rule = '"+params.getModel().getIndexRule()+"' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportType())) {
            sql = sql + " and b.report_catgory = '"+params.getModel().getReportType()+"' ";
        }
        sql += " order by r.report_table,r.index_code ";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 删除校验指标信息
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteReportDataIndex(Map<String, Object> params) throws Exception {
        String sql = "delete from base_reportdata_index_config where index_code = $S{indexCode}";
        super.update(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 修改校验指标信息
     * @param params
     * @return
     * @throws Exception
     */
    public void updateReportDataIndex(Map<String, Object> params) throws Exception {
        String sql = "update base_reportdata_index_config r " +
                     "   set r.report_table    = $S{reportTable}," +
                     "       r.row_num         = $S{rowNum}," +
                     "       r.column_num      = $S{columnNum}," +
                     "       r.row_name        = $S{rowName}," +
                     "       r.list_name       = $S{listName}," +
                     "       r.index_type      = $S{indexType}," +
                     "       r.index_name      = $S{indexName}," +
                     "       r.express         = $S{express}," +
                     "       r.allow_deviation = $D{allowDeviation}," +
                     "       r.index_detail    = $S{indexDetail}," +
                     "       r.correct_prompt  = $S{correctPrompt}," +
                     "       r.error_prompt    = $S{errorPrompt}," +
                     "       r.remark          = $S{remark}," +
                     "       r.relation_tables          = $S{relationTables}" +
                     " where r.index_code      = $S{indexCode}";
        super.update(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 新增校验指标信息
     * @param params
     * @return
     * @throws Exception
     */
    public void addReportDataIndex(Map<String, Object> params) throws Exception {
        String sql = "insert into  base_reportdata_index_config (index_code, report_table, row_num, column_num, row_name, list_name, index_type, express, index_detail, " +
                     "correct_prompt, error_prompt, remark, allow_deviation, relation_tables,is_effect,index_name) " +
                     "values ($S{indexCode}, $S{reportTable}, $S{rowNum}, $S{columnNum}, $S{rowName}, $S{listName}, $S{indexType}, $S{express}, $S{indexDetail}, " +
                     "$S{correctPrompt}, $S{errorPrompt}, $S{remark}, $D{allowDeviation}, $S{relationTables}, '01', $S{indexName})";
        super.update(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 获取报表数据字典
     * @return
     * @throws Exception
     */
    public List<SqlRow> getReportTableDict () throws Exception {
        String sql = "SELECT report_table AS `value`, table_name AS `label` FROM base_report_info ";
        return super.findRows(sql, DataSourceProperty.PUB);
    }

    /**
     * 指标状态失效
     * @param params
     * @return
     * @throws Exception
     */
    public int stopIndexStatus(SqlParam<ReportValidationIndexModel> params) throws Exception {
        return super.update("UPDATE base_reportdata_index_config SET is_effect='02' WHERE index_code = $S{indexCode}", DataSourceProperty.PUB, params.getParams()).getEffect();
    }

    /**
     * 指标状态生效
     * @param params
     * @return
     * @throws Exception
     */
    public int recoverIndexStatus(SqlParam<ReportValidationIndexModel> params) throws Exception {
        return super.update("UPDATE base_reportdata_index_config SET is_effect='01' WHERE index_code = $S{indexCode}", DataSourceProperty.PUB, params.getParams()).getEffect();
    }

    /**
     * 查询指标代码
     * @return
     * @throws Exception
     */
    public List<SqlRow> queryReportValidationIndexConfigByIndex(Map<String, Object> params) throws Exception {
        String sql = "select index_code " +
                "       from base_reportdata_index_config " +
                "      where index_code = '" + params.get("indexCode") + "'";
        return super.findRows(sql, DataSourceProperty.PUB);
    }
}
