package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.Context;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 21/05/2017.
 */
@Repository
public class ContextDao extends ComnDao {
    public void saveContext(Context entity) {}

    public void removeContext(String id) {}

    public Context getContext(String id) {
        return null;
    }

    public List<Context> listContext(Map<String, Object> queryCriteria) {
        return null;
    }

    public void updateContext(Context entity) {}
}
