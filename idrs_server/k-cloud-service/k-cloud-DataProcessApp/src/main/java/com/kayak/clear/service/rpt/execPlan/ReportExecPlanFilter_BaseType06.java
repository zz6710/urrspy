package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * base_type = 06 每月最后一个自然日
 */
@Component
public class ReportExecPlanFilter_BaseType06 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "06";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception{
        String strLastMonthEndDay = getLastMonthEndDay(req.getMacDate()); //获取上一个月最后一个自然日
        return strLastMonthEndDay;
    }
}
