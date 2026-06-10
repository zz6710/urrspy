package com.kayak.bak.business.dao;

import com.kayak.bak.core.config.SourceConfig;
import com.kayak.bak.enums.SpecialColumnEnum;
import com.kayak.bak.model.dto.*;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.check.exception.DataValidateExecuteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class BakPhaseDao extends ComnDao {

    @Autowired
    protected DaoService daoService;

    /**
     * 获取归档数据
     * @param dto
     * @return
     * @throws Exception
     */
    public List<SqlRow> getTargetData(BakTaskDTO dto, Integer left, Integer right) throws Exception {
        String sql = "select * from "+dto.getTargetTable()+" where "+dto.getTargetField()+" >= $S{nextLeftDate} and "
                +dto.getTargetField()+" <= $S{nextRightDate} limit "+ left + ","  + right;
        return super.findRows(sql, SourceConfig.getDataSource(dto.getTargetDb()),dto);
    }

    /**
     * 获取待归档数据量
     * @param dto
     * @return
     * @throws Exception
     */
    public Integer getBakNum(BakTaskDTO dto) throws Exception {
        String sql = "select count(1) num from "+dto.getTargetTable()+" where "+dto.getTargetField()+" >= $S{nextLeftDate} and "+dto.getTargetField()+" <= $S{nextRightDate}";
        SqlRow ret = super.findRow(sql, SourceConfig.getDataSource(dto.getTargetDb()),dto);
        return ret.getInteger("num");
    }

    /**
     * 删除已归档数据
     * @param dto
     * @return
     * @throws Exception
     */
    public void deleteTargetData(BakTaskDTO dto) throws Exception {
        String sql = "delete from "+dto.getTargetTable()+" where "+dto.getTargetField()+" >= $S{nextLeftDate} and "+dto.getTargetField()+" <= $S{nextRightDate}";
        super.update(sql, SourceConfig.getDataSource(dto.getTargetDb()), dto);
    }

    /**
     * 归档完毕更新配置信息
     * @param dto
     * @return
     * @throws Exception
     */
    public void updateAfterBak(BakConfigDTO dto) throws Exception {
        String sql = "update sys_bak_config set next_left_date = $S{nextLeftDate}, next_date = $S{nextDate}," +
                "next_right_date = $S{nextRightDate},table_bak_num = $S{tableBakNum},last_date = $S{lastDate}," +
                "last_left_date = $S{lastLeftDate},last_right_date = $S{lastRightDate},update_time = $S{updateTime} " +
                "where id = $S{id}";
        super.update(sql, SourceConfig.BAK, dto);
    }


    /**
     * 查询需要执行的备份任务配置
     * @param dealDate
     * @return
     * @throws Exception
     */
    public List<SysBakConfigPO> getSysBakConfig(String dealDate) throws Exception {
        String sql = "select * from sys_bak_config where next_date = $S{deal_date}";
        return super.findRows(SysBakConfigPO.class, sql, SourceConfig.BAK, dealDate);
    }

    /**
     * 查询备份表集合
     * @param sysBakConfigPOS
     * @return
     * @throws Exception
     */
    public List<SysBakCollectionPO>  getSysBakCollection(List<SysBakConfigPO> sysBakConfigPOS) throws Exception {
        StringBuffer bakConfigIdStr = new StringBuffer();
        sysBakConfigPOS.stream().forEach(po ->
                bakConfigIdStr.append(po.getId() + "','")
        );
        bakConfigIdStr.delete(bakConfigIdStr.length() - 3, bakConfigIdStr.length());
        String sql = "select * from sys_bak_collection where (bak_config_id, bak_sort) in (" +
                "select bak_config_id,max(bak_sort) from sys_bak_collection where bak_config_id " +
                "in ('"+bakConfigIdStr+"') group by bak_config_id)";
        return super.findRows(SysBakCollectionPO.class, sql, SourceConfig.BAK, null);
    }

    /**
     * 创建归档仓库
     * @param dto
     * @throws Exception
     */
    public void createBakTable(BakCreateTableDTO dto) throws Exception {
        String sql = "CREATE TABLE "+dto.getBakTableName()+
                "("+ dto.getFields() +") ENGINE = InnoDB COMMENT = '"+ dto.getTbDesc() +"' ROW_FORMAT = Dynamic";
        try {
            super.update(sql, SourceConfig.BAK, null);
        } catch (SQLSyntaxErrorException e) {
            log.error("自动建表失败, 表名:" + dto.getBakTableName(), e);
            throw new Exception("自动建表失败, 表名:" + dto.getBakTableName(), e);
        }
    }

    /**
     * 新增仓库集合数据
     * @param dto
     * @throws Exception
     */
    public void addCollection(BakTaskDTO dto) throws Exception {
        String sql = "INSERT INTO sys_bak_collection(" +
                "bak_config_id, target_db, target_table, bak_sort, bak_table, bak_num, start_date, end_date, new_date, create_time) " +
                "VALUES " +
                "($S{bakConfigId}, $S{targetDb}, $S{targetTable}, $S{bakSort}, $S{bakTableName}, 0, $S{nextLeftDate}, $S{nextRightDate}, '', NOW()) ";
        super.update(sql, SourceConfig.BAK, dto);
    }

    /**
     * 查询表字段信息
     * @return
     */
    public List<BakFieldDTO> getFieldInfo(String db, String table) throws Exception {
        // 获取表信息
        Connection connection = super.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        // 获取表字段信息
        List<BakFieldDTO> colList = new ArrayList<>();
        ResultSet set = metaData.getColumns(db, null, table, "%");
        while (set.next()) {
            BakFieldDTO fieldDTO = new BakFieldDTO()
                    .setColName(set.getString("COLUMN_NAME"))
                    .setType(set.getString("TYPE_NAME"))
                    .setSize(set.getString("COLUMN_SIZE"))
                    .setData(set.getString("COLUMN_DEF"))
                    .setDigits(set.getString("DECIMAL_DIGITS"))
                    .setRemark(set.getString("REMARKS"));
            colList.add(fieldDTO);
        }
        return colList;
    }

    /**
     * 归档数据批量入库
     * @param rowList 字段对应值
     * @param fieldList 字段
     * @param sharding 数据源
     * @throws Exception
     */
    public void batchInsertBak(String sql, List<SqlRow> rowList, List<BakFieldDTO> fieldList, int sharding) throws Exception {
        daoService.doTrans(() -> {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            //字段值替换占位符
            for (SqlRow sqlRow : rowList) {
                for (int i = 0; i < fieldList.size(); i++) {
                    BakFieldDTO fieldDTO = fieldList.get(i);
                    ps.setObject(i + 1, sqlRow.get(fieldDTO.getColName()));
                }
                ps.addBatch();
            }
            try {
                ps.executeBatch();
                ps.clearBatch();
            } catch (Exception e) {
                throw new DataValidateExecuteException("数据归档批量入库异常执行异常: " + e.getMessage());
            } finally {
                ps.close();
            }
        }, sharding);
    }

}
