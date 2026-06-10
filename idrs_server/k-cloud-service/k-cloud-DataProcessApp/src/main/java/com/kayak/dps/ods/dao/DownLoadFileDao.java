package com.kayak.dps.ods.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.model.DownLoadFileInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class DownLoadFileDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;
    @Value("${database.schemas}")
    private String schemas;

    public SqlResult<DownLoadFileInfo> findTableName(SqlParam<DownLoadFileInfo> params) throws Exception {
        String []schemaGroup =schemas.split(",");
        String newschemas=StringUtils.join(schemaGroup,"','");
        String sql = "select TABLE_NAME,TABLE_COMMENT from information_schema.TABLES where TABLE_SCHEMA in ('"+newschemas+"') and (TABLE_NAME LIKE '%$U{tableName}%' or TABLE_COMMENT LIKE '%$U{tableName}%')  and TABLE_TYPE != 'VIEW' ";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    public SqlResult<DownLoadFileInfo> findColumnName(SqlParam<DownLoadFileInfo> params) throws Exception {
        String []schemaGroup =schemas.split(",");
        String newschemas=StringUtils.join(schemaGroup,"','");
        String sql = "select COLUMN_NAME,COLUMN_COMMENT" +
                " from information_schema.`COLUMNS` c" +
                " where TABLE_SCHEMA in ('"+newschemas+"') and TABLE_NAME = '$U{tableName}'" +
                " order by ORDINAL_POSITION asc";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }
}


