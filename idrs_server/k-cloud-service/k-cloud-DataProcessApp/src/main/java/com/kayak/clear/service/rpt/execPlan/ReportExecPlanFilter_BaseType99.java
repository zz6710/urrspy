package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import org.springframework.stereotype.Component;

/**
 * base_type = 99 业务发生日
 */
@Component
public class ReportExecPlanFilter_BaseType99 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "99";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception {
        return req.getWorkDate(); //默认每个工作日都处理
    }
}
