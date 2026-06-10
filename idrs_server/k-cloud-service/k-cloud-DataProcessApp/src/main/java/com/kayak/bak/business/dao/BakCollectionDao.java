package com.kayak.bak.business.dao;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.core.config.SourceConfig;
import com.kayak.bak.enums.SpecialColumnEnum;
import com.kayak.bak.model.dto.BakFieldDTO;
import com.kayak.bak.model.dto.BakTaskDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Slf4j
@Repository
public class BakCollectionDao extends ComnDao {

    /**
     * 查询归档配置列表
     * @param params
     * @return
     */
    public SqlResult<SysBakCollectionPO> getBakCollectionList(SqlParam<SysBakCollectionPO> params) throws Exception {
        String sql = "select * from sys_bak_collection t where 1=1";

        if (StringUtils.isNotBlank(params.getModel().getBakConfigId())) {
            sql = sql + " and t.bak_config_id like '%" + params.getModel().getTargetDb() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getTargetDb())) {
            sql = sql + " and t.target_db like '%" + params.getModel().getTargetDb() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getTargetTable())) {
            sql = sql + " and t.target_table like '%" + params.getModel().getTargetTable() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getNewDate())) {
            sql = sql + " and t.new_date like '%" + params.getModel().getNewDate() + "%'";
        }
        return super.findRows(sql, SourceConfig.BAK, params);
    }

    /**
     * 新增校验指标信息
     * @param params
     * @return
     * @throws Exception
     */
    public void addBakCollection(SysBakCollectionPO params) throws Exception {
        String sql = "INSERT INTO sys_bak_collection (bak_config_id,target_db,target_table,bak_sort,bak_table,bak_num," +
                "create_time,update_time)" +
                " VALUES " +
                "($S{bakConfigId},$S{targetDb},$S{targetTable},$S{bakSort},$S{bakTable},0,$S{createTime}" +
                ",$S{updateTime})";
        super.update(sql, DataSourceProperty.BAK, params);
    }

    /**
     * 修改仓库表字段信息
     * @param po  仓库实体
     * @param fieldDTO  变更字段信息
     * @return
     * @throws Exception
     */
    public void alterBakField(SysBakCollectionPO po, BakFieldDTO fieldDTO) throws Exception {
        String size = " (" + fieldDTO.getSize() + ") ";
        if (ObjectUtil.isNotEmpty(fieldDTO.getDigits())) {
            //浮点数处理字符长度限制
            size = " (" + fieldDTO.getSize() + "," + fieldDTO.getDigits() + ") ";
        } else if (SpecialColumnEnum.containVal(fieldDTO.getType())) {
            //LONG_TEXT等类型字段有长度为空处理
            size = "";
        }
        String sql = "alter table "+ po.getBakTable() +" add COLUMN "+fieldDTO.getColName()+" "+
                fieldDTO.getType() + size + " COMMENT '" + fieldDTO.getRemark() + "'";
        super.update(sql, DataSourceProperty.BAK, null);
    }

    /**
     * 归档完毕更新集合表信息
     * @param dto
     * @return
     * @throws Exception
     */
    public void updateAfterCollection(BakTaskDTO dto, Integer size) throws Exception {
        String newDate = dto.getSysBakConfigPO().getNextDate();
        //更新仓库归档数量及日期
        String sql = "update sys_bak_collection set new_date = "+ newDate +", bak_num = (select count(1) from " +
                ""+ dto.getBakTableName() +"), end_date = $S{nextRightDate} where id = (select * from (select id " +
                "from sys_bak_collection where bak_config_id = $S{bakConfigId} order by bak_sort desc limit 1) t)";
        super.update(sql, SourceConfig.BAK, dto);
    }

    /**
     * 查询表数据量
     * source 数据源
     * table 表名
     * @return
     */
    public int getCountForDb(int source, String table) throws Exception {
        String sql = "select count(1) cnt from " + table;
        return Integer.parseInt(super.findRow(sql, source, new HashMap<>(0)).getString("cnt"));
    }

    /**
     *
     * @param params
     * @throws Exception
     */
    public void deleteBakCollection(SqlParam<SysBakCollectionPO> params) throws Exception {
        String sql = "delete from sys_bak_collection where id = $S{id}";
        super.update(sql, DataSourceProperty.BAK, params.getModel());
    }

    public void updateBakCollection(SqlParam<SysBakCollectionPO> params) throws Exception {
        String sql = "update sys_bak_collection t set " +
                "bak_config_id = $S{bakConfigId}," +
                "target_db = $S{targetDb}," +
                "target_table = $S{targetTable}, " +
                "bak_sort = $S{bakSort}," +
                "bak_table = $S{bakTable}," +
                "bak_num = $S{bakNum}," +
                "start_date = $S{startDate}," +
                "end_date = $S{endDate}," +
                "new_date = $S{newDate}" +
                "where t.id = $S{id}";
        super.update(sql, DataSourceProperty.BAK, params.getModel());
    }

    public UpdateResult updateBakCollectionBakNum(SysBakCollectionPO params) throws Exception {
        String sql = "update sys_bak_collection t set " +
                "bak_num = $S{bakNum}," +
                "where t.id = $S{id}";
        return super.update(sql, DataSourceProperty.BAK, params);
    }
}
