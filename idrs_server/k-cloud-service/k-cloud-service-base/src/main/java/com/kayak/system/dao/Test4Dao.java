package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.model.Test4;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicReference;

@Repository
public class Test4Dao extends ComnDao {
    public SqlResult<Test4> find(SqlParam<Test4> params) throws Exception {
        params.setMakeSql(true);
        return super.findRows("select * from test4",params);
    }
    public int delete(String id) throws Exception {
        return super.update("DELETE FROM test4 WHERE id = $S{id}", id).getEffect();
    }
    public String add(SqlParam<Test4> param) throws Exception {
        AtomicReference<String> a= new AtomicReference<>("");
        doTrans(() -> {
            a.set(super.update("insert into test4(id,name) VALUES ($S{id}, $S{name})", param.getParams()).getAutoId());
        });
        return a.get();
    }
}
