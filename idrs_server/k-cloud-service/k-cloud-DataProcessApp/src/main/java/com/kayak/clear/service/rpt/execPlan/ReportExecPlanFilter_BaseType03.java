package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Component;

/**
 * base_type = 03  实际终止日期
 */
@Component
public class ReportExecPlanFilter_BaseType03 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "03";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception{
        String sql = "select count(*) cnt from DWS_EVT_PRD_CALL_INF where PROD_ACTL_TMN_DT = '"+getPreDate(req)+"'";
        SqlRow row = comnDao.findRow(sql, req);
        int size = row.getInteger("cnt");
        if(size > 0){
            return getPreDate(req);
        }else{
            return null;
        }
    }
}
