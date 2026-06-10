package com.kayak.clear.service.rpt.execPlan;

import com.kayak.clear.req.ReportTimeExecPlanInput;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * base_type = 04 每周三
 */
@Component
public class ReportExecPlanFilter_BaseType04 extends AbstractReportExecPlanFilter {

    @Override
    public String baseType() {
        return "04";
    }

    /**
     * 获取基准日期
     * @param req 请求对象
     * @return
     */
    @Override
    public String getBaseLineDate(ReportTimeExecPlanInput req) throws Exception{
        String macDate = req.getMacDate();
        SimpleDateFormat predf = new SimpleDateFormat("yyyyMMdd");
        Date d = predf.parse(macDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if(day == 4){
            return req.getWorkDate();
        }else{
            return null;
        }
    }
}
