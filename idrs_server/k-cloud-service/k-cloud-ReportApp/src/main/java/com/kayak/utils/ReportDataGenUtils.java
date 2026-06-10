package com.kayak.utils;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.subject.dao.ReportDataGenDao;
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
            String mark = "0";
            if(tparam.containsKey("base_type")){//基准日期
                report_table = tparam.get("sys_ref_table").toString();
                String data_type = tparam.get("data_type").toString();
                int inner_submission_time_require = Integer.parseInt(tparam.get("inner_submission_time_require").toString());
                int supervise_submission_time_require = Integer.parseInt(tparam.get("supervise_submission_time_require").toString());
                report_date = settle_date;

                if("02".equals(data_type)){//日期类型: 01-工作日 02-自然日
                    theory_report_start_date = DateUtil.add(settle_date,"yyyyMMdd",inner_submission_time_require);
                    theory_report_end_date = DateUtil.add(settle_date,"yyyyMMdd",supervise_submission_time_require);
                }else{
                    theory_report_start_date = DateUtil.addSysWordDay(settle_date,inner_submission_time_require);
                    theory_report_end_date = DateUtil.addSysWordDay(settle_date,supervise_submission_time_require);
                }
                mark=reportDataGenDao.getTabSucNum(report_table,theory_report_start_date);//是否已经报送完成1-是 0-否
            }
            if("0".equals(mark)){
                /**报送计算数据处理*/
                List<SqlRow> list = ExeQuery.queryPortSqlByTaskId(task_id);
                convertAndSaveData(list, report_date, report_table, theory_report_start_date, theory_report_end_date, "1");
                /**报送合计数据处理*/
                List<SqlRow> sum_list = ExeQuery.querySumPortSqlByTaskId(task_id);
                convertAndSaveData(sum_list, report_date, report_table, theory_report_start_date, theory_report_end_date, "2");
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
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

}
