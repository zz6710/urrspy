package com.kayak.privilege.dao;

import java.util.List;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

/**
 * 描述：执行脚本 dao
 * @author zhaojie
 * @version 1.0
 * @date 2021/5/15 09:32
 */
@Repository
public class PrivilegeDao extends ComnDao {

   /**描述：执行select查询语句*/
    public List<SqlRow> getRows(String str) throws Exception {
        String startsql = "select * from (";
        String endsql = ") as pi limit 0,200";
        if (str.endsWith("all") || str.endsWith("all;")) {
            startsql = str.replaceAll("all[;]?$", "");
        } else {
            startsql = startsql + str + endsql;
        }
        return super.findRows(startsql);
    }
    /**描述：执行update、delete语句*/
    public int runUpdate(String sql) throws Exception {
        return super.update(sql,"").getEffect();
    }

}
