package com.kayak.dps.app.utils;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.app.dao.ReportDataGenDao;
import com.kayak.dps.check.constants.ErrorCollectionConstants;
import com.kayak.dps.check.util.PrimaryDataCheckUtil;
import com.kayak.dps.ods.constants.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
@Slf4j
public class ReportDataGenUtils {

    private static ReportDataGenDao reportDataGenDao = SysBeans.getBean("reportDataGenDao");

    /**
     * 处理报送数据
     * @param task_id
     * @param settle_date 任务日期
     * @throws Exception
     */
    public static void reportDataHandlerProcess (String task_id,String settle_date) throws Exception {
        String theory_report_start_date = "";
        String theory_report_end_date = "";
        String report_table = "";
        String report_date = "";//数据日期
        try{
            Map<String,Object> tparam = reportDataGenDao.getTaskBaseType(task_id);
            String base_type = "";
            String mark = "0";
            String cal_date = "";//根据月末or季末日期计算数据任务日期

            if(tparam.containsKey("base_type")){//基准日期
                report_table = tparam.get("sys_ref_table").toString();
                base_type = tparam.get("base_type").toString();
                String data_type = tparam.get("data_type").toString();
                int inner_submission_time_require = Integer.parseInt(tparam.get("inner_submission_time_require").toString());
                int supervise_submission_time_require = Integer.parseInt(tparam.get("supervise_submission_time_require").toString());
                int data_gener_time_require = Integer.parseInt(tparam.get("data_gener_time_require").toString());
                report_date = DateUtil.calReportDateByDateType(settle_date, base_type, data_gener_time_require);//计算报送数据日期

                if("02".equals(data_type)){//日期类型: 01-工作日 02-自然日
                    theory_report_start_date = DateUtil.add(report_date,"yyyyMMdd",inner_submission_time_require);
                    theory_report_end_date = DateUtil.add(report_date,"yyyyMMdd",supervise_submission_time_require);
                    cal_date = DateUtil.add(report_date,"yyyyMMdd",data_gener_time_require);//根据报送数据日期和报送数据生成日期(N)推算出任务日期
                }else{
                    theory_report_start_date = DateUtil.addSysWordDay(report_date,inner_submission_time_require);
                    theory_report_end_date = DateUtil.addSysWordDay(report_date,supervise_submission_time_require);
                    cal_date = DateUtil.addSysWordDay(report_date,data_gener_time_require);
                }
                mark=reportDataGenDao.getTabSucNum(report_table,theory_report_start_date);//是否已经报送完成1-是 0-否
            }else{
                tparam = reportDataGenDao.getTaskReportTable(task_id);
                report_table = tparam.get("report_table").toString();
                if(settle_date.equals(DateUtil.getLastDayOfMonth(settle_date))){
                    report_date=DateUtil.getLastCycleDay(settle_date,0,1);
                }else{
                    report_date=DateUtil.getLastCycleDay(settle_date,-1,1);
                }
            }

            /** 判断任务task_id在当前数据日期settle_date下是否需要锁表 */
            if(!checkTaskIsLocked(report_date, task_id)){
                log.info("清算任务" + task_id + "报送数据表已锁定,请解除当前"+report_date+"跑批日期锁定后重试,退出当前清算任务！");
                return;
            }

            if("0".equals(mark)){
                if("06".equals(base_type)){//每月最后一个自然日
                    if(settle_date.equals(cal_date)){
                        mark = "0";
                    }else{
                        mark = "1";
                    }
                }else if("07".equals(base_type)) {//每季度最后一个自然日
                    if(report_date.endsWith("0331") || report_date.endsWith("0630") || report_date.endsWith("0930") || report_date.endsWith("1231")){
                        if(settle_date.equals(cal_date)){
                            mark = "0";
                        }else{
                            mark = "1";
                        }
                    }else{
                        mark = "1";
                    }
                }else {
                    mark = "0";
                }
            }
            if("0".equals(mark)){
                /**报送计算数据处理*/
                List<SqlRow> list = ExeQuery.queryPortSqlByTaskId(task_id);
                convertAndSaveData(list, report_date, report_table, theory_report_start_date, theory_report_end_date, Constants.REPORT_DATA_TYPE_CAL);
                /**报送合计数据处理*/
                List<SqlRow> sum_list = ExeQuery.querySumPortSqlByTaskId(task_id);
                convertAndSaveData(sum_list, report_date, report_table, theory_report_start_date, theory_report_end_date, Constants.REPORT_DATA_TYPE_SUM);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_REPORT_DATA_BATCH_HANDLE, "报送数据生成任务: 报送数据处理任务异常:" + e);
            throw new Exception("报送数据生成任务: 报送数据处理任务异常:" + e);
        }
    }

    /**
     * 处理报送数据，通过传入的日期跑数
     * @param task_id
     * @param base_date 任务日期
     * @throws Exception
     */
    public static void baseDataHandlerProcess (String task_id,String base_date) throws Exception {
        try{
            Map<String,Object> tparam = reportDataGenDao.getTaskReportTable(task_id);
            String report_table = tparam.get("report_table").toString();

            /** 判断任务task_id在当前数据日期settle_date下是否需要锁表 */
            if(!checkTaskIsLocked(base_date, task_id)){
                log.info("清算任务" + task_id + "报送数据表已锁定,请解除当前"+base_date+"跑批日期锁定后重试,退出当前清算任务！");
            } else {
                /**报送计算数据处理*/
                List<SqlRow> list = ExeQuery.queryPortSqlByTaskId(task_id);
                convertAndSaveData(list, base_date, report_table, "", "", Constants.REPORT_DATA_TYPE_CAL);
                /**报送合计数据处理*/
                List<SqlRow> sum_list = ExeQuery.querySumPortSqlByTaskId(task_id);
                convertAndSaveData(sum_list, base_date, report_table, "", "", Constants.REPORT_DATA_TYPE_SUM);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_REPORT_DATA_BATCH_HANDLE, "报送数据生成任务: 报送数据处理任务异常:" + e);
            throw new Exception("报送数据生成任务: 报送数据处理任务异常:" + e);
        }
    }

    /**
     * 对查询结果集进行汇总转换处理插入报送表
     * @param list
     * @param settle_date
     * @param report_table
     * @param theory_report_start_date
     * @param theory_report_end_date
     * @param data_type 数据类型：1-计算 2-汇总
     * @throws Exception
     */
    private static void convertAndSaveData(List<SqlRow> list, String settle_date, String report_table,String theory_report_start_date,String theory_report_end_date, String data_type) throws Exception {
        List<String> rowIdList = new ArrayList<>();
        List<String> columnIdList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();

        for (SqlRow sqlRow:list){
            List<SqlRow> reportSearchDataRes = reportDataGenDao.executeExeId(sqlRow.get("sqlstr").toString(),settle_date);
            if(reportSearchDataRes.size() <= 0) {
                continue;//无查询结果则退出
            }
            //列名正则匹配c/C开头接数值的字符
            Pattern column_regex = Pattern.compile("^[C|c]\\d");
            /** 对查询的结果集进行遍历处理 */
            for (SqlRow rowData : reportSearchDataRes) {
                //分解行数据
                Set<String> keySetList = rowData.keySet();
                String row_id = rowData.getString("row_index");

                for (String column_code : keySetList) {
                    if(column_regex.matcher(column_code).find()){
                        rowIdList.add(row_id);
                        //规则为cxx，截取一位后数值
                        columnIdList.add(column_code.substring(1));
                        valueList.add(rowData.getString(column_code));
                    }
                }
            }
        }
        reportDataGenDao.putDataToAppReportTable(rowIdList, columnIdList, valueList,settle_date,report_table,theory_report_start_date,theory_report_end_date,data_type);
    }

    /**
     * 检查任务ID是否为锁定报送任务表
     * @param settle_date
     * @param task_id
     * @return
     */
    public static boolean checkTaskIsLocked(String settle_date, String task_id) {
        return !reportDataGenDao.checkTaskIsLocked(settle_date, task_id);
    }

}
