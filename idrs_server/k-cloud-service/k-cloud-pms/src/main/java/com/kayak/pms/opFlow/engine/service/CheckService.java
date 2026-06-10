package com.kayak.pms.opFlow.engine.service;

import com.kayak.pms.opFlow.engine.dao.CheckDao;
import com.kayak.pms.opFlow.engine.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by daniel on 31/05/2017.
 */
@Service
public class CheckService {

    @Autowired
    private CheckDao checkDao;

    public long countProcess(Map<String, Object> queryCriteria) {
        return checkDao.countProcess(queryCriteria);
    }

    public long countEntity(Entity entity) {
        return checkDao.countEntity(entity);
    }

    public long countContext(Context context) {
        return checkDao.countContext(context);
    }

    public long countForm(Form form) {
        return checkDao.countForm(form);
    }

    public long countCtx(Ctx ctx) {
        return checkDao.countCtx(ctx);
    }

    public long countEnv(Env env) {
        return checkDao.countEnv(env);
    }

    public long countButtonProcessMapping(ButtonProcessMapping bpm) {
        return checkDao.countButtonProcessMapping(bpm);
    }
}
