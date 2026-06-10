package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.pms.opFlow.engine.entity.Surrogate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 07/06/2017.
 */
@Repository
public class SurrogateDao extends ComnDao {
    public void save(Surrogate surrogate) {}

    public void remove(String id) {}

    public void update(Surrogate surrogate) {}

    public Surrogate get(String id) {
        return null;
    }

    public List<Surrogate> list(Map<String, Object> queryCriteria) {
        return null;
    }

    /**
     * 查询当前用于可以代理的流程
     *
     * @param queryCriteria
     * @return
     */
    public List<Surrogate> listProcessIdAndOrignalUserId(Map<String, Object> queryCriteria) {
        return null;
    }

    public String getUserIdByUser(@Param("userName") String userName) {
        return null;
    }
}
