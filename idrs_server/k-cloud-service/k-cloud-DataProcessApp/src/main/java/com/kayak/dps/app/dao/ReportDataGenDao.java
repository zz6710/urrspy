package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.exception.ReportDataProcessExecuteException;
import com.kayak.dps.ods.constants.Constants;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class ReportDataGenDao extends ComnDao {

    /**
     * 执行应用层sql语句
     * @param sqlStr
     * @param settle_date
     * @return
     * @throws Exception
     */
    public List<SqlRow> executeExeId (String sqlStr,String settle_date) throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("settle_date",settle_date);
        return super.findRows(sqlStr, DataSourceProperty.PUB, params);
    }

    /**
     * 二维报表处理后的报送数据入库处理
     * @param rowIdList
     * @param columnIdList
     * @param valueList
     * @param report_date
     * @param report_table
     * @param data_type 数据类型：1-计算 2-汇总
     * @throws Exception
     */
    public void putDataToAppReportTable (List<String> rowIdList, List<String> columnIdList, List<String> valueList, String report_date, String report_table,
                                         String theory_report_start_date,String theory_report_end_date, String data_type) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into " + report_table + "(id, report_date, row_id, column_id, data_value, theory_report_start_date, theory_report_end_date, register_status, register_date, sys_data_source," +
                   "       sys_data_status,sys_data_version,imp_date) ");
        sql.append("values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                   "        ?, ?, ?)");
        int maxId = this.getReportTableMaxId(report_table ,rowIdList.size());//获取最大id且更新序列
        String cur_date = DateUtil.getNowDate();
        //开启事务处理批处理
        doTrans( () ->{
            //获取版本号
            String version = this.getReportAppDataVersionByDate(report_date, report_table, data_type);
            PreparedStatement preparedStatement=null;
            try {
                    Connection connection = this.getConnection();
                    preparedStatement = connection.prepareStatement(sql.toString());
                    for (int n=0; n<rowIdList.size(); n++) {
                        preparedStatement.setInt(1, maxId+n);
                        preparedStatement.setString(2, report_date);
                        preparedStatement.setString(3, rowIdList.get(n));
                        preparedStatement.setString(4, columnIdList.get(n));
                        preparedStatement.setString(5, valueList.get(n));
                        preparedStatement.setString(6, theory_report_start_date);
                        preparedStatement.setString(7, theory_report_end_date);
                        preparedStatement.setString(8, "0");/*登记状态,默认为0-未报送*/
                        preparedStatement.setString(9, cur_date);/*登记日期*/
                        preparedStatement.setString(10, "1");/*数据源,默认1-系统生成*/
                        preparedStatement.setString(11, "1");/*状态,默认1-生效*/
                        preparedStatement.setString(12, version);/*版本,1.0开始*/
                        preparedStatement.setString(13, cur_date);/*导入日期*/
                        preparedStatement.addBatch();
                    }
                    preparedStatement.executeBatch();
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                throw new ReportDataProcessExecuteException("App层 " + report_table + " 表报送数据插入报送业务表语句执行异常:" + e);
            } finally {
                preparedStatement.close();
            }
        });
    }

    /**
     * 查询报送数据表最大id+1且更新些
     * @param report_table 表名
     * @param size 数据量
     * @return
     * @throws Exception
     */
    private int getReportTableMaxId (String report_table, int size) throws Exception {
        String queryStr = "SELECT IFNULL(MAX(id), 0)+1 AS id FROM " + report_table;
        int maxId = super.findRow(queryStr, DataSourceProperty.PUB, null).getInteger("id");
        String updStr = "update sys_sequence set maxid = " + (maxId + size) + " where tablename = '" + report_table + "'";
        super.update(updStr, DataSourceProperty.PUB, null);
        return maxId;
    }

    /**
     * 删除当日报表已生成应用层数据
     * @param report_date
     * @param report_table
     * @param data_type 数据类型：1-计算 2-汇总
     * @throws Exception
     */
    public String getReportAppDataVersionByDate (String report_date, String report_table, String data_type) throws Exception{
        String version = "1.0";
        String querySql = "select max(sys_data_version) as cur_version from " + report_table + " where report_date = '" + report_date + "'" ;
        SqlRow sqlRow = super.findRow(querySql,null);
        if (!"".equals(sqlRow.getString("cur_version")) && Constants.REPORT_DATA_TYPE_CAL.equals(data_type)) {//数据类型为计算重新往上堆叠版本
            String last_version = sqlRow.getString("cur_version");//获取上一个版本号
            version = String.valueOf(new BigDecimal(sqlRow.getString("cur_version")).add(new BigDecimal("0.1")));
            //将上一个版本号数据重置为失效状态
            String expire_sql = "update " + report_table + " set sys_data_status = '0' where sys_data_version = '" + last_version + "' and report_date = '" + report_date + "'";
            super.update(expire_sql, null);
        } else if (!"".equals(sqlRow.getString("cur_version")) && Constants.REPORT_DATA_TYPE_SUM.equals(data_type)) {//汇总是对上次计算结果进行汇总,版本取上一个计算版本,既最大版本号
            version =  sqlRow.getString("cur_version");
        }
        return version;
    }

    /**
     * 获取报送时点信息
     * @param task_id
     * @return
     * @throws Exception
     */
    public Map<String,Object> getTaskBaseType(String task_id) throws Exception{
        Map<String,Object> params = new HashMap<>();
        String sql = "select k.base_type,k.data_type,ifnull(k.inner_submission_time_require,0) as inner_submission_time_require," +
                "ifnull(k.supervise_submission_time_require,0) as supervise_submission_time_require," +
                "ifnull(k.data_gener_time_require,0) as data_gener_time_require,k1.sys_ref_table " +
                "from base_submission_time_config k left join base_report_info k1 on k.report_table=k1.report_table " +
                "where k1.task_id='"+task_id+"'";
        List<SqlRow> list = super.findRows(sql);
        if (list.size()>0){
            params.put("base_type",list.get(0).getString("base_type"));
            params.put("data_type",list.get(0).getString("data_type"));
            params.put("sys_ref_table",list.get(0).getString("sys_ref_table"));
            params.put("inner_submission_time_require",list.get(0).getInteger("inner_submission_time_require"));
            params.put("supervise_submission_time_require",list.get(0).getInteger("supervise_submission_time_require"));
            params.put("data_gener_time_require",list.get(0).getInteger("data_gener_time_require"));//报送数据生成日期(天)
        }
        return params;
    }

    /**
     * 特殊的二维报表出数
     * @param task_id
     * @return
     * @throws Exception
     */
    public Map<String,Object> getTaskReportTable(String task_id) throws Exception{
        Map<String,Object> params = new HashMap<>();
        String sql = "select * from base_report_info k1 where k1.task_id='"+task_id+"'";
        List<SqlRow> list = super.findRows(sql);
        if (list.size()>0){
            params.put("report_table",list.get(0).getString("report_table"));
        }
        return params;
    }

    /**
     * 获取报送成功条数
     * @param report_table
     * @param theory_report_start_date
     * @return
     * @throws Exception
     */
    public String getTabSucNum(String report_table,String theory_report_start_date) throws Exception{
        String sql = "select count(1) data_num from "+report_table+" where register_status='3' and theory_report_start_date='"+theory_report_start_date+"'";
        int data_num = super.findRows(sql).get(0).getInteger("data_num");
        if (data_num>0){
            return "1";
        }
        return "0";
    }

    /**
     * 查询报送数据锁表配置表
     * @param settle_date
     * @param task_id
     * @return
     */
    public boolean checkTaskIsLocked(String settle_date, String task_id) {
        try {
            String checkSql = "select distinct dd.lock_status, dd.report_table" +
                    "            from base_report_data_lock_record dd" +
                    "            left join base_report_data_lock_config dl on dl.report_table = dd.report_table " +
                    "           where dd.report_date = '" + settle_date + "' " +
                    "             and dl.task_id = '" + task_id + "' ";
            SqlRow row = super.findRow(checkSql, null);
            if (row == null || row.size() == 0) {
                return false;
            }
            return ("01".equals(row.getString("lock_status")));//需同时满足配置记录表锁表+表中存在数据时才返回锁表
        } catch (Exception e) {
            log.error("查询报送数据锁定配置表信息异常:" + e.getMessage());
            return false;
        }
    }

}
