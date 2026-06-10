package com.kayak.pms.opFlow.engine.service;

import com.kayak.pms.opFlow.engine.dao.ContextDao;
import com.kayak.pms.opFlow.engine.dao.EntityDao;
import com.kayak.pms.opFlow.engine.entity.Context;
import com.kayak.pms.opFlow.engine.entity.Entity;
import com.kayak.pms.opFlow.engine.entity.vo.BootstrapTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 21/05/2017.
 */
@Service
@Transactional
public class ContextService {
    @Autowired
    ContextDao contextDao;
    @Autowired
    EntityDao entityDao;

    public void saveContext(Context context) {
        contextDao.saveContext(context);
    }

    public void removeContext(String id) {
        contextDao.removeContext(id);
    }

    public Context getContext(String id) {
        return contextDao.getContext(id);
    }

    public BootstrapTableVO<Context> listContext(Map<String, Object> queryCriteria) {
//        PageHelper.offsetPage((Integer) queryCriteria.get("offset"), (Integer) queryCriteria.get("limit"));
        List<Context> allContext = contextDao.listContext(queryCriteria);
//        PageInfo page = new PageInfo<Context>(allContext);
        return new BootstrapTableVO<Context>(allContext, allContext.size());
    }

    public void updateContext(Context context) {
        contextDao.updateContext(context);
    }

    public List<Context> listSelect2() {
        return contextDao.listContext(new HashMap<String, Object>());
    }

    public List<Entity> entitysTree(String entityIds) {
        return entityDao.listEntityByIds(Arrays.asList(entityIds.split(",")));
    }

    public List<Context> listContextSelect2() {
        return contextDao.listContext(new HashMap<String, Object>());
    }
}
