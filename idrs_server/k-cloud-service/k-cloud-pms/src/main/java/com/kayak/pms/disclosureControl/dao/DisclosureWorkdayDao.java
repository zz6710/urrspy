package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.disclosureControl.model.DisclosureWorkday;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DisclosureWorkdayDao extends ComnDao {

    /**
     * 功能：根据产品id与日期区间查询工作日
     *
     * @param param
     * @return
     * @throws Exception
     */
    public List<DisclosureWorkday> findByProdId(Map<String, String> param) throws Exception {

        String sql = "SELECT s.pgmno,s.workday " +
                " FROM sys_workday_set s " +
                " left join t8_prod_calendar c " +
                " on s.pgmno = c.pgmno " +
                " WHERE c.t8_prod_info_id = $S{prodId} "
                + " and s.workday >=$S{startDate} and s.workday<= $S{endDate}";

        return super.findRows(DisclosureWorkday.class, sql, 0, param);
    }

    /**
     * 功能：根据工作日方案编号与日期区间查询工作日
     * 作者：rennannan
     * 日期：20210622
     *
     * @param param
     * @return
     * @throws Exception
     */
    public List<DisclosureWorkday> findWorkDayByPgmNo(Map<String, String> param) throws Exception {
        String sql = "select workday " +
                "   from sys_workday_set " +
                "   where pgmno=$S{pgmno} " +
                "     and workday>=$S{startDate} " +
                "     and workday <= $S{endDate}";

        return super.findRows(DisclosureWorkday.class, sql,
                DataSourceProperty.PUB, param);
    }
    public List<DisclosureWorkday> findWorkDayByfiveDays(Map<String, String> param) throws Exception {
        String sql = "select workday " +
                "   from sys_workday_set " +
                "   where pgmno=$S{pgmno} " +
                "   and workday>=$S{startDate} " +
                "   order by workday " +
                "   limit $U{days}";

        return super.findRows(DisclosureWorkday.class, sql,
                DataSourceProperty.PUB, param);
    }

    public List<DisclosureWorkday> findWorkDays(Map<String, String> param) throws Exception {
        String sql = "select workday " +
                "   from sys_workday_set " +
                "   where pgmno=$S{pgmno} " +
                "   and workday<$S{startDate} " +
                "   order by workday desc" +
                "   limit $U{days}";

        return super.findRows(DisclosureWorkday.class, sql,
                DataSourceProperty.PUB, param);
    }

    /**
     * 功能：根据产品id、基准日期、加减工作日天数获取预计生成报告日期
     * 作者：rennannan
     * 日期：20210527
     *
     * @return
     */
    public List<DisclosureWorkday> findPlanCrtDate(Map<String, String> param) throws Exception {
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
        return super.findRows(DisclosureWorkday.class, sql,
                DataSourceProperty.PUB, param);
    }

    /**
     * 功能：根据产品id与日期区间查询产品开放日
     * 作者：rennannan
     * 日期：20210622
     *
     * @param param
     * @return
     * @throws Exception date_type='1' 代表查询日期类型为开放日的
     */
    public List<DisclosureWorkday> findProdOpenDays(Map<String, String> param) throws Exception {
        String sql = "select change_date workday" +
                " from t8_prod_days " +
                " where t8_prod_info_id=$S{prodId} " +
                " and date_type='1' " +
                " and change_date >=$S{startDate} " +
                " and change_date <= $S{endDate}";
        return super.findRows(DisclosureWorkday.class, sql, 0, param);
    }

    /**
     * 功能：根据产品代码和日期区间查询封闭期投资日起始日期和封闭期投资日结束日期
     * 作者：rennannan
     * 日期：20210622
     *
     * @param param
     * @return
     * @throws Exception
     */
    public List<DisclosureWorkday> findCloseDays(Map<String, String> param) throws Exception {
        String sql = "select close_start_date,close_end_date \n" +
                "  from t8_prod_schedule \n" +
                " where prod_code = $S{prodCode} \n" +
                "   and establish_open_date >= $S{startDate} \n" +
                "\t and establish_open_date <= $S{endDate};";
        return super.findRows(DisclosureWorkday.class, sql, 0, param);
    }

    /**
     * 功能：查询某个日期区间工作日
     * 作者：rennannan
     * 日期：20210621
     *
     * @param param
     * @return
     */
    public List<DisclosureWorkday> findWorkDay(Map<String, String> param) throws Exception {
        String sql = "select workday from sys_workday_set where pgmno=(\n" +
                " select cal.pgmno from t8_prod_calendar cal\n" +
                " where cal.t8_prod_info_id=$S{t8ProdInfoId})\n" +
                "   and workday >= $S{startDate} \n" +
                "\t and workday <= $S{endDate}";
        return super.findRows(DisclosureWorkday.class, sql, 0, param);

    }

    /**
     * 功能：根据方案编号与日期查询对应日期下一个工作日
     * 作者：rennannan
     * 日期：20210625
     *
     * @param
     * @return
     * @throws Exception
     */
    public List<DisclosureWorkday> findNextWorkDay(Map<String, String> param) throws Exception {
        String sql = "select workday from sys_workday_set where workday > $S{workday} and pgmno=$S{pgmno} limit 1;";
        return super.findRows(DisclosureWorkday.class, sql, 0, param);
    }

    /**
     * 功能：根据产品id与日期查询对应日日期,当月最后一个工作日
     * 作者：zls
     * 日期：20210806
     *
     * @param
     * @return
     * @throws Exception
     */
    public String findLastWorkDayOfMonth(Map<String, String> param) throws Exception {
        String month = param.get("month");

        String sql = "select sws.workday from t8_prod_info tpi left join t8_prod_calendar tpc " +
                " on tpi.id = tpc.t8_prod_Info_id left join sys_workday_set sws on sws.pgmno=tpc.pgmno " +
                " where tpi.id=$S{prodId} and sws.workday like '"+month+"%' order by sws.workday desc limit 1";
        return super.findRows(DisclosureWorkday.class, sql, 0, param).get(0).getWorkday();
    }

    /**
     * 功能：根据产品代码与日期查询对应日期是否为产品的封闭期投资起始日期或者结束日期
     * 作者：rennannan
     * 日期：20210625
     *
     * @param param
     * @return
     * @throws Exception
     */
    public int getCloseDayCount(Map<String, String> param) throws Exception {
        String sql = "select count(1) as count " +
                " from t8_prod_schedule " +
                " where prod_code = $S{prodCode} " +
                " and (close_start_date=$S{workday} or close_end_date=$S{workday}) ;";
        SqlRow sqlRow = super.findRow(sql, param);
        return sqlRow.getInteger("count");
    }

    /**
     * 功能：根据产品代码和日期查询对应日期是否为分红除权日
     * 作者：rennannan
     * 日期：20210526
     *
     * @param param
     * @return
     * @throws Exception
     */
    public int getDivideDayCount(Map<String, String> param) throws Exception {
        String sql = "select count(1) as count from t8_prod_dividend_plan where prod_code = $S{prodCode} and dividend_ex_date=$S{workday}";
        SqlRow sqlRow = super.findRow(sql, param);
        return sqlRow.getInteger("count");
    }
}
