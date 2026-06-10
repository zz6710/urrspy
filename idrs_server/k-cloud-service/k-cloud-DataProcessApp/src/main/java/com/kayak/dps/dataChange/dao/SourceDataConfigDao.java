package com.kayak.dps.dataChange.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.dataChange.model.SourceDataConfigModel;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SourceDataConfigDao extends ComnDao {

    public UpdateResult addSourceDataConfigModel(SourceDataConfigModel params) throws Exception {
        return super.update("INSERT INTO `rem_sourcedata_config`(`TABLE_NAME`, `REMIND_FIELD`, `REMIND_TYPE`,`COMPARISON_RULES`, `RELATED_REPORT`, `TASK_GROUP`, `CRT_DT`, `FIELD_TYPE`, `OUT_DICT`,`UPD_DT`) VALUES ($S{tableName}, $S{remindField}, $S{remindType},$S{comparisonRules}, $S{relatedReport}, $S{taskGroup}, DATE_FORMAT(NOW(), '%Y%m%d'), $S{fieldType}, $S{outDict}, DATE_FORMAT(NOW(), '%Y%m%d')) ",
                DataSourceProperty.PUB,params);
    }

    public UpdateResult editSourceDataConfigModel(SqlParam<SourceDataConfigModel> params) throws Exception {
        return super.update("UPDATE `rem_sourcedata_config` set `TABLE_NAME` = $S{tableName}, `REMIND_FIELD` = $S{remindField}, `REMIND_TYPE` = $S{remindType}, `RELATED_REPORT` = $S{relatedReport}, `TASK_GROUP` = $S{taskGroup}, `UPD_DT` = DATE_FORMAT(NOW(), '%Y%m%d') where `id` = $S{id} ",
                DataSourceProperty.PUB,params.getModel());
    }

    public UpdateResult deleteSourceDataConfigModel(SqlParam<SourceDataConfigModel> params) throws Exception {
        return super.update("DELETE FROM `rem_sourcedata_config` where `table_name` = $S{tableName} ",
                DataSourceProperty.PUB,params.getModel());
    }

    public SqlResult<SourceDataConfigModel> findSourceDataConfigModel(SqlParam<SourceDataConfigModel> params) throws Exception {
        String sql = "select distinct t.TABLE_NAME,t1.port_name as tables_name, t.REMIND_TYPE, t.RELATED_REPORT, t.TASK_GROUP,t.COMPARISON_RULES, t.UPD_DT " +
                "from rem_sourcedata_config t " +
                "left join base_port_manage t1 on t.TABLE_NAME = t1.port_table " +
                "where 1=1";
        if(Strings.isNotBlank(params.getModel().getTableName())){
            sql += " and t.table_name = $S{tableName}";
        }
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public SqlResult<SourceDataConfigModel> findTables(SqlParam<SourceDataConfigModel> params) throws Exception {
        String sql = "select port_table as tables,port_name as tables_name from base_port_manage where port_type in ('5','7','8')";
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public SqlResult<SourceDataConfigModel> findRemindField(SqlParam<SourceDataConfigModel> params, String whereNK) throws Exception {
        String sql = "select field_code as remindfields,field_name as remindfields_name from base_port_field_manage " +
                "where port_code = (select port_code from base_port_manage where port_table = $S{tableName}) " +
                "and field_code not in ("+whereNK+"'DQRQ','CPZT','IS_EFFECTIVE','DEAL_DATE')" ;
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public SqlResult<SourceDataConfigModel> findRemindFieldAll(SqlParam<SourceDataConfigModel> params, String whereNK) throws Exception {
        String sql = "select field_code as remindfields,field_type from base_port_field_manage " +
                "where port_code = (select port_code from base_port_manage where port_table = $S{tableName}) " +
                "and field_code not in ("+whereNK+"'DQRQ','CPZT','IS_EFFECTIVE','DEAL_DATE')" ;
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public SqlResult<SourceDataConfigModel> findTablesForApp(SqlParam<SourceDataConfigModel> params) throws Exception {
        String sql = "select table_name as tables,table_comment as tables_name from information_schema.tables where TABLE_SCHEMA = (select database()) and table_name like 'app%'";
        return super.findRows(sql,DataSourceProperty.PUB,params);
    }

    public List<SqlRow> findReminName(String table) throws Exception {
        String sql = "select t.remind_field,t.field_type,t.out_dict  from rem_sourcedata_config t " +
                "where t.table_name = '"+table+"'";
        return super.findRows(sql,DataSourceProperty.PUB, (Object) null);
    }

    public SqlRow findReportName(String whererelatedReport) throws Exception {
        String sql = "select GROUP_CONCAT(TABLE_COMMENT) as report_name from information_schema.tables " +
                "where TABLE_NAME in ("+whererelatedReport+") and TABLE_SCHEMA = (select schema())";
        return super.findRow(sql,null);
    }

    public SqlResult<SourceDataConfigModel> findRemindFieldMsg(SqlParam<SourceDataConfigModel> params) throws Exception {
        String sql = "select t.FIELD_TYPE field_type from base_port_manage t1 left join base_port_field_manage t on t.PORT_CODE = t1.port_code where t1.port_table = $S{tableName} and t.FIELD_CODE = $S{remindField}";
        return super.findRows(sql,params);
    }

    public SqlResult<SourceDataConfigModel> findTaskGroup(SqlParam<SourceDataConfigModel> params) throws Exception {
        String sql = "SELECT task_group,task_group_name FROM kbatch_group_info where exec_task_type='9' ORDER BY should_exec_time";
        return super.findRows(sql,params);
    }

    public SqlRow findNatrueKey(String tablename) throws Exception {
        String sql = "select natural_key from rem_datanatural_key " +
                "where TABLE_NAME = '"+tablename+"'" ;
        return super.findRow(sql,null);
    }
}
