package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class JXSystemPushingDataGenDao extends ComnDao {

    @Autowired
    protected DaoService daoService;
    @Autowired
    protected ComnDao comnDao;


    /**
     * 根据报送报表存储数据的表名获取执行语句逻辑
     * 对数据进行加工处理的方法
     * @param params 清算任务id及日期
     * @return
     * @throws Exception
     */
    public List<String> getPushDatalistByTaskId(Map<String, Object> params) throws Exception {
        List<String> strList = new ArrayList<>();
        String SqlStr = getSqlStrByExeId(params);
        List<SqlRow> sqlRows = comnDao.findRows(SqlStr, DataSourceProperty.PUB, params);
        if (sqlRows.size() > 0) {
            for (SqlRow sqlRes : sqlRows) {
                strList.add(sqlRes.getString("str"));
            }
            return strList;
        }
        return strList;
    }

    /**
     * 根据task_id获取查询语句
     * @param params
     * @return
     * @throws Exception
     */
    private String getSqlStrByExeId(Map<String, Object> params) throws Exception {
        String SqlStr = "SELECT sqlStr FROM base_port_sql_info WHERE task_id = $S{task_id} ";
        SqlRow sqlRes = comnDao.findRow(SqlStr, DataSourceProperty.PUB, params);
        if (sqlRes != null){
            return sqlRes.getString("sqlStr");
        }
        return null;
    }


}
