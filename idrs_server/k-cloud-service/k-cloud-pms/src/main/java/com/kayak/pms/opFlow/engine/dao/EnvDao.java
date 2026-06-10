package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.Env;
import com.kayak.pms.opFlow.engine.entity.SelectEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 21/05/2017.
 */
@Repository
public class EnvDao extends ComnDao {
    public void save(Env env) {}

    public void update(Env env) {}

    public void remove(String id) {}

    public Env get(String id) {
        return null;
    }

    public List<Env> list(Map<String, Object> queryCriteria) {
        return null;
    }

    public List<SelectEntity> listEnvSelect2() {
        return null;
    }
}
