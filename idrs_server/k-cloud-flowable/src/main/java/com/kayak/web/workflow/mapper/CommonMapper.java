package com.kayak.web.workflow.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yuanjinqiao
 */
@Repository("commonMapper")
public interface CommonMapper {
    /**
     * 获取列表, 用于获取多个用户
     *
     * @param sql
     * @return
     */
    List<String> listBySql(@Param("sql") String sql);

    /**
     * 执行获取返回一个字符串
     *
     * @param sql
     * @return
     */
    String getBySqlRetStr(@Param("sql") String sql);

    /**
     * 执行获取返回一个字符串
     *
     * @param sql
     * @return
     */
    Integer getBySqlRetInteger(@Param("sql") String sql);

    void updateBySql(@Param("sql") String sql);

    Object getBySqlRetObject(@Param("sql") String sql);

    List<Object> getBySqlRetList(@Param("sql") String sql);

}
