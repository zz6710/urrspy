package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.EnvItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by daniel on 21/05/2017.
 */
@Repository("envItemDao")
public class EnvItemDao extends ComnDao {
    public void save(EnvItem env) {}

    public void remove(String id) {}

    public List<EnvItem> list() {
        return null;
    }

    public List<EnvItem> listById(String id) {
        return null;
    }

    public String getSqlByItemKey(@Param("processId") String processId, @Param("itemKey") String itemKey) {
        return null;
    }

}
