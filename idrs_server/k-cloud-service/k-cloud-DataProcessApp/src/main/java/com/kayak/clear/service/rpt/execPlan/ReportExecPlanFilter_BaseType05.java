package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * base_type = 05 每周最后一个工作日
 */
@Component
public class ReportExecPlanFilter_BaseType05 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "05";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception{
        String workDate = req.getWorkDate();
        String workDtAdd1 = getAfterDay(workDate, 1); //工作日后的一个自然日
        String sql = "select count(*) cnt from sys_workday_set t where pgmno = '001' and workday = '"+workDtAdd1+"'";
        SqlRow row = comnDao.findRow(sql, req);
        int size = row.getInteger("cnt");
        if(size == 0){//工作日后的一个自然日，不是工作日则为该工作日为本周最后一个工作日
            return workDate;
        }else{
            return null;
        }
    }
}
