package com.kayak.dps.export.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：导出文件dao
 *
 * @author zhaojie
 */
@Repository
public class ExportExcelDao extends ComnDao {

    public List<SqlRow> getTableComment (String tableName) throws Exception {
        String sqlStr = " select table_comment as tableComment , upper(table_schema) as tableSchema from information_schema.tables t  where table_name='"+tableName+"' and TABLE_TYPE != 'VIEW' limit 1";
        return super.findRows(sqlStr, DataSourceProperty.PUB);
    }

    public List<SqlRow> getCols(String tableName, String tableSchema) throws Exception {

        String sql = " select   COLUMN_NAME as col, COLUMN_COMMENT as colName from INFORMATION_SCHEMA.COLUMNS Where table_name = '"+ tableName +"' and table_schema ='"+tableSchema+"' order by ORDINAL_POSITION asc " ;
        return super.findRows(sql);
    }


    public String getDataCount (String sql ,String tableSchema) throws Exception {
        List<SqlRow> rowList = super.findRows(sql,  DataSourceProperty.getDataSource(tableSchema));
        return String.valueOf(rowList.get(0).get("count")).trim();
    }


    public List<SqlRow> getDatas(String sql ,String tableSchema) throws Exception {
        return super.findRows(sql, DataSourceProperty.getDataSource(tableSchema));
    }

    public List<SqlRow> getDictListByTableName(String tableName) throws Exception {
        String sql = " select bfd.field ,sdi.itemkey ,sdi.itemval  from base_field_dict bfd  left join sys_dict_item sdi  on bfd.dict = sdi.dict where bfd.table_name ='"+ tableName +"' order by field,itemkey   " ;
        return super.findRows(sql);
    }


}
