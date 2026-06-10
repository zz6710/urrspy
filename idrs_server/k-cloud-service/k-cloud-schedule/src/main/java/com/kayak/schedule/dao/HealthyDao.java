package com.kayak.schedule.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HealthyDao extends ComnDao {
    public List<SqlRow> findAll() throws Exception {
        String sql="select * from healthy";
       return super.findRows(sql);
    }
}
