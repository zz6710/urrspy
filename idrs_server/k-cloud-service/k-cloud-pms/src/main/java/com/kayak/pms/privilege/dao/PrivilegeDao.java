package com.kayak.pms.privilege.dao;

import java.util.List;


import com.kayak.base.dao.ComnDao;

import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;


@Repository(value = "PrivilegeDao")
public class PrivilegeDao extends ComnDao {

    public List<SqlRow> getColumn(String channel_code) throws Exception {
        return super.findRows("select distinct COLUMN_NAME from information_schema.COLUMNS where table_name = '"+channel_code+"' ");
    }

    public List<SqlRow> getRows(String sql) throws Exception {
        return super.findRows(sql);
    }

    public int runUpdate(String sql) throws Exception {
        return super.update(sql,"").getEffect();
    }

}
