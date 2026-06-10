package com.kayak.pms.opFlow.engine.service;

import com.kayak.pms.opFlow.engine.dao.EntityDao;
import com.kayak.pms.opFlow.engine.entity.Entity;
import com.kayak.pms.opFlow.engine.entity.vo.BootstrapTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 21/05/2017.
 */
@Service
@Transactional
public class EntityService {

    @Autowired
    EntityDao entityDao;

    public void saveEntity(Entity entity) {
        entityDao.saveEntity(entity);
    }

    public void removeEntity(String entityName) {
        entityDao.removeEntity(entityName);
    }

    public Entity getEntity(String entityName) {
        return entityDao.getEntity(entityName);
    }

    public BootstrapTableVO<Entity> listEntity(Map<String, Object> queryCriteria) {
//        PageHelper.offsetPage((Integer) queryCriteria.get("offset"), (Integer) queryCriteria.get("limit"));
        List<Entity> allEntity = entityDao.listEntity(queryCriteria);
//        PageInfo page = new PageInfo<Entity>(allEntity);
        return new BootstrapTableVO<>(allEntity, allEntity.size());
    }

    public void updateEntity(Entity entity) {
        entityDao.updateEntity(entity);
    }

    public List<Entity> listSelect2() {
        return entityDao.listEntity(new HashMap<String, Object>());
    }
}
