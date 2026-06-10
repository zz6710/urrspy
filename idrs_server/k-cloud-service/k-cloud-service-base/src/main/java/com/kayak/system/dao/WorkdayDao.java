package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.util.Tools;
import com.kayak.system.model.WorkdayItem;
import com.kayak.system.model.WorkdayProgram;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Repository
public class WorkdayDao extends ComnDao {

    public SqlResult<WorkdayProgram> findProgram(SqlParam<WorkdayProgram> params) throws Exception {
        return super.findRows("SELECT * FROM sys_workday_pgm ", params);
    }

    public int addProgram(SqlParam<WorkdayProgram> params) throws Exception {
        return super.update("INSERT INTO sys_workday_pgm " +
                " (pgmname, pgmno, pgmtype, remark) VALUES " +
                " ($S{pgmname}, $S{pgmno}, $S{pgmtype}, $S{remark})", params.getModel()).getEffect();
    }

    public void delProgram(SqlParam<WorkdayProgram> params) throws Exception {
        doTrans(() -> {
            super.update(
                    "DELETE FROM sys_workday_set WHERE pgmno = $S{pgmno}", params.getModel());
            super.update(
                    "DELETE FROM sys_workday_pgm WHERE pgmno = $S{pgmno}", params.getModel());
        });
    }

    public int updateProgram(SqlParam<WorkdayProgram> params) throws Exception {
        return super.update("UPDATE sys_workday_pgm " +
                " SET pgmname = $S{pgmname}, pgmtype = $S{pgmtype}, remark = $S{remark}" +
                " WHERE pgmno = $S{pgmno} ", params.getModel()).getEffect();
    }

    public WorkdayProgram getProgram(SqlParam<WorkdayProgram> params) throws Exception {
        return super.findRow(WorkdayProgram.class, "SELECT * FROM sys_workday_pgm WHERE pgmno = $S{pgmno}",
                0, params);
    }

    public List<WorkdayItem> find(WorkdayItem workdayItem) throws Exception {
        return super.findRows(WorkdayItem.class, "SELECT * FROM sys_workday_set WHERE pgmno = $S{pgmno} ",
                0, workdayItem);
    }

    public SqlResult<WorkdayItem> find(SqlParam<WorkdayItem> params) throws Exception {
        String workday = params.getModel().getWorkday();
        String sql;
        if (Tools.isBlank(workday)) {
            sql = "SELECT * FROM sys_workday_set WHERE pgmno = $S{pgmno}";
        } else {
            sql = "SELECT * FROM sys_workday_set WHERE pgmno = $S{pgmno} AND workday LIKE '" + workday + "%'";
        }
        return super.findRows(sql, params);
    }

    
    public SqlResult<WorkdayItem> findByProdId(SqlParam<WorkdayItem> params) throws Exception {
        
        String sql = "SELECT s.* FROM sys_workday_set s left join t8_prod_calendar c on s.pgmno = c.pgmno WHERE c.t8_prod_info_id = $S{prodId}";
       
        return super.findRows(sql, params);
    }

    //查询最大值和最小值
    public SqlResult<WorkdayItem> findMaxMin(SqlParam<WorkdayItem> params) throws Exception {
        String workday = params.getModel().getWorkday();
        String sql;
        if (Tools.isBlank(workday)) {
            sql = "SELECT min(workday) min_workday , max(workday) max_workday FROM sys_workday_set WHERE pgmno = $S{pgmno}";
        } else {
            sql = "SELECT min(workday) min_workday , max(workday) max_workday FROM sys_workday_set WHERE pgmno = $S{pgmno} AND workday LIKE '" + workday + "%' order by workday";
        }
        return super.findRows(sql, params);
    }



    public void saveWorkdayItems(String pgmno, String year, List<WorkdayItem> newData)
            throws Exception {
        if (Tools.isBlank(pgmno) || Tools.isBlank(year)) {
            return;
        }
        doTrans(()->{
            super.update("DELETE FROM sys_workday_set " +
                    " WHERE pgmno = '" + pgmno + "' AND workday LIKE '" + year + "%'");
            if (CollectionUtils.isEmpty(newData)) {
                return;
            }

            for (WorkdayItem workdayItem : newData) {
                super.update("INSERT INTO sys_workday_set (pgmno, workday) VALUES " +
                        " ($S{pgmno}, $S{workday})", workdayItem);
            }
        });
    }

}
