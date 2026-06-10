package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.report.model.ExcelByTemplate;
import com.kayak.report.model.ReportConvert;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExcelByTemplateDao extends ComnDao {

    /**
     * 查询导出列表
     * @param reportId
     * @return
     * @throws Exception
     */
    public SqlResult<ExcelByTemplate> findExcelByTemplateConfig(SqlParam<ExcelByTemplate> params) throws Exception{
        String sql = "SELECT xx.id,  xx.row_start,  xx.column_start,  xx.system_table_name,ap.system_table_name as report_table, ap.export_table_id,CONCAT(ap.system_table_name_cn,'-',ap.system_table_name) as  table_name, " +
                "template_name, template_file_name, template_file_path, import_type, version, template_status, imp_usr, oss_file_path," +
                " date_format(str_to_date(concat(imp_date, ' ', imp_time), '%Y%m%d %H%i%s'),'%Y-%m-%d %H:%i:%s') as imp_date, imp_time\n" +
                "FROM import_template_manage  xx inner join app_table_info ap on ap.id = xx.system_table_name   and  export_template ='1' where  xx.id in (select  max(a.id)  from import_template_manage  a group  by  a.system_table_name)";
        if (Strings.isNotBlank(params.getModel().getSystemTableName())) {
            sql += " and xx.system_table_name = $S{systemTableName}";
        }
        sql += " order by ap.id asc";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }
}
