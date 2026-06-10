package com.kayak.dps.app.service;

import com.kayak.core.sql.SqlRow;
import com.kayak.dps.app.dao.BuildReportResultDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建报表执行结果服务类
 */
@Slf4j
@Service
public class BuildReportResultService {

    @Resource(name = "buildReportResultDao")
    public BuildReportResultDao dao;

    /**
     * 构建执行结果
     * @param dealDate 处理日期（工作日）
     * @throws Exception
     */
    public void buildResult(String dealDate) throws Exception{
        List<SqlRow> rows = runReportConfigPlanData(dealDate);
        if(rows != null && rows.size() > 0){
            buildReportResult(dealDate, rows);
        }
    }

    /**
     *
     * @param dealDate
     * @return
     * @throws Exception
     */
    private List<SqlRow> runReportConfigPlanData(String dealDate) throws Exception {
        return dao.getReportConfigPlanData(dealDate);
    }

    /**
     * 构建执行结果
     * @param dealDate 处理日期（工作日）
     * @param reportTableList 要处理的报表列表
     * @throws Exception
     */
    private void buildReportResult(String dealDate,List<SqlRow> reportTableList) throws Exception{
        dao.deleteReportResultByDealDate(dealDate);  //删除数据
        Map<String, Object> params = new HashMap<>();
        for (SqlRow sqlRow :reportTableList) {
            params.put("report_type", sqlRow.getString("report_type"));
            params.put("report_table",sqlRow.getString("report_table"));
            params.put("report_table_name",sqlRow.getString("report_table_name"));
            params.put("work_date",sqlRow.getString("work_date"));
            params.put("base_line_date",sqlRow.getString("base_line_date"));
            params.put("start_date",sqlRow.getString("start_date"));
            params.put("end_date",sqlRow.getString("end_date"));
            params.put("exec_status",sqlRow.getString("exec_status"));
            params.put("base_type",sqlRow.getString("base_type")); //基准类型
            params.put("data_type",sqlRow.getString("data_type")); //日期类型 01 工作日； 02 自然日
            if(dao.isReportResult(params)){
                List<SqlRow> reList ;
                int data_num=0;
                reList = dao.getReportNum(params);

                if(reList.size()>0){
                    data_num = reList.get(0).getInteger("data_num");
                }
                if(data_num>0){
                    params.put("data_num",data_num);
                    dao.addReportResultByDealDate(params); //插入结果
                }
            }
        }
    }
}
