package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.*;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by daniel on 31/05/2017.
 */
@Repository
public class CheckDao extends ComnDao {
    public long countProcess(Map<String, Object> queryCriteria) {
        return 0;
    }

    public long countEntity(Entity entity) {
        return 0;
    }

    public long countContext(Context context) {
        return 0;
    }

    public long countForm(Form form) {
        return 0;
    }

    public long countCtx(Ctx ctx) {
        return 0;
    }

    public long countEnv(Env env) {
        return 0;
    }

    public long countButtonProcessMapping(ButtonProcessMapping ctx) {
        return 0;
    }
}
