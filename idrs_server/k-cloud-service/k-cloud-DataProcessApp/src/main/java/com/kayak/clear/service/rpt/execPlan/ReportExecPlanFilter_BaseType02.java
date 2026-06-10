package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Component;

/**
 * base_type = 02 成立日期
 */
@Component
public class ReportExecPlanFilter_BaseType02 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "02";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception{
        String sql = "select count(*) cnt from dwd_prd_prd_bas_inf where FOUND_DT = '"+this.getPreDate(req)+"'";
        SqlRow row = comnDao.findRow(sql, req);
        int size = row.getInteger("cnt");
        if(size > 0){
            return this.getPreDate(req);
        }else{
            return null;
        }
    }
}
