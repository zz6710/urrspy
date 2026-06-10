package com.kayak.bak.business.dao;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.core.config.SourceConfig;
import com.kayak.bak.model.dto.BakFieldDTO;
import com.kayak.bak.model.dto.SourceTableDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class BakConfigDao extends ComnDao {

    /**
     * 查询归档配置信息
     * @param bakConfigId
     * @return
     * @throws Exception
     */
    public SysBakConfigPO getBakConfig(String bakConfigId) throws Exception {
        String sql = "select * from sys_bak_config where id = $S{bakConfigId}";
        SysBakConfigPO retPO = super.findRow(SysBakConfigPO.class, sql, SourceConfig.BAK, bakConfigId);
        if (ObjectUtil.isNull(retPO)) {
            throw new Exception("未找到对应归档配置, id:" + bakConfigId);
        }
        return retPO;
    }

    public SysBakConfigPO getBakConfigByTableName(String targetTable) throws Exception {
        String sql = "select * from sys_bak_config where id = $S{targetTable}";
        SysBakConfigPO retPO = super.findRow(SysBakConfigPO.class, sql, SourceConfig.BAK, targetTable);
        if (ObjectUtil.isNull(retPO)) {
            throw new Exception("未找到对应归档配置, target_table:" + targetTable);
        }
        return retPO;
    }

    /**
     * 查询归档配置列表
     * @param params
     * @return
     */
    public SqlResult<SysBakConfigPO> getBakConfigList(SqlParam<SysBakConfigPO> params) throws Exception {
        String sql = "select * from sys_bak_config t where 1=1";

        if (StringUtils.isNotBlank(params.getModel().getTargetDb())) {
            sql = sql + " and t.target_db like '%" + params.getModel().getTargetDb() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getTargetTable())) {
            sql = sql + " and t.target_table like '%" + params.getModel().getTargetTable() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getType())) {
            sql = sql + " and t.type = " + params.getModel().getType();
        }
        if (StringUtils.isNotBlank(params.getModel().getAutoTable())) {
            sql = sql + " and t.auto_type = " + params.getModel().getAutoTable();
        }
        if (StringUtils.isNotBlank(params.getModel().getSourceData())) {
            sql = sql + " and t.source_data = " + params.getModel().getSourceData();
        }
        if (StringUtils.isNotBlank(params.getModel().getDeleteStartDate())) {
            sql = sql + " and t.delete_start_date = " + params.getModel().getSourceData();
        }
        if (StringUtils.isNotBlank(params.getModel().getDeleteEndDate())) {
            sql = sql + " and t.delete_end_date = " + params.getModel().getSourceData();
        }
        sql += " order by update_time desc";
        return super.findRows(sql, SourceConfig.BAK, params);
    }

    /**
     * 删除校验指标信息
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteBakConfig(SqlParam<SysBakConfigPO> params) throws Exception {
        String sql = "delete from sys_bak_config where id = $S{id}";
        super.update(sql, DataSourceProperty.BAK, params.getModel());
    }

    /**
     * 修改校验指标信息
     * @param params
     * @return
     * @throws Exception
     */
    public void updateBakConfig(SysBakConfigPO params) throws Exception {
        String sql = "update sys_bak_config t set " +
                "target_field = $S{targetField}," +
                "type = $S{type}," +
                "auto_table = $S{autoTable}, " +
                "frequency = $S{frequency}," +
                "threshold = $S{threshold}," +
                "source_data = $S{sourceData}," +
                "field_change = $S{fieldChange}," +
                "next_date = $S{nextDate}," +
                "next_left_date = $S{nextLeftDate}," +
                "next_right_date = $S{nextRightDate}, " +
                "delete_start_date = $S{deleteStartDate}, " +
                "delete_end_date = $S{deleteEndDate}, " +
                "delete_type = $S{deleteType}, " +
                "delete_frequency = $S{deleteFrequency} " +
                "where t.id = $S{id}";
        super.update(sql, DataSourceProperty.BAK, params);
    }

    /**
     * 新增校验指标信息
     * @param params
     * @return
     * @throws Exception
     */
    public void addBakConfig(SysBakConfigPO params) throws Exception {
        String sql = "INSERT INTO sys_bak_config (target_db,target_table,target_field,table_bak_num,type," +
                "auto_table,frequency,threshold,source_data,field_change,begin_date,last_date,next_date,last_left_date,last_right_date,next_left_date," +
                "next_right_date,create_time,update_time,delete_start_date,delete_end_date,delete_type,delete_frequency)" +
                " VALUES " +
                "($S{targetDb},$S{targetTable},$S{targetField},$S{tableBakNum},$S{type},$S{autoTable}," +
                "$S{frequency},$S{threshold},$S{sourceData},$S{fieldChange},$S{beginDate},$S{lastDate},$S{nextDate},$S{lastLeftDate},$S{lastRightDate}," +
                "$S{nextLeftDate},$S{nextRightDate},$S{createTime},$S{updateTime},$S{deleteStartDate},$S{deleteEndDate},$S{deleteType},$S{deleteFrequency})";
        super.update(sql, DataSourceProperty.BAK, params);
    }

    /**
     * 查询归档集合列表
     * @param params
     * @return
     */
    public List<SysBakCollectionPO> getBakCollectionList(SqlParam<SysBakConfigPO> params) throws Exception {
        String sql = "select * from sys_bak_collection t where bak_config_id = $S{id} order by sort bak_sort limit 1";
        return super.findRows(SysBakCollectionPO.class, sql, SourceConfig.BAK, params.getModel().getId());
    }

    /**
     * 查询表数据量
     * source 数据源
     * table 表名
     * @return
     */
    public int getCountForDb(int source, String table) throws Exception {
        String sql = "select count(1) cnt from " + table;
        return Integer.valueOf(super.findRow(sql, source).getString("cnt"));
    }

    /**
     * 查询表列表
     * @param db
     * @return
     * @throws Exception
     */
    public List<SourceTableDTO> getTableList(String dbName) throws Exception {
        // 获取表信息
        Connection connection = super.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        // 获取表字段信息
        List<SourceTableDTO> tbList = new ArrayList<>();
        ResultSet set = metaData.getTables(dbName, null, "%",null);
        while (set.next()) {
            SourceTableDTO sourceTableDTO = new SourceTableDTO()
                    .setTableName(set.getString("TABLE_NAME"));
            tbList.add(sourceTableDTO);
        }
        return tbList;
    }
}
