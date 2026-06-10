package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.pms.disclosureControl.model.ScheduleWorkday;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ScheduleWorkdayDao extends ComnDao {

    /**
     * 功能：根据产品id与日期区间查询工作日
     *
     * @param param
     * @return
     * @throws Exception
     */
    public List<ScheduleWorkday> findByProdId(Map<String, String> param) throws Exception {

        String sql = "SELECT s.pgmno,s.workday " +
                " FROM sys_workday_set s " +
                " left join t8_prod_calendar c " +
                " on s.pgmno = c.pgmno " +
                " WHERE c.t8_prod_info_id = $S{prodId} "
                + " and s.workday >=$S{startDate} and s.workday<= $S{endDate}";

        return super.findRows(ScheduleWorkday.class, sql, 0, param);
    }

    /**
     * 功能：根据产品id、基准日期、加减工作日天数获取预计生成报告日期
     * 作者：rennannan
     * 日期：20210527
     *
     * @return
     */
    public List<ScheduleWorkday> findPlanCrtDate(Map<String, String> param) throws Exception {
        String sql = "select workday from (\n" +
                "select workday from sys_workday_set \n" +
                "where pgmno = $S{pgmno}";
        if (param.get("rule").equals("1")) {  //1代表-  2代表+
            sql += " and workday<$S{workday} order by workday desc ";
        } else {
            sql += " and workday>$S{workday}";
        }
        sql += " limit $U{days}" +
                ") wd";
        if (param.get("rule").equals("1")) {
            sql += " order by workday";
        } else {
            sql += " order by workday desc";
        }
        sql += " limit 1";
        return super.findRows(ScheduleWorkday.class, sql, 0, param);
    }
}
