package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.Entity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 21/05/2017.
 */
@Repository
public class EntityDao extends ComnDao {
    public void saveEntity(Entity entity) {}

    public void removeEntity(String id) {}

    public Entity getEntity(String id) {
        return null;
    }

    public List<Entity> listEntity(Map<String, Object> queryCriteria) {
        return null;
    }

    public void updateEntity(Entity entity) {}

    public List<Entity> listEntityByIds(List<String> ids) {
        return null;
    }

}
