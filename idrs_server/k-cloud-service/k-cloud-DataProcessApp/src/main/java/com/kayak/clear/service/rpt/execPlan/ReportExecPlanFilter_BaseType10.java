package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import org.springframework.stereotype.Component;

/**
 * base_type = 10 每个工作日的数据处理
 */
@Component
public class ReportExecPlanFilter_BaseType10 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "10";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req){
        return req.getWorkDate();
    }
}
