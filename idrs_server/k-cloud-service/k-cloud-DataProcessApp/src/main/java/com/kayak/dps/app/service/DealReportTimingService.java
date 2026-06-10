package com.kayak.dps.app.service;

import com.kayak.clear.utils.DateUtils;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.app.dao.DealReportTimingDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DealReportTimingService {

    @Resource(name = "dealReportTimingDao")
    public DealReportTimingDao dealReportTimingDao;

    public  void dealAllPortInfo(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> portList = null;
        //按天处理
       List<SqlRow> reportTableList = getReportTableList(params);
        if(StringUtils.equals("1",params.get("dealType").toString())){
            if(StringUtils.isEmpty(params.get("dealDate").toString()))
                throw new RuntimeException("数据日期输入不正确！");
            runBaseReportResultByDealDate(params.get("dealDate").toString().replace("-",""),reportTableList);
        }else{//按日期区间处理
            if(StringUtils.isEmpty(params.get("dealDates[0]").toString()) || StringUtils.isEmpty(params.get("dealDates[1]").toString()))
                throw new RuntimeException("数据区间输入不正确！");
            String startDate = params.get("dealDates[0]").toString();
            String endDate = params.get("dealDates[1]").toString();
           List<SqlRow> dealDateList = getDealDateList(startDate, endDate);
           for (SqlRow sqlRow :dealDateList) {
                try {
                    params.put("dealDate",sqlRow.getString("dealDate").replace("-",""));
                    runBaseReportResultByDealDate(params.get("dealDate").toString(),reportTableList);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }



    private void runBaseReportResultByDealDate(String dealDate,List<SqlRow> reportTableList) throws Exception {
        Map<String, Object> params = new HashMap<>();
        for (SqlRow sqlRow :reportTableList) {
            params.put("reportTable",sqlRow.getString("report_table"));
            params.put("dataype",sqlRow.getString("data_type"));
            params.put("theoryReportStartDate",dealDate);
            int intervalDays = sqlRow.getInteger("supervise_submission_time_require");
            String theoryReportEndDate ="";
            if(StringUtils.equals(sqlRow.getString("data_type"),"01")){//工作日
                theoryReportEndDate = dealReportTimingDao.getWeekdayByIntervalDays(params,intervalDays);
            }else{//自然日
                theoryReportEndDate= DateUtils.getDateAddDays(dealDate,intervalDays);
            }
            params.put("theoryReportEndDate",theoryReportEndDate);
            if( StringUtils.equals(sqlRow.getString("report_table"),"app_rpt_bm_01") || StringUtils.equals(sqlRow.getString("report_table"),"app_rpt_bm_02") || StringUtils.equals(sqlRow.getString("report_table"),"app_rpt_pvd3")
                    || StringUtils.equals(sqlRow.getString("report_table"),"app_rpt_g06_01")  || StringUtils.equals(sqlRow.getString("report_table"),"app_rpt_g06_02") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_1_1")
                    || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_1_2") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_1_3") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_1_4")
                    || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_1_5") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_1_6") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_2_1")
                    || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_2_2") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_2_3") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_2_4")
                    || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_2_5") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_3_1") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_3_2")
                    || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_3_3") || StringUtils.equals(sqlRow.getString("report_table"),"app_pbc_report_4_1")) {
                dealReportTimingDao.updatetheoryReportEndDate01 (params);
            }else{
                dealReportTimingDao.updatetheoryReportEndDate (params);
            }
        }
    }

    private List<SqlRow> getReportTableList(Map<String, Object> params) throws Exception {
        return dealReportTimingDao.getReportTableInfo(params);
    }

    public List<SqlRow> getDealDateList(String startDate, String endDate) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return dealReportTimingDao.getDealDateList(params);
    }

}
