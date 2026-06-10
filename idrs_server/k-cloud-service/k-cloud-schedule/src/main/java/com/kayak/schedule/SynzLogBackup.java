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
public class SynzLogBackup extends MyJob {
    /**
     * 功能：每月跑  日志备份并进行删除
     *
     * @throws Exception
     */
    @Override
    public void execute(JobExecutionContext context) {
        try {
            if (checkIp()) {
                Map<String, Object> param = new HashMap<>();
                super.execute(context);
                ServerUtil.requestPost("PmsApp", "com.kayak.pms.schedule.model.LogBackup", "LogBackup", param);
            }
        } catch (Exception e) {
            log.error("日志备份 【{}】", e.toString());
        }
    }

}
