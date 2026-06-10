package com.kayak.pms.opFlow.engine.service;

import com.kayak.pms.opFlow.engine.dao.CtxDao;
import com.kayak.pms.opFlow.engine.entity.Ctx;
import com.kayak.pms.opFlow.engine.entity.FieldMap;
import com.kayak.pms.opFlow.engine.entity.vo.BootstrapTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 27/06/2017.
 */
@Service
@Transactional
public class CtxService {
    @Autowired
    CtxDao ctxDao;

    public void save(Ctx ctx) {
        ctxDao.save(ctx);
    }

    public void remove(String id) {
        ctxDao.remove(id);
    }

    public Ctx get(String id) {
        return ctxDao.get(id);
    }

    public BootstrapTableVO<Ctx> list(Map<String, Object> queryCriteria) {
//        PageHelper.offsetPage((Integer) queryCriteria.get("offset"), (Integer) queryCriteria.get("limit"));
        List<Ctx> allCtx = ctxDao.list(queryCriteria);
//        PageInfo page = new PageInfo<Ctx>(allCtx);
        return new BootstrapTableVO<Ctx>(allCtx, allCtx.size());
    }

    public void update(Ctx ctx) {
        ctxDao.update(ctx);

    }

    public List<FieldMap> fieldList(String btnId) {
        return ctxDao.listField(btnId);
    }

    public List<Ctx> listCtxSelect2() {
        return  ctxDao.list(new HashMap<String, Object>());
    }
}
