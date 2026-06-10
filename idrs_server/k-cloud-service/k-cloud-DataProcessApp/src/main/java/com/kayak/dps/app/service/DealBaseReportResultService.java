package com.kayak.dps.app.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.dps.app.dao.DealBaseReportResultDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DealBaseReportResultService {

    @Resource(name = "dealBaseReportResultDao")
    public DealBaseReportResultDao dealBaseReportResultDao;
    private static final Map<String, String> specialAppTab = new HashMap<>(); //特殊表处理
    static {
        specialAppTab.put("app_cust_register_info","stg_cust_register_info");
        specialAppTab.put("app_cust_trans_info","stg_cust_trans_info");
        specialAppTab.put("app_cust_vol_register_info","stg_cust_vol_register_info");
    }
    public  void dealAllPortInfo(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> portList = null;
        //按天处理
       List<SqlRow> reportTableList = runBaseReportData(params);
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
        params.put("theoryReportStartDate",dealDate);
        dealBaseReportResultDao.deleteReportResrltByDealDate(params);
        for (SqlRow sqlRow :reportTableList) {
            String report_table = sqlRow.getString("sys_ref_table");
            params.put("report_table",report_table);
            params.put("report_type",sqlRow.getString("report_catgory"));
            params.put("table_name",sqlRow.getString("table_name"));
            params.put("theoryReportStartDate",dealDate);
            params.put("specialTB",specialAppTab.get(report_table)==null?"":specialAppTab.get(report_table));
            if(dealBaseReportResultDao.isReportResult(params)){
                List<SqlRow> reList ;
                int data_num=0;
                if(!StringUtils.isEmpty(params.get("specialTB").toString())){
                    reList =dealBaseReportResultDao.getSpecialReportNum(params);
                }else {
                    reList =dealBaseReportResultDao.getReportNum(params);
                }
                if(reList.size()>0){
                    data_num = reList.get(0).getInteger("data_num");
                }
                if(data_num>0){
                    params.put("data_num",data_num);
                    dealBaseReportResultDao.addReportResrltByDealDate(params);
                }
            }
        }
    }

    private  List<SqlRow>  runBaseReportData(Map<String, Object> params) throws Exception {
        return dealBaseReportResultDao.getBaseReportData(params);
    }


    public List<SqlRow> getDealDateList(String startDate, String endDate) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return dealBaseReportResultDao.getDealDateList(params);
    }

}
