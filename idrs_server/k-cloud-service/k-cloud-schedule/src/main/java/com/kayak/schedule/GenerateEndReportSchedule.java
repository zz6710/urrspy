package com.kayak.schedule;

import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成定期公告定时任务
 */
@Slf4j
@Component
public class GenerateEndReportSchedule extends MyJob {


    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        try {
            if (checkIp()) {
                Map<String, Object> param = new HashMap<>();
                super.execute(context);
                ServerUtil.requestPost("PmsApp", "com.kayak.pms.disclosureControl.model.ScheduleNotice", "autoGenerateEndNotice", param);
            }
        } catch (Exception e) {
            log.error("生成定期公告定时任务任务异常【{}】", e.toString());
        }
    }


}
