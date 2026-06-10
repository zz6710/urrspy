package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Component;

/**
 * base_type = 11 申购赎回确认日
 */
@Component
public class ReportExecPlanFilter_BaseType11 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "11";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception{
        String sql = "select count(*) cnt from ods_prod_days where DEAL_DATE = '"+getPreDate(req)+"' and(IS_BUT_DT = '1' or IS_RDM_DT = '1')";
        SqlRow row = comnDao.findRow(sql, req);
        int size = row.getInteger("cnt");
        if(size > 0){
            return req.getWorkDate();
        }else{
            return null;
        }
    }
}
