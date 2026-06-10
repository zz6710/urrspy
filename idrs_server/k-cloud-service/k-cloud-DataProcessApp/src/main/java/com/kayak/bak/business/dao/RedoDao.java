package com.kayak.bak.business.dao;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.core.config.SourceConfig;
import com.kayak.bak.core.config.SubConfig;
import com.kayak.bak.model.dto.BakFieldDTO;
import com.kayak.bak.model.dto.RedoDataDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


import javax.annotation.Resource;
import java.util.List;

/**
 * 已归档数据还原
 */
@Slf4j
@Repository
public class RedoDao extends ComnDao {

    @Resource
    private BakPhaseDao bakPhaseDao;

    /**
     * 查询已归档集合
     * @param dto
     * @return
     * @throws Exception
     */
    public List<SysBakCollectionPO> getBakCollection(RedoDataDTO dto) throws Exception {
        String sql = "select * from sys_bak_collection where target_db = $S{targetDb} and target_table = $S{targetTable}" +
                "and (($S{redoStartDate} between start_date and end_date) or ($S{redoEndDate} between start_date and end_date))";
        List<SysBakCollectionPO> poList = super.findRows(SysBakCollectionPO.class, sql, SourceConfig.BAK, dto);
        if (ObjectUtil.isEmpty(poList)) {
            throw new Exception("未找到对应归档仓库, bakConfigId:" + dto.getId());
        }
        return poList;
    }

    /**
     * 获取待还原数据
     * @param redoDataDTOList
     * @return
     * @throws Exception
     */
    public List<SqlRow> getRedoData(List<RedoDataDTO> redoDataDTOList) throws Exception {
        StringBuffer sql = new StringBuffer();
        redoDataDTOList.stream().forEach(dto -> {
            //拼接要查询的字段
            StringBuffer fieldStr = new StringBuffer();
            dto.getFieldDTOList().stream().forEach(o -> fieldStr.append(o.getColName() + ","));
            fieldStr.deleteCharAt(fieldStr.length() - 1);
            sql.append("select "+ fieldStr +" from "+ dto.getBakTable() +" where "+ dto.getTargetField() +" between '"
                    + dto.getRedoStartDate() +"' and '"+ dto.getRedoEndDate() +"' union all ");
        });
        sql.delete(sql.length() - 10, sql.length());
        //获取要还原的数据量
        SqlRow row = super.findRow("select count(1) num from (" + sql.toString() + ") t", SourceConfig.BAK, null);
        if (row.getInteger("num") > SubConfig.MAX_REDO_NUM) {
            throw new Exception("超出还原数据量限制");
        }
        //查询待还原数据
        List<SqlRow> sqlRowList = super.findRows(sql.toString(), SourceConfig.BAK);
        if (ObjectUtil.isEmpty(sqlRowList)) {
            throw new Exception("待还原数据为空");
        }
        return sqlRowList;
    }

    /**
     * 入库还原数据
     *
     * @param sql 批量入库sql
     * @param rowList 入库数据列表
     * @param fieldList 待入库字段列表
     * @param sharding 数据源标识
     * @return
     * @throws Exception
     */
    public void addRedoData(String sql, List<SqlRow> rowList, List<BakFieldDTO> fieldList, int sharding) throws Exception {
        bakPhaseDao.batchInsertBak(sql, rowList, fieldList, sharding);
    }
}
