package com.kayak.schedule.action;

import com.kayak.core.system.SysBeans;
import com.kayak.schedule.ExecuteTaskScheduled;
import com.kayak.schedule.NetValDaysSchedule;
import com.kayak.schedule.biz.MyJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Controller;

/**
 * com.kayak.schedule.action
 * user:rennannan
 * date:2021/5/31 19:59
 * function:
 */
@Controller
public class ExecNetValDaysAction extends MyJob {
    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        super.execute(context);
        try {
            NetValDaysSchedule date = SysBeans.getBean("netValDaysSchedule");
            date.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
