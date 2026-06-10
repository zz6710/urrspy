package com.kayak.pms.opFlow.engine.service;

import com.kayak.pms.opFlow.engine.dao.EnvDao;
import com.kayak.pms.opFlow.engine.dao.EnvItemDao;
import com.kayak.pms.opFlow.engine.entity.Env;
import com.kayak.pms.opFlow.engine.entity.EnvItem;
import com.kayak.pms.opFlow.engine.entity.SelectEntity;
import com.kayak.pms.opFlow.engine.entity.vo.BootstrapTableVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 27/06/2017.
 */
@Service
@Transactional
public class EnvService {

    @Autowired
    EnvDao envDao;

    @Autowired
    EnvItemDao envItemDao;

    public void saveEnv(Env env) {
        envDao.save(env);
    }

    public void getEnv(String id) {
        envDao.get(id);
    }

    public void updateEnv(Env env) {
        envDao.update(env);
    }

    public void removeEnv(String id) {
        envDao.remove(id);
    }

    public void removeEnvItem(String id) {
        envItemDao.remove(id);
    }

    public void saveEnvItem(EnvItem envItem) {
        envItemDao.save(envItem);
    }

    public Env get(String name) {
        return envDao.get(name);
    }

    public BootstrapTableVO<Env> list(Map<String, Object> queryCriteria) {
//        PageHelper.offsetPage((Integer) queryCriteria.get("offset"), (Integer) queryCriteria.get("limit"));
        List<Env> allCtx = envDao.list(queryCriteria);
//        PageInfo page = new PageInfo<Env>(allCtx);
        return new BootstrapTableVO<>(allCtx, allCtx.size());
    }

    public List<EnvItem> listEnvItem() {
        List<EnvItem> envItems = envItemDao.list();
        return envItems;
    }

    public List<EnvItem> listEnvItemById(String id) {
        List<EnvItem> envItems = envItemDao.listById(id);
        return envItems;
    }

    public List<SelectEntity> listEnvSelect2() {
        return envDao.listEnvSelect2();
    }
}
