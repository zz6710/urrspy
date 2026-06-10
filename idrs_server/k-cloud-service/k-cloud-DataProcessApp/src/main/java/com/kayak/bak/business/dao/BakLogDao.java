package com.kayak.bak.business.dao;

import com.kayak.bak.core.config.SourceConfig;
import com.kayak.bak.model.dto.BakLogDTO;
import com.kayak.bak.model.po.SysBakLogPO;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * 归档记录
 */
@Slf4j
@Repository
public class BakLogDao extends ComnDao {

    /**
     * 记录归档操作信息
     * @param dto
     */
    public void addRecordLog(BakLogDTO dto) throws Exception {
        String sql = "insert into sys_bak_log (bak_config_id,operate_desc,type,operate_date,duration,create_time,update_time) values (" +
                "$S{bakConfigId},$S{operateDesc},$S{type},$S{operateDate},$S{duration},$S{createTime},$S{updateTime})";

        super.update(sql, SourceConfig.BAK, dto);
    }

    /**
     * 查询记录列表
     * @param params
     * @return
     */
    public SqlResult<SysBakLogPO> getBakLogList(SqlParam<SysBakLogPO> params) throws Exception {
        String sql = "select * from sys_bak_log t where 1=1";

        if (StringUtils.isNotBlank(params.getModel().getOperateDesc())) {
            sql += " and t.operate_desc like '%" + params.getModel().getOperateDesc() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getType())) {
            sql += " and t.type = " + params.getModel().getType();
        }
        if (StringUtils.isNotBlank(params.getModel().getOperateDate())) {
            sql += " and t.operate_date like " + params.getModel().getOperateDate();
        }
        sql += " order by create_time desc";
        return super.findRows(sql, SourceConfig.BAK, params);
    }

    /**
     * 删除归档相关操作记录
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteBakLog(SqlParam<SysBakLogPO> params) throws Exception {
        String sql = "delete from sys_bak_config where id = $S{id}";
        super.update(sql, DataSourceProperty.BAK, params.getModel());
    }
}
