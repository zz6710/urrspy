package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.Ctx;
import com.kayak.pms.opFlow.engine.entity.FieldMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 21/05/2017.
 */
@Repository
public class CtxDao extends ComnDao {
    public void save(Ctx context) {
    }

    public void remove(String id) {
    }

    public Ctx get(String id) {
        return null;
    }

    public List<Ctx> list(Map<String, Object> queryCriteria) {
        return null;
    }

    public void update(Ctx entity) {
    }

    public List<FieldMap> listField(String btnId) {
        return null;
    }
}
