package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Component;

/**
 * base_type = 13 净值发布日
 */
@Component
public class ReportExecPlanFilter_BaseType13 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "13";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception{
        String sql = "select count(*) cnt from ods_prod_net_worth where deal_date  = '"+getPreDate(req)+"' ";
        SqlRow row = comnDao.findRow(sql, req);
        int size = row.getInteger("cnt");
        if(size > 0){
            return req.getWorkDate();
        }else{
            return null;
        }
    }
}
