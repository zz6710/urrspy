package com.kayak.dps.dataChange.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.dataChange.model.DataNaturalKeyModel;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DataNarutalKeyDao extends ComnDao {

    public UpdateResult addDataNarutalKeyModel(DataNaturalKeyModel params) throws Exception {
        //需要删除当前提醒字段中的数据
        super.update("delete from `rem_sourcedata_config` where `TABLE_NAME` = $S{tableName} and `REMIND_FIELD` = upper($S{naturalKey})",
                DataSourceProperty.PUB,params);
        return super.update("INSERT INTO `rem_datanatural_key`(`HIERARCHY`, `TABLE_NAME`, `NATURAL_KEY`,`OUT_DICT`, `STANDARD_KEY`, `CRT_DT`, `UPD_DT`) VALUES ($S{hierarchy}, $S{tableName}, $S{naturalKey}, $S{outDict}, $S{standardKey},  DATE_FORMAT(NOW(), '%Y%m%d'), DATE_FORMAT(NOW(), '%Y%m%d')) ",
                DataSourceProperty.PUB,params);
    }

    public SqlResult<DataNaturalKeyModel> findDataNarutalKeyModel(SqlParam<DataNaturalKeyModel> params) throws Exception {
        String sql = " select distinct a.hierarchy,a.table_name,t1.TABLE_COMMENT as tables_name,a.upd_dt " +
                " from rem_datanatural_key a " +
                " left join information_schema.tables t1 on a.TABLE_NAME =t1.TABLE_NAME and t1.TABLE_SCHEMA = (select SCHEMA()) " +
                " where 1=1 ";
        if (Strings.isNotBlank(params.getModel().getNaturalKey())) {
            sql += " and a.natural_key like  '%$U{naturalKey}%' ";
        }
        if (Strings.isNotBlank(params.getModel().getTableName())) {
            sql += " and a.table_name =  $S{tableName} ";
        }
        if (Strings.isNotBlank(params.getModel().getHierarchy())) {
            sql += " and a.hierarchy =  $S{hierarchy} ";
        }
        return super.findRows(sql,
                DataSourceProperty.PUB, params);
    }

    public UpdateResult deleteDataNarutalKeyModel(SqlParam<DataNaturalKeyModel> params) throws Exception {
        String sql = " delete from rem_datanatural_key where table_name = $S{tableName}";
        return super.update(sql, DataSourceProperty.PUB, params.getModel());
    }

    public UpdateResult editDataNarutalKeyModel(SqlParam<DataNaturalKeyModel> params) throws Exception {
        return super.update("update rem_datanatural_key set hierarchy = $S{hierarchy},table_name = $S{tableName},natural_key = $S{naturalKey},standard_key = $S{standardKey},upd_dt =  DATE_FORMAT(NOW(), '%Y%m%d')  where id = $S{id}",
                DataSourceProperty.PUB, params.getModel());
    }

    public SqlResult<DataNaturalKeyModel> findTables(SqlParam<DataNaturalKeyModel> params) throws Exception {
        String sql = "select table_name as tables,table_comment as tables_name from information_schema.tables where TABLE_SCHEMA = (select database()) ";
        String flag = "and table_name like ";
        if (Strings.isNotBlank(params.getModel().getHierarchy())) {
            switch (params.getModel().getHierarchy()){
                case "01": flag += "'%stg%'";break;
                case "02": flag += "'%ods%'";break;
                case "03": flag += "'%dwd%'";break;
                case "04": flag += "'%dws%'";break;
                case "05": flag += "'%app%'";break;
                default:flag = "'% %'";
            }
            sql+=flag;
        }
        return super.findRows(sql,
                DataSourceProperty.PUB, params);
    }

    public SqlResult<DataNaturalKeyModel> findNaturalKey(SqlParam<DataNaturalKeyModel> params) throws Exception {
        String sql = "select field_code as natural_key,field_name as natural_key_name from base_port_field_manage " +
                "where port_code = (select port_code from base_port_manage where port_table = $S{tableName}) " +
                "and field_code not in ('DQRQ','CPZT','IS_EFFECTIVE','DEAL_DATE')";
        return super.findRows(sql,
                DataSourceProperty.PUB, params);
    }

    public List<SqlRow> findNaturalKeys(String tableName) throws Exception {
        String sql = " select a.natural_key,a.standard_key,a.out_dict " +
                " from rem_datanatural_key a where a.table_name = '"+tableName+"'";
        return super.findRows(sql, (Object) null);
    }
}
