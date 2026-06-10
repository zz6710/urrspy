package com.kayak.bak.business.dao;

import com.kayak.bak.core.config.SourceConfig;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Slf4j
@Repository
public class BakDeleteDao extends ComnDao {

    public UpdateResult truncate(String bakTable) throws Exception {
        String sql = "truncate table " + bakTable;
        return super.update(sql, SourceConfig.BAK, new HashMap<>(0));
    }

    public UpdateResult delete(String bakTable, String targetField, String deleteStartDate, String deleteEndDate) throws Exception {
        String sql = "delete from " + bakTable + "where " + targetField + "between" + deleteStartDate + "and "+ deleteEndDate;
        return super.update(sql, SourceConfig.BAK, new HashMap<>(0));
    }

}
