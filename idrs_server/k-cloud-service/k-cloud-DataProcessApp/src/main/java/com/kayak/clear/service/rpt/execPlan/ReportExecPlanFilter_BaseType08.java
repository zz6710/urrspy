package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import org.springframework.stereotype.Component;

/**
 * base_type = 08 每半年最后一个自然日
 */
@Component
public class ReportExecPlanFilter_BaseType08 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "08";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception{
        String strLastMonthEndDay = getLastMonthEndDay(req.getMacDate()); //获取上一个月最后一个自然日
        String dtMonth = strLastMonthEndDay.substring(4,6);
        if(dtMonth.equals("06") ||
                dtMonth.equals("12")){
            return strLastMonthEndDay;
        }else{
            return null;
        }
    }
}
