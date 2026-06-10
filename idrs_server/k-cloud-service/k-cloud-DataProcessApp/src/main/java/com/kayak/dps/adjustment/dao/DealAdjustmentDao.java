package com.kayak.dps.adjustment.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class DealAdjustmentDao extends ComnDao {

    @Resource(name = "comnDao")
    private ComnDao comnDao;

    public List<SqlRow> findThisMsg(Map<String,Object> params) throws Exception {
        String sql = "select azt.row_id ,azt.column_id ,azt.data_value from app_zz_transform_01 azt " +
                "where azt.report_date = $S{dealDate} and azt.column_id in ('1','7','10') order by azt.row_id+'0',azt.column_id+'0'";
        return comnDao.findRows(sql, params);
    }

    public void dealAdjust(Map<String, Object> params) throws Exception {
        String sql = "update app_zz_transform_01 set data_value = $S{balanceZz} where report_date = $S{dealDate} and row_id = $S{row_id} and column_id = '10'";
        comnDao.update(sql,params);
    }
}
