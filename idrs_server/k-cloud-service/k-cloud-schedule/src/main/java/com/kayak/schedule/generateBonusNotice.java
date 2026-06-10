package com.kayak.schedule;

import com.kayak.schedule.biz.MyJob;
import com.kayak.server.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class generateBonusNotice extends MyJob {
    /**
     * 功能：每天跑  生成分红提醒任务
     *
     * @throws Exception
     */
    @Override
    public void execute(JobExecutionContext context) {
        try {
            if (checkIp()) {
                Map<String, Object> param = new HashMap<>();
                super.execute(context);
                ServerUtil.requestPost("PmsApp", "com.kayak.pms.bonus.model.T8ProdBonusRule", "generateBonusNoticeTask", param);
            }
        } catch (Exception e) {
            log.error("每天定时跑，生成分红公告任务【{}】", e.toString());
        }
    }
}
