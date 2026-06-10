package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by daniel on 14/06/2017.
 */
@Repository("commonDao")
public class CommonDao extends ComnDao {
    /**
     * 获取列表, 用于获取多个用户
     *
     * @param sql
     * @return
     */
    public List<String> listBySql(@Param("sql") String sql) {
        return null;
    }

    /**
     * 执行获取返回一个字符串
     *
     * @param sql
     * @return
     */
    public String getBySqlRetStr(@Param("sql") String sql) {
        return null;
    }

    /**
     * 执行获取返回一个字符串
     *
     * @param sql
     * @return
     */
    public Integer getBySqlRetInteger(@Param("sql") String sql) {
        return null;
    }


    public void updateBySql(@Param("sql") String sql) {
    }
}
