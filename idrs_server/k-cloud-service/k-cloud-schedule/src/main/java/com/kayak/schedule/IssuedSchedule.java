package com.kayak.schedule;

import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 发行公告定时任务
 */
@Slf4j
@Component
public class IssuedSchedule extends MyJob {


    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        try {
            if(checkIp()) {
                Map<String, Object> param = new HashMap<>();
                super.execute(context);
                ServerUtil.requestPost("PmsApp", "com.kayak.pms.disclosureControl.model.DisclosureProdTask", "autoGenerateIssueTask", param);
            }
        } catch (Exception e) {
            log.error("发行公告定时任务任务异常【{}】",e.toString());
        }
    }


}
